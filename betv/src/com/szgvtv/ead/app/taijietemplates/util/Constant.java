/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: Constant.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 常量定义
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:35:36
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

public class Constant 
{
	public static final boolean URL_LOCAL_SERVICE = false;   //是不是本地服务器 自己电脑作为服务器
	public static final int TERMAL_TYPE = 4;                 //终端类型:1:tvpad; 2:pc; 3:ios; 4:android
	public static final int NETWORK_TIMEOUT = 15000;         //网络请求的超时时间设置
	public static final int OTHER_ERROR = -999;              //客户端未知错误
	public static final int DATABASE_VERSION = 3;            //数据库版本 修改数据库要修改此代码
	
	public static final String APPID = "com.szgvtv.ead.app.taijietemplates_betvytv";  
	public static final String REQUEST_URL_MAIN_HOST = "http://epg.ccohi.nz";  
	public static final String REQUEST_URL_PORT = "8080"; 
	public static final String REQUEST_URL_HOST = REQUEST_URL_MAIN_HOST + ":" + REQUEST_URL_PORT + "/" + APPID; 
	
	public static final String APP_VERSION = "V3.2";      //应用显示版本
	public static final String DATABASE_NAME = "data.db"; //数据库文件
	public final static String ENCODING_UTF8 = "utf-8";   
	public static final String ACTION_FRESH_DRAMA = "com.szgvtv.ead.app.taijietemplates_betvytv.action.freshdrama";     //更新剧集广播action
	public static final String ACTION_TIMEAUTH_FAIL = "com.szgvtv.ead.app.taijietemplates_betvytv.action.timeauthfail"; //定时鉴权失败广播action
}
