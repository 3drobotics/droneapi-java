package com.geeksville.apiproxy;

import java.io.IOException;

public class TestClient {
	public static void main(String[] args) {
		GCSHooks webapi = new GCSHookImpl();

		System.out.println("Starting test client");
		try {
			webapi.loginUser("bob", "sekrit");
			webapi.flush();

			int interfaceNum = 0;
			int sysId = 1;
			webapi.setVehicleId("blahblahvehicle", interfaceNum, sysId);

			byte[] payload = new byte[] { (byte) 0xfe, (byte) 0x0e,
					(byte) 0x9d, (byte) 0x01, (byte) 0x01, (byte) 0x1d,
					(byte) 0xf9, (byte) 0x46, (byte) 0x01, (byte) 0x00,
					(byte) 0x33, (byte) 0x03, (byte) 0x7c, (byte) 0x44,
					(byte) 0xec, (byte) 0x51, (byte) 0x1e, (byte) 0xbe,
					(byte) 0x27, (byte) 0x01, (byte) 0xca, (byte) 0x8f };
			webapi.filterMavlink(interfaceNum, payload);
			webapi.flush();

			System.out.println("Test successful");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
