/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: InElementListPacket.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket
 * @Description: 下行客户端数据
 * @author: zhaoqy
 * @date: 2014-8-14 下午5:21:07
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket;

import java.nio.ByteBuffer;
import com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket.ElementListData;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.xmlpull.PullParse;
import com.szgvtv.ead.app.taijietemplates.util.Token;

public class InElementListPacket implements InPacket
{
	private int             mToken;           //标识一个Http请求，由调用者传入
	private UICallBack      mCallBack;        //界面回调
	private ElementListData mElementLitData; 
	
	/**
	 * 从服务器接收到的元素列表数据包的构造函数
	 * @author zhaoqy
	 * @param uiCallback 提供给上层的界面回调
	 * @param token Http请求的标识，由调用者传入
	 */
	public InElementListPacket(UICallBack uiCallback, int token, ElementListData data) 
	{
		mCallBack = uiCallback;
		mToken = token;
		mElementLitData = data;
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
		ElementListData uiObjects = null;
		
		switch (mElementLitData.getToken()) 
		{
		case Token.TOKEN_APP_AUTH:            //应用鉴权
		{
			uiObjects = PullParse.parseAppAuth(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_APP_UPGRADE:         //应用升级
		{
			uiObjects = PullParse.parseAppUpgrade(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_ADVICE:              //应用意见反馈列表
		{
			uiObjects = PullParse.parseAdvice(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_ADVICE_RESULT:       //应用反馈意见收集
		{
			break;
		}
		case Token.TOKEN_APP_STARTUP:         //应用启动页
		{
			uiObjects = PullParse.parseAppStartUp(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_APP_RECD:            //应用首页推荐
		{
			uiObjects = PullParse.parseHomeRecommend(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_SPECIAL_LIST:        //专题列表
		{
			uiObjects = PullParse.parseSpecialList(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_SPECIAL_VIDEO:       //专题下面资源    
		{
			uiObjects = PullParse.parseSpecialVideo(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_TV_LIST:             //直播频道列表  
		{
			uiObjects = PullParse.parseTVList(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_TV_NOTICE:           //直播节目预告 
		{
			//uiObjects = PullParse.parseTVNotice(responseBytes, mElementLitData.getToken());
			//为了传入elementListData.getArgument().get(0)这个参数
			uiObjects = PullParse.parseTVNotice(responseBytes, mElementLitData);
			break;
		}
		case Token.TOKEN_TV_REPLAY:           //直播精彩回放 
		{
			uiObjects = PullParse.parseTVReplay(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_VIDEO_APP_RECOMMEND: //点播应用推荐
		{
			uiObjects = PullParse.parseVideoAppRecommend(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_VIDEO_CLASSIFY:      //点播分类列表
		{
			uiObjects = PullParse.parseVideoClassify(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_VIDEO_LIST:          //点播分类下资源
		{
			uiObjects = PullParse.parseVideoList(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_HOT_WORD:            //点播热词
		{
			uiObjects = PullParse.parseHotWord(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_SEARCH_VIDEO:        //点播资源搜索  
		{
			uiObjects = PullParse.parseVideoList(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_VIDEO:               //点播资源详情
		{
			uiObjects = PullParse.parseVideo(responseBytes, mElementLitData.getToken());
			break;
		}
		case Token.TOKEN_VIDEO_RECOMMEND:     //点播推荐
		{
			uiObjects = PullParse.parseVideoRecommend(responseBytes, mElementLitData.getToken());
			break;
		}
		default:
			break;
		}
		return uiObjects;
	}	
}
