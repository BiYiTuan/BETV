/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: TimeUtil.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 时间Util
 * @author: zhaoqy
 * @date: 2014-9-17 上午10:34:00
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.TimeInfo;
import android.annotation.SuppressLint;
import android.util.Log;

public class TimeUtil 
{
	/**
	 * 解决android系统里设置为繁体时，获取不到具体时区格式的问题
	 * @author qc
	 * @param 
	 * @return 时区 GMT+8.00格式
	 */
	public static String getTimeZone()
	{
		StringBuffer buffer = new StringBuffer("GMT");
		
		Calendar cal = Calendar.getInstance(Locale.getDefault()); 
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET); 
		String zone=Integer.toString(zoneOffset/60/60/1000);
		Log.v("GMT timezone:", ""+zone);

		if (zone.charAt(0) == '-') 
		{
			buffer.append("%2d");
			for(int i=1; i<zone.length(); i++)
			{
				buffer.append(zone.charAt(i));
			}
		}
		else
		{
			buffer.append("%2b");
			for(int i=0; i<zone.length(); i++)
			{
				buffer.append(zone.charAt(i));
			}
		}
		//buffer.append(".00");
	    //buffer.append(".00");注释掉这一句，加上后面3句，就ok 了
		int min = (Math.abs(zoneOffset)/1000/60)%60;
		buffer.append(".");
		buffer.append(String.format("%02d",min));
			
		Logcat.d("zonebuf:", buffer.toString());
		Logcat.d(FlagConstant.TAG, " zone: " + buffer.toString());
		return buffer.toString();	
	}
	
	/**
	 * @Title: getTimeStamp
	 * @Description: 得到时间戳
	 * @param info 自定义时间格式
	 * @return
	 * @return: long
	 */
	public static long getTimeStamp(TimeInfo info)
	{
		Calendar time=Calendar.getInstance(); 
		time.clear(); 
		time.set(Calendar.YEAR, info.getYear()); 
		time.set(Calendar.MONTH, info.getMonth() - 1); //注意,Calendar对象默认一月为0 
		time.set(Calendar.DAY_OF_MONTH, info.getDay());
		time.set(Calendar.HOUR, info.getHour());
		time.set(Calendar.MINUTE, info.getMinute());
		time.set(Calendar.SECOND, info.getSecond()); 	
		return time.getTimeInMillis();
	}
	
	/**
	 * @Title: getTimeInfo
	 * @Description: 接口时间格式转为自定义时间格式
	 * @param info 接口时间区间
	 * @return
	 * @return: ArrayList<TimeInfo>
	 */
	@SuppressLint("UseValueOf")
	public static ArrayList<TimeInfo> getTimeInfo(LivePreviewItem info)
	{
		ArrayList<TimeInfo> timeList = new ArrayList<TimeInfo>();
		TimeInfo timeItem = null;
		
		try 
		{
			String dateArray[] = info.getDate().split("-");
	    	String zoneArray[] = info.getTime().split("-");
	    	
	    	for (int i = 0; i < zoneArray.length; i++) 
	    	{
	    		timeItem = new TimeInfo();
	    		String timeArray[] = zoneArray[i].split(":");
	    		timeItem.setHour(new Integer(timeArray[0]));
	    		timeItem.setMinute(new Integer(timeArray[1]));
	    		
	    		if(timeArray.length > 2)
	    		{
	    			timeItem.setSecond(new Integer(timeArray[2]));
	    		}	
	    		timeItem.setYear(new Integer(dateArray[0]));
	    		timeItem.setMonth(new Integer(dateArray[1]));
	    		timeItem.setDay(new Integer(dateArray[2]));
	    		timeList.add(timeItem);
			}
		}
		catch (Exception e) 
		{
		}
		return timeList;
	}
	
	/**
	 * @Title: getCurProgramIndex
	 * @Description: 得到当前播放节目索引
	 * @return 索引
	 * @return: int
	 */
	public static int getCurProgramIndex(ArrayList<LivePreviewItem> list)
	{
		long curTime = System.currentTimeMillis();
		long todateStartTime = 0,todateEndTime = 0;
		ArrayList<TimeInfo> timeList;
		int index = -1;
			
		for (int i=0; i<list.size(); i++) 
		{
			timeList = TimeUtil.getTimeInfo(list.get(i));
			todateStartTime = TimeUtil.getTimeStamp(timeList.get(0));
			todateEndTime = TimeUtil.getTimeStamp(timeList.get(1)) + 59*1000;
			
			if(curTime >= todateStartTime && curTime <= todateEndTime)
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * @Title: getUpcomingStart
	 * @Description: 得到第一条预告节目索引
	 * @return 索引
	 * @return: int
	 */
	public static int getUpcomingStart(ArrayList<LivePreviewItem> list,int curProgram)
	{
		long curTime = System.currentTimeMillis();
		long todateStartTime = 0;
		ArrayList<TimeInfo> timeList;
		int index = -1;
			
		for (int i=curProgram; i<list.size(); i++) 
		{
			timeList = TimeUtil.getTimeInfo(list.get(i));
			todateStartTime = TimeUtil.getTimeStamp(timeList.get(0));
			
			if(curTime < todateStartTime)
			{
				index = i;
				break;
			}
		}
		return index;
	}
}
