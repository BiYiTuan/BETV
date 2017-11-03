/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: HomeRecommendItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用首页推荐
 * @author: zhaoqy
 * @date: 2014-9-1 上午11:07:30
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;

public class HomeRecommendItem 
{
	private String mName;  //推荐项的名称
	private String mCode;  //资源编码
	private String mImage; //图片地址
	private String mType;  //类型：1-直播频道 2-点播 3-点播分类 4-专题 5-直播推荐 6-点播推荐
	
	public void setName(String name)
	{
		mName = name;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public void setCode(String code)
	{
		mCode = code;
	}
	
	public String getCode()
	{
		return mCode;
	}
	
	public void setImage(String image)
	{
		Logcat.d(FlagConstant.TAG, " mImage: " + image);
		mImage = image;
	}
	
	public String getImage()
	{
		return mImage;
	}
	
	public void setType(String type)
	{
		mType = type;
	}
	
	public String getType()
	{
		return mType;
	}
}
