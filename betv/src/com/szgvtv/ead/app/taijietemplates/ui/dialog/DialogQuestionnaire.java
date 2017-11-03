/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DialogQuestionnaire.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.dialog
 * @Description: 问卷调查入口对话框
 * @author: zhaoqy
 * @date: 2014-9-17 上午10:27:43
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
import android.widget.TextView;

public class DialogQuestionnaire extends Dialog implements OnClickListener
{
	private onClickCustomListener mListener;
	private TextView              mText;       
	
	public DialogQuestionnaire(Context context) 
	{
		super(context);
	}
	
	public DialogQuestionnaire(Context context, int theme) 
	{
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  //设置成系统级别的Dialog，即全局性质的Dialog，在任何界面下都可以弹出来
		getWindow().setContentView(R.layout.dialog_questionnaire);
		init();
	}
	
	private void init()
	{
		mText = (TextView) findViewById(R.id.id_dialog_questionnaire_text);
		mText.setOnClickListener(this);
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
		switch (keyCode) 
		{
		case KeyEvent.KEYCODE_MENU:
		{
			this.dismiss();
			return true;
		}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
