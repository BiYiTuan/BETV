/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DictionaryValueItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用词典状态值item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:24:38
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class DictionaryValueItem 
{
	private String mLanguage; //语言
	private String mContent;  //内容
	
	public void setLanguage(String language)
	{
		mLanguage = language;
	}
	
	public String getLanguage()
	{
		return mLanguage;
	}
	
	public void setContent(String content)
	{
		mContent = content;
	}
	
	public String getContent()
	{
		return mContent;
	}
}
