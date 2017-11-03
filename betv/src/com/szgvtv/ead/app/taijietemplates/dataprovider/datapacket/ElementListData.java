/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ElementListData.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket
 * @Description: 下行客户端数据
 * @author: zhaoqy
 * @date: 2014-8-12 下午2:49:44
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;

public class ElementListData 
{
	private int mToken;                                            //列表标识, 如: APP_AUTH
	private int mSize;                                             //指定数量，每次最多返回数据的个数
	private int mPage = -1;                                        //请求第几页
	private int mTotal;                                            //数据总个数
	private ArrayList<String> mArgument = new ArrayList<String>(); //不定参数, 如关键字,分类ID等
	private ArrayList<Object> mList = new ArrayList<Object>();     //返回数据
	
	public void setToken(int token) 
	{
		mToken = token;
	}
	
	public int getToken() 
	{
		return mToken;
	}

	public void setSize(int size) 
	{
		mSize = size;
	}
	
	public int getSize() 
	{
		return mSize;
	}

	public void setPage(int page) 
	{
		mPage = page;
	}

	public int getPage() 
	{
		return mPage;
	}
	
	public void setTotal(int total) 
	{
		mTotal = total;
	}

	public int getTotal() 
	{
		return mTotal;
	}

	public void setArgument(ArrayList<String> argument) 
	{
		mArgument = argument;
	}

	public ArrayList<String> getArgument() 
	{
		return mArgument;
	}
	
	public void setList(ArrayList<Object> list) 
	{
		mList = list;
	}
	
	public ArrayList<Object> getList() 
	{
		return mList;
	}
	
	/**
	 * 供下行填充数据用
	 * @param token 列表标识
	 * @param size 指定数量
	 * @param page 当前页数
	 * @param objs 不定参数
	 */
	public ElementListData(int token, int size, int page, Object... objs) 
	{
		mToken = token;
		mSize = size;
		mPage = page;
		
		for (Object obj: objs)
		{
			Logcat.d(FlagConstant.TAG, " mArgument: " + (String)obj);
			mArgument.add((String)obj);
		}
	}
}
