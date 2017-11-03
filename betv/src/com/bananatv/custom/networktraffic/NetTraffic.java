/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: NetTraffic.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.networktraffic
 * @Description: 监控网络速度
 * @author: zhaoqy
 * @date: 2014-8-11 下午8:39:04
 * @version: V1.0
 */

package com.bananatv.custom.networktraffic;

public class NetTraffic 
{
	static 
	{
		System.loadLibrary("networktraffic");
	}

	public static native void startMonitor();
	public static native void stopMonitor();
	public static native String getSpeed();

	/**
	 * 
	 * @Title: start
	 * @Description: 开始网络速度监控服务
	 * @return: void
	 */
	public void start() 
	{
		new SpeedThread().start();
	}

	/**
	 * 
	 * @Title: stop
	 * @Description: 停止网络速度监控服务
	 * @return: void
	 */
	public void stop() 
	{
		NetTraffic.stopMonitor();
	}

	public class SpeedThread extends Thread 
	{
		public void run() 
		{
			NetTraffic.startMonitor();
		}
	}
}
