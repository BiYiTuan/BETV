/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: TimeAuthService.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.service.timeAuth
 * @Description: 定时鉴权(15分钟一次)
 * @author: zhaoqy
 * @date: 2014-12-30 下午2:40:17
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.service.timeAuth;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AuthResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogAuthResult;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;

public class TimeAuthService extends Service implements UICallBack
{
	private Context          mContext;  //上下文
	private AuthResult       mAuthItem; //鉴权结果
	private DialogAuthResult mDialog;   //鉴权结果对话框
	private int              mTime = 0; //定时鉴权次数
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		//mHandler.sendEmptyMessageDelayed(FlagConstant.AUTH_COUNTDOWN, 900000);  //(15*60)*1000
		//定时鉴权服务启动后, 30秒后, 开始第一次鉴权
		mHandler.sendEmptyMessageDelayed(FlagConstant.AUTH_COUNTDOWN, 30000);  //(30)*1000
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		
		if(mHandler != null)
		{
			Logcat.i(FlagConstant.TAG, " close TimeAuth mHandler.");
			mHandler.removeCallbacksAndMessages(null);
		}
	}
	
	/**
	 * 
	 * @Title: requestAuthResult
	 * @Description: 请求应用鉴权
	 * @return: void
	 */
	@SuppressLint("SimpleDateFormat")
	private void requestAuthResult()
	{
		mTime++;
		Logcat.i(FlagConstant.TAG, " TimeAuth requestAuth ");
		long time = System.currentTimeMillis();
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(time));
		Logcat.i(FlagConstant.TAG, " date: " + date + ", " + mTime);
		RequestDataManager.requestData(this, mContext, Token.TOKEN_APP_AUTH, 0, 0);  
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
			case Token.TOKEN_APP_AUTH:
			{
				mAuthItem = (AuthResult) RequestDataManager.getData(in);
				checkAuthResult();
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, " TimeAuth " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		//15分钟后, 做下一次鉴权
		mHandler.sendEmptyMessageDelayed(FlagConstant.AUTH_COUNTDOWN, 900000);  //(15*60)*1000
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	/**
	 * 
	 * @Title: sendMsgBroadCast
	 * @Description: 发送消息广播(通知Activity鉴权失败, 可以结束该应用)
	 * @return: void
	 */
	private void sendMsgBroadCast()
	{
		Logcat.i(FlagConstant.TAG, " TimeAuth sendMsgBroadCast");
		Intent intent = new Intent();
		intent.setAction(Constant.ACTION_TIMEAUTH_FAIL);
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 
	 * @Title: checkAuthResult
	 * @Description: 验证鉴权结果
	 * @return: void
	 */
	private void checkAuthResult()
	{
		int result = mAuthItem.getAuthResult();  
		Logcat.i(FlagConstant.TAG, " TimeAuth result: " + result);
		
		if (result == 0)  //0: 表示成功;
		{
			//15分钟后, 做下一次鉴权
			mHandler.sendEmptyMessageDelayed(FlagConstant.AUTH_COUNTDOWN, 900000);  //(15*60)*1000
		}
		else
		{
			//鉴权失败
			showAuthFailResult(result);
		}
	}
	
	/**                                                                                                                                           
	 * 
	 * @Title: showAuthFailResult
	 * @Description: 显示鉴权失败结果                                                                                   
	 * @param result
	 * @return: void
	 */
	private void showAuthFailResult(int result)
	{
		String content = Util.getContent(mContext, result);
		createDialogAuthResult(content);
	}
	
	/**
	 * 
	 * @Title: createDialogAuthResult
	 * @Description: 创建鉴权失败对话框
	 * @param msg
	 * @return: void
	 */
	private void createDialogAuthResult(String msg)
	{
		mDialog = new DialogAuthResult(mContext, R.style.dialog_style, msg);
		mDialog.setOnClickCustomListener(new onClickCustomListener()
    	{
    		@Override
    		public void OnClick(View v)
    		{
    			switch (v.getId()) 
    			{
    			case R.id.id_dialog_auth_sure:
    			{
    				mDialog.dismiss();
    				//发送鉴权失败广播
    				sendMsgBroadCast();
    				break;
    			}
    			default:
    				break;
    			}
    		}
    	});
		mDialog.show();
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case FlagConstant.AUTH_COUNTDOWN:
			{
				requestAuthResult();
				break;
			}
			default:
				break;
			}
		}
	};
}
