/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: AdaptDate.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 回放日期Adapt
 * @author: zhaoqy
 * @date: 2015-2-5 下午3:27:41
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptDate extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<String>   mDataList;
	
	public AdaptDate(Context context, List<String> dataList) 
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDataList = dataList;
	}
	
	@Override
	public int getCount() 
	{
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder mHolder = null;
		
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_date, null);
			mHolder = new ViewHolder();
			mHolder.mNavigation = (TextView) convertView.findViewById(R.id.date_navigation);
			mHolder.mPos = position;
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		String source = mDataList.get(position);
		int start = source.indexOf("-");
		Logcat.d(FlagConstant.TAG, " **************************");
		String des = source.substring(start+1);
		Logcat.d(FlagConstant.TAG, " des: " + des);
		Logcat.d(FlagConstant.TAG, " **************************");
		
		mHolder.mNavigation.setText(des);
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView  mNavigation; 
        int       mPos;
    }
}
