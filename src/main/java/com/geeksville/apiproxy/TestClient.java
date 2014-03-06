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
			webapi.filterMavlink(interfaceNum, new byte[] { 1, 2, 3, 4, 5 });
			webapi.flush();

			System.out.println("Test successful");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
