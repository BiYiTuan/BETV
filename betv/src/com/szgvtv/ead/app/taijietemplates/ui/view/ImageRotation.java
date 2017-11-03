/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ImageRotation.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 一张图片旋转控件
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:45:39
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ImageRotation extends ImageView implements Runnable
{
	public static final int PERIOD_IMAGE = 300; //一张图片旋转时间间隔300ms
	private Handler mHandler;     //handler
	private int     mDegree = 0;  //旋转角度
	
	public ImageRotation(Context context) 
	{
		super(context);
	}
	
	public ImageRotation(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) 
	{
		int nWidth = getWidth()/2;
		int nHeight = getHeight()/2;
		
		canvas.rotate(mDegree, nWidth, nHeight);
		Drawable drawable = getDrawable();
		if (drawable != null) 
		{
			drawable.draw(canvas);
		}
	}
	
	@Override
	public void run() 
	{
		mDegree += 30;
		mDegree %= 360;
		
		postInvalidate();
		if (mHandler != null) 
		{
			mHandler.postDelayed(this, PERIOD_IMAGE);
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) 
	{
		if (mHandler != null)  //重置
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		
		if (visibility == View.VISIBLE) 
		{
			mHandler = new Handler(); //显示
			mHandler.postDelayed(this, PERIOD_IMAGE);
		}
		super.onWindowVisibilityChanged(visibility);
	}
}
