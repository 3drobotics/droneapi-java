package com.geeksville.apiproxy;

import java.io.IOException;

public class TestClient {
	public static void main(String[] args) {
		GCSHooks webapi = new GCSHookImpl();

		System.out.println("Starting test client");
		try {
			webapi.loginUser("bob", "sekrit");
			webapi.flush();
			webapi.filterMavlink(0, new byte[] { 1, 2, 3, 4, 5 });
			webapi.flush();

			System.out.println("Test successful");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
