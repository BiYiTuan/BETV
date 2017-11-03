/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ShowToast.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 自定义Toast
 * @author: zhaoqy
 * @date: 2014-8-13 上午10:19:49
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowToast 
{
	private static ShowToast mShowToast = new ShowToast();
	private static Toast     mToast;     //Toast
	private TextView         mMainText;  //主要信息
	private TextView         mMinorText; //次要信息
	
	/**
	 * 
	 * @Title: getShowToast
	 * @Description: 获取ShowToast实例
	 * @return
	 * @return: ShowToast
	 */
	public static ShowToast getShowToast()
	{
		if (mShowToast == null)
		{
			mShowToast = new ShowToast();
		}
		return mShowToast;
	}
	
	/**
	 * 
	 * @Title: createToast
	 * @Description: 创建Toast, 并显示Toast
	 * @param context
	 * @param resid 图片icon
	 * @param main_text 主要信息
	 * @param minor_text 次要信息(时而有,根据需要)
	 * @return: void
	 */
	public void createToast(Context context, String main_text, String minor_text)
	{
		if(mToast == null)
		{
			mToast = new Toast(context);
			View view = View.inflate(context, R.layout.view_showtoast, null);
			
			mMainText = (TextView) view.findViewById(R.id.id_toast_main_text);
			mMinorText = (TextView) view.findViewById(R.id.id_toast_minor_text);
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setView(view);
		}
		else
		{
			mMainText = (TextView) mToast.getView().findViewById(R.id.id_toast_main_text);
			mMinorText = (TextView) mToast.getView().findViewById(R.id.id_toast_minor_text);
		}
		
		if (minor_text.isEmpty())
		{
			mMainText.setVisibility(View.VISIBLE);
			mMinorText.setVisibility(View.GONE);
			mMainText.setText(main_text);
		}
		else
		{
			mMainText.setVisibility(View.VISIBLE);
			mMinorText.setVisibility(View.VISIBLE);
			mMainText.setText(main_text);
			mMinorText.setText(minor_text);
		}
		
		mToast.setGravity(Gravity.TOP | Gravity.LEFT, 353, 420); //改变Toast的位置(以点(0,0)为起始点）
		mToast.show();
	}
}
