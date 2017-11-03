/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DotGroup.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 表示页码的圆点集（页码总数小于6）
 * @author: zhaoqy
 * @date: 2014-8-11 下午1:43:23
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DotGroup extends RelativeLayout
{
	public static final int WIDTH = 1280;                   //DotGroup的宽度
	public static final int TOP = 635;                      //DotGroup的高度
	public static final int SPACE = 16;                     //dot的间距
	public static final int DOT_WIDTH = 16;                 //dot的宽度
	public static final int DOT_HEIGHT = 16;                //dot的高度
	public static final int MAX = 5;                        //最多显示dot个数
	private Context        mContext;                        //上下文
	private ImageView      mDotIcon[] = new ImageView[MAX]; //图片表示页码
	private TextView       mPage;                           //数字表示页码
	private int            mTotNum = 0;                     //总页数
	private int            mInit_x = 0;                     //第一张图片的x坐标
	private int            mDot_x = 0;                      //每张图片的x坐标
	private int            mTop = TOP;                      //高度
	
	public DotGroup(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_dotgroup, this);
		mDotIcon[0] = (ImageView) findViewById(R.id.id_dot_icon1);
		mDotIcon[1] = (ImageView) findViewById(R.id.id_dot_icon2);
		mDotIcon[2] = (ImageView) findViewById(R.id.id_dot_icon3);
		mDotIcon[3] = (ImageView) findViewById(R.id.id_dot_icon4);
		mDotIcon[4] = (ImageView) findViewById(R.id.id_dot_icon5);
		mPage = (TextView) findViewById(R.id.id_dot_page);
	}
	
	/**
	 * 
	 * @Title: setDotTop
	 * @Description: 设置高度(绝对高度)
	 * @param top
	 * @return: void
	 */
	public void setDotTop(int top)
	{
		mTop = top;
	}
	
	/**
	 * 
	 * @Title: setDotTotalNumber
	 * @Description: 设置总页数(对外接口)
	 * @param number
	 * @return: void
	 */
	public void setDotTotalNumber(int number)
	{
		mTotNum = number;
		setDotPosition();
		
		if (mTotNum > MAX)
		{
			for(int i=0; i<MAX; i++)
			{
				mDotIcon[i].setVisibility(View.GONE);
			}
			mPage.setVisibility(View.VISIBLE);
		}
		else
		{
			mPage.setVisibility(View.GONE);
			
			for(int i=0; i<MAX; i++)
			{
				if (number == 1)
				{
					mDotIcon[i].setVisibility(View.GONE);
				}
				else
				{
					if (i < number)
					{
						mDotIcon[i].setVisibility(View.VISIBLE);
					}
					else
					{
						mDotIcon[i].setVisibility(View.GONE);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: setDotCurrentNumber
	 * @Description: 设置当前页码(对外接口)
	 * @param number
	 * @return: void
	 */
	public void setDotCurrentNumber(int number)
	{
		if (mTotNum > MAX)
		{
			mPage.setText(number + "/" + mTotNum);
		}
		else
		{
			if (mTotNum > 1 && mTotNum <= MAX)
			{
				for (int i=0; i<mTotNum; i++)
				{
					if (i == number-1)
					{
						mDotIcon[i].setImageResource(R.drawable.dot_focus);
					}
					else
					{
						mDotIcon[i].setImageResource(R.drawable.dot_unfocus);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title: setDotPosition
	 * @Description: 重新设置控件坐标
	 * @return: void
	 */
	private void setDotPosition()
	{
		if (mTotNum > 0)
		{
			if (mTotNum <= MAX)
			{
				if (mTotNum > 1)
				{
					mInit_x = (int)(WIDTH - DOT_WIDTH*mTotNum - SPACE*(mTotNum-1))/2;
					
					for (int i=0; i<mTotNum; i++)
					{
						mDot_x = mInit_x + i*(DOT_WIDTH + SPACE);
						Util.setCoordinateOfView(mDotIcon[i], mDot_x, mTop, DOT_WIDTH, DOT_HEIGHT);
					}
				}
			}
			else
			{
				mInit_x = 0;
				Util.setCoordinateOfView2(mPage, mInit_x, mTop);
			}
		}
	}
}
