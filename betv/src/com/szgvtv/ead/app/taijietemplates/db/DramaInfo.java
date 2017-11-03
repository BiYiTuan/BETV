/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DramaInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 数据库-剧集信息
 * @author: zhaoqy
 * @date: 2014-8-8 上午9:31:51
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;

public class DramaInfo extends DramaItem
{
	private String mVideoCode; //vod编码
	private int    mSort;      //0-收藏，1-播放历史
	
	public void setVideoCode(String videoCode) 
	{
		mVideoCode = videoCode;
	}
	
	public String getVideoCode() 
	{
		return mVideoCode;
	}
	
	public void setSort(int sort) 
	{
		mSort = sort;
	}
	
	public int getSort() 
	{
		return mSort;
	}
}
