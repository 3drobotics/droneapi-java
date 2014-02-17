package com.geeksville.apiproxy;

import java.io.IOException;

public class TestClient {
	static void main(String args[]) {
		GCSHooks webapi = new GCSHookImpl();

		try {
			webapi.loginUser("bob", "sekrit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
