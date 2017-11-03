/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivitySearchResult.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 搜索结果Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:55:13
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.service.bi.BI;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.view.DotGroup;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.NoContent;
import com.szgvtv.ead.app.taijietemplates.ui.view.VodItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitySearchResult extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context              mContext;                             //上下文
	private TextView             mTitle;                               //标题
	private TextView             mKeyPre;                              //关键字
	private TextView             mKey;                                 //关键字
	private TextView             mKeySuf;                              //关键字
	private TextView             mNum;                                 //搜索个数
	private ImageView            mFocus;                               //放大Image
	private VodItem              mVodItems[] = new VodItem[10];        //点播资源Item
	private LoadingPage          mLoading;                             //加载
	private NoContent            mNoContent;                           //无内容
	private DotGroup             mDotGroup;                            //页码
	private Animation            mScaleBig;                            //放大动画
	private Animation            mScaleSmall;                          //缩小动画
	private ArrayList<VideoItem> mVideos = new ArrayList<VideoItem>(); //所有点播资源列表
	private String               mHotWord = "";                        //热词
	private int                  mCurPage = 0;                         //当前页数
	private int                  mTotPage = 0;                         //总页数
	private int                  mLoadedPage = 0;                      //下载页数
	private int                  mRequestTime = 1;                     //请求次数
	private int                  mCount = 0;                           //总个数
	private int                  mSize = 10;                           //每页最多显示个数
	private int                  mState = 0;                           //选中状态
	private int                  mListId = 0;                          //搜索列表耗时启动id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_search_result);
		
		initViews();
		getHotWord();
		requestSearchResult();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		//在详情界面, 取消收藏, 返回到搜索结果界面时, 刷新是否收藏的状态
	}
	
	private void initViews()
	{
		mTitle = (TextView) findViewById(R.id.id_result_title);
		mKeyPre = (TextView) findViewById(R.id.id_result_keyword_prefix);
		mKey = (TextView) findViewById(R.id.id_result_keyword);
		mKeySuf = (TextView) findViewById(R.id.id_result_keyword_suffix);
		mNum = (TextView) findViewById(R.id.id_result_number);
		mFocus = (ImageView) findViewById(R.id.id_result_focus);
		mLoading = (LoadingPage) findViewById(R.id.id_result_loading_page);
		mNoContent = (NoContent) findViewById(R.id.id_result_no_content);
		mDotGroup = (DotGroup) findViewById(R.id.id_result_dot);
		mVodItems[0] = (VodItem) findViewById(R.id.id_result_item0);
		mVodItems[1] = (VodItem) findViewById(R.id.id_result_item1);
		mVodItems[2] = (VodItem) findViewById(R.id.id_result_item2);
		mVodItems[3] = (VodItem) findViewById(R.id.id_result_item3);
		mVodItems[4] = (VodItem) findViewById(R.id.id_result_item4);
		mVodItems[5] = (VodItem) findViewById(R.id.id_result_item5);
		mVodItems[6] = (VodItem) findViewById(R.id.id_result_item6);
		mVodItems[7] = (VodItem) findViewById(R.id.id_result_item7);
		mVodItems[8] = (VodItem) findViewById(R.id.id_result_item8);
		mVodItems[9] = (VodItem) findViewById(R.id.id_result_item9);
		
		for (int i=0; i<10; i++)
		{
			mVodItems[i].setOnClickListener(this);
			mVodItems[i].setOnFocusChangeListener(this);
		}
		mLoading.setOnClickListener(this);
		mVideos.clear();
	}
	
	/**
	 * 
	 * @Title: getHotWord
	 * @Description: 获取搜索关键字
	 * @return: void
	 */
	private void getHotWord()
	{
		Bundle bundle = getIntent().getExtras();
		mHotWord = bundle.getString(FlagConstant.HotWordKey);
		setKeyTitleAndWord(mHotWord);
	}
	
	/**
	 * 
	 * @Title: requestSearchResult
	 * @Description: 请求搜索结果
	 * @return: void
	 */
	private void requestSearchResult()
	{
		mListId = BI.getStartTimeId();
		for (int i=0; i<10; i++)
		{
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
		mLoading.setLoadPageFail(false);
		mLoading.show();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_SEARCH_VIDEO, 20, mRequestTime, mHotWord);  
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_result_item0:
		case R.id.id_result_item1:
		case R.id.id_result_item2:
		case R.id.id_result_item3:
		case R.id.id_result_item4:
		case R.id.id_result_item5:
		case R.id.id_result_item6:
		case R.id.id_result_item7:
		case R.id.id_result_item8:
		case R.id.id_result_item9:
		{
			int curIndex = getVodItemFocusIndex();
			enterDetail(curIndex);
			break;
		}
		case R.id.id_result_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				mState = 1;  //加载失败
				requestSearchResult();
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
		VideoItem item = mVideos.get(index + (mCurPage-1)*mSize);
		int dramas = item.getDramaList().size();
		Logcat.d(FlagConstant.TAG, " dramas: " + dramas);
		//只有剧集个数大于0的时候, 才可以进入详情界面
		if (dramas > 0)
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("5"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
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
			case Token.TOKEN_SEARCH_VIDEO:
			{
				//搜索耗时BI
				BiMsg.sendSearchTimeBiMsg(mListId);
				
				ArrayList<VideoItem> temp = new ArrayList<VideoItem>(); 
				temp = (ArrayList<VideoItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					VideoItem item = temp.get(i);
					item.setClassifyCode("");
					item.setClassifyName("");
					mVideos.add(item);
				}
				
				if (mRequestTime == 1)
				{
					mCount = RequestDataManager.getTotal(in);
					setNumber(mCount);
				}
				
				if (temp.size() > 0)
				{
					mLoading.hide();
					mTotPage = (mCount-1)/mSize + 1;
					mCurPage++;
					mLoadedPage = (mVideos.size()-1)/mSize + 1;
					freshSearchResult();
				}
				else
				{
					if (mRequestTime == 1)
					{
						mLoading.hide();
						mNoContent.setVisibility(View.VISIBLE);
						mNoContent.setMessage(getResources().getString(R.string.no_result));
						setNumber(0);
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
			Logcat.e(FlagConstant.TAG, "===ActivitySearchResult==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		//搜索耗时BI
		BiMsg.sendSearchTimeBiMsg(mListId);
		
		mLoading.setLoadPageFail(true);
		mLoading.requestFocus();
	}

	/**
	 * 
	 * @Title: freshSearchResult
	 * @Description: 刷新搜索结果
	 * @return: void
	 */
	private void freshSearchResult()
	{
		for (int i=0; i<mSize; i++)
		{
			if(i + (mCurPage-1)*mSize < mCount)
			{
				VideoItem item = mVideos.get(i + (mCurPage-1)*mSize);
				if (UILApplication.mImageLoader != null)
				{
					UILApplication.mImageLoader.displayImage(item.getSmallPic(), mVodItems[i].getIcon(), UILApplication.mVodOption);
				}
				mVodItems[i].setVideo(item);
				mVodItems[i].setKeyword(mHotWord);
				mVodItems[i].setName(item.getName());
				mVodItems[i].setVisibility(View.VISIBLE);
				mVodItems[i].setFocusable(true);
				mTitle.setFocusable(false);
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
		
		mVodItems[curIndex].requestFocus();
	}
	
	/**
	 * 
	 * @Title: setKeyWord
	 * @Description: 设置关键字
	 * @param keyword
	 * @return: void
	 */
	private void setKeyTitleAndWord(String keyword)
	{
		mTitle.setText(getResources().getString(R.string.home_search)/* + "\""*/);
		mKeyPre.setText("\"");
		mKey.setText(keyword);
		mKeySuf.setText("\"");
	}
	
	/**
	 * 
	 * @Title: setNumber
	 * @Description: 设置搜索结果总个数
	 * @param number
	 * @return: void
	 */
	private void setNumber(int number)
	{
		mNum.setText(/*"\"" + */getResources().getString(R.string.search_pre) + number + getResources().getString(R.string.search_suf));
		mNum.setVisibility(View.VISIBLE);
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
			{
				int curIndex = getVodItemFocusIndex();
				
				if (curIndex >=5 && curIndex <= 9)
				{
					curIndex -= 5;
					mVodItems[curIndex].requestFocus();
				}
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
			{
				if (mCurPage < mTotPage)
				{
					for (int i=0; i<10; i++)
					{
						mTitle.setFocusable(true);
						mVodItems[i].setFocusable(false);
						mVodItems[i].setVisibility(View.INVISIBLE);
					}
					
					mState = 2;
					if (mCurPage < mLoadedPage)
					{
						mCurPage++;
						freshSearchResult();
					}
					else
					{
						mRequestTime++;
						requestSearchResult();
					}
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
			{
				if (mCurPage > 1)
				{
					for (int i=0; i<10; i++)
					{
						mTitle.setFocusable(true);
						mVodItems[i].setFocusable(false);
						mVodItems[i].setVisibility(View.INVISIBLE);
					}
					
					mState = 2;
					mCurPage--;
					freshSearchResult();
				}
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
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
							for (int i=0; i<10; i++)
							{
								mTitle.setFocusable(true);
								mVodItems[i].setFocusable(false);
								mVodItems[i].setVisibility(View.INVISIBLE);
							}
							
							if (curIndex == 0)
							{
								mState = 5;
							}
							else if (curIndex == 5)
							{
								mState = 6;
							}
							
							mCurPage--;
							freshSearchResult();
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
		if (mCount > 0  && mLoading.getVisibility() != View.VISIBLE)
		{
			if (isVodItemsFocus())
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
							for (int i=0; i<10; i++)
							{
								mTitle.setFocusable(true);
								mVodItems[i].setFocusable(false);
								mVodItems[i].setVisibility(View.INVISIBLE);
							}
							
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
								freshSearchResult();
							}
							else
							{
								mRequestTime++;
								requestSearchResult();
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
		}
		return true;
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
}
