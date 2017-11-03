/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: SpecialListItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 专题列表Item
 * @author: zhaoqy
 * @date: 2014-8-22 下午4:21:22
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SpecialListItem extends RelativeLayout 
{
	private Context   mContext; //上下文
	private ImageView mIcon;    //icon
	
	public SpecialListItem(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_special_item, this);
		mIcon = (ImageView) findViewById(R.id.id_special_icon);
	}
	
	/**
	 * 
	 * @Title: getIcon
	 * @Description: 获取mIcon
	 * @return
	 * @return: ImageView
	 */
	public ImageView getIcon()
	{
		return mIcon;
	}
}
