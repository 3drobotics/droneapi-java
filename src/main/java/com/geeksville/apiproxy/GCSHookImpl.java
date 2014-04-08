package com.geeksville.apiproxy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

import com.geeksville.dapi.Webapi.Envelope;
import com.geeksville.dapi.Webapi.LoginMsg;
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

	private TCPProtobufClient weblink;

	/**
	 * Time in usecs
	 */
	private long startTime;

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

		send(Envelope.newBuilder().setMavlink(mav).build());
	}

	@Override
	public void loginUser(String userName, String password)
			throws UnknownHostException, IOException {
		weblink = new TCPProtobufClient(APIConstants.DEFAULT_SERVER,
				APIConstants.DEFAULT_TCP_PORT);

		startTime = System.currentTimeMillis() * 1000;

		LoginMsg m = LoginMsg.newBuilder().setUsername(userName)
				.setPassword(password).setStartTime(startTime).build();
		Envelope msg = Envelope.newBuilder().setLogin(m).build();
		send(msg);
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
		weblink.send(e);
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
