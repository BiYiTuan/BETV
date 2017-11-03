/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityPlayVideo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 点播播放Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:50:04
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import com.bananatv.custom.networktraffic.NetTraffic;
import com.bananatv.custom.player.PPPServer;
import com.bananatv.custom.player.PlayUriParser.uriCallBack;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfo;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfoTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.DemandMenu;
import com.szgvtv.ead.app.taijietemplates.ui.view.ExitToast;
import com.szgvtv.ead.app.taijietemplates.ui.view.GVideoView;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingVideo;
import com.szgvtv.ead.app.taijietemplates.ui.view.PauseAd;
import com.szgvtv.ead.app.taijietemplates.ui.view.ProgressVideo;
import com.szgvtv.ead.app.taijietemplates.ui.view.ProgressVolume;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.ReflectUtils;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class ActivityPlayVideo extends ActivityReceiver implements uriCallBack, OnPreparedListener, OnCompletionListener, OnErrorListener, OnInfoListener
{
	private Context        mContext;         //上下文
	private GVideoView     mVideoView;       //播放器
	private LoadingVideo   mLoading;         //加载
	private PauseAd        mPauseAd;         //暂停广告
	private ProgressVideo  mPlay;            //播放进度
	private ProgressVolume mVolume;          //音量
	private DemandMenu     mMenu;            //点播菜单
	private ImageView      mPreserve;        //流媒体维护
	private DialogPrompt   mDialog;          //提示框
	private VideoItem      mVideo;           //点播资源item
	private VideoInfo      mVideoInfo;       //数据库资源item
	private PPPServerAsync mPPPServer;       //ppp同步服务
	private NetTraffic     mNetTraffic;      //网络监测
    private boolean        mStoped = false;  //是否停止
    private boolean        mContinue = true; //是否续播
    private boolean        mWantExit = false; //是否想退出
	private int            mIndex = 0;       //当前播放剧集索引
	private int            mTotal = 0;       //总时间
	private int            mRet = -2;        //jni回调结果
	private int            mDramaSize = 0;   //剧集总集数
	private int            mBufferId = 0;    //点播缓冲时间启动id
	private int            mPlayId = 0;      //播放时间启动id
	private String         mErrorType = "";  //错误类型
	private int            mErrorCode = -1;  //错误码
	private int            mErrorExtra;      //错误附加值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.activity_play_video);
		initViews();
		getVodInfo();
		init();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		
		mPlayId = BI.getStartTimeId();
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnInfoListener(this);
		
		PPPServer.setCallBack(this);
		startTask();
	}
	
	private void initViews()
	{
		mVideoView = (GVideoView) findViewById(R.id.id_play_video_videoview);
		mLoading = (LoadingVideo) findViewById(R.id.id_play_video_loading);
		mPauseAd = (PauseAd) findViewById(R.id.id_play_video_pause_ad);
		mPlay = (ProgressVideo) findViewById(R.id.id_play_video_progress_video);
		mVolume = (ProgressVolume) findViewById(R.id.id_play_video_volume);
		mMenu = (DemandMenu) findViewById(R.id.id_play_video_menu);
		mPreserve = (ImageView) findViewById(R.id.id_ppp_preserve);
	} 
	
	private void init()
	{
		mPlay.setVideoView(mVideoView);
		mMenu.setVideoItem(mVideo);
		mNetTraffic = new NetTraffic();
		mNetTraffic.start();
	}
	
	/**
	 * 
	 * @Title: getVodInfo
	 * @Description: 获取Video资源
	 * @return: void
	 */
	private void getVodInfo()
	{
		Bundle bundle = getIntent().getExtras();
		mVideo = bundle.getParcelable(FlagConstant.ToActivityPlayVideoKey);
		if (mVideo != null)
		{
			mDramaSize = mVideo.getDramaList().size();
		}
		mIndex = bundle.getInt(FlagConstant.DramaIndexKey, 0);
		Logcat.d(FlagConstant.TAG, " ==== ActivityPlayVideo ==== mPlayIndex: " + mIndex);
		int playTime = bundle.getInt(FlagConstant.ToActivityPlayTimeKey, 0);
		Logcat.d(FlagConstant.TAG, " ==== ActivityPlayVideo ==== playTime: " + playTime);
		if (playTime < 30*1000)
		{
			//mContinue等于-1时, 则不续播, 要从头开始播(只针对有播放历史的情况)
			mContinue = false;
		}
		mVideoInfo = VideoInfoTable.getPlayHistory(mVideo.getVideoCode());
	}
	
	/**
	 * 
	 * @Title: startTask
	 * @Description: 开始任务
	 * @return: void
	 */
	public void startTask() 
	{
		StaticVariable.gPrePared = false;
		startLoading();
		startPPPServerAsyncTask();
	}
	
	/**
	 * 
	 * @Title: startPPPServerAsyncTask
	 * @Description: 开始ppp服务异步任务
	 * @return: void
	 */
	private void startPPPServerAsyncTask()
	{
		if (mPPPServer != null)
		{
			mPPPServer.cancel(true);
		}
		mPPPServer = new PPPServerAsync();
		mPPPServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	/**
	 * 
	 * @Title: startLoading
	 * @Description: 开始加载动画
	 * @return: void
	 */
	private void startLoading()
	{
		//隐藏mPauseAd
		if (mPauseAd.isShow())
		{
			mPauseAd.hide();
		}
		//隐藏mProgressVideo
		if (mPlay.isShow())
		{
			mPlay.hide();
		}
		
		//当加载电视剧和综艺时, 显示剧集号(add 2014-12-05)
		String vodtype = mVideo.getVodtype();
		if (vodtype.equals("1"))  //电影时, 只显示名称
		{
			mLoading.setName(mVideo.getName());
		}
		else if (vodtype.equals("2") || vodtype.equals("3"))  //电视剧 综艺时, 显示名称+剧集号
		{
			String number = mVideo.getDramaList().get(mIndex).getNumber();
			mLoading.setNameDrama(mVideo.getName(), number);
		}
		
		mLoading.setParentHandler(mHandler);
		mLoading.show();
	}
	
	/**
	 * 
	 * @Title: startPPPServer
	 * @Description: 开始PPP服务
	 * @param url
	 * @return: void
	 */
	private void startPPPServer(String url) 
	{
		if (url.startsWith("gvppp"))   //流媒体播放地址
		{
			Logcat.i(FlagConstant.TAG, " 111111111 gPlayerRet: " + StaticVariable.gPlayerRet);
			if(StaticVariable.gPlayerRet != 0)
			{
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_RETASK, 500);
			}
			else
			{
				Logcat.i(FlagConstant.TAG, " url: " + url);
				PPPServer.setPlayUri(url);
				PPPServer.startToHttpUri();
			}
		}
		else  //原力播放地址
		{
			Logcat.i(FlagConstant.TAG, " url: " + url);
			PPPServer.setPlayUri(url);
			PPPServer.startToHttpUri();
		}
	}
	
	/**
	 * 
	 * @Title: stopVideoView
	 * @Description: 关闭播放器和ppp服务
	 * @return: void
	 */
	private void stopVideoView()
	{
		if (mLoading.isShow())
		{
			mLoading.hide();  //关闭加载线程
		}
		mVideoView.stopPlayback();
		mVideoView.destroyDrawingCache();
		PPPServer.stopPlay();
	}
	
	class PPPServerAsync extends AsyncTask<Void, Void, Void> 
	{
		@Override
		protected Void doInBackground(Void... params) 
		{
			String url = mVideo.getDramaList().get(mIndex).getUrl();
			
			if(!url.isEmpty())
			{
				mBufferId = BI.getStartTimeId();
				startPPPServer(url);
			}
			return null;
		}
	}
	
	@Override
	public void jni_callback_found_http(int ret, String http_uri, String org_uri)
	{
		Logcat.i(FlagConstant.TAG, " jni_callback_found_http: " + ret + ", " + http_uri);
		if (ret != 0 || TextUtils.isEmpty(http_uri)) 
		{
			Logcat.i(FlagConstant.TAG, "PLAY_ERROR: jni_callback_found_http");
			mErrorType = FlagConstant.ERROR_LIBRARY; //错误码: 流媒体库
			mErrorCode = ret;
			mHandler.sendEmptyMessage(FlagConstant.PLAY_ERROR);
		} 
		else 
		{
			Message msg = new Message();
			msg.what = FlagConstant.PLAY_SET_URL;
			msg.obj = http_uri;
			//mHandler.sendMessageDelayed(msg, 3000);
			mHandler.sendMessage(msg); //modify by zhaoqy-2015-2-27
		}
	}
	
	@Override
	public void jni_callback_server_maintain(int ret) 
	{
		Logcat.d(FlagConstant.TAG, " ret: " + ret);
		mRet = ret;
		if (ret == 0)  //服务器恢复
		{
			mHandler.sendEmptyMessage(FlagConstant.PLAY_COMPLATE_PPP_PRESERVE);
		}
		else if (ret == -1)  //服务器维护
		{
			mHandler.sendEmptyMessage(FlagConstant.PLAY_START_PPP_PRESERVE);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		//add by zhaoqy 2015-05-25
		if(ReflectUtils.existSetParameterMethod())
		{
			mVideoView.setParameter(1);
		}	
		StaticVariable.gSeeking = false;
		mTotal = mVideoView.getDuration();
		mPlay.setTotalTime(mTotal);
		mPlay.setParentHandler(mHandler);
		String vodtype = mVideo.getVodtype();
		int dramas = mVideo.getDramaList().size();
		Logcat.d(FlagConstant.TAG, " vodtype: " + vodtype + ",  dramas: " + dramas);
		
		if (vodtype.equals("1"))  //电影时, 只显示名称
		{
			mPlay.setName(mVideo.getName());  
		}
		else if (vodtype.equals("2") || vodtype.equals("3"))  //电视剧 综艺时, 显示名称+剧集号
		{
			String number = mVideo.getDramaList().get(mIndex).getNumber();
			mPlay.setName(mVideo.getName());
			mPlay.setDramaNumber(number);
		}
		
		if (mVideoInfo != null)
		{
			if (!mVideoInfo.getDramaCode().isEmpty() && mVideoInfo.getDramaCode().equals(mVideo.getDramaList().get(mIndex).getDramaCode()))
			{
				int playTime = mVideoInfo.getPlayTime();
				Logcat.d(FlagConstant.TAG, " playTime: " + playTime);
				
				if (playTime < 30*1000)
				{
					mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
				}
				else if (playTime >= 30*1000)
				{
					if (!mContinue) //从头开始播
					{
						mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
					}
					else
					{
						mVideoView.seekTo(playTime);
					}
				}
			}
			else
			{
				mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
			}
		}
		else
		{
			mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
		}
		
		mp.setOnSeekCompleteListener(new OnSeekCompleteListener() 
		{
			@Override
			public void onSeekComplete(MediaPlayer mp) 
			{
				mHandler.sendEmptyMessage(FlagConstant.PLAY_SEEKED);
			}
		});
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		mp.reset();
		mHandler.sendEmptyMessage(FlagConstant.PLAY_COMPLETE);
	}
	
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) 
	{
		Logcat.d(FlagConstant.TAG, " onInfo: " + what + "," + extra);
		switch(what)
		{
		case 0x12000:
		{
			Logcat.i(FlagConstant.TAG, " +++++0x12000+++++ ");
			if(mLoading.isShow())
			{
				mLoading.hide();
			}
		}
		default:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) 
	{
		Logcat.i(FlagConstant.TAG, "PLAY_ERROR: onError");
		mErrorType = FlagConstant.ERROR_PLAYER; //错误码: 播放器
		mErrorCode = what;
		mErrorExtra = extra;
		mHandler.sendEmptyMessage(FlagConstant.PLAY_ERROR);
		return true;
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case FlagConstant.PLAY_TIMEOUT:
			{
				if (!ActivityPlayVideo.this.isFinishing())
				{
					String code = mVideo.getVideoCode();
					String name = mVideo.getName();
					String classifyCode = mVideo.getClassifyCode();
					String classifyName = mVideo.getClassifyName();
					String dramaCode = mVideo.getDramaList().get(mIndex).getDramaCode();
					String dramaName = mVideo.getDramaList().get(mIndex).getDramaName();
					//点播缓冲时长BI
					BiMsg.sendDemandBufferTimeBiMsg(code, name, classifyCode, classifyName, dramaCode, dramaName, mBufferId);
					
					mHandler.removeMessages(FlagConstant.PLAY_RETASK);
					mHandler.removeMessages(FlagConstant.PLAY_TIMEOUT);
					stopVideoView();
					if (mDialog == null || !mDialog.isShowing())
					{
						mErrorType = FlagConstant.ERROR_TIMEOUT; //错误码: 加载超时
						mErrorCode = -3;
						createDialogPrompt(/*R.drawable.tips_fail, */
		                           getResources().getString(R.string.dialog_load_failed), 
		                           getResources().getString(R.string.dialog_reload), 
		                           getResources().getString(R.string.dialog_cancel));
					}
				}
				break;
			}
			case FlagConstant.PLAY_ERROR:
			{
				if (!ActivityPlayVideo.this.isFinishing())
				{
					String code = mVideo.getVideoCode();
					String name = mVideo.getName();
					String classifyCode = mVideo.getClassifyCode();
					String classifyName = mVideo.getClassifyName();
					String dramaCode = mVideo.getDramaList().get(mIndex).getDramaCode();
					String dramaName = mVideo.getDramaList().get(mIndex).getDramaName();
					//点播缓冲时长BI
					BiMsg.sendDemandBufferTimeBiMsg(code, name, classifyCode, classifyName, dramaCode, dramaName, mBufferId);
					
					mHandler.removeMessages(FlagConstant.PLAY_RETASK);
					mHandler.removeMessages(FlagConstant.PLAY_TIMEOUT);
					stopVideoView();
					if (mDialog == null || !mDialog.isShowing())
					{
						createDialogPrompt(/*R.drawable.tips_fail, */
		                           getResources().getString(R.string.dialog_load_failed), 
		                           getResources().getString(R.string.dialog_reload), 
		                           getResources().getString(R.string.dialog_cancel));
					}
				}
				break;
			}
			case FlagConstant.PLAY_SET_URL:
			{
				mVideoView.setVideoURI(Uri.parse((String) msg.obj));
				break;
			}
			case FlagConstant.PLAY_COMPLETE:
			{
				if (mIndex < mVideo.getDramaList().size()-1) 
				{
					stopVideoView();
					mIndex++;
					sendMsgBroadCast();
					startTask();
				} 
				else 
				{
					//add by zhaoqy 2015-05-25
					if(ReflectUtils.existSetParameterMethod())
					{
						mVideoView.setParameter(0); //退出播放界面
					}
					StaticVariable.gCurPosition = 0;
					stopVideoView();
					savePlayHistory();
					finish();
				}
				break;
			}
			case FlagConstant.PLAY_START:
			{
				String code = mVideo.getVideoCode();
				String name = mVideo.getName();
				String classifyCode = mVideo.getClassifyCode();
				String classifyName = mVideo.getClassifyName();
				String dramaCode = mVideo.getDramaList().get(mIndex).getDramaCode();
				String dramaName = mVideo.getDramaList().get(mIndex).getDramaName();
				//点播缓冲时长BI
				BiMsg.sendDemandBufferTimeBiMsg(code, name, classifyCode, classifyName, dramaCode, dramaName, mBufferId);
				
				mContinue = true;
				StaticVariable.gPrePared = true;
				Logcat.d(FlagConstant.TAG, " PLAY_START gPrePared: " + StaticVariable.gPrePared);
				//add by zhaoqy 2015-05-25
				if(!ReflectUtils.existSetParameterMethod())
				{
					mLoading.hide(); //兼容老版本加载隐藏
				}
				mVideoView.start();
				StaticVariable.gSeeking = false;
				break;
			}
			case FlagConstant.PLAY_SEEKING:
			{
				mVideoView.seekTo(StaticVariable.gCurPosition);
				break;
			}
			case FlagConstant.PLAY_SEEKED:
			{
				if (mLoading.isShow())
				{
					mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
				}
				else
				{
					if (!mStoped)
					{
						mPauseAd.hide();
						if (!mVideoView.isPlaying())  //从暂停状态快进/快退
						{
							mPlay.onKeyPauseToSeek();
						}
						else
						{
							mVideoView.start();
						}
					}
				}
				StaticVariable.gSeeking = false;
				break;
			}
			case FlagConstant.PLAY_AD_END:
			{
				Logcat.d(FlagConstant.TAG, "PLAY_AD_END gPrePared: " + StaticVariable.gPrePared);
				mHandler.removeMessages(FlagConstant.PLAY_START);
				mLoading.hide();
				mVideoView.start();
				StaticVariable.gSeeking = false;
				break;
			}
			case FlagConstant.PLAY_RETASK:
			{
				Logcat.i(FlagConstant.TAG, " 222222 gPlayerRet: " + StaticVariable.gPlayerRet);
				if(StaticVariable.gPlayerRet != 0)
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_RETASK, 500);
				}
				else
				{
					startPPPServerAsyncTask();
				}
				break;
			}
			case FlagConstant.PLAY_START_PPP_PRESERVE:
			{
				mVideoView.pause();
				mPreserve.setBackgroundResource(R.drawable.ppp_preserve);
				mPreserve.invalidate();
				Logcat.i(FlagConstant.TAG, "start to ppp preserve.");
				break;
			}
			case FlagConstant.PLAY_COMPLATE_PPP_PRESERVE:
			{
				mPreserve.setBackground(null);
				mVideoView.start();
				Logcat.i(FlagConstant.TAG, "complete to ppp preserve.");
				break;
			}
			case FlagConstant.PLAY_HIDE_PPP_PRESERVE:
			{
				mPreserve.setBackground(null);
				mRet = -2;
				Logcat.i(FlagConstant.TAG, "hide ppp preserve imageview.");
				break;
			}
			case FlagConstant.PLAY_WANT_EXIT_PLAY:
			{
				mWantExit = false;
				break;
			}
			default:
				break;
			}
		}
	};
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		boolean nRet = false;

		if (mRet == -1)
		{
			mHandler.sendEmptyMessage(FlagConstant.PLAY_HIDE_PPP_PRESERVE);
		}
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyBack();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyEnter();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyMenu();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyRight();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyPageUp();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyPageDown();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyVolumeDown();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyVolumeUp();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyFastForward();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_REWIND && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRewind();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyStop();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyPause();
		}

		if (nRet) 
		{
			return nRet;
		} 
		else 
		{
			return super.dispatchKeyEvent(event);
		}
	}
	
	/**
	 * 
	 * @Title: hide
	 * @Description: 隐藏view
	 * @return: void
	 */
	private void hide()
	{
		if (mLoading.isShow())
		{
			mLoading.hide();
		}
		
		if (mPlay.isShow())
		{
			mPlay.hide();
		}
		
		if (mPauseAd.isShow())
		{
			mPauseAd.hide();
		}
		
		if (mVolume.isShow())
		{
			mVolume.hide();
		}
	}
	
	/**
	 * 
	 * @Title: onSave
	 * @Description: 退出保存
	 * @return: void
	 */
	private void onSave()
	{
		StaticVariable.gCurPosition = mVideoView.getCurrentPosition();
		if ( mTotal-30*1000 > 0 && StaticVariable.gCurPosition > mTotal-30*1000)
		{
			StaticVariable.gCurPosition = mTotal - 30*1000;
		}
		
		stopVideoView();	
		savePlayHistory();
		finish();
	}
	
	/**
	 * 
	 * @Title: onExitPlay
	 * @Description: 退出播放状态
	 * @return: void
	 */
	public void onExitPlay()
	{
		//add by zhaoqy 2015-05-25
		if(ReflectUtils.existSetParameterMethod())
		{
			mVideoView.setParameter(0); //退出播放界面
		}
		sendPlayTimeBi();
		if (mVideoInfo != null)  //有播放历史
		{
			if (!mVideoInfo.getDramaCode().isEmpty() && mVideoInfo.getDramaCode().equals(mVideo.getDramaList().get(mIndex).getDramaCode()))
			{
				//是当前剧集有播放历史, 则不保存
				hide();
				stopVideoView();
				finish();
			}
			else
			{
				//不是当前剧集有播放历史, 则保存
				hide();
				onSave();
			}
		}
		else  //无播放历史, 则保存
		{
			hide();
			onSave();
		}
	}
	
	/**
	 * 
	 * @Title: doKeyBack
	 * @Description: 响应返回键
	 * @return
	 * @return: boolean
	 */
	public boolean doKeyBack()
	{
		if (mMenu.isShow())  //菜单栏可见时
		{
			mMenu.hide();
		}
		else
		{
			if (!mWantExit)
			{
				mPauseAd.hide();
				mPlay.hide();
				mWantExit = true;
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_WANT_EXIT_PLAY, 2500);
				ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_want_to_exit_play));
			}
			else
			{
				mHandler.removeMessages(FlagConstant.PLAY_WANT_EXIT_PLAY);
				if (mLoading.isShow())  //正在加载中
				{
					onExitPlay();
				}
				else   //正在播放中, 则保存
				{
					//add by zhaoqy 2015-05-25
					if(ReflectUtils.existSetParameterMethod())
					{
						mVideoView.setParameter(0); //退出播放界面
					}
					sendPlayTimeBi();
					hide();
					onSave();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: sendPlayTimeBi
	 * @Description: 播放时长
	 * @return: void
	 */
	private void sendPlayTimeBi()
	{
		String type = "1"; //1,点播
		String code = mVideo.getVideoCode();
		String name = mVideo.getName();
		String classifyCode = mVideo.getClassifyCode();
		String classifyName = mVideo.getClassifyName();
		String dramaCode = mVideo.getDramaList().get(mIndex).getDramaCode();
		String dramaName = mVideo.getDramaList().get(mIndex).getDramaName();
		//播放时长BI
		BiMsg.sendPlayTimeBiMsg(type, code, name, classifyCode, classifyName, dramaCode, dramaName, mPlayId);
	}

	/**
	 * 
	 * @Title: doKeyEnter
	 * @Description: 响应OK键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyEnter()
	{
		if (!mLoading.isShow())
		{
			if (mMenu.isShow())  //菜单栏可见时点击OK切换剧集
			{
				//切换剧集
				int tempIndex = mMenu.getPlayIndex();
				if (mIndex == tempIndex)
				{
					mMenu.hide();
				}
				else
				{
					mMenu.hide();
					stopVideoView();
					mIndex = tempIndex;
					sendMsgBroadCast();
					startTask();
				}
			}
			else   //暂停/播放
			{
				if (!StaticVariable.gSeeking)
				{
					mVolume.hide();
					mStoped = false;
					if (mVideoView.isPlaying())
					{
						mPauseAd.show();
					}
					else
					{
						mPauseAd.hide();
					}
					mPlay.onKeyCenter();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyMenu
	 * @Description: 响应菜单键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyMenu()
	{
		if (!mLoading.isShow())
		{
			//剧集数大于1时, 才响应菜单键
			if (mDramaSize > 1)
			{
				if (mMenu.isShow())
				{
					mMenu.hide();
				}
				else 
				{
					mMenu.setPlayIndex(mIndex);
					mMenu.onKeyMenu();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyRight
	 * @Description: 响应右键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyRight()
	{
		if (!mLoading.isShow())
		{
			if (mMenu.isShow())  //菜单栏可见时
			{
				mMenu.onKeyRight();
			}
			else
			{
				if (mPlay.isShow())  //当播放进度栏可见的时候
				{
					mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
					mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
					mStoped = false;
					mPlay.onKeyRight();
				}
				else
				{
					mVolume.hide();
					mPlay.resetTime();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyLeft()
	{
		if (!mLoading.isShow())
		{
			if (mMenu.isShow())  //菜单栏可见时
			{
				mMenu.onKeyLeft();
			}
			else
			{
				if (mPlay.isShow())  //当播放进度栏可见的时候
				{
					mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
					mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
					mStoped = false;
					mPlay.onKeyLeft();
				}
				else
				{
					mVolume.hide();
					mPlay.resetTime();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyPageUp
	 * @Description: 响应上一页键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyPageUp()
	{
		if (mMenu.isShow())
		{
			mMenu.onKeyPageUp();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyPageDown
	 * @Description: 响应下一页键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyPageDown()
	{
		if (mMenu.isShow())
		{
			mMenu.onKeyPageDown();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doVolumeUp
	 * @Description: 响应音量加
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyVolumeUp()
	{
		if (!mLoading.isShow())
		{
			mPlay.hide();
			mMenu.hide();
			mVolume.doVolumeAdd();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyVolumeDown
	 * @Description: 响应音量减
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyVolumeDown()
	{
		if (!mLoading.isShow())
		{
			mPlay.hide();
			mMenu.hide();
			mVolume.doVolumeSub();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyFastForward
	 * @Description: 响应快进键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyFastForward()
	{
		if (!mLoading.isShow())
		{
			mVolume.hide();
			mMenu.hide();
			
			if (mPlay.isShow())  //当播放进度栏可见的时候
			{
				mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
				mStoped = false;
				mPlay.onKeyRight();
			}
			else
			{
				mPlay.resetTime();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyRewind
	 * @Description: 响应快退键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyRewind()
	{
		if (!mLoading.isShow())
		{
			mVolume.hide();
			mMenu.hide();
			
			if (mPlay.isShow())  //当播放进度栏可见的时候
			{
				mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
				mStoped = false;
				mPlay.onKeyLeft();
			}
			else
			{
				mPlay.resetTime();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyStop
	 * @Description: 响应停止键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyStop()
	{
		if (!mLoading.isShow())
		{
			mVolume.hide();
			mMenu.hide();
			
			mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
			StaticVariable.gCurPosition = 0;
			StaticVariable.gSeeking = true;
			mStoped = true;
			mPlay.onKeyStop();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyPause
	 * @Description: 响应暂停键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyPause()
	{
		if (!mLoading.isShow())
		{
			if (!StaticVariable.gSeeking)
			{
				mVolume.hide();
				mMenu.hide();
				
				mStoped = false;
				if (mVideoView.isPlaying())
				{
					mPauseAd.show();
				}
				else
				{
					mPauseAd.hide();
				}
				mPlay.onKeyCenter();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: savePlayHistory
	 * @Description: 保存播放历史(只要进入播放页面, 包括加载画面, 都算有播放历史)
	 * @return: void
	 */
	private void savePlayHistory() 
	{
		Logcat.d(FlagConstant.TAG, "gCurPosition: " + StaticVariable.gCurPosition);
		Logcat.d(FlagConstant.TAG, "111111 mIndex: " + mIndex);
		Logcat.d(FlagConstant.TAG, "dramacode: " + mVideo.getDramaList().get(mIndex).getDramaCode());
		VideoInfo item = new VideoInfo(mVideo, VideoInfoTable.SORT_HISTORY);  //数据库类型：0-收藏; 1-历史
		item.setDramaCode(mVideo.getDramaList().get(mIndex).getDramaCode());
		item.setPlayTime(StaticVariable.gCurPosition);
		VideoInfoTable.insertVideo(item);
	}
	
	/**
	 * 
	 * @Title: createDialogPrompt
	 * @Description: 创建提示对话框
	 * @param resid
	 * @param info
	 * @param sure
	 * @param cancel
	 * @return: void
	 */
	private void createDialogPrompt(/*int resid, */String info, String sure, String cancel)
	{
		String error = "";
		if (mErrorType == FlagConstant.ERROR_LIBRARY || mErrorType == FlagConstant.ERROR_TIMEOUT || mErrorType == FlagConstant.ERROR_NETWORK)
		{
			error = getResources().getString(R.string.dialog_error_code) + ": " + mErrorCode + "(" + mErrorType + ")";
		}
		else if (mErrorType == FlagConstant.ERROR_PLAYER)
		{
			error = getResources().getString(R.string.dialog_error_code) + ": " + mErrorCode + ", " + mErrorExtra + "(" + mErrorType + ")";
		}
		
		mDialog = new DialogPrompt(mContext, /*R.style.dialog_style, resid, */info + "\n" + error, sure, cancel);
		mDialog.setOnClickCustomListener(new onClickCustomListener() 
		{
			@Override
			public void OnClick(View v) 
			{
				switch (v.getId()) 
				{
				case R.id.id_dialog_prompt_sure:
				{
					mDialog.dismiss();
					startTask();
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					onExitPlay();
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
		mDialog.setFlag(FlagConstant.ACTIVITY_PLAY_VIDEO);
		mDialog.setActivity(this);
	}
	
	/**
	 * 
	 * @Title: sendMsgBroadCast
	 * @Description: 发送消息广播(为了返回到详情页面时, 光标放在当前播放剧集上)
	 * @return: void
	 */
	private void sendMsgBroadCast()
	{
		Intent intent = new Intent();
		intent.setAction(Constant.ACTION_FRESH_DRAMA);
		Bundle bundle = new Bundle();
		bundle.putInt(FlagConstant.DramaIndexKey, mIndex);
		intent.putExtras(bundle);
		mContext.sendBroadcast(intent);
		
		StaticVariable.gCurPosition = 0;
		savePlayHistory();
	}
	
	@Override
	protected void onReceive(Intent intent) 
	{
		super.onReceive(intent);
		
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) 
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			
			if ((netInfo == null || !netInfo.isAvailable()) || (netInfo != null && !netInfo.isAvailable())) 
			{
				if (mVideoView.getCurrentPosition() > 0 && mVideoView.isPlaying())
				{
					ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_network_normal));
				}
			}
			else if (netInfo != null && netInfo.isAvailable())
			{
				if (mVideoView.getCurrentPosition() > 0 && mVideoView.isPlaying())
				{
					mVideoView.start();
				}
			}
		}
		else  if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) 
        {
            String reason = intent.getStringExtra("reason");
            
            if (reason != null) 
            {
                if (reason.equals("homekey")) 
                {
                	if (mLoading.isShow())  //正在加载中
        			{
        				onExitPlay();
        			}
        			else   //正在播放中, 则保存
        			{
        				//add by zhaoqy 2015-05-25
        				if(ReflectUtils.existSetParameterMethod())
        				{
        					mVideoView.setParameter(0); //退出播放界面
        				}
        				hide();
        				onSave();
        			}
                } 
            }
        }
		else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) 
		{
			if (mLoading.isShow())  //正在加载中
			{
				onExitPlay();
			}
			else   //正在播放中, 则保存
			{
				//add by zhaoqy 2015-05-25
				if(ReflectUtils.existSetParameterMethod())
				{
					mVideoView.setParameter(0); //退出播放界面
				}
				hide();
				onSave();
			}
		}
		else if (Constant.ACTION_TIMEAUTH_FAIL.equals(intent.getAction())) 
		{
			if (mLoading.isShow())  //正在加载中
			{
				onExitPlay();
			}
			else   //正在播放中, 则保存
			{
				//add by zhaoqy 2015-05-25
				if(ReflectUtils.existSetParameterMethod())
				{
					mVideoView.setParameter(0); //退出播放界面
				}
				hide();
				onSave();
			}
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		if (mPPPServer != null)
		{
			mPPPServer.cancel(true);
			mPPPServer = null;
		}
		
		if(mNetTraffic != null)
		{
			mNetTraffic.stop();
			mNetTraffic = null;
		}
		
		if(mHandler != null)
		{
			mHandler.removeCallbacksAndMessages(null);
		}
	}
}
