/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveChannelInfoTable.java
 * @Prject: TvbTemps
 * @Package: com.szgvtv.ead.app.tvbtemps.db
 * @Description: 操作直播频道数据库
 * @author: zhaoqy
 * @date: 2014-12-16 下午5:06:52
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LiveChannelItem;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LiveChannelTable 
{
	public static final String AUTHORITY = "com.szgvtv.ead.app.taijietemplates_betvytv.db.DatabaseManager"; //类名
	public static final String CHANNEL_TABLE = "channeltable";                                            //表名
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://" + AUTHORITY + "/" + CHANNEL_TABLE); //访问uri
	public static final String ID = "_id";                                                                //主键
	public static final String CHANNEL_ID = "channel_id";                                                 //直播频道ID	
	public static final String CHANNEL_CODE = "channel_code";                                             //直播频道编码 
	public static final String CHANNEL_NAME = "channel_name";                                             //直播频道名称
	public static final String CHANNEL_LOGO = "channel_logo";                                             //直播频道logo
	public static final String CHANNEL_URL = "channelurl";                                                //直播频道播放地址
	public static final String CHANNEL_REPLAY = "channel_replay";                                         //直播频道播放地址
	
	/**
	 * @Title: getCreateSql
	 * @Description: 得到创建表spl
	 * @return 返回创建表spl
	 * @return: String
	 */
	public static String getCreateSql() 
	{
		/**
		 * SQL语句：
		 * db.execSQL("CREATE TABLE  IF NOT EXISTS " + TABLE_DOWNLOAD + "( "
		 * + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "_app_title TEXT," +
		 * APP_INTENT + " TEXT," + "_app_sort Integer," +
		 * "_app_sort_child Integer," + APP_PACKAGE_NAME + " TEXT"+");");
		 */		
		StringBuffer sb = new StringBuffer();	
		sb.append("CREATE TABLE  IF NOT EXISTS ");
		sb.append(CHANNEL_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(CHANNEL_ID);
		sb.append(" TEXT,");
		sb.append(CHANNEL_CODE);
		sb.append(" TEXT,");
		sb.append(CHANNEL_NAME);
		sb.append(" TEXT,");
		sb.append(CHANNEL_LOGO);
		sb.append(" TEXT,");
		sb.append(CHANNEL_URL);
		sb.append(" TEXT,");
		sb.append(CHANNEL_REPLAY);
		sb.append(" Integer");
		sb.append(");");
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @Title: getUpgradeSql
	 * @Description: 得到升级表spl
	 * @return
	 * @return: String
	 */
	public static String getUpgradeSql() 
	{
		/**
		 * SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		 * qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0)); Cursor
		 * abcCursor=qb.query(db,null,null,null,null,null,null);
		 */
		String string = "DROP TABLE IF EXISTS " + CHANNEL_TABLE;
		return string;
	}
	
	/**
	 * 
	 * @Title: queryAllLiveChannelInfos
	 * @Description: 查询频道信息列表
	 * @return
	 * @return: ArrayList<LiveChannelInfo>
	 */
	public static ArrayList<LiveChannelItem> queryAllLiveChannels()
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		ArrayList<LiveChannelItem> channelList = new ArrayList<LiveChannelItem>();
		Cursor mCursor = null;
		String where = null;
		//String orderBy = ID + " DESC"; //该参数表示查询顺序, (DESC-倒序), 查询返回的直播列表是顺序 
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(CHANNEL_TABLE, null, where, null, null, null, /*orderBy*/null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(), CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i=0; i<n; i++) 
			{
				mCursor.moveToPosition(i);
				LiveChannelItem channelItem = new LiveChannelItem();
				channelItem.setTvId(mCursor.getString(mCursor.getColumnIndex(CHANNEL_ID)));
				channelItem.setTvCode(mCursor.getString(mCursor.getColumnIndex(CHANNEL_CODE)));
				channelItem.setTvName(mCursor.getString(mCursor.getColumnIndex(CHANNEL_NAME)));
				channelItem.setTvLogo(mCursor.getString(mCursor.getColumnIndex(CHANNEL_LOGO)));
				channelItem.setTvUrl(mCursor.getString(mCursor.getColumnIndex(CHANNEL_URL)));
				channelItem.setIsReplay(mCursor.getInt(mCursor.getColumnIndex(CHANNEL_REPLAY)));
				channelList.add(channelItem);
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return channelList;
	}
	
	/**
	 * 
	 * @Title: queryChannelInfo
	 * @Description: 根据channelCode查询频道信息
	 * @param channelCode
	 * @return
	 * @return: LiveChannelInfo
	 */
	public static LiveChannelItem queryChannel(String channelCode)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		LiveChannelItem channelItem = null;
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = CHANNEL_CODE + "=\"" + channelCode + "\" ";
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		mCursor = db.query(CHANNEL_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(), CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i=0; i<n; i++) 
			{
				mCursor.moveToPosition(i);
				channelItem = new LiveChannelItem();
				channelItem.setTvId(mCursor.getString(mCursor.getColumnIndex(CHANNEL_ID)));
				channelItem.setTvCode(mCursor.getString(mCursor.getColumnIndex(CHANNEL_CODE)));
				channelItem.setTvName(mCursor.getString(mCursor.getColumnIndex(CHANNEL_NAME)));
				channelItem.setTvLogo(mCursor.getString(mCursor.getColumnIndex(CHANNEL_LOGO)));
				channelItem.setTvUrl(mCursor.getString(mCursor.getColumnIndex(CHANNEL_URL)));
				channelItem.setIsReplay(mCursor.getInt(mCursor.getColumnIndex(CHANNEL_REPLAY)));
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return channelItem;
	}
	
	/**
	 * 
	 * @Title: insertChannelInfoList
	 * @Description: 插入频道列表
	 * @param channelInfoList
	 * @return
	 * @return: boolean
	 */
	public static boolean insertChannelList(ArrayList<LiveChannelItem> channelList)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		//查询数据库中频道列表个数是否大于0
		if(queryAllLiveChannels().size() > 0)
		{
			//如果数据库中频道列表个数大于0, 则删除该列表
			deleteChannelList();
		}
		
		//插入新的频道列表
		db.beginTransaction();
		for (int i=0; i<channelList.size(); i++) 
		{
			LiveChannelItem channelItem = channelList.get(i);
			//插入该频道信息
			ContentValues values = new ContentValues();
			values.put(CHANNEL_ID, channelItem.getTvId());
			values.put(CHANNEL_CODE, channelItem.getTvCode());
			values.put(CHANNEL_NAME, channelItem.getTvName());
			values.put(CHANNEL_LOGO, channelItem.getTvLogo());
			values.put(CHANNEL_URL, channelItem.getTvUrl());
			values.put(CHANNEL_REPLAY, channelItem.getIsReplay());
			db.insert(CHANNEL_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}

	/**
	 * 
	 * @Title: deleteChannelInfoList
	 * @Description: 删除该频道信息列表
	 * @return
	 * @return: boolean
	 */
	public static boolean deleteChannelList()
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(CHANNEL_TABLE, where, null);
		return true;
	}	
}
