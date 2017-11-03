/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveGuideInfo.java
 * @Prject: TvbTemps
 * @Package: com.szgvtv.ead.app.tvbtemps.db
 * @Description: 直播频道预告信息存储
 * @author: zhaoqy
 * @date: 2014-12-16 下午3:28:39
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;

public class LivepreviewInfo extends LivePreviewItem
{
	public String mTvCode; //直播频道编码 
	
	public void setTvCode(String tvCode) 
	{
		mTvCode = tvCode;
	}

	public String getTvCode() 
	{
		return mTvCode;
	}
}
