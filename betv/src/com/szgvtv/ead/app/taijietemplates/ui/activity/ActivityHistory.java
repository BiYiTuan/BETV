/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityHistory.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 播放历史Activity
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:52:30
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.application.UILApplication;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfo;
import com.szgvtv.ead.app.taijietemplates.db.VideoInfoTable;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.NoContent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ActivityHistory extends ActivityBase implements OnClickListener, OnFocusChangeListener, AnimationListener, UICallBack
{
	private Context              mContext;                             //上下文
	private RelativeLayout       mClear;                               //清除全部
	private ImageView            mFocus;                               //放大Image
	private VodItem              mVodItems[] = new VodItem[10];        //点播资源Item
	private NoContent            mNoContent;                           //无内容
	private ArrayList<VideoInfo> mVideos = new ArrayList<VideoInfo>(); //数据库资源列表
	private Animation            mScaleBig;                            //放大动画
	private Animation            mScaleSmall;                          //缩小动画
	private DialogPrompt         mDialog;                              //提示框
	private int                  mCount = 0;                           //总个数
	private int                  mSize = 10;                           //最多显示个数
	private int                  mIndex = -1;                          //当前更新索引
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_history);
		
		initViews();
		getRecordInfoList();
		freshRecord();
		new Thread(Init).start();
	}
	
	private void initViews()
	{
		mClear = (RelativeLayout) findViewById(R.id.id_history_clear);
		mFocus = (ImageView) findViewById(R.id.id_history_focus);
		mNoContent = (NoContent) findViewById(R.id.id_history_no_content);
		mVodItems[0] = (VodItem) findViewById(R.id.id_history_item0);
		mVodItems[1] = (VodItem) findViewById(R.id.id_history_item1);
		mVodItems[2] = (VodItem) findViewById(R.id.id_history_item2);
		mVodItems[3] = (VodItem) findViewById(R.id.id_history_item3);
		mVodItems[4] = (VodItem) findViewById(R.id.id_history_item4);
		mVodItems[5] = (VodItem) findViewById(R.id.id_history_item5);
		mVodItems[6] = (VodItem) findViewById(R.id.id_history_item6);
		mVodItems[7] = (VodItem) findViewById(R.id.id_history_item7);
		mVodItems[8] = (VodItem) findViewById(R.id.id_history_item8);
		mVodItems[9] = (VodItem) findViewById(R.id.id_history_item9);
		
		mClear.setOnClickListener(this);
		mClear.setFocusable(false);
		for (int i=0; i<mSize; i++)
		{
			mVodItems[i].setVisibility(View.INVISIBLE);
			mVodItems[i].setOnClickListener(this);
			mVodItems[i].setOnFocusChangeListener(this);
		}
	}
	
	/**
	 * 
	 * @Title: requestVideo
	 * @Description: 请求点播资源详情
	 * @param code
	 * @return: void
	 */
	private void requestVideo(String code)
	{
		RequestDataManager.requestData(this, mContext, Token.TOKEN_VIDEO, 0, 0, code);  
	}
	
	private Runnable Init = new Runnable() 
	{
		public void run() 
		{
			mHandler.sendEmptyMessage(FlagConstant.RECORD_REQUEST_VIDEO);
		}
	};
	
	/**
	 * 
	 * @Title: getRecordInfoList
	 * @Description: 获取记录数据列表
	 * @return: void
	 */
	private void getRecordInfoList()
	{
		mVideos.clear();
		ArrayList<VideoInfo> temp = new ArrayList<VideoInfo>();
		temp = VideoInfoTable.queryAllSortVideos(VideoInfoTable.SORT_HISTORY);
		
		for (int i=0; i<temp.size(); i++)
		{
			mVideos.add(temp.get(i));
		}
		mCount = mVideos.size();
	}
	
	/**
	 * 
	 * @Title: freshRecord
	 * @Description: 刷新记录列表
	 * @return: void
	 */
	private void freshRecord()
	{
		if (mCount > 0)
		{
			mClear.setVisibility(View.VISIBLE);
			mNoContent.setVisibility(View.INVISIBLE);
			mVodItems[0].requestFocus();
			
			for (int i=0; i<mSize; i++)
			{
				if(i < mCount)
				{
					VideoItem item = mVideos.get(i);
					/*if (UILApplication.mImageLoader != null)
					{
						UILApplication.mImageLoader.displayImage(item.getSmallPic(), mVodItems[i].getIcon(), UILApplication.mVodOption);
					}*/
					mVodItems[i].setName(item.getName());
					mVodItems[i].setVisibility(View.VISIBLE);
				}
				else
				{
					mVodItems[i].setVisibility(View.INVISIBLE);
				}
			}
		}
		else
		{
			showNoRecord();
		}
	}
	
	/**
	 * 
	 * @Title: freshSmallPic
	 * @Description: 刷新海报(单独刷新海报, 是为了避免RoundedBitmapDisplayer对图片的影响)
	 * @param index
	 * @return: void
	 */
	private void freshSmallPic(int index)
	{
		if (mCount > 0 && index < mCount)
		{
			VideoItem item = mVideos.get(index);
			if (UILApplication.mImageLoader != null)
			{
				//使用RoundedBitmapDisplayer, 它会创建新的ARGB_8888格式的Bitmap对象；
				UILApplication.mImageLoader.displayImage(item.getSmallPic(), mVodItems[index].getIcon(), UILApplication.mVodOption);
			}
		}
	}
	
	/**
	 * 
	 * @Title: freshRecordDrama
	 * @Description: 刷新剧集
	 * @return: void
	 */
	private void freshRecordDrama()
	{
		if (mIndex < mCount-1)
		{
			mIndex++;
			VideoItem item = mVideos.get(mIndex);
			String type = item.getVodtype();
			if (type.equals("1"))
			{
				freshSmallPic(mIndex);
				mHandler.sendEmptyMessage(FlagConstant.RECORD_REQUEST_VIDEO);
			}
			if (type.equals("2") || type.equals("3"))
			{
				requestVideo(item.getVideoCode());
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case FlagConstant.RECORD_REQUEST_VIDEO:
			{
				freshRecordDrama();
				break;
			}
			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @Title: showNoRecord
	 * @Description: 没有记录
	 * @return: void
	 */
	private void showNoRecord()
	{
		mClear.setVisibility(View.INVISIBLE);
		mNoContent.setVisibility(View.VISIBLE);
		mNoContent.setMessage(getResources().getString(R.string.no_history));
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
		case R.id.id_history_clear:
		{
			createDialogPrompt(/*R.drawable.tips_question, */
					   getResources().getString(R.string.dialog_clear_all), 
					   getResources().getString(R.string.dialog_sure), 
					   getResources().getString(R.string.dialog_cancel));
			break;
		}
		case R.id.id_history_item0:
		case R.id.id_history_item1:
		case R.id.id_history_item2:
		case R.id.id_history_item3:
		case R.id.id_history_item4:
		case R.id.id_history_item5:
		case R.id.id_history_item6:
		case R.id.id_history_item7:
		case R.id.id_history_item8:
		case R.id.id_history_item9:
		{
			int curIndex = getVodItemFocusIndex();
			enterDetail(curIndex);
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
		if (dramas > 0)  //更新剧集完成之后, 才可以响应OK键
		{
			//进入影视详情BI
			BiMsg.sendVodDetailBiMsg("4"); //来源类型:1专题,2推荐,3收藏,4历史,5搜索,6点播
			
			String code = item.getVideoCode();
			String name = item.getName();
			String classifyCode = item.getClassifyCode();
			String classifyName = item.getClassifyName();
			String dramaCode = "";
			String dramaName = "";
			//点击历史BI
			BiMsg.sendHistoryBiMsg(code, name, classifyCode, classifyName, dramaCode, dramaName);
			
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
				bundle.putInt(FlagConstant.ToActivityDetailRecordKey, VideoInfoTable.SORT_HISTORY);
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
				bundle.putInt(FlagConstant.ToActivityDetailRecordKey, VideoInfoTable.SORT_HISTORY);
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
	}

	@Override
	public void onSuccessful(Object in, int token) 
	{
		try 
		{
			switch (token) 
			{
			case Token.TOKEN_VIDEO:
			{
				VideoItem item = (VideoItem) RequestDataManager.getData(in);
				VideoInfo videoinfo = mVideos.get(mIndex);
				freshSmallPic(mIndex);
				
				//通过videocode获取资源详情时, 由于一个videocode对应多个资源, 可能获取到的资源不是播放的那个资源, 所以详情信息重新设置
				videoinfo.setVideoCode(item.getVideoCode());
				videoinfo.setName(item.getName());
				videoinfo.setArea(item.getArea());
				videoinfo.setType(item.getType());
				videoinfo.setDirector(item.getDirector());
				videoinfo.setActor(item.getActor());
				videoinfo.setTime(item.getTime());
				videoinfo.setSummary(item.getSummary());
				videoinfo.setSmallPic(item.getSmallPic());
				videoinfo.setBigPic(item.getBigPic());
				videoinfo.setPosters(item.getPosters());
				videoinfo.setScore(item.getScore());
				videoinfo.setHot(item.getHot());
				videoinfo.setTotalDramas(item.getTotalDramas());
				videoinfo.setRatings(item.getRatings());
				videoinfo.setVodtype(item.getVodtype());
				//videoinfo.setDramaList(item.getDramaList());
				videoinfo.setClassifyCode(item.getClassifyCode());
				videoinfo.setClassifyName(item.getClassifyName());
				
				if (item.getVodtype().equals("3"))
				{
					ArrayList<DramaItem> tempList = new ArrayList<DramaItem>(); 
					int size = item.getDramaList().size();
					for (int i=size-1; i>=0; i--)
					{
						DramaItem drama = item.getDramaList().get(i);
						tempList.add(drama);
					}
					item.setDramaList(tempList);
				}
				videoinfo.setDramaList(item.getDramaList());  //更新剧集
				
				mVodItems[mIndex].setVideo(item);
				mHandler.sendEmptyMessage(FlagConstant.RECORD_REQUEST_VIDEO);
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "===ActivityHistory==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		mHandler.sendEmptyMessage(FlagConstant.RECORD_REQUEST_VIDEO);
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
		if (getCurrentFocus() == mClear)
		{
			mVodItems[0].requestFocus();
		}
		else if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
			if (curIndex >= 0 && curIndex <= 4)
			{
				if (mCount-1 > 4)
				{
					curIndex += 5;
				}
				
				if (curIndex > mCount-1)
				{
					curIndex = mCount-1;
				}
				mVodItems[curIndex].requestFocus();
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: doKeyUp
	 * @Description: 响应上键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyUp()
	{
		if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
			if (curIndex >= 0 && curIndex <= 4)
			{
				if (!mClear.isFocusable())
				{
					mClear.setFocusable(true);
				}
				mClear.requestFocus();
			}
			else if (curIndex >= 5 && curIndex <= 9)
			{
				curIndex -= 5;
				mVodItems[curIndex].requestFocus();
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
		if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
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
		if (isVodItemsFocus())
		{
			int curIndex = getVodItemFocusIndex();
			
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
		return true;
	}
	
	/**
	 * 
	 * @Title: createDialogPrompt
	 * @Description: 创建提示对话框
	 * @param resid
	 * @param info
	 * @param sure
	 * @param cancel
	 * @return: void
	 */
	private void createDialogPrompt(/*int resid,*/ String info, String sure, String cancel)
	{
		mDialog = new DialogPrompt(mContext, /*R.style.dialog_style, resid,*/ info, sure, cancel);
		mDialog.setOnClickCustomListener(new onClickCustomListener() 
		{
			@Override
			public void OnClick(View v) 
			{
				switch (v.getId()) 
				{
				case R.id.id_dialog_prompt_sure:
				{
					mDialog.dismiss();
					clearAll();
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
		mDialog.getCancelBtn().requestFocus();
	}
	
	/**
	 * 
	 * @Title: clearAll
	 * @Description: 清空全部
	 * @return: void
	 */
	private void clearAll()
	{
		for (int i=0; i<mCount; i++)
		{
			VideoInfoTable.deleteVideo(mVideos.get(i).getVideoCode(), VideoInfoTable.SORT_HISTORY);
			mVodItems[i].setVisibility(View.INVISIBLE);
		}
		showNoRecord();
	}
	
	/**
	 * 
	 * @Title: isVodItemsFocus
	 * @Description: 判断Item是否选中
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
	 * @Description: 获取Item的选中index
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
