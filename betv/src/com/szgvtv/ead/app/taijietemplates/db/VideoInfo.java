/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: VideoInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: video信息存储(收藏和播放历史)
 * @author: zhaoqy
 * @date: 2014-8-8 上午9:35:46
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;

public class VideoInfo extends VideoItem
{
	private String mDramaCode;  //剧集编码
	private int    mSort;       //0-收藏，1-播放历史
	private int    mPlayTime;   //播放时间
	
	public VideoInfo() 
	{
		super();
	}
	
	public VideoInfo(VideoItem item, int sort) 
	{
		super(item);
		mSort = sort;
	}
	
	public void setDramaCode(String dramaCode) 
	{
		mDramaCode = dramaCode;
	}
	
	public String getDramaCode() 
	{
		return mDramaCode;
	}
	
	public void setSort(int sort) 
	{
		mSort = sort;
	}

	public int getSort() 
	{
		return mSort;
	}
	
	public void setPlayTime(int playTime) 
	{
		mPlayTime = playTime;
	}
	
	public int getPlayTime() 
	{
		return mPlayTime;
	}
}
