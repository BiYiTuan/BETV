package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomeSortInflectItem extends RelativeLayout
{
	private Context   mContext; //上下文
	private ImageView mIcon;    //icon
	
	public HomeSortInflectItem(Context context) 
	{
		super(context);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_home_item_sort_inflect, this);
		mIcon = (ImageView) findViewById(R.id.id_sort_inflect_icon);
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
	 * @Description: 设置view的位置
	 * @param v
	 * @param i
	 * @return: void
	 */
	public void setPosition(View v, int i)
	{
		int x = 0;
		
		if (i%2 == 1)
		{
			x = (i-1)/2*(290+12);
		}
		
		Util.setCoordinateOfView(v, x, 0, 290, 140); //该view的 width=290; height=140
	}
}
