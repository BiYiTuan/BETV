/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: HotwordItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 热词item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:25:37
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class HotwordItem 
{
	private int    mId;	       //热词id
	private int    mFrequency; //出现频率
	private String mName;      //热词名称
	
	public void setId(int id) 
	{
		mId = id;
	}
	
	public int getId() 
	{
		return mId;
	}
	
	public int getFrequency() 
	{
		return mFrequency;
	}
	
	public void setFrequency(int frequency) 
	{
		mFrequency = frequency;
	}
	
	public void setName(String name)
	{
		mName = name;
	}
	
	public String getName()
	{
		return mName;
	}
}
