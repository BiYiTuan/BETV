/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: NetUtil.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.http
 * @Description: 网络类型
 * @author: zhaoqy
 * @date: 2014-8-12 下午2:54:10
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetUtil 
{
	/**
	 * 
	 * @Title: isNetConnected
	 * @Description: 网络是否连接
	 * @param context
	 * @return
	 * @return: boolean
	 */
	public static boolean isNetConnected(Context context) 
	{
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		
		if (networkInfo != null)
		{
			return networkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * 获取网络类型
	 * @author zhaoqy 
	 * @param context Android上下文环境
	 * @return unknown 0, gprs 1, 3g 2, wifi 3
	 */
	public static int getNetType(Context context) 
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		
		if (activeNetInfo != null) 
		{
			int type = activeNetInfo.getType();
			
			if (type == ConnectivityManager.TYPE_WIFI)
			{
				return 3;
			}
			else if (type == ConnectivityManager.TYPE_MOBILE) 
			{
				type = activeNetInfo.getSubtype();
				
				if (type == TelephonyManager.NETWORK_TYPE_UMTS || type == TelephonyManager.NETWORK_TYPE_CDMA)
				{
					return 2;
				}
				else
				{
					return 1;
				}
			}
		}
		return 0;
	}
}
