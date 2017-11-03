/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DemandMenu.java
 * @Prject: TaijieTemplates1_gytv
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 点播菜单(选集)
 * @author: zhaoqy
 * @date: 2014-12-8 下午9:00:11
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DemandMenu extends RelativeLayout implements Runnable, OnFocusChangeListener, AnimationListener
{
	public static final int PERIOD_MENU_SHOW = 4000;     //间隔时间为5秒
	private Context     mContext;                        //上下文
	private ImageView   mLeft;                           //左方向图标
	private ImageView   mRight;                          //右方向图标
	private ImageView   mFocus;                          //放大image
	private SelectDrama mDramas [] = new SelectDrama[5]; //选集Item
	private Animation   mScaleBig;                       //放大动画
	private Animation   mScaleSmall;                     //缩小动画
	private VideoItem   mVideoItem;                      //点播资源
	private Handler     mHandler = null;                 //handler
	private int         mCurPage = 0;                    //当前页数
	private int         mTotPage = 0;                    //总页数
	private int         mIndex = 0;                      //选集索引
	private int         mSize = 5;                       //每页个数
	private int         mCount = 0;                      //总个数
	private int         mPlayIndex = 0;                  //设置当前播放索引          
	
	public DemandMenu(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_demand_menu, this);
		mLeft = (ImageView) findViewById(R.id.id_drama_left);
		mRight = (ImageView) findViewById(R.id.id_drama_right);
		mFocus = (ImageView) findViewById(R.id.id_drama_focus);
		mDramas[0] = (SelectDrama) findViewById(R.id.id_drama_item0);
		mDramas[1] = (SelectDrama) findViewById(R.id.id_drama_item1);
		mDramas[2] = (SelectDrama) findViewById(R.id.id_drama_item2);
		mDramas[3] = (SelectDrama) findViewById(R.id.id_drama_item3);
		mDramas[4] = (SelectDrama) findViewById(R.id.id_drama_item4);
		
		for (int i=0; i<5; i++)
		{
			mDramas[i].setOnFocusChangeListener(this);
			mDramas[i].setVisibility(View.INVISIBLE);
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
			View view = mDramas[mIndex];
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
		case R.id.id_drama_item0:
		case R.id.id_drama_item1:
		case R.id.id_drama_item2:
		case R.id.id_drama_item3:
		case R.id.id_drama_item4:
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
	
	/**
	 * 
	 * @Title: setVideoItem
	 * @Description: 设置点播资源
	 * @param videoItem
	 * @return: void
	 */
	public void setVideoItem(VideoItem videoItem)
	{
		mVideoItem = videoItem;
	}
	
	/**
	 * 
	 * @Title: setPlayIndex
	 * @Description: 设置当前播放索引
	 * @param playIndex
	 * @return: void
	 */
	public void setPlayIndex(int playIndex)
	{
		mPlayIndex = playIndex;
	}
	
	/**
	 * 
	 * @Title: getPlayIndex
	 * @Description: 获取当前播放索引
	 * @return
	 * @return: int
	 */
	public int getPlayIndex()
	{
		int index = (mCurPage-1)*mSize + mIndex;
		return index;
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
				if (mPlayIndex == i + (mCurPage-1)*mSize)
				{
					mDramas[i].setNumber(item.getNumber() + "  " + getResources().getString(R.string.view_playing));
				}
				else
				{
					if (mVideoItem.getVodtype().equals("2"))
					{
						mDramas[i].setNumber(item.getNumber());
					}
					else if (mVideoItem.getVodtype().equals("3"))
					{
						mDramas[i].setNumberName(item.getNumber(), item.getDramaName());
					}
				}
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
	 * @Title: freshPage
	 * @Description: 刷新页码
	 * @return: void
	 */
	private void freshPage()
	{
		if (mCurPage > 0 && mCurPage <= mTotPage)
		{
			if (mCurPage == 1)
			{
				mLeft.setVisibility(View.INVISIBLE);
			}
			else
			{
				mLeft.setVisibility(View.VISIBLE);
			}
			
			if (mCurPage == mTotPage)
			{
				mRight.setVisibility(View.INVISIBLE);
			}
			else
			{
				mRight.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 
	 * @Title: doKeyMenu
	 * @Description: 响应菜单键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyMenu()
	{
		resetTime();
		show();
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyLeft()
	{
		resetTime();
		
		if (mIndex == 0) //索引等于0
		{
			if (mTotPage == 1)     //总页数等于1
			{
				mIndex = mCount-1-(mCurPage-1)*mSize;
				mDramas[mIndex].requestFocus();
			}
			else if (mTotPage > 1)  //总页数大于1
			{
				if (mCurPage == 1)  //当前页数等于1
				{
					mCurPage = mTotPage;
					freshVideoDrama();
					freshPage();
					mIndex = mCount-1-(mCurPage-1)*mSize;
					mDramas[mIndex].requestFocus();
				}
				else if (mCurPage > 1)  //当前页数大于1
				{
					mCurPage--;
					freshVideoDrama();
					freshPage();
					mIndex = 4;
					mDramas[mIndex].requestFocus();
				}
			}
		}
		else if (mIndex > 0)  //索引大于0
		{
			mIndex--;
			mDramas[mIndex].requestFocus();
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
	public boolean onKeyRight()
	{
		resetTime();
		
		if (mTotPage == 1)
		{
			if (mIndex == mCount-1-(mCurPage-1)*mSize)
			{
				mIndex = 0;
				mDramas[mIndex].requestFocus();
			}
			else
			{
				mIndex++;
				mDramas[mIndex].requestFocus();
			}
		}
		else if (mTotPage > 1)
		{
			if (mCurPage == mTotPage)
			{
				if (mIndex == mCount-1-(mCurPage-1)*mSize)
				{
					mCurPage = 1;
					freshVideoDrama();
					freshPage();
					mIndex = 0;
					mDramas[mIndex].requestFocus();
				}
				else 
				{
					mIndex++;
					mDramas[mIndex].requestFocus();
				}
			}
			else if (mCurPage < mTotPage)
			{
				if (mIndex == 4)
				{
					mCurPage++;
					freshVideoDrama();
					freshPage();
					mIndex = 0;
					mDramas[mIndex].requestFocus();
				}
				else
				{
					mIndex++;
					mDramas[mIndex].requestFocus();
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
	public boolean onKeyPageUp()
	{
		resetTime();
		
		if (mTotPage > 1)
		{
			if (mCurPage == 1)
			{
				mCurPage = mTotPage;
			}
			else if (mCurPage > 1)
			{
				mCurPage--;
			}
			freshVideoDrama();
			freshPage();
			mIndex = 0;
			mDramas[mIndex].requestFocus();
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
	public boolean onKeyPageDown()
	{
		resetTime();
		
		if (mTotPage > 1)
		{
			if (mCurPage == mTotPage)
			{
				mCurPage = 1;
			}
			else if (mCurPage < mTotPage)
			{
				mCurPage++;
			}
			freshVideoDrama();
			freshPage();
			mIndex = 0;
			mDramas[mIndex].requestFocus();
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: show
	 * @Description: 显示该view
	 * @return: void
	 */
	public void show()
	{
		if (mVideoItem != null)
		{
			mCount = mVideoItem.getDramaList().size();
			if (mCount > 0)
			{
				mIndex = mPlayIndex%mSize;
				mCurPage = mPlayIndex/mSize + 1;
				mTotPage = (mCount-1)/mSize + 1;
				
				freshVideoDrama();
				freshPage();
				mDramas[mIndex].requestFocus();
			}
			setVisibility(View.VISIBLE);
		}
		else
		{
			setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 
	 * @Title: hide
	 * @Description: 隐藏该view
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
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: 重置定时器
	 * @return: void
	 */
	private void resetTime()
	{
		if (mHandler != null) //重置
		{
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}	
		mHandler = new Handler();
		mHandler.postDelayed(this, PERIOD_MENU_SHOW);
	}
	
	@Override
	public void run() 
	{
		hide();
	}
}
