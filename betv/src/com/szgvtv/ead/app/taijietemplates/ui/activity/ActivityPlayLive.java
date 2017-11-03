/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityPlayLive.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 直播播放Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:48:39
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
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
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.db.LiveChannelTable;
import com.szgvtv.ead.app.taijietemplates.db.LivePreviewInfoTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.ChannelNumber;
import com.szgvtv.ead.app.taijietemplates.ui.view.ExitToast;
import com.szgvtv.ead.app.taijietemplates.ui.view.GVideoView;
import com.szgvtv.ead.app.taijietemplates.ui.view.LiveChannelInfo;
import com.szgvtv.ead.app.taijietemplates.ui.view.LiveMenu;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingVideo;
import com.szgvtv.ead.app.taijietemplates.ui.view.ProgressVolume;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.ReflectUtils;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;

public class ActivityPlayLive extends ActivityReceiver implements UICallBack, uriCallBack, OnPreparedListener, OnCompletionListener, OnErrorListener, OnInfoListener
{
	private Context                    mContext;           //上下文
	private GVideoView                 mVideoView;         //播放器
	private LoadingVideo               mLoading;           //加载
	private LiveMenu                   mLiveMenu;          //直播菜单
	private LiveChannelInfo            mChannelInfo;       //直播频道信息
	private ProgressVolume             mVolume;            //音量
	private ImageView                  mPreserve;          //流媒体维护
	private ChannelNumber              mChannelNumber;     //频道编码
	private DialogPrompt               mDialog;            //提示框
	private PPPServerAsync             mPPPServer;         //ppp同步服务
	private NetTraffic                 mNetTraffic;        //网络监测
	private ArrayList<LiveChannelItem> mChannels = new ArrayList<LiveChannelItem>(); //直播频道列表
	private ArrayList<LivePreviewItem> mPreviews = new ArrayList<LivePreviewItem>(); //频道预告列表
	private StringBuffer               mBuffer;            //buffer
	private String                     mChannelCode = "";  //频道编码
	private boolean                    mFailed = false;    //是否加载失败
	private boolean                    mCallbacked = true; //回调是否结束
	private boolean                    mUpDown = false;    //是否上下切换过频道信息
    private boolean                    mWantExit = false;  //是否想退出
	private int                        mNoticeType = 0;    //1-菜单频道预告; 2-频道信息预告
	private int                        mRet = -2;          //jni回调结果
	private int                        mTempIndex = 0;     //临时索引
	private int                        mListId = 0;        //直播列表耗时启动id
	private int                        mBufferId = 0;      //直播缓冲时间启动id
	private int                        mPlayId = 0;        //播放时间启动id
	private String                     mErrorType = "";    //错误类型
	private int                        mErrorCode = -1;    //错误码
	private int                        mErrorExtra;        //错误附加值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.activity_play_live);
		initViews();
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
		startLoading();
		
		//先查询数据库是否有频道列表: 频道个数大于0, 则使用该频道列表；若小于0, 则请求频道列表
		checkChannelList();
	}
	
	private void initViews()
	{
		mVideoView = (GVideoView) findViewById(R.id.id_play_live_videoview);
		mLoading = (LoadingVideo) findViewById(R.id.id_play_live_loading);
		mLiveMenu = (LiveMenu) findViewById(R.id.id_play_live_menu);
		mChannelInfo = (LiveChannelInfo) findViewById(R.id.id_play_live_info);
		mVolume = (ProgressVolume) findViewById(R.id.id_play_live_volume);
		mPreserve = (ImageView) findViewById(R.id.id_ppp_preserve);
		mChannelNumber = (ChannelNumber) findViewById(R.id.id_play_live_number);
		
		mNetTraffic = new NetTraffic();
		mNetTraffic.start();
	}
	
	/**
	 * 
	 * @Title: checkChannelList
	 * @Description: 检测频道列表
	 * @return: void
	 */
	private void checkChannelList()
	{
		mChannels.clear();
		mChannels = LiveChannelTable.queryAllLiveChannels();
		if (mChannels.size() > 0)
		{
			getChannelCode();
			startTask();
			
			//30秒后再从后台请求直播频道列表
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_REQUEST_CHANNEL, 30*1000);
		}
		else
		{
			requestChannelList();
		}
	}
	
	/**
	 * 
	 * @Title: requestTVList
	 * @Description: 请求频道列表
	 * @return: void
	 */
	private void requestChannelList()
	{
		mListId = BI.getStartTimeId();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_TV_LIST, 0, 0);  
	}
	
	/**
	 * 
	 * @Title: checkChannelPreview
	 * @Description: 检测频道预告
	 * @param code
	 * @return: void
	 */
	private void checkChannelPreview(String code)
	{
		Logcat.d(FlagConstant.TAG, " input_code: " + code);
		mPreviews.clear();
		mPreviews = LivePreviewInfoTable.queryPreviewByCode(code);
		Logcat.d(FlagConstant.TAG, " preview_size: " + mPreviews.size());
		if (mPreviews.size() > 0)
		{
			freshPreviewList(code);
		}
		else
		{
			if (mNoticeType == 1)  //1-菜单频道预告
			{
				mLiveMenu.clearPreviewList();
			}
			else if (mNoticeType == 2)  //2-频道信息预告
			{
				mChannelInfo.clearPreviewList();
			}
			
			mHandler.removeMessages(FlagConstant.PLAY_REQUEST_PREVIEW);
			Message msg = new Message();
			msg.what = FlagConstant.PLAY_REQUEST_PREVIEW;
			msg.obj = code;
			mHandler.sendMessageDelayed(msg, 1000);
		}
	}
	
	/**
	 * 
	 * @Title: requestPreview
	 * @Description: 请求节目预告
	 * @return: void
	 */
	private void requestPreview(String code)
	{
		RequestDataManager.requestData(this, mContext, Token.TOKEN_TV_NOTICE, 10, 0, code);  
	}
	
	/**
	 * 
	 * @Title: getChannelCode
	 * @Description: 获取频道编码
	 * @return: void
	 */
	private void getChannelCode()
	{
		//播放上次退出时的频道
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		mChannelCode = sp.getString("code", "");
		//第一次进入直播, 没有保存记录, 此时播放第一个频道
		if (mChannelCode.isEmpty() && mChannels.size() > 0)
		{
			mChannelCode = mChannels.get(0).getTvCode();
		}
	}
	
	/**
	 * 
	 * @Title: getChannelIndex
	 * @Description: 根据频道编码, 获取索引值
	 * @return
	 * @return: int
	 */
	private int getChannelIndex()
	{
		int index = 0;
		if (!mChannelCode.isEmpty())
		{
			for (int i=0; i<mChannels.size(); i++)
			{
				if (mChannelCode.equals(mChannels.get(i).getTvCode()))
				{
					index = i;
				}
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @Title: freshPreviewList
	 * @Description: 刷新预告列表
	 * @param channel_code
	 * @return: void
	 */
	private void freshPreviewList(String channel_code)
	{
		String tempCode = mChannels.get(mTempIndex).getTvCode();
		
		if (tempCode.equals(channel_code))
		{
			if (mNoticeType == 1)       //1-菜单频道预告
			{
				if (mLiveMenu.isShow())
				{
					mLiveMenu.setPreviewList(mPreviews);
				}
			}
			else if (mNoticeType == 2)  //2-频道信息预告
			{
				if (mChannelInfo.isShow())
				{
					mChannelInfo.setPreviewList(mPreviews);
					mChannelInfo.show();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: startTask
	 * @Description: 开始任务
	 * @return: void
	 */
	public void startTask() 
	{
		mLoading.setName(mChannels.get(getChannelIndex()).getTvName());
		if (!mCallbacked)
		{
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_TASK, 1000);
			return;
		}
		
		mHandler.removeMessages(FlagConstant.PLAY_SET_URL);
		mHandler.removeMessages(FlagConstant.PLAY_TASK);
		mTempIndex = getChannelIndex();
		mCallbacked = false;
		mFailed = false;
		mPreviews.clear();
		StaticVariable.gPrePared = false;
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
			Logcat.i(FlagConstant.TAG, " 1111111 gPlayerRet: " + StaticVariable.gPlayerRet);
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
			String url = mChannels.get(getChannelIndex()).getTvUrl();
			
			if(!url.isEmpty())
			{
				mBufferId = BI.getStartTimeId();
				startPPPServer(url);
			}
			return null;
		}
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

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		mp.reset();
	}

	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		//add by zhaoqy 2015-05-25
		if(ReflectUtils.existSetParameterMethod())
		{
			mVideoView.setParameter(1);
		}	
		mHandler.sendEmptyMessage(FlagConstant.PLAY_START);
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
			mCallbacked = true;
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
	public void onCancel(OutPacket out, int token) 
	{
		onNetError(-1, "error", null, token);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccessful(Object in, int token) 
	{
		try 
		{
			switch (token) 
			{
			case Token.TOKEN_TV_LIST:
			{
				//直播列表耗时BI
				BiMsg.sendLiveListTimeBiMsg(mListId);
				
				ArrayList<LiveChannelItem> temp = new ArrayList<LiveChannelItem>(); 
				temp = (ArrayList<LiveChannelItem>) RequestDataManager.getData(in);
				
				if (mChannels.size() > 0)
				{
					//从数据库中取到了直播频道列表
					//此时从后台取到的直播频道列表写到数据库中, 下次进入直播界面时, 更新该列表
					LiveChannelTable.insertChannelList(temp);
				}
				else
				{
					//数据库中的直播频道个数为0
					for (int i=0; i<temp.size(); i++)
					{
						LiveChannelItem item = temp.get(i);
						mChannels.add(item);
					}
					
					int tvlist_size = mChannels.size();
					Logcat.d(FlagConstant.TAG, " tvlist_size: " + tvlist_size);
					if (tvlist_size > 0)
					{
						getChannelCode();
						startTask();
						
						//此时要插入到数据库中
						LiveChannelTable.insertChannelList(temp);
					}
					else
					{
						onNetError(-1, "error", null, token);
					}
				}
				break;
			}
			case Token.TOKEN_TV_NOTICE:
			{
				mPreviews.clear();
				ArrayList<LivePreviewItem> temp = new ArrayList<LivePreviewItem>(); 
				temp = (ArrayList<LivePreviewItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					LivePreviewItem item = temp.get(i);
					mPreviews.add(item);
				}
				
				if (mPreviews.size() > 0)
				{
					String channel_code = RequestDataManager.getChannelCode(in);
					Logcat.d(FlagConstant.TAG, " channel_code: " + channel_code);
					freshPreviewList(channel_code);
					LivePreviewInfoTable.insertPreviews(mPreviews, channel_code);
				}
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, " ===ActivityPlayLive==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		switch (token) 
		{
		case Token.TOKEN_TV_LIST:
		{
			//进入直播界面, 数据库中没有直播频道列表
			if (mChannels.size() <= 0)
			{
				//直播列表耗时BI
				BiMsg.sendLiveListTimeBiMsg(mListId);
				
				Logcat.i(FlagConstant.TAG, " PLAY_ERROR: onNetError");
				mErrorType = FlagConstant.ERROR_NETWORK; //错误码: 网络异常
				mErrorCode = -2;
				mHandler.sendEmptyMessage(FlagConstant.PLAY_ERROR);
			}
			break;
		}
		default:
			break;
		}
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
				if (mChannels.size() > 0)
				{
					//直播缓冲时间BI
					String tvName = mChannels.get(getChannelIndex()).getTvName();
					BiMsg.sendLiveBufferTimeBiMsg(mChannelCode, tvName, mBufferId);
				}
				
				if (!ActivityPlayLive.this.isFinishing())
				{
					if (!mFailed)
					{
						mHandler.removeMessages(FlagConstant.PLAY_RETASK);
						mHandler.removeMessages(FlagConstant.PLAY_TIMEOUT);
						stopVideoView();
						mFailed = true;
						mCallbacked = true;
						
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
				}
				break;
			}
			case FlagConstant.PLAY_ERROR:
			{
				if (mChannels.size() > 0)
				{
					//直播缓冲时间BI
					String tvName = mChannels.get(getChannelIndex()).getTvName();
					BiMsg.sendLiveBufferTimeBiMsg(mChannelCode, tvName, mBufferId);
				}
				
				if (!ActivityPlayLive.this.isFinishing())
				{
					if (!mFailed)
					{
						mHandler.removeMessages(FlagConstant.PLAY_RETASK);
						mHandler.removeMessages(FlagConstant.PLAY_TIMEOUT);
						stopVideoView();
						mFailed = true;
						mCallbacked = true;
						
						if (mDialog == null || !mDialog.isShowing())
						{
							createDialogPrompt(/*R.drawable.tips_fail, */
			                           getResources().getString(R.string.dialog_load_failed), 
			                           getResources().getString(R.string.dialog_reload), 
			                           getResources().getString(R.string.dialog_cancel));
						}
					}
				}
				break;
			}
			case FlagConstant.PLAY_SET_URL:
			{
				Logcat.i(FlagConstant.TAG, " PLAY_SET_URL ");
				mVideoView.setVideoURI(Uri.parse((String) msg.obj));
				break;
			}
			case FlagConstant.PLAY_START:
			{
				if (mChannels.size() > 0)
				{
					//直播缓冲时间BI
					String tvName = mChannels.get(getChannelIndex()).getTvName();
					BiMsg.sendLiveBufferTimeBiMsg(mChannelCode, tvName, mBufferId);
				}
				
				StaticVariable.gPrePared = true;
				Logcat.d(FlagConstant.TAG, " PLAY_START gPrePared: " + StaticVariable.gPrePared);
				//add by zhaoqy 2015-05-25
				if(!ReflectUtils.existSetParameterMethod())
				{
					mLoading.hide(); //兼容老版本加载隐藏
				}
				mVideoView.start();
				break;
			}
			case FlagConstant.PLAY_AD_END:
			{
				Logcat.d(FlagConstant.TAG, " PLAY_AD_END gPrePared: " + StaticVariable.gPrePared);
				mHandler.removeMessages(FlagConstant.PLAY_START);
				mLoading.hide();
				mVideoView.start();
				break;
			}
			case FlagConstant.PLAY_SWITCH:
			{
				mLiveMenu.hide();
				mVolume.hide();
				mChannelInfo.hide();
				mBuffer = mChannelNumber.getBuffer();
				if (mBuffer != null)
				{
					//切换频道
					int index = Integer.parseInt(mBuffer.toString())-1;
					if (index >= 0 && index <= mChannels.size()-1)
					{
						switchChannel(index);
					}
				}
				break;
			}
			case FlagConstant.PLAY_TASK:
			{
				startTask();
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
				Logcat.i(FlagConstant.TAG, " start to ppp preserve.");
				break;
			}
			case FlagConstant.PLAY_COMPLATE_PPP_PRESERVE:
			{
				mPreserve.setBackground(null);
				mVideoView.start();
				Logcat.i(FlagConstant.TAG, " complete to ppp preserve.");
				break;
			}
			case FlagConstant.PLAY_HIDE_PPP_PRESERVE:
			{
				mPreserve.setBackground(null);
				mRet = -2;
				Logcat.i(FlagConstant.TAG, " hide ppp preserve imageview.");
				break;
			}
			case FlagConstant.PLAY_CHECK_PREVIEW:
			{
				String channelCode = mChannels.get(mTempIndex).getTvCode();
				checkChannelPreview(channelCode);
				break;
			}
			case FlagConstant.PLAY_REQUEST_CHANNEL:
			{
				requestChannelList();
				break;
			}
			case FlagConstant.PLAY_REQUEST_PREVIEW:
			{
				String code = (String) msg.obj;
				requestPreview(code);
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
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyUp();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyDown();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyPageUp();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyPageDown();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet =  doVolumeUp();
		} 
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyVolumeDown();
		} 
		else if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9  && event.getAction() == KeyEvent.ACTION_DOWN)  
		{
			int code = event.getKeyCode() - KeyEvent.KEYCODE_0;
			nRet = doKeyDigitalNumber(code);
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
		
		if (mChannelInfo.isShow())
		{
			mChannelInfo.hide();
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
		if (mChannels.size() > 0)
		{
			String type = "0"; //0，直播；
			String code = mChannelCode;
			String name = mChannels.get(getChannelIndex()).getTvName();
			String classifyCode = "";
			String classifyName = "";
			String dramaCode = "";
			String dramaName = "";
			//播放时长BI
			BiMsg.sendPlayTimeBiMsg(type, code, name, classifyCode, classifyName, dramaCode, dramaName, mPlayId);
		}
		else
		{
			BiMsg.sendPlayTimeBiMsg("0", "", "", "", "", "", "", mPlayId);
		}
	}
	
	/**
	 * 
	 * @Title: onExitPlay
	 * @Description: 退出播放, 保存状态
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
		saveChannelCode();
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
		if (mLiveMenu.isShow())
		{
			mLiveMenu.hide();
		}
		else
		{
			if (!mWantExit)
			{
				mChannelInfo.hide();
				mWantExit = true;
				mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_WANT_EXIT_PLAY, 2500);
				ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_want_to_exit_play));
			}
			else
			{
				mHandler.removeMessages(FlagConstant.PLAY_WANT_EXIT_PLAY);
				onExitPlay();
			}
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
		if (mLoading.isShow())
		{
			//进入直播界面, 频道列表还没有取到时, 按OK键, 不响应; 否则, 索引异常, 导致程序崩溃.(add 2014-12-06)
			if (mChannels.size() <= 0)
			{
				return true;
			}
		}
		
		if (mLiveMenu.isShow())
		{
			if (mChannelNumber.isBufferNull())
			{
				//切换频道
				int index = mLiveMenu.getChannelIndex();
				switchChannel(index);
			}
			return true;
		}
		else if (mVolume.isShow())
		{
			mVolume.hide();
		}
		else if (mChannelInfo.isShow())
		{
			int index = getChannelIndex();
			Logcat.d(FlagConstant.TAG, "index: " + index);
			Logcat.d(FlagConstant.TAG, "mTempIndex: " + mTempIndex);
			if (mTempIndex != index)
			{
				if (!mUpDown)
				{
					mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
				}
				switchChannelForOK(mTempIndex);
			}
			else
			{
				mChannelInfo.show();
			}
			return true;
		}
		
		int index = getChannelIndex();
		String name = mChannels.get(index).getTvName();
		mChannelInfo.setChannelInfo(index, name);
		mChannelInfo.show();
		
		if (mTempIndex == getChannelIndex())
		{
			if (!mPreviews.isEmpty())
			{
				mChannelInfo.setPreviewList(mPreviews);
			}
			else
			{
				//取预告
				mTempIndex = getChannelIndex();
				mNoticeType = 2;
				mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);;
			}
		}
		else
		{
			//取预告
			mTempIndex = getChannelIndex();
			mNoticeType = 2;
			mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
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
		if (mLoading.isShow())
		{
			//进入直播界面, 频道列表还没有取到时, 按菜单键键, 不响应; 否则, 索引异常, 导致程序崩溃.(add 2014-12-06)
			if (mChannels.size() <= 0)
			{
				return true;
			}
		}	
		
		if (mLiveMenu.isShow())
		{
			mLiveMenu.hide();
			return true;
		}
		else if (mVolume.isShow())
		{
			mVolume.hide();
		}
		else if (mChannelInfo.isShow())
		{
			mChannelInfo.hide();
		}
		
		mLiveMenu.setChannelList(mChannels);
		mLiveMenu.setChannelCode(mChannelCode);
		mLiveMenu.show();
		
		if (mTempIndex == getChannelIndex())
		{
			if (!mPreviews.isEmpty())
			{
				mLiveMenu.setPreviewList(mPreviews);
			}
			else
			{
				//取预告
				int index = getChannelIndex();
				mTempIndex = index;
				mNoticeType = 1;
				mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
			}
		}
		else
		{
			//取预告
			int index = getChannelIndex();
			mTempIndex = index;
			mNoticeType = 1;
			mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyUp
	 * @Description: 响应上键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyUp()
	{
		if (mLoading.isShow())
		{
			//进入直播界面, 频道列表还没有取到时, 按上键, 不响应; 否则, 索引异常, 导致程序崩溃.(add 2014-12-06)
			if (mChannels.size() <= 0)
			{
				return true;
			}
		}		
		
		if (mLiveMenu.isShow())
		{
			if (mLiveMenu.onKeyUp())
			{
				getMenuPreview();
			}
		}
		else
		{
			int index = 0;
			
			if (mVolume.isShow())
			{
				mVolume.hide();
			}
			
			if (mChannelInfo.isShow())
			{
				index = mTempIndex;
			}	
			else
			{
				index = getChannelIndex();
			}
			
			index--;
			if (index < 0)
			{
				index = mChannels.size()-1;
			}
			
			mTempIndex = index;
			String name = mChannels.get(index).getTvName();
			mChannelInfo.setChannelInfo(index, name);
			mChannelInfo.show();
			//取预告
			mUpDown = true;
			mNoticeType = 2;
			mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyDown
	 * @Description: 响应下键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyDown()
	{
		if (mLoading.isShow())
		{
			//进入直播界面, 频道列表还没有取到时, 按下键, 不响应; 否则, 索引异常, 导致程序崩溃.(add 2014-12-06)
			if (mChannels.size() <= 0)
			{
				return true;
			}
		}		
				
		if (mLiveMenu.isShow())
		{
			if (mLiveMenu.onKeyDown())
			{
				getMenuPreview();
			}
		}
		else
		{
			int index = 0;
			
			if (mVolume.isShow())
			{
				mVolume.hide();
			}
			
			if (mChannelInfo.isShow())
			{
				index = mTempIndex;
			}	
			else
			{
				index = getChannelIndex();
			}
			
			index++;
			if (index > mChannels.size()-1)
			{
				index = 0;
			}
			
			mTempIndex = index;
			String name = mChannels.get(index).getTvName();
			mChannelInfo.setChannelInfo(index, name);
			mChannelInfo.show();
			//取预告
			mUpDown = true;
			mNoticeType = 2;
			mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyPageUp
	 * @Description: 响应上一页
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyPageUp()
	{
		if (mLiveMenu.isShow())
		{
			if (mLiveMenu.onKeyPageUp())
			{
				getMenuPreview();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyPageDown
	 * @Description: 响应下一页
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyPageDown()
	{
		if (mLiveMenu.isShow())
		{
			if (mLiveMenu.onKeyPageDown())
			{
				getMenuPreview();
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
		if (mLiveMenu.isShow())
		{
			mLiveMenu.hide();
		}
		else if (mChannelInfo.isShow())
		{
			mChannelInfo.hide();
		}
		return mVolume.doVolumeAdd();
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
		if (mLiveMenu.isShow())
		{
			mLiveMenu.hide();
		}
		else if (mChannelInfo.isShow())
		{
			mChannelInfo.hide();
		}
		return mVolume.doVolumeSub();
	}
	
	/**
	 * 
	 * @Title: doKeyDigitalNumber
	 * @Description: 响应数字键(0-9)
	 * @param code
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyDigitalNumber(int code)
	{
		if (!mLoading.isShow())
		{
			if (mChannelNumber.getParentHandler() == null)
			{
				mChannelNumber.setParentHandler(mHandler);
			}
		    mChannelNumber.doKeyDigitalNumber(code);
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: saveChannelCode
	 * @Description: 保存退出前正在播放的频道编码
	 * @return: void
	 */
	@SuppressLint("CommitPrefEdits")
	private void saveChannelCode()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("code", mChannelCode);
		editor.commit();
	}
	
	/**
	 * 
	 * @Title: switchChannel
	 * @Description: 切换频道
	 * @param index
	 * @return: void
	 */
	private void switchChannel(int index)
	{
		String code = mChannels.get(index).getTvCode();
		Logcat.d(FlagConstant.TAG, "code: " + code);
		Logcat.d(FlagConstant.TAG, "mChannelCode: " + mChannelCode);
		
		if (!code.equals(mChannelCode) || (code.equals(mChannelCode) && mFailed))
		{
			mTempIndex = index;
			mChannelCode = code;
			mLiveMenu.hide();
			mVideoView.stopPlayback();
			mVideoView.destroyDrawingCache();
			startLoading();
			mLoading.setName(mChannels.get(getChannelIndex()).getTvName());
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_TASK, 2000);
		}
	}
	
	/**
	 * 
	 * @Title: switchChannelForOK
	 * @Description: 上下键切换频道信息, 按OK键切换频道
	 * @param index
	 * @return: void
	 */
	private void switchChannelForOK(int index)
	{
		String code = mChannels.get(index).getTvCode();
		mChannelCode = code;
		mLiveMenu.hide();
		mChannelInfo.hide();
		mVideoView.stopPlayback();
		mVideoView.destroyDrawingCache();
		startLoading();
		mLoading.setName(mChannels.get(getChannelIndex()).getTvName());
		startTask();
	}
	
	/**
	 * 
	 * @Title: getMenuPreview
	 * @Description: 获取菜单预告
	 * @return: void
	 */
	private void getMenuPreview()
	{
		int index = mLiveMenu.getChannelIndex();
		mTempIndex = index;
		mNoticeType = 1;
		mHandler.sendEmptyMessage(FlagConstant.PLAY_CHECK_PREVIEW);
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
					startLoading();
					
					int size = mChannels.size();
					if (size > 0)   //频道列表个数大于0, 则重新加载该频道资源
					{
						startTask();
					}
					else            //频道列表个数小于或等于0, 则重新加载频道列表
					{
						mFailed = false;
						requestChannelList();
					}
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					int size = mChannels.size();
					if (size <= 0)  //频道列表个数小于或等于0, 则退出播放界面
					{
						//add by zhaoqy 2015-05-25
						if(ReflectUtils.existSetParameterMethod())
						{
							mVideoView.setParameter(0); //退出播放界面
						}
						sendPlayTimeBi();
						stopVideoView();
						finish();
					}
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
		mDialog.setFlag(FlagConstant.ACTIVITY_PLAY_LIVE);
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
				if (mVideoView.isPlaying())
				{
					ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_network_normal));
				}
			}
			else if (netInfo != null && netInfo.isAvailable())
			{
				if (mVideoView.isPlaying())
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
