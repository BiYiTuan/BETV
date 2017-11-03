/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: StbInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 机顶盒信息
 * @author: zhaoqy
 * @date: 2014-8-11 下午9:08:02
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.setting.settingservice.SettingManager;

public class StbInfo 
{
	/**
	 * 
	 * @Title: getCPUID
	 * @Description: 获取cpuid
	 * @return
	 * @return: String
	 */
	@SuppressLint("DefaultLocale")
	public static String getCPUID(Context context) 
    {  
		String cpuid = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				cpuid = setting.getCPUID();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return cpuid;
    }
	
	/**
	 * 
	 * @Title: getLocalMacAddress
	 * @Description: 获取mac
	 * @return
	 * @return: String
	 */
	@SuppressLint("DefaultLocale")
	public static String getLocalMacAddress(Context context) 
	{  
		String mac = "";  
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				mac = setting.getMac();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
        return mac;  
    } 
	
	/**
	 * 
	 * @Title: getTimeZone
	 * @Description: 获取时区
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getTimeZone(Context context) 
	{
		String timezone = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				timezone = setting.getTimeZone();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return timezone;
	}
	
	/**
	 * 
	 * @Title: getHwVersion
	 * @Description: 获取硬件型号
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getHwVersion(Context context)
	{
		String hwversion = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				hwversion = setting.getHwVersion();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return hwversion;
	}
	
	/**
	 * 
	 * @Title: getVersion
	 * @Description: 获取固件版本
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getVersion(Context context)
	{
		String version = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				version = setting.getSfVersion();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return version;
	}
	
	/**
	 * 
	 * @Title: getPmVersion
	 * @Description: 获取产品型号
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getPmVersion(Context context)
	{
		String pmversion = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				pmversion = setting.getPver();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return pmversion;
	}
	
	/**
	 * 
	 * @Title: getCulLang
	 * @Description: 获取当前语言
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getCulLang(Context context)
	{
		String lang = "";
		if (context != null)
		{
			SettingManager setting; 
			try
			{
				setting = ((SettingManager)context.getSystemService(Context.SETTING_SERVICE));
				lang = setting.getCurLang();
			}
			catch (Error e)
			{
				e.printStackTrace();
			}
		}
		return lang;
	}
	
	/**
	 * 
	 * @Title: getPortLaug
	 * @Description: 获取接口语言(传给接口的参数)
	 * @return
	 * @return: String
	 */
	public static String getPortLaug() 
	{
		String language = "";
		language = "zh_CN";  //接口语言为中文简体
		return language;
	}
	
	/**
	 * 
	 * @Title: getShowLaug
	 * @Description: 获取显示语言(作为显示的语言 翻译 字典 BI)
	 * @return
	 * @return: String
	 */
	public static String getShowLaug() 
	{
		String language = "";
		language = "zh_CN";  //显示语言为中文简体
		return language;
	}
}
