/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LoadingPage.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 加载页面
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:15:26
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingPage extends RelativeLayout
{
	private Context        mContext;           //上下文
	private RelativeLayout mLoadingPage;       //加载页面
	private RelativeLayout mLoadingPageFail;   //加载失败
	private TextView       mLoadingMessage;    //加载信息
	private Button         mReload;            //重新加载
	private boolean        mLoadFail = false;  //是否加载失败
	
	public LoadingPage(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_loading_page, this);
		mLoadingPage = (RelativeLayout) findViewById(R.id.id_loading_page);
		mLoadingPageFail = (RelativeLayout) findViewById(R.id.id_loading_page_fail);
		mLoadingMessage = (TextView) findViewById(R.id.id_loading_page_message);
		mReload = (Button) findViewById(R.id.id_loading_page_fail_reload);
	}
	
	/**
	 * 
	 * @Title: setLoadPageFail
	 * @Description: 设置是否下载失败
	 * @param fail
	 * @return: void
	 */
	public void setLoadPageFail(boolean fail)
	{
		mLoadFail = fail;
		
		if (fail)
		{
			mLoadingPage.setVisibility(View.INVISIBLE);
			mLoadingPageFail.setVisibility(View.VISIBLE);
			setReloadFocus(true);
		}
		else
		{
			mLoadingPage.setVisibility(View.VISIBLE);
			mLoadingMessage.setText(getResources().getString(R.string.loading_page)/* + "..."*/);
			mLoadingPageFail.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 
	 * @Title: setReloadFocus
	 * @Description: 设置重新加载按钮背景图
	 * @param focus
	 * @return: void
	 */
	public void setReloadFocus(boolean focus)
	{
		if (focus)
		{
			mReload.setBackgroundResource(R.drawable.button_focus);
		}
		else
		{
			mReload.setBackgroundResource(R.drawable.button_unfocus);
		}
	}
	
	/**
	 * 
	 * @Title: getLoadPageState
	 * @Description: 获取是否下载失败状态
	 * @return
	 * @return: boolean
	 */
	public boolean getLoadPageState()
	{
		return mLoadFail;
	}
	
	/**
	 * 
	 * @Title: show
	 * @Description: 显示该View
	 * @return: void
	 */
	public void show()
	{
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
	}
}
