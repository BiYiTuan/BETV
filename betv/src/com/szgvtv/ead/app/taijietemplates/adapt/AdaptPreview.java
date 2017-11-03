/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdaptPreview.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 节目预告Adapt
 * @author: zhaoqy
 * @date: 2014-9-1 下午7:40:57
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptPreview extends BaseAdapter
{
	private LayoutInflater        mInflater;
	private List<LivePreviewItem> mDataList;
	
	public AdaptPreview(Context context, List<LivePreviewItem> dataList) 
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
			convertView = mInflater.inflate(R.layout.item_preview_item, null);
			mHolder = new ViewHolder();
			mHolder.mTime  = (TextView) convertView.findViewById(R.id.id_preview_item_time);
			mHolder.mName  = (TextView) convertView.findViewById(R.id.id_preview_item_name);
			mHolder.mPos = position;
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.mTime.setText(mDataList.get(position).getTime());
		mHolder.mName.setText(mDataList.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView  mTime;
        TextView  mName;
        int       mPos;
    }
}
