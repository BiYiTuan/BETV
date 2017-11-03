/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveMenu.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 直播菜单(包括直播频道和节目预告)
 * @author: zhaoqy
 * @date: 2014-9-1 下午8:05:55
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptChannel;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptPreview;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.TimeUtil;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveMenu  extends RelativeLayout implements Runnable
{
	public static final int PERIOD_MENU_SHOW = 6000;                                    //间隔时间为5秒
	private Context                    mContext;                                        //上下文
	private TextView                   mTotal;                                          //总数
	private TextView                   mPage;                                           //页码
	private TextView                   mPlaying;                                        //正在播放内容
	private ListView                   mChannelListView;                                //频道ListView
	private ListView                   mPreviewListView;                                //预告ListView
	private AdaptChannel               mAdaptChannel;                                   //频道Adapt
	private AdaptPreview               mAdaptPreview;                                   //预告Adapt
	private Handler                    mHandler = null;                                 //handler
	private ArrayList<LiveChannelItem> mCurChannels = new ArrayList<LiveChannelItem>(); //当前频道列表
	private ArrayList<LiveChannelItem> mTotChannels = new ArrayList<LiveChannelItem>(); //所有频道列表
	private ArrayList<LivePreviewItem> mCurPreviews = new ArrayList<LivePreviewItem>(); //当前预告列表
	private ArrayList<LivePreviewItem> mTotPreviews = new ArrayList<LivePreviewItem>(); //所有预告列表
	private int                        mCount;                                          //总个数
	private int                        mCurPage;                                        //当前页数
	private int                        mTotPage;                                        //总共页数
	private int                        mChannelSize = 10;                               //每页最多显示频道个数
	private int                        mPreviewSize = 9;                                //最多显示预告个数
	
	public LiveMenu(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
		initData();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_live_menu, this);
		mTotal = (TextView) findViewById(R.id.id_live_channel_total);
		mPage = (TextView) findViewById(R.id.id_live_channel_page);
		mPlaying = (TextView) findViewById(R.id.id_live_channel_playing_program);
		mChannelListView = (ListView) findViewById(R.id.id_live_channel_list);
		mPreviewListView = (ListView) findViewById(R.id.id_live_channel_upcoming_program);
	}
	
	private void initData()
	{
		mAdaptChannel = new AdaptChannel(mContext, mCurChannels, mChannelSize);
		mChannelListView.setAdapter(mAdaptChannel);
		mCurChannels.clear();
		mTotChannels.clear();
		
		mAdaptPreview = new AdaptPreview(mContext, mCurPreviews);
		mPreviewListView.setAdapter(mAdaptPreview);
		mCurPreviews.clear();
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
	
	/**
	 * 
	 * @Title: setChannelList
	 * @Description: 设置频道列表
	 * @param channelList
	 * @return: void
	 */
	public void setChannelList(ArrayList<LiveChannelItem> channelList)
	{
		if (mTotChannels.size() <= 0)
		{
			for (int i=0; i<channelList.size(); i++)
			{
				LiveChannelItem item = channelList.get(i);
				mTotChannels.add(item);
			}
		
			mCount = mTotChannels.size();
			mCurPage = 1;
			mTotPage = (mCount-1)/mChannelSize + 1;
			freshChannelList();
		}
	}
	
	/**
	 * 
	 * @Title: setPreviewList
	 * @Description: 设置预告列表
	 * @param preview
	 * @return: void
	 */
	public void setPreviewList(ArrayList<LivePreviewItem> preview)
	{
		mTotPreviews.clear();
		for (int i=0; i<preview.size(); i++)
		{
			LivePreviewItem item = preview.get(i);
			Logcat.d(FlagConstant.TAG, " name: " + item.getName() + ", date: " + item.getDate() + ", time: " + item.getTime());
			mTotPreviews.add(item);
		}
		freshPreviewList();
	}
	
	/**
	 * 
	 * @Title: freshChannelList
	 * @Description: 刷新频道列表
	 * @return: void
	 */
	private void freshChannelList()
	{
		mCurChannels.clear();
		for (int i=0; i<mChannelSize; i++)
		{
			if(i + (mCurPage-1)*mChannelSize < mCount)
			{
				LiveChannelItem item = mTotChannels.get(i + (mCurPage-1)*mChannelSize);
				mCurChannels.add(item);
			}
		}
		mAdaptChannel.setCurrentPage(mCurPage);
		mAdaptChannel.notifyDataSetChanged();
		setCount();
		setPage();
	}
	
	/**
	 * 
	 * @Title: freshPreviewList
	 * @Description: 刷新预告列表
	 * @return: void
	 */
	private void freshPreviewList()
	{
		if (mTotPreviews.size() > 0)
		{
			mCurPreviews.clear();
			int previewIndex = 0;
			int playIndex = TimeUtil.getCurProgramIndex(mTotPreviews);
			if (playIndex >= 0)
			{
				LivePreviewItem one = mTotPreviews.get(playIndex);
				mPlaying.setText(one.getTime() + " " + one.getName());
				previewIndex = TimeUtil.getUpcomingStart(mTotPreviews, playIndex);
			}
			else
			{
				mPlaying.setText("");
				previewIndex = TimeUtil.getUpcomingStart(mTotPreviews, 0);
			}
			
			if (previewIndex >= 0)
			{
				for (int i=previewIndex; i<mPreviewSize+previewIndex; i++)
				{
					if(i < mTotPreviews.size())
					{
						LivePreviewItem item = mTotPreviews.get(i);
						mCurPreviews.add(item);
					}
				}
				mAdaptPreview.notifyDataSetChanged();
			}
		}
		else
		{
			clearPreviewList();
		}
	}
	
	/**
	 * 
	 * @Title: clearPreviewList
	 * @Description: 清除预告列表
	 * @return: void
	 */
	public void clearPreviewList()
	{
		mPlaying.setText("");
		mCurPreviews.clear();
		for (int i=0; i<mPreviewSize; i++)
		{
			LivePreviewItem item = new LivePreviewItem();
			item.setTime("");
			item.setName("");
			mCurPreviews.add(item);
		}
		mAdaptPreview.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @Title: setCount
	 * @Description: 设置总个数
	 * @return: void
	 */
	private void setCount()
	{
		mTotal.setText(getResources().getString(R.string.live_total_prefix) + mCount + getResources().getString(R.string.live_total_suffix));
	}
	
	/**
	 * 
	 * @Title: setPage
	 * @Description: 设置页码
	 * @return: void
	 */
	private void setPage() 
	{
		mPage.setText(mCurPage + "/" + mTotPage);
	}
	
	/**
	 * 
	 * @Title: onKeyUp
	 * @Description: 响应上键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyUp()
	{
		resetTime();
		int curIndex = mChannelListView.getSelectedItemPosition();
		
		if (mCurPage == 1)
		{
			if (curIndex > 0)
			{
				curIndex--;
				mChannelListView.setSelection(curIndex);
			}
			else
			{
				//当选中第一页的第一个频道时, 不刷新节目预告
				return false;
			}
		}
		else if (mCurPage > 1)
		{
			if (curIndex == 0)
			{
				mCurPage--;
				freshChannelList();
				curIndex = 9;
				mChannelListView.setSelection(curIndex);
			}
			else if (curIndex > 0)
			{
				curIndex--;
				mChannelListView.setSelection(curIndex);
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyDown
	 * @Description: 响应下键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyDown()
	{
		resetTime();
		int curIndex = mChannelListView.getSelectedItemPosition();
		
		if (mCurPage == mTotPage)
		{
			if (curIndex < mCount-1-(mCurPage-1)*mChannelSize)
			{
				curIndex++;
				mChannelListView.setSelection(curIndex);
			}
			else
			{
				//当选中最后一页的第后一个频道时, 不刷新节目预告
				return false;
			}
		}
		else if (mCurPage < mTotPage)
		{
			if (curIndex == 9)
			{
				mCurPage++;
				freshChannelList();
				curIndex = 0;
				mChannelListView.setSelection(curIndex);
			}
			else if (curIndex < 9)
			{
				curIndex++;
				mChannelListView.setSelection(curIndex);
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: onKeyPageUp
	 * @Description: 响应上一页键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyPageUp()
	{
		resetTime();
		if (mCurPage > 1)
		{
			mCurPage--;
			freshChannelList();
			mChannelListView.setSelection(0);
			return true;
		}
		//当选中第一页的频道时, 不刷新节目预告
		return false;
	}
	
	/**
	 * 
	 * @Title: onKeyPageDown
	 * @Description: 响应下一页键
	 * @return
	 * @return: boolean
	 */
	public boolean onKeyPageDown()
	{
		resetTime();
		if (mCurPage < mTotPage)
		{
			mCurPage++;
			freshChannelList();
			mChannelListView.setSelection(0);
			return true;
		}
		//当选中第后一页的频道时, 不刷新节目预告
		return false;
	}
	
	/**
	 * 
	 * @Title: getChannelIndex
	 * @Description: 获取频道索引
	 * @return
	 * @return: int
	 */
	public int getChannelIndex()
	{
		int curIndex = mChannelListView.getSelectedItemPosition();
		int index = (mCurPage-1)*mChannelSize + curIndex;
		return index;
	}
	
	/**
	 * 
	 * @Title: setChannelCode
	 * @Description: 设置频道编码
	 * @param code
	 * @return: void
	 */
	public void setChannelCode(String code)
	{
		int index = 0;
		if (!code.isEmpty())
		{
			for (int i=0; i<mTotChannels.size(); i++)
			{
				if (code.equals(mTotChannels.get(i).getTvCode()))
				{
					index = i;
				}
			}
		}
		mCurPage = index/mChannelSize + 1;
		freshChannelList();
		
		int curIndex = index%mChannelSize;
		mChannelListView.setSelection(curIndex);
	}
	
	/**
	 * 
	 * @Title: show
	 * @Description: 显示该View
	 * @return: void
	 */
	public void show()
	{
		clearPreviewList();
		resetTime();
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
		else
		{
			return false;
		}
	}
}
