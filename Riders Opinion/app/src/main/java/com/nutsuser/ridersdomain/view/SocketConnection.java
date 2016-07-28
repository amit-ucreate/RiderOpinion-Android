package com.nutsuser.ridersdomain.view;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public  class SocketConnection {

	public static final String TAG = SocketConnection.class.getSimpleName();
	public static String convertStreamToString(InputStream iS)
			throws IOException {
		String response = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					iS, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "");
			}
			response = sb.toString();
			Log.d(TAG + "::Json String Response", response);
		} catch (Exception e) {
			Log.e(TAG + "Buffer Error",
					"Error converting result " + e.toString());
		} finally {
			if (iS != null) {
				iS.close();
			}

		}
		return response;

	}

}
