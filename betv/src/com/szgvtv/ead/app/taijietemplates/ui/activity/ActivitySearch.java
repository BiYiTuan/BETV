/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivitySearch.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 搜索Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:52:58
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.adapt.AdaptHotword;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HotwordItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.service.bi.BiMsg;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.ShowToast;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import com.szgvtv.ead.app.taijietemplates.util.Util;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ActivitySearch extends ActivityBase implements OnItemClickListener, OnClickListener, UICallBack
{
	private static final int MAX_COUNT = 32;                             //最多输入32个字符
	private Context                mContext;                             //上下文
	private LoadingPage            mLoading;                             //加载
	private EditText               mEditText;                            //编辑框
	private RelativeLayout         mSearch;                              //搜索
	private GridView               mGridView;                            //热词GridView
	private AdaptHotword           mAdapt;                               //热词Adapt
	private ArrayList<HotwordItem> mHots = new ArrayList<HotwordItem>(); //热词列表
	private int                    mCount = 0;                           //热词总个数
	private int                    mSize = 15;                           //最多显示个数
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_search);
		
		initViews();
		requestHotWord();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_search_loading_page);
		mEditText = (EditText) findViewById(R.id.id_search_edit);
		mSearch = (RelativeLayout) findViewById(R.id.id_search_button);
		mGridView = (GridView) findViewById(R.id.id_search_hotword);
		
		mSearch.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mAdapt = new AdaptHotword(mContext, mHots);
		mGridView.setAdapter(mAdapt);
		mHots.clear();
		
		mEditText.addTextChangedListener(mTextWatcher);  
        mEditText.setSelection(mEditText.length()); // 将光标移动最后一个字符后面  
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() 
	{  
        private int editStart;  
        private int editEnd;  
  
        public void afterTextChanged(Editable s) 
        {  
            editStart = mEditText.getSelectionStart();  
            editEnd = mEditText.getSelectionEnd();  
  
            // 先去掉监听器，否则会出现栈溢出  
            mEditText.removeTextChangedListener(mTextWatcher);  
  
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度  
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1  
            while (Util.calculateLength(s.toString()) > MAX_COUNT)   //当输入字符个数超过限制的大小时，进行截断操作  
            {
                s.delete(editStart - 1, editEnd);  
                editStart--;  
                editEnd--;  
            }  
            
            mEditText.setSelection(editStart);  
            mEditText.addTextChangedListener(mTextWatcher);    //恢复监听器  
        }  
  
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {  
        }  
  
        public void onTextChanged(CharSequence s, int start, int before, int count) 
        {  
        }
    };  
	
	/**
	 * 
	 * @Title: requestHotWord
	 * @Description: 请求热词
	 * @return: void
	 */
	private void requestHotWord()
	{
		mLoading.setLoadPageFail(false);
		mLoading.show();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_HOT_WORD, mSize, 1);  
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_search_button:
		{
			String hotword = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(hotword))
			{
				ShowToast.getShowToast().createToast(mContext, getResources().getString(R.string.toast_no_keyword), "");
			}
			else
			{
				//点击搜索BI
				BiMsg.sendSearchBiMsg(hotword);
				
				Intent intent = new Intent(this, ActivitySearchResult.class);
				intent.putExtra(FlagConstant.HotWordKey, hotword);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击热词BI
		BiMsg.sendHotSearchBiMsg(mHots.get(position).getName());	
		
		String hotword = mHots.get(position).getName();
		Intent intent = new Intent(this, ActivitySearchResult.class);
		intent.putExtra(FlagConstant.HotWordKey, hotword);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onCancel(OutPacket out, int token) 
	{
		onNetError(-1, "error", null, token);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccessful(Object in, int token) 
	{
		try 
		{
			switch (token) 
			{
			case Token.TOKEN_HOT_WORD:
			{
				ArrayList<HotwordItem> temp = new ArrayList<HotwordItem>();
				temp = (ArrayList<HotwordItem>) RequestDataManager.getData(in);
				for (int i=0; i<temp.size(); i++)
				{
					mHots.add(temp.get(i));
				}
				freshHotwordList();
				break;
			}
			default:
				break;
			}
		} 
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== ActivitySearch ==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		mLoading.hide();
	}
	
	/**
	 * 
	 * @Title: freshHotwordList
	 * @Description: 刷新热词列表
	 * @return: void
	 */
	private void freshHotwordList()
	{
		mLoading.hide();
		mCount = mHots.size();
		Logcat.d(FlagConstant.TAG, "=== mCount: " + mCount);
		
		if (mCount > 0)
		{
			mAdapt.notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		boolean nRet = false;
		
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyDown();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyUp();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = true;
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRight();
		}
		
		if (nRet)
		{
			return nRet;
		}
		else 
		{
			return super.dispatchKeyEvent(event);  
		}
	}
	
	/**
	 * 
	 * @Title: doKeyDown
	 * @Description: 响应下键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyDown()
	{
		if (mEditText.isFocused() || mSearch.isFocused())
		{
			if (mCount > 0)
			{
				mGridView.requestFocus();
				mGridView.setSelection(0);
			}
			return true;
		}
		else if (mGridView.isFocused())
		{
			int curIndex = mGridView.getSelectedItemPosition();
			
			if (curIndex >= 12 && curIndex <= 14)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyUp
	 * @Description: 响应上键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyUp()
	{
		if (mGridView.isFocused())
		{
			int curIndex = mGridView.getSelectedItemPosition();
			
			if (curIndex >= 0 && curIndex <= 1)
			{
				mEditText.requestFocus();
				return true;
			}
			else if (curIndex == 2)
			{
				mSearch.requestFocus();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyLeft
	 * @Description: 响应左键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyLeft()
	{
		if (mGridView.isFocused())
		{
			int curIndex = mGridView.getSelectedItemPosition();
			int count = mGridView.getChildCount();
			Logcat.d(FlagConstant.TAG, "=== count: " + count);
			
			if (curIndex == 0)
			{
				mGridView.setSelection(count-1);
				return true;
			}
			else if (curIndex == 3 || curIndex == 6 || curIndex == 9 || curIndex == 12)
			{
				mGridView.setSelection(curIndex-1);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyRight
	 * @Description: 响应右键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyRight()
	{
		if (mGridView.isFocused())
		{
			int curIndex = mGridView.getSelectedItemPosition();
			int count = mGridView.getChildCount();
			
			if (curIndex == count-1)
			{
				mGridView.setSelection(0);
				return true;
			}
			else if (curIndex == 2 || curIndex == 5 || curIndex == 8 || curIndex == 11)
			{
				mGridView.setSelection(curIndex+1);
				return true;
			}
		}
		return false;
	}
}
