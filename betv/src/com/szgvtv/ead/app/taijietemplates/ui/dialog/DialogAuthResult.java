/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DialogAuthResult.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.dialog
 * @Description: 鉴权结果对话框
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:27:36
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.dialog;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogAuthResult extends Dialog implements OnClickListener
{
	private onClickCustomListener mListener;   //点击监听
	private TextView              mInfo;       //信息
	private Button                mColse;      //关闭
	private String                mStringInfo; //信息String
	
	public DialogAuthResult(Context context) 
	{
		super(context);
	}
	
	public DialogAuthResult(Context context, int theme, String info) 
	{
		super(context, theme);
		mStringInfo = info;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  //设置成系统级别的Dialog，即全局性质的Dialog，在任何界面下都可以弹出来
		getWindow().setContentView(R.layout.dialog_auth_result);
		
		init();
	}
	
	private void init()
	{
		mInfo = (TextView) findViewById(R.id.id_dialog_auth_message);
		mColse = (Button) findViewById(R.id.id_dialog_auth_sure);
		
		mInfo.setText(mStringInfo);
		mColse.setOnClickListener(this);
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
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
