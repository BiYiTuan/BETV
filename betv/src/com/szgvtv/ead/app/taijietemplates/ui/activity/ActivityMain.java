/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityMain.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 首页Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:53:39
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bananatv.custom.player.PPPServer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HomeRecommendItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.db.AppRecdInfo;
import com.szgvtv.ead.app.taijietemplates.db.AppRecdInfoTable;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfo;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfoTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.service.timeAuth.TimeAuthService;
import com.szgvtv.ead.app.taijietemplates.service.upgrade.UpgradeModelService;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogQuestionnaire;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.ExitToast;
import com.szgvtv.ead.app.taijietemplates.ui.view.HomeItem;
import com.szgvtv.ead.app.taijietemplates.ui.view.HomeSortInflectItem;
import com.szgvtv.ead.app.taijietemplates.ui.view.HomeSortItem;
import com.szgvtv.ead.app.taijietemplates.ui.view.StartUpScreen;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.ConvertViewToBitmap;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.GenerateBitmap;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;

public class ActivityMain extends Activity implements UICallBack, OnClickListener, onClickCustomListener,  OnFocusChangeListener, AnimationListener
{
	private static final int HOME_ITEM = 1;
	private static final int HOME_SORT_ITEM = 2;
	private Context                      mContext;                                       //上下文
	private HorizontalScrollView         mHs;                                            //横向滚动条
	private StartUpScreen                mStartUp;                                       //应用启动页item
	private ImageView                    mFocus;                                         //放大Image
	private ImageView                    mInflects[] = new ImageView[6];                 //倒影
	private HomeItem                     mHomeItems[] = new HomeItem[10];                //首页Item
	private RelativeLayout               mHomeSort;                                      //首页分类
	private RelativeLayout               mHomeSortInflect;                               //首页分类倒影
	private TextView                     mAppVersion;                                    //应用版本号
	private Animation                    mScaleBig;                                      //放大动画
	private Animation                    mScaleSmall;                                    //缩小动画
	private ArrayList<HomeRecommendItem> mRecds = new ArrayList<HomeRecommendItem>();    //推荐列表
	private ArrayList<HomeRecommendItem> mVodSorts = new ArrayList<HomeRecommendItem>(); //点播分类列表
	private ArrayList<HomeRecommendItem> mVodRecds = new ArrayList<HomeRecommendItem>(); //点播推荐列表
	private ArrayList<VideoItem>         mVideos = new ArrayList<VideoItem>();           //点播资源列表
	private int                          mFocusType = 0;                                 //选中类型:1-HOME_ITEM；2-HOME_SORT_ITEM
	private int                          mStartTimeId = 0;                               //BI启动id
	private int                          mTime = 0;                                      //请求次数
	private int                          mRecdTime = 0;
	private boolean                      mFinished = false;                              //开机动画结束
	private boolean                      mExit = false;                                  //是否已经退出应用
	private boolean                      mWantExit = false;                              //是否想退出
	private GenerateBitmap               mGenerateBitmap0;                               //生成Bitmap的线程0
	private GenerateBitmap               mGenerateBitmap1;                               //生成Bitmap的线程1
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		
		initRegister();
		initViews();
		new Thread(Init).start();
		mHandler.sendEmptyMessageDelayed(FlagConstant.HOME_STARTUP_FINISH, 1000);  
		requestAppRecd();
		startTimeAuthService();
	}
	
	//注册待机广播(响应按"待机"键时, 退出应用的操作)
	private void initRegister()
	{
		IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction("android.intent.action.SCREEN_OFF"); 
	    intentFilter.addAction(Constant.ACTION_TIMEAUTH_FAIL);  
	    registerReceiver(mBroadcastReceiver, intentFilter);
	}
	
	protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() 
	{
		@Override
	    public void onReceive(Context context, Intent intent) 
	    {
			BiMsg.sendAppExitBiMsg(mContext);
			//响应待机键广播 退出应用
			finish();
		}
	};
	
	private void initViews()
	{
		mHs = (HorizontalScrollView) findViewById(R.id.id_hs);
		mStartUp = (StartUpScreen) findViewById(R.id.id_home_startup);
		mFocus = (ImageView) findViewById(R.id.id_home_focus);
		mInflects[0] = (ImageView) findViewById(R.id.id_home_playback_inflection);
		mInflects[1] = (ImageView) findViewById(R.id.id_home_search_inflection);
		mInflects[2] = (ImageView) findViewById(R.id.id_home_history_inflection);
		mInflects[3] = (ImageView) findViewById(R.id.id_home_favorite_inflection);
		mInflects[4] = (ImageView) findViewById(R.id.id_home_recommend1_inflection);
		mInflects[5] = (ImageView) findViewById(R.id.id_home_recommend2_inflection);
		mHomeSort = (RelativeLayout) findViewById(R.id.id_home_sort);
		mHomeSortInflect = (RelativeLayout) findViewById(R.id.id_home_sort_inflection);
		mHomeItems[0] = (HomeItem) findViewById(R.id.id_home_tv);
		mHomeItems[1] = (HomeItem) findViewById(R.id.id_home_moive);
		mHomeItems[2] = (HomeItem) findViewById(R.id.id_home_live);
		mHomeItems[3] = (HomeItem) findViewById(R.id.id_home_special);
		mHomeItems[4] = (HomeItem) findViewById(R.id.id_home_playback);
		mHomeItems[5] = (HomeItem) findViewById(R.id.id_home_search);
		mHomeItems[6] = (HomeItem) findViewById(R.id.id_home_history);
		mHomeItems[7] = (HomeItem) findViewById(R.id.id_home_favorite);
		mHomeItems[8] = (HomeItem) findViewById(R.id.id_home_recommend1);
		mHomeItems[9] = (HomeItem) findViewById(R.id.id_home_recommend2);
		mAppVersion = (TextView) findViewById(R.id.id_app_version);
		
		for (int i=0; i<10; i++)
		{
			mHomeItems[i].setOnFocusChangeListener(this);
			mHomeItems[i].setOnClickListener(this);
			mHomeItems[i].setVisibility(View.INVISIBLE);
		}
		mVideos.clear();
	}
	
	/**
	 * 初始化各种服务
	 */
	private Runnable Init = new Runnable() 
	{
		public void run() 
		{
			StaticVariable.initStaticValue();
			UILApplication.initService(getApplicationContext());  //初始化各种服务
			mStartTimeId = BI.getStartTimeId();
		}
	};
	
	/**
	 * 初始化ppp流媒体(把流媒体的初始化跟其它服务初始化分开做, 是为了快速进入应用首页, 因为流媒体的初始化操作是一个阻塞的过程)
	 */
	private Runnable InitPPP = new Runnable() 
	{
		public void run() 
		{
			PPPServer.initPPPServer(getApplicationContext()); //初始化ppp服务
		}
	};
	
	/**
	 * 
	 * @Title: requestAppRecd
	 * @Description: 请求应用首页推荐
	 * @return: void
	 */
	private void requestAppRecd()
	{
		mRecdTime++;
		RequestDataManager.requestData(this, mContext, Token.TOKEN_APP_RECD, 0, 0);  
	}
	
	/**
	 * 
	 * @Title: requestVideo
	 * @Description: 请求点播资源详情
	 * @param code
	 * @return: void
	 */
	private void requestVideo(String code)
	{
		RequestDataManager.requestData(this, mContext, Token.TOKEN_VIDEO, 0, 0, code);  
	}

	@Override
	public void onAnimationStart(Animation animation) 
	{
	}

	@Override
	public void onAnimationEnd(Animation animation) 
	{
		if (animation == mScaleBig) 
		{
			View view = getCurrentFocus();
			Util.amplifyItem(view, mFocus, 0.20);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) 
	{
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) 
	{
		if (!hasFocus) 
		{
			mFocus.setVisibility(View.INVISIBLE);
			mScaleSmall = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_HOME_SMALL_ANIMS);
			mScaleSmall.setFillAfter(false);
			mScaleSmall.setAnimationListener(this);
			v.startAnimation(mScaleSmall);
		}
		else
		{
			mScaleBig = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_HOME_BIG_ANIMS);
			mScaleBig.setFillAfter(true);
			mScaleBig.setAnimationListener(this);
			v.startAnimation(mScaleBig);
			v.bringToFront();
			if (mFocusType == HOME_SORT_ITEM)
			{
				mHomeSort.bringToFront();
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_home_tv:
		{
			if (mVodSorts.size() > 0)
			{
				enterClassifyVideo(0);
			}
			else
			{
				Intent intent = new Intent(this, ActivityClassifyVideo.class);
				intent.putExtra(FlagConstant.ClassifyCodeKey, getCode(0));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		}
		case R.id.id_home_moive:
		{
			if (mVodSorts.size() > 1)
			{
				enterClassifyVideo(1);
			}
			else
			{
				Intent intent = new Intent(this, ActivityClassifyVideo.class);
				intent.putExtra(FlagConstant.ClassifyCodeKey, getCode(1));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		}
		case R.id.id_home_live:
		{
			Intent intent = new Intent(this, ActivityPlayLive.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.id_home_special:  
		{
			if (mVodSorts.size() > 2)
			{
				enterClassifyVideo(2);
			}
			else
			{
				Intent intent = new Intent(this, ActivityClassifyVideo.class);
				intent.putExtra(FlagConstant.ClassifyCodeKey, getCode(2));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		}
		case R.id.id_home_playback:
		{
			Intent intent = new Intent(this, ActivityPlaybackChannel.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.id_home_search:
		{
			Intent intent = new Intent(this, ActivitySearch.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.id_home_history:
		{
			Intent intent = new Intent(this, ActivityHistory.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.id_home_favorite:
		{
			Intent intent = new Intent(this, ActivityFavorite.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case R.id.id_home_recommend1:
		{
			if (mVideos.size() > 0)
			{
				enterDetail(0);
			}
			break;
		}
		case R.id.id_home_recommend2:
		{
			if (mVideos.size() > 1)
			{
				enterDetail(1);
			}
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: enterDetail
	 * @Description: 进入详情
	 * @param index
	 * @return: void
	 */
	private void enterDetail(int index)
	{
		VideoItem item = mVideos.get(index);
		int dramas = item.getDramaList().size();
		Logcat.d(FlagConstant.TAG, " name: " + item.getName());
		Logcat.d(FlagConstant.TAG, " dramas: " + dramas);
		//只有剧集个数大于0的时候, 才可以进入详情界面
		if (dramas > 0)
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("2"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
			//资源类型字段：1-电影；2-电视剧；3-综艺;
			int type = Integer.parseInt(item.getVodtype());
			Logcat.d(FlagConstant.TAG, " type: " + type);
			
			switch (type) 
			{
			case 1:
			{
				Intent intent = new Intent(this, ActivityDetailFilm.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(FlagConstant.ToActivityDetailFilmKey, item);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
			case 2:
			case 3:
			{
				Intent intent = new Intent(this, ActivityDetailVideo.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(FlagConstant.ToActivityDetailVideoKey, item);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}	
			default:
				break;
			}
		}
	}
	/**
	 * 
	 * @Title: enterClassifyVideo
	 * @Description: 进入分类下资源
	 * @param index
	 * @return: void
	 */
	private void enterClassifyVideo(int index)
	{
		HomeRecommendItem item = mVodSorts.get(index);
		String code = item.getCode();
		Intent intent = new Intent(this, ActivityClassifyVideo.class);
		intent.putExtra(FlagConstant.ClassifyCodeKey, code);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@SuppressLint("UseValueOf")
	@Override
	public void OnClick(View v) 
	{
		int tag = new Integer(v.getTag().toString());
		//enterClassifyVideo(tag+2);
		enterClassifyVideo(tag+3);
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
			case Token.TOKEN_APP_RECD:
			{
				mRecds.clear();
				ArrayList<HomeRecommendItem> temp = new ArrayList<HomeRecommendItem>(); 
				temp = (ArrayList<HomeRecommendItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					HomeRecommendItem item = temp.get(i);
					mRecds.add(item);
				}
				freshRecdList();
				break;
			}
			case Token.TOKEN_VIDEO:
			{
				VideoItem item = (VideoItem) RequestDataManager.getData(in);
				if (item.getVodtype().equals("3"))
				{
					ArrayList<DramaItem> tempList = new ArrayList<DramaItem>(); 
					int size = item.getDramaList().size();
					for (int j=size-1; j>=0; j--)
					{
						DramaItem drama = item.getDramaList().get(j);
						tempList.add(drama);
						
					}
					item.setDramaList(tempList);
				}
				VideoInfo videoInfo = new VideoInfo(item, VideoInfoTable.SORT_RECOMMEND);
				VideoInfoTable.insertVideo(videoInfo);
				mVideos.add(item);
				mHandler.sendEmptyMessage(FlagConstant.HOME_REQUEST_VIDEO);
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "===ActivityMain==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		switch (token) 
		{
		case Token.TOKEN_APP_RECD:
		{
			if (mRecdTime < 3)
			{
				requestAppRecd();
			}
			
			//后台获取推荐影片个数为0, 从数据库获取推荐影片
			/*ArrayList<AppRecdInfo> appRecdList = new ArrayList<AppRecdInfo>();
			appRecdList = AppRecdInfoTable.queryAllAppRecd();
			for (int i=0; i<2; i++)
			{
				if (i < appRecdList.size())
				{
					HomeRecommendItem item = new HomeRecommendItem();
					AppRecdInfo temp = appRecdList.get(i);
					item.setName(temp.getName());
					item.setCode(temp.getCode());
					item.setType(temp.getType());
					item.setImage(temp.getImage());
					mVodRecds.add(item);
				}
			}
			
			//开始请求详情接口
			mHandler.sendEmptyMessageDelayed(FlagConstant.HOME_REQUEST_VIDEO, 1000);*/
			break;
		}
		case Token.TOKEN_VIDEO:
		{
			Logcat.d(FlagConstant.TAG, " onNetError: Token.TOKEN_VIDEO");
			if (mTime == 1)  //请求第一个推荐详情接口错误
			{
				Logcat.d(FlagConstant.TAG, " mTime: " + mTime);
				if (mVodRecds.size() > 1)
				{
					Logcat.d(FlagConstant.TAG, " add an empty data. ");
					//添加一个空数据
					VideoItem item = new VideoItem();
					item.setVideoCode("");
					item.setName("");
					mVideos.add(item);
					
					mTime++;
					requestVideo(mVodRecds.get(1).getCode());
				}
				else
				{
					//请求完推荐数据,开启升级服务
					startUpgradeService();
				}
			}
			else if (mTime == 2)  //请求第二个推荐详情接口错误
			{
				Logcat.d(FlagConstant.TAG, " mTime: " + mTime);
				Logcat.d(FlagConstant.TAG, " add an empty data. ");
				
				//添加一个空数据
				VideoItem item = new VideoItem();
				item.setVideoCode("");
				item.setName("");
				mVideos.add(item);
				
				//请求完推荐数据,开启升级服务
				startUpgradeService();
			}
			break;
		}
		default:
			break;
		}
	}
	
	private void checkVideoDetail()
	{
		for (int i=0; i<mVodRecds.size(); i++)
		{
			VideoInfo videoInfo = VideoInfoTable.queryVideo(mVodRecds.get(i).getCode(), VideoInfoTable.SORT_RECOMMEND);
			if (videoInfo != null)
			{
				mVideos.add(videoInfo);
			}
			else
			{
				VideoItem item = new VideoItem();
				item.setVideoCode("");
				item.setName("");
				mVideos.add(item);
			}
		}
	}
	
	private void onStartupFinished()
	{
		for (int i=0; i<10; i++)
		{
			mHomeItems[i].setVisibility(View.VISIBLE);
		}
		mStartUp.hide();
		mHomeItems[2].requestFocus();
		
		for (int i=0; i<6; i++)
		{
			mInflects[i].setImageBitmap(ConvertViewToBitmap.createReflection(ConvertViewToBitmap.convertViewToBitmap(mHomeItems[4+i]), 140));
			mInflects[i].setVisibility(View.VISIBLE);
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case FlagConstant.HOME_STARTUP_FINISH:
			{
				mFinished = true;
				mFocusType = HOME_ITEM;
				BiMsg.sendAppStartBiMsg(mContext, mStartTimeId);
				onStartupFinished();
				mAppVersion.setText(Constant.APP_VERSION);
				new Thread(InitPPP).start();
				
				checkVodRecdList();
				freshVodRecdImage();
				checkVideoDetail();
				break;
			}
			case FlagConstant.HOME_REQUEST_VIDEO:
			{
				if (mTime <= 2)
				{
					mTime++;
					if (mTime == 1)
					{
						if (mVodRecds.size() > 0)
						{
							requestVideo(mVodRecds.get(0).getCode());
						}
						else
						{
							//请求完推荐数据,开启升级服务
							startUpgradeService();
						}
					}
					else if (mTime == 2)
					{
						if (mVodRecds.size() > 1)
						{
							requestVideo(mVodRecds.get(1).getCode());
						}
						else
						{
							//请求完推荐数据,开启升级服务
							startUpgradeService();
						}
					}
					else if (mTime == 3)
					{
						//请求完推荐数据,开启升级服务
						startUpgradeService();
					}
				}
				break;
			}
			case FlagConstant.HOME_BITMAP:
			{
				if (mExit)  //如果已经退出应用, 则释放Bitmap的内存
				{
					if (mGenerateBitmap0 != null)
					{
						Util.recycle(mGenerateBitmap0.getBitmap());
					}
					
					if (mGenerateBitmap1 != null)
					{
						Util.recycle(mGenerateBitmap1.getBitmap());
					}
				}
				else
				{
					if ((Integer)msg.obj == 0)
					{
						mInflects[4].setImageBitmap(mGenerateBitmap0.getBitmap());
					}
					else if ((Integer)msg.obj == 1)
					{
						mInflects[5].setImageBitmap(mGenerateBitmap1.getBitmap());
					}
				}
				break;
			}
			case FlagConstant.HOME_WANT_EXIT_APP:
			{
				mWantExit = false;
				break;
			}
			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @Title: freshRecdList
	 * @Description: 刷新应用推荐
	 * @return: void
	 */
	private void freshRecdList()
	{
		int recd_size = mRecds.size();
		Logcat.d(FlagConstant.TAG, " recd_size: " + recd_size);
		if (recd_size > 0)
		{
			mVodSorts.clear();
			mVodRecds.clear();
			for (int i=0; i<recd_size; i++)
			{
				if (mRecds.get(i).getType().equals("3"))      //点播分类
				{
					mVodSorts.add(mRecds.get(i));
				}
				else if (mRecds.get(i).getType().equals("2")) //点播
				{
					mVodRecds.add(mRecds.get(i));
				}
			}
			freshVodSortList();
			freshVodRecdList();
		}
		else
		{
			startUpgradeService();
		}
	}
	
	/**
	 * 
	 * @Title: freshVodSortList
	 * @Description: 刷新资源分类
	 * @return: void
	 */
	private void freshVodSortList()
	{
		int vod_sort_size = mVodSorts.size();
		Logcat.d(FlagConstant.TAG, " vod_sort_size: " + vod_sort_size);
		for (int i=0; i<3; i++)
		{
			if (i < vod_sort_size)
			{
				if (i == 2)
				{
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(mVodSorts.get(i).getImage(), mHomeItems[i+1].getIcon(), UILApplication.mHomeSortOption);
					}
					mHomeItems[i+1].setName(mVodSorts.get(i).getName());
				}
				else
				{
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(mVodSorts.get(i).getImage(), mHomeItems[i].getIcon(), UILApplication.mHomeSortOption);
					}
					mHomeItems[i].setName(mVodSorts.get(i).getName());
				}
				
				//保存分类code
				saveCode(i, mVodSorts.get(i).getCode());
			}
		}
	
		if (vod_sort_size > 3)
		{
			freshHomeSort();
		}
	}
	
	private void saveCode(int index, String code)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		String key = index + "";
		editor.putString(key, code);
		editor.commit();
	}
	
	private String getCode(int index)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		String key = index + "";
		return  sp.getString(key, "");
	}
	
	/**
	 * 
	 * @Title: freshVodRecdList
	 * @Description: 刷新资源推荐
	 * @return: void
	 */
	private void freshVodRecdList()
	{
		checkVodRecdList();
		//开始请求详情接口
		mVideos.clear();
		mHandler.sendEmptyMessageDelayed(FlagConstant.HOME_REQUEST_VIDEO, 1000);
		recycleBitmap();
		freshVodRecdImage();
	}
	
	private void checkVodRecdList()
	{
		int vod_recd_size = mVodRecds.size();
		Logcat.d(FlagConstant.TAG, " vod_recd_size: " + vod_recd_size);
		if (vod_recd_size == 0)  //后台获取个数为0
		{
			ArrayList<AppRecdInfo> appRecdList = new ArrayList<AppRecdInfo>();
			appRecdList = AppRecdInfoTable.queryAllAppRecd();
			for (int i=0; i<2; i++)
			{
				if (i < appRecdList.size())
				{
					HomeRecommendItem item = new HomeRecommendItem();
					AppRecdInfo temp = appRecdList.get(i);
					item.setName(temp.getName());
					item.setCode(temp.getCode());
					item.setType(temp.getType());
					item.setImage(temp.getImage());
					mVodRecds.add(item);
				}
			}
		}
		else if (vod_recd_size == 1)  //后台获取个数为1
		{
			ArrayList<AppRecdInfo> appRecdList = new ArrayList<AppRecdInfo>();
			appRecdList = AppRecdInfoTable.queryAllAppRecd();
			for (int i=0; i<appRecdList.size(); i++)
			{
				if (!mVodRecds.get(0).getCode().equals(appRecdList.get(i).getCode()))
				{
					HomeRecommendItem item = new HomeRecommendItem();
					AppRecdInfo temp = appRecdList.get(i);
					item.setName(temp.getName());
					item.setCode(temp.getCode());
					item.setType(temp.getType());
					item.setImage(temp.getImage());
					mVodRecds.add(item);
					break;
				}
			}
			
			AppRecdInfo appRecd = new AppRecdInfo();
			appRecd.setName(mVodRecds.get(0).getName());
			appRecd.setCode(mVodRecds.get(0).getCode());
			appRecd.setType(mVodRecds.get(0).getType());
			appRecd.setImage(mVodRecds.get(0).getImage());
			AppRecdInfoTable.insertAppRecd(appRecd);
		}
		else if (vod_recd_size >= 2)  //后台获取个数为大于2
		{
			for (int i=0; i<2; i++)
			{
				AppRecdInfo appRecd = new AppRecdInfo();
				appRecd.setName(mVodRecds.get(i).getName());
				appRecd.setCode(mVodRecds.get(i).getCode());
				appRecd.setType(mVodRecds.get(i).getType());
				appRecd.setImage(mVodRecds.get(i).getImage());
				AppRecdInfoTable.insertAppRecd(appRecd);
			}
		}
	}
	
	private void freshVodRecdImage()
	{
		for (int i=0; i<2; i++)
		{
			if (i < mVodRecds.size())
			{
				if (i == 0)
				{
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(mVodRecds.get(i).getImage(), mHomeItems[i+8].getIcon(), UILApplication.mHomeRecdOption,
						new ImageLoadingListener()
						{
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {}

							@Override
							public void onLoadingComplete(String arg0, View arg1,Bitmap arg2) 
							{
								//在线程生成倒影的Bitmap(防止主线程阻塞)
								mGenerateBitmap0 = new GenerateBitmap(arg2, mHandler, 0);
								mGenerateBitmap0.start();
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {}

							@Override
							public void onLoadingStarted(String arg0, View arg1) {}
						});
					}
				}
				else if (i == 1)
				{
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(mVodRecds.get(i).getImage(), mHomeItems[i+8].getIcon(), UILApplication.mHomeRecdOption,
						new ImageLoadingListener()
						{
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {}

							@Override
							public void onLoadingComplete(String arg0, View arg1,Bitmap arg2) 
							{
								//在线程生成倒影的Bitmap(防止主线程阻塞)
								mGenerateBitmap1 = new GenerateBitmap(arg2, mHandler, 1);
								mGenerateBitmap1.start();
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {}

							@Override
							public void onLoadingStarted(String arg0, View arg1) {}
						});
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: freshHomeSort
	 * @Description: 刷新首页分类
	 * @return: void
	 */
	private void freshHomeSort()
	{
		//int count = mVodSorts.size()-2;
		int count = mVodSorts.size()-3;
		Logcat.d(FlagConstant.TAG, " count: " + count);
		
		for (int i=0; i<count; i++)
		{
			HomeSortItem homeSortItem = new HomeSortItem(mContext);
			homeSortItem.setIndex(i);
			//homeSortItem.setName(mVodSorts.get(i+2).getName());
			homeSortItem.setName(mVodSorts.get(i+3).getName());
			homeSortItem.setOnFocusChangeListener(this);
			homeSortItem.setOnClickCustomListener(this);
			homeSortItem.setTag("" + i);
			homeSortItem.getIcon().setTag("" + i);
			mHomeSort.addView(homeSortItem);
			homeSortItem.setPosition(homeSortItem, i);
			//求余数
			int residue = i%2;
			if (residue == 0)
			{
				if (UILApplication.mImageLoader != null)
				{
					//UILApplication.mImageLoader.displayImage(mVodSorts.get(i+2).getImage(), homeSortItem.getIcon(), UILApplication.mHomeSortOption);
					UILApplication.mImageLoader.displayImage(mVodSorts.get(i+3).getImage(), homeSortItem.getIcon(), UILApplication.mHomeSortOption);
				}
			}
			else if (residue == 1)
			{
				HomeSortInflectItem inflectItem = new HomeSortInflectItem(mContext);
				mHomeSortInflect.addView(inflectItem);
				inflectItem.setPosition(inflectItem, i);
				inflectItem.setTag("" + i);
				inflectItem.getIcon().setImageBitmap(ConvertViewToBitmap.createReflection(ConvertViewToBitmap.convertViewToBitmap(homeSortItem), 140));
				if (UILApplication.mImageLoader != null)
				{
					//UILApplication.mImageLoader.displayImage(mVodSorts.get(i+2).getImage(), homeSortItem.getIcon(), UILApplication.mHomeSortOption, 
					UILApplication.mImageLoader.displayImage(mVodSorts.get(i+3).getImage(), homeSortItem.getIcon(), UILApplication.mHomeSortOption, 
					new ImageLoadingListener()
					{
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) 
						{
							//刷新倒影
							View view = (View)arg1.getParent();
							if (view != null)
							{
								String tag = arg1.getTag().toString();
								HomeSortInflectItem item = (HomeSortInflectItem)getHomeSortInflectItem(tag);
									
								if (item != null)
								{
									ImageView image = item.getIcon();
									if (image != null)
									{
										image.setImageBitmap(ConvertViewToBitmap.createReflection(ConvertViewToBitmap.convertViewToBitmap(view), 140));
									}
								}
							}
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {}
					});
				}
			}
		}
		
		//设置 mHomeSort 的宽度, 至于为什么是这么多, 我也不知道, 这个值是经过多次调试出来的
		RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mHomeSort.getLayoutParams();
		params1.width = 6*count*(290+12);
		mHomeSort.setLayoutParams(params1);
		
		RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mHomeSortInflect.getLayoutParams();
		params2.width = 6*count*(290+12);
		mHomeSortInflect.setLayoutParams(params2);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		boolean nRet = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyDown();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyUp();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRight();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyBack();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyMenu();
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
	 * @Title: doKeyDown
	 * @Description: 响应下键
	 * @return
	 * @return: boolean
	 */
	@SuppressLint("UseValueOf")
	private boolean doKeyDown()
	{
		if (!mFinished)
		{
			return true;
		}
		
		if (mFocusType == HOME_SORT_ITEM)
		{
			View v = getCurrentFocus();
			int tag = new Integer(v.getTag().toString());
			//int count = mVodSorts.size()-2;
			int count = mVodSorts.size()-3;
			
			if (tag%2 == 0)
			{
				if(tag == count-1)
				{
					return true;
				}
				View nextView = getHomeSortItem((tag+1) + "");
				nextView.requestFocus();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyUp
	 * @Description: 响应上键
	 * @return
	 * @return: boolean
	 */
	@SuppressLint("UseValueOf")
	private boolean doKeyUp()
	{
		if (!mFinished)
		{
			return true;
		}
		
		if (mFocusType == HOME_SORT_ITEM)
		{
			View v = getCurrentFocus();
			int tag = new Integer(v.getTag().toString());
			
			if (tag%2 == 1)
			{
				View nextView = getHomeSortItem((tag-1) + "");
				nextView.requestFocus();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	@SuppressLint("UseValueOf")
	private boolean doKeyLeft()
	{
		if (!mFinished)
		{
			return true;
		}
		
		if (mFocusType == HOME_ITEM)
		{
			if (getCurrentFocus() == mHomeItems[9])
			{
				mHs.smoothScrollTo(0, 0);
				mHomeItems[8].requestFocus();
				return true;
			}
		}
		if (mFocusType == HOME_SORT_ITEM)
		{
			View v = getCurrentFocus();
			int tag = new Integer(v.getTag().toString());
			
			if(tag == 0 || tag == 1)
			{
				mFocusType = HOME_ITEM;
				mHomeItems[9].requestFocus();
			}
			else
			{
				if(tag%8 == 4 || tag%8 == 5)
				{
					if (tag/8 == 0)
					{
						mHs.smoothScrollTo(1000, 0);
					}
					else if (tag/8 > 0)
					{
						mHs.smoothScrollTo(810 + tag/8*1205, 0);
					}
				}
				
				View nextView = getHomeSortItem((tag-2) + "");
				nextView.requestFocus();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyRight
	 * @Description: 响应右键
	 * @return
	 * @return: boolean
	 */
	@SuppressLint("UseValueOf")
	private boolean doKeyRight()
	{
		if (!mFinished)
		{
			return true;
		}
		
		if (mFocusType == HOME_ITEM)
		{
			if (getCurrentFocus() == mHomeItems[8])
			{
				mHs.smoothScrollTo(1000, 0);
				mHomeItems[9].requestFocus();
				return true;
			}
			else if (getCurrentFocus() == mHomeItems[9])
			{
				//if (mVodSorts.size() > 2)
				if (mVodSorts.size() > 3)
				{
					mFocusType = HOME_SORT_ITEM;
					View nextView = getHomeSortItem("0");
					nextView.requestFocus();
				}
				return true;
			}
		}
		else if(mFocusType == HOME_SORT_ITEM)
		{
			View v = getCurrentFocus();
			int tag = new Integer(v.getTag().toString());
			int count = mVodSorts.size()-3;
			//int count = mVodSorts.size()-2;
			
			if(tag == count-1 || tag == count-2)
			{
				return true;
			}
			else
			{
				if(tag%8 == 2 || tag%8 == 3)
				{
					mHs.smoothScrollTo(810 + (tag/8 + 1)*1205, 0);
				}
				
				View nextView = getHomeSortItem((tag+2) + "");
				nextView.requestFocus();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyBack
	 * @Description: 响应返回键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyBack()
	{
		if (!mWantExit)
		{
			mWantExit = true;
			mHandler.sendEmptyMessageDelayed(FlagConstant.HOME_WANT_EXIT_APP, 2500);
			ExitToast.getExitToast().createToast(mContext, getResources().getString(R.string.toast_want_to_exit_app));
		}
		else
		{
			mExit = true;
			//退出应用BI
			BiMsg.sendAppExitBiMsg(mContext);
			mHandler.removeMessages(FlagConstant.HOME_WANT_EXIT_APP);
			finish();
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
		if (!mFinished)
		{
			return true;
		}
		
		createQuestionnaireDialog();
		return true;
	}
	
	/**
	 * 
	 * @Title: startQuestionnaire
	 * @Description: 进入问卷调查
	 * @return: void
	 */
	private void startQuestionnaire()
	{
		Intent intent = new Intent(this, ActivityQuestionnaire.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	/**
	 * 
	 * @Title: getHomeSortItem
	 * @Description: 获取首页分类Item(动态)
	 * @param tag
	 * @return
	 * @return: View
	 */
	private View getHomeSortItem(String tag)
	{
		View view = null;
		
		for (int i=0; i<mHomeSort.getChildCount(); i++)
		{
			view = mHomeSort.getChildAt(i);
			if (view.getTag().toString().equalsIgnoreCase(tag))
			{
				return view;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: getHomeSortInflectItem
	 * @Description: 获取首页分类Item的倒影(动态)
	 * @param tag
	 * @return
	 * @return: View
	 */
	private View getHomeSortInflectItem(String tag)
	{
		View image = null;
		
		for (int i=0; i<mHomeSortInflect.getChildCount(); i++)
		{
			image = mHomeSortInflect.getChildAt(i);
			if (image.getTag().toString().equalsIgnoreCase(tag))
			{
				return image;
			}
		}
		return null;
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		UILApplication.unInitService();
		stopUpgradeService();
		stopTimeAuthService();
		unregisterReceiver(mBroadcastReceiver);
		
		Util.recycle(StaticVariable.gBufferBitmap);
		Util.recycle(StaticVariable.gBufferBitmap);
		Util.recycle(StaticVariable.gBufferBitmap);
		recycleBitmap();
		
		
		if(mHandler != null)
		{
			mHandler.removeCallbacksAndMessages(null);
		}
	}
	
	private void recycleBitmap()
	{
		if (mGenerateBitmap0 != null)
		{
			Util.recycle(mGenerateBitmap0.getBitmap());
		}
		
		if (mGenerateBitmap1 != null)
		{
			Util.recycle(mGenerateBitmap1.getBitmap());
		}
	}
	
	/**
	 * 
	 * @Title: createQuestionnaireDialog
	 * @Description: 创建问卷调查对话框
	 * @return: void
	 */
	private void createQuestionnaireDialog()
	{
		final DialogQuestionnaire dialog = new DialogQuestionnaire(mContext, R.style.dialog_style);
		dialog.setOnClickCustomListener(new onClickCustomListener()
		{
			@Override
			public void OnClick(View v) 
			{
				switch (v.getId()) 
				{
				case R.id.id_dialog_questionnaire_text:
				{
					dialog.dismiss();
					startQuestionnaire();
					break;
				}
				default:
					break;
				}
			}
		});
		dialog.show();
	}
	
	/**
	 * 
	 * @Title: startUpgradeService
	 * @Description: 启动升级服务
	 * @return: void
	 */
	private void startUpgradeService()
	{ 
		Logcat.i(FlagConstant.TAG, " start upgrade service.");
        Intent intent = new Intent();   
        intent.setClass(this, UpgradeModelService.class);
        startService(intent);
	}
	
	/**
	 * 
	 * @Title: stopUpgradeService
	 * @Description: 关闭升级服务
	 * @return: void
	 */
	private void stopUpgradeService()
	{ 
		Logcat.i(FlagConstant.TAG, " stops upgrade service.");
        Intent intent = new Intent(); 
        intent.setClass(this, UpgradeModelService.class);
        stopService(intent);
	}
	
	/**
	 * 
	 * @Title: startTimeAuthService
	 * @Description: 启动定时鉴权服务
	 * @return: void
	 */
	private void startTimeAuthService()
	{ 
		Logcat.i(FlagConstant.TAG, " start TimeAuth service.");
        Intent intent = new Intent();   
        intent.setClass(this, TimeAuthService.class);
        startService(intent);
	}
	
	/**
	 * 
	 * @Title: stopTimeAuthService
	 * @Description: 关闭定时鉴权服务
	 * @return: void
	 */
	private void stopTimeAuthService()
	{ 
		Logcat.i(FlagConstant.TAG, " stops TimeAuth service.");
        Intent intent = new Intent(); 
        intent.setClass(this, TimeAuthService.class);
        stopService(intent);
	}
}