/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityReceiver.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 基类Activity(带广播)
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:44:50
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

public class ActivityReceiver extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");       //网络广播
	    intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS"); //Home键广播
	    intentFilter.addAction("android.intent.action.SCREEN_OFF");           //待机键广播
	    intentFilter.addAction(Constant.ACTION_TIMEAUTH_FAIL);                //定时鉴权失败广播
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
			 ActivityReceiver.this.onReceive(intent);
		 }
	 };
	 
	 protected void onReceive(Intent intent) 
	 {
	 }
}
