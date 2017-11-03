/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityPlaybackChannel.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 回放频道列表Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:39:27
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.db.LiveChannelTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.view.DotGroup;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.PlaybackChannelItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityPlaybackChannel extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context                    mContext;                                     //上下文
	private TextView                   mTitle;                                       //标题
	private ImageView                  mFocus;                                       //放大Image
	private LoadingPage                mLoading;                                     //加载
	private PlaybackChannelItem        mPlaybacks[] = new PlaybackChannelItem[12];   //回放频道Item
	private DotGroup                   mDotGroup;                                    //页码
	private Animation                  mScaleBig;                                    //放大动画
	private Animation                  mScaleSmall;                                  //缩小动画
	private ArrayList<LiveChannelItem> mChannels = new ArrayList<LiveChannelItem>(); //所有点播资源列表
	private int                        mCurPage = 0;                                 //当前页数
	private int                        mTotPage = 0;                                 //总页数
	private int                        mCount = 0;                                   //总个数
	private int                        mSize = 12;                                   //每页最多显示个数
	private int                        mState = 0;                                   //选中状态
	private int                        mListId = 0;                                  //直播列表耗时启动id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_playback_channel);
		
		initViews();
		mLoading.setLoadPageFail(false);
		mLoading.show();
		
		//先查询数据库是否有频道列表: 频道个数大于0, 则使用该频道列表；若小于0, 则请求频道列表
		checkChannelList();
	}
	
	private void initViews()
	{
		mTitle = (TextView) findViewById(R.id.id_playback);
		mFocus = (ImageView) findViewById(R.id.id_playback_focus);
		mDotGroup = (DotGroup) findViewById(R.id.id_playback_dot);
		mLoading = (LoadingPage) findViewById(R.id.id_playback_loading_page);
		mPlaybacks[0] = (PlaybackChannelItem) findViewById(R.id.id_playback_item0);
		mPlaybacks[1] = (PlaybackChannelItem) findViewById(R.id.id_playback_item1);
		mPlaybacks[2] = (PlaybackChannelItem) findViewById(R.id.id_playback_item2);
		mPlaybacks[3] = (PlaybackChannelItem) findViewById(R.id.id_playback_item3);
		mPlaybacks[4] = (PlaybackChannelItem) findViewById(R.id.id_playback_item4);
		mPlaybacks[5] = (PlaybackChannelItem) findViewById(R.id.id_playback_item5);
		mPlaybacks[6] = (PlaybackChannelItem) findViewById(R.id.id_playback_item6);
		mPlaybacks[7] = (PlaybackChannelItem) findViewById(R.id.id_playback_item7);
		mPlaybacks[8] = (PlaybackChannelItem) findViewById(R.id.id_playback_item8);
		mPlaybacks[9] = (PlaybackChannelItem) findViewById(R.id.id_playback_item9);
		mPlaybacks[10] = (PlaybackChannelItem) findViewById(R.id.id_playback_item10);
		mPlaybacks[11] = (PlaybackChannelItem) findViewById(R.id.id_playback_item11);
		mLoading.setOnClickListener(this);
		
		for (int i=0; i<12; i++)
		{
			mPlaybacks[i].setVisibility(View.INVISIBLE);
			mPlaybacks[i].setOnFocusChangeListener(this);
			mPlaybacks[i].setOnClickListener(this);
		}
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
		ArrayList<LiveChannelItem> temp = new ArrayList<LiveChannelItem>(); 
		temp = LiveChannelTable.queryAllLiveChannels();
		
		for (int i=0; i<temp.size(); i++)
		{
			LiveChannelItem item = temp.get(i);
			int replay = item.getIsReplay();
			if (replay == 1)  //0-无，1-有
			{
				mChannels.add(item);
			}
		}
		
		mCount = mChannels.size();
		Logcat.d(FlagConstant.TAG, " mCount: " + mCount);
		if (mChannels.size() > 0)
		{
			mLoading.hide();
			mTotPage = (mCount-1)/mSize + 1;
			mCurPage++;
			freshChannelList();
			
			//10秒后再从后台请求直播频道列表
			mHandler.sendEmptyMessageDelayed(FlagConstant.PLAY_REQUEST_CHANNEL, 10*1000);
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
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) 
	{
		if (!hasFocus) 
		{
			v.setSelected(false);
			mFocus.setVisibility(View.GONE);
			mScaleSmall = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_PLAYBACK_SMALL_ANIMS);
			mScaleSmall.setFillAfter(false);
			mScaleSmall.setAnimationListener(this);
			v.startAnimation(mScaleSmall);
		}
		else
		{
			v.setSelected(true);
			mScaleBig = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_PLAYBACK_BIG_ANIMS);
			mScaleBig.setFillAfter(true);
			mScaleBig.setAnimationListener(this);
			v.startAnimation(mScaleBig);
			v.bringToFront();
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_playback_item0:
		case R.id.id_playback_item1:
		case R.id.id_playback_item2:
		case R.id.id_playback_item3:
		case R.id.id_playback_item4:
		case R.id.id_playback_item5:
		case R.id.id_playback_item6:
		case R.id.id_playback_item7:
		case R.id.id_playback_item8:
		case R.id.id_playback_item9:
		case R.id.id_playback_item10:
		case R.id.id_playback_item11:
		{
			int curIndex = getPlaybackItemFocusIndex();
			int index = curIndex + (mCurPage-1)*mSize;
			LiveChannelItem item = mChannels.get(index);
			Intent intent = new Intent(this, ActivityPlaybackProgram.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(FlagConstant.ToActivityPlaybackProgramKey, item);
			bundle.putInt(FlagConstant.ToActivityPlaybackProgramIndexKey, index);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
		case R.id.id_playback_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				mState = 1;
				requestChannelList();
			}
			break;
		}
		default:
			break;
		}
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
					//此时要插入到数据库中
					LiveChannelTable.insertChannelList(temp);

					for (int i=0; i<temp.size(); i++)
					{
						LiveChannelItem item = temp.get(i);
						//mChannels.add(item);
						//该频道有节目预告时, 才将该频道展示出来(add by zhaoqy 2014-12-17)
						int replay = item.getIsReplay();
						if (replay == 1)  //0-无，1-有
						{
							mChannels.add(item);
						}
					}
					
					mCount = mChannels.size();
					Logcat.d(FlagConstant.TAG, " mCount: " + mCount);
					if (mCount > 0)
					{
						mLoading.hide();
						mTotPage = (mCount-1)/mSize + 1;
						mCurPage++;
						freshChannelList();
					}
					else 
					{
						onNetError(-1, "error", null, token);
					}
				}
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "===ActivityPlaybackChannel==== onSuccessful error + " + e.toString());
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
				
				mLoading.setLoadPageFail(true);
				mLoading.requestFocus();
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
			case FlagConstant.PLAY_REQUEST_CHANNEL:
			{
				requestChannelList();
				break;
			}
			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @Title: freshSearchResult
	 * @Description: 刷新搜索结果
	 * @return: void
	 */
	private void freshChannelList()
	{
		for (int i=0; i<mSize; i++)
		{
			if(i + (mCurPage-1)*mSize < mCount)
			{
				int index = i + (mCurPage-1)*mSize;
				LiveChannelItem item = mChannels.get(index);
				if (UILApplication.mImageLoader != null)
				{
					UILApplication.mImageLoader.displayImage(item.getTvLogo(), mPlaybacks[i].getIcon(), UILApplication.mChannelIconOption);
				}
				mPlaybacks[i].setItemBackground(index);
				mPlaybacks[i].setName(item.getTvName());
				mPlaybacks[i].setVisibility(View.VISIBLE);
				mPlaybacks[i].setFocusable(true);
				mTitle.setFocusable(false);
			}
			else
			{
				mPlaybacks[i].setVisibility(View.INVISIBLE);
			}
		}
		setDefaultFocus();
		updateDot();
	}
	
	/**
	 * 
	 * @Title: setDefaultFocus
	 * @Description: 设置默认光标
	 * @return: void
	 */
	private void setDefaultFocus()
	{
		int curIndex = 0;
		switch (mState) 
		{
		case 0:  //第一次加载
		case 1:  //加载失败
		case 2:  //下一页/上一页
		case 3:  //第一排最后一个向右
		{
			curIndex = 0;
			break;
		}
		case 4:  //第二排最后一个向右
		{
			if ((mCount-1)-(mCurPage-1)*mSize < 4)  
			{
				curIndex = (mCount-1)-(mCurPage-1)*mSize;
			}
			else
			{
				curIndex = 4;
			}
			break;
		}
		case 5:  //第三排最后一个向右
		{
			if ((mCount-1)-(mCurPage-1)*mSize < 8)  
			{
				curIndex = (mCount-1)-(mCurPage-1)*mSize;
			}
			else
			{
				curIndex = 8;
			}
			break;
		}
		case 6:  //第一排第一个向左
		{
			curIndex = 3;
			break;
		}
		case 7:  //第二排第一个向左
		{
			curIndex = 7;
			break;
		}
		case 8:  //第三排第一个向左
		{
			curIndex = 11;
			break;
		}
		default:
			break;
		}
		mPlaybacks[curIndex].requestFocus();
	}
	
	/**
	 * 
	 * @Title: updateDot
	 * @Description: 更新页码
	 * @return: void
	 */
	private void updateDot()
	{
		if (mCurPage > 0 && mCurPage <= mTotPage)
		{
			mDotGroup.setDotTotalNumber(mTotPage);
			mDotGroup.setDotCurrentNumber(mCurPage);
		}
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
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyPageDown();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyPageUp();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRight();
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
	private boolean doKeyDown()
	{
		if (isPlaybackItemsFocus())
		{
			int curIndex = getPlaybackItemFocusIndex();
			
			if (curIndex >=0 && curIndex <= 3)
			{
				if (mCount-(mCurPage-1)*mSize-1 > 3)
				{
					curIndex += 4;
				}
			}
			else if (curIndex >=4 && curIndex <= 7)
			{
				if (mCount-(mCurPage-1)*mSize-1 > 7)
				{
					curIndex += 4;
				}
			}
			
			if (curIndex > mCount-(mCurPage-1)*mSize-1)
			{
				curIndex = mCount-(mCurPage-1)*mSize-1;
			}
			mPlaybacks[curIndex].requestFocus();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyUp
	 * @Description: 响应上建
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyUp()
	{
		if (isPlaybackItemsFocus())
		{
			int curIndex = getPlaybackItemFocusIndex();
			
			if (curIndex >=4 && curIndex <= 11)
			{
				curIndex -= 4;
				mPlaybacks[curIndex].requestFocus();
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
		if (isPlaybackItemsFocus())
		{
			if (mCurPage < mTotPage)
			{
				for (int i=0; i<12; i++)
				{
					mTitle.setFocusable(true);
					mPlaybacks[i].setFocusable(false);
					mPlaybacks[i].setVisibility(View.INVISIBLE);
				}
				
				mState = 2;
				mCurPage++;
				freshChannelList();
			}
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
		if (isPlaybackItemsFocus())
		{
			if (mCurPage > 1)
			{
				for (int i=0; i<12; i++)
				{
					mTitle.setFocusable(true);
					mPlaybacks[i].setFocusable(false);
					mPlaybacks[i].setVisibility(View.INVISIBLE);
				}
				
				mState = 2;
				mCurPage--;
				freshChannelList();
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
		if (isPlaybackItemsFocus())
		{
			int curIndex = getPlaybackItemFocusIndex();
			
			if (mCurPage == 1 && mTotPage == 1)
			{
				if (curIndex == 0)
				{
					mPlaybacks[mCount-1].requestFocus();
				}
				else 
				{
					curIndex--;
					mPlaybacks[curIndex].requestFocus();
				}
			}
			else if (mTotPage > 1)
			{
				if (mCurPage > 1)
				{
					if (curIndex == 0 || curIndex == 4 || curIndex == 8)
					{
						for (int i=0; i<12; i++)
						{
							mTitle.setFocusable(true);
							mPlaybacks[i].setFocusable(false);
							mPlaybacks[i].setVisibility(View.INVISIBLE);
						}
						
						if (curIndex == 0)
						{
							mState = 6;
						}
						else if (curIndex == 4)
						{
							mState = 7;
						}
						else if (curIndex == 8)
						{
							mState = 8;
						}
						
						mCurPage--;
						freshChannelList();
					}
					else
					{
						curIndex--;
						mPlaybacks[curIndex].requestFocus();
					}
				}
				else
				{
					if (curIndex != 0 && curIndex != 4 && curIndex != 8)
					{
						curIndex--;
						mPlaybacks[curIndex].requestFocus();
					}
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
		if (isPlaybackItemsFocus())
		{
			int curIndex = getPlaybackItemFocusIndex();
			
			if (mCurPage == 1 && mTotPage == 1)
			{
				if (curIndex == mCount-1)
				{
					mPlaybacks[0].requestFocus();
				}
				else 
				{
					curIndex++;
					mPlaybacks[curIndex].requestFocus();
				}
			}
			else if (mTotPage > 1)
			{
				if (mCurPage < mTotPage)
				{
					if (curIndex == 3 || curIndex == 7 || curIndex == 11)
					{
						for (int i=0; i<12; i++)
						{
							mTitle.setFocusable(true);
							mPlaybacks[i].setFocusable(false);
							mPlaybacks[i].setVisibility(View.INVISIBLE);
						}
						
						if (curIndex == 3)
						{
							mState = 3;
						}
						else if (curIndex == 7)
						{
							mState = 4;
						}
						else if (curIndex == 11)
						{
							mState = 5;
						}
						
						mCurPage++;
						freshChannelList();
					}
					else
					{
						curIndex++;
						mPlaybacks[curIndex].requestFocus();
					}
				}
				else
				{
					if (curIndex < mCount-1-(mCurPage-1)*mSize && curIndex != 3 && curIndex != 7)
					{
						curIndex++;
						mPlaybacks[curIndex].requestFocus();
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: isPlaybackItemsFocus
	 * @Description: 判断Item是否选中
	 * @return
	 * @return: boolean
	 */
	private boolean isPlaybackItemsFocus()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mPlaybacks[i])
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: getPlaybackItemFocusIndex
	 * @Description: 获取Item的选中index
	 * @return
	 * @return: int
	 */
	private int getPlaybackItemFocusIndex()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mPlaybacks[i])
			{
				return i;
			}
		}
		return 0;
	}
}
