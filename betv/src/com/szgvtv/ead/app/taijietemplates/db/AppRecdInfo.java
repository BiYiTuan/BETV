/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AppRecdInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 数据库-首页推荐信息
 * @author: zhaoqy
 * @date: 2014-9-15 下午5:50:32
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

public class AppRecdInfo 
{
	private String mName;  //推荐项的名称
	private String mCode;  //资源编码
	private String mType;  //类型：1-直播频道 2-点播 3-点播分类 4-专题 5-直播推荐 6-点播推荐
	private String mImage; //图片地址
	
	public void setName(String name)
	{
		mName = name;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public void setCode(String code)
	{
		mCode = code;
	}
	
	public String getCode()
	{
		return mCode;
	}
	
	public void setImage(String image)
	{
		mImage = image;
	}
	
	public String getImage()
	{
		return mImage;
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
