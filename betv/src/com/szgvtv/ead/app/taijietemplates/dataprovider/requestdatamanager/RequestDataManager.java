/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: RequestDataManager.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager
 * @Description: 请求数据管理
 * @author: zhaoqy
 * @date: 2014-8-7 下午9:28:45
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.requestdatamanager;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AuthResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HomeRecommendItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HotwordItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.SpecialItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.StartUpItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.UpgradeItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.ClassifyItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket.ElementListData;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.HttpEngineManager;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.UICallBack;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket.InElementListPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket.InSendAdvicePacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutElementListPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutSendAdvicePacket;
import com.szgvtv.ead.app.taijietemplates.util.Token;
import android.content.Context;

public class RequestDataManager 
{
	/**
	 * 
	 * @Title: requestData
	 * @Description:      请求数据
	 * @param uiCallback: 回调对象
	 * @param context:    上下文切换
	 * @param token:      列表标识ID
	 * @param size:       每页个数
	 * @param page:       当前页
	 * @param argument:   不定参数
	 * @return: void
	 */
	public static void requestData(UICallBack uiCallback, Context context, int token, int size, int page, Object... argument)
	{
		ElementListData data = null;
		data = new ElementListData(token, size, page, argument);
		
		OutElementListPacket mOutPacket = new OutElementListPacket(uiCallback, token, data);
		InElementListPacket mInPacket = new InElementListPacket(uiCallback, token, data);
		HttpEngineManager.createHttpEngine(mOutPacket, mInPacket, context);
	}
	
	/**
	 * 
	 * @Title: requestAdviceResult
	 * @Description:      请求数据(意见反馈结果)
	 * @param uiCallback: 回调对象
	 * @param context:    上下文切换
	 * @param token:      列表标识ID
	 * @param datas:      意见反馈内容
	 * @return: void
	 */
	public static void requestAdviceResult(UICallBack uiCallback, Context context, int token, ArrayList<AdviceItem> datas)
	{
		OutSendAdvicePacket mOutPacket = new OutSendAdvicePacket(uiCallback, Token.TOKEN_ADVICE_RESULT, datas);
		InSendAdvicePacket mInPacket = new InSendAdvicePacket(uiCallback, Token.TOKEN_ADVICE_RESULT);
		HttpEngineManager.createHttpEngine(mOutPacket, mInPacket, context);
	}
	
	/**
	 * 
	 * @Title: getAdviceResultData
	 * @Description: 获取数据(意见反馈结果)
	 * @param in:    将服务器返回的数据解析之后的数据
	 * @return
	 * @return: AdviceResult
	 */
	public static AdviceResult getAdviceResultData(Object in)
	{
		AdviceResult adviceResult = (AdviceResult)in;
		return adviceResult;
	}
	
	/**
	 * 
	 * @Title: getData
	 * @Description: 获取数据
	 * @param in:    将服务器返回的数据解析之后的数据
	 * @return
	 * @return: Object
	 */
	@SuppressWarnings("unchecked")
	public static Object getData(Object in)
	{
		Object out = null;
		ElementListData data = (ElementListData) in;
		
		switch (data.getToken()) 
		{
		case Token.TOKEN_APP_AUTH:             //应用鉴权
		{
			for (Object obj: data.getList()) 
			{
				out = (AuthResult)obj;
			}
			break;
		}
		case Token.TOKEN_APP_UPGRADE:          //应用升级
		{
			out = new ArrayList<UpgradeItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					UpgradeItem upgradeItem = (UpgradeItem)item;
					((ArrayList<UpgradeItem>)out).add(upgradeItem);
				}
			}	
			break;
		}
		case Token.TOKEN_ADVICE:               //意见反馈
		{
			out = new ArrayList<AdviceItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					AdviceItem adviceItem = (AdviceItem)item;
					((ArrayList<AdviceItem>)out).add(adviceItem);
				}
			}	
			break;
		}
		case Token.TOKEN_ADVICE_RESULT:        //意见反馈结果
		{
			for (Object obj: data.getList()) 
			{
				out = (AdviceResult)obj;
			}
			break;
		}
		case Token.TOKEN_APP_STARTUP:          //应用启动页
		{
			out = new ArrayList<StartUpItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					StartUpItem startUpItem = (StartUpItem)item;
					((ArrayList<StartUpItem>)out).add(startUpItem);
				}
			}	
			break;
		}
		case Token.TOKEN_APP_RECD:             //应用首页推荐
		{
			out = new ArrayList<HomeRecommendItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					HomeRecommendItem homeRecommendItem = (HomeRecommendItem)item;
					((ArrayList<HomeRecommendItem>)out).add(homeRecommendItem);
				}
			}	
			break;
		}
		case Token.TOKEN_SPECIAL_LIST:         //专题列表
		{
			out = new ArrayList<SpecialItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					SpecialItem specialItem = (SpecialItem)item;
					((ArrayList<SpecialItem>)out).add(specialItem);
				}
			}	
			break;
		}
		case Token.TOKEN_SPECIAL_VIDEO:        //专题下面资源 
		{
			out = new ArrayList<VideoItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					VideoItem videoItem = (VideoItem)item;
					((ArrayList<VideoItem>)out).add(videoItem);
				}
			}	
			break;
		}
		case Token.TOKEN_TV_LIST:              //直播频道列表  
		{
			out = new ArrayList<LiveChannelItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					LiveChannelItem liveChannelItem = (LiveChannelItem)item;
					((ArrayList<LiveChannelItem>)out).add(liveChannelItem);
				}
			}	
			break;
		}
		case Token.TOKEN_TV_NOTICE:            //直播节目预告 
		{
			out = new ArrayList<LivePreviewItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					LivePreviewItem livePreviewItem = (LivePreviewItem)item;
					((ArrayList<LivePreviewItem>)out).add(livePreviewItem);
				}
			}	
			break;
		}
		case Token.TOKEN_TV_REPLAY:            //直播精彩回放 
		{
			out = new ArrayList<LivePlaybackItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					LivePlaybackItem livePlaybackItem = (LivePlaybackItem)item;
					((ArrayList<LivePlaybackItem>)out).add(livePlaybackItem);
				}
			}	
			break;
		}
		case Token.TOKEN_VIDEO_APP_RECOMMEND:  //点播应用推荐
		{
			out = new ArrayList<VideoItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					VideoItem videoItem = (VideoItem)item;
					((ArrayList<VideoItem>)out).add(videoItem);
				}
			}	
			break;
		}
		case Token.TOKEN_VIDEO_CLASSIFY:       //点播分类列表
		{
			out = new ArrayList<ClassifyItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					ClassifyItem classifyItem = (ClassifyItem)item;
					((ArrayList<ClassifyItem>)out).add(classifyItem);
				}
			}	
			break;
		}
		case Token.TOKEN_VIDEO_LIST:           //点播分类下资源
		{
			out = new ArrayList<VideoItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					VideoItem videoItem = (VideoItem)item;
					((ArrayList<VideoItem>)out).add(videoItem);
				}
			}	
			break;
		}
		case Token.TOKEN_HOT_WORD:             //点播热词
		{
			out = new ArrayList<HotwordItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					HotwordItem hotwordItem = (HotwordItem)item;
					((ArrayList<HotwordItem>)out).add(hotwordItem);
				}
			}	
			break;
		}
		case Token.TOKEN_SEARCH_VIDEO:         //点播资源搜索  
		{
			out = new ArrayList<VideoItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					VideoItem videoItem = (VideoItem)item;
					((ArrayList<VideoItem>)out).add(videoItem);
				}
			}	
			break;
		}
		case Token.TOKEN_VIDEO:                //点播资源详情
		{
			for (Object obj: data.getList()) 
			{
				out = (VideoItem)obj;
			}
			break;
		}
		case Token.TOKEN_VIDEO_RECOMMEND:      //点播推荐
		{
			out = new ArrayList<VideoItem>();
			
			if(data != null)
			{
				for (Object item : data.getList()) 
				{
					VideoItem videoItem = (VideoItem)item;
					((ArrayList<VideoItem>)out).add(videoItem);
				}
			}	
			break;
		}
		default:
			break;
		}
		
		return out;
	}
	
	/**
	 * 
	 * @Title: getChannelCode
	 * @Description: 获取频道编码
	 * @param in
	 * @return
	 * @return: String
	 */
	public static String getChannelCode(Object in)
	{
		String code = "";
		ElementListData data = (ElementListData) in;
		
		switch (data.getToken()) 
		{
		case Token.TOKEN_TV_NOTICE:
		{
			code = data.getArgument().get(0);
			break;
		}
		default:
			break;
		}
		
		return code;
	}
	
	/**
	 * 
	 * @Title: getTotal
	 * @Description: 获取数据个数
	 * @param in
	 * @return
	 * @return: int
	 */
	public static int getTotal(Object in)
	{
		int count = 0;
		ElementListData data = (ElementListData) in;
		
		switch (data.getToken()) 
		{
		case Token.TOKEN_APP_AUTH:            //应用鉴权
		case Token.TOKEN_APP_UPGRADE:         //应用升级
		case Token.TOKEN_ADVICE:              //意见反馈
		case Token.TOKEN_ADVICE_RESULT:       //意见反馈结果
		case Token.TOKEN_SPECIAL_LIST:        //专题列表
		case Token.TOKEN_SPECIAL_VIDEO:       //专题下面资源   
		case Token.TOKEN_TV_LIST:             //直播频道列表  
		case Token.TOKEN_TV_NOTICE:           //直播节目预告
		case Token.TOKEN_TV_REPLAY:           //直播精彩回放 
		case Token.TOKEN_VIDEO:               //点播资源详情
		{
			break;
		}
		case Token.TOKEN_VIDEO_APP_RECOMMEND: //点播应用推荐
		case Token.TOKEN_VIDEO_CLASSIFY:      //点播分类列表
		case Token.TOKEN_VIDEO_LIST:          //点播分类下资源
		case Token.TOKEN_HOT_WORD:            //点播热词
		case Token.TOKEN_SEARCH_VIDEO:        //点播资源搜索  
		case Token.TOKEN_VIDEO_RECOMMEND:     //点播推荐
		{
			count = data.getTotal();
			break;
		}
		default:
			break;
		}
		
		return count;
	}
}
