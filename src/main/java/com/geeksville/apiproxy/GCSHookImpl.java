package com.geeksville.apiproxy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

import com.geeksville.dapi.Webapi.Envelope;
import com.geeksville.dapi.Webapi.LoginMsg;
import com.geeksville.dapi.Webapi.LoginRequestCode;
import com.geeksville.dapi.Webapi.LoginResponseMsg;
import com.geeksville.dapi.Webapi.MavlinkMsg;
import com.geeksville.dapi.Webapi.SetVehicleMsg;
import com.geeksville.dapi.Webapi.StartMissionMsg;
import com.geeksville.dapi.Webapi.StopMissionMsg;
import com.google.protobuf.ByteString;

/**
 * Create an instance of this class to be able to connect to the web API.
 * 
 * @author kevinh
 * 
 */
public class GCSHookImpl implements GCSHooks {

	private IProtobufClient weblink;

	private boolean loggedIn = false;

	/**
	 * Time in usecs
	 */
	private long startTime;

	public void connect() throws UnknownHostException, IOException {
		// weblink = new TCPProtobufClient(APIConstants.DEFAULT_SERVER,
		// APIConstants.DEFAULT_TCP_PORT);
		weblink = new ZMQProtobufClient(APIConstants.ZMQ_URL);

		startTime = System.currentTimeMillis() * 1000;
	}

	@Override
	public void setCallback(GCSCallback cb) {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterMavlink(int fromInterface, byte[] bytes)
			throws IOException {
		long deltat = (System.currentTimeMillis() * 1000) - startTime;

		MavlinkMsg mav = MavlinkMsg.newBuilder().setSrcInterface(fromInterface)
				.setDeltaT(deltat).addPacket(ByteString.copyFrom(bytes))
				.build();

		sendNoBlock(Envelope.newBuilder().setMavlink(mav).build());
	}

	@Override
	public void loginUser(String userName, String password)
			throws UnknownHostException, IOException {

		LoginMsg m = LoginMsg.newBuilder().setUsername(userName)
				.setCode(LoginRequestCode.LOGIN).setPassword(password)
				.setStartTime(startTime).build();
		Envelope msg = Envelope.newBuilder().setLogin(m).build();
		sendUnchecked(msg);
		checkLoginOkay();
	}

	// / Ask server if the specified username is available for creation
	public boolean isUsernameAvailable(String userName)
			throws UnknownHostException, IOException {
		LoginMsg m = LoginMsg.newBuilder().setUsername(userName)
				.setCode(LoginRequestCode.CHECK_USERNAME).build();
		Envelope msg = Envelope.newBuilder().setLogin(m).build();
		sendUnchecked(msg);
		LoginResponseMsg r = readLoginResponse();

		return (r.getCode() == LoginResponseMsg.ResponseCode.OK);
	}

	// / Create a new user account
	@Override
	public void createUser(String userName, String password, String email)
			throws UnknownHostException, IOException {
		LoginMsg.Builder builder = LoginMsg.newBuilder().setUsername(userName)
				.setCode(LoginRequestCode.CREATE).setPassword(password)
				.setStartTime(startTime);

		if (email != null)
			builder.setEmail(email);

		Envelope msg = Envelope.newBuilder().setLogin(builder.build()).build();
		sendUnchecked(msg);
		checkLoginOkay();
	}

	private Envelope readEnvelope() throws IOException {
		return weblink.receive();
	}

	private LoginResponseMsg readLoginResponse() throws IOException {
		flush(); // Make sure any previous commands has been sent
		LoginResponseMsg r = readEnvelope().getLoginResponse();

		// No matter what, if the server is telling us to hang up, we must bail
		// immediately
		if (r.getCode() == LoginResponseMsg.ResponseCode.CALL_LATER)
			throw new CallbackLaterException(r.getMessage(),
					r.getCallbackDelay());

		return r;
	}

	private void checkLoginOkay() throws IOException {
		LoginResponseMsg r = readLoginResponse();
		if (r.getCode() != LoginResponseMsg.ResponseCode.OK)
			throw new LoginFailedException(r.getMessage());

		loggedIn = true;
	}

	@Override
	public void setVehicleId(String vehicleId, int interfaceId,
			int mavlinkSysId, boolean canAcceptCommands) throws IOException {
		SetVehicleMsg mav = SetVehicleMsg.newBuilder()
				.setGcsInterface(interfaceId).setSysId(mavlinkSysId)
				.setCanAcceptCommands(canAcceptCommands)
				.setVehicleUUID(vehicleId).build();

		send(Envelope.newBuilder().setSetVehicle(mav).build());
	}

	@Override
	public void flush() throws IOException {
		if (weblink != null)
			weblink.flush();
	}

	@Override
	public void close() throws IOException {
		if (weblink != null) {
			weblink.close();
			weblink = null;
		}
	}

	@Override
	public void send(Envelope e) throws IOException {
		if (loggedIn)
			sendUnchecked(e);
	}

	public void sendNoBlock(Envelope e) throws IOException {
		if (loggedIn && weblink != null)
			weblink.send(e, true);
	}

	/**
	 * Send without checking to see if we are logged in
	 * 
	 * @param e
	 * @throws IOException
	 */
	private void sendUnchecked(Envelope e) throws IOException {
		if (weblink != null)
			weblink.send(e, false);
	}

	@Override
	public void startMission(Boolean keep, UUID uuid) throws IOException {
		StartMissionMsg mav = StartMissionMsg.newBuilder().setKeep(keep)
				.setUuid(uuid.toString()).build();

		send(Envelope.newBuilder().setStartMission(mav).build());
	}

	@Override
	public void stopMission(Boolean keep) throws IOException {
		StopMissionMsg mav = StopMissionMsg.newBuilder().setKeep(keep).build();

		send(Envelope.newBuilder().setStopMission(mav).build());
	}

}
