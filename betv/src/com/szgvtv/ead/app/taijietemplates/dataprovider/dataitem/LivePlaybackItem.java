/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LivePlaybackItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 直播精彩回放item
 * @author: zhaoqy
 * @date: 2014-8-7 下午7:47:13
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import android.os.Parcel;
import android.os.Parcelable;

public class LivePlaybackItem implements Parcelable
{
	private String mCode; //精彩回放编码
	private String mName; //精彩回放名称
	private String mDate; //精彩回放日期(YYYY-MM-DD)
	private String mTime; //精彩回放具体时间(HH:mm)
	private String mUrl;  //精彩回放播放地址
	
	public LivePlaybackItem() 
	{
	}

	public LivePlaybackItem(LivePlaybackItem item) 
	{
		mCode = item.mCode;		  	
		mName = item.mName;   
		mDate = item.mDate;    
		mTime = item.mTime;    
		mUrl = item.mUrl;
	}
	
	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(mCode);
		out.writeString(mName);
		out.writeString(mDate);
		out.writeString(mTime);
		out.writeString(mUrl);
	}

	public static final Parcelable.Creator<LivePlaybackItem> CREATOR = new Parcelable.Creator<LivePlaybackItem>() 
	{
		public LivePlaybackItem createFromParcel(Parcel in) 
		{
			return new LivePlaybackItem(in);
		}

		public LivePlaybackItem[] newArray(int size) 
		{
			return new LivePlaybackItem[size];
		}
	};

	private LivePlaybackItem(Parcel in) 
	{
		mCode = in.readString();
		mName = in.readString();
		mDate =  in.readString();
		mTime = in.readString();
		mUrl = in.readString();
	}
	
	public void setReplaycode(String replaycode) 
	{
		mCode = replaycode;
	}
	
	public String getReplaycode() 
	{
		return mCode;
	}
	
	public void setUrl(String url) 
	{
		mUrl = url;
	}
	
	public String getUrl() 
	{
		return mUrl;
	}
	
	public void setName(String name) 
	{
		mName = name;
	}
	
	public String getName() 
	{
		return mName;
	}
	
	public void setDate(String date) 
	{
		mDate = date;
	}
	
	public String getDate() 
	{
		return mDate;
	}
	
	public void setTime(String time) 
	{
		mTime = time;
	}
	
	public String getTime() 
	{
		return mTime;
	}
}
