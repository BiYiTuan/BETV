/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: SpecialItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 专题item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:10:55
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import android.os.Parcel;
import android.os.Parcelable;

public class SpecialItem implements Parcelable
{
	private String mAlbumId;   //资源ID
	private String mAlbumCode; //专题编码
	private String mName;      //资源名称
	private String mImageUrl;  //资源海报
	private String mSummary;   //介绍
	
	public SpecialItem()
	{
	}
	
	public SpecialItem(SpecialItem item)
	{
		mAlbumId = item.mAlbumId;
		mAlbumCode = item.mAlbumCode;
		mName = item.mName;
		mImageUrl = item.mImageUrl;
		mSummary = item.mSummary;
	}
	
	@Override
	public int describeContents() 
	{
		return 0;
	}
	
	public static final Parcelable.Creator<SpecialItem> CREATOR = new Parcelable.Creator<SpecialItem>() 
	{
		public SpecialItem createFromParcel(Parcel in) 
		{
			return new SpecialItem(in);
		}

		public SpecialItem[] newArray(int size) 
		{
			return new SpecialItem[size];
		}
	};

	private SpecialItem(Parcel in) 
	{
		mAlbumId = in.readString();
		mAlbumCode = in.readString();
		mName = in.readString();
		mImageUrl = in.readString();
		mSummary = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel out, int arg1) 
	{
		out.writeString(mAlbumId);
		out.writeString(mAlbumCode);
		out.writeString(mName);
		out.writeString(mImageUrl);
		out.writeString(mSummary);
	}
	
	public void setAlbumId(String albumId) 
	{
		mAlbumId = albumId;
	}
	
	public String getAlbumId() 
	{
		return mAlbumId;
	}
	
	public void setAlbumCode(String albumCode) 
	{
		mAlbumCode = albumCode;
	}
	
	public String getAlbumCode() 
	{
		return mAlbumCode;
	}
	
	public void setName(String name) 
	{
		mName = name;
	}
	
	public String getName() 
	{
		return mName;
	}
	
	public void setImageUrl(String imageUrl) 
	{
		mImageUrl = imageUrl;
	}
	
	public String getImageUrl() 
	{
		return mImageUrl;
	}
	
	public void setSummary(String summary) 
	{
		mSummary = summary;
	}
	
	public String getSummary() 
	{
		return mSummary;
	}
}
