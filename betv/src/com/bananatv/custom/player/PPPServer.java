/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PPPServer.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.player
 * @Description: ppp流媒体服务
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:02:43
 * @version: V1.0
 */

package com.bananatv.custom.player;

import com.bananatv.custom.player.PlayUriParser.uriCallBack;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.app.taijietemplates.util.StbInfo;
import android.content.Context;

public class PPPServer 
{
	/**
	 * 
	 * @Title: initPPPServer
	 * @Description: 初始化PPP服务
	 * @return: void
	 */
	public static void initPPPServer(Context context)  
	{ 
		//String serv_ip = "gyyyer.plbegy.com:3901|vistv.plbegy.com:3901|mhleve.oyhrtp.com:3901|plview.oyhrtp.com:3901|ngdvc.gvplayer.com:80";
		String serv_ip = "poxy.plbegy.com:3901|poxy.lighope.top:3901";
		String urs_name = StbInfo.getCPUID(context);   
		String pwd = StbInfo.getLocalMacAddress(context); 
		String key = "aewrasdeiotasd";
		String type = Constant.APPID;
		String version = PlayUriParser.getHttpUri().GetPlayerVersion();
		StaticVariable.gPlayerRet = PlayUriParser.getHttpUri().PPPServiceInit(serv_ip, urs_name, pwd, key, type);
		
		Logcat.i(FlagConstant.TAG, " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Logcat.i(FlagConstant.TAG, " cpuid: " + urs_name + ",  mac: " + pwd + ",  version: " + version);
		Logcat.i(FlagConstant.TAG, " gPlayerRet: " + StaticVariable.gPlayerRet);
		Logcat.i(FlagConstant.TAG, " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		initForceTV(context);
		StaticVariable.gInitPler = true;
	}
	
	/**
	 * 
	 * @Title: stopService
	 * @Description: 关闭服务
	 * @return: void
	 */
	public static void stopService()
	{
		PlayUriParser.getHttpUri().stopService();
	}
	
	/**
	 * 
	 * @Title: stopPlay
	 * @Description: 关闭播放
	 * @return: void
	 */
	public static int stopPlay()
	{
		return PlayUriParser.getHttpUri().stopPlay();
	}
	
	/**
	 * 
	 * @Title: startToHttpUri
	 * @Description: 流媒体地址转http地址
	 * @return: void
	 */
	public static void startToHttpUri()
	{
		PlayUriParser.getHttpUri().startToHttpUri();
	}
	
	/**
	 * 
	 * @Title: setPlayUri
	 * @Description: 设置流媒体的播放地址
	 * @param url
	 * @return: void
	 */
	public static void setPlayUri(String url)
	{
		PlayUriParser.getHttpUri().setPlayUri(url);
	}
	
	/**
	 * 
	 * @Title: setCallBack
	 * @Description: 设置回调函数
	 * @param activity
	 * @return: void
	 */
	public static void setCallBack(uriCallBack activity)
	{
		PlayUriParser.getHttpUri().setCallBack(activity);
	}
	
	/**
	 * 
	 * @Title: initForceTV
	 * @Description: forcetv 点播播放初始化函数,限普通apk调用，系统apk不用调用
	 * @param context
	 * @return: void
	 */
	public static void initForceTV(Context context)
	{
		PlayUriParser.getHttpUri().ForceTvInit(context);
	}
	
	/**
	 * 
	 * @Title: getBufferStatus
	 * @Description: 获取缓存百分比，建议1s获取一次
	 * @return
	 * @return: int
	 */
	public static int getBufferStatus()
	{
		return PlayUriParser.getHttpUri().PPPGetBufferStatus();
	}
}
