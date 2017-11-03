/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdaptPlayback.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 回放节目Adapt
 * @author: zhaoqy
 * @date: 2014-9-1 下午7:43:08
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptPlayback extends BaseAdapter
{
	private LayoutInflater         mInflater;
	private List<LivePlaybackItem> mDataList;
	
	public AdaptPlayback(Context context, List<LivePlaybackItem> dataList) 
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
			convertView = mInflater.inflate(R.layout.item_playback, null);
			mHolder = new ViewHolder();
			//mHolder.mDate  = (TextView) convertView.findViewById(R.id.id_playback_item_date);
			mHolder.mTime  = (TextView) convertView.findViewById(R.id.id_playback_item_time);
			mHolder.mName  = (TextView) convertView.findViewById(R.id.id_playback_item_name);
			mHolder.mPos = position;
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		//mHolder.mDate.setText(mDataList.get(position).getDate());
		mHolder.mTime.setText(mDataList.get(position).getTime());
		mHolder.mName.setText(mDataList.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView  mDate;
        TextView  mTime;
        TextView  mName;
        int       mPos;
    }
}
