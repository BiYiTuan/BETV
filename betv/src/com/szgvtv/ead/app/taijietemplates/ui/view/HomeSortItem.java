/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: HomeSortItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 首页Item(位置不固定, 动态加载)
 * @author: zhaoqy
 * @date: 2014-8-19 下午5:24:03
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeSortItem extends RelativeLayout 
{
	private Context        mContext;         //上下文
	private onClickCustomListener mListener; //点击监听
	private ImageView      mBg;              //背景图
	private ImageView      mIcon;            //icon
	private TextView       mName;            //名称
	
	public HomeSortItem(Context context) 
	{
		super(context);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_home_item_sort, this);
		mBg = (ImageView) findViewById(R.id.id_sort_bg);
		mIcon = (ImageView) findViewById(R.id.id_sort_icon);
		mName = (TextView) findViewById(R.id.id_sort_name);
		setFocusable(true);
	}
	
	/**
	 * 
	 * @Title: setIndex
	 * @Description: 通过设置索引, 设置背景颜色图
	 * @param index
	 * @return: void
	 */
	public void setIndex(int index)
	{
		int i = index%8;
		switch (i) 
		{
		case 0:
		{
			mBg.setImageResource(R.drawable.color_green);
			break;
		}
		case 1:
		{
			mBg.setImageResource(R.drawable.color_orange);
			break;
		}
		case 2:
		{
			mBg.setImageResource(R.drawable.color_blue);
			break;
		}
		case 3:
		{
			mBg.setImageResource(R.drawable.color_red);
			break;
		}
		case 4:
		{
			mBg.setImageResource(R.drawable.color_orange);
			break;
		}
		case 5:
		{
			mBg.setImageResource(R.drawable.color_green);
			break;
		}
		case 6:
		{
			mBg.setImageResource(R.drawable.color_red);
			break;
		}
		case 7:
		{
			mBg.setImageResource(R.drawable.color_blue);
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: setName
	 * @Description: 设置分类名称
	 * @param name
	 * @return: void
	 */
	public void setName(String name)
	{
		mName.setText(name);
	}
	
	/**
	 * 
	 * @Title: setIcon
	 * @Description: 获取Icon
	 * @param resid
	 * @return: void
	 */
	public ImageView getIcon()
	{
		return mIcon;
	}
	
	/**
	 * 
	 * @Title: setPosition
	 * @Description: 设置该View的位置
	 * @param v
	 * @param i
	 * @return: void
	 */
	public void setPosition(View v, int i)
	{
		int x = 0;
		int y = 0;
		
		if (i%2 == 0)
		{
			x =  1437 + 12 + i/2*(290+12);
			y = 112;
		}
		else if (i%2 == 1)
		{
			x = 1437 +  12 + (i-1)/2*(290+12);
			y = 228 + 112;
		}
		
		Util.setCoordinateOfView(v, x, y, 290, 216);  //该view的 width=290; height=216
	}
	
	public void setOnClickCustomListener(onClickCustomListener listener) 
	{  
		mListener = listener;  
    }
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		switch (event.getKeyCode()) 
		{
		case KeyEvent.KEYCODE_DPAD_CENTER: 
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN) 
			{
				mListener.OnClick(this);
			}
			break;
		}
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}
}
