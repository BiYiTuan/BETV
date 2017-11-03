/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: GeneralParam.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 共有参数
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:33:26
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.content.Context;

public class GeneralParam 
{
	/**
	 * 
	 * @Title: generateGeneralParam
	 * @Description: 生成共有参数
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String generateGeneralParam(Context context) 
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("appid=");
		sb.append(Constant.APPID);
		sb.append("&");
		sb.append("terminal=");
		sb.append(Constant.TERMAL_TYPE);
		sb.append("&");
		sb.append("machineno=");
		sb.append(StbInfo.getLocalMacAddress(context));
		sb.append("&");
		sb.append("lang=");
		sb.append(StbInfo.getPortLaug());  
		
		return sb.toString();
	}
}
