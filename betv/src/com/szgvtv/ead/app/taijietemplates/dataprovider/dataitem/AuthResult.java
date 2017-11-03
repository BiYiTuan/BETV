/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AuthResult.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 应用鉴权item
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:23:56
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

public class AuthResult 
{
	private int mResult;  //鉴权结果：-1-系统错误，0-成功，1-区域限制, 2-终端限制, 3-黑名单等等
	
	public void setAuthResult(int result)
	{
		mResult = result;
	}
	
	public int getAuthResult()
	{
		return mResult;
	}
}
