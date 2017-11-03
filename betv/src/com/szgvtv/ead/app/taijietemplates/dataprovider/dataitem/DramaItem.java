/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DramaItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 剧集信息item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:25:06
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import android.os.Parcel;
import android.os.Parcelable;

public class DramaItem implements Parcelable
{
	private String mNumber;	  //当前第几集
	private String mSize;        //大小
	private String mTime;	      //时长
	private String mDramaName;   //剧集名
	private String mDramaCode;   //集数编码
	private String mRate;        //码率
	private String mFormat;      //格式
	private String mUrl;         //播放地址
	private String mScreenshots; //剧集截图
	
    public DramaItem()
    {
	}
	
	public DramaItem(DramaItem item)
	{
		mNumber = item.mNumber;
		mSize = item.mSize;
		mTime = item.mTime;
		mDramaName = item.mDramaName;
		mDramaCode = item.mDramaCode;
		mRate = item.mRate;
		mFormat = item.mFormat;
		mUrl = item.mUrl;
		mScreenshots = item.mScreenshots;
	}
	
	@Override
	public int describeContents() 
	{
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(mNumber);
		out.writeString(mSize);
		out.writeString(mTime);
        out.writeString(mDramaName);
        out.writeString(mDramaCode);
        out.writeString(mRate);
        out.writeString(mFormat);
        out.writeString(mUrl);
        out.writeString(mScreenshots);
	}
	
	public static final Parcelable.Creator<DramaItem> CREATOR = new Parcelable.Creator<DramaItem>() 
	{
		public DramaItem createFromParcel(Parcel in) 
		{
			return new DramaItem(in);
		}

		public DramaItem[] newArray(int size) 
		{
			return new DramaItem[size];
		}
	};

	private DramaItem(Parcel in) 
	{
		mNumber = in.readString();
		mSize = in.readString();
		mTime = in.readString();
		mDramaName = in.readString();
		mDramaCode = in.readString();
		mRate = in.readString();
		mFormat = in.readString();
		mUrl = in.readString();
		mScreenshots = in.readString();
	}
	
	public void setNumber(String number) 
	{
		mNumber = number;
	}
	
	public String getNumber() 
	{
		return mNumber;
	}
	
	public void setSize(String size) 
	{
		mSize = size;
	}
	
	public String getSize() 
	{
		return mSize;
	}
	
	public void setDramaTime(String time) 
	{
		mTime = time;
	}
	
	public String getDramaTime() 
	{
		return mTime;
	}
	
	public void setDramaName(String dramaName) 
	{
		mDramaName = dramaName;
	}
	
	public String getDramaName() 
	{
		return mDramaName;
	}
	
	public void setDramaCode(String dramaCode) 
	{
		mDramaCode = dramaCode;
	}
	
	public String getDramaCode() 
	{
		return mDramaCode;
	}
	
	public void setRate(String rate) 
	{
		mRate = rate;
	}
	
	public String getRate() 
	{
		return mRate;
	}
	
	public void setFormat(String format) 
	{
		mFormat = format;
	}
	
	public String getFormat() 
	{
		return mFormat;
	}
	
	public void setUrl(String url) 
	{
		mUrl = url;
	}
	
	public String getUrl() 
	{
		return mUrl;
	}
	
	public void setScreenshots(String screenshots) 
	{
		mScreenshots = screenshots;
	}
	
	public String getScreenshots() 
	{
		return mScreenshots;
	}
}
