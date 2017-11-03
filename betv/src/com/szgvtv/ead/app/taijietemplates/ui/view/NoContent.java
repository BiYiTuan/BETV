/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: NoContent.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: 无内容(无搜索结果 无回放列表 无历史记录 无收藏记录)
 * @author: zhaoqy
 * @date: 2014-8-13 上午9:59:29
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoContent extends RelativeLayout
{
	private Context  mContext; //上下文
	private TextView mMessage; //无内容信息
	
	public NoContent(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init()
	{
		inflate(mContext, R.layout.view_no_content, this);
		mMessage = (TextView) findViewById(R.id.id_nocontent_message);
	}
	
	/**
	 * 
	 * @Title: setInfo
	 * @Description: 设置信息
	 * @param info
	 * @return: void
	 */
	public void setMessage(String message)
	{
		mMessage.setText(message);
	}
}
