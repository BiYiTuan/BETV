/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DictionaryItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用词典item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:24:19
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import java.util.ArrayList;

public class DictionaryItem 
{
	private int mStatus;  
	private ArrayList<DictionaryValueItem> mContentList = new ArrayList<DictionaryValueItem>();
	
	public void setStatus(int status)
	{
		mStatus = status;
	}
	
	public int getStatus()
	{
		return mStatus;
	}
	
	public ArrayList<DictionaryValueItem> getContentList() 
	{
		return mContentList;
	}

	public void setContentList(ArrayList<DictionaryValueItem> contentList) 
	{
		mContentList = contentList;
	}
}
