/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: OutElementListPacket.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket
 * @Description: 本文件定义上行至服务器的元素列表数据包
 * @author: zhaoqy
 * @date: 2014-8-14 下午4:09:28
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket.ElementListData;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.GeneralParam;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.StbInfo;
import com.szgvtv.ead.app.taijietemplates.util.TimeUtil;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import android.content.Context;

public class OutElementListPacket implements OutPacket
{
	private ElementListData mElementLitData; 
	
	/**
	 * 外发元素列表数据的构造函数
	 * @author zhaoqy
	 * @param uiCallback
	 * @param token
	 * @param datas
	 */
	public OutElementListPacket(UICallBack uiCallback, int token, ElementListData data) 
	{
		mElementLitData = data;
	}

	/**
	 * 
	 * @author zhaoqy
	 * @param context  android上下文context
	 * @return 请求URL地址
	 * @throws ProtocolException
	 * @throws IOException
	 */
	@Override
	public URL serviceURL(Context context) throws ProtocolException, IOException 
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.REQUEST_URL_HOST);
		
		switch (mElementLitData.getToken()) 
		{
		case Token.TOKEN_APP_AUTH:           //应用鉴权
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("app_auth.xml");
			}
			else
			{
				sb.append("/");
				sb.append("deviceappauth.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				//add 2014-10-16
				sb.append("&");
				sb.append("firmware=");
				sb.append(StbInfo.getHwVersion(context));
				sb.append("&");
				sb.append("pmid=");
				sb.append(StbInfo.getPmVersion(context));
				sb.append("&");
				sb.append("version=");
				sb.append(StbInfo.getVersion(context));
			}
			break;
		}
		case Token.TOKEN_APP_UPGRADE:        //应用升级
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("app_upgrade.xml");
			}
			else
			{
				sb.append("/");
				sb.append("upgrade.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("version=");
				sb.append(mElementLitData.getArgument().get(0));  //传versionName(String), 而不是versionCode(int)
			}
			break;
		}
		case Token.TOKEN_ADVICE:             //应用意见反馈列表
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("advice.xml");
			}
			else
			{
				sb.append("/");
				sb.append("feedbacktable.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_ADVICE_RESULT:      //应用反馈意见收集
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("advice_result.xml");
			}
			else
			{
				sb.append("/");
				sb.append("useropinion.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_APP_STARTUP:             //应用启动页
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("app_startup.xml");
			}
			else
			{
				sb.append("/");
				sb.append("appstartup.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_APP_RECD:             //应用首页推荐
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("apprcd.xml");
			}
			else
			{
				sb.append("/");
				sb.append("apprcd.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_SPECIAL_LIST:        //专题列表
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("special_list.xml");
			}
			else
			{
				sb.append("/");
				sb.append("album.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_SPECIAL_VIDEO:       //专题下面资源   
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("special_video.xml");
			}
			else
			{
				sb.append("/");
				sb.append("albumvideo.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("size=");
				sb.append(mElementLitData.getSize());
				sb.append("&");
				sb.append("page=");
				sb.append(mElementLitData.getPage());
				sb.append("&");
				sb.append("sorttype=");
				sb.append(1);  //按时间
				sb.append("&");
				sb.append("albumcode=");
				sb.append(mElementLitData.getArgument().get(0));
			}
			break;
		}
		case Token.TOKEN_TV_LIST:             //直播频道列表  
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("tv_list.xml");
			}
			else
			{
				sb.append("/");
				sb.append("tvlist.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("hasgroup=");
				sb.append(2);
			}
			break;
		}
		case Token.TOKEN_TV_NOTICE:           //直播节目预告 
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("tv_notice.xml");
			}
			else
			{
				sb.append("/");
				sb.append("tvnotice.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("tvcode=");
				sb.append(mElementLitData.getArgument().get(0));
				sb.append("&");
				sb.append("date=");
				sb.append("");
				sb.append("&");
				sb.append("zone=");
				sb.append(TimeUtil.getTimeZone());
				sb.append("&");
				sb.append("strategy=");
				sb.append(1);
			}
			break;
		}
		case Token.TOKEN_TV_REPLAY:           //直播精彩回放 
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("tv_replay.xml");
			}
			else
			{
				sb.append("/");
				sb.append("tvreplay.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("tvcode=");
				sb.append(mElementLitData.getArgument().get(0));
			}
			break;
		}
		case Token.TOKEN_VIDEO_APP_RECOMMEND: //点播应用推荐
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("video_app_recommend.xml");
			}
			else
			{
				sb.append("/");
				sb.append("indexrecommend.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_VIDEO_CLASSIFY:      //点播分类列表
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("video_classify.xml");
			}
			else
			{
				sb.append("/");
				sb.append("videoclassify.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("size=");
				sb.append(mElementLitData.getSize());
				sb.append("&");
				sb.append("page=");
				sb.append(mElementLitData.getPage());
				sb.append("&");
				sb.append("pclassifycode=");
				sb.append(mElementLitData.getArgument().get(0));
			}
			break;
		}
		case Token.TOKEN_VIDEO_LIST:          //点播分类下资源
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("video_list.xml");  
			}
			else
			{
				sb.append("/");
				sb.append("videolist.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("size=");
				sb.append(mElementLitData.getSize());
				sb.append("&");
				sb.append("page=");
				sb.append(mElementLitData.getPage());
				sb.append("&");
				sb.append("sorttype=");
				sb.append(1);
				sb.append("&");
				sb.append("classifycode=");
				sb.append(mElementLitData.getArgument().get(0));
			}
			break;
		}
		case Token.TOKEN_HOT_WORD:            //点播热词
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("hot_word.xml");
			}
			else
			{
				sb.append("/");
				sb.append("hotword.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
			}
			break;
		}
		case Token.TOKEN_SEARCH_VIDEO:        //点播资源搜索  
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("search_video.xml");
			}
			else
			{
				sb.append("/");
				sb.append("searchvideo.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("size=");
				sb.append(mElementLitData.getSize());
				sb.append("&");
				sb.append("page=");
				sb.append(mElementLitData.getPage());
				sb.append("&");
				sb.append("sorttype=");
				sb.append(1);
				sb.append("&");
				sb.append("keyword=");
				sb.append(URLEncoder.encode(mElementLitData.getArgument().get(0), "UTF-8"));
			}
			break;
		}
		case Token.TOKEN_VIDEO:               //点播资源详情
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("video.xml");
			}
			else
			{
				sb.append("/");
				sb.append("video.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("videocode=");
				sb.append(mElementLitData.getArgument().get(0));
			}
			break;
		}
		case Token.TOKEN_VIDEO_RECOMMEND:     //点播推荐
		{
			if(Constant.URL_LOCAL_SERVICE)
			{
				sb.append("video_recommend.xml");
			}
			else
			{
				sb.append("/");
				sb.append("videorecommend.action?");
				sb.append(GeneralParam.generateGeneralParam(context));
				sb.append("&");
				sb.append("size=");
				sb.append(mElementLitData.getSize());
				sb.append("&");
				sb.append("page=");
				sb.append(mElementLitData.getPage());
				sb.append("&");
				sb.append("videocode=");
				sb.append(mElementLitData.getArgument().get(0));
				sb.append("&");
				sb.append("dimension=");
				sb.append(3);
				sb.append("&");
				sb.append("keyword=");
				sb.append("");
			}
			break;
		}
		default:
			break;
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
		Method method = Method.GET;
		
		switch (mElementLitData.getToken()) 
		{
		case Token.TOKEN_APP_AUTH:            //应用鉴权
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_APP_UPGRADE:         //应用升级
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_ADVICE:              //应用意见反馈列表
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_ADVICE_RESULT:       //应用反馈意见收集
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_APP_STARTUP:         //应用启动页
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_APP_RECD:            //应用首页推荐
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_SPECIAL_LIST:        //专题列表
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_SPECIAL_VIDEO:       //专题下面资源    
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_TV_LIST:             //直播频道列表  
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_TV_NOTICE:           //直播节目预告 
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_TV_REPLAY:           //直播精彩回放 
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_VIDEO_APP_RECOMMEND: //点播应用推荐
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_VIDEO_CLASSIFY:      //点播分类列表
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_VIDEO_LIST:          //点播分类下资源
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_HOT_WORD:            //点播热词
		{
			method = Method.GET;
			break;
		}
		case Token.TOKEN_SEARCH_VIDEO:        //点播资源搜索  
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_VIDEO:               //点播资源详情
		{
			method = Method.POST;
			break;
		}
		case Token.TOKEN_VIDEO_RECOMMEND:     //点播推荐
		{
			method = Method.POST;
			break;
		}
		default:
			break;
		}
		return method;
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
