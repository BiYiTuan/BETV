/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: UILApplication.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.application
 * @Description: Application对象
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:00:34
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.application;

import com.bananatv.custom.player.PPPServer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.service.ad.Ad;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import android.app.Application;
import android.content.Context;

public class UILApplication extends Application
{
	public static ImageLoader mImageLoader; //图片下载管理
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
	}
	
	@Override
	public void onTerminate() 
	{
		super.onTerminate();
	}
	
	/**
	 * 
	 * @Title: initService
	 * @Description: 初始化各类服务(广告服务 图片下载服务 BI服务 ppp服务)
	 * @param context
	 * @return: void
	 */
	public static void initService(Context context)
	{
		BI.startBi(context);              //初始化BI服务
		initImageLoaderService(context);  //初始化图片下载服务
		Ad.startAd(context);              //初始化广告服务
	}
	
	/**
	 * 
	 * @Title: unInitService
	 * @Description: 反初始化各类服务
	 * @return: void
	 */
	public static void unInitService()
	{
		unInitImageLoaderService();
		Ad.stopAd();
		BI.stopBi();
		Logcat.d(FlagConstant.TAG, " gInitPler: " + StaticVariable.gInitPler);
		if (StaticVariable.gInitPler)
		{
			PPPServer.stopService();
		}
	}
	
	/**
	 * 
	 * @Title: initImageLoaderService
	 * @Description: 初始化图片下载服务
	 * @param context
	 * @return: void
	 */
	public static void initImageLoaderService(Context context)
	{
		if(!ImageLoader.getInstance().isInited())
		{
			initImageLoader(context.getApplicationContext());
			mImageLoader = ImageLoader.getInstance();
		}
	}
	
	/**
	 * 
	 * @Title: initImageLoader
	 * @Description: 初始化图片下载器
	 * @param context
	 * @return: void
	 */
	public static void initImageLoader(Context context) 
	{
		/*
		 * This configuration tuning is custom. You can tune every option, 
		 * you may tune some of them, or you can create default configuration 
		 * by ImageLoaderConfiguration.createDefault(this); 
		 */
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheSize(10*1024*1024)
				//.enableLogging() 
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 
	 * @Title: unInit
	 * @Description: 反初始化图片下载服务,清除缓存
	 * @return: void
	 */
	public static void unInitImageLoaderService()
	{
		if(mImageLoader != null)
		{
			mImageLoader.clearMemoryCache();
			//mImageLoader.clearDiscCache();
		}	
	}
	
	/**
	 * 应用启动页选项
	 */
	public static DisplayImageOptions mStartUpOption = new DisplayImageOptions.Builder()
                                                   .cacheInMemory()
                                                   .cacheOnDisc()
                                                   .displayer(new SimpleBitmapDisplayer())
                                                   .build();
	
	/**
	 * 首页分类选项
	 */
	public static DisplayImageOptions mHomeSortOption = new DisplayImageOptions.Builder()
                                                   //.showStubImage(R.drawable.home_sort_d)
                                                   //.showImageForEmptyUri(R.drawable.home_sort_d)
                                                   //.showImageOnFail(R.drawable.home_sort_d)
                                                   .cacheInMemory()
                                                   .cacheOnDisc()
                                                   .displayer(new SimpleBitmapDisplayer())
                                                   .build();
	/**
	 * 首页推荐选项
	 */
	public static DisplayImageOptions mHomeRecdOption = new DisplayImageOptions.Builder()
                                                   .showStubImage(R.drawable.home_recommend_d)
                                                   .showImageForEmptyUri(R.drawable.home_recommend_d)
                                                   .showImageOnFail(R.drawable.home_recommend_d)
                                                   .cacheInMemory()
                                                   .cacheOnDisc()
                                                   //.displayer(new SimpleBitmapDisplayer())
                                                   .displayer(new RoundedBitmapDisplayer(14))  //设置圆角
                                                   .build();
	
	/**
	 * 点播资源选项
	 */
	public static DisplayImageOptions mVodOption = new DisplayImageOptions.Builder()
												   .showStubImage(R.drawable.vod_d)
												   .showImageForEmptyUri(R.drawable.vod_d)
												   .showImageOnFail(R.drawable.vod_d)
												   .cacheInMemory()
												   .cacheOnDisc()
												   //.displayer(new SimpleBitmapDisplayer())
												   .displayer(new RoundedBitmapDisplayer(14))  //设置圆角
												   .build();
	
	/**
	 * 频道Icon选项
	 */
	public static DisplayImageOptions mChannelIconOption = new DisplayImageOptions.Builder()
	   						                       .showStubImage(R.drawable.playback_icon_d)
	   						                       .showImageForEmptyUri(R.drawable.playback_icon_d)
	   						                       .showImageOnFail(R.drawable.playback_icon_d)
	   						                       .cacheInMemory()
	   						                       .cacheOnDisc()
	   						                       .displayer(new SimpleBitmapDisplayer())
	   						                       .build();
	
	/**
	 * 专题选项
	 */
	public static DisplayImageOptions mSpecialOption = new DisplayImageOptions.Builder()
                                                   .showStubImage(R.drawable.special_d)
                                                   .showImageForEmptyUri(R.drawable.special_d)
                                                   .showImageOnFail(R.drawable.special_d)
                                                   .cacheInMemory()
                                                   .cacheOnDisc()
                                                   .displayer(new SimpleBitmapDisplayer())
                                                   .build();
	
	/**
	 * 专题背景选项
	 */
	public static DisplayImageOptions mSpecialBgOption = new DisplayImageOptions.Builder()
												   .cacheInMemory()
												   .cacheOnDisc()
												   .displayer(new SimpleBitmapDisplayer())
												   .build();
	
	/**
	 * 详情海报选项
	 */
	public static DisplayImageOptions mDetailOption = new DisplayImageOptions.Builder()
												   .showStubImage(R.drawable.detail_d)
    											   .showImageForEmptyUri(R.drawable.detail_d)
    											   .showImageOnFail(R.drawable.detail_d)
	                                               .cacheInMemory()
	                                               .cacheOnDisc()
	                                               //.displayer(new SimpleBitmapDisplayer())
	                                               .displayer(new RoundedBitmapDisplayer(8))  //设置圆角
	                                               .build();
	
	/**
	 * 选集选项
	 */
	public static DisplayImageOptions mSelectDramaOption = new DisplayImageOptions.Builder()
	   											   .showStubImage(R.drawable.drama_d)
	   											   .showImageForEmptyUri(R.drawable.drama_d)
	   											   .showImageOnFail(R.drawable.drama_d)
	   											   .cacheInMemory()
	   											   .cacheOnDisc()
	   											   //.displayer(new SimpleBitmapDisplayer())
	   											   .displayer(new RoundedBitmapDisplayer(13))  //设置圆角
	   											   .build();
}
