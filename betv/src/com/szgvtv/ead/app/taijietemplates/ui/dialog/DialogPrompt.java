/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DialogPrompt.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.dialog
 * @Description: 提示对话框
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:30:17
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.dialog;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.ui.activity.ActivityPlayLive;
import com.szgvtv.ead.app.taijietemplates.ui.activity.ActivityPlayVideo;
import com.szgvtv.ead.app.taijietemplates.ui.activity.ActivityPlayback;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogPrompt extends Dialog implements OnClickListener
{
	private onClickCustomListener mListener; //点击监听
	private TextView  mInfo;         //信息
	private Button    mSure;         //确定
	private Button    mCancel;       //取消
	private String    mStringInfo;   //信息String
	private String    mStringSure;   //确定String
	private String    mStringCancel; //取消String
	private int       mFlag = 0;     //1-ActivityPlayLive; 2-ActivityPlayVideo; 3-ActivityPlayback
	private Activity  mActivity;     //Activity
	
	public DialogPrompt(Context context) 
	{
		super(context);
	}
	
	public DialogPrompt(Context context, String info, String sure, String cancel) 
	{
		super(context, R.style.dialog_style);
		
		mStringInfo = info;
		mStringSure = sure;
		mStringCancel = cancel;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setContentView(R.layout.dialog_prompt);
		init();
	}
	
	private void init()
	{
		mInfo = (TextView) findViewById(R.id.id_dialog_prompt_info);
		mSure = (Button) findViewById(R.id.id_dialog_prompt_sure);
		mCancel = (Button) findViewById(R.id.id_dialog_prompt_cancel);
		
		mInfo.setText(mStringInfo);
		mSure.setText(mStringSure);
		mCancel.setText(mStringCancel);
		
		mSure.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}
	
	public Button getSureBtn()
	{
		return mSure;
	}
	
	public Button getCancelBtn()
	{
		return mCancel;
	}
	
	public void setFlag(int flag)
	{
		mFlag = flag;
	}
	
	public void setActivity(Activity activity)
	{
		mActivity = activity;
	}
	
	@Override
	public void onClick(View v) 
	{
		mListener.OnClick(v);
	}
	
	public void setOnClickCustomListener(onClickCustomListener listener) 
	{  
		mListener = listener;  
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (mFlag == FlagConstant.ACTIVITY_PLAY_LIVE)        
			{
				dismiss();
				((ActivityPlayLive)mActivity).onExitPlay();
				return true;
			}
			else if (mFlag == FlagConstant.ACTIVITY_PLAY_VIDEO) 
			{
				dismiss();
				((ActivityPlayVideo)mActivity).onExitPlay();
				return true;
			}
			else if (mFlag == FlagConstant.ACTIVITY_PLAY_BACK)   
			{
				dismiss();
				((ActivityPlayback)mActivity).onExitPlay();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
