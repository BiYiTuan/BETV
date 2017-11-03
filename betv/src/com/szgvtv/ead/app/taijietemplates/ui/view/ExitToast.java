package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ExitToast 
{
	private static ExitToast mExitToast = new ExitToast();
	private static Toast     mToast; //Toast
	private TextView         mText;  //主要信息
	
	public static ExitToast getExitToast()
	{
		if (mExitToast == null)
		{
			mExitToast = new ExitToast();
		}
		return mExitToast;
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
	public void createToast(Context context, String text)
	{
		if(mToast == null)
		{
			mToast = new Toast(context);
			View view = View.inflate(context, R.layout.view_exittoast, null);
			
			mText = (TextView) view.findViewById(R.id.id_toast_text);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.setView(view);
		}
		else
		{
			mText = (TextView) mToast.getView().findViewById(R.id.id_toast_text);
		}
		
		mText.setText(text);
		mToast.setGravity(Gravity.TOP | Gravity.LEFT, 353, 500); //改变Toast的位置(以点(0,0)为起始点）
		mToast.show();
	}
}
