/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PlayUriParser.java
 * @Prject: AndroidPlayer
 * @Package: com.bananatv.custom.player
 * @Description: 播放节目JNI接口，目前支持gvppp、force、sop、http格式播放
 * @author: lijungang 
 * @date: 2014-1-24 下午4:04:53
 */
package com.bananatv.custom.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.forcetech.android.ForceTV;

public class PlayUriParser {
	
	private PlayUriParser() {
	}
	private static PlayUriParser m_playuri = new PlayUriParser();
	public static PlayUriParser getHttpUri(){
		if(m_playuri == null) m_playuri = new PlayUriParser();
		return m_playuri;
	}
	
	static {
		System.loadLibrary("gvplayer");
		//System.load("/mnt/sdcard/libgvplayer.so");
	}
	
	private uriCallBack m_uriCallBack;
	public void setCallBack(uriCallBack test_activity){
		this.m_uriCallBack = test_activity;
	}
	public interface uriCallBack
	{
		/**
		 * 
		 * @Title: jni_callback_found_http
		 * @Description: http地址回调
		 * @param ret为0表示http地址回调成功可用http_uri进行播放，ret其它值表示生成http地址失败,org_uri原始播放地址
		 * @return: void
		 */
		void jni_callback_found_http(int ret, String http_uri, String org_uri);
		/**
		 * 
		 * @Title: jni_callback_server_maintain
		 * @Description: 后台维护状态回调,ret = -1表示在服务器处于维护状态，ret = 0表示服务器恢复正常
		 * @param ret
		 * @return: void
		 */
		void jni_callback_server_maintain(int ret);
	}
	
	public void callback_found_http_uri(int ret, String http_uri, String org_uri)
	{
		m_uriCallBack.jni_callback_found_http(ret, http_uri, org_uri);
		if(ret == 0){
			isCallBack = true;
		}
		if(null != org_uri && org_uri.startsWith("gvppp://") && (ret == 0)){
			isServerNormal = true;
			startServerMaintainCheck();
		}
	}
	
	private ScheduledExecutorService mServerCheck = null;
	private boolean isServerNormal = true;
	private static ForceTV mForceTv = null;
	private boolean isCallBack = false;

	/**
	 * 
	 * @Title: startServerMaintainCheck
	 * @Description: 启动定时检测ppp服务器维护状态
	 * @return: void
	 */
	private void startServerMaintainCheck(){
      	if(mServerCheck != null){
      		mServerCheck.shutdownNow(); 
      		mServerCheck = null;
    	}
		if(mServerCheck == null){
			mServerCheck = Executors.newScheduledThreadPool(1); 
			mServerCheck.scheduleAtFixedRate(new CheckServer(), 0, 20000, TimeUnit.MILLISECONDS); 
		}
	}
	/**
	 * 
	 * @Title: CheckServer
	 * @Description: 定时检测ppp服务器状态
	 * @return: void
	 */
	class CheckServer implements Runnable{
		@Override
		public void run() {
		// TODO Auto-generated method stub
			try {
				int ret = PPPGetMaintainStatus();
				Log.v("Player", "MainStatus ret:" + ret);
				if(ret == -2){
					if(isServerNormal){
						m_uriCallBack.jni_callback_server_maintain(-1);
						isServerNormal = false;
					}
				}else if(ret == 0){
					if(!isServerNormal){
						m_uriCallBack.jni_callback_server_maintain(0);
						isServerNormal = true;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}	
	/**
	 * 
	 * @Title: ForceTvInit
	 * @Description: 原力视频服务初始化
	 * @return: void
	 */
	public void ForceTvInit(Context mcontext)
	{   
    	if(null == mForceTv) {
    		mForceTv = new ForceTV();
    		mForceTv.initForceClient();
    		String mAppFilesDir = mcontext.getFilesDir().getAbsolutePath();
    	    String mAppLogDir = mAppFilesDir + "/../playerlog";
    	    File mLogDir = new File(mAppLogDir);
    	    if(!mLogDir.exists()) mLogDir.mkdir();
    		initNativeLogPath(mAppLogDir);
    	}
	}
	/**
	 * 
	 * @Title: getMD5Str
	 * @Description: 获取16位md5值
	 * @return: string
	 */
	public String getMD5Str(String str) {
	      MessageDigest messageDigest = null; 
	      try {       
	          messageDigest = MessageDigest.getInstance("MD5");  
	          messageDigest.reset();       
	          messageDigest.update(str.getBytes("UTF-8"));       
	      } catch (NoSuchAlgorithmException e) { 
	          Log.v("Player", "NoSuchAlgorithmException caught!");
	          return "Android";      
	      } catch (UnsupportedEncodingException e) {       
	          e.printStackTrace();
	          return "Android";
	      }       
	      byte[] byteArray = messageDigest.digest();
	      StringBuffer md5StrBuff = new StringBuffer();
	      for (int i = 0; i < byteArray.length; i++) {                   
	          if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)       
	              md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));       
	          else       
	              md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));       
	      } 
	      return md5StrBuff.substring(8, 24).toString(); 
	}
	/**
	 * 
	 * @Title: PPPServiceInit
	 * @Description: ppp流媒体服务初始化
	 * @return: int
	 */
	public int PPPServiceInit(String serv_ip, String usr_name, String pwd, String key, String pkg_name){
		return PPPServiceInitNative(serv_ip, usr_name, pwd, key, getMD5Str(pkg_name));
	}
	
	/**
	 * 
	 * @Title: PPPGetBufferStatus
	 * @Description: 获取缓存百分比，建议1s获取一次
	 * @return: int
	 */
	public int PPPGetBufferStatus(){
		if(!isCallBack) return 0;
		return PPPGetBufferStatusNative();
	}
	
	/**
	 * 
	 * @Title: stopPlay
	 * @Description: 停止当前视频播放，用于在当前节目播放退出时调用,返回0正常退出，-1退出播放异
	 * @return: int
	 */
	public int stopPlay(){
      	if(mServerCheck != null){
      		mServerCheck.shutdownNow(); 
      		mServerCheck = null;
    	}
      	isServerNormal = true;
      	isCallBack = false;
      	return stopPlayNative();
	}
	
	/**
	 * 
	 * @Title: stopService
	 * @Description: 流媒体退出时调用，用于释放初始化时分配的内存及线程资源等
	 * @return: void
	 */
	public void stopService(){
		stopServiceNative();
    	if(mForceTv != null){
    		mForceTv.stop();
    		mForceTv = null;
    	}
	}
	
	public native void initNativeLogPath(String path);
	/**
	 * 
	 * @Title: PPPServiceInitNative
	 * @Description: ppp流媒体播放本地初始化函数
	 * @param serv_ip
	 * @param usr_name
	 * @param pwd
	 * @param key
	 * @param term_type
	 * @return
	 * @return: int
	 */
	public native int PPPServiceInitNative(String serv_ip, String usr_name, String pwd, String key, String term_type);
	/**
	 * 
	 * @Title: setPlayUri
	 * @Description: 设置流媒体的播放地址
	 * @param url
	 * @return: void
	 */
	public native void setPlayUri(String url);
	/**
	 * 
	 * @Title: startToHttpUri
	 * @Description: 流媒体地址转http地址
	 * @return: void
	 */
	public native void startToHttpUri();
	/**
	 * 
	 * @Title: stopPlayNative
	 * @Description: 退出节目时jni层调用
	 * @return: int
	 */
	public native int stopPlayNative();
	/**
	 * 
	 * @Title: stopServiceNative
	 * @Description: jni层流媒体退出时调用，用于释放初始化时分配的内存及线程资源等
	 * @return: void
	 */
	public native void stopServiceNative();
	/**
	 * 
	 * @Title: PPPGetBufferStatusNative
	 * @Description: jni层获取缓存百分比，建议1s获取一次
	 * @return: int
	 */
	public native int  PPPGetBufferStatusNative();
	/**
	 * 
	 * @Title: PPPGetMaintainStatus
	 * @Description: ppp获取服务器是否在维护，建议20s获取一次
	 * @return: int 返回值-2表示在维护，0表示正常
	 */
	public native int  PPPGetMaintainStatus();
	/**
	 * 
	 * @Title: GetPlayerVersion
	 * @Description: 获取库的版本号
	 * @return: String
	 */
	public native String  GetPlayerVersion();
}
