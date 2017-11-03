/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PauseAd.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 暂停广告(点播资源播放时按OK键)
 * @author: zhaoqy
 * @date: 2014-8-13 上午9:38:21
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.service.ad.Ad;
import com.szgvtv.ead.app.taijietemplates.service.ad.AdPosition;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StaticVariable;
import com.szgvtv.ead.framework.advertisement.AdManager.IAdCallback;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PauseAd extends RelativeLayout implements IAdCallback
{
	private Context   mContext; //上下文
	private ImageView mAd;      //广告图片
	
	public PauseAd(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_pause_ad, this);
		mAd = (ImageView) findViewById(R.id.id_pause_ad_icon);
	}
	
	@Override
	protected void onDetachedFromWindow() 
	{
		super.onDetachedFromWindow();
		stopThread();
	}
	
	/**
	 * 
	 * @Title: requestAd
	 * @Description: 请求广告
	 * @return: void
	 */
	private void requestAd()
	{
		if (StaticVariable.gPauseAdTime < 1)
		{
			if (Ad.mAdManager != null)
			{
				Ad.mAdManager.addPosition(AdPosition.getAdPositionOfPause());
				int group = Ad.mAdManager.setAdCallback(this);
				Ad.mAdManager.activePosition(group, AdPosition.getAdPositionOfPause(), 1);
			}
		}
		else
		{
			if(StaticVariable.gPauseBitmap != null && mAd != null)
			{
				mAd.setImageBitmap(StaticVariable.gPauseBitmap);
			}
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) 
	{
		super.onWindowVisibilityChanged(visibility);
	}
	
	/**
	 * 
	 * @Title: stopThread
	 * @Description: 关闭线程
	 * @return: void
	 */
	private void stopThread()
	{
		if (Ad.mAdManager != null)
		{
			Ad.mAdManager.freezePosition(AdPosition.getAdPositionOfPause());
		}
	}
	
	/**
	 * 
	 * @Title: startThread
	 * @Description: 开始线程
	 * @return: void
	 */
	private void startThread()
	{
		requestAd();
	}

	/**
	 * 激活广告位
	 * @param count		后续广告个数
	 * @param ret		结果
	 * @param id		广告位ID
	 * @param code		广告编码
	 * @param name		广告名称
	 * @param type		广告格式
	 * @param word		广告内容
	 * @param showtime	播放时长
	 * @param hasDetail	是否有详情
	 */
	@Override
	public void onAdActivated(int count, boolean ret, String id, String code, String name, String type, String word, int showtime, boolean hasDetail) 
	{
		Logcat.d(FlagConstant.TAG, " ret: " + ret);
		Logcat.d(FlagConstant.TAG, " word: " + word);
		
		if(ret && code!= null)
		{
			try 
			{
				if (StaticVariable.gPauseAdTime < 1)
				{
					//应用广告展示BI
					BiMsg.sendAppAdShowBiMsg(code, type, id, showtime + "");
					//实例化Bitmap
					StaticVariable.gPauseBitmap = BitmapFactory.decodeFile(word);
					StaticVariable.gPauseAdTime = 1;
				}
			} 
			catch (OutOfMemoryError e) 
			{
			}
			
			if(StaticVariable.gPauseBitmap != null && mAd != null)
			{
				mAd.setImageBitmap(StaticVariable.gPauseBitmap);
				Ad.mAdManager.showDetails(id, code);
			}
		}
		else
		{
			if (Ad.mAdManager != null)
			{
				Ad.mAdManager.freezePosition(AdPosition.getAdPositionOfPause());
			}
		}
		
		/*Bitmap bm = BitmapFactory.decodeFile(word);
		if(bm != null && mAd != null)
		{
			mAd.setImageBitmap(bm);
		}
		Ad.mAdManager.showDetails(id, code);*/
	}

	/**
	 * 取得广告详情
	 * @param count		后续详情个数
	 * @param ret		结果
	 * @param id		广告位ID
	 * @param code		广告编码
	 * @param type		广告格式
	 * @param detail	详情内容
	 */
	@Override
	public void onDetailShow(int count, boolean ret, String id, String code, String type, String detail) 
	{
		if (ret)
		{
			//应用广告详情BI
			//BiMsg.sendAppAdDetailBiMsg(code, id);
		}
	}

	/**
	 * 加载广告结果
	 * @param id	广告位ID
	 * @param ret	加载结果
	 */
	@Override
	public void onLoadFinished(String id, boolean ret) 
	{
	}
	
	/**
	 * 
	 * @Title: show
	 * @Description: 显示该View
	 * @return: void
	 */
	public void show()
	{
		startThread();
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
		setVisibility(View.INVISIBLE);
		stopThread();
	}
	
	/**
	 * 
	 * @Title: isShow
	 * @Description: 该View是否显示
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
}
