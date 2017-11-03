/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: TimeInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 时间类
 * @author: zhaoqy
 * @date: 2014-9-22 上午11:30:00
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class TimeInfo 
{
	private int mYear;   //年
	private int mMonth;  //月
	private int mDay;	 //日
	private int mHour;   //时		  
	private int mMinute; //分
	private int mSecond; //秒
	
	public void setYear(int year) 
	{
		mYear = year;
	}
	
	public int getYear() 
	{
		return mYear;
	}
	
	public void setMonth(int month) 
	{
		mMonth = month;
	}
	
	public int getMonth() 
	{
		return mMonth;
	}
	
	public void setDay(int day) 
	{
		mDay = day;
	}
	
	public int getDay() 
	{
		return mDay;
	}
	
	public void setHour(int hour) 
	{
		mHour = hour;
	}
	
	public int getHour() 
	{
		return mHour;
	}
	
	public void setMinute(int minute) 
	{
		mMinute = minute;
	}
	
	public int getMinute() 
	{
		return mMinute;
	}
	
	public void setSecond(int second) 
	{
		mSecond = second;
	}	
	
	public int getSecond() 
	{
		return mSecond;
	}
}
