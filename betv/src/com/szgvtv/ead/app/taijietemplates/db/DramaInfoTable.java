/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DramaInfoTable.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 操作剧集数据库
 * @author: zhaoqy
 * @date: 2014-8-8 上午9:33:19
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import java.util.ArrayList;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.DramaItem;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DramaInfoTable 
{
	public static final String AUTHORITY = "com.szgvtv.ead.app.taijietemplates_betvytv.db.DatabaseManager"; //类名
	public static final String DRAMA_TABLE = "dramatable";                                              //表名
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://" + AUTHORITY + "/" + DRAMA_TABLE); //访问uri
	public static final String ID = "_id";                                                              //主键
	public static final String VIDEO_CODE = "video_code";                                               //资源编码
	public static final String VIDEO_SORT = "video_sort";                                               //资源类型(0-收藏; 1-播放历史)
	public static final String DRAMA_NAME = "drama_name";                                               //剧集名称
	public static final String DRAMA_CODE = "drama_code";                                               //剧集编码
	public static final String DRAMA_NUM = "drama_num";                                                 //剧集数
	public static final String DRAMA_SIZE = "drama_size";                                               //剧集大小
	public static final String DRAMA_TIME = "drama_time";                                               //剧集时间
	public static final String DRAMA_RATE = "drama_rate";                                               //剧集码流
	public static final String DRAMA_FORMAT = "drama_format";	                                        //剧集格式
	public static final String DRAMA_MOVIEURL = "drama_movieurl";                                       //剧集播放url
	public static final String DRAMA_SCREENSHOTS = "drama_screenshots";                                 //剧集截图
	
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
		sb.append(DRAMA_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(VIDEO_CODE);
		sb.append(" TEXT,");
		sb.append(VIDEO_SORT);
		sb.append(" Integer,");
		sb.append(DRAMA_NAME);
		sb.append(" TEXT,");
		sb.append(DRAMA_CODE);
		sb.append(" TEXT,");
		sb.append(DRAMA_NUM);
		sb.append(" TEXT,");
		sb.append(DRAMA_SIZE);
		sb.append(" TEXT,");
		sb.append(DRAMA_TIME);
		sb.append(" TEXT,");
		sb.append(DRAMA_RATE);
		sb.append(" TEXT,");
		sb.append(DRAMA_FORMAT);
		sb.append(" TEXT,");
		sb.append(DRAMA_MOVIEURL);
		sb.append(" TEXT,");
		sb.append(DRAMA_SCREENSHOTS);
		sb.append(" TEXT");
		sb.append(");");
		
		return sb.toString();
	}

	public static String getUpgradeSql() 
	{
		/**
		 * SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		 * qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0)); Cursor
		 * abcCursor=qb.query(db,null,null,null,null,null,null);
		 */
		String string = "DROP TABLE IF EXISTS " + DRAMA_TABLE;
		return string;
	}
	
	/**
	 * @Title: queryAllDramas
	 * @Description: 查询所有剧集
	 * @param videoId 电影id
	 * @return 剧集列表
	 * @return: ArrayList<DramaInfo>
	 */
	public static ArrayList<DramaItem> queryAllDramas(String videoCode, int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		ArrayList<DramaItem> dramaList = new ArrayList<DramaItem>();
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(DRAMA_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				DramaInfo dramainfo = new DramaInfo();
				
				dramainfo.setVideoCode(videoCode);
				dramainfo.setSort(sort);
				dramainfo.setDramaName(mCursor.getString(mCursor.getColumnIndex(DRAMA_NAME)));
				dramainfo.setDramaCode(mCursor.getString(mCursor.getColumnIndex(DRAMA_CODE)));
				dramainfo.setNumber(mCursor.getString(mCursor.getColumnIndex(DRAMA_NUM)));
				dramainfo.setSize(mCursor.getString(mCursor.getColumnIndex(DRAMA_SIZE)));
				dramainfo.setDramaTime(mCursor.getString(mCursor.getColumnIndex(DRAMA_TIME)));
				dramainfo.setRate(mCursor.getString(mCursor.getColumnIndex(DRAMA_RATE)));
				dramainfo.setFormat(mCursor.getString(mCursor.getColumnIndex(DRAMA_FORMAT)));
				dramainfo.setUrl(mCursor.getString(mCursor.getColumnIndex(DRAMA_MOVIEURL)));
				dramainfo.setScreenshots(mCursor.getString(mCursor.getColumnIndex(DRAMA_SCREENSHOTS)));
				dramaList.add(dramainfo);
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return dramaList;
	}
	
	/**
	 * @Title: insertDrama
	 * @Description: 插入剧集
	 * @param dramaList 需要插入剧集
	 * @return: void
	 */
	public static void insertDrama(ArrayList<DramaItem> dramaList, String videoCode, int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return ;
		}
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		db.beginTransaction();
		for (int i = 0; i < dramaList.size(); i++) 
		{
			DramaItem info = dramaList.get(i);
			ContentValues values = new ContentValues();
			values.put(VIDEO_CODE, videoCode);
			values.put(VIDEO_SORT, sort);
			values.put(DRAMA_NAME, info.getDramaName());
			values.put(DRAMA_CODE, info.getDramaCode());
			values.put(DRAMA_NUM, info.getNumber());
			values.put(DRAMA_SIZE, info.getSize());
			values.put(DRAMA_TIME, info.getDramaTime());
			values.put(DRAMA_RATE, info.getRate());
			values.put(DRAMA_FORMAT, info.getFormat());
			values.put(DRAMA_MOVIEURL, info.getUrl());
			values.put(DRAMA_SCREENSHOTS, info.getScreenshots());
			db.insert(DRAMA_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	/**
	 * @Title: deleteAllDrama
	 * @Description: 删除剧集
	 * @param movieId 电影id
	 * @return: void
	 */
	public static void deleteAllDrama(String videoCode, int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return ;
		}
		
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(DRAMA_TABLE, where, null);
	}
}
