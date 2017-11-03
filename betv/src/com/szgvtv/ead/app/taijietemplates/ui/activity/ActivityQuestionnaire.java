/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ActivityQuestionnaire.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.ui.activity
 * @Description: 问卷调查Activity
 * @author: zhaoqy
 * @date: 2014-8-8 下午1:51:14
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.ui.activity;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceOption;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager.RequestDataManager;
import com.szgvtv.ead.app.taijietemplates.ui.dialog.DialogPrompt;
import com.szgvtv.ead.app.taijietemplates.ui.interfaces.onClickCustomListener;
import com.szgvtv.ead.app.taijietemplates.ui.view.LoadingPage;
import com.szgvtv.ead.app.taijietemplates.ui.view.ShowToast;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityQuestionnaire extends ActivityBase implements OnClickListener, UICallBack
{
	private Context               mContext;                               //上下文
	private RelativeLayout        mOptionsRlyt[] = new RelativeLayout[6]; //调查View
	private RelativeLayout        mOption;                                //调查内容
	private LoadingPage           mLoading;                               //加载
	private ImageView             mIcon[] = new ImageView[6];             //图标
	private TextView              mContent[] = new TextView[6];           //内容
	private TextView              mTopic;                                 //题标
	private TextView              mTitle;                                 //题目
	private Button                mSend;                                  //发送
	private DialogPrompt          mDialog;                                //提示框
	private ArrayList<AdviceItem> mAdvices = new ArrayList<AdviceItem>(); //建议列表
	private int                   mType;                                  //类型(单选 多选)
	private int                   mNumber = 0;                            //题号
	private boolean               mSendResulting = false;                 //是否正在发送调查结果
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_questionnaire);
		
		initViews();
		requestAdvice();
	}
	
	private void initViews()
	{
		mLoading = (LoadingPage) findViewById(R.id.id_questionnaire_loading_page);
		mTitle = (TextView) findViewById(R.id.id_questionnaire_title);
		mTopic = (TextView) findViewById(R.id.id_questionnaireitem_topics);
		mSend = (Button) findViewById(R.id.id_questionnaire_send);
		mOption = (RelativeLayout) findViewById(R.id.id_questionnaire_option);
		mOptionsRlyt[0] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option0);
		mOptionsRlyt[1] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option1);
		mOptionsRlyt[2] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option2);
		mOptionsRlyt[3] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option3);
		mOptionsRlyt[4] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option4);
		mOptionsRlyt[5] = (RelativeLayout) findViewById(R.id.id_questionnaireitem_option5);
		mIcon[0] = (ImageView) findViewById(R.id.id_questionnaireitem_option0_icon);
		mIcon[1] = (ImageView) findViewById(R.id.id_questionnaireitem_option1_icon);
		mIcon[2] = (ImageView) findViewById(R.id.id_questionnaireitem_option2_icon);
		mIcon[3] = (ImageView) findViewById(R.id.id_questionnaireitem_option3_icon);
		mIcon[4] = (ImageView) findViewById(R.id.id_questionnaireitem_option4_icon);
		mIcon[5] = (ImageView) findViewById(R.id.id_questionnaireitem_option5_icon);
		mContent[0] = (TextView) findViewById(R.id.id_questionnaireitem_option0_content);
		mContent[1] = (TextView) findViewById(R.id.id_questionnaireitem_option1_content);
		mContent[2] = (TextView) findViewById(R.id.id_questionnaireitem_option2_content);
		mContent[3] = (TextView) findViewById(R.id.id_questionnaireitem_option3_content);
		mContent[4] = (TextView) findViewById(R.id.id_questionnaireitem_option4_content);
		mContent[5] = (TextView) findViewById(R.id.id_questionnaireitem_option5_content);
		
		for (int i=0; i<6; i++)
		{
			mOptionsRlyt[i].setOnClickListener(this);
		}
		
		mLoading.setOnClickListener(this);
		mSend.setOnClickListener(this);
		mSendResulting = false;
	}
	
	/**
	 * 
	 * @Title: requestAdvice
	 * @Description: 请求意见反馈
	 * @return: void
	 */
	private void requestAdvice()
	{
		mLoading.setLoadPageFail(false);
		mLoading.show();
		RequestDataManager.requestData(this, mContext, Token.TOKEN_ADVICE, 0, 0, "");  
	}
	
	/**
	 * 
	 * @Title: requestAdviceResult
	 * @Description: 请求意见反馈结果
	 * @return: void
	 */
	private void requestAdviceResult()
	{
		ArrayList<AdviceItem> adviceItemss = new ArrayList<AdviceItem>();
		AdviceItem item = null;
		
		for (int i=0; i<mAdvices.size(); i++)
		{
			item = new AdviceItem();
			ArrayList<AdviceOption> optionList = new ArrayList<AdviceOption>();
			
			for (int j=0; j<mAdvices.get(i).getOptionList().size(); j++)
			{
				if (mAdvices.get(i).getOptionList().get(j).getSelected())
				{
					optionList.add(mAdvices.get(i).getOptionList().get(j));
				}
			}
			
			item.setTopicCode(mAdvices.get(i).getTopicCode());
			item.setTopicName(mAdvices.get(i).getTopicName());
			item.setType(mAdvices.get(i).getType());
			item.setOptionList(optionList);
			adviceItemss.add(item);
		}
		
		if(adviceItemss.size() > 0)
		{
			RequestDataManager.requestAdviceResult(this, mContext, Token.TOKEN_ADVICE_RESULT, adviceItemss);  
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_questionnaire_send:
		{
			if (mNumber < mAdvices.size()-1)
			{
				mNumber++;
				setAdvice();
			}
			else
			{
				if (!mSendResulting)  //正在请求问卷调查结果的时候不能再次请求, 每次调查只能请求调查结果一次(请求结果失败时, 可以再次请求)
				{
					mSendResulting = true;
					requestAdviceResult();
				}
			}
			break;
		}
		case R.id.id_questionnaireitem_option0:
		case R.id.id_questionnaireitem_option1:
		case R.id.id_questionnaireitem_option2:
		case R.id.id_questionnaireitem_option3:
		case R.id.id_questionnaireitem_option4:
		case R.id.id_questionnaireitem_option5:
		{
			int index = getCurViewIndex(v.getId());
			boolean selected = mAdvices.get(mNumber).getOptionList().get(index).getSelected();
			
			if (mType == FlagConstant.QUESTIONNAIRE_SINGLE_CHOICE)
			{
				if (!selected)
				{
					mAdvices.get(mNumber).getOptionList().get(index).setSelected(true);
				}
				
				for (int i=0; i<mAdvices.get(mNumber).getOptionList().size(); i++)
				{
					if (i != index)
					{
						mAdvices.get(mNumber).getOptionList().get(i).setSelected(false);
					}
				}
			}
			else if (mType == FlagConstant.QUESTIONNAIRE_MULTI_CHOICE)
			{
				if (selected)
				{
					mAdvices.get(mNumber).getOptionList().get(index).setSelected(false);
				}
				else
				{
					mAdvices.get(mNumber).getOptionList().get(index).setSelected(true);
				}
			}
			
			setOptionSelected();
			break;
		}
		case R.id.id_questionnaire_loading_page:
		{
			if (mLoading.getLoadPageState())
			{
				requestAdvice();
				break;
			}
		}
		default:
			break;
		}
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
			case Token.TOKEN_ADVICE:
			{
				mAdvices.clear();
				ArrayList<AdviceItem> temp = new ArrayList<AdviceItem>();
				temp = (ArrayList<AdviceItem>) RequestDataManager.getData(in);
				
				for (int i=0; i<temp.size(); i++)
				{
					AdviceItem item = temp.get(i);
					if (item.getType().equals("1") || item.getType().equals("2"))  //该应用的问卷调查只支持单选题和多选题, 不支持简答题
					{
						if (item.getOptionList().size() > 0)  //每道题目至少有一个答案选项
						{
							mAdvices.add(item);
						}
					}
				}
				freshAdviceList();
				break;
			}
			case Token.TOKEN_ADVICE_RESULT:
			{
				AdviceResult result = (AdviceResult) RequestDataManager.getAdviceResultData(in);
				
				if (result.getAdviceResult() == 0)
				{
					ShowToast.getShowToast().createToast(mContext,
							                             getResources().getString(R.string.toast_send_success), 
							                             getResources().getString(R.string.toast_thanks));
				    
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run() 
						{
							finish();
						}
					},2500);
				}
				else
				{
					mSendResulting = false;
					ShowToast.getShowToast().createToast(mContext, 
							                             getResources().getString(R.string.toast_send_fail), 
							                             "");
				}
				break;
			}
			default:
				break;
			}
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "===ActivityQuestionnaire==== onSuccessful error + " + e.toString());
		}
	}

	@Override
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token) 
	{
		switch (token) 
		{
		case Token.TOKEN_ADVICE:
		{
			mLoading.setLoadPageFail(true);
			mLoading.requestFocus();
			break;
		}
		case Token.TOKEN_ADVICE_RESULT:
		{
			mSendResulting = false;
			break;
		}	
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: freshAdviceList
	 * @Description: 刷新请求问卷调查内容后的结果
	 * @return: void
	 */
	private void freshAdviceList()
	{
		if (mAdvices.size() > 0)
		{
			mLoading.hide();
			mOption.setVisibility(View.VISIBLE);
			mSend.setVisibility(View.VISIBLE);
			mNumber = 0;
			setAdvice();
		}
		else
		{
			onNetError(-1, "error", null, Token.TOKEN_ADVICE);
		}
	}
	
	/**
	 * 
	 * @Title: setAdvice
	 * @Description: 设置当前问卷调查的内容(包括: 序号 标题 答案选项 选中情况 按钮内容)
	 * @return: void
	 */
	private void setAdvice()
	{
		if (mAdvices.get(mNumber).getOptionList().size() > 0)
		{
			mOptionsRlyt[0].requestFocus();
			setNumber();
			setTopic();
			setOptionContent();
			setOptionSelected();
			setSendText();
		}
	}
	
	/**
	 * 
	 * @Title: setNumber
	 * @Description: 设置当前题目的序号
	 * @return: void
	 */
	private void setNumber()
	{
		int number = mNumber +1;
		mTitle.setText(getResources().getString(R.string.home_questionnaire) + " ( " + number + "/" + mAdvices.size() + " )");
	}
	
	/**
	 * 
	 * @Title: setTopic
	 * @Description: 设置问题的标题
	 * @return: void
	 */
	private void setTopic()
	{
		int number = mNumber + 1;
		mType = Integer.parseInt(mAdvices.get(mNumber).getType());
		
		if (mType == FlagConstant.QUESTIONNAIRE_SINGLE_CHOICE)
		{
			mTopic.setText(number + ". " + mAdvices.get(mNumber).getTopicName());
		}
		else if (mType == FlagConstant.QUESTIONNAIRE_MULTI_CHOICE)
		{
			mTopic.setText(number + ". " + mAdvices.get(mNumber).getTopicName() + " (" + getResources().getString(R.string.questionnaire_choices) + ")");
		}
	}
	
	/**
	 * 
	 * @Title: setOptionContent
	 * @Description: 设置答案选项的内容
	 * @return: void
	 */
	private void setOptionContent()
	{
		for (int i=0; i<6; i++)
		{
			int size = mAdvices.get(mNumber).getOptionList().size();
			
			if (i < size)
			{
				mContent[i].setText(mAdvices.get(mNumber).getOptionList().get(i).getItemName());
				mOptionsRlyt[i].setVisibility(View.VISIBLE);
			}
			else
			{
				mOptionsRlyt[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	/**
	 * 
	 * @Title: setOptionSelected
	 * @Description: 设置答案选项的选中情况
	 * @return: void
	 */
	private void setOptionSelected()
	{
		for (int i=0; i<6; i++)
		{
			int size = mAdvices.get(mNumber).getOptionList().size();
			
			if (i < size)
			{
				boolean selected = mAdvices.get(mNumber).getOptionList().get(i).getSelected();
				
				if (mType == FlagConstant.QUESTIONNAIRE_SINGLE_CHOICE)
				{
					if (selected)
					{
						mIcon[i].setImageResource(R.drawable.questionnaire_choice_f);
					}
					else
					{
						mIcon[i].setImageResource(R.drawable.questionnaire_choice_uf);
					}
				}
				else if (mType == FlagConstant.QUESTIONNAIRE_MULTI_CHOICE)
				{
					if (selected)
					{
						mIcon[i].setImageResource(R.drawable.questionnaire_choice_f);
					}
					else
					{
						mIcon[i].setImageResource(R.drawable.questionnaire_choice_uf);
					}
				}
				mOptionsRlyt[i].setVisibility(View.VISIBLE);
			}
			else
			{
				mOptionsRlyt[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	/**
	 * 
	 * @Title: setSendText
	 * @Description: 设置"下一题/提交"按钮的内容
	 * @return: void
	 */
	private void setSendText()
	{
		int size = mAdvices.size();
		
		if (mNumber < size-1)
		{
			mSend.setText(getResources().getString(R.string.questionnaire_next));
		}
		else if (mNumber == size-1)
		{
			mSend.setText(getResources().getString(R.string.questionnaire_send));
		}
	}
	
	/**
	 * 
	 * @Title: getCurViewIndex
	 * @Description: 获取答案选项聚焦index
	 * @param id
	 * @return
	 * @return: int
	 */
	private int getCurViewIndex(int id)
	{
		for (int i=0; i<mOptionsRlyt.length; i++) 
		{
			if(mOptionsRlyt[i].getId() == id)
			{
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @Title: getSelectedCount
	 * @Description: 获取答案选中个数
	 * @return
	 * @return: int
	 */
	private int getSelectedCount()
	{
		int count = 0;
		int size = mAdvices.get(mNumber).getOptionList().size();
		
		for (int i=0; i<size; i++)
		{
			boolean selected = mAdvices.get(mNumber).getOptionList().get(i).getSelected();
			
			if (selected)
			{
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @Title: isOptionsRlytFocus
	 * @Description: 判断答案选项是否获得焦点
	 * @return
	 * @return: boolean
	 */
	private boolean isOptionsRlytFocus()
	{
		for (int i=0; i<6; i++)
		{
			if (mOptionsRlyt[i].isFocused())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: getOptionsRlytFocusIndex
	 * @Description: 获取答案选项聚焦index
	 * @return
	 * @return: int
	 */
	private int getOptionsRlytFocusIndex()
	{
		for (int i=0; i<6; i++)
		{
			if (mOptionsRlyt[i].isFocused())
			{
				return i;
			}
		}
		return 0;
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
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			nRet = doKeyLeft();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyRight();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			nRet = doKeyBack();
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
		if (isOptionsRlytFocus())
		{
			int size = mAdvices.get(mNumber).getOptionList().size();
			int index = getOptionsRlytFocusIndex(); 
			
			if (size > 6)
			{
				size = 6;
			}
			
			if (size%2 == 0)
			{
				if (index == size-1 || index == size-2)
				{
					if (getSelectedCount() == 0)
					{
						ShowToast.getShowToast().createToast(mContext, 
								                             getResources().getString(R.string.toast_select_answer), 
								                             "");
						return true;
					}
				}
			}
			else if (size%2 == 1)
			{
				if (index == size-1)
				{
					if (getSelectedCount() == 0)
					{
						ShowToast.getShowToast().createToast(mContext, 
								                             getResources().getString(R.string.toast_select_answer), 
								                             "");
						return true;
					}
				}
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
		if (mSend.isFocused())
		{
			return true;
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
		if (isOptionsRlytFocus())
		{
			int size = mAdvices.get(mNumber).getOptionList().size();
			int index = getOptionsRlytFocusIndex();
			
			if (index == size-1)
			{
				return true;
			}
		}
		else if (mSend.isFocused())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title: doKeyBack
	 * @Description: 响应返回键
	 * @return
	 * @return: boolean
	 */
	private boolean doKeyBack()
	{
		createDialogPrompt(/*R.drawable.tips_question, */
                getResources().getString(R.string.dialog_exit_questionnaire), 
                getResources().getString(R.string.dialog_yes), 
                getResources().getString(R.string.dialog_no));
		return true;
	}
	
	/**
	 * 
	 * @Title: createDialogPrompt
	 * @Description: 创建提示对话框
	 * @param resid  资源id(图片)
	 * @param info   提示信息
	 * @param sure   确定
	 * @param cancel 取消
	 * @return: void
	 */
	private void createDialogPrompt(/*int resid, */String info, String sure, String cancel)
	{
		mDialog = new DialogPrompt(mContext, /*R.style.dialog_style, resid,*/ info, sure, cancel);
		mDialog.setOnClickCustomListener(new onClickCustomListener() 
		{
			@Override
			public void OnClick(View v) 
			{
				switch (v.getId()) 
				{
				case R.id.id_dialog_prompt_sure:
				{
					mDialog.dismiss();
					finish();
					break;
				}
				case R.id.id_dialog_prompt_cancel:
				{
					mDialog.dismiss();
					break;
				}	
				default:
					break;
				}
			}
		});
		mDialog.show();
		mDialog.getCancelBtn().requestFocus();
	}
}
