/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PullParse.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.xmlpull
 * @Description: pull解析器  注意utf-8有两种格式带bom和不带bom 现在解析是不带bom  注意属性引号必须是英文引号
 * @author: zhaoqy
 * @date: 2014-8-15 下午4:38:11
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.xmlpull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceOption;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AdviceResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.AuthResult;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DictionaryItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DictionaryValueItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HomeRecommendItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.HotwordItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePlaybackItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.SpecialItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.StartUpItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.UpgradeItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.VideoItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.ClassifyItem;
import com.szgvtv.ead.app.taijietemplates.dataprovider.datapacket.ElementListData;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;

public class PullParse 
{
	/**
	 * 
	 * @Title: parseAppAuth
	 * @Description: 解析应用鉴权结果
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseAppAuth(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		AuthResult authItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("auth".equalsIgnoreCase(parser.getName()))
            		{
            			authItem = new AuthResult();
            		}
            		else if (authItem != null)
            		{
            			if ("result".equalsIgnoreCase(parser.getName()))
                		{
                			authItem.setAuthResult(new Integer(parser.nextText()));
                		}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("auth".equalsIgnoreCase(parser.getName()) && authItem != null) 
            		{
						emData.getList().add(authItem);
						authItem = null;
					}
					break;
            	}
            	default:
            		break;
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseAppAuth " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseAppUpgrade
	 * @Description: 解析应用升级
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseAppUpgrade(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		UpgradeItem upgradeItem = null;
		UpgradeItem tempItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("result".equalsIgnoreCase(parser.getName()))
            		{
            			tempItem = new UpgradeItem();
            			tempItem.setIsupgrade(new Integer(parser.getAttributeValue(null, "isupgrade")));
            		}
            		else if ("package".equalsIgnoreCase(parser.getName()))
            		{
            			upgradeItem = new UpgradeItem();
            			upgradeItem.setIsupgrade(tempItem.getIsupgrade());
            		}
            		else if (upgradeItem != null)
            		{
            			if ("version".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setVersion(parser.nextText());
                		}
            			else if ("md5".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setMD5(parser.nextText());
                		}
            			else if ("url".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setUrl(parser.nextText());
                		}
            			else if ("message".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setMessage(parser.nextText());
                		}
            			else if ("isforce".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setIsforce(new Integer(parser.nextText()));
                		}
            			else if ("upgradeway".equalsIgnoreCase(parser.getName()))
                		{
            				upgradeItem.setUpgradeway(new Integer(parser.nextText()));
                		}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("package".equalsIgnoreCase(parser.getName()) && upgradeItem != null) 
            		{
						emData.getList().add(upgradeItem);
						upgradeItem = null;
					}
					break;
            	}
            	default:
            		break;
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseAppUpgrade " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseAdvice
	 * @Description: 解析意见反馈表单
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseAdvice(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		AdviceItem adviceItem = null;
		AdviceOption adviceOption = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("result".equalsIgnoreCase(parser.getName()))
            		{
            		}
            		else if ("topic".equalsIgnoreCase(parser.getName()))
            		{
            			adviceItem = new AdviceItem();
            		}
            		else if (adviceItem != null)
            		{
            			if (adviceOption != null)
            			{
            				if ("itemcode".equalsIgnoreCase(parser.getName()))
                    		{
            					adviceOption.setItemCode(parser.nextText());
                    		}
                			else if ("itemname".equalsIgnoreCase(parser.getName()))
                    		{
                				adviceOption.setItemName(parser.nextText());
                    		}
            			}
            			else
            			{
            				if ("topiccode".equalsIgnoreCase(parser.getName()))
                    		{
            					adviceItem.setTopicCode(parser.nextText());
                    		}
                			else if ("topicname".equalsIgnoreCase(parser.getName()))
                    		{
                				adviceItem.setTopicName(parser.nextText());
                    		}
                			else if ("type".equalsIgnoreCase(parser.getName()))
                    		{
                				adviceItem.setType(parser.nextText());
                    		}
                			else if ("item".equalsIgnoreCase(parser.getName()))
                    		{
                				adviceOption = new AdviceOption();
                    		}
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("topic".equalsIgnoreCase(parser.getName()) && adviceItem != null) 
            		{
						emData.getList().add(adviceItem);
						adviceItem = null;
					}
            		else if ("item".equalsIgnoreCase(parser.getName()) && adviceItem != null && adviceOption != null) 
            		{
            			adviceItem.getOptionList().add(adviceOption);
            			adviceOption = null;
					}
					break;
            	}
            	default:
            		break;
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseAdvice " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseAdviceResult
	 * @Description: 解析意见反馈结果
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static AdviceResult parseAdviceResult(ByteBuffer responseBytes, int token)
	{
		AdviceResult adviceResult = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("evaluate".equalsIgnoreCase(parser.getName()))
            		{
            			adviceResult = new AdviceResult();
            		}
            		else if (adviceResult != null)
            		{
            			if ("result".equalsIgnoreCase(parser.getName()))
                		{
            				adviceResult.setAdviceResult(new Integer(parser.nextText()));
                		}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("evaluate".equalsIgnoreCase(parser.getName()) && adviceResult != null) 
            		{
						//emData.getList().add(adviceResult);
						//adviceResult = null;
					}
					break;
            	}
            	default:
            		break;
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseAdviceResult " + e.toString());
		}
		return adviceResult;
	}
	
	/**
	 * 
	 * @Title: parseAppStartUp
	 * @Description: 解析应用启动页
	 * @param responseBytes 服务器数据
	 * @param token 列表id
	 * @return
	 * @return: ElementListData
	 */
	public static ElementListData parseAppStartUp(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		StartUpItem item = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("item".equalsIgnoreCase(parser.getName()))
					{
						item = new StartUpItem();
					}
					else if(item != null)
					{
						if("url".equalsIgnoreCase(parser.getName()))
						{
							item.setUrl(parser.nextText());  
		                }
						else if("type".equalsIgnoreCase(parser.getName()))
						{
							item.setType(parser.nextText());  
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("item".equalsIgnoreCase(parser.getName()) && item != null) 
					{
						emData.getList().add(item);
						item = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseAppStartUp " + e.toString());
		}   
		
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseHomeRecommend
	 * @Description: 解析首页推荐
	 * @param responseBytes 服务器数据
	 * @param token 列表id
	 * @return
	 * @return: ElementListData
	 */
	public static ElementListData parseHomeRecommend(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		HomeRecommendItem item = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("item".equalsIgnoreCase(parser.getName()))
					{
						item = new HomeRecommendItem();
					}
					else if(item != null)
					{
						if("name".equalsIgnoreCase(parser.getName()))
						{
							item.setName(parser.nextText());  
		                }
						else if("code".equalsIgnoreCase(parser.getName()))
						{
							item.setCode(parser.nextText());  
		                }
						else if("type".equalsIgnoreCase(parser.getName()))
						{
							item.setType(parser.nextText());  
		                }
						else if("image".equalsIgnoreCase(parser.getName()))
						{
							item.setImage(parser.nextText());  
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("item".equalsIgnoreCase(parser.getName()) && item != null) 
					{
						emData.getList().add(item);
						item = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseHomeRecommend " + e.toString());
		}   
		
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseSpecialList
	 * @Description: 解析专题列表
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseSpecialList(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		SpecialItem item = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("album".equalsIgnoreCase(parser.getName()))
					{
						item = new SpecialItem();
					}
					else if(item != null)
					{
						if("albumid".equalsIgnoreCase(parser.getName()))
						{
							item.setAlbumId(parser.nextText());  
		                }
						else if("albumcode".equalsIgnoreCase(parser.getName()))
						{
							item.setAlbumCode(parser.nextText());  
		                }
						else if("name".equalsIgnoreCase(parser.getName()))
						{
		                	item.setName(parser.nextText());  
		                }
						else if("image".equalsIgnoreCase(parser.getName()))
						{
		                	item.setImageUrl(parser.nextText());  
		                }
						else if("summary".equalsIgnoreCase(parser.getName()))
						{
		                	item.setSummary(parser.nextText());  
		                }	
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("album".equalsIgnoreCase(parser.getName()) && item != null) 
					{
						emData.getList().add(item);
						item = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseSpecialList " + e.toString());
		}   
		
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseSpecialVideo
	 * @Description: 解析专题下面资源
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseSpecialVideo(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		VideoItem vedioItem = null;
		DramaItem dramaItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("resources".equalsIgnoreCase(parser.getName()))
            		{
            			emData.setPage(new Integer(parser.getAttributeValue(null, "currentpage")));
						emData.setSize(new Integer(parser.getAttributeValue(null, "num")));
						emData.setTotal(new Integer(parser.getAttributeValue(null, "total")));
					}
            		else if ("video".equalsIgnoreCase(parser.getName()))
            		{
            			vedioItem = new VideoItem();
            		}
            		else if (vedioItem != null)
            		{
            			if (dramaItem != null)
            			{
            				if("dramaname".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem.setDramaName(parser.nextText());  
			                }
            				else if("dramacode".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaCode(parser.nextText());  
			                }
			                else if("number".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setNumber(parser.nextText());  
			                }
			                else if("size".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setSize(parser.nextText());  
			                }
			                else if("time".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaTime(parser.nextText());  
			                }
			                else if("rate".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setRate(parser.nextText());  
			                }
			                else if("format".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setFormat(parser.nextText());  
			                }
			                else if("url".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setUrl(parser.nextText());  
			                }
			                else if("screenshots".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setScreenshots(parser.nextText());  
			                }
            			}
            			else
            			{
            				if("videocode".equalsIgnoreCase(parser.getName()))
            				{
            					vedioItem.setVideoCode(parser.nextText()); 
			                }
            				else if("name".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setName(parser.nextText());  
			                }
            				else if("area".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setArea(parser.nextText());  
			                }
            				else if("type".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setType(parser.nextText());  
			                }
            				else if("director".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setDirector(parser.nextText());  
			                }
            				else if("actor".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setActor(parser.nextText());  
			                }
            				else if("time".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTime(parser.nextText());  
			                }
            				else if("summary".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSummary(parser.nextText());  
			                }
            				else if("smallpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSmallPic(parser.nextText());  
			                }
            				else if("bigpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setBigPic(parser.nextText());
			                }
            				else if("score".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setScore(parser.nextText());  
			                }
            				else if("hot".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setHot(parser.nextText());  
			                }
            				else if("totaldramas".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTotalDramas(parser.nextText());  
			                }
            				else if("ratings".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setRatings(parser.nextText());  
			                }
            				else if("vodtype".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setVodtype(parser.nextText());  
			                }
            				else if("drama".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem = new DramaItem();
			                }
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("video".equalsIgnoreCase(parser.getName()) && vedioItem != null) 
            		{
						emData.getList().add(vedioItem);
						vedioItem = null;
					}
            		else if("drama".equalsIgnoreCase(parser.getName()) && vedioItem!=null && dramaItem != null)
            		{
						vedioItem.getDramaList().add(dramaItem);
						dramaItem = null;
					}
            		break;
            	}
            	default:
            		break;	
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseSpecialVideo " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseTVList
	 * @Description: 解析直播频道列表  
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseTVList(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		LiveChannelItem liveItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("tv".equalsIgnoreCase(parser.getName()))
					{
						liveItem = new LiveChannelItem();
					}
					else if(liveItem != null)
					{
						if("tvid".equalsIgnoreCase(parser.getName()))
						{
							liveItem.setTvId(parser.nextText());
						}
						else if("resid".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setResId(parser.nextText());  
		                	
		                }
						else if("tvcode".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setTvCode(parser.nextText());
		                	
		                }
						else if("tvname".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setTvName(parser.nextText());
		                }
						else if("tvurl".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setTvUrl(parser.nextText());  
		                }
						else if("tvlogo".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setTvLogo(parser.nextText());
		                }
						else if("isreplay".equalsIgnoreCase(parser.getName()))
						{
		                	liveItem.setIsReplay(new Integer(parser.nextText()));
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("tv".equalsIgnoreCase(parser.getName()) && liveItem != null) 
					{
						emData.getList().add(liveItem);
						liveItem = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseTVList " + e.toString());
		}  
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseTVNotice
	 * @Description: 解析直播节目预告
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseTVNotice(ByteBuffer responseBytes, /*int token*/ElementListData elementListData)
	{
		//ElementListData emData = new ElementListData(token, 0, 0, "");
		ElementListData emData = new ElementListData(elementListData.getToken(), 0, 0, elementListData.getArgument().get(0));
		LivePreviewItem programItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("tvnotice".equalsIgnoreCase(parser.getName()))
					{
						programItem = new LivePreviewItem();
					}
					else if(programItem != null)
					{
						if("name".equalsIgnoreCase(parser.getName()))
						{
							programItem.setName(parser.nextText());
		                }
						else if("date".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setDate(parser.nextText());  
		                }
						else if("time".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setTime(parser.nextText());  
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("tvnotice".equalsIgnoreCase(parser.getName()) && programItem != null) 
					{
						emData.getList().add(programItem);
						programItem = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseTVNotice " + e.toString());
		}  
		
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseTVReplay
	 * @Description: 解析直播精彩回放 
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseTVReplay(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		LivePlaybackItem programItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("tvreplay".equalsIgnoreCase(parser.getName()))
					{
						programItem = new LivePlaybackItem();
					}
					else if(programItem != null)
					{
						if("name".equalsIgnoreCase(parser.getName()))
						{
							programItem.setName(parser.nextText());
		                }
						else if("date".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setDate(parser.nextText());  
		                }
						else if("time".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setTime(parser.nextText());  
		                }
						else if("replaycode".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setReplaycode(parser.nextText());  
		                }
						else if("url".equalsIgnoreCase(parser.getName()))
		                {
		                	programItem.setUrl(parser.nextText());  
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("tvreplay".equalsIgnoreCase(parser.getName()) && programItem != null) 
					{
						emData.getList().add(programItem);
						programItem = null;
					}
					break;
				}
				default:
					break;
				}
                eventType = parser.next();  
            }  
		} 
        catch (Exception e) 
		{
        	Logcat.e(FlagConstant.TAG, "=== parseTVReplay " + e.toString());
		}  
		
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseAppRecommendRes
	 * @Description: 解析点播应用推荐
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseVideoAppRecommend(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		VideoItem vedioItem = null;
		DramaItem dramaItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("videos".equalsIgnoreCase(parser.getName()))
            		{
					}
            		else if ("video".equalsIgnoreCase(parser.getName()))
            		{
            			vedioItem = new VideoItem();
            		}
            		else if (vedioItem != null)
            		{
            			if (dramaItem != null)
            			{
            				if("dramaname".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem.setDramaName(parser.nextText());  
			                }
            				else if("dramacode".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaCode(parser.nextText());  
			                }
			                else if("number".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setNumber(parser.nextText());  
			                }
			                else if("size".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setSize(parser.nextText());  
			                }
			                else if("time".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaTime(parser.nextText());  
			                }
			                else if("rate".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setRate(parser.nextText());  
			                }
			                else if("format".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setFormat(parser.nextText());  
			                }
			                else if("url".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setUrl(parser.nextText());  
			                }
			                else if("screenshots".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setScreenshots(parser.nextText());  
			                }
            			}
            			else
            			{
            				if("videocode".equalsIgnoreCase(parser.getName()))
            				{
            					vedioItem.setVideoCode(parser.nextText()); 
			                }
            				else if("name".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setName(parser.nextText());  
			                }
            				else if("area".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setArea(parser.nextText());  
			                }
            				else if("type".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setType(parser.nextText());  
			                }
            				else if("director".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setDirector(parser.nextText());  
			                }
            				else if("actor".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setActor(parser.nextText());  
			                }
            				else if("time".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTime(parser.nextText());  
			                }
            				else if("summary".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSummary(parser.nextText());  
			                }
            				else if("smallpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSmallPic(parser.nextText());  
			                }
            				else if("bigpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setBigPic(parser.nextText());
			                }
            				else if("posters".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setPosters(parser.nextText());
			                }
            				else if("score".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setScore(parser.nextText());  
			                }
            				else if("hot".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setHot(parser.nextText());  
			                }
            				else if("totaldramas".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTotalDramas(parser.nextText());  
			                }
            				else if("ratings".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setRatings(parser.nextText());  
			                }
            				else if("vodtype".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setVodtype(parser.nextText());  
			                }
            				else if("drama".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem = new DramaItem();
			                }
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("video".equalsIgnoreCase(parser.getName()) && vedioItem != null) 
            		{
						emData.getList().add(vedioItem);
						vedioItem = null;
					}
            		else if("drama".equalsIgnoreCase(parser.getName()) && vedioItem!=null && dramaItem != null)
            		{
						vedioItem.getDramaList().add(dramaItem);
						dramaItem = null;
					}
            		break;
            	}
            	default:
            		break;	
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseVideoAppRecommend " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseVideoClassify
	 * @Description: 解析点播分类列表
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseVideoClassify(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		ClassifyItem vodSort = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("classifys".equalsIgnoreCase(parser.getName()))
					{
						emData.setPage(new Integer(parser.getAttributeValue(null, "currentpage")));
						emData.setSize(new Integer(parser.getAttributeValue(null, "num")));
						emData.setTotal(new Integer(parser.getAttributeValue(null, "total")));
					}
					else if("classify".equalsIgnoreCase(parser.getName()))
					{
						vodSort = new ClassifyItem();
					}
					else if(vodSort != null)
					{
						if("classifycode".equalsIgnoreCase(parser.getName()))
						{
							 vodSort.setClassifyCode(parser.nextText());  
		                }
						else if("classifyname".equalsIgnoreCase(parser.getName()))
						{
		                    vodSort.setClassifyName(parser.nextText());  
		                }
						else if("isgroup".equalsIgnoreCase(parser.getName()))
						{
		                    vodSort.setIsgroup(parser.nextText());  
		                }
						else if("icon".equalsIgnoreCase(parser.getName()))
						{
		                    vodSort.setIcon(parser.nextText());  
		                } 
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("classify".equalsIgnoreCase(parser.getName()) && vodSort != null) 
					{
						emData.getList().add(vodSort);
						vodSort = null;
					}
					break;
				}
				default:
					break;
				}
  
                eventType = parser.next();  
            }  
		}
        catch (Exception e) 
        {
        	Logcat.e(FlagConstant.TAG, "=== parseVideoClassify " + e.toString());
		}
        return emData;
	}
	
	/**
	 * 
	 * @Title: parseHotWord
	 * @Description: 解析热词
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseHotWord(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		HotwordItem hotword = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
        try 
        {
        	XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {  
                switch (eventType) 
                {
				case XmlPullParser.START_DOCUMENT:
				{
					break;
				}
				case XmlPullParser.START_TAG:
				{
					if("hotword".equalsIgnoreCase(parser.getName()))
					{
						hotword = new HotwordItem();
					}
					else if(hotword != null)
					{
						if("id".equalsIgnoreCase(parser.getName()))
						{
							hotword.setId(new Integer(parser.nextText()));  
		                }
						else if("name".equalsIgnoreCase(parser.getName()))
						{
		                	hotword.setName(parser.nextText());  
		                }
						else if("frequency".equalsIgnoreCase(parser.getName()))
						{
		                	hotword.setFrequency(new Integer(parser.nextText()));
		                }
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					if ("hotword".equalsIgnoreCase(parser.getName()) && hotword != null) 
					{
						emData.getList().add(hotword);
						hotword = null;
					}
					break;
				}
				default:
					break;
				}
  
                eventType = parser.next();  
            }  
		} catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "parseHotWord " + e.toString());
		}  
        return emData;
	}
	
	
	
	/**
	 * 
	 * @Title: parseVideoList
	 * @Description: 解析分类下资源(包括搜索结果)
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseVideoList(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		VideoItem vedioItem = null;
		DramaItem dramaItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("videos".equalsIgnoreCase(parser.getName()))
            		{
            			emData.setPage(new Integer(parser.getAttributeValue(null, "currentpage")));
						emData.setSize(new Integer(parser.getAttributeValue(null, "num")));
						emData.setTotal(new Integer(parser.getAttributeValue(null, "total")));
					}
            		else if ("video".equalsIgnoreCase(parser.getName()))
            		{
            			vedioItem = new VideoItem();
            		}
            		else if (vedioItem != null)
            		{
            			if (dramaItem != null)
            			{
            				if("dramaname".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem.setDramaName(parser.nextText());  
			                }
            				else if("dramacode".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaCode(parser.nextText());  
			                }
			                else if("number".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setNumber(parser.nextText());  
			                }
			                else if("size".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setSize(parser.nextText());  
			                }
			                else if("time".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaTime(parser.nextText());  
			                }
			                else if("rate".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setRate(parser.nextText());  
			                }
			                else if("format".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setFormat(parser.nextText());  
			                }
			                else if("url".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setUrl(parser.nextText());  
			                }
			                else if("screenshots".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setScreenshots(parser.nextText());  
			                }
            			}
            			else
            			{
            				if("videocode".equalsIgnoreCase(parser.getName()))
            				{
            					vedioItem.setVideoCode(parser.nextText()); 
			                }
            				else if("name".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setName(parser.nextText());  
			                }
            				else if("area".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setArea(parser.nextText());  
			                }
            				else if("type".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setType(parser.nextText());  
			                }
            				else if("director".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setDirector(parser.nextText());  
			                }
            				else if("actor".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setActor(parser.nextText());  
			                }
            				else if("time".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTime(parser.nextText());  
			                }
            				else if("summary".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSummary(parser.nextText());  
			                }
            				else if("smallpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSmallPic(parser.nextText());  
			                }
            				else if("bigpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setBigPic(parser.nextText());
			                }
            				else if("score".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setScore(parser.nextText());  
			                }
            				else if("hot".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setHot(parser.nextText());  
			                }
            				else if("totaldramas".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTotalDramas(parser.nextText());  
			                }
            				else if("ratings".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setRatings(parser.nextText());  
			                }
            				else if("vodtype".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setVodtype(parser.nextText());  
			                }
            				else if("drama".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem = new DramaItem();
			                }
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("video".equalsIgnoreCase(parser.getName()) && vedioItem != null) 
            		{
						emData.getList().add(vedioItem);
						vedioItem = null;
					}
            		else if("drama".equalsIgnoreCase(parser.getName()) && vedioItem!=null && dramaItem != null)
            		{
						vedioItem.getDramaList().add(dramaItem);
						dramaItem = null;
					}
            		break;
            	}
            	default:
            		break;	
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseVideoList " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseVideo
	 * @Description: 解析资源详情
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseVideo(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		VideoItem vedioItem = null;
		DramaItem dramaItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("result".equalsIgnoreCase(parser.getName()))
            		{
					}
            		else if ("classify".equalsIgnoreCase(parser.getName()))
            		{
					}
            		else if ("video".equalsIgnoreCase(parser.getName()))
            		{
            			vedioItem = new VideoItem();
            		}
            		else if (vedioItem != null)
            		{
            			if (dramaItem != null)
            			{
            				if("dramaname".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem.setDramaName(parser.nextText());  
			                }
            				else if("dramacode".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaCode(parser.nextText());  
			                }
			                else if("number".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setNumber(parser.nextText());  
			                }
			                else if("size".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setSize(parser.nextText());  
			                }
			                else if("time".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaTime(parser.nextText());  
			                }
			                else if("rate".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setRate(parser.nextText());  
			                }
			                else if("format".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setFormat(parser.nextText());  
			                }
			                else if("url".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setUrl(parser.nextText());  
			                }
			                else if("screenshots".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setScreenshots(parser.nextText());  
			                }
            			}
            			else
            			{
            				if("videocode".equalsIgnoreCase(parser.getName()))
            				{
            					vedioItem.setVideoCode(parser.nextText()); 
			                }
            				else if("name".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setName(parser.nextText());  
			                }
            				else if("area".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setArea(parser.nextText());  
			                }
            				else if("type".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setType(parser.nextText());  
			                }
            				else if("director".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setDirector(parser.nextText());  
			                }
            				else if("actor".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setActor(parser.nextText());  
			                }
            				else if("time".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTime(parser.nextText());  
			                }
            				else if("summary".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSummary(parser.nextText());  
			                }
            				else if("smallpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSmallPic(parser.nextText());  
			                }
            				else if("bigpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setBigPic(parser.nextText());
			                }
            				else if("score".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setScore(parser.nextText());  
			                }
            				else if("hot".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setHot(parser.nextText());  
			                }
            				else if("totaldramas".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTotalDramas(parser.nextText());  
			                }
            				else if("ratings".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setRatings(parser.nextText());  
			                }
            				else if("vodtype".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setVodtype(parser.nextText());  
			                }
            				else if("drama".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem = new DramaItem();
			                }
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("video".equalsIgnoreCase(parser.getName()) && vedioItem != null) 
            		{
						emData.getList().add(vedioItem);
						vedioItem = null;
					}
            		else if("drama".equalsIgnoreCase(parser.getName()) && vedioItem!=null && dramaItem != null)
            		{
						vedioItem.getDramaList().add(dramaItem);
						dramaItem = null;
					}
            		break;
            	}
            	default:
            		break;	
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseVideo " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseVideoRecommend
	 * @Description: 解析点播推荐
	 * @param responseBytes: 服务器数据
	 * @param token: 列表id
	 * @return
	 * @return: ElementListData
	 */
	@SuppressLint("UseValueOf")
	public static ElementListData parseVideoRecommend(ByteBuffer responseBytes, int token)
	{
		ElementListData emData = new ElementListData(token, 0, 0, "");
		VideoItem vedioItem = null;
		DramaItem dramaItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());  
        InputStreamReader in = new InputStreamReader(bin);
        
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);  
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("videos".equalsIgnoreCase(parser.getName()))
            		{
            			emData.setPage(new Integer(parser.getAttributeValue(null, "currentpage")));
						emData.setSize(new Integer(parser.getAttributeValue(null, "num")));
						emData.setTotal(new Integer(parser.getAttributeValue(null, "total")));
					}
            		else if ("video".equalsIgnoreCase(parser.getName()))
            		{
            			vedioItem = new VideoItem();
            		}
            		else if (vedioItem != null)
            		{
            			if (dramaItem != null)
            			{
            				if("dramaname".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem.setDramaName(parser.nextText());  
			                }
            				else if("dramacode".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaCode(parser.nextText());  
			                }
			                else if("number".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setNumber(parser.nextText());  
			                }
			                else if("size".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setSize(parser.nextText());  
			                }
			                else if("time".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setDramaTime(parser.nextText());  
			                }
			                else if("rate".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setRate(parser.nextText());  
			                }
			                else if("format".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setFormat(parser.nextText());  
			                }
			                else if("url".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setUrl(parser.nextText());  
			                }
			                else if("screenshots".equalsIgnoreCase(parser.getName()))
			                {
			                	dramaItem.setScreenshots(parser.nextText());  
			                }
            			}
            			else
            			{
            				if("videocode".equalsIgnoreCase(parser.getName()))
            				{
            					vedioItem.setVideoCode(parser.nextText()); 
			                }
            				else if("name".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setName(parser.nextText());  
			                }
            				else if("area".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setArea(parser.nextText());  
			                }
            				else if("type".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setType(parser.nextText());  
			                }
            				else if("director".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setDirector(parser.nextText());  
			                }
            				else if("actor".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setActor(parser.nextText());  
			                }
            				else if("time".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTime(parser.nextText());  
			                }
            				else if("summary".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSummary(parser.nextText());  
			                }
            				else if("smallpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setSmallPic(parser.nextText());  
			                }
            				else if("bigpic".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setBigPic(parser.nextText());
			                }
            				else if("score".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setScore(parser.nextText());  
			                }
            				else if("hot".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setHot(parser.nextText());  
			                }
            				else if("totaldramas".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setTotalDramas(parser.nextText());  
			                }
            				else if("ratings".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setRatings(parser.nextText());  
			                }
            				else if("vodtype".equalsIgnoreCase(parser.getName()))
            				{
			                	vedioItem.setVodtype(parser.nextText());  
			                }
            				else if("drama".equalsIgnoreCase(parser.getName()))
            				{
			                	dramaItem = new DramaItem();
			                }
            			}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("video".equalsIgnoreCase(parser.getName()) && vedioItem != null) 
            		{
						emData.getList().add(vedioItem);
						vedioItem = null;
					}
            		else if("drama".equalsIgnoreCase(parser.getName()) && vedioItem!=null && dramaItem != null)
            		{
						vedioItem.getDramaList().add(dramaItem);
						dramaItem = null;
					}
            		break;
            	}
            	default:
            		break;	
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseVideoRecommend " + e.toString());
		}
		return emData;
	}
	
	/**
	 * 
	 * @Title: parseDictonary
	 * @Description: 解析应用词典
	 * @param context
	 * @return
	 * @throws IOException
	 * @return: ArrayList<DictionaryItem>
	 */
	@SuppressLint("UseValueOf")
	public static ArrayList<DictionaryItem> parseDictionary(Context context) throws IOException
	{
		AssetManager am = null;
		InputStream is = null;
		ArrayList<DictionaryItem> dictorylist = new ArrayList<DictionaryItem>();
		DictionaryItem dictionaryItem = null;
		DictionaryValueItem dictionaryValueItem = null;
		
		try 
		{		
			am = context.getAssets();
			is = am.open("dictionary.xml");
			
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlParser = pullFactory.newPullParser();
			xmlParser.setInput(is, "UTF-8");

			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) 
			{
				String tag = xmlParser.getName();
				switch (evtType) 
				{
				case XmlPullParser.START_TAG: 
				{
					if (tag.equalsIgnoreCase("item")) 
					{
						dictionaryItem = new DictionaryItem();
					} 
					else if (dictionaryItem != null) 
					{
						if (tag.equalsIgnoreCase("status")) 
						{
							dictionaryItem.setStatus(new Integer(xmlParser.nextText()));
						}
						
						if (tag.equalsIgnoreCase("value")) 
						{
							dictionaryValueItem = new DictionaryValueItem();
							dictionaryValueItem.setLanguage(xmlParser.getAttributeValue(null, "lang"));
							dictionaryValueItem.setContent(xmlParser.nextText());
							dictionaryItem.getContentList().add(dictionaryValueItem);
							dictionaryValueItem = null;
						}
					}
					break;
				}
				case XmlPullParser.END_TAG: 
				{
					if (tag.equalsIgnoreCase("item") && dictionaryItem != null) 
					{
						dictorylist.add(dictionaryItem);
						dictionaryItem = null;
					}
					break;
				}
				default:
            		break;	
				}
				evtType = xmlParser.next();
			}
		} 
		catch (XmlPullParserException e) 
		{
			Logcat.e(FlagConstant.TAG, "=== parseDictionary " + e.toString());
			e.printStackTrace();
		} 
		finally 
		{
			if (is != null)
			{
				is.close();
			}
		}

		return dictorylist;
	}
}
