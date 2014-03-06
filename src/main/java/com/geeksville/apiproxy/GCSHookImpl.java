package com.geeksville.apiproxy;

import java.io.IOException;
import java.net.UnknownHostException;

import com.geeksville.dapi.Webapi.Envelope;
import com.geeksville.dapi.Webapi.LoginMsg;
import com.geeksville.dapi.Webapi.MavlinkMsg;
import com.geeksville.dapi.Webapi.SetVehicleMsg;
import com.google.protobuf.ByteString;

/**
 * Create an instance of this class to be able to connect to the web API.
 * 
 * @author kevinh
 * 
 */
public class GCSHookImpl implements GCSHooks {

	TCPProtobufClient weblink;

	@Override
	public void setCallback(GCSCallback cb) {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterMavlink(int fromInterface, byte[] bytes)
			throws IOException {
		MavlinkMsg mav = MavlinkMsg.newBuilder().setSrcInterface(fromInterface)
				.addPacket(ByteString.copyFrom(bytes)).build();

		weblink.send(Envelope.newBuilder().setMavlink(mav).build());
	}

	@Override
	public void loginUser(String userName, String password)
			throws UnknownHostException, IOException {
		weblink = new TCPProtobufClient(APIConstants.DEFAULT_SERVER,
				APIConstants.DEFAULT_TCP_PORT);

		LoginMsg m = LoginMsg.newBuilder().setUsername(userName)
				.setPassword(password).build();
		Envelope msg = Envelope.newBuilder().setLogin(m).build();
		weblink.send(msg);
	}

	@Override
	public void setVehicleId(String vehicleId, int interfaceId, int mavlinkSysId)
			throws IOException {
		SetVehicleMsg mav = SetVehicleMsg.newBuilder()
				.setGcsInterface(interfaceId).setSysId(mavlinkSysId)
				.setVehicleId(vehicleId).build();

		weblink.send(Envelope.newBuilder().setSetVehicle(mav).build());
	}

	@Override
	public void flush() throws IOException {
		weblink.flush();
	}

}
