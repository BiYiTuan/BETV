/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdviceItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 意见反馈题目item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:22:21
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import java.util.ArrayList;

public class AdviceItem 
{
	private String mTopicCode;	//题目编码
	private String mTopicName;	//题目名称（多语言）
	private String mType;		//类型：1-单选；2-多选；3-简答
	private ArrayList<AdviceOption> mOptionList = new ArrayList<AdviceOption>();  //题目
	
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
	
	public void setOptionList(ArrayList<AdviceOption> optionList) 
	{
		mOptionList = optionList;
	}
	
	public ArrayList<AdviceOption> getOptionList() 
	{
		return mOptionList;
	}
}
