/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: GenerateBitmap.java
 * @Prject: TaijieTemplates1_gytv
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 用一个线程生成Bitmap(防止主线程阻塞)
 * @author: zhaoqy
 * @date: 2014-12-10 下午8:39:14
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class GenerateBitmap extends Thread
{
	private Bitmap  mOriginal; //原始的Bitmap
	private Bitmap  mBitmap;   //生成的Bitmap
	private Handler mHandler;  //Handler
	private int     mIndex;    //索引
	
	
	public GenerateBitmap(Bitmap original, Handler handler, int index)
	{
		mOriginal = original;
		mHandler = handler;
		mIndex = index;
	}
	
	@Override
	public void run() 
	{
		super.run();
		
		mBitmap = ConvertViewToBitmap.createReflection(mOriginal, 140);
		Message msg = new Message();
		msg.what = FlagConstant.HOME_BITMAP;
		msg.obj = mIndex;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 
	 * @Title: getBitmap
	 * @Description: 获取Bitmap
	 * @return
	 * @return: Bitmap
	 */
	public Bitmap getBitmap()
	{
		return mBitmap;
	}
}
