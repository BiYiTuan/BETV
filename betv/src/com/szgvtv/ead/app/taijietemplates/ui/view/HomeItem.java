/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: HomeItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 首页Item(固定位置)
 * @author: zhaoqy
 * @date: 2014-8-19 下午5:23:22
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeItem extends RelativeLayout 
{
	private Context   mContext;   //上下文
	private ImageView mIcon;      //icon
	private TextView  mName;      //名称
	private int       mIndex = 0; //索引

	@SuppressLint("Recycle")
	public HomeItem(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		
		TypedArray attrs_index = context.obtainStyledAttributes(attrs, R.styleable.attrs_index);
		mIndex = attrs_index.getInteger(R.styleable.attrs_index_index, 0);
		init();
	}
	
	private void init()
	{
		switch (mIndex) 
		{
		case 1:
		{
			inflate(mContext, R.layout.view_home_item_tv, this);
			mIcon = (ImageView) findViewById(R.id.id_tv_icon);
			mName = (TextView) findViewById(R.id.id_tv_name);
			break;
		}
		case 2:
		{
			inflate(mContext, R.layout.view_home_item_moive, this);
			mIcon = (ImageView) findViewById(R.id.id_moive_icon);
			mName = (TextView) findViewById(R.id.id_moive_name);
			break;
		}	
		case 3:
		{
			inflate(mContext, R.layout.view_home_item_live, this);
			mIcon = (ImageView) findViewById(R.id.id_live_icon);
			mName = (TextView) findViewById(R.id.id_live_name);
			break;
		}
		case 4:
		{
			inflate(mContext, R.layout.view_home_item_special, this);
			mIcon = (ImageView) findViewById(R.id.id_special_icon);
			mName = (TextView) findViewById(R.id.id_special_name);
			break;
		}
		case 5:
		{
			inflate(mContext, R.layout.view_home_item_playback, this);
			mIcon = (ImageView) findViewById(R.id.id_playback_icon);
			mName = (TextView) findViewById(R.id.id_playback_name);
			break;
		}	
		case 6:
		{
			inflate(mContext, R.layout.view_home_item_search, this);
			mIcon = (ImageView) findViewById(R.id.id_search_icon);
			mName = (TextView) findViewById(R.id.id_search_name);
			break;
		}	
		case 7:
		{
			inflate(mContext, R.layout.view_home_item_history, this);
			mIcon = (ImageView) findViewById(R.id.id_history_icon);
			mName = (TextView) findViewById(R.id.id_history_name);
			break;
		}	
		case 8:
		{
			inflate(mContext, R.layout.view_home_item_favorite, this);
			mIcon = (ImageView) findViewById(R.id.id_favorite_icon);
			mName = (TextView) findViewById(R.id.id_favorite_name);
			break;
		}	
		case 9:
		{
			inflate(mContext, R.layout.view_home_item_recommend, this);
			mIcon = (ImageView) findViewById(R.id.id_recommend_icon);
			break;
		}	
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: getIcon
	 * @Description: 获取Icon
	 * @return
	 * @return: ImageView
	 */
	public ImageView getIcon()
	{
		return mIcon;
	}
	
	/**
	 * 
	 * @Title: setName
	 * @Description: 设置名称
	 * @param name
	 * @return: void
	 */
	public void setName(String name)
	{
		mName.setText(name);
	}
}
