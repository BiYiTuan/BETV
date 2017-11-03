/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: UpgradeItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用升级item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:26:06
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class UpgradeItem 
{
	private String mVersion; //版本号
	private String mMD5;     //文件MD5校验值
	private String mUrl;     //该版本软件包下载地址
	private String mMessage; //升级提示信息（多语言）
	private int    mUpgrade; //是否需要升级: 0-不需要 1-需要
	private int    mForce;   //是否强制升级: 0-不强制 1-强制
	private int    mWay;     //升级方式：0-全量，1-增量
	
	public void setVersion(String version)
	{
		mVersion = version;
	}
	
	public String getVersion()
	{
		return mVersion;
	}
	
	public void setMD5(String md5)
	{
		mMD5 = md5;
	}
	
	public String getMD5()
	{
		return mMD5;
	}
	
	public void setUrl(String url)
	{
		mUrl = url;
	}
	
	public String getUrl()
	{
		return mUrl;
	}
	
	public void setMessage(String message)
	{
		mMessage = message;
	}
	
	public String getMessage()
	{
		return mMessage;
	}
	
	public void setIsupgrade(int isupgrade)
	{
		mUpgrade = isupgrade;
	}
	
	public int getIsupgrade()
	{
		return mUpgrade;
	}
	
	public void setIsforce(int isforce)
	{
		mForce = isforce;
	}
	
	public int getIsforce()
	{
		return mForce;
	}
	
	public void setUpgradeway(int upgrade)
	{
		mWay = upgrade;
	}
	
	public int getUpgradeway()
	{
		return mWay;
	}
}
