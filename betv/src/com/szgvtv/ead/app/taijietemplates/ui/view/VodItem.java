/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: VodItem.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 点播资源Item
 * @author: zhaoqy
 * @date: 2014-8-13 上午9:46:03
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VodItem extends RelativeLayout
{
	private Context   mContext;      //上下文
	private ImageView mIcon;         //icon
	private TextView  mName;         //名称
	private TextView  mUpdate;       //更新
	private String    mKeyword = ""; //关键字
	
	public VodItem(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_voditem, this);
		mIcon = (ImageView) findViewById(R.id.id_voditem_icon);
		mName = (TextView) findViewById(R.id.id_voditem_name);
		mUpdate = (TextView) findViewById(R.id.id_voditem_update);
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
	 * @Title: setKeyword
	 * @Description: 设置关键字
	 * @param keyword
	 * @return: void
	 */
	public void setKeyword(String keyword)
	{
		mKeyword = keyword;
	}
	
	/**
	 * 
	 * @Title: setName
	 * @Description: 设置名称(关键字高亮显示)
	 * @param name
	 * @return: void
	 */
	public void setName(String name)
	{
		if (mKeyword.isEmpty())
		{
			mName.setText(name);
		}
		else
		{
			//关键字高亮显示
			SpannableString s = new SpannableString(name);
			Pattern p = Pattern.compile(mKeyword);
			Matcher m = p.matcher(s);
			
			 while (m.find()) 
			 {
				 int color = mContext.getResources().getColor(R.color.blue);
				 int start = m.start();
		         int end = m.end();
		         s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		     }
			 mName.setText(s);		
		}
	}
	
	/**
	 * 
	 * @Title: setVideo
	 * @Description: 设置更新集数
	 * @param item
	 * @return: void
	 */
	public void setVideo(VideoItem item)
	{
		if (item != null )
		{
			String name = item.getName();
			int number = item.getDramaList().size();
			int total = Integer.parseInt(item.getTotalDramas());
			Logcat.d(FlagConstant.TAG, " vodtype: " + item.getVodtype() + ", name: " + name + ", number: " + number + ", total: " + total);
			Logcat.d(FlagConstant.TAG, " images: " + item.getSmallPic());
			Logcat.d(FlagConstant.TAG, " ==============================================");
			
			if (item.getVodtype().equals("2") || item.getVodtype().equals("3"))
			{
				if (total <= 1)
				{
					mUpdate.setVisibility(View.INVISIBLE);
				}
				else
				{
					if (number == total)
					{
						mUpdate.setVisibility(View.VISIBLE);
						mUpdate.setText(getResources().getString(R.string.vod_prefix_all) + number + getResources().getString(R.string.vod_suffix));
					}
					else if (number < total)
					{
						mUpdate.setVisibility(View.VISIBLE);
						mUpdate.setText(getResources().getString(R.string.vod_prefix_update) + number + getResources().getString(R.string.vod_suffix));
					}
					else  //number 大于 total
					{
						mUpdate.setVisibility(View.INVISIBLE);
					}
				}
			}
			else if (item.getVodtype().equals("1"))
			{
				mUpdate.setVisibility(View.INVISIBLE);
			}
		}
	}
}
