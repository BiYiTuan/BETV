/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveChannelItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 直播频道item
 * @author: zhaoqy
 * @date: 2014-8-7 下午7:42:58
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveChannelItem implements Parcelable
{
	public String mTvId;   //直播频道ID	
	public String mResId;  //资源库ID
	public String mTvCode; //直播频道编码 
	public String mTvName; //直播频道名称
	public String mTvLogo; //直播频道logo
	public String mTvUrl;  //直播频道播放地址
	public int    mReplay; //是否有精彩回放: 0-无，1-有
	
	public LiveChannelItem() 
	{
	}

	public LiveChannelItem(LiveChannelItem item) 
	{
		mTvId = item.mTvId;		  	
		mResId = item.mResId;   
		mTvCode = item.mTvCode;    
		mTvName = item.mTvName;    
		mTvLogo = item.mTvLogo;    
		mTvUrl = item.mTvUrl;   
		mReplay = item.mReplay;
	}
	
	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(mTvId);
		out.writeString(mResId);
		out.writeString(mTvCode);
		out.writeString(mTvName);
		out.writeString(mTvLogo);
		out.writeString(mTvUrl);
		out.writeInt(mReplay);
	}

	public static final Parcelable.Creator<LiveChannelItem> CREATOR = new Parcelable.Creator<LiveChannelItem>() 
	{
		public LiveChannelItem createFromParcel(Parcel in) 
		{
			return new LiveChannelItem(in);
		}

		public LiveChannelItem[] newArray(int size) 
		{
			return new LiveChannelItem[size];
		}
	};

	private LiveChannelItem(Parcel in) 
	{
		mTvId = in.readString();
		mResId = in.readString();
		mTvCode =  in.readString();
		mTvName = in.readString();
		mTvLogo = in.readString();
		mTvUrl = in.readString();
		mReplay = in.readInt();
	}
	
	public void setTvId(String tvId) 
	{
		mTvId = tvId;
	}

	public String getTvId() 
	{
		return mTvId;
	}

	public void setResId(String resId) 
	{
		mResId = resId;
	}

	public String getResId() 
	{
		return mResId;
	}

	public void setTvCode(String tvCode) 
	{
		mTvCode = tvCode;
	}

	public String getTvCode() 
	{
		return mTvCode;
	}

	public void setTvName(String tvName) 
	{
		mTvName = tvName;
	}

	public String getTvName() 
	{
		return mTvName;
	}

	public void setTvLogo(String tvLogo) 
	{
		mTvLogo = tvLogo;
	}

	public String getTvLogo() 
	{
		return mTvLogo;
	}
	
	public void setTvUrl(String tvUrl) 
	{
		mTvUrl = tvUrl;
	}

	public String getTvUrl() 
	{
		return mTvUrl;
	}

	public void setIsReplay(int isReplay) 
	{
		mReplay = isReplay;
	}

	public int getIsReplay() 
	{
		return mReplay;
	}
}
