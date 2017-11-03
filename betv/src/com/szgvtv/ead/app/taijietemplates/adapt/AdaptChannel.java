/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdaptChannel.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 直播频道Adapt
 * @author: zhaoqy
 * @date: 2014-9-1 下午7:41:39
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptChannel extends BaseAdapter
{
	private LayoutInflater        mInflater;
	private List<LiveChannelItem> mDataList;
	private int                   mCurPage;
	private int                   mSize;
	
	public AdaptChannel(Context context, List<LiveChannelItem> dataList, int size) 
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDataList = dataList;
		mSize = size;
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
			convertView = mInflater.inflate(R.layout.item_channel_item, null);
			mHolder = new ViewHolder();
			mHolder.mNum  = (TextView) convertView.findViewById(R.id.id_channel_item_num);
			mHolder.mName  = (TextView) convertView.findViewById(R.id.id_channel_item_name);
			mHolder.mPos = position;
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		int number = position + 1 + (mCurPage-1)*mSize;
		if (number < 10)
		{
			mHolder.mNum.setText("00" + number);
		}
		else if (number < 100)
		{
			mHolder.mNum.setText("0" + number);
		}
		else if (number < 1000)
		{
			mHolder.mNum.setText("" + number);
		}
		
		mHolder.mName.setText(mDataList.get(position).getTvName());
		return convertView;
	}
	
	public void setCurrentPage(int current)
	{
		mCurPage = current;
	}
	
	static class ViewHolder 
	{
        TextView  mNum;
        TextView  mName;
        int       mPos;
    }
}
