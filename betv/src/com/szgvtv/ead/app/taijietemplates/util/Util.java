/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: Util.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 工具类
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:30:34
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DictionaryItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DictionaryValueItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.xmlpull.PullParse;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Util 
{
	/**
	 * 
	 * @Title: amplifyItem
	 * @Description: 放大自定义View
	 * @param view   当前选中View
	 * @param focus  放大ImageView
	 * @param multi  放大倍数
	 * @return: void
	 */
	public static void amplifyItem(View view, ImageView focus, double multi)
	{
		int width = view.getWidth();
		int height = view.getHeight();
		int top = view.getTop();
		int left = view.getLeft();
		int addWidth = (int) ((float) multi * width);
		int addHeight = (int) ((float) multi * height);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) focus.getLayoutParams();
		params.leftMargin = left - addWidth/2 - 20;
		params.topMargin = top - addHeight/2 -20;
		params.width = width + addWidth + 20*2;
		params.height = height + addHeight + 20*2;
				                   
		focus.setLayoutParams(params);
		focus.setVisibility(View.VISIBLE);
		focus.bringToFront();
	}
	
	/**
	 * 
	 * @Title: amplifySpecialItem
	 * @Description: 放大自定义专题View
	 * @param view
	 * @param focus
	 * @param multi
	 * @return: void
	 */
	public static void amplifySpecialItem(View view, ImageView focus, double multi)
	{
		int width = view.getWidth();
		int height = view.getHeight();
		int top = view.getTop();
		int left = view.getLeft();
		int addWidth = (int) ((float) multi * width);
		int addHeight = (int) ((float) multi * height);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) focus.getLayoutParams();
		params.leftMargin = left - addWidth/2 - 20 - 144;
		params.topMargin = top - addHeight/2 -20;
		params.width = width + addWidth + 20*2;
		params.height = height + addHeight + 20*2;
				                   
		focus.setLayoutParams(params);
		focus.setVisibility(View.VISIBLE);
		focus.bringToFront();
	}
	
	/**
	 * 
	 * @Title: getCoordinateOfView
	 * @Description: 获取View的横坐标
	 * @param v
	 * @return
	 * @return: int
	 */
	public static int getXCoordinateOfView(View v)
	{
		//获取View在屏幕的坐标
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int x = location[0];   //x坐标
	    //int y = location[1]; //y坐标
		return x;		
	}
	
	/**
	 * 
	 * @Title: setCoordinateOfView
	 * @Description: 设置坐标 (设置控件所在的位置YY，并且不改变宽高，XY为绝对位置)
	 * @param view
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return: void
	 */
	public static void setCoordinateOfView(View view,int x,int y, int width, int height)
	{
		RelativeLayout.LayoutParams rllp =new RelativeLayout.LayoutParams(view.getLayoutParams());
		rllp.leftMargin = x;
		rllp.topMargin = y;
		rllp.width = width;
		rllp.height = height;
		view.setLayoutParams(rllp);
	} 
	
	public static void setCoordinateOfView2(View view,int x,int y/*, int width, int height*/)
	{
		//使用MarginLayoutParams有bug
		MarginLayoutParams margin = new MarginLayoutParams(view.getLayoutParams());
		int width = x+margin.width;
		int height = y+margin.height;
		margin.setMargins(x, y, width, height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams);
	} 
	
	/** 
     * 格式化时间单元(时、分、秒) 小于10的话在十位上补0，如传入2的话返回02，传入12的话返回12 
     * @param time 播放时间 
     * @return 格式化后的时间,如(02) 
     */  
    public static String formatTimeUnit(int time) 
    {  
        return time < 10 ? "0" + time : "" + time;  
    }  
	
	/** 
     * @param format_time 
     * @return (时:分:秒)格式的时间格式，如(00:03:00) 
     */  
    public static String formatTimeString(int format_time) 
    {  
    	int second = format_time / 1000;
        String hours=formatTimeUnit(second / 3600);        //时  
        String minutes=formatTimeUnit((second / 60) % 60); //分  
        String seconds=formatTimeUnit(second % 60);        //秒  
        
        return hours + ":" + minutes+ ":" + seconds;  
    }  
	
	/** 
     * @param current_time 当前播放时间 
     * @param total_time 总播放时间 
     * @return 当前播放时间/总播放时间，如(00:03:02/00:31:52) 
     */  
    public static String getFormatTime(int current_time, int total_time) 
    {  
        current_time = Math.abs(current_time); //得到当前播放时间的绝对值  
        total_time = Math.abs(total_time);     //得到总播放时间的绝对值  
        
        return formatTimeString(current_time) + "/" + formatTimeString(total_time);  
    }
	
	/**
     * 
     * @Title: calculateLength
     * @Description: 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1 
     * @param c
     * @return
     * @return: long
     */
	public static long calculateLength(CharSequence c)
    {  
        double len = 0;  
        for (int i = 0; i < c.length(); i++) 
        {  
            int tmp = (int) c.charAt(i);  
            if (tmp > 0 && tmp < 127) 
            {  
                len += 0.5;  
            } 
            else 
            {  
                len++;  
            }  
        }  
        return Math.round(len);  
    }  
	
	/**
	 * 
	 * @Title: recycle
	 * @Description: 内存回收
	 * @param bitmap
	 * @return: void
	 */
	public static void recycle(Bitmap bitmap)
	{
		//释放 Bitmap对象
	    if(bitmap != null && !bitmap.isRecycled()) 
	    {  
	    	bitmap.recycle();  
	    	System.gc();
	    	bitmap = null;
	    }  
	}
	
	/**
	 * 
	 * @Title: getContent
	 * @Description: 获取字典内容
	 * @param context
	 * @param result
	 * @return
	 * @return: String
	 */
	public static String getContent(Context context, int result)
	{
		String content = "";
		ArrayList<DictionaryItem> dictonaryList = new ArrayList<DictionaryItem>();
		
		try 
		{
			dictonaryList = PullParse.parseDictionary(context);
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, " parseDictonary error.");
		}
		
		DictionaryItem item;
		DictionaryValueItem value;
		
		for (int i=0; i<dictonaryList.size(); i++)
		{
			item = dictonaryList.get(i);
			if (result == item.getStatus())
			{
				for (int j=0; j<item.getContentList().size(); j++)
				{
					value = item.getContentList().get(j);
					if (value.getLanguage().equals(StbInfo.getShowLaug()))
					{
						content = value.getContent();
					}
				}
			}
		}
		
		return content;
	}
}
