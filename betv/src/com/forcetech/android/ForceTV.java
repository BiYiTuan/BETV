package com.forcetech.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.util.Log;

public class ForceTV {
	private boolean p2pIsStart = false;
	private String path = "/sdcard";
	private byte[] pathbyte = new byte[path.length()+1];

	public void initForceClient() {
		Log.v("Player", "initForceClient!");
		try {
			Process process = Runtime.getRuntime().exec("netstat");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()), 1024);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("0.0.0.0:9906"))
					p2pIsStart = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.arraycopy(path.getBytes(),0,pathbyte,0,path.length());
		if (!p2pIsStart)Log.d("Player", "initForceClient ret=:" + String.valueOf(start(9906, 20 * 1024 * 1024)));
	}

	public native int start(int port, int size);
	public native int startWithLog(int port, int size, byte[] path);
	public native int stop();
	static {
		System.loadLibrary("forcetv");
	}
}
