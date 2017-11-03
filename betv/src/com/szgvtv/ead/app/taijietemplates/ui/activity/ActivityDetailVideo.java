/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityDetailVideo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 点播资源(如:电视剧 体育 综艺等等, 电影除外)详情Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:45:45
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfo;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfoTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.VodItem;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityDetailVideo extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context              mContext;                             //上下文
	private ImageView            mPoster;                              //海报
	private ImageView            mFavoriteIcon;                        //收藏图标
	private TextView             mName;                                //名称
	private TextView             mUpdate;                              //更新
	private TextView             mStar;                                //主演
	private TextView             mTime;                                //上映
	private TextView             mBrief;                               //简介
	private TextView             mPlay;                                //播放
	private TextView             mFavoriteText;                        //收藏信息
	private RelativeLayout       mSelection;                           //选集
	private RelativeLayout       mFavorite;                            //收藏
	private ImageView            mFocus;                               //放大image
	private VodItem              mVodItems[] = new VodItem[5];         //点播资源Item
	private DialogPrompt         mDialog;                              //提示框
	private Animation            mScaleBig;                            //放大动画
	private Animation            mScaleSmall;                          //缩小动画
	private ArrayList<VideoItem> mVideos = new ArrayList<VideoItem>(); //当前点播资源列表
	private VideoItem            mVideoItem;                           //点播资源
	private VideoInfo            mVideoInfo;                           //数据库资源item
	private boolean              mFavoritting = false;                 //是否正在收藏中
	private int                  mType = -1;                           //进入详情类型(0:收藏；1:播放历史；-1:其它)
	private int                  mSize = 5;                            //最多显示个数
	private int                  mCount = 0;                           //总个数
	private int                  mHistoryIndex = 0;                    //历史剧集索引
	private int                  mPlayTime = 0;                        //播放时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_detail_video);
		
		initViews();
		getVideoInfo();
		registerIntentReceivers();
	}
	
	private void initViews()
	{
		mPoster = (ImageView) findViewById(R.id.id_detail_video_poster_icon);
		mFavoriteIcon = (ImageView) findViewById(R.id.id_detail_video_favorite_icon);
		mName  = (TextView) findViewById(R.id.id_detail_video_name);
		mUpdate = (TextView) findViewById(R.id.id_detail_video_update);
		mStar = (TextView) findViewById(R.id.id_detail_video_star_name);
		mTime = (TextView) findViewById(R.id.id_detail_video_time);
		mBrief = (TextView) findViewById(R.id.id_detail_video_brief_content);
		mPlay = (TextView) findViewById(R.id.id_detail_video_play_drama);
		mFavoriteText = (TextView) findViewById(R.id.id_detail_video_favorite_text);
		mSelection = (RelativeLayout) findViewById(R.id.id_detail_video_selection);
		mFavorite = (RelativeLayout) findViewById(R.id.id_detail_video_favorite);
		mFocus = (ImageView) findViewById(R.id.id_detail_video_focus);
		mVodItems[0] = (VodItem) findViewById(R.id.id_detail_video_item0);
		mVodItems[1] = (VodItem) findViewById(R.id.id_detail_video_item1);
		mVodItems[2] = (VodItem) findViewById(R.id.id_detail_video_item2);
		mVodItems[3] = (VodItem) findViewById(R.id.id_detail_video_item3);
		mVodItems[4] = (VodItem) findViewById(R.id.id_detail_video_item4);
		
		mPlay.setOnClickListener(this);
		mSelection.setOnClickListener(this);
		mFavorite.setOnClickListener(this);
		
		for (int i=0; i<5; i++)
		{
			mVodItems[i].setOnClickListener(this);
			mVodItems[i].setOnFocusChangeListener(this);
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 
	 * @Title: requestVideoRecd
	 * @Description: 请求点播推荐
	 * @return: void
	 */
	private void requestVideoRecd()
	{
		RequestDataManager.requestData(this, mContext, Token.TOKEN_VIDEO_RECOMMEND, mSize, 1, mVideoItem.getVideoCode());  
	}
	
	/**
	 * 
	 * @Title: getVideoInfo
	 * @Description: 获取点播资源信息
	 * @return: void
	 */
	private void getVideoInfo()
	{
		Bundle bundle = getIntent().getExtras();
		mVideoItem = bundle.getParcelable(FlagConstant.ToActivityDetailVideoKey);
		mType = bundle.getInt(FlagConstant.ToActivityDetailRecordKey, -1);
		freshVideoInfo();
		requestVideoRecd();
	}
	
	/**
	 * 
	 * @Title: freshVideoInfo
	 * @Description: 刷新点播资源信息
	 * @return: void
	 */
	private void freshVideoInfo()
	{
		if (mVideoItem == null)
		{
			return;
		}
		mPlay.requestFocus();
		mVideoInfo = VideoInfoTable.getPlayHistory(mVideoItem.getVideoCode());
		mHistoryIndex = getHistoryIndex(mVideoInfo);
		String number = mVideoItem.getDramaList().get(mHistoryIndex).getNumber();
		int type = Integer.parseInt(mVideoItem.getVodtype());
		if (type == 2)
		{
			mPlay.setText(number);
		}
		else if (type == 3)
		{
			String name = mVideoItem.getDramaList().get(mHistoryIndex).getDramaName();
			mPlay.setText(number + " " + name);
		}
		
		if (UILApplication.mImageLoader != null)
		{
			UILApplication.mImageLoader.displayImage(mVideoItem.getSmallPic(), mPoster, UILApplication.mDetailOption);
		}
		mName.setText(mVideoItem.getName());
		int total = Integer.parseInt(mVideoItem.getTotalDramas());
		int current = mVideoItem.getDramaList().size();
		if (current == total)
		{
			mUpdate.setText(getResources().getString(R.string.detail_all) +  total + getResources().getString(R.string.detail_drama_suffix));
		}
		else if (current < total)
		{
			mUpdate.setText(getResources().getString(R.string.detail_update) +  current + getResources().getString(R.string.detail_drama_suffix));
		}
		mStar.setText(mVideoItem.getActor());
		mTime.setText(getResources().getString(R.string.detail_time) + " " + mVideoItem.getTime());
		mBrief.setText(mVideoItem.getSummary());
		
		if (!VideoInfoTable.existVideo(mVideoItem.getVideoCode(), VideoInfoTable.SORT_FAVORITE))  //是否收藏过
		{
			mFavoriteText.setText(getResources().getString(R.string.detail_favorite));
			mFavoriteIcon.setImageResource(R.drawable.detail_icon_favorite);
		}
		else
		{
			mFavoriteText.setText(getResources().getString(R.string.detail_favorited));
			mFavoriteIcon.setImageResource(R.drawable.detail_icon_favorited);
		}
	}
	
	/**
	 * 
	 * @Title: getHistoryIndex
	 * @Description: 获取历史剧集索引
	 * @return
	 * @return: int
	 */
	private int getHistoryIndex(VideoInfo videoInfo)
	{
		if (videoInfo != null)
		{
			for (int i=0; i<mVideoItem.getDramaList().size(); i++)
			{
				if (videoInfo.getDramaCode().equals(mVideoItem.getDramaList().get(i).getDramaCode()))
				{
					Logcat.d(FlagConstant.TAG, "mIndex: " + i);
					Logcat.d(FlagConstant.TAG, "videoInfo_dramacode: " + videoInfo.getDramaCode());
					return i;
				}
			}
		}
		return 0;
	}
	
	@Override
	public void onCancel(OutPacket out, int token) 
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccessful(Object in, int token) 
	{
		try 
		{
			switch (token) 
			{
			case Token.TOKEN_VIDEO_RECOMMEND:
			{
				mVideos.clear();
				ArrayList<VideoItem> temp = new ArrayList<VideoItem>(); 
				temp = (ArrayList<VideoItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					VideoItem item = temp.get(i);
					item.setClassifyCode("");
					item.setClassifyName("");
					
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
					
					mVideos.add(item);
				}
				freshVideoRecd();
				break;
			}
			case Token.TOKEN_VIDEO:
			{
				VideoItem item = (VideoItem) RequestDataManager.getData(in);
				if (item != null)
				{
					mVideoItem = item;
				}
				freshVideoInfo();
				requestVideoRecd();
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "===ActivityDetailVideo==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
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
			Util.amplifyItem(view, mFocus, 0.10);
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
			v.setSelected(false);
			mFocus.setVisibility(View.GONE);
			mScaleSmall = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_VOD_SMALL_ANIMS);
			mScaleSmall.setFillAfter(false);
			mScaleSmall.setAnimationListener(this);
			v.startAnimation(mScaleSmall);
		}
		else
		{
			v.setSelected(true);
			mScaleBig = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_VOD_BIG_ANIMS);
			mScaleBig.setFillAfter(true);
			mScaleBig.setAnimationListener(this);
			v.startAnimation(mScaleBig);
			v.bringToFront();
		}
	}
	
	/**
	 * 
	 * @Title: onPlay
	 * @Description: 响应播放按钮
	 * @param playTime
	 * @return: void
	 */
	private void onPlay(int playTime)
	{
		Logcat.d(FlagConstant.TAG, " ==== ActivityDetailVideo ==== mPlayIndex: " + mHistoryIndex);
		Intent intent = new Intent(this, ActivityPlayVideo.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(FlagConstant.ToActivityPlayVideoKey, mVideoItem);
		bundle.putInt(FlagConstant.DramaIndexKey, mHistoryIndex);
		bundle.putInt(FlagConstant.ToActivityPlayTimeKey, playTime);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_detail_video_play_drama:
		{
			VideoInfo videoInfo = VideoInfoTable.getPlayHistory(mVideoItem.getVideoCode());
			if (videoInfo != null)
			{
				mPlayTime = videoInfo.getPlayTime();
				String playDramaCode = videoInfo.getDramaCode();
				String dramaCode = mVideoItem.getDramaList().get(mHistoryIndex).getDramaCode();
				Logcat.d(FlagConstant.TAG, " mPlayTime: " + mPlayTime);
				Logcat.d(FlagConstant.TAG, " playDramaCode: " + playDramaCode);
				Logcat.d(FlagConstant.TAG, " dramaCode: " + dramaCode);
				
				//只有历史剧集编码和当前剧集编码相同时, 该剧集才有播放历史
				if (mPlayTime >= 30*1000 && playDramaCode.equals(dramaCode))
				{
					//有播放历史, 且历史播放时间大于0, 则选择是否续播
					String info = getResources().getString(R.string.dialog_history) + 
						      	  Util.formatTimeString(mPlayTime) + 
						          "\n" + "          " + 
						          getResources().getString(R.string.dialog_continue_play);
					createPlayPrompt(info, 
					    		     getResources().getString(R.string.dialog_yes), 
					    		     getResources().getString(R.string.dialog_no));
				}
				else
				{
					//有播放历史, 但历史播放时间等于0, 则从头开始播放
					onPlay(0);
				}
			}
			else
			{
				//没有播放历史, 则从头开始播放
				onPlay(0);
			}
			break;
		}
		case R.id.id_detail_video_selection:
		{
			int type = Integer.parseInt(mVideoItem.getVodtype());
			if (type == 2)
			{
				Intent intent = new Intent(this, ActivityDramaTV.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(FlagConstant.ToActivityDramaTVKey, mVideoItem);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
			}
			else if (type == 3)
			{
				Intent intent = new Intent(this, ActivityDramaOther.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(FlagConstant.ToActivityDramaOtherKey, mVideoItem);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
			}
			break;
		}
		case R.id.id_detail_video_favorite:
		{
			if (!mFavoritting)
			{
				onFavorite();
			}
			break;
		}
		case R.id.id_detail_video_item0:
		case R.id.id_detail_video_item1:
		case R.id.id_detail_video_item2:
		case R.id.id_detail_video_item3:
		case R.id.id_detail_video_item4:
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("2"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
			int curIndex = getVodItemFocusIndex();
			VideoItem item = mVideos.get(curIndex);
			int type = Integer.parseInt(item.getVodtype());
			if (type == 1)
			{
				Intent intent = new Intent(this, ActivityDetailFilm.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(FlagConstant.ToActivityDetailFilmKey, item);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
			else if (type == 2 || type == 3)
			{
				mVideoItem = item;
				freshVideoInfo();
				for (int i=0; i<5; i++)
				{
					mVodItems[i].setVisibility(View.INVISIBLE);
				}
				requestVideoRecd();
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: freshVideoRecd
	 * @Description: 刷新点播资源列表
	 * @return: void
	 */
	private void freshVideoRecd()
	{
		mCount = mVideos.size();
		Logcat.d(FlagConstant.TAG, " mCount: " + mCount);
		Logcat.d(FlagConstant.TAG, " mSize: " + mSize);
		Logcat.d(FlagConstant.TAG, " ++++++++++++++++++++++++++++++++++++++++");
		
		if (mCount > mSize)
		{
			mCount = mSize;
		}
		
		if (mCount > 0)
		{
			for (int i=0; i<mSize; i++)
			{
				if(i < mCount)
				{
					VideoItem item = mVideos.get(i);
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(item.getSmallPic(), mVodItems[i].getIcon(), UILApplication.mVodOption);
					}
					mVodItems[i].setVideo(item);
					mVodItems[i].setName(item.getName());
					mVodItems[i].setVisibility(View.VISIBLE);
				}
				else
				{
					mVodItems[i].setVisibility(View.INVISIBLE);
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: onFavorite
	 * @Description: 响应收藏按钮
	 * @return: void
	 */
	private void onFavorite()
	{
		boolean favorited = VideoInfoTable.existVideo(mVideoItem.getVideoCode(), VideoInfoTable.SORT_FAVORITE);
		
		if (!favorited)
		{
			int count = VideoInfoTable.queryAllSortVideos(VideoInfoTable.SORT_FAVORITE).size();
			
			if (count >= 8)  //最多有8条收藏数据
			{
				createDialogPrompt(/*R.drawable.tips_question, */
				                   getResources().getString(R.string.dialog_full), 
				                   getResources().getString(R.string.dialog_goto_favorite), 
				                   getResources().getString(R.string.dialog_cancel));
			}
			else
			{
				startToRWDataBase(favorited);
			}
		}
		else
		{
			mFavoritting = true;
			VideoInfoTable.deleteVideo(mVideoItem.getVideoCode(), VideoInfoTable.SORT_FAVORITE);
			endToRWDataBase(favorited);
		}
	}
	
	/**
	 * 
	 * @Title: startToRWDataBase
	 * @Description: 开始写数据到数据库
	 * @param isCollected
	 * @return: void
	 */
	private void startToRWDataBase(boolean isCollected)
	{
		mFavoritting = true;
		VideoInfo item = new VideoInfo(mVideoItem, VideoInfoTable.SORT_FAVORITE);
		VideoInfoTable.insertVideo(item);
		endToRWDataBase(isCollected);
	}
	
	/**
	 * 
	 * @Title: endToRWDataBase
	 * @Description: 结束写数据库
	 * @param favorited
	 * @return: void
	 */
	private void endToRWDataBase(boolean favorited)
	{
		mFavoritting = false;
		if (!favorited)
		{
			mFavoriteText.setText(getResources().getString(R.string.detail_favorited));
			mFavoriteIcon.setImageResource(R.drawable.detail_icon_favorited);
		}
		else
		{
			mFavoriteText.setText(getResources().getString(R.string.detail_favorite));
			mFavoriteIcon.setImageResource(R.drawable.detail_icon_favorite);
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		boolean nRet = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyBack();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRight();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyDown();
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
	 * @Title: doKeyBack
	 * @Description: 响应返回键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyBack()
	{
		if (mType == VideoInfoTable.SORT_FAVORITE)
		{
			Intent intent = getIntent();
			setResult(1, intent);  //resultCode = 1
			finish();
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
	private boolean doKeyLeft()
	{
		if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			if (curIndex == 0)
			{
				mVodItems[mCount-1].requestFocus();
				return true;
			}
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
	private boolean doKeyRight()
	{
		if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			if (curIndex == mCount-1)
			{
				mVodItems[0].requestFocus();
				return true;
			}
		}
		return false;
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
		if (!isVodItemsFocus() && mCount > 0)
		{
			mVodItems[0].requestFocus();
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: isVodItemsFocus
	 * @Description: VodItems是否选中
	 * @return
	 * @return: boolean
	 */
	private boolean isVodItemsFocus()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mVodItems[i])
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: getVodItemFocusIndex
	 * @Description: VodItems选中索引
	 * @return
	 * @return: int
	 */
	private int getVodItemFocusIndex()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mVodItems[i])
			{
				return i;
			}
		}
		return 0;
	}
	
	private FreshIndexReceiver mReceiver = new FreshIndexReceiver();
	private class FreshIndexReceiver extends BroadcastReceiver 
	{
		public void onReceive(Context context, Intent intent) 
		{
			if (intent.getAction().equals(Constant.ACTION_FRESH_DRAMA)) 
			{
				//更新剧集索引的同时, 也要更新数据库资源item(add by zhaoqy 2014-11-19)
				mVideoInfo = VideoInfoTable.getPlayHistory(mVideoItem.getVideoCode());
				
				mHistoryIndex = intent.getIntExtra(FlagConstant.DramaIndexKey, 0);
				String number = mVideoItem.getDramaList().get(mHistoryIndex).getNumber();
				int type = Integer.parseInt(mVideoItem.getVodtype());
				if (type == 2)
				{
					mPlay.setText(number);
				}
				else if (type == 3)
				{
					String name = mVideoItem.getDramaList().get(mHistoryIndex).getDramaName();
					mPlay.setText(number + " " + name);
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: registerIntentReceivers
	 * @Description: 注册广播
	 * @return: void
	 */
	private void registerIntentReceivers() 
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_FRESH_DRAMA);
		registerReceiver(mReceiver, filter);
	}
	
	/**
	 * 
	 * @Title: unregisterIntentReceivers
	 * @Description: 注销广播
	 * @return: void
	 */
	private void unregisterIntentReceivers() 
	{
		if (mReceiver != null) 
		{
			unregisterReceiver(mReceiver);
			//mReceiver = null;
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterIntentReceivers();
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
		mDialog = new DialogPrompt(mContext, /*R.style.dialog_style, resid,*/ info, sure, cancel);
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
					startToRWDataBase(false);
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
	}
	
	/**
	 * 
	 * @Title: createPlayPrompt
	 * @Description: 创建是否续播对话框
	 * @param info
	 * @param sure
	 * @param cancel
	 * @return: void
	 */
	private void createPlayPrompt(String info, String sure, String cancel)
	{
		if (mDialog != null)
		{
			mDialog = null;
		}
		
		mDialog = new DialogPrompt(mContext, info, sure, cancel);
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
					//选择续播
					onPlay(mPlayTime);
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					//选择从头开始播
					onPlay(-1);
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
	}
}
