/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: LiveGuideInfoTable.java
 * @Prject: TvbTemps
 * @Package: com.szgvtv.ead.app.tvbtemps.db
 * @Description: 操作直播预告数据库
 * @author: zhaoqy
 * @date: 2014-12-16 下午5:07:49
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import java.util.ArrayList;
import java.util.Calendar;
import com.szgvtv.ead.app.taijietemplates.dataprovider.dataitem.LivePreviewItem;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LivePreviewInfoTable 
{
	public static final String AUTHORITY = "com.szgvtv.ead.app.taijietemplates_betvytv.db.DatabaseManager"; //类名
	public static final String PREVIEW_TABLE = "previewtable";                                              //表名
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://" + AUTHORITY + "/" + PREVIEW_TABLE); //访问uri
	public static final String ID = "_id";                                                                //主键
	public static final String CHANNEL_CODE = "channel_code";                                             //频道编码
	public static final String PREVIEW_NAME = "preview_name";                                             //节目预告名称
	public static final String PREVIEW_DATE = "preview_date";                                             //节目预告日期
	public static final String PREVIEW_TIME = "preview_time";                                             //节目预告时间
	
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
		sb.append(PREVIEW_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(CHANNEL_CODE);
		sb.append(" TEXT,");
		sb.append(PREVIEW_NAME);
		sb.append(" TEXT,");
		sb.append(PREVIEW_DATE);
		sb.append(" TEXT,");
		sb.append(PREVIEW_TIME);
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
		String string = "DROP TABLE IF EXISTS " + PREVIEW_TABLE;
		return string;
	}
	
	/**
	 * 
	 * @Title: queryGuide
	 * @Description: 根据频道编码查询预告
	 * @param channelCode
	 * @return
	 * @return: ArrayList<LivePreviewItem>
	 */
	public static ArrayList<LivePreviewItem> queryPreviewByCode(String channelCode)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		
		ArrayList<LivePreviewItem> previewList = new ArrayList<LivePreviewItem>();
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = CHANNEL_CODE + "=\"" + channelCode + "\" ";
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(PREVIEW_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i=0; i<n; i++) 
			{
				mCursor.moveToPosition(i);
				LivePreviewItem previewItem = new LivePreviewItem();
				previewItem.setName(mCursor.getString(mCursor.getColumnIndex(PREVIEW_NAME)));
				previewItem.setDate(mCursor.getString(mCursor.getColumnIndex(PREVIEW_DATE)));
				previewItem.setTime(mCursor.getString(mCursor.getColumnIndex(PREVIEW_TIME)));
				previewList.add(previewItem);
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		
		if (previewList.size() > 0)
		{
			Calendar c = Calendar.getInstance();  
			int curYear = c.get(Calendar.YEAR);
			int curMonth = c.get(Calendar.MONTH) + 1;
			int curDay = c.get(Calendar.DAY_OF_MONTH);
			
			//比较第一条数据的日期
			String date = previewList.get(0).getDate();
			String curDate = "";
			String year = "" + curYear;
			String month = "";
			String day = "";
			
			if (curMonth < 10) 
			{
				month = "0" + curMonth;
			}
			else
			{
				month = "" + curMonth;
			}
			
			if (curDay < 10)
			{
				day = "0" + curDay;
			}
			else
			{
				day = "" + curDay;
			}
			
			curDate = year + "-" + month + "-" + day;
			Logcat.d(FlagConstant.TAG, " curDate: " + curDate);
			Logcat.d(FlagConstant.TAG, " date: " + date);
			
			if (curDate.endsWith(date))
			{
				return previewList;
			}
			else
			{
				Logcat.d(FlagConstant.TAG, " delete " + channelCode);
				//日期不匹配, 则清除
				previewList.clear();
				deletePreviews(channelCode);
			}
		}
		
		return previewList;
	}
	
	/**
	 * @Title: insertGuide
	 * @Description: 插入预告
	 * @param previewList 需要插入预告
	 * @return: void
	 */
	public static void insertPreviews(ArrayList<LivePreviewItem> previewList, String channelCode)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return ;
		}
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		//如果该频道有以前的节目预告, 则先删除掉
		if (queryPreviewByCode(channelCode).size() > 0)
		{
			deletePreviews(channelCode);
		}
		
		db.beginTransaction();
		for (int i=0; i<previewList.size(); i++) 
		{
			LivePreviewItem info = previewList.get(i);
			ContentValues values = new ContentValues();
			values.put(CHANNEL_CODE, channelCode);
			values.put(PREVIEW_NAME, info.getName());
			values.put(PREVIEW_DATE, info.getDate());
			values.put(PREVIEW_TIME, info.getTime());
			db.insert(PREVIEW_TABLE, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	/**
	 * @Title: deleteGuide
	 * @Description: 根据频道编码删除预告
	 * @param channelCode 频道编码
	 * @return: void
	 */
	public static void deletePreviews(String channelCode)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return ;
		}
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = CHANNEL_CODE + "=\"" + channelCode + "\" ";
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(PREVIEW_TABLE, where, null);
	}
}
