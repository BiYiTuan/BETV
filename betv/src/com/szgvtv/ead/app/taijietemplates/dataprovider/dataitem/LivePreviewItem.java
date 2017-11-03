/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LivePreviewItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 直播节目预告item
 * @author: zhaoqy
 * @date: 2014-8-7 下午7:38:53
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import android.os.Parcel;
import android.os.Parcelable;

public class LivePreviewItem implements Parcelable
{
	private String mName; //节目预告名称
	private String mDate; //节目预告日期(YYYY-MM-DD)
	private String mTime; //节目预告具体时间(HH:mm)
	
	public LivePreviewItem() 
	{
	}

	public LivePreviewItem(LivePreviewItem item) 
	{
		mName = item.mName;		  	
		mDate = item.mDate;   
		mTime = item.mTime; 
	}
	
	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(mName);
		out.writeString(mDate);
		out.writeString(mTime);
	}

	public static final Parcelable.Creator<LivePreviewItem> CREATOR = new Parcelable.Creator<LivePreviewItem>() 
	{
		public LivePreviewItem createFromParcel(Parcel in) 
		{
			return new LivePreviewItem(in);
		}

		public LivePreviewItem[] newArray(int size) 
		{
			return new LivePreviewItem[size];
		}
	};

	private LivePreviewItem(Parcel in) 
	{
		mName = in.readString();
		mDate = in.readString();
		mTime = in.readString();
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
