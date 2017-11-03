/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: StaticVariable.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 全局静态变量
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:31:14
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.graphics.Bitmap;

public class StaticVariable 
{
	public static boolean gPrePared = false; //播放是否准备好
	public static boolean gSeeking = false;  //是否在快进(包括快退)
	public static boolean gInitPler = false; //是否已经初始化ppp
	public static int     gPlayerRet = -1;   //初始化PPP服务结果
	public static int     gCurPosition = 0;  //当前播放时间
	
	public static int     gBufferAdTime = 0;      //请求缓冲广告次数
	public static Bitmap  gBufferBitmap = null;
	public static int     gPauseAdTime = 0;       //请求暂停广告次数
	public static Bitmap  gPauseBitmap = null;
	public static int     gFavoriteAdTime = 0;    //请求收藏广告次数
	public static Bitmap  gFavoriteBitmap = null;
	
	
	/**
	 * 
	 * @Title: initStaticValue
	 * @Description: 初始化静态变量, 以防退出应用后, 该静态变量没有及时销毁, 导致下次用时, 该变量初始值是上次的退出前的值
	 * @return: void
	 */
	public static void initStaticValue()
	{
		StaticVariable.gSeeking = false;
		StaticVariable.gPrePared = false;
		StaticVariable.gInitPler = false;
		StaticVariable.gPlayerRet = -1;
		StaticVariable.gCurPosition = 0;
		
		StaticVariable.gBufferAdTime = 0;
		StaticVariable.gBufferBitmap = null;
		StaticVariable.gPauseAdTime = 0;
		StaticVariable.gPauseBitmap = null;
		StaticVariable.gFavoriteAdTime = 0;
		StaticVariable.gFavoriteBitmap = null;
	}
}
