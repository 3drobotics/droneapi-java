package com.geeksville.apiproxy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

public class TestClient {
	/**
	 * Do one full connection/upload session
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void runTest() throws UnknownHostException, IOException {
		GCSHooks webapi = new GCSHookImpl();
		webapi.loginUser("test-bob@3drobotics.com", "sekrit");
		webapi.flush();

		int interfaceNum = 0;
		int sysId = 1;
		webapi.setVehicleId("550e8400-e29b-41d4-a716-446655440000",
				interfaceNum, sysId, false);

		webapi.startMission(false, UUID.randomUUID());

		byte[] payload = new byte[] { (byte) 0xfe, (byte) 0x0e, (byte) 0x9d,
				(byte) 0x01, (byte) 0x01, (byte) 0x1d, (byte) 0xf9,
				(byte) 0x46, (byte) 0x01, (byte) 0x00, (byte) 0x33,
				(byte) 0x03, (byte) 0x7c, (byte) 0x44, (byte) 0xec,
				(byte) 0x51, (byte) 0x1e, (byte) 0xbe, (byte) 0x27,
				(byte) 0x01, (byte) 0xca, (byte) 0x8f };
		webapi.filterMavlink(interfaceNum, payload);

		webapi.stopMission(true);

		webapi.flush();
		webapi.close();

		System.out.println("Test successful");
	}

	public static void main(String[] args) {
		System.out.println("Starting test client");
		try {
			runTest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
