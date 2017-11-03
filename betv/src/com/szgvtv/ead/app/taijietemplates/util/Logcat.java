/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: Log.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 打印日志
 * @author: zhaoqy
 * @date: 2014-9-18 下午3:09:03
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.util.Log;

public class Logcat 
{
	private static boolean sVerbose = true; //冗长
	private static boolean sDebug = false;   //调试
	private static boolean sInfo = true;    //信息
	private static boolean sWarn = true;    //警告
	private static boolean sError = true;   //错误
	
	public static void v(String tag, String msg)
	{
		if (sVerbose)
		{
			Log.v(tag, msg);
		}
	}
	
	public static void d(String tag, String msg)
	{
		if (sDebug)
		{
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag, String msg)
	{
		if (sInfo)
		{
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg)
	{
		if (sWarn)
		{
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg)
	{
		if (sError)
		{
			Log.e(tag, msg);
		}
	}
}
