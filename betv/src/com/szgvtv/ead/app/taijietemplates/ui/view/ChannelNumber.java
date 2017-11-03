/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ChannelNumber.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 响应数字键
 * @author: zhaoqy
 * @date: 2014-10-13 下午4:16:48
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChannelNumber extends RelativeLayout implements Runnable
{
	public static final int PERIOD_CHANNEL_NUMBER_SHOW = 3000; //间隔时间为3秒
	private Context      mContext;        //上下文
	private TextView     mNumber;         //无内容信息
	private Handler      mPHandler;       //父handler
	private Handler      mHandler = null; //handler
	private StringBuffer mBuffer;         //buffer

	public ChannelNumber(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_channel_number, this);
		mNumber = (TextView) findViewById(R.id.id_channel_number);
	}
	
	/**
	 * 
	 * @Title: setParentHandler
	 * @Description: 设置Handler
	 * @param handler
	 * @return: void
	 */
	public void setParentHandler(Handler handler)
	{
		mPHandler = handler;
	}
	
	/**
	 * 
	 * @Title: getParentHandler
	 * @Description: 获取Handler
	 * @return
	 * @return: Handler
	 */
	public Handler getParentHandler()
	{
		return mPHandler;
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: 重置定时器
	 * @return: void
	 */
	private void resetTime()
	{
		if (mHandler != null) //重置
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}	
		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_CHANNEL_NUMBER_SHOW);
	}

	@Override
	public void run() 
	{
		mNumber.setBackground(null);  //去掉TextView的背景图
		mNumber.setText("");
		mBuffer = null;
		if (mHandler != null) 
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
	}
	
	/**
	 * 
	 * @Title: isBufferNull
	 * @Description: buffer是否为空
	 * @return
	 * @return: boolean
	 */
	public boolean isBufferNull()
	{
		if (mBuffer == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @Title: getBuffer
	 * @Description: 获取buffer
	 * @return
	 * @return: StringBuffer
	 */
	public StringBuffer getBuffer()
	{
		return mBuffer;
	}
	
	/**
	 * 
	 * @Title: doKeyDigitalNumber
	 * @Description: 响应数字键
	 * @param code
	 * @return
	 * @return: boolean
	 */
	public boolean doKeyDigitalNumber(int code)
	{
		if (mBuffer == null)
		{
			mBuffer = new StringBuffer("");
			mPHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SWITCH, 3000);
		}
		
		if (mBuffer.length() < 3)
		{
			mBuffer.append(code);
			mNumber.setText(mBuffer);
			mNumber.setBackgroundResource(R.drawable.live_number_bg);  //为TextView设置背景图
			resetTime();
		}
		return true;
	}
}
