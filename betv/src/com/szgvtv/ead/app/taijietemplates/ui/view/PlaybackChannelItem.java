/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PlaybackChannelItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 回放频道Item
 * @author: zhaoqy
 * @date: 2014-8-20 下午2:21:26
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaybackChannelItem extends RelativeLayout
{
	private Context   mContext; //上下文
	private ImageView mBg;      //item背景
	private ImageView mIcon;    //icon
	private ImageView mIconBg;  //icon背景
	private TextView  mName;    //名称
	
	public PlaybackChannelItem(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_playback_channel_item, this);
		mBg = (ImageView) findViewById(R.id.id_playback_item_bg);
		mIcon = (ImageView) findViewById(R.id.id_playback_item_icon);
		mIconBg = (ImageView) findViewById(R.id.id_playback_item_icon_bg);
		mName = (TextView) findViewById(R.id.id_playback_item_name_r);
	}
	
	/**
	 * 
	 * @Title: getItemIcon
	 * @Description: 获取Item的图标Icon
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
	 * @Description: 设置Item的名称
	 * @param name
	 * @return: void
	 */
	public void setName(String name)
	{
		mName.setText(name);
	}

	/**
	 * 
	 * @Title: setItemBackground
	 * @Description: 设置Item的背景图
	 * @param index
	 * @return: void
	 */
	public void setItemBackground(int index)
	{
		switch (index%16) 
		{
		case 0:
		{
			mBg.setImageResource(R.drawable.color_blue);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_blue);
			break;
		}
		case 1:
		{
			mBg.setImageResource(R.drawable.color_orange);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_orange);
			break;
		}
		case 2:
		{
			mBg.setImageResource(R.drawable.color_red);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_red);
			break;
		}	
		case 3:
		{
			mBg.setImageResource(R.drawable.color_green);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_green);
			break;
		}	
		case 4:
		{
			mBg.setImageResource(R.drawable.color_orange);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_orange);
			break;
		}
		case 5:
		{
			mBg.setImageResource(R.drawable.color_red);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_red);
			break;
		}
		case 6:
		{
			mBg.setImageResource(R.drawable.color_green);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_green);
			break;
		}
		case 7:
		{
			mBg.setImageResource(R.drawable.color_blue);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_blue);
			break;
		}
		case 8:
		{
			mBg.setImageResource(R.drawable.color_red);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_red);
			break;
		}
		case 9:
		{
			mBg.setImageResource(R.drawable.color_green);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_green);
			break;
		}
		case 10:
		{
			mBg.setImageResource(R.drawable.color_blue);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_blue);
			break;
		}
		case 11:
		{
			mBg.setImageResource(R.drawable.color_orange);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_orange);
			break;
		}
		
		case 12:
		{
			mBg.setImageResource(R.drawable.color_green);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_green);
			break;
		}
		case 13:
		{
			mBg.setImageResource(R.drawable.color_blue);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_blue);
			break;
		}
		case 14:
		{
			mBg.setImageResource(R.drawable.color_orange);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_orange);
			break;
		}
		case 15:
		{
			mBg.setImageResource(R.drawable.color_red);
			mIconBg.setImageResource(R.drawable.playback_icon_bg_red);
			break;
		}
		default:
			break;
		}
	}
}
