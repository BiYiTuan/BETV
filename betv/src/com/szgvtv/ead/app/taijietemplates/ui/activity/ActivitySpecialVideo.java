/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivitySpecialVideo.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 专题资源Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午2:00:47
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.SpecialItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitySpecialVideo extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context              mContext;                             //上下文
	private LoadingPage          mLoading;                             //加载
	private ImageView            mPoster;                              //专题海报
	private TextView             mName;                                //名称
	private TextView             mBrief;                               //简介
	private HorizontalScrollView mHs;                                  //横向滚动条
	private VodItem              mVodItems[] = new VodItem[7];         //点播资源Item
	private ImageView            mFocus;                               //放大Image
	private Animation            mScaleBig;                            //放大动画
	private Animation            mScaleSmall;                          //缩小动画
	private Animation            mToRight;                             //右移动画
	private Animation            mToLeft;                              //左移动画
	private SpecialItem          mSpecial;                             //专题item
	private ArrayList<VideoItem> mVideos = new ArrayList<VideoItem>(); //所有点播资源列表
	private boolean              mAnimationed = true;                  //动画是否结束
	private int                  mSize = 7;                            //最多显示个数
	private int                  mIndex = 0;                           //item索引
	private int                  mOffset = 0;                          //偏移量
	private int                  mCount = 0;                           //总个数
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_special_video);
		
		initViews();
		getSpecialItem();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_special_video_loading_page);
		mPoster = (ImageView) findViewById(R.id.id_special_video_poster);
		mName = (TextView) findViewById(R.id.id_special_video_name);
		mBrief = (TextView) findViewById(R.id.id_special_video_brief);
		mHs = (HorizontalScrollView) findViewById(R.id.id_special_video_hs);
		mFocus = (ImageView) findViewById(R.id.id_special_video_focus);
		mVodItems[0] = (VodItem) findViewById(R.id.id_special_video_item0);
		mVodItems[1] = (VodItem) findViewById(R.id.id_special_video_item1);
		mVodItems[2] = (VodItem) findViewById(R.id.id_special_video_item2);
		mVodItems[3] = (VodItem) findViewById(R.id.id_special_video_item3);
		mVodItems[4] = (VodItem) findViewById(R.id.id_special_video_item4);
		mVodItems[5] = (VodItem) findViewById(R.id.id_special_video_item5);
		mVodItems[6] = (VodItem) findViewById(R.id.id_special_video_item6);
		
		for (int i=0; i<mSize; i++)
		{
			mVodItems[i].setOnFocusChangeListener(this);
			mVodItems[i].setOnClickListener(this);
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
		mLoading.setOnClickListener(this);
		mToRight = AnimationUtils.loadAnimation(this, FlagConstant.TRANSLATE_SPECIAL_RIGHT_ANIMS);
		mToLeft = AnimationUtils.loadAnimation(this, FlagConstant.TRANSLATE_SPECIAL_LEFT_ANIMS);
		mToRight.setFillAfter(true);
		mToLeft.setFillAfter(true);
		mToRight.setAnimationListener(this);
		mToLeft.setAnimationListener(this);
	}
	
	private void getSpecialItem()
	{
		Bundle bundle = getIntent().getExtras();
		mSpecial = bundle.getParcelable(FlagConstant.ToActivitySpecialVideoKey);
		
		if (mSpecial != null)
		{
			if (UILApplication.mImageLoader != null)
			{
				UILApplication.mImageLoader.displayImage(mSpecial.getImageUrl(), mPoster, UILApplication.mSpecialBgOption);
			}
			mName.setText(mSpecial.getName());
			mBrief.setText("         " + mSpecial.getSummary());
			requestSpecialVideoList();
		}
	}
	
	/**
	 * 
	 * @Title: requestSpecialVideoList
	 * @Description: 请求专题下资源
	 * @return: void
	 */
	private void requestSpecialVideoList()
	{
		mLoading.setLoadPageFail(false);
		mLoading.show();
		//size = 1000; page = 1; 专题下面的列表要分页
		RequestDataManager.requestData(this, mContext, Token.TOKEN_SPECIAL_VIDEO, 1000, 1, mSpecial.getAlbumCode());  
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
			Util.amplifySpecialItem(view, mFocus, 0.10);
		}
		else if (animation == mToRight || animation == mToLeft)
		{
			mAnimationed = true;
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

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_special_video_item0:
		case R.id.id_special_video_item1:
		case R.id.id_special_video_item2:
		case R.id.id_special_video_item3:
		case R.id.id_special_video_item4:
		case R.id.id_special_video_item5:
		case R.id.id_special_video_item6:
		{
			int curIndex = mIndex + mOffset;
			enterDetail(curIndex);
			break;
		}
		case R.id.id_special_video_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				requestSpecialVideoList();
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
		VideoItem item = mVideos.get(index);
		int dramas = item.getDramaList().size();
		Logcat.d(FlagConstant.TAG, " dramas: " + dramas);
		//只有剧集个数大于0的时候, 才可以进入详情界面
		if (dramas > 0)
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("1"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
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
			case Token.TOKEN_SPECIAL_VIDEO:
			{
				mVideos.clear();
				ArrayList<VideoItem> temp = new ArrayList<VideoItem>(); 
				temp = (ArrayList<VideoItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					VideoItem item = temp.get(i);
					item.setClassifyCode("");
					item.setClassifyName("");
					mVideos.add(item);
				}
				
				mCount = mVideos.size();
				Logcat.d(FlagConstant.TAG, " specialvideo_size: " + mCount);
				if (mCount > 0)
				{
					mLoading.hide();
					mName.setVisibility(View.VISIBLE);
					mBrief.setVisibility(View.VISIBLE);
					freshSpecialVideoList();
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
			Logcat.e(FlagConstant.TAG, "===ActivitySpecialVideo==== onSuccessful error + " + e.toString());
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
	 * @Title: freshSpecialVideoList
	 * @Description: 刷新专题下资源列表
	 * @return: void
	 */
	private void freshSpecialVideoList()
	{
		for (int i=0; i< mSize; i++)
		{
			if (i + mOffset < mCount)
			{
				VideoItem item = mVideos.get(i + mOffset);
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
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		boolean nRet = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
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
	 * @Title: doKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	@SuppressLint("UseValueOf")
	private boolean doKeyLeft()
	{
		if (!mAnimationed || (mIndex == 0 && mOffset == 0))
		{
			return true;
		}
		
		int x = 0;
		mIndex--;
		if (mIndex < 1 && mOffset > 0)
		{
			mIndex = 1;
			
			if (mIndex + mOffset < mCount-1 && mOffset > 0)
			{
				mOffset--;
				freshSpecialVideoList();
			}
			else if (mIndex + mOffset < mCount-1 && mOffset == 0)
			{
				mIndex = 0;
				freshSpecialVideoList();
			}
			mAnimationed = false;
			mHs.startAnimation(mToLeft);
		}
		
		x = Util.getXCoordinateOfView(mVodItems[mIndex]);
		if (x < 0)
		{
			if (mIndex == 0)
			{
				mHs.smoothScrollTo(30, 0);
			}
			else if (mIndex == 1)
			{
				mHs.smoothScrollTo(230, 0);
			}
		}
		mVodItems[mIndex].requestFocus();
		return true;
	}
	
    /**
     * 
     * @Title: doKeyRight
     * @Description: 响应右键
     * @return
     * @return: boolean
     */
	@SuppressLint("UseValueOf")
	private boolean doKeyRight()
	{
		if (!mAnimationed || (mIndex + mOffset == mCount-1))
		{
			return true;
		}
		
		int x = 0;
		mIndex++;
		if (mIndex > 5)
		{
			
			mIndex = 5;
			
			if (mIndex + mOffset < mCount-2)
			{
				
				mOffset++;
				freshSpecialVideoList();
				mAnimationed = false;
				mHs.startAnimation(mToRight);
			}
			else if (mIndex + mOffset == mCount-2)
			{
				mIndex = 6;
				freshSpecialVideoList();
				x = Util.getXCoordinateOfView(mVodItems[mIndex]);
				if ( x + 214 > 1280)
				{
					mAnimationed = false;
					mHs.startAnimation(mToRight);
				}
			}
		}
	
		x = Util.getXCoordinateOfView(mVodItems[mIndex]);
		if (x + 214 > 1280)
		{
			if (mIndex == 5)
			{
				mHs.smoothScrollTo(200, 0);
			}
			else if (mIndex == 6)
			{
				mHs.smoothScrollTo(460, 0);
			}
		}
		mVodItems[mIndex].requestFocus();
		return true;
	}
}
