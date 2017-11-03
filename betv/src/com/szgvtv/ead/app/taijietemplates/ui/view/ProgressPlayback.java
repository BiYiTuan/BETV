/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ProgressPlayback.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 回放进度信息
 * @author: zhaoqy
 * @date: 2014-8-12 下午5:54:43
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.bananatv.custom.networktraffic.NetTraffic;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ProgressPlayback extends RelativeLayout implements Runnable
{
	public static final int PERIOD_PROGRESS_SHOW = 5000; //无操作,5000ms后隐藏
	public static final int PERIOD_PROGRESS_FRESH = 300; //每隔300ms,刷新当前播放时间
	private Context          mContext;      //上下文
	private ImageView        mIcon;         //icon
	private TextView         mNum;          //当前集
	private TextView         mName;         //名称
	private TextView         mContent;      //内容
	private TextView         mDate;         //日期
	private TextView         mTime;         //时间
	private SeekBar          mBar;          //进度条
	private TextView         mSpeed;        //速度
	private TextView         mCurTime;      //当前时间
	private GVideoView       mVideoView;    //播放器
	private Handler          mPHandler;     //父handler
	private Handler          mHandler;      //handler
	private LiveChannelItem  mLiveChannel;  //频道Item
	private LivePlaybackItem mLivePlayback; //直播回放Item
	private int              mTotal;        //总时间
	private int              mDeta;         //快进/快退时间段

	public ProgressPlayback(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_progress_playback, this);
		mIcon = (ImageView) findViewById(R.id.id_playback_icon);
		mNum = (TextView) findViewById(R.id.id_playback_number);
		mName = (TextView) findViewById(R.id.id_playback_name);
		mContent = (TextView) findViewById(R.id.id_playback_content);
		mDate = (TextView) findViewById(R.id.id_playback_date);
		mTime = (TextView) findViewById(R.id.id_playback_time);
		mBar = (SeekBar) findViewById(R.id.id_playback_seekbar);
		mSpeed = (TextView) findViewById(R.id.id_playback_speed);
		mCurTime = (TextView) findViewById(R.id.id_playback_current_time);
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
	
	private Runnable progressRun = new Runnable() 
	{
		public void run() 
		{
			freshView();
			
			if (mHandler != null) 
			{
				mHandler.postDelayed(progressRun, PERIOD_PROGRESS_FRESH);
			}
		}
	}; 

	@Override
	public void run() 
	{
		hide();
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: 重置定时器
	 * @return: void
	 */
	public void resetTime()
	{
		show();
		freshView();
		
		if (mHandler != null) 
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}

		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_PROGRESS_SHOW);
		mHandler.postDelayed(progressRun, PERIOD_PROGRESS_FRESH);
	}
	
	/**
	 * 
	 * @Title: freshView
	 * @Description: 刷新当前界面(主要是当前播放时间)
	 * @return: void
	 */
	private void freshView()
	{
		mSpeed.setText(getResources().getString(R.string.view_load_speed) + ": " + NetTraffic.getSpeed());  //设置加载速度
		if (!StaticVariable.gSeeking)
		{
			setCurrentTime(mVideoView.getCurrentPosition());
		}
		else
		{
			setCurrentTime(StaticVariable.gCurPosition);
		}
	}
	
	/**
	 * 
	 * @Title: onKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyLeft()
	{
		if (StaticVariable.gSeeking)
		{
			StaticVariable.gCurPosition -= mDeta;
		}
		else
		{
			StaticVariable.gCurPosition = mVideoView.getCurrentPosition();
			StaticVariable.gCurPosition -= mDeta;
		}
		
		if (StaticVariable.gCurPosition < 0)
		{
			StaticVariable.gCurPosition = 0;
		}
		
		StaticVariable.gSeeking = true;
		resetTime();
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyRight
	 * @Description: 响应右键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyRight()
	{
		if (StaticVariable.gSeeking)
		{
			StaticVariable.gCurPosition += mDeta;
		}
		else
		{
			StaticVariable.gCurPosition = mVideoView.getCurrentPosition();
			if (StaticVariable.gCurPosition > mTotal-30*1000)
			{
				mPHandler.removeMessages(FlagConstant.PLAY_SEEKING);
				return true;
			}
			else
			{
				StaticVariable.gCurPosition += mDeta;
			}
		}
		
		if (StaticVariable.gCurPosition > mTotal-30*1000)
		{
			StaticVariable.gCurPosition = mTotal - 30*1000;
		}
		
		StaticVariable.gSeeking = true;
		resetTime();
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyCenter
	 * @Description: 响应OK键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyCenter()
	{
		if(mVideoView.isPlaying())
		{
			mVideoView.pause();
			setPause(true);
	
			//暂停之后, 播放进度条一直显示
			show();
			freshView();
			
			if (mHandler != null) 
			{
				mHandler.removeCallbacksAndMessages(null);
				mHandler = null;
			}

			//每隔300毫秒刷新当前速度
			mHandler = new Handler();
			mHandler.postDelayed(progressRun, PERIOD_PROGRESS_FRESH);
		}
		else
		{
			mVideoView.start();
			setPause(false);
			resetTime();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyPauseToSeek
	 * @Description: 从暂停状态快进/快退
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyPauseToSeek()
	{
		mVideoView.start();
		setPause(false);
		hide();
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyStop
	 * @Description: 响应停止键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyStop()
	{
		mVideoView.pause();
		setPause(true);
		resetTime();
		return true;
	}
	
	/**
	 * 
	 * @Title: setVideoView
	 * @Description: 设置VideoView
	 * @param videoView
	 * @return: void
	 */
	public void setVideoView(GVideoView videoView) 
	{
		mVideoView = videoView;
	}
	
	/**
	 * 
	 * @Title: setChannelItem
	 * @Description: 设置频道Item
	 * @param item
	 * @return: void
	 */
	public void setChannelItem(LiveChannelItem item)
	{
		mLiveChannel = item;
		
		if (mLiveChannel != null)
		{
			mName.setText(mLiveChannel.getTvName());
		}
	}
	
	/**
	 * 
	 * @Title: setPlaybackItem
	 * @Description: 设置回放Item
	 * @param item
	 * @return: void
	 */
	public void setPlaybackItem(LivePlaybackItem item)
	{
		mLivePlayback = item;
		
		if (mLivePlayback != null)
		{
			mContent.setText(getResources().getString(R.string.view_palyback_relook) + " : " + mLivePlayback.getName());
			mDate.setText(mLivePlayback.getDate());
			mTime.setText(mLivePlayback.getTime());
		}
	}
	
	/**
	 * 
	 * @Title: setNum
	 * @Description: 设置频道号
	 * @param num
	 * @return: void
	 */
	public void setNum(int num)
	{
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
	}
	
	/**
	 * 
	 * @Title: setPause
	 * @Description: 设置是否暂停
	 * @param pause
	 * @return: void
	 */
	private void setPause(boolean pause)
	{
		if (pause)
		{
			mIcon.setImageResource(R.drawable.view_pause);
		}
		else
		{
			mIcon.setImageResource(R.drawable.view_play);
		}
	}
	
	/**
	 * 
	 * @Title: setCurrentTime
	 * @Description: 设置当前时间
	 * @param current
	 * @return: void
	 */
	private void setCurrentTime(int current)
	{
		mCurTime.setText(Util.getFormatTime(current, mTotal));
		mBar.setMax((mVideoView.getDuration()));
		mBar.setProgress((current));
	}
	
	/**
	 * 
	 * @Title: setTotalTime
	 * @Description: 设置总时间
	 * @param total
	 * @return: void
	 */
	public void setTotalTime(int total)
	{
		mTotal = total;
		mDeta = mTotal/100;
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
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 
	 * @Title: isShow
	 * @Description: 该View是否显示
	 * @return
	 * @return: boolean
	 */
	public boolean isShow()
	{
		if (getVisibility() == View.VISIBLE)
		{
			return true;
		}
		return false;
	}
}
