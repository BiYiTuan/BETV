/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: SelectDrama.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 选集Item
 * @author: zhaoqy
 * @date: 2014-8-26 下午1:48:21
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectDrama extends RelativeLayout
{
	private Context   mContext; //上下文
	private ImageView mIcon;    //icon
	private TextView  mName;    //名称

	public SelectDrama(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init()
	{
		inflate(mContext, R.layout.view_select_drama, this);
		mIcon = (ImageView) findViewById(R.id.id_select_drama_icon);
		mName = (TextView) findViewById(R.id.id_select_drama_name);
	}
	
	/**
	 * 
	 * @Title: getIconImage
	 * @Description: 获取mIcon
	 * @return
	 * @return: ImageView
	 */
	public ImageView getIcon()
	{
		return mIcon;
	}
	
	/**
	 * 
	 * @Title: setNumber
	 * @Description: 设置剧集number
	 * @param number
	 * @return: void
	 */
	public void setNumber(String number)
	{
		mName.setText(number);
	}
	
	/**
	 * 
	 * @Title: setNumberName
	 * @Description: 设置剧集number+名称
	 * @param number
	 * @param name
	 * @return: void
	 */
	public void setNumberName(String number, String name)
	{
		mName.setText(number + " " + name);
	}
}
