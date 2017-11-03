/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityBase.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 基类（Activity）:屏蔽菜单键
 * @author: zhaoqy
 * @date: 2014-8-8 上午11:39:08
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import com.szgvtv.ead.app.taijietemplates.util.Constant;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;

public class ActivityBase extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction("android.intent.action.SCREEN_OFF");  //待机键广播
	    intentFilter.addAction(Constant.ACTION_TIMEAUTH_FAIL);       //定时鉴权失败广播
	    registerReceiver(mBroadcastReceiver, intentFilter);
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
	
	protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() 
	{
		@Override
	    public void onReceive(Context context, Intent intent) 
	    {
			ActivityBase.this.onReceive(intent);
		}
	};
	 
	protected void onReceive(Intent intent) 
	{
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//返回false, 不响应菜单键  
		return false;
	}
}
