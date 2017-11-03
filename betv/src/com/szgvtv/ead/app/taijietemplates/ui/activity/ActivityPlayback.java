/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityPlayback.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 回放播放Activity
 * @author: zhaoqy
 * @date: 2014-8-29 上午11:37:42
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

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
import com.bananatv.custom.networktraffic.NetTraffic;
import com.bananatv.custom.player.PPPServer;
import com.bananatv.custom.player.PlayUriParser.uriCallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.ExitToast;
import com.szgvtv.ead.app.taijietemplates.ui.view.GVideoView;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingVideo;
import com.szgvtv.ead.app.taijietemplates.ui.view.ProgressPlayback;
import com.szgvtv.ead.app.taijietemplates.ui.view.ProgressVolume;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.ReflectUtils;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;

public class ActivityPlayback extends ActivityReceiver implements uriCallBack, OnPreparedListener, OnCompletionListener, OnErrorListener, OnInfoListener
{
	private Context          mContext;        //上下文
	private GVideoView       mVideoView;      //播放器
	private LoadingVideo     mLoading;        //加载
	private ProgressPlayback mPlayback;       //播放进度
	private ProgressVolume   mVolume;         //音量
	private ImageView        mPreserve;       //流媒体维护
	private DialogPrompt     mDialog;         //提示框
	private LivePlaybackItem mLivePlayback;   //直播回放Item
	private LiveChannelItem  mLiveChannel;    //频道Item
	private PPPServerAsync   mPPPServer;      //ppp同步服务
	private NetTraffic       mNetTraffic;     //网络监测
    private boolean          mStoped = false; //是否停止
    private boolean          mWantExit = false; //是否想退出
    private int              mTotal = 0;      //总时间
	private int              mRet = -2;       //jni回调结果
	private int              mPlayId = 0;     //播放时间启动id
	private String           mErrorType = ""; //错误类型
	private int              mErrorCode = -1; //错误码
	private int              mErrorExtra;     //错误附加值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.activity_playback);
		initViews();
		init();
		getPlaybackItem();
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
		mVideoView = (GVideoView) findViewById(R.id.id_playback_videoview);
		mLoading = (LoadingVideo) findViewById(R.id.id_playback_loading_video);
		mPlayback = (ProgressPlayback) findViewById(R.id.id_playback_progress_playback);
		mVolume = (ProgressVolume) findViewById(R.id.id_playback_progress_volume);
		mPreserve = (ImageView) findViewById(R.id.id_ppp_preserve);
	} 
	
	private void init()
	{
		mPlayback.setVideoView(mVideoView);
		mNetTraffic = new NetTraffic();
		mNetTraffic.start();
	}
	
	/**
	 * 
	 * @Title: getPlaybackItem
	 * @Description: 获取回放Item
	 * @return: void
	 */
	private void getPlaybackItem()
	{
		Bundle bundle = getIntent().getExtras();
		mLivePlayback = bundle.getParcelable(FlagConstant.ToActivityLivePlaybacItemkKey);
		mLiveChannel = bundle.getParcelable(FlagConstant.ToActivityLiveChannelItemKey);
		int index = bundle.getInt(FlagConstant.ToActivityPlaybackProgramIndexKey, 0);
		
		mPlayback.setPlaybackItem(mLivePlayback);
		mPlayback.setChannelItem(mLiveChannel);
		mPlayback.setNum(index);
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
		mLoading.setParentHandler(mHandler);
		mLoading.setName(mLivePlayback.getName());
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
			Logcat.i(FlagConstant.TAG, " 111111 gPlayerRet: " + StaticVariable.gPlayerRet);
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
			String url = mLivePlayback.getUrl();
			
			if(!url.isEmpty())
			{
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
			Logcat.i(FlagConstant.TAG, " PLAY_ERROR: jni_callback_found_http");
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
		mPlayback.setTotalTime(mTotal);
		mPlayback.setParentHandler(mHandler);
		mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
		
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
		Logcat.i(FlagConstant.TAG, " PLAY_ERROR: onError");
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
				if (!ActivityPlayback.this.isFinishing())
				{
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
				if (!ActivityPlayback.this.isFinishing())
				{
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
				onExitPlay();
				break;
			}
			case FlagConstant.PLAY_START:
			{
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
						if (!mVideoView.isPlaying())  //从暂停状态快进/快退
						{
							mPlayback.onKeyPauseToSeek();
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
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyRight();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyVolumeDown();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doVolumeUp();
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
		
		if (mPlayback.isShow())
		{
			mPlayback.hide();
		}
		
		if (mVolume.isShow())
		{
			mVolume.hide();
		}
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
		String code = mLivePlayback.getReplaycode();
		String name = mLivePlayback.getName();
		String classifyCode ="";
		String classifyName = "";
		String dramaCode = "";
		String dramaName = "";
		//播放时长BI
		BiMsg.sendPlayTimeBiMsg(type, code, name, classifyCode, classifyName, dramaCode, dramaName, mPlayId);
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
		hide();
		stopVideoView();
		finish();
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
		if (!mWantExit)
		{
			mPlayback.hide();
			mWantExit = true;
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_WANT_EXIT_PLAY, 2500);
			ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_want_to_exit_play));
		}
		else
		{
			mHandler.removeMessages(FlagConstant.PLAY_WANT_EXIT_PLAY);
			onExitPlay();
		}
		return true;
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
			if (!StaticVariable.gSeeking)
			{
				mVolume.hide();
				mStoped = false;
				mPlayback.onKeyCenter();
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
			mVolume.hide();
			
			if (mPlayback.isShow())  //当播放进度栏可见的时候
			{
				mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
				mStoped = false;
				mPlayback.onKeyRight();
			}
			else
			{
				mPlayback.resetTime();
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
			mVolume.hide();
			
			if (mPlayback.isShow())  //当播放进度栏可见的时候
			{
				mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
				mStoped = false;
				mPlayback.onKeyLeft();
			}
			else
			{
				mPlayback.resetTime();
			}
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
	private boolean doVolumeUp()
	{
		if (!mLoading.isShow())
		{
			mPlayback.hide();
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
			mPlayback.hide();
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
		return doKeyRight();
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
		return doKeyLeft();
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
			mHandler.removeMessages(FlagConstant.PLAY_SEEKING);
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_SEEKING, 500);
			StaticVariable.gCurPosition = 0;
			StaticVariable.gSeeking = true;
			mStoped = true;
			mPlayback.onKeyStop();
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
		return doKeyEnter();
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
		mDialog.setFlag(FlagConstant.ACTIVITY_PLAY_BACK);
		mDialog.setActivity(this);
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
                	onExitPlay();
                } 
            }
        }
		else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) 
		{
			onExitPlay();
		}
		else if (Constant.ACTION_TIMEAUTH_FAIL.equals(intent.getAction())) 
		{
			onExitPlay();
		}
	}
}
