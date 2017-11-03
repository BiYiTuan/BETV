/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: InSendAdvicePacket.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket
 * @Description: 意见反馈结果 下行客户端数据
 * @author: zhaoqy
 * @date: 2014-8-14 下午5:26:35
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket;

import java.nio.ByteBuffer;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.xmlpull.PullParse;
import com.szgvtv.ead.app.taijietemplates.util.Token;

public class InSendAdvicePacket implements InPacket
{
	private int        mToken;     //标识一个Http请求，由调用者传入
	private UICallBack mCallBack;  //界面回调
		
	/**
	 * 从服务器接收到的元素列表数据包的构造函数
	 * @author zhaoqy
	 * @param uiCallback 提供给上层的界面回调
	 * @param token Http请求的标识，由调用者传入
	 */
	public InSendAdvicePacket(UICallBack uiCallback, int token) 
	{
		mCallBack = uiCallback;
		mToken = token;
	}

	/**
	 * Http请求时的响应回调
	 * @author zhaoqy
	 * @param responseBytes 从服务器接收到的数据
	 * @param encoding 从服务器接收到的数据的编码格式，如"UTF-8"
	 * @param cryptographic 是否加密
	 * @param responseFinish 是否响应完成
	 * @param id HttpEngine类中保存的请求的唯一ID
	 * @param out 本次请求上行至服务器的数据包
	 * @see um.market.android.network.packet.InPacket#httpResponse(java.nio.ByteBuffer, java.lang.String, boolean, boolean, int, um.market.android.network.packet.OutPacket)
	 */
	@Override
	public void httpResponse(ByteBuffer responseBytes, String encoding, boolean cryptographic, boolean responseFinish, int id, OutPacket out) 
	{
		return;
	}

	/**
	 * 接收数据时是否使用Cache
	 * @author zhaoqy
	 * @return true代表使用，将会在读取完服务器的数据后再回调onSuccessful，否则读取一次响应一次
	 */
	@Override
	public boolean useCache() 
	{
		return true;
	}

	/**
	 * 界面回调时进入取消Http请求的回调函数
	 * @author zhaoqy
	 * @param out 本次请求上行至服务器的数据包
	 */
	@Override
	public void onCancel(OutPacket out) 
	{
		mCallBack.onCancel(out, mToken);
	}

	/**
	 * 响应服务器且接收数据成功时的行为
	 * @author zhaoqy
	 * @param buffer 存放从服务器得到的数据
	 * @param bufLen 从服务器得到的数据的长度
	 */
	@Override
	public void onSuccessful(ByteBuffer buffer, int bufLen) 
	{
		Object object = parse(buffer, bufLen);
		
		if (object != null)
		{
			mCallBack.onSuccessful(object, mToken);
		}
		else
		{
			mCallBack.onNetError(-1, "error", null, mToken);
		}
	}

	/**
	 * 请求失败或发生网络错误时的回调
	 * @author zhaoqy
	 * @param responseCode 服务器的Http响应代码，如200, 404等
	 * @param errorDesc 错误的描述
	 * @param id HttpEngine类中保存的请求的唯一ID
	 * @param out 本次请求上行至服务器的数据包
	 */
	@Override
	public void onNetError(int responseCode, String errorDesc, int id, OutPacket out) 
	{
		mCallBack.onNetError(responseCode, errorDesc, out, mToken);
	}

	/**
	 * 数据解析
	 * @author zhaoqy
	 * @param responseBytes 服务器返回数据
	 * @param responseBytesLen 错误的描述
	 */
	@Override
	public Object parse(ByteBuffer responseBytes, int responseBytesLen) 
	{
		Object uiObjects = PullParse.parseAdviceResult(responseBytes, Token.TOKEN_ADVICE_RESULT);
		
		return uiObjects;
	}
}
