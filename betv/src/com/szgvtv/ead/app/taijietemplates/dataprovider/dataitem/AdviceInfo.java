/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdviceInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 意见反馈(每一条建议)
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:13:27
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class AdviceInfo 
{
	private String mTopicCode;	//题目编码
	private String mTopicName;	//题目名称（多语言）
	private String mType;		//类型：1-单选；2-多选；3-简答
	private String mItemCode;	//条目编码
	private String mItemName;	//条目名称
	
	public void setTopicCode(String topicCode) 
	{
		mTopicCode = topicCode;
	}
	
	public String getTopicCode() 
	{
		return mTopicCode;
	}
	
	public void setTopicName(String topicName) 
	{
		mTopicName = topicName;
	}
	
	public String getTopicName() 
	{
		return mTopicName;
	}
	
	public void setType(String type) 
	{
		mType = type;
	}
	
	public String getType() 
	{
		return mType;
	}
	
	public void setItemCode(String itemCode) 
	{
		mItemCode = itemCode;
	}
	
	public String getItemCode() 
	{
		return mItemCode;
	}
	
	public void setItemName(String itemName) 
	{
		mItemName = itemName;
	}
	
	public String getItemName() 
	{
		return mItemName;
	}
}
