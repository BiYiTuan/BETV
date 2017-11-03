/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ProgressVolume.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 音量进度信息
 * @author: zhaoqy
 * @date: 2014-8-12 下午6:00:43
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ProgressVolume extends RelativeLayout implements Runnable
{
	public static final int PERIOD_VOLUME_SHOW = 3000; //间隔时间为3秒
	private Context        mContext;        //上下文
	private TextView       mValue;          //音量
	private SeekBar        mSeekBar;        //进度条
	private AudioManager   mAudioManager;   //音频管理器
	private Handler        mHandler = null; //handler
	private int            mMaxVolume = 0;  //最大音量值
	private int            mCurVolume = 0;  //当前音量值
	
	public ProgressVolume(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
		setAudioManager();
		freshCurVolume();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_progress_volume, this);
		mValue = (TextView) findViewById(R.id.id_volume_value);
		mSeekBar = (SeekBar) findViewById(R.id.id_volume_seekbar);
	}
	
	/**
	 * 
	 * @Title: setAudioManager
	 * @Description: 设置音频管理器
	 * @return: void
	 */
	private void setAudioManager()
	{
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSeekBar.setMax(mMaxVolume);
	}
	
	/**
	 * 
	 * @Title: freshCurVolume
	 * @Description: 设置当前音量
	 * @return: void
	 */
	private void freshCurVolume()
	{
		mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSeekBar.setProgress(mCurVolume);
		mValue.setText(getResources().getString(R.string.view_volume_value) + " : "+ mCurVolume);
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: 重置定时器
	 * @return: void
	 */
	private void resetTime()
	{
		show();
		
		if (mHandler != null) //重置
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}	
		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_VOLUME_SHOW);
	}

	@Override
	public void run() 
	{
		hide();
	}
	
	/**
	 * 
	 * @Title: doVolumeSub
	 * @Description: 音量减操作
	 * @return
	 * @return: boolean
	 */
	public boolean doVolumeSub()
	{
		resetTime();
		onVolumeSub(true);
		freshCurVolume();
		return true;
	}
	
	/**
	 * 
	 * @Title: doVolumeAdd
	 * @Description: 音量加操作
	 * @return
	 * @return: boolean
	 */
	public boolean doVolumeAdd()
	{
		resetTime();
		onVolumeSub(false);
		freshCurVolume();
		return true;
	}
	
	/**
	 * 
	 * @Title: onVolumeSub
	 * @Description: 响应音量加减
	 * @param flag
	 * @return: void
	 */
	private void onVolumeSub(boolean flag)
	{
		if(flag)
		{
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
		}
		else
		{
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
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
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 
	 * @Title: isShow
	 * @Description: 该view是否可见
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
