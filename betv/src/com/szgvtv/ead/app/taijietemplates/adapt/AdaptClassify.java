/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AdaptClassify.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.adapt
 * @Description: 分类Adapt
 * @author: zhaoqy
 * @date: 2014-8-26 下午4:07:57
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.adapt;

import java.util.List;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.ClassifyItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptClassify extends BaseAdapter
{
	private LayoutInflater     mInflater;
	private List<ClassifyItem> mDataList;
	
	public AdaptClassify(Context context, List<ClassifyItem> dataList) 
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
	
	public int getItemIndex(View v)
	{
		ViewHolder holder = (ViewHolder) v.getTag();
		
		if(holder != null)
		{
			return holder.mPos;
		}
		else
		{
			return -1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_classify, null);
			holder = new ViewHolder();
			holder.mNavigation = (TextView) convertView.findViewById(R.id.classify_name);
			holder.mPos = position;
			convertView.setTag(position);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mNavigation.setText(mDataList.get(position).getClassifyName());
		return convertView;
	}
	
	static class ViewHolder 
	{
        TextView mNavigation; 
        int  mPos;
    }
}
