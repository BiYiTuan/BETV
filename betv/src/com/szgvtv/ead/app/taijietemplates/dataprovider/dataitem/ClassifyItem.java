/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: VideoClassifyItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 点播分类item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:12:53
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class ClassifyItem 
{
	private String mCode;  //分类编码
	private String mName;  //分类名称
	private String mGroup; //0-无子分类，1-有子分类
	private String mIcon;  //分类图片地址
	
	public void setClassifyCode(String classifyCode) 
	{
		mCode = classifyCode;
	}
	
	public String getClassifyCode() 
	{
		return mCode;
	}
	
	public void setClassifyName(String classifyName) 
	{
		mName = classifyName;
	}
	
	public String getClassifyName() 
	{
		return mName;
	}
	
	public void setIsgroup(String isgroup) 
	{
		mGroup = isgroup;
	}
	
	public String getIsgroup() 
	{
		return mGroup;
	}
	
	public void setIcon(String icon) 
	{
		mIcon = icon;
	}
	
	public String getIcon() 
	{
		return mIcon;
	}
}
