/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdviceOption.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 意见反馈题目option
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:22:51
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class AdviceOption 
{
	private String  mItemCode;		   //条目编码
	private String  mItemName;		   //条目名称
	private boolean mSelected = false; //该选项是否被选中
	
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
	
	public void setSelected(boolean selected)
	{
		mSelected = selected;
	}
	
	public boolean getSelected()
	{
		return mSelected;
	}
}
