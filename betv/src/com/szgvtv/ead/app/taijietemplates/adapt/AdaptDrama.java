/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdaptDrama.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 剧集Adapt
 * @author: zhaoqy
 * @date: 2014-8-26 下午4:07:13
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptDrama extends BaseAdapter
{
	private LayoutInflater mInflater;
	private List<String>   mDataList;
	
	public AdaptDrama(Context context, List<String> dataList) 
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
			convertView = mInflater.inflate(R.layout.item_drama, null);
			mHolder = new ViewHolder();
			mHolder.mNavigation = (TextView) convertView.findViewById(R.id.drama_navigation);
			mHolder.mPos = position;
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.mNavigation.setText(mDataList.get(position));
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView  mNavigation; 
        int       mPos;
    }
}
