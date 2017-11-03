/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AppInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 应用信息
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:22:26
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfo 
{
	/**
	 * 
	 * @Title: getVersionName
	 * @Description: 获取版本名称(用于升级)
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getVersionName(Context context)
	{
	    String versionName = "";  //当前应用的版本名称  
	   
	    try
	    {
	    	PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	    	versionName = info.versionName;
	    } 
	    catch (NameNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    return versionName;
	}
	
	/**
	 * 
	 * @Title: getPackageName
	 * @Description: 获取应用包名
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getPackageName(Context context)
	{
		String packageNames = "";  //当前版本的包名
		 
		try
		{
		    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
		    packageNames = info.packageName;  
		} 
		catch (NameNotFoundException e)
		{
		    e.printStackTrace();
		}
		return packageNames;
	}
	
	/**
	 * 
	 * @Title: getVersionCode
	 * @Description: 获取版本号
	 * @param context
	 * @return
	 * @return: int
	 */
	public static int getVersionCode(Context context)
	{
	    int versionCode = 0;  //当前版本的版本号  
	    
	    try
	    {
	    	PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	        versionCode = info.versionCode;  
	    } 
	    catch (NameNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    return versionCode;
	}
	
	/**
	 * 
	 * @Title: getApplicationName
	 * @Description: 获取应用名
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getApplicationName(Context context) 
	{
		String applicationName = "";  //应用名称
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		
		try 
		{
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} 
		catch (PackageManager.NameNotFoundException e) 
		{
			applicationInfo = null;
		}
		
		applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	} 
}
