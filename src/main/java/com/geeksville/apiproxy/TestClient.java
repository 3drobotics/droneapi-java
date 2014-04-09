package com.geeksville.apiproxy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * A quick test to be easy to embed into Droidplanner.
 * 
 * @author kevinh
 * 
 */
public class TestClient extends GCSHookImpl {

	public int interfaceNum = 0;

	public void connect() throws UnknownHostException, IOException {
		loginUser("test-bob@3drobotics.com", "sekrit");
		flush();

		int sysId = 1;
		setVehicleId("550e8400-e29b-41d4-a716-446655440000", interfaceNum,
				sysId, false);

		startMission(false, UUID.randomUUID());
	}

	@Override
	public void close() throws IOException {
		stopMission(true);

		flush();
		super.close();

		System.out.println("Test successful");
	}

	/**
	 * Do one full connection/upload session
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void runTest() throws UnknownHostException, IOException {
		TestClient webapi = new TestClient();
		byte[] payload = new byte[] { (byte) 0xfe, (byte) 0x0e, (byte) 0x9d,
				(byte) 0x01, (byte) 0x01, (byte) 0x1d, (byte) 0xf9,
				(byte) 0x46, (byte) 0x01, (byte) 0x00, (byte) 0x33,
				(byte) 0x03, (byte) 0x7c, (byte) 0x44, (byte) 0xec,
				(byte) 0x51, (byte) 0x1e, (byte) 0xbe, (byte) 0x27,
				(byte) 0x01, (byte) 0xca, (byte) 0x8f };
		webapi.filterMavlink(webapi.interfaceNum, payload);
		webapi.close();
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
