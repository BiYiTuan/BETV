/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: Ad.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ad
 * @Description: 广告服务
 * @author: zhaoqy
 * @date: 2014-9-29 上午10:10:54
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.service.ad;

import android.content.Context;
import android.login.loginservice.LoginManager;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StbInfo;
import com.szgvtv.ead.framework.advertisement.AdManager;

public class Ad 
{
	public static AdManager mAdManager; //广告管理
	
	/**
	 * 
	 * @Title: startAd
	 * @Description: 开始广告服务
	 * @param context
	 * @return: void
	 */
	public static void startAd(Context context)
	{
		if (mAdManager == null)
		{
			mAdManager = AdManager.getAdManager(context);
			mAdManager.setServer(getAdServerUrl(context));
			mAdManager.setSysInfo(StbInfo.getTimeZone(context), 
		               StbInfo.getLocalMacAddress(context), 
		               StbInfo.getCPUID(context), 
		               StbInfo.getVersion(context), 
		               StbInfo.getHwVersion(context), "1");
		}
	}
	
	/**
	 * 
	 * @Title: stopAd
	 * @Description: 关闭广告服务
	 * @return: void
	 */
	public static void stopAd()
	{
		if (mAdManager != null)
		{
			mAdManager.quitManager();
			mAdManager = null;
		}
	}
	
	/**
	 * 
	 * @Title: getAdServerUrl
	 * @Description: 获取广告服务地址
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getAdServerUrl(Context context)
	{
		String adServer = "";
		String version = "";
		
		if (context != null)
		{
			LoginManager login; 
			try
			{
				login = (LoginManager)context.getSystemService(Context.LOGIN_SERVICE);  //tvpad4+
				adServer = login.getServerUrl("AD");
				Logcat.i(FlagConstant.TAG, " address_adServer: " + adServer);  //寻址广告服务地址
			}
			catch (Error e)
			{
				e.printStackTrace();
				Logcat.e(FlagConstant.TAG, e.toString());
			}
		}
		
		if (adServer.isEmpty())
		{
			adServer = "http://www.advertepg.com:5858";
		}
		
		if (mAdManager != null)
		{
			version = AdManager.getVersion();
		}
		
		Logcat.i(FlagConstant.TAG, " adServer: " + adServer + ",  version: " + version);
		return adServer;
	}
}
