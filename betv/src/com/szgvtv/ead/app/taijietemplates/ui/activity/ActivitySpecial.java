/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivitySpecial.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 专题列表Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:56:20
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.SpecialItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.ui.view.DotGroup;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.SpecialListItem;
import com.szgvtv.ead.app.taijietemplates.util.ConvertViewToBitmap;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class ActivitySpecial extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context                mContext;                                 //上下文
	private TextView               mTitle;                                   //标题
	private LoadingPage            mLoading;                                 //加载
	private ImageView              mFocus;                                   //放大Image
	private ImageView              mInflects[] = new ImageView[3];           //倒影Image
	private DotGroup               mDotGroup;                                //页码
	private SpecialListItem        mSpecialItems[] = new SpecialListItem[6]; //专题Item
	private Animation              mScaleBig;                                //放大动画
	private Animation              mScaleSmall;                              //缩小动画
	private ArrayList<SpecialItem> mSpecials = new ArrayList<SpecialItem>(); //所有点播资源列表
	private int                    mCurPage = 0;                             //当前页数
	private int                    mTotPage = 0;                             //总页数
	private int                    mCount = 0;                               //总个数
	private int                    mSize = 6;                                //每页最多显示个数
	private int                    mState = 0;                               //选中状态
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_special);
		
		initViews();
		requestSpecialList();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_special_loading_page);
		mTitle = (TextView) findViewById(R.id.id_special_name);
		mFocus = (ImageView) findViewById(R.id.id_special_focus);
		mInflects[0] = (ImageView) findViewById(R.id.id_special_item3_inflection);
		mInflects[1] = (ImageView) findViewById(R.id.id_special_item4_inflection);
		mInflects[2] = (ImageView) findViewById(R.id.id_special_item5_inflection);
		mDotGroup = (DotGroup) findViewById(R.id.id_special_dot);
		mSpecialItems[0] = (SpecialListItem) findViewById(R.id.id_special_item0);
		mSpecialItems[1] = (SpecialListItem) findViewById(R.id.id_special_item1);
		mSpecialItems[2] = (SpecialListItem) findViewById(R.id.id_special_item2);
		mSpecialItems[3] = (SpecialListItem) findViewById(R.id.id_special_item3);
		mSpecialItems[4] = (SpecialListItem) findViewById(R.id.id_special_item4);
		mSpecialItems[5] = (SpecialListItem) findViewById(R.id.id_special_item5);
		
		mLoading.setOnClickListener(this);
		for (int i=0; i<6; i++)
		{
			mSpecialItems[i].setVisibility(View.INVISIBLE);
			mSpecialItems[i].setOnClickListener(this);
			mSpecialItems[i].setOnFocusChangeListener(this);
		}
	}
	
	/**
	 * 
	 * @Title: requestSpecialList
	 * @Description: 请求专题列表
	 * @return: void
	 */
	private void requestSpecialList()
	{
		mLoading.setLoadPageFail(false);
		mLoading.show();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_SPECIAL_LIST, 0, 0);  
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) 
	{
		if (!hasFocus) 
		{
			v.setSelected(false);
			mFocus.setVisibility(View.GONE);
			mScaleSmall = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_SPECIAL_SMALL_ANIMS);
			mScaleSmall.setFillAfter(false);
			mScaleSmall.setAnimationListener(this);
			v.startAnimation(mScaleSmall);
		}
		else
		{
			v.setSelected(true);
			mScaleBig = AnimationUtils.loadAnimation(mContext, FlagConstant.SCALE_SPECIAL_BIG_ANIMS);
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
		case R.id.id_special_item0:
		case R.id.id_special_item1:
		case R.id.id_special_item2:
		case R.id.id_special_item3:
		case R.id.id_special_item4:
		case R.id.id_special_item5:
		{
			int curIndex = getSpecialItemFocusIndex();
			SpecialItem item = mSpecials.get(curIndex + (mCurPage-1)*mSize);
			Intent intent = new Intent(this, ActivitySpecialVideo.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(FlagConstant.ToActivitySpecialVideoKey, item);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
		case R.id.id_special_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				mState = 1;
				requestSpecialList();
				break;
			}
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
			case Token.TOKEN_SPECIAL_LIST:
			{
				ArrayList<SpecialItem> temp = new ArrayList<SpecialItem>(); 
				temp = (ArrayList<SpecialItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					SpecialItem item = temp.get(i);
					mSpecials.add(item);
				}
				
				mCount = mSpecials.size();
				Logcat.d(FlagConstant.TAG, " special_size: " + mCount);
				if (mCount > 0)
				{
					mLoading.hide();
					mTotPage = (mCount-1)/mSize + 1;
					mCurPage++;
					freshSpecialList();
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
			Logcat.e(FlagConstant.TAG, "===ActivitySpecial==== onSuccessful error + " + e.toString());
		}
	}
	
	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		mLoading.setLoadPageFail(true);
		mLoading.requestFocus();
	}
	
	/**
	 * 
	 * @Title: freshSpecialList
	 * @Description: 刷新专题列表
	 * @return: void
	 */
	private void freshSpecialList()
	{
		for (int i=0; i<mSize; i++)
		{
			if(i + (mCurPage-1)*mSize < mCount)
			{
				SpecialItem item = mSpecials.get(i + (mCurPage-1)*mSize);
				mSpecialItems[i].setVisibility(View.VISIBLE);
				mSpecialItems[i].setFocusable(true);
				mTitle.setFocusable(false);
				
				if (i >= 0 && i <= 2)
				{
					if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(item.getImageUrl(), mSpecialItems[i].getIcon(), UILApplication.mSpecialOption);
					}
				}
				else
				{
					mInflects[i-3].setImageBitmap(ConvertViewToBitmap.createReflection(
							ConvertViewToBitmap.convertViewToBitmap(mSpecialItems[i]), 70));
					mInflects[i-3].setVisibility(View.VISIBLE);
					
					if (i == 3)
					{
						if (UILApplication.mImageLoader != null)
						{
							UILApplication.mImageLoader.displayImage(item.getImageUrl(), mSpecialItems[i].getIcon(), UILApplication.mSpecialOption, 
							new ImageLoadingListener()
							{
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) 
								{
									mInflects[0].setImageBitmap(ConvertViewToBitmap.createReflection(
										ConvertViewToBitmap.convertViewToBitmap(mSpecialItems[3]), 70));
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

								@Override
								public void onLoadingStarted(String arg0, View arg1) {}
							});
						}
					}
					else if (i == 4)
					{
						if (UILApplication.mImageLoader != null)
						{
							UILApplication.mImageLoader.displayImage(item.getImageUrl(), mSpecialItems[i].getIcon(), UILApplication.mSpecialOption, 
							new ImageLoadingListener()
							{
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) 
								{
									mInflects[1].setImageBitmap(ConvertViewToBitmap.createReflection(
										ConvertViewToBitmap.convertViewToBitmap(mSpecialItems[4]), 70));
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

								@Override
								public void onLoadingStarted(String arg0, View arg1) {}
							});
						}
					}
					else if (i == 5)
					{
						if (UILApplication.mImageLoader != null)
						{
							UILApplication.mImageLoader.displayImage(item.getImageUrl(), mSpecialItems[i].getIcon(), UILApplication.mSpecialOption, 
							new ImageLoadingListener()
							{
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) 
								{
									mInflects[2].setImageBitmap(ConvertViewToBitmap.createReflection(
										ConvertViewToBitmap.convertViewToBitmap(mSpecialItems[5]), 70));
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

								@Override
								public void onLoadingStarted(String arg0, View arg1) {}
							});
						}
					}
				}
			}
			else
			{
				mSpecialItems[i].setVisibility(View.INVISIBLE);
				if (i >= 3 && i <= 5)
				{
					mInflects[i-3].setVisibility(View.INVISIBLE);
				}
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
			if ((mCount-1)-(mCurPage-1)*mSize < 3)  
			{
				curIndex = (mCount-1)-(mCurPage-1)*mSize;
			}
			else
			{
				curIndex = 3;
			}
			break;
		}
		case 5:  //第一排第一个向左
		{
			curIndex = 2;
			break;
		}
		case 6:  //第二排第一个向左
		{
			curIndex = 5;
			break;
		}
		default:
			break;
		}
		
		mSpecialItems[curIndex].requestFocus();
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
			if (isSpecialItemsFocus())
			{
				int curIndex = getSpecialItemFocusIndex();
				
				if (curIndex >=0 && curIndex <= 2)
				{
					if (mCount-1-(mCurPage-1)*mSize > 2)
					{
						curIndex += 3;
					}
					
					if (curIndex > mCount-1-(mCurPage-1)*mSize)
					{
						curIndex = mCount-1-(mCurPage-1)*mSize;
					}
					mSpecialItems[curIndex].requestFocus();
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
			if (isSpecialItemsFocus())
			{
				int curIndex = getSpecialItemFocusIndex();
				
				if (curIndex >=3 && curIndex <= 5)
				{
					curIndex -= 3;
					mSpecialItems[curIndex].requestFocus();
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
			if (mCurPage < mTotPage)
			{
				for (int i=0; i<6; i++)
				{
					mTitle.setFocusable(true);
					mSpecialItems[i].setFocusable(false);
					mSpecialItems[i].setVisibility(View.INVISIBLE);
				}
				
				mState = 2;
				mCurPage++;
				freshSpecialList();
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
			if (isSpecialItemsFocus())
			{
				if (mCurPage > 1)
				{
					for (int i=0; i<6; i++)
					{
						mTitle.setFocusable(true);
						mSpecialItems[i].setFocusable(false);
						mSpecialItems[i].setVisibility(View.INVISIBLE);
					}
					
					mState = 2;
					mCurPage--;
					freshSpecialList();
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
			if (isSpecialItemsFocus())
			{
				int curIndex = getSpecialItemFocusIndex();
				
				if (mCurPage == 1 && mTotPage == 1)
				{
					if (curIndex == 0)
					{
						mSpecialItems[mCount-1].requestFocus();
					}
					else 
					{
						curIndex--;
						mSpecialItems[curIndex].requestFocus();
					}
				}
				else if (mTotPage > 1)
				{
					if (mCurPage > 1)
					{
						if (curIndex == 0 || curIndex == 3)
						{
							for (int i=0; i<6; i++)
							{
								mTitle.setFocusable(true);
								mSpecialItems[i].setFocusable(false);
								mSpecialItems[i].setVisibility(View.INVISIBLE);
							}
							
							if (curIndex == 0)
							{
								mState = 5;
							}
							else if (curIndex == 3)
							{
								mState = 6;
							}
							
							mCurPage--;
							freshSpecialList();
						}
						else
						{
							curIndex--;
							mSpecialItems[curIndex].requestFocus();
						}
					}
					else
					{
						if (curIndex != 0 && curIndex != 3)
						{
							curIndex--;
							mSpecialItems[curIndex].requestFocus();
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
			if (isSpecialItemsFocus())
			{
				int curIndex = getSpecialItemFocusIndex();
				
				if (mCurPage == 1 && mTotPage == 1)
				{
					if (curIndex == mCount-1)
					{
						mSpecialItems[0].requestFocus();
					}
					else 
					{
						curIndex++;
						mSpecialItems[curIndex].requestFocus();
					}
				}
				else if (mTotPage > 1)
				{
					if (mCurPage < mTotPage)
					{
						if (curIndex == 2 || curIndex == 5)
						{
							for (int i=0; i<6; i++)
							{
								mTitle.setFocusable(true);
								mSpecialItems[i].setFocusable(false);
								mSpecialItems[i].setVisibility(View.INVISIBLE);
							}
							
							if (curIndex == 2)
							{
								mState = 3;
							}
							else if (curIndex == 5)
							{
								mState = 4;
							}
							
							mCurPage++;
							freshSpecialList();
						}
						else
						{
							curIndex++;
							mSpecialItems[curIndex].requestFocus();
						}
					}
					else
					{
						if (curIndex < mCount-1-(mCurPage-1)*mSize && curIndex != 2)
						{
							curIndex++;
							mSpecialItems[curIndex].requestFocus();
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @Title: isSpecialItemsFocus
	 * @Description: 判断Item是否选中
	 * @return
	 * @return: boolean
	 */
	private boolean isSpecialItemsFocus()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mSpecialItems[i])
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: getSpecialItemFocusIndex
	 * @Description: 获取Item的选中index
	 * @return
	 * @return: int
	 */
	private int getSpecialItemFocusIndex()
	{
		for (int i=0; i<mSize; i++)
		{
			if (getCurrentFocus() == mSpecialItems[i])
			{
				return i;
			}
		}
		return 0;
	}
}
