/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdviceResult.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem
 * @Description: 意见收集结果
 * @author: zhaoqy
 * @date: 2014-8-7 下午8:23:27
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem;

import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;

public class AdviceResult 
{
    private int mResult;  //操作结果：0-成功，-1-系统错误
	
	public void setAdviceResult(int result)
	{
		Logcat.i(FlagConstant.TAG, " AdviceResult: " + result);
		mResult = result;
	}
	
	public int getAdviceResult()
	{
		return mResult;
	}
}
