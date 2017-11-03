/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: BI.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.bi
 * @Description: ＢI类
 * @author: zhaoqy
 * @date: 2014-8-11 下午8:47:38
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.service.bi;

import android.content.Context;
import android.login.loginservice.LoginManager;
import android.text.TextUtils;
import com.bananatv.custom.newbi.NewBi;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StbInfo;

public class BI 
{
	private static String mServer = "";
	
	/**
	 * 
	 * @Title: startBi
	 * @Description: 开始BI, 设置服务 终端类型 mac 系统类型 固件版本 终端产品版本(默认值为1)
	 * @param context
	 * @return: void
	 */
	public static void startBi(Context context)
	{
		mServer = getBiServerUrl(context);
		if (!TextUtils.isEmpty(mServer)) //mServer为null或""时, 不发送BI
		{
			NewBi.startBi(context);
			NewBi.setBiServer(mServer);
			NewBi.setSysInfo(NewBi.BITT_ATB, StbInfo.getLocalMacAddress(context), "Android", StbInfo.getVersion(context), "1");
		}
	}
	
	/**
	 * 
	 * @Title: stopBi
	 * @Description: 关闭BI
	 * @return: void
	 */
	public static void stopBi()
	{
		if (!TextUtils.isEmpty(mServer)) //mServer为null或""时, 不发送BI
		{
			NewBi.stopBi();
		}
	}
	
	/**
	 * 
	 * @Title: getBiServer
	 * @Description: 获取BI服务地址
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getBiServerUrl(Context context)
	{
		String biServer = "";
		String version = "";
		
		if (context != null)
		{
			LoginManager login; 
			try
			{
				login = (LoginManager)context.getSystemService(Context.LOGIN_SERVICE); //tvpad4+
				biServer = login.getBIUrl();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		
		if (!TextUtils.isEmpty(biServer))
		{
			version = NewBi.getVersion();
		}
		
		Logcat.i(FlagConstant.TAG, " biServer: " + biServer + ",  version: " + version);
		return biServer;
	}
	
	/**
	 * 
	 * @Title: getStartTimeId
	 * @Description: 获取开始时间id
	 * @return
	 * @return: int
	 */
	public static int getStartTimeId()
	{
		int id = 0;
		if (!TextUtils.isEmpty(mServer))
		{
			id = NewBi.startTime();
		}
		return id;
	}
	
	/**
	 * 
	 * @Title: getConsumingTime
	 * @Description: 获取耗时
	 * @return
	 * @return: String
	 */
	public static String getConsumingTime(int id)
	{
		String time = "";
		if (!TextUtils.isEmpty(mServer))
		{
			time = NewBi.elapseTime(id);
		}
		return time;
	}
	
	/**
	 * 
	 * @Title: sendBiMsg
	 * @Description: 发送BI消息
	 * @param type
	 * @param code
	 * @param msg
	 * @return: void
	 */
	public static void sendBiMsg(int type, int code, String ...msg)
	{
		if (!TextUtils.isEmpty(mServer))
		{
			NewBi.sendBi(type, code, msg);
		}
	}
}
