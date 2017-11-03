/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityDramaTV.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 点播资源(电视剧)选集Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:49:54
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptDrama;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfo;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfoTable;
import com.szgvtv.ead.app.taijietemplates.ui.view.HorizontalListView;
import com.szgvtv.ead.app.taijietemplates.ui.view.SelectDrama;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;

public class ActivityDramaTV extends ActivityBase implements OnItemClickListener, OnClickListener, OnFocusChangeListener, AnimationListener
{
	private Context            mContext;                           //上下文
	private ImageView          mLeft;                              //左方向图标
	private ImageView          mRight;                             //右方向图标
	private ImageView          mFocus;                             //放大image
	private HorizontalListView mHListView;                         //横向ListView
	private SelectDrama        mDramas[] = new SelectDrama[10];    //选集Item
	private AdaptDrama         mAdapt;                             //剧集Adapt
	private Animation          mScaleBig;                          //放大动画
	private Animation          mScaleSmall;                        //缩小动画
	private VideoItem          mVideoItem;                         //点播资源
	private VideoInfo          mVideoInfo;                         //数据库资源item
	private ArrayList<String>  mCurList = new ArrayList<String>(); //剧集导航栏当前列表信息
	private ArrayList<String>  mTotList = new ArrayList<String>(); //剧集导航栏所有列表信息
	private int                mCurPage = 0;                       //当前页数
	private int                mTotPage = 0;                       //总页数
	private int                mCurNaviPage = 0;                   //剧集导航栏当前页
	private int                mTotNaviPage = 0;                   //剧集导航栏所有页
	private int                mSize = 10;                         //每页最多显示个数
	private int                mCount = 0;                         //总个数
	private int                mIndex = 0;                         //播放剧集索引
	private int                mHindex = 0;                        //剧集导航栏索引
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_drama_tv);
		
		initViews();
		getVideoInfo();
	}
	
	private void initViews()
	{
		mLeft = (ImageView) findViewById(R.id.id_drama_tv_left);
		mRight = (ImageView) findViewById(R.id.id_drama_tv_right);
		mFocus = (ImageView) findViewById(R.id.id_drama_tv_focus);
		mHListView = (HorizontalListView) findViewById(R.id.id_drama_tv_hlistview);
		
		mDramas[0] = (SelectDrama) findViewById(R.id.id_drama_tv_item0);
		mDramas[1] = (SelectDrama) findViewById(R.id.id_drama_tv_item1);
		mDramas[2] = (SelectDrama) findViewById(R.id.id_drama_tv_item2);
		mDramas[3] = (SelectDrama) findViewById(R.id.id_drama_tv_item3);
		mDramas[4] = (SelectDrama) findViewById(R.id.id_drama_tv_item4);
		mDramas[5] = (SelectDrama) findViewById(R.id.id_drama_tv_item5);
		mDramas[6] = (SelectDrama) findViewById(R.id.id_drama_tv_item6);
		mDramas[7] = (SelectDrama) findViewById(R.id.id_drama_tv_item7);
		mDramas[8] = (SelectDrama) findViewById(R.id.id_drama_tv_item8);
		mDramas[9] = (SelectDrama) findViewById(R.id.id_drama_tv_item9);
		
		mHListView.setOnItemClickListener(this);
		mAdapt = new AdaptDrama(mContext, mCurList);
		mHListView.setAdapter(mAdapt);
		
		for (int i=0; i<10; i++)
		{
			mDramas[i].setOnClickListener(this);
			mDramas[i].setOnFocusChangeListener(this);
			mDramas[i].setVisibility(View.INVISIBLE);
		}
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
		mVideoItem = bundle.getParcelable(FlagConstant.ToActivityDramaTVKey);
		mVideoInfo = VideoInfoTable.getPlayHistory(mVideoItem.getVideoCode());
		
		if (mVideoItem != null)
		{
			mCount = mVideoItem.getDramaList().size();
			if (mCount > 0)
			{
				mTotPage = (mCount-1)/mSize + 1;
				mTotNaviPage = (mTotPage-1)/8 + 1;
				
				if (mVideoInfo == null)
				{
					mCurPage = 1;
					mCurNaviPage = 1;
				}
				else
				{
					int index = getHistoryIndex(mVideoInfo);
					mIndex = index%mSize;
					mCurPage = index/mSize + 1;
					mCurNaviPage  = (mCurPage-1)/8 + 1;
				}
				updateDot();
				getDramaNavigationTotalList();
				freshDramaNavigation();
				mHandler.sendEmptyMessage(FlagConstant.NAVIGATION_FINISHED);
			}
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
			for (int i=0; i<mCount; i++)
			{
				if (videoInfo.getDramaCode().equals(mVideoItem.getDramaList().get(i).getDramaCode()))
				{
					return i;
				}
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @Title: freshVideoDrama
	 * @Description: 刷新剧集
	 * @return: void
	 */
	private void freshVideoDrama()
	{
		for (int i=0; i<mSize; i++)
		{
			if(i + (mCurPage-1)*mSize < mCount)
			{
				DramaItem item = mVideoItem.getDramaList().get(i + (mCurPage-1)*mSize);
				if (UILApplication.mImageLoader != null)
				{
					UILApplication.mImageLoader.displayImage(item.getScreenshots(), mDramas[i].getIcon(), UILApplication.mSelectDramaOption);
				}
				mDramas[i].setNumber(item.getNumber());
				mDramas[i].setVisibility(View.VISIBLE);
				mDramas[i].setFocusable(true);
			}
			else
			{
				mDramas[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	/**
	 * 
	 * @Title: getDramaNavigationTotalList
	 * @Description: 获取剧集导航栏所有数据
	 * @return: void
	 */
	private void getDramaNavigationTotalList()
	{
		mTotList.clear();
		for (int i=0; i<mTotPage; i++)
		{
			int start = i*mSize + 1;
			int end = (i + 1)*mSize;
			
			if (end > mCount)
			{
				end = mCount;
			}
			
			String item = new String();
			item = start + "-" + end;
			mTotList.add(item);
		}
	}
	
	/**
	 * 
	 * @Title: freshDramaNavigation
	 * @Description: 刷新剧集导航栏当前页
	 * @return: void
	 */
	private void freshDramaNavigation()
	{
		mCurList.clear();
		for (int i=0; i<8; i++)
		{
			if (i + (mCurNaviPage-1)*8 < mTotPage)
			{
				String item = new String();
				item = mTotList.get(i + (mCurNaviPage-1)*8);
				mCurList.add(item);
			}
		}
		mAdapt.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @Title: updateDot
	 * @Description: 更新页码
	 * @return: void
	 */
	private void updateDot()
	{
		if (mCurPage > 0 && mTotPage > 0)
		{
			if (mTotPage > 8)
			{
				mLeft.setVisibility(View.VISIBLE);
				mRight.setVisibility(View.VISIBLE);
			}
			else
			{
				mLeft.setVisibility(View.INVISIBLE);
				mRight.setVisibility(View.INVISIBLE);
			}
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
	public void onFocusChange(View v, boolean hasFocus) 
	{
		switch (v.getId()) 
		{
		case R.id.id_drama_tv_item0:
		case R.id.id_drama_tv_item1:
		case R.id.id_drama_tv_item2:
		case R.id.id_drama_tv_item3:
		case R.id.id_drama_tv_item4:
		case R.id.id_drama_tv_item5:
		case R.id.id_drama_tv_item6:
		case R.id.id_drama_tv_item7:
		case R.id.id_drama_tv_item8:
		case R.id.id_drama_tv_item9:
		{
			if (!hasFocus) 
			{
				v.setSelected(false);
				mFocus.setVisibility(View.GONE);
				mScaleSmall = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_DRAMA_SMALL_ANIMS);
				mScaleSmall.setFillAfter(false);
				mScaleSmall.setAnimationListener(this);
				v.startAnimation(mScaleSmall);
			}
			else
			{
				v.setSelected(true);
				mScaleBig = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_DRAMA_BIG_ANIMS);
				mScaleBig.setFillAfter(true);
				mScaleBig.setAnimationListener(this);
				v.startAnimation(mScaleBig);
				v.bringToFront();
			}
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//监听HorizontalListView, 否则点击就会挂掉
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_drama_tv_item0:
		case R.id.id_drama_tv_item1:
		case R.id.id_drama_tv_item2:
		case R.id.id_drama_tv_item3:
		case R.id.id_drama_tv_item4:
		case R.id.id_drama_tv_item5:
		case R.id.id_drama_tv_item6:
		case R.id.id_drama_tv_item7:
		case R.id.id_drama_tv_item8:
		case R.id.id_drama_tv_item9:
		{
			int curIndex = getDramaItemFocusIndex();
			int index = curIndex + (mCurPage-1)*mSize;
			Logcat.d(FlagConstant.TAG, " ==== ActivityDramaTV ==== playIndex: " + index);
			Intent intent = new Intent(this, ActivityPlayVideo.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(FlagConstant.ToActivityPlayVideoKey, mVideoItem);
			bundle.putInt(FlagConstant.DramaIndexKey, index);
			intent.putExtras(bundle);
			startActivity(intent);
			sendMsgBroadCast(index);
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: sendMsgBroadCast
	 * @Description: 发送消息广播(为了返回到详情页面时, 光标放在当前播放剧集上)
	 * @return: void
	 */
	private void sendMsgBroadCast(int index)
	{
		Intent intent = new Intent();
		intent.setAction(Constant.ACTION_FRESH_DRAMA);
		Bundle bundle = new Bundle();
		bundle.putInt(FlagConstant.DramaIndexKey, index);
		intent.putExtras(bundle);
		mContext.sendBroadcast(intent);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		boolean nRet = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyBack();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
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
	 * @Title: doKeyBack
	 * @Description: 响应返回键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyBack()
	{
		finish();
		overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
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
		if (isHListviewFocus())
		{
			mHListView.getChildAt(mHindex).setSelected(true);
			mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(false);
			mDramas[0].requestFocus();
		}
		else if (isDramaItemItemsFocus())
		{
			int curIndex = getDramaItemFocusIndex();
			if (curIndex >=0 && curIndex <= 4)
			{
				if (mCount-1-(mCurPage-1)*mSize > 4)
				{
					curIndex += 5;
				}
				
				if (curIndex > mCount-1-(mCurPage-1)*mSize)
				{
					curIndex = mCount-1-(mCurPage-1)*mSize;
				}
				mDramas[curIndex].requestFocus();
			}
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
		if (isDramaItemItemsFocus())
		{
			int curIndex = getDramaItemFocusIndex();
			if (curIndex >=0 && curIndex <= 4)
			{
				mHListView.getChildAt(mHindex).setSelected(false);
				mHListView.getChildAt(mHindex).requestFocus();
				mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
			}
			else if (curIndex >=5 && curIndex <= 9)
			{
				curIndex -= 5;
				mDramas[curIndex].requestFocus();
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
		if (isHListviewFocus())
		{
			if (mTotNaviPage == 1)
			{
				return true;
			}
			
			for (int i=0; i<10; i++)
			{
				mDramas[i].setFocusable(false);
			}
			
			if (mCurNaviPage < mTotNaviPage)
			{
				mCurNaviPage++;
			}
			else if (mCurNaviPage == mTotNaviPage)
			{
				mCurNaviPage = 1;
			}
			mCurPage = (mCurNaviPage - 1)*8 + 1;
			freshDramaNavigation();
			mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
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
		if (isHListviewFocus())
		{
			if (mTotNaviPage == 1)
			{
				return true;
			}
			
			for (int i=0; i<10; i++)
			{
				mDramas[i].setFocusable(false);
			}
			
			if (mCurNaviPage == 1)
			{
				mCurNaviPage = mTotNaviPage;
			}
			else if (mCurNaviPage > 1)
			{
				mCurNaviPage--;
			}
			mCurPage = (mCurNaviPage - 1)*8 + 1;
			freshDramaNavigation();
			mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
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
		if (isHListviewFocus())
		{
			if (mTotList.size() == 1)
			{
				return true;
			}
			
			mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(false);
			if (mCurNaviPage == 1)
			{
				if (mCurPage == 1)
				{
					for (int i=0; i<10; i++)
					{
						mDramas[i].setFocusable(false);
					}
					mCurPage = mTotPage;
					mCurNaviPage = mTotNaviPage;
					freshDramaNavigation();
					mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
				}
				else if (mCurPage > 1)
				{
					mCurPage--;
					freshVideoDrama();
					mHindex = mCurPage%8 - 1;
					if (mHindex < 0)
					{
						mHindex = 7;
					}
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
				}
			}
			else if (mCurNaviPage > 1)
			{
				if (mCurPage%8 == 1)
				{
					for (int i=0; i<10; i++)
					{
						mDramas[i].setFocusable(false);
					}
					mCurPage--;
					mCurNaviPage--;
					freshDramaNavigation();
					mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
				}
				else
				{
					mCurPage--;
					freshVideoDrama();
					mHindex = mCurPage%8 - 1;
					if (mHindex < 0)
					{
						mHindex = 7;
					}
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
				}
			}
		}
		else if (isDramaItemItemsFocus())
		{
			int curIndex = getDramaItemFocusIndex();
			if (curIndex == 0)
			{
				if (mCurPage == mTotPage)
				{
					mDramas[mCount-1-(mCurPage-1)*mSize].requestFocus();
				}
				else
				{
					mDramas[9].requestFocus();
				}
			}
			else 
			{
				curIndex--;
				mDramas[curIndex].requestFocus();
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
		if (isHListviewFocus())
		{
			if (mTotList.size() == 1)
			{
				return true;
			}
			
			mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(false);
			if (mCurPage < mTotPage)
			{
				if (mCurNaviPage < mTotNaviPage)
				{
					mCurPage++;
					if (mCurPage%8 == 1)
					{
						for (int i=0; i<10; i++)
						{
							mDramas[i].setFocusable(false);
						}
						mCurNaviPage++;
						freshDramaNavigation();
						mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
					}
					else
					{
						freshVideoDrama();
						mHindex = mCurPage%8 - 1;
						if (mHindex < 0)
						{
							mHindex = 7;
						}
						mHListView.getChildAt(mHindex).requestFocus();
						mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
					}
				}
				else if (mCurNaviPage == mTotNaviPage)
				{
					mCurPage++;
					freshVideoDrama();
					mHindex = mCurPage%8 - 1;
					if (mHindex < 0)
					{
						mHindex = 7;
					}
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
				}
			}
			else if (mCurPage == mTotPage)
			{
				for (int i=0; i<10; i++)
				{
					mDramas[i].setFocusable(false);
				}
				mCurPage = 1;
				mCurNaviPage = 1;
				freshDramaNavigation();
				mHandler.sendEmptyMessage(FlagConstant.SWITCH_FINISHED);
			}
		}
		else if (isDramaItemItemsFocus())
		{
			int curIndex = getDramaItemFocusIndex();
			if (curIndex == mCount-1-(mCurPage-1)*mSize || curIndex == 9)
			{
				mDramas[0].requestFocus();
			}
			else 
			{
				curIndex++;
				mDramas[curIndex].requestFocus();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: isHListviewFocus
	 * @Description: mHListView是否选中
	 * @return
	 * @return: boolean
	 */
	private boolean isHListviewFocus()
	{
		if (getCurrentFocus().getParent() == mHListView)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: isDramaItemItemsFocus
	 * @Description: VodItems是否选中
	 * @return
	 * @return: boolean
	 */
	private boolean isDramaItemItemsFocus()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mDramas[i])
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
	private int getDramaItemFocusIndex()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mDramas[i])
			{
				return i;
			}
		}
		return 0;
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
			case FlagConstant.NAVIGATION_FINISHED:
			{
				int tempHindex = mCurPage%8 - 1;
				
				if (tempHindex != mHindex)
				{
					if (mHListView.getChildAt(mHindex) != null)
					{
						mHListView.getChildAt(mHindex).setSelected(false);
						mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
					}
					
					mHindex = tempHindex;
				}
				
				if (mHindex < 0)
				{
					mHindex = 7;
				}
				
				if (mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).setSelected(true);
					mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(false);
					
					freshVideoDrama();
					mDramas[mIndex].requestFocus();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.NAVIGATION_FINISHED, 100);
				}
				break;
			}
			case FlagConstant.DRAMA_FINISHED:
			{
				break;
			}
			case FlagConstant.SWITCH_FINISHED:
			{
				mHindex = mCurPage%8 - 1;
				
				if (mHindex < 0)
				{
					mHindex = 7;
				}
				
				if(mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.drama_navigation).setSelected(true);
					freshVideoDrama();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.SWITCH_FINISHED, 100);
				}
				break;
			}
			default:
				break;
			}
		}
	};
}
