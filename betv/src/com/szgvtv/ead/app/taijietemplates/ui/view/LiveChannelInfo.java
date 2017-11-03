/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveChannelInfo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 直播信息
 * @author: zhaoqy
 * @date: 2014-8-11 下午8:33:16
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import java.util.ArrayList;
import com.bananatv.custom.networktraffic.NetTraffic;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.TimeUtil;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveChannelInfo extends RelativeLayout implements Runnable
{
	public static final int PERIOD_CHANNEL_INFO_SHOW = 6000; //间隔时间为5秒
	public static final int PERIOD_PROGRESS_FRESH = 1000;    //每隔1000ms,刷新当前速度
	private Context        mContext;                         //上下文
	private Handler        mHandler = null;                  //handler
	private TextView       mNum;                             //当前集
	private TextView       mName;                            //名称
	private TextView       mSpeed;                           //速度
	private TextView       mPreItem[] = new TextView[2];     //预告节目
	private ArrayList<LivePreviewItem> mPreviewList = new ArrayList<LivePreviewItem>();  
	 
	public LiveChannelInfo(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_live_channel_info, this);
		mNum = (TextView) findViewById(R.id.id_live_channel_number);
		mName = (TextView) findViewById(R.id.id_live_channel_name);
		mSpeed = (TextView) findViewById(R.id.id_live_channel_speed);
		mPreItem[0] = (TextView) findViewById(R.id.id_live_channel_preview1);
		mPreItem[1] = (TextView) findViewById(R.id.id_live_channel_preview2);
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: 重置定时器
	 * @return: void
	 */
	public void resetTime()
	{
		if (mHandler != null) //重置
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}	
		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_CHANNEL_INFO_SHOW);
		mHandler.postDelayed(progressRun, PERIOD_PROGRESS_FRESH);
	}

	@Override
	public void run() 
	{
		hide();
	}
	
	private Runnable progressRun = new Runnable() 
	{
		public void run() 
		{
			mSpeed.setText(getResources().getString(R.string.view_load_speed) + " : " + NetTraffic.getSpeed());
			
			if (mHandler != null) 
			{
				mHandler.postDelayed(progressRun, PERIOD_PROGRESS_FRESH);
			}
		}
	}; 
	
	/**
	 * 
	 * @Title: setChannelInfo
	 * @Description: 设置频道信息
	 * @param num
	 * @param name
	 * @return: void
	 */
	public void setChannelInfo(int num, String name)
	{
		Logcat.d(FlagConstant.TAG, "num: " + num + ", name: " + name);
		int number = num + 1;
		if (number < 10)
		{
			mNum.setText("00" + number);
		}
		else if (number < 100)
		{
			mNum.setText("0" + number);
		}
		else if (number < 1000)
		{
			mNum.setText("" + number);
		}
		mName.setText(name);
		mSpeed.setText(getResources().getString(R.string.view_load_speed) + " : " + NetTraffic.getSpeed());
	}
	
	/**
	 * 
	 * @Title: setPreviewList
	 * @Description: 设置预告列表
	 * @param preview
	 * @return: void
	 */
	public void setPreviewList(ArrayList<LivePreviewItem> previewList)
	{
		mPreviewList = previewList;
		int playIndex = TimeUtil.getCurProgramIndex(mPreviewList);
		if (playIndex >= 0)
		{
			for (int i=playIndex; i<playIndex+2; i++)
			{
				if (i < mPreviewList.size())
				{
					LivePreviewItem item = mPreviewList.get(i);
					String date = item.getDate();
					String time = item.getTime();
					String name = item.getName();
					Logcat.d(FlagConstant.TAG, "date: " + date + ", time: " + time + ", name: " + name);
					mPreItem[i-playIndex].setText(date + "    " + time + "    " + name);
					mPreItem[i-playIndex].setVisibility(View.VISIBLE);
				}
				else
				{
					mPreItem[i-playIndex].setVisibility(View.INVISIBLE);
				}
			}
		}
		else
		{
			for (int i=0; i<2; i++)
			{
				mPreItem[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	/**
	 * 
	 * @Title: clearPreviewList
	 * @Description: 清除预告列表
	 * @return: void
	 */
	public void clearPreviewList()
	{
		for (int i=0; i<2; i++)
		{
			mPreItem[i].setVisibility(View.INVISIBLE);
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
		resetTime();
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
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		setVisibility(View.INVISIBLE);
	}
	
	public boolean isShow()
	{
		if (getVisibility() == View.VISIBLE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
