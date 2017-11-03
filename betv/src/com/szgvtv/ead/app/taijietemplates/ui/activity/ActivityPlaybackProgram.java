/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityPlaybackProgram.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 回放节目列表Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:42:35
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptDate;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptPlayback;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.ui.view.HorizontalListView;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.NoContent;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityPlaybackProgram extends ActivityBase  implements OnClickListener, OnItemClickListener, UICallBack
{
	private Context                     mContext;                                         //上下文
	private LoadingPage                 mLoading;                                         //加载
	private ImageView                   mIcon;                                            //频道Icon
	private TextView                    mItem;                                            //页码
	private TextView                    mName;                                            //频道名称
	private HorizontalListView          mHListView;                                       //横向ListView
	private ListView                    mListView;                                        //回放节目ListView 
	private NoContent                   mNoContent;                                       //无回放节目
	private AdaptDate                   mAdaptDate;                                       //频道回放Adapt
	private AdaptPlayback               mAdaptPlayback;                                   //频道回放Adapt
	private ArrayList<String>           mCurDates = new ArrayList<String>();              //日期导航栏当前列表信息
	private ArrayList<LivePlaybackItem> mCurPrograms = new ArrayList<LivePlaybackItem>(); //频道回放列表
	private ArrayList<LivePlaybackItem> mTotPrograms = new ArrayList<LivePlaybackItem>(); //频道回放总列表
	private LiveChannelItem             mLiveChannel;                                     //频道Item
	private String                      mTvCode;                                          //频道编码
	private int                         mIndex = 0;                                       //频道索引值
	private int                         mHindex = 0;                                      //导航日期索引值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_playback_program_t);
		
		initViews();
		getLiveChannelItem();
		requestProgramList();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_playback_program_loading_page);
		mIcon = (ImageView) findViewById(R.id.id_playback_program_icon);
		mItem = (TextView) findViewById(R.id.id_playback_program_item);
		mName = (TextView) findViewById(R.id.id_playback_program_name);
		mHListView = (HorizontalListView) findViewById(R.id.id_playback_program_hlistview);
		mListView = (ListView) findViewById(R.id.id_playback_program_listview);
		mNoContent = (NoContent) findViewById(R.id.id_playback_program_no_content);
		
		mLoading.setOnClickListener(this);
		mHListView.setOnItemClickListener(this);
		mAdaptDate = new AdaptDate(mContext, mCurDates);
		mHListView.setAdapter(mAdaptDate);
		
		mListView.setOnItemClickListener(this);
		mAdaptPlayback = new AdaptPlayback(mContext, mCurPrograms);
		mListView.setAdapter(mAdaptPlayback);
	}
	
	/**
	 * 
	 * @Title: getLiveChannelItem
	 * @Description: 获取直播频道
	 * @return: void
	 */
	private void getLiveChannelItem()
	{
		Bundle bundle = getIntent().getExtras();
		mLiveChannel = bundle.getParcelable(FlagConstant.ToActivityPlaybackProgramKey);
		mIndex = bundle.getInt(FlagConstant.ToActivityPlaybackProgramIndexKey, 0);
		mTvCode = mLiveChannel.getTvCode();
		mName.setText(mLiveChannel.getTvName());
		if (UILApplication.mImageLoader != null)
		{
			UILApplication.mImageLoader.displayImage(mLiveChannel.getTvLogo(), mIcon, UILApplication.mChannelIconOption);
		}
	}
	
	/**
	 * 
	 * @Title: requestProgramList
	 * @Description: 请求回放节目列表
	 * @return: void
	 */
	private void requestProgramList()
	{
		mListView.setVisibility(View.INVISIBLE);
		mLoading.setLoadPageFail(false);
		mLoading.show();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_TV_REPLAY, 0, 0, mTvCode);  
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_playback_program_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				requestProgramList();
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
		if (isHListviewFocus())
		{
			//监听HorizontalListView, 否则点击就会挂掉
		}
		else if (mListView.isFocused())
		{
			int curIndex = mListView.getSelectedItemPosition();
			LivePlaybackItem item = mCurPrograms.get(curIndex);
			Intent intent = new Intent(this, ActivityPlayback.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(FlagConstant.ToActivityLivePlaybacItemkKey, item);
			bundle.putParcelable(FlagConstant.ToActivityLiveChannelItemKey, mLiveChannel);
			bundle.putInt(FlagConstant.ToActivityPlaybackProgramIndexKey, mIndex);
			intent.putExtras(bundle);
			startActivity(intent);
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
			case Token.TOKEN_TV_REPLAY:
			{
				mTotPrograms.clear();
				ArrayList<LivePlaybackItem> temp = new ArrayList<LivePlaybackItem>();
				temp = (ArrayList<LivePlaybackItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					LivePlaybackItem item = temp.get(i);
					mTotPrograms.add(item);
				}
				
				if (mTotPrograms.size() > 0)
				{
					mLoading.hide();
					mListView.setVisibility(View.VISIBLE);
					
					freshProgramDate();
					freshProgramList(mHindex);
					mHandler.sendEmptyMessage(FlagConstant.DRAMA_FINISHED);
				}
				else
				{
					mLoading.hide();
					mNoContent.setVisibility(View.VISIBLE);
					mNoContent.setMessage(getResources().getString(R.string.no_program));
				}
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== ActivityPlaybackProgram ==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		mLoading.setLoadPageFail(true);
		mLoading.requestFocus();
	}
	
	private void freshProgramDate()
	{
		mCurDates.clear();
		ArrayList<String> temp = new ArrayList<String>(); 
		temp.clear();
		
		int size = mTotPrograms.size();
		Logcat.d(FlagConstant.TAG, "size: " + size);
		for (int i=0; i<size; i++)
		{
			Logcat.d(FlagConstant.TAG, "program" + i + ": " + mTotPrograms.get(i).getDate() + "   " + 
															  mTotPrograms.get(i).getTime() + "   " + 
															  mTotPrograms.get(i).getName());
		}
		
		temp.add(mTotPrograms.get(0).getDate());
		for (int i=0; i<size-1; i++)
		{
			if (!mTotPrograms.get(i).getDate().equals(mTotPrograms.get(i+1).getDate()))
			{
				temp.add(mTotPrograms.get(i+1).getDate());
			}
		}
		
		for (int i=0; i<temp.size(); i++)
		{
			//最多显示7天的回放
			if (i<7)
			{
				Logcat.d(FlagConstant.TAG, "date" + i + ": " + temp.get(i));
				mCurDates.add(temp.get(i));
			}
		}
		mAdaptDate.notifyDataSetChanged();
		mHandler.sendEmptyMessage(FlagConstant.NAVIGATION_FINISHED);
	}
	
	/**
	 * 
	 * @Title: freshProgramList
	 * @Description: 刷新回放节目
	 * @return: void
	 */
	private void freshProgramList(int index)
	{
		String source = mCurDates.get(index);
		Logcat.d(FlagConstant.TAG, "source: " + source);
		
		mCurPrograms.clear();
		for (int i=0; i<mTotPrograms.size(); i++)
		{
			String des = mTotPrograms.get(i).getDate();
			
			if (source.equals(des))
			{
				mCurPrograms.add(mTotPrograms.get(i));
			}
		}
		mAdaptPlayback.notifyDataSetChanged();
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
		if (isHListviewFocus())
		{
			mHListView.getChildAt(mHindex).setSelected(true);
			mHandler.sendEmptyMessage(FlagConstant.DRAMA_FINISHED);  //将mListView设置为焦点
		}
		else if (mListView.isFocused())
		{
			int curIndex = mListView.getSelectedItemPosition();
			int size = mCurPrograms.size();
			if (curIndex < size-1)
			{
				curIndex++;
				mItem.setText(curIndex + 1 + "/" + size);
			}
			return false;
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
	    if (mListView.isFocused())
		{
			int curIndex = mListView.getSelectedItemPosition();
			int size = mCurPrograms.size();
			if (curIndex > 0)
			{
				curIndex--;
				mItem.setText(curIndex + 1 + "/" + size);
				return false;
			}
			else
			{
				mItem.setText(0 + "/" + size);
				mHListView.getChildAt(mHindex).requestFocus();
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
		if (isHListviewFocus())
		{
			mHListView.getChildAt(mHindex).setSelected(false);
			mHindex--;
			if (mHindex < 0)
			{
				mHindex = mCurDates.size()-1;
			}
			mHListView.getChildAt(mHindex).requestFocus();
			freshProgramList(mHindex);
			int size = mCurPrograms.size();
			mItem.setText(0 + "/" + size);
		}
		//add by zhaoqy 2015-2-26
		else if (mListView.isFocused())
		{
			mHListView.getChildAt(mHindex).setSelected(false);
			mHindex--;
			if (mHindex < 0)
			{
				mHindex = mCurDates.size()-1;
			}
			mHListView.getChildAt(mHindex).setSelected(true);
			freshProgramList(mHindex);
			mHandler.sendEmptyMessage(FlagConstant.DRAMA_FINISHED);  //将mListView设置为焦点
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
			mHListView.getChildAt(mHindex).setSelected(false);
			mHindex++;
			if (mHindex > mCurDates.size()-1)
			{
				mHindex = 0;
			}
			mHListView.getChildAt(mHindex).requestFocus();
			freshProgramList(mHindex);
			int size = mCurPrograms.size();
			mItem.setText(0 + "/" + size);
		}
		//add by zhaoqy 2015-2-26
		else if (mListView.isFocused())
		{
			mHListView.getChildAt(mHindex).setSelected(false);
			mHindex++;
			if (mHindex > mCurDates.size()-1)
			{
				mHindex = 0;
			}
			mHListView.getChildAt(mHindex).setSelected(true);
			freshProgramList(mHindex);
			mHandler.sendEmptyMessage(FlagConstant.DRAMA_FINISHED);  //将mListView设置为焦点
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
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
			case FlagConstant.NAVIGATION_FINISHED:
			{
				if (mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).setSelected(true);
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.NAVIGATION_FINISHED, 100);
				}
				break;
			}
			case FlagConstant.DRAMA_FINISHED:
			{
				if (mListView != null)
				{
					mListView.requestFocus();
					mListView.setSelection(0);
					int size = mCurPrograms.size();
					mItem.setText(1 + "/" + size);
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.DRAMA_FINISHED, 100);
				}
				break;
			}
			default:
				break;
			}
		}
	};
}
