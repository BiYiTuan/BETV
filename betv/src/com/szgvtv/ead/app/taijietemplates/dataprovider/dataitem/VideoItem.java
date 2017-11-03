/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: VideoItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 点播资源详情信息item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:26:28
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable
{
	private String mVideoCode;		//点播资源ID
	private String mName;			//电影名称
	private String mArea;			//地区
	private String mType;			//类型(如：剧情、刑侦、武侠等)
	private String mDirector;		//导演
	private String mActor;			//主要演员
	private String mTime;			//上映时间
	private String mSummary;		//简介
	private String mSmallPic;		//小图地址
	private String mBigPic;			//大图地址
	private String mPosters;		//海报地址
	private String mScore;			//评分
	private String mHot;			//热度
	private String mTotalDramas;	//总集数
	private String mRatings;		//用户点评结果
	private String mVodtype;        //类型：1-电影；2-电视剧；3-综艺;
	private ArrayList<DramaItem> mDramaList = new ArrayList<DramaItem>();  //剧集列表
	private String mClassifyCode = "";	//分类编码(发送BI时用到的参数)
	private String mClassifyName = "";	//分类名称(发送BI时用到的参数)
	
	public VideoItem()
	{
	}
	
	public VideoItem(VideoItem item)
	{
		mVideoCode = item.mVideoCode;
		mName = item.mName;
		mArea = item.mArea;
		mType = item.mType;
		mDirector = item.mDirector;
		mActor = item.mActor;
		mTime = item.mTime;
		mSummary = item.mSummary;
		mSmallPic = item.mSmallPic;
		mBigPic = item.mBigPic;
		mPosters = item.mPosters;
		mScore = item.mScore;
		mHot = item.mHot;
		mTotalDramas = item.mTotalDramas;
		mRatings = item.mRatings;
		mVodtype = item.mVodtype;
		mDramaList = item.mDramaList;
		mClassifyCode = item.mClassifyCode;
		mClassifyName = item.mClassifyName;
	}
	
	public void setDramaList(ArrayList<DramaItem> dramaList)
	{
		mDramaList = dramaList;
	}
	
	public ArrayList<DramaItem> getDramaList() 
	{
		return mDramaList;
	}
	
	public void setVideoCode(String videoCode) 
	{
		mVideoCode = videoCode;
	}
	
	public String getVideoCode() 
	{
		return mVideoCode;
	}
	
	public void setName(String name) 
	{
		mName = name;
	}
	
	public String getName() 
	{
		return mName;
	}
	
	public void setArea(String area) 
	{
		mArea = area;
	}
	
	public String getArea() 
	{
		return mArea;
	}
	
	public void setType(String type) 
	{
		mType = type;
	}
	
	public String getType() 
	{
		return mType;
	}
	
	public void setDirector(String director) 
	{
		mDirector = director;
	}
	
	public String getDirector() 
	{
		return mDirector;
	}
	
	public void setActor(String actor) 
	{
		mActor = actor;
	}
	
	public String getActor()
	{
		return mActor;
	}
	
	public void setTime(String time) 
	{
		mTime = time;
	}
	
	public String getTime() 
	{
		return mTime;
	}
	
	public void setSummary(String summary) 
	{
		mSummary = summary;
	}
	
	public String getSummary() 
	{
		return mSummary;
	}
	
	public void setSmallPic(String smallPic) 
	{
		mSmallPic = smallPic;
	}
	
	public String getSmallPic() 
	{
		return mSmallPic;
	}

	public void setBigPic(String bigPic) 
	{
		mBigPic = bigPic;
	}

	public String getBigPic() 
	{
		return mBigPic;
	}

	public void setPosters(String posters) 
	{
		mPosters = posters;
	}
	
	public String getPosters() 
	{
		return mPosters;
	}

	public void setScore(String score) 
	{
		mScore = score;
	}
	
	public String getScore() 
	{
		return mScore;
	}

	public void setHot(String hot) 
	{
		mHot = hot;
	}

	public String getHot() 
	{
		return mHot;
	}

	public void setTotalDramas(String totalDramas) 
	{
		mTotalDramas = totalDramas;
	}

	public String getTotalDramas() 
	{
		return mTotalDramas;
	}

	public void setRatings(String ratings) 
	{
		mRatings = ratings;
	}

	public String getRatings() 
	{
		return mRatings;
	}
	
	public void setVodtype(String vodtype)
	{
		mVodtype = vodtype;
	}
	
	public String getVodtype()
	{
		return mVodtype;
	}
	
	public void setClassifyCode(String classCode) 
	{
		mClassifyCode = classCode;
	}

	public String getClassifyCode() 
	{
		return mClassifyCode;
	}
	
	public void setClassifyName(String className) 
	{
		mClassifyName = className;
	}

	public String getClassifyName() 
	{
		return mClassifyName;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(mVideoCode);
		out.writeString(mName);
		out.writeString(mArea);
		out.writeString(mType);
		out.writeString(mDirector);
		out.writeString(mActor);
		out.writeString(mTime);
		out.writeString(mSummary);
		out.writeString(mSmallPic);
		out.writeString(mBigPic);
		out.writeString(mPosters);
		out.writeString(mScore);
		out.writeString(mHot);
		out.writeString(mTotalDramas);
		out.writeString(mRatings);
		out.writeString(mVodtype);
		out.writeList(mDramaList);
		out.writeString(mClassifyCode);
		out.writeString(mClassifyName);
	}
	
	public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() 
	{
		public VideoItem createFromParcel(Parcel in) 
		{
			return new VideoItem(in);
		}

		public VideoItem[] newArray(int size) 
		{
			return new VideoItem[size];
		}
	};

	@SuppressWarnings("unchecked")
	private VideoItem(Parcel in) 
	{
		mVideoCode = in.readString();
		mName = in.readString();
		mArea = in.readString();
		mType = in.readString();
		mDirector = in.readString();
		mActor = in.readString();
		mTime = in.readString();
		mSummary = in.readString();
		mSmallPic = in.readString();
		mBigPic = in.readString();
		mPosters = in.readString();
		mScore = in.readString();
		mHot = in.readString();
		mTotalDramas = in.readString();
		mRatings = in.readString();
		mVodtype = in.readString();
		mDramaList = in.readArrayList(DramaItem.class.getClassLoader());
		mClassifyCode = in.readString();
		mClassifyName = in.readString();
	}
}
