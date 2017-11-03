/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityDramaOther.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 点播资源(如:体育 综艺等等, 除去电影 电视剧)选集Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:47:12
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
import com.szgvtv.ead.app.taijietemplates.ui.view.DotGroup;
import com.szgvtv.ead.app.taijietemplates.ui.view.HorizontalListView;
import com.szgvtv.ead.app.taijietemplates.ui.view.SelectDrama;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class ActivityDramaOther extends ActivityBase implements OnItemClickListener, OnClickListener, OnFocusChangeListener, AnimationListener
{
	private Context            mContext;                           //上下文
	private ImageView          mFocus;                             //放大image
	private HorizontalListView mHListView;                         //横向ListView(解决第二次进入选集页面, 剧集图片边角显示异常的问题)
	private SelectDrama        mDramas[] = new SelectDrama[10];    //选集Item
	private AdaptDrama         mAdaptDrama;                        //剧集Adapt
	private DotGroup           mDotGroup;                          //页码
	private Animation          mScaleBig;                          //放大动画
	private Animation          mScaleSmall;                        //缩小动画
	private VideoItem          mVideoItem;                         //点播资源
	private VideoInfo          mVideoInfo;                         //数据库资源item
	private ArrayList<String>  mCurList = new ArrayList<String>(); //剧集导航栏当前列表信息
	private ArrayList<String>  mTotList = new ArrayList<String>(); //剧集导航栏所有列表信息
	private int                mCurPage = 0;                       //当前页数
	private int                mTotPage = 0;                       //总页数
	private int                mCurNaviPage = 0;                   //剧集导航栏当前页
	private int                mSize = 10;                         //每页最多显示个数
	private int                mCount = 0;                         //总个数
	private int                mIndex = 0;                         //播放剧集索引
	private int                mState = 0;                         //下载状态
	private int                mHindex = 0;                        //剧集导航栏索引
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_drama_other);
		
		initViews();
		getVideoInfo();
	}
	
	private void initViews()
	{
		mHListView = (HorizontalListView) findViewById(R.id.id_drama_tv_hlistview);
		mFocus = (ImageView) findViewById(R.id.id_drama_other_focus);
		mDotGroup = (DotGroup) findViewById(R.id.id_drama_other_dot);
		mDramas[0] = (SelectDrama) findViewById(R.id.id_drama_other_item0);
		mDramas[1] = (SelectDrama) findViewById(R.id.id_drama_other_item1);
		mDramas[2] = (SelectDrama) findViewById(R.id.id_drama_other_item2);
		mDramas[3] = (SelectDrama) findViewById(R.id.id_drama_other_item3);
		mDramas[4] = (SelectDrama) findViewById(R.id.id_drama_other_item4);
		mDramas[5] = (SelectDrama) findViewById(R.id.id_drama_other_item5);
		mDramas[6] = (SelectDrama) findViewById(R.id.id_drama_other_item6);
		mDramas[7] = (SelectDrama) findViewById(R.id.id_drama_other_item7);
		mDramas[8] = (SelectDrama) findViewById(R.id.id_drama_other_item8);
		mDramas[9] = (SelectDrama) findViewById(R.id.id_drama_other_item9);
		
		mDotGroup.setDotTop(660);
		mHListView.setOnItemClickListener(this);
		mAdaptDrama = new AdaptDrama(mContext, mCurList);
		mHListView.setAdapter(mAdaptDrama);
		
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
		mVideoItem = bundle.getParcelable(FlagConstant.ToActivityDramaOtherKey);
		mVideoInfo = VideoInfoTable.getPlayHistory(mVideoItem.getVideoCode());
		
		if (mVideoItem != null)
		{
			mCount = mVideoItem.getDramaList().size();
			if (mCount > 0)
			{
				mTotPage = (mCount-1)/mSize + 1;
				if (mVideoInfo == null)
				{
					mCurPage = 1;
					mCurNaviPage = 1;
				}
				else
				{
					mState = 1;
					int index = getHistoryIndex(mVideoInfo);
					mIndex = index%mSize;
					mCurPage = index/mSize + 1;
					mCurNaviPage  = (mCurPage-1)/8 + 1;
				}
				//freshVideoDrama();
				getDramaNavigationTotalList();
				freshDramaNavigation();
				mHandler.sendEmptyMessage(FlagConstant.NAVIGATION_FINISHED);
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
		mAdaptDrama.notifyDataSetChanged();
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
				mDramas[i].setNumberName(item.getNumber(), item.getDramaName());
				mDramas[i].setVisibility(View.VISIBLE);
			}
			else
			{
				mDramas[i].setVisibility(View.INVISIBLE);
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
		case 1:  //历史记录索引
		{
			curIndex = mIndex;
			break;
		}
		case 0:  //第一次加载
		case 2:  //下一页/上一页
		case 3:  //第一排最后一个向右
		{
			curIndex = 0;
			break;
		}
		case 4:  //第二排最后一个向右
		{
			if (mCount-1-(mCurPage-1)*mSize < 5)  
			{
				curIndex = mCount-1-(mCurPage-1)*mSize;
			}
			else
			{
				curIndex = 5;
			}
			break;
		}
		case 5:  //第一排第一个向左
		{
			curIndex = 4;
			break;
		}
		case 6:  //第二排第一个向左
		{
			curIndex = 9;
			break;
		}
		default:
			break;
		}
		
		mDramas[curIndex].requestFocus();
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
		case R.id.id_drama_other_item0:
		case R.id.id_drama_other_item1:
		case R.id.id_drama_other_item2:
		case R.id.id_drama_other_item3:
		case R.id.id_drama_other_item4:
		case R.id.id_drama_other_item5:
		case R.id.id_drama_other_item6:
		case R.id.id_drama_other_item7:
		case R.id.id_drama_other_item8:
		case R.id.id_drama_other_item9:
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
		case R.id.id_drama_other_item0:
		case R.id.id_drama_other_item1:
		case R.id.id_drama_other_item2:
		case R.id.id_drama_other_item3:
		case R.id.id_drama_other_item4:
		case R.id.id_drama_other_item5:
		case R.id.id_drama_other_item6:
		case R.id.id_drama_other_item7:
		case R.id.id_drama_other_item8:
		case R.id.id_drama_other_item9:
		{
			int curIndex = getDramaItemFocusIndex();
			int index = curIndex + (mCurPage-1)*mSize;
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
		int curIndex = getDramaItemFocusIndex();
		if (curIndex >=5 && curIndex <= 9)
		{
			curIndex -= 5;
			mDramas[curIndex].requestFocus();
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
		if (mCurPage < mTotPage)
		{
			mState = 2;
			mCurPage++;
			freshVideoDrama();
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
		if (mCurPage > 1)
		{
			mState = 2;
			mCurPage--;
			freshVideoDrama();
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
		int curIndex = getDramaItemFocusIndex();
		
		if (mCurPage == 1 && mTotPage == 1)
		{
			if (curIndex == 0)
			{
				mDramas[mCount-1].requestFocus();
			}
			else 
			{
				curIndex--;
				mDramas[curIndex].requestFocus();
			}
		}
		else if (mTotPage > 1)
		{
			if (mCurPage > 1)
			{
				if (curIndex == 0 || curIndex == 5)
				{
					if (curIndex == 0)
					{
						mState = 5;
					}
					else if (curIndex == 5)
					{
						mState = 6;
					}
					
					mCurPage--;
					freshVideoDrama();
				}
				else
				{
					curIndex--;
					mDramas[curIndex].requestFocus();
				}
			}
			else
			{
				if (curIndex != 0 && curIndex != 5)
				{
					curIndex--;
					mDramas[curIndex].requestFocus();
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
		int curIndex = getDramaItemFocusIndex();
		
		if (mCurPage == 1 && mTotPage == 1)
		{
			if (curIndex == mCount-1)
			{
				mDramas[0].requestFocus();
			}
			else 
			{
				curIndex++;
				mDramas[curIndex].requestFocus();
			}
		}
		else if (mTotPage > 1)
		{
			if (mCurPage < mTotPage)
			{
				if (curIndex == 4 || curIndex == 9)
				{
					if (curIndex == 4)
					{
						mState = 3;
					}
					else if (curIndex == 9)
					{
						mState = 4;
					}
					
					mCurPage++;
					freshVideoDrama();
				}
				else
				{
					curIndex++;
					mDramas[curIndex].requestFocus();
				}
			}
			else
			{
				if (curIndex < mCount-1-(mCurPage-1)*mSize && curIndex != 4)
				{
					curIndex++;
					mDramas[curIndex].requestFocus();
				}
			}
		}
		return true;
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
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.NAVIGATION_FINISHED, 100);
				}
				break;
			}
			default:
				break;
			}
		}
	};
}
