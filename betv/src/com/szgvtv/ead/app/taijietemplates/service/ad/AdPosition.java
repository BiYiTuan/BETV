/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdPosition.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ad
 * @Description: 广告位
 * @author: zhaoqy
 * @date: 2014-9-28 下午5:25:55
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.service.ad;

import com.szgvtv.ead.app.taijietemplates.util.Constant;

public class AdPosition 
{
	/**
	 * 
	 * @Title: getAdPositionOfFavorite
	 * @Description: 收藏页广告位
	 * @return
	 * @return: String
	 */
	public static String getAdPositionOfFavorite()
	{
		String position = "";
		position = Constant.APPID + "_4" + "535M0";
		return position;
	}
	
	/**
	 * 
	 * @Title: getAdPositionOfPause
	 * @Description: 暂停页广告位
	 * @return
	 * @return: String
	 */
	public static String getAdPositionOfPause()
	{
		String position = "";
		position = Constant.APPID + "_4" + "je4sh";
		return position;
	}
	
	/**
	 * 
	 * @Title: getAdPositionOfBuffer
	 * @Description: 播放缓冲广告位
	 * @return
	 * @return: String
	 */
	public static String getAdPositionOfBuffer()
	{
		String position = "";
		position = Constant.APPID + "_4" + "66KWj";
		return position;
	}
}
