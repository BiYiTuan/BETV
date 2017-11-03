/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: StartUpItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用启动项
 * @author: zhaoqy
 * @date: 2014-9-1 上午10:45:49
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class StartUpItem 
{
	private String mUrl;  //资源URL
	private String mType; //资源类型: 1-图片 2-视频
	
	public void setUrl(String url)
	{
		mUrl = url;
	}
	
	public String getUrl()
	{
		return mUrl;
	}
	
	public void setType(String type)
	{
		mType = type;
	}
	
	public String getType()
	{
		return mType;
	}
}
