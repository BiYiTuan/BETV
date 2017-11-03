/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LoadingVideo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 加载点播
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:21:43
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import java.util.ArrayList;
import com.bananatv.custom.networktraffic.NetTraffic;
import com.bananatv.custom.player.PPPServer;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.service.ad.Ad;
import com.szgvtv.ead.app.taijietemplates.service.ad.AdPosition;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.framework.advertisement.AdManager.IAdCallback;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class LoadingVideo extends RelativeLayout implements Runnable, IAdCallback
{
	public static final int PERIOD_COUNT_DOWN = 1000;             //倒计时时间间隔1000ms
	private Context           mContext;                           //上下文
	private ImageView         mAd;                                //广告图片
	private TextView          mCountdown;                         //倒计时
	private TextView          mName;                              //名称
	private TextView          mBuffer;                            //缓冲
	private TextView          mSpeed;                             //速度
	private Handler           mPHandler;                          //父handler
	private Handler           mHandler;                           //handler
	private ArrayList<String> mAllCode = new ArrayList<String>(); //广告编码列表
	private boolean           mAdFinished = false;                //广告是否播放结束
	private int               mTimeout = 0;                       //加载最长时间
	private int               mDowntime = 10;                     //倒计时时间
	
	public LoadingVideo(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_loading_video, this);
		mAd = (ImageView) findViewById(R.id.id_loading_video_ad);
		mCountdown = (TextView) findViewById(R.id.id_loading_video_countdown);
		mName = (TextView) findViewById(R.id.id_loading_video_name);
		mBuffer = (TextView) findViewById(R.id.id_loading_video_buffer);
		mSpeed = (TextView) findViewById(R.id.id_loading_video_speed);
		mName.setText(getResources().getString(R.string.loading_video) + "…");
		mCountdown.setText("" + mDowntime);
	}
	
	@Override
	protected void onDetachedFromWindow() 
	{
		super.onDetachedFromWindow();
		stopThread();
	}
	
	/**
	 * 
	 * @Title: requestAd
	 * @Description: 请求广告
	 * @return: void
	 */
	private void requestAd()
	{
		if (StaticVariable.gBufferAdTime < 1)
		{
			if (Ad.mAdManager != null)
			{
				//缓冲广告去掉addPosition
				int group = Ad.mAdManager.setAdCallback(this);
				Ad.mAdManager.activePosition(group, AdPosition.getAdPositionOfBuffer(), 1);
			}
		}
		else
		{
			if(StaticVariable.gBufferBitmap != null && mAd != null)
			{
				mAd.setImageBitmap(StaticVariable.gBufferBitmap);
			}
		}
	}
	
	@Override
	public void run() 
	{
		freshView();
		postInvalidate();
		
		if (mHandler != null) 
		{
			mHandler.postDelayed(this, PERIOD_COUNT_DOWN);
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) 
	{
		super.onWindowVisibilityChanged(visibility);
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
	 * @Title: stopThread
	 * @Description: 关闭线程
	 * @return: void
	 */
	private void stopThread()
	{
		if (mHandler != null) 
		{
			mHandler.removeCallbacksAndMessages(null);
			//mHandler = null;
		}
		
		if (Ad.mAdManager != null)
		{
			Ad.mAdManager.freezePosition(AdPosition.getAdPositionOfBuffer());
		}
	}
	
	/**
	 * 
	 * @Title: startThread
	 * @Description: 开始线程
	 * @return: void
	 */
	private void startThread()
	{
		if (mHandler != null) 
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		
		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_COUNT_DOWN);
		
		mTimeout = 20;   //自定义的20秒超时
		mDowntime = 10;  //倒计时时间10秒
		mAllCode.clear();
		mHandler.postDelayed(mFreshRun, PERIOD_COUNT_DOWN);
		
		freshView();
		requestAd();
	}
	
	/**
	 * 
	 * @Title: freshView
	 * @Description: 刷新页面
	 * @return: void
	 */
	private void freshView()
	{
		setCountdownTime();
		mBuffer.setText(getResources().getString(R.string.loading_video_buffer) + PPPServer.getBufferStatus() + "%");  //设置缓冲百分比
		mSpeed.setText("" + NetTraffic.getSpeed());  //设置加载速度
	}
	
	/**
	 * 
	 * @Title: reset
	 * @Description: 重新初始化
	 * @return: void
	 */
	private void reset()
	{
		mAdFinished = false;
		mBuffer.setText(getResources().getString(R.string.loading_video_buffer) + "0%");
		mSpeed.setText(" 0KB/s");
	}
	
	private Runnable mFreshRun = new Runnable() 
	{
		public void run() 
		{
			if (mHandler != null) 
			{
				mHandler.postDelayed(mFreshRun, PERIOD_COUNT_DOWN);
				
				if(mPHandler != null) 
				{
					mTimeout--;
					mDowntime--;
					freshView();
					
					if (mTimeout <= 0)
					{
						Logcat.i(FlagConstant.TAG, " PLAY_TIMEOUT ");
						mPHandler.sendEmptyMessage(FlagConstant.PLAY_TIMEOUT);
					}
				}
			}
		}
	}; 

	/**
	 * 激活广告位
	 * @param count		后续广告个数
	 * @param ret		结果
	 * @param id		广告位ID
	 * @param code		广告编码
	 * @param name		广告名称
	 * @param type		广告格式
	 * @param word		广告内容
	 * @param showtime	播放时长
	 * @param hasDetail	是否有详情
	 */
	@Override
	public void onAdActivated(int count, boolean ret, String id, String code, String name, String type, String word, int showtime, boolean hasDetail) 
	{
		Logcat.d(FlagConstant.TAG, " ret: " + ret);
		Logcat.d(FlagConstant.TAG, " word: " + word);
		
		if(ret && code!= null && !mAllCode.contains(code))
		{
			mAllCode.add(code);
			try 
			{
				if (StaticVariable.gBufferAdTime < 1)
				{
					//应用广告展示BI
					BiMsg.sendAppAdShowBiMsg(code, type, id, showtime + "");
					//实例化Bitmap
					StaticVariable.gBufferBitmap = BitmapFactory.decodeFile(word);
					StaticVariable.gBufferAdTime = 1;
				}
			} 
			catch (OutOfMemoryError e) 
			{
			}
			
			if(StaticVariable.gBufferBitmap != null && mAd != null)
			{
				mAd.setImageBitmap(StaticVariable.gBufferBitmap);
			}
			
			/*mBitmap = BitmapFactory.decodeFile(word);
			if(mBitmap != null && mAd != null)
			{
				mAd.setImageBitmap(mBitmap);
			}*/
		}
		else
		{
			mAllCode.clear();
			if (Ad.mAdManager != null)
			{
				Ad.mAdManager.freezePosition(AdPosition.getAdPositionOfBuffer());
			}
		}
	}

	/**
	 * 取得广告详情
	 * @param count		后续详情个数
	 * @param ret		结果
	 * @param id		广告位ID
	 * @param code		广告编码
	 * @param type		广告格式
	 * @param detail	详情内容
	 */
	@Override
	public void onDetailShow(int count, boolean ret, String id, String code, String type, String detail) 
	{
		if (ret)
		{
			//应用广告详情BI
			//BiMsg.sendAppAdDetailBiMsg(code, id);
		}
	}

	/**
	 * 加载广告结果
	 * @param id	广告位ID
	 * @param ret	加载结果
	 */
	@Override
	public void onLoadFinished(String id, boolean ret) 
	{
		//缓冲时间设计:
		//1. 进入加载页, 开始缓冲, 10秒倒计时开始. 10秒之内不能播, 则一直显示0
		//2. 缓冲超时时间设为20秒, 20秒后还不能播放, 则提示加载失败; 20秒内可以播放了, 则立即开始播放, 隐藏加载页, 此时不管广告有没有播完
	}
	
	/**
	 * 
	 * @Title: getAdFinished
	 * @Description: 获取广告是否播放完状态值
	 * @return
	 * @return: boolean
	 */
	public boolean getAdFinished()
	{
		return mAdFinished;
	}
	
	/**
	 * 
	 * @Title: setName
	 * @Description: 设置加载名称
	 * @param name
	 * @return: void
	 */
	public void setName(String name)
	{
		mName.setText(getResources().getString(R.string.loading_video) + " " + name);
	}
	
	/**
	 * 
	 * @Title: setNameDrama
	 * @Description: 设置加载名称+剧集
	 * @param name
	 * @param number
	 * @return: void
	 */
	public void setNameDrama(String name, String number)
	{
		mName.setText(getResources().getString(R.string.loading_video) + " " + name + "  " + 
	                  getResources().getString(R.string.detail_drama_prefix) + number + getResources().getString(R.string.detail_drama_suffix));
	}
	
	/**
	 * 
	 * @Title: setCountdownTime
	 * @Description: 设置倒计时时间
	 * @return: void
	 */
	private void setCountdownTime()
	{
		if (mDowntime >= 0)
		{
			mCountdown.setText("" + mDowntime);
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
		reset();
		startThread();
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
		setVisibility(View.INVISIBLE);
		stopThread();
	}
	
	/**
	 * 
	 * @Title: isShow
	 * @Description: 该View是否可见
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
