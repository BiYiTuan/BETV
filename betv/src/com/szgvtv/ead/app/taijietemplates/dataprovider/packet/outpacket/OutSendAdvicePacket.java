/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: OutSendAdvicePacket.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket
 * @Description: 本文件定义上行至服务器的元素列表数据包(意见反馈结果)
 * @author: zhaoqy
 * @date: 2014-8-14 下午4:08:33
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.GeneralParam;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import android.content.Context;

public class OutSendAdvicePacket implements OutPacket
{
	private ArrayList<AdviceItem> mDatas; 
	
	/**
	 * 应用升级更新的上行数据包类
	 * @author zhaoqy
	 * @param uiCallback 界面回调
	 * @param token 调用者传入的用于标识本次Http请求的标识符
	 * @param datas 调用者传入的程序更新升级数据信息
	 */
	public OutSendAdvicePacket(UICallBack uiCallback, int token, ArrayList<AdviceItem> datas) 
	{
		mDatas = datas;
	}

	/**
	 * 返回应用升级更新的服务器请求地址
	 * @author zhaoqy
	 * @param context android上下文环境
	 * @return 添加了通传参数的应用升级更新包的请求URL地址
	 * @throws ProtocolException
	 * @throws IOException
	 * @see um.market.android.network.packet.OutPacket#serviceURL(android.content.Context)
	 */
	@Override
	public URL serviceURL(Context context) throws ProtocolException, IOException 
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.REQUEST_URL_HOST);
		
		if(Constant.URL_LOCAL_SERVICE)
		{
			sb.append("advice_result.xml");
		}
		else
		{
			try 
			{
				sb.append("/");
				sb.append("useropinion.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("selected=");
				 
		        JSONArray array = new JSONArray();  
		        StringBuffer itemcode = null;
		        for (int i = 0; i < mDatas.size(); i++) 
		        { 
		            JSONObject person = new JSONObject();  
		            AdviceItem p = mDatas.get(i);
		            itemcode = new StringBuffer();
		            
		            for (int j=0; j<p.getOptionList().size(); j++) 
		            {
		            	itemcode.append(p.getOptionList().get(j).getItemCode());
		            	if(j != (p.getOptionList().size()-1))
		            	{
		            		itemcode.append(",");
		            	}	
					}
		            
		            person.put("topiccode", p.getTopicCode()); 
		            person.put("itemcode", itemcode.toString()); 
		            array.put(person); 
		        } 

				sb.append(array.toString());
			} 
			catch (Exception e) 
			{
			}
		}
		
		Logcat.i(FlagConstant.TAG, " URL: " + sb.toString());
		return new URL(sb.toString());
	}

	/**
	 * 设置外发包请求服务器时所用的方法
	 * @author zhaoqy
	 * @return 外发包向服务器的请求方法
	 */
	@Override
	public Method requestMethod() 
	{
		return Method.POST;
	}

	/**
	 * 设置外发包请求服务器的连接超时，数据读取超时
	 * @author zhaoqy
	 * @return 请求超时的时间
	 */
	@Override
	public int getTimeout() 
	{
		return Constant.NETWORK_TIMEOUT;
	}

	/**
	 * 设置请求Http头信息
	 * @author zhaoqy
	 * @param httpConn Http请求连接
	 * @throws SocketTimeoutException
	 * @throws IOException
	 */
	@Override
	public void setRequestProperty(HttpURLConnection httpConn) throws SocketTimeoutException, IOException 
	{
		return;
	}
	
	@Override
	public boolean fillData(OutputStream output, Context context) throws IOException 
	{
		return true;
	}

	@Override
	public String generateGeneralParam(Context context) 
	{
		return null;
	}
}
