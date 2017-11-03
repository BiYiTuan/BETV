/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: StartUpScreen.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 启动画面(图片轮播)
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:03:55
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import java.util.ArrayList;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.StartUpItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StartUpScreen extends RelativeLayout
{
	private static final int CHANGE_IMAGE = 0;  
	private Context                mContext;                               //上下文
	private ImageView              mImage;                                 //图片
	private ArrayList<StartUpItem> mStarts = new ArrayList<StartUpItem>(); //应用启动页列表
	private int                    mIndex = 0;                             //当前索引
	
	public StartUpScreen(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_startup_screen, this);
		mImage = (ImageView) findViewById(R.id.id_startup_image);
	}
	
	/**
	 * 
	 * @Title: setStartUpList
	 * @Description: 设置应用启动页列表
	 * @param list
	 * @return: void
	 */
	public void setStartUpList(ArrayList<StartUpItem> list)
	{
		mStarts = list;
		
		if (mStarts.size() > 0)
		{
			if (UILApplication.mImageLoader != null)
			{
				UILApplication.mImageLoader.displayImage(mStarts.get(mIndex).getUrl(), mImage, UILApplication.mStartUpOption);
			}
			mHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, 1000);
		}
	}
	
	/**
	 * 
	 * @Title: show
	 * @Description: 显示该View
	 * @return: void
	 */
	public void show()
	{
		setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @Title: hide
	 * @Description: 隐藏该View
	 * @return: void
	 */
	public void hide()
	{
		if (mHandler != null)
		{
			mHandler.removeCallbacksAndMessages(this);
		}
		setVisibility(View.INVISIBLE);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case CHANGE_IMAGE:
			{
				if (mIndex < mStarts.size()-1)
				{
					mIndex++;
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(mStarts.get(mIndex).getUrl(), mImage, UILApplication.mStartUpOption);
					}
					mHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, 1000);
				}
				break;
			}
			default:
				break;
			}
		}
	};
}
