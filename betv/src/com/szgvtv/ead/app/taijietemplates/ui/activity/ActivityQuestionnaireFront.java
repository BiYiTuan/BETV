/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityQuestionnaireFront.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 问卷调查前沿Activity
 * @author: zhaoqy
 * @date: 2014-8-26 下午7:03:14
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityQuestionnaireFront extends ActivityBase implements OnClickListener
{
	private Button   mStart;   //立即参加
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire_front);
		initViews();
	}
	
	private void initViews()
	{
		mStart = (Button) findViewById(R.id.id_questionnaire_front_start);
		mStart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_questionnaire_front_start:
		{
			Intent intent = new Intent(this, ActivityQuestionnaire.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
	}
}
