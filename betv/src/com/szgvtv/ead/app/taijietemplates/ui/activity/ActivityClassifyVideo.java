/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityClassifyVideo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 分类资源Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:42:21
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptClassify;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.ClassifyItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.view.DotGroup;
import com.szgvtv.ead.app.taijietemplates.ui.view.HorizontalListView;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.VodItem;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class ActivityClassifyVideo extends ActivityBase implements UICallBack, OnItemClickListener, OnClickListener, OnFocusChangeListener, AnimationListener
{
	private Context                 mContext;                                      //上下文
	private LoadingPage             mLoading;                                      //加载
	private HorizontalListView      mHListView;                                    //横向ListView
	private ImageView               mLeft;                                         //左方向图标
	private ImageView               mRight;                                        //右方向图标
	private ImageView               mFocus;                                        //放大Image
	private DotGroup                mDotGroup;                                     //页码
	private VodItem                 mVodItems[] = new VodItem[10];                 //点播资源Item
	private AdaptClassify           mAdapt;                                        //分类Adapt
	private Animation               mScaleBig;                                     //放大动画
	private Animation               mScaleSmall;                                   //缩小动画
	private ArrayList<VideoItem>    mTotVideos = new ArrayList<VideoItem>();       //所有点播资源List
	private ArrayList<ClassifyItem> mCurClassifys = new ArrayList<ClassifyItem>(); //当前分类列表
	private ArrayList<ClassifyItem> mTotClassifys = new ArrayList<ClassifyItem>(); //全部分类列表
	private String                  mFirstClassifyCode;                            //一级分类编码
	private String                  mSecondClassifycode;                           //二级分类编码
	private int                     mState = 0;                                    //选中状态
	private int                     mHindex = 0;                                   //分类导航当前索引
	private int                     mPreHindex = 0;                                //分类导航上一次索引
	private int                     mCurPage = 0;                                  //当前页
	private int                     mTotPage = 0;                                  //总页数
	private int                     mTempNaviPage = 0;                             //剧集导航栏页
	private int                     mCurNaviPage = 0;                              //剧集导航栏当前页
	private int                     mTotNaviPage = 0;                              //剧集导航栏所有页
	private int                     mLoadedPage = 0;                               //下载页数
	private int                     mRequestTime = 1;                              //请求次数
	private int                     mCount = 0;                                    //分类下资源总个数
	private int                     mSize = 10;                                    //每页最多显示个数
	private int                     mToken = 0;                                    //列表标识
	private int                     mListId = 0;                                   //点播列表耗时启动id
	private boolean                 mLoaded = false;                               //是否下载完成
	private boolean                 mSwitched = true;                              //是否切换完成

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_classify_video);
		
		initViews();
		getClassifyCode();
		requestSecondClassifyList();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_classify_video_loading_page);
		mHListView = (HorizontalListView) findViewById(R.id.id_classify_video_hlistview);
		mLeft = (ImageView) findViewById(R.id.id_classify_video_left);
		mRight = (ImageView) findViewById(R.id.id_classify_video_right);
		mFocus = (ImageView) findViewById(R.id.id_classify_video_focus);
		mDotGroup = (DotGroup) findViewById(R.id.id_classify_video_dot);
		mVodItems[0] = (VodItem) findViewById(R.id.id_classify_video_item0);
		mVodItems[1] = (VodItem) findViewById(R.id.id_classify_video_item1);
		mVodItems[2] = (VodItem) findViewById(R.id.id_classify_video_item2);
		mVodItems[3] = (VodItem) findViewById(R.id.id_classify_video_item3);
		mVodItems[4] = (VodItem) findViewById(R.id.id_classify_video_item4);
		mVodItems[5] = (VodItem) findViewById(R.id.id_classify_video_item5);
		mVodItems[6] = (VodItem) findViewById(R.id.id_classify_video_item6);
		mVodItems[7] = (VodItem) findViewById(R.id.id_classify_video_item7);
		mVodItems[8] = (VodItem) findViewById(R.id.id_classify_video_item8);
		mVodItems[9] = (VodItem) findViewById(R.id.id_classify_video_item9);
		
		mLoading.setOnClickListener(this);
		mHListView.setOnItemClickListener(this);
		mAdapt = new AdaptClassify(mContext, mCurClassifys);
		mHListView.setAdapter(mAdapt);
		
		for (int i=0; i<10; i++)
		{
			mVodItems[i].setOnClickListener(this);
			mVodItems[i].setOnFocusChangeListener(this);
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 
	 * @Title: reInit
	 * @Description: 重新初始化
	 * @return: void
	 */
	private void reInit()
	{
		mTotPage = 0;
		mCurPage = 0;
		mLoadedPage = 0;
		mTotVideos.clear();
		mRequestTime = 1;
		mDotGroup.setDotTotalNumber(mTotPage);
		mDotGroup.setDotCurrentNumber(mCurPage);
	}
	
	private void getClassifyCode()
	{
		Bundle bundle = getIntent().getExtras();
		mFirstClassifyCode = bundle.getString(FlagConstant.ClassifyCodeKey);
	}
	
	/**
	 * 
	 * @Title: requestSecondClassifyList
	 * @Description: 请求二级分类列表
	 * @return: void
	 */
	private void requestSecondClassifyList()
	{
		mLoading.setLoadPageFail(false);
		mLoading.show();
		mToken = Token.TOKEN_VIDEO_CLASSIFY;
		RequestDataManager.requestData(this, mContext, Token.TOKEN_VIDEO_CLASSIFY, 0, 0, mFirstClassifyCode);  
	}
	
	/**
	 * 
	 * @Title: requestVodSortList
	 * @Description: 请求二级分类下的资源
	 * @return: void
	 */
	private void requestVodClassifyList()
	{
		mListId = BI.getStartTimeId();
		mLoaded = false;
		for (int i=0; i<10; i++)
		{
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
		mLoading.setLoadPageFail(false);
		mLoading.show();
		mToken = Token.TOKEN_VIDEO_LIST;
		mSecondClassifycode = mCurClassifys.get(mHindex).getClassifyCode();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_VIDEO_LIST, 20, mRequestTime, mSecondClassifycode);  
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
		case R.id.id_classify_video_item0:
		case R.id.id_classify_video_item1:
		case R.id.id_classify_video_item2:
		case R.id.id_classify_video_item3:
		case R.id.id_classify_video_item4:
		case R.id.id_classify_video_item5:
		case R.id.id_classify_video_item6:
		case R.id.id_classify_video_item7:
		case R.id.id_classify_video_item8:
		case R.id.id_classify_video_item9:
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
		case R.id.id_classify_video_item0:
		case R.id.id_classify_video_item1:
		case R.id.id_classify_video_item2:
		case R.id.id_classify_video_item3:
		case R.id.id_classify_video_item4:
		case R.id.id_classify_video_item5:
		case R.id.id_classify_video_item6:
		case R.id.id_classify_video_item7:
		case R.id.id_classify_video_item8:
		case R.id.id_classify_video_item9:
		{
			int curIndex = getVodItemFocusIndex();
			enterDetail(curIndex);
			break;
		}
		case R.id.id_classify_video_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				if (mToken == Token.TOKEN_VIDEO_CLASSIFY)
				{
					requestSecondClassifyList();
				}
				else if (mToken == Token.TOKEN_VIDEO_LIST)
				{
					mState = 1;
					requestVodClassifyList();
				}
			}
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
		VideoItem item = mTotVideos.get(index + (mCurPage-1)*mSize);
		int dramas = item.getDramaList().size();
		Logcat.d(FlagConstant.TAG, " dramas: " + dramas);
		//只有剧集个数大于0的时候, 才可以进入详情界面
		if (dramas > 0)
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("6"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
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
	 * @Title: switchVodByClassify
	 * @Description: 按左右键后, 获取分类下的资源(这样处理可以让用户体验更流畅)
	 * @return: void
	 */
	private void switchVodByClassify()
	{
		if (mTempNaviPage == mCurNaviPage)
		{
			if (mPreHindex != mHindex)
			{
				mSwitched = false;
				mState = 7;
				mTempNaviPage = mCurNaviPage;
				mPreHindex = mHindex;
				reInit();
				requestVodClassifyList();
			}
		}
		else
		{
			mSwitched = false;
			mState = 7;
			mTempNaviPage = mCurNaviPage;
			mPreHindex = mHindex;
			reInit();
			requestVodClassifyList();
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
			case Token.TOKEN_VIDEO_CLASSIFY:
			{
				mTotClassifys.clear();
				ArrayList<ClassifyItem> temp = new ArrayList<ClassifyItem>(); 
				temp = (ArrayList<ClassifyItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					mTotClassifys.add(temp.get(i));
				}
				
				if (temp.size() > 0)
				{
					mTotNaviPage = (mTotClassifys.size()-1)/7 + 1;
					mCurNaviPage++;
					mTempNaviPage = mCurNaviPage;
					updateClassifyCount();
					freshSecondClassifyList();
					mHandler.sendEmptyMessageDelayed(FlagConstant.FINISHED, 1000);
				}
				else
				{
					onNetError(-1, "error", null, token);
				}
				break;
			}
			case Token.TOKEN_VIDEO_LIST:
			{
				//点播列表耗时BI
				BiMsg.sendDemandListTimeBiMsg(mSecondClassifycode, "2", mListId);
				
				ArrayList<VideoItem> temp = new ArrayList<VideoItem>(); 
				temp = (ArrayList<VideoItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					VideoItem item = temp.get(i);
					item.setClassifyCode(mCurClassifys.get(mHindex).getClassifyCode());
					item.setClassifyName(mCurClassifys.get(mHindex).getClassifyName());
					
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
					
					mTotVideos.add(item);
				}
				
				if (mRequestTime == 1)
				{
					mCount = RequestDataManager.getTotal(in);
				}
				
				if (temp.size() > 0)
				{
					mLoaded = true;
					mLoading.hide();
					mTotPage = (mCount-1)/mSize + 1;
					mCurPage++;
					mLoadedPage = (mTotVideos.size()-1)/mSize + 1;
					freshVodClassifyList();
				}
				else
				{
					onNetError(-1, "error", null, token);
				}
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== ActivityClassifyVideo === onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		mLoaded = true;
		if (token == Token.TOKEN_VIDEO_LIST)
		{
			//点播列表耗时BI
			BiMsg.sendDemandListTimeBiMsg(mSecondClassifycode, "2", mListId);
			
			mSwitched = true;
			mHListView.getChildAt(mHindex).setSelected(true);
			mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(false);
		}
		mLoading.setLoadPageFail(true);
		mLoading.requestFocus();
	}
	
	/**
	 * 
	 * @Title: freshSecondClassifyList
	 * @Description: 刷新二级分类列表
	 * @return: void
	 */
	private void freshSecondClassifyList()
	{
		mCurClassifys.clear();
		for (int i=0; i<7; i++)
		{
			if (i + (mCurNaviPage-1)*7 < mTotClassifys.size())
			{
				ClassifyItem item = new ClassifyItem();
				item = mTotClassifys.get(i + (mCurNaviPage-1)*7);
				mCurClassifys.add(item);
			}
		}
		mAdapt.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @Title: freshVodSortList
	 * @Description: 刷新分类下资源列表
	 * @return: void
	 */
	private void freshVodClassifyList()
	{
		for (int i=0; i<mSize; i++)
		{
			if(i + (mCurPage-1)*mSize < mCount)
			{
				VideoItem item = mTotVideos.get(i + (mCurPage-1)*mSize);
				if (UILApplication.mImageLoader != null)
				{
					UILApplication.mImageLoader.displayImage(item.getSmallPic(), mVodItems[i].getIcon(), UILApplication.mVodOption);
				}
				mVodItems[i].setVideo(item);
				mVodItems[i].setName(item.getName());
				mVodItems[i].setVisibility(View.VISIBLE);
				mVodItems[i].setFocusable(true);
			}
			else
			{
				mVodItems[i].setVisibility(View.INVISIBLE);
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
			mVodItems[curIndex].requestFocus();
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
			mVodItems[curIndex].requestFocus();
			break;
		}
		case 5:  //第一排第一个向左
		{
			curIndex = 4;
			mVodItems[curIndex].requestFocus();
			break;
		}
		case 6:  //第二排第一个向左
		{
			curIndex = 9;
			mVodItems[curIndex].requestFocus();
			break;
		}
		case 7:
		{
			mSwitched = true;
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: updateDot
	 * @Description: 更新页码
	 * @return: void
	 */
	private void updateClassifyCount()
	{
		if (mTotClassifys.size() > 7)
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (isHListviewFocus())
		{
			if (mLoading.getVisibility() == View.VISIBLE)
			{
				if (mLoading.getLoadPageState())
				{
					if (mTempNaviPage == mCurNaviPage)
					{
						mHListView.getChildAt(mPreHindex).setSelected(true);
						mHListView.getChildAt(mPreHindex).findViewById(R.id.classify_name).setSelected(false);
					}
					mLoading.setReloadFocus(true);
					mLoading.requestFocus();
				}
			}
			else if (mLoading.getVisibility() != View.VISIBLE)
			{
				if (mTempNaviPage == mCurNaviPage)
				{
					mHListView.getChildAt(mPreHindex).setSelected(true);
					mHListView.getChildAt(mPreHindex).findViewById(R.id.classify_name).setSelected(false);
				}
				
				if (mTotVideos.size() > 0)
				{
					mVodItems[0].requestFocus();
				}
			}
		}
		else if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
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
				mVodItems[curIndex].requestFocus();
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (mLoading.isFocused())
		{
			if (mLoading.getLoadPageState())
			{
				if (mTempNaviPage == mCurNaviPage)
				{
					mHindex = mPreHindex;
					mHListView.getChildAt(mPreHindex).setSelected(false);
					mHListView.getChildAt(mPreHindex).requestFocus();
					mHListView.getChildAt(mPreHindex).findViewById(R.id.classify_name).setSelected(true);
				}
				else
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
				}
				mLoading.setReloadFocus(false);
			}
		}
		else if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
			if (curIndex >= 0 && curIndex <= 4)
			{
				if (mTempNaviPage == mCurNaviPage)
				{
					mHindex = mPreHindex;
					mHListView.getChildAt(mPreHindex).setSelected(false);
					mHListView.getChildAt(mPreHindex).requestFocus();
					mHListView.getChildAt(mPreHindex).findViewById(R.id.classify_name).setSelected(true);
				}
				else
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
				}
			}
			else if (curIndex >=5 && curIndex <= 9)
			{
				curIndex -= 5;
				mVodItems[curIndex].requestFocus();
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (isHListviewFocus())
		{
			if (mTotNaviPage == 1)
			{
				return true;
			}
			
			for (int i=0; i<10; i++)
			{
				mVodItems[i].setFocusable(false);
				mVodItems[i].setVisibility(View.INVISIBLE);
			}
			
			if (mCurNaviPage < mTotNaviPage)
			{
				mCurNaviPage++;
			}
			else if (mCurNaviPage == mTotNaviPage)
			{
				mCurNaviPage = 1;
			}
			freshSecondClassifyList();
			mHandler.sendEmptyMessage(FlagConstant.SWITCH_PAGE_DOWN_FINISHED);
		}
		else if (isVodItemsFocus())
		{
			if (mCurPage < mTotPage)
			{
				mState = 2;
				if (mCurPage < mLoadedPage)
				{
					mCurPage++;
					freshVodClassifyList();
				}
				else
				{
					mDotGroup.requestFocus();   //防止光标跑到导航栏
					mRequestTime++;
					requestVodClassifyList();
				}
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (isHListviewFocus())
		{
			if (mTotNaviPage == 1)
			{
				return true;
			}
			
			for (int i=0; i<10; i++)
			{
				mVodItems[i].setFocusable(false);
				mVodItems[i].setVisibility(View.INVISIBLE);
			}
			
			if (mCurNaviPage == 1)
			{
				mCurNaviPage = mTotNaviPage;
			}
			else if (mCurNaviPage > 1)
			{
				mCurNaviPage--;
			}
			freshSecondClassifyList();
			mHandler.sendEmptyMessage(FlagConstant.SWITCH_PAGE_UP_FINISHED);
		}
		else if (isVodItemsFocus())
		{
			if (mCurPage > 1)
			{
				mState = 2;
				mCurPage--;
				freshVodClassifyList();
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (isHListviewFocus())
		{
			if (mTotClassifys.size() == 1)
			{
				return true;
			}
			
			if (!mSwitched)
			{
				return true;
			}
			
			if (mHindex == 0)
			{
				for (int i=0; i<10; i++)
				{
					mVodItems[i].setFocusable(false);
					mVodItems[i].setVisibility(View.INVISIBLE);
				}
				
				setHitemSelected(false);
				if (mCurNaviPage == 1)
				{
					mCurNaviPage = mTotNaviPage;
				}
				else if (mCurNaviPage > 1)
				{
					mCurNaviPage--;
				}
				freshSecondClassifyList();
				mHandler.sendEmptyMessage(FlagConstant.SWITCH_LEFT_FINISHED);
			}
			else if (mHindex > 0)
			{
				mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(false);
				mHindex--;
				mHListView.getChildAt(mHindex).requestFocus();
				mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
				switchVodByClassify();
			}
		}
		else if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
			if (mCurPage == 1 && mTotPage == 1)
			{
				if (curIndex == 0)
				{
					mVodItems[mCount-1].requestFocus();
				}
				else 
				{
					curIndex--;
					mVodItems[curIndex].requestFocus();
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
						freshVodClassifyList();
					}
					else
					{
						curIndex--;
						mVodItems[curIndex].requestFocus();
					}
				}
				else
				{
					if (curIndex != 0 && curIndex != 5)
					{
						curIndex--;
						mVodItems[curIndex].requestFocus();
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
		if (!mLoaded)
		{
			return true;
		}
		
		if (mLoading.isFocused())
		{
			if (mTotClassifys.size() <= 0)
			{
				return true;
			}
		}
		
		if (isHListviewFocus())
		{
			if (mTotClassifys.size() == 1)
			{
				return true;
			}
			
			if (!mSwitched)
			{
				return true;
			}
			
			if (mHindex == 6)
			{
				for (int i=0; i<10; i++)
				{
					mVodItems[i].setFocusable(false);
					mVodItems[i].setVisibility(View.INVISIBLE);
				}
				
				setHitemSelected(false);
				if (mCurNaviPage < mTotNaviPage)
				{
					mCurNaviPage++;
				}
				else if (mCurNaviPage == mTotNaviPage)
				{
					mCurNaviPage = 1;
				}
				freshSecondClassifyList();
				mHandler.sendEmptyMessage(FlagConstant.SWITCH_RIGHT_FINISHED);
			}
			else if (mHindex < 6)
			{
				if (mCurNaviPage == mTotNaviPage)
				{
					if (mHindex == mTotClassifys.size()-1-(mCurNaviPage-1)*7)
					{
						for (int i=0; i<10; i++)
						{
							mVodItems[i].setFocusable(false);
							mVodItems[i].setVisibility(View.INVISIBLE);
						}
						
						mCurNaviPage = 1;
						freshSecondClassifyList();
						mHandler.sendEmptyMessage(FlagConstant.SWITCH_RIGHT_FINISHED);
						return true;
					}
				}

				mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(false);
				mHindex++;
				mHListView.getChildAt(mHindex).requestFocus();
				mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
				switchVodByClassify();
			}
		}
		else if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
			if (mCurPage == 1 && mTotPage == 1)
			{
				if (curIndex == mCount-1)
				{
					mVodItems[0].requestFocus();
				}
				else 
				{
					curIndex++;
					mVodItems[curIndex].requestFocus();
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
						
						if (mCurPage < mLoadedPage)
						{
							mCurPage++;
							freshVodClassifyList();
						}
						else
						{
							mDotGroup.requestFocus();   //防止光标跑到导航栏
							mRequestTime++;
							requestVodClassifyList();
						}
					}
					else
					{
						curIndex++;
						mVodItems[curIndex].requestFocus();
					}
				}
				else
				{
					if (curIndex < mCount-1-(mCurPage-1)*mSize && curIndex != 4)
					{
						curIndex++;
						mVodItems[curIndex].requestFocus();
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: setHitemSelected
	 * @Description: 设置选中二级分类
	 * @param selected
	 * @return: void
	 */
	private void setHitemSelected(boolean selected)
	{
		if (mPreHindex == mHindex)
		{
			if (mTempNaviPage == mCurNaviPage)
			{
				mHListView.getChildAt(mPreHindex).setSelected(selected);
				mHListView.getChildAt(mPreHindex).findViewById(R.id.classify_name).setSelected(!selected);
			}
		}
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
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
			case FlagConstant.FINISHED:
			{
				if(mHListView.getChildAt(0) != null)
				{
					mHListView.getChildAt(mHindex).setSelected(true);
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(false);
					requestVodClassifyList();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.FINISHED, 1000);
				}
				break;
			}
			case FlagConstant.SWITCH_LEFT_FINISHED:
			{
				if (mCurNaviPage == mTotNaviPage)
				{
					mHindex = mTotClassifys.size()-1- (mCurNaviPage-1)*7;
				}
				else if (mCurNaviPage <= mTotNaviPage)
				{
					mHindex = 6;
				}
				
				if(mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
					switchVodByClassify();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.SWITCH_LEFT_FINISHED, 100);
				}
				break;
			}
			case FlagConstant.SWITCH_RIGHT_FINISHED:
			{
				mHindex = 0;
				if(mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
					switchVodByClassify();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.SWITCH_RIGHT_FINISHED, 100);
				}
				break;
			}
			case FlagConstant.SWITCH_PAGE_UP_FINISHED:
			{
				mHindex = 0;
				if(mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
					switchVodByClassify();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.SWITCH_PAGE_UP_FINISHED, 100);
				}
				break;
			}
			case FlagConstant.SWITCH_PAGE_DOWN_FINISHED:
			{
				mHindex = 0;
				if(mHListView.getChildAt(mHindex) != null)
				{
					mHListView.getChildAt(mHindex).requestFocus();
					mHListView.getChildAt(mHindex).findViewById(R.id.classify_name).setSelected(true);
					switchVodByClassify();
				}
				else
				{
					mHandler.sendEmptyMessageDelayed(FlagConstant.SWITCH_PAGE_DOWN_FINISHED, 100);
				}
				break;
			}
			default:
				break;
			}
		}
	};
}
