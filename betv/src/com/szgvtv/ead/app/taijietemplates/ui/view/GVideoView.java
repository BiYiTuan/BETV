/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: GVideoView.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.view
 * @Description: VideoView播放器类
 * @author: zhaoqy
 * @date: 2014-8-8 上午10:42:33
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class GVideoView extends VideoView
{
	public GVideoView(Context context) 
	{
		super(context);
	}
	
	public GVideoView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public GVideoView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		//播放界面全屏显示
		int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width,height);
	}
}
