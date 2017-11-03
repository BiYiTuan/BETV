/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: AppRecdInfoTable.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 操作首页推荐数据库
 * @author: zhaoqy
 * @date: 2014-9-15 下午5:51:17
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class AppRecdInfoTable 
{
	public static final String AUTHORITY = "com.szgvtv.ead.app.taijietemplates_betvytv.db.DatabaseManager";  //类名
	public static final String APPRECD_TABLE = "apprecdtable";                                            //表名
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://" + AUTHORITY + "/" + APPRECD_TABLE); //访问uri
	public static final String ID = "_id";                                                                //主键
	public static final String APPRECD_NAME = "apprecd_name";                                             //推荐项名称
	public static final String APPRECD_CODE = "apprecd_code";                                             //资源编码
	public static final String APPRECD_TYPE = "apprecd_type";                                             //类型
	public static final String APPRECD_IMAGE = "apprecd_image";                                           //图片地址
	
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
		sb.append(APPRECD_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(APPRECD_NAME);
		sb.append(" TEXT,");
		sb.append(APPRECD_CODE);
		sb.append(" TEXT,");
		sb.append(APPRECD_TYPE);
		sb.append(" TEXT,");
		sb.append(APPRECD_IMAGE);
		sb.append(" TEXT");
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
		String string = "DROP TABLE IF EXISTS " + APPRECD_TABLE;
		return string;
	}
	
	/**
	 * 
	 * @Title: queryAllAppRecd
	 * @Description: 查询所有首页推荐
	 * @return
	 * @return: ArrayList<AppRecdInfo>
	 */
	public static ArrayList<AppRecdInfo> queryAllAppRecd()
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		ArrayList<AppRecdInfo> recdList = new ArrayList<AppRecdInfo>();
		Cursor mCursor = null;
		String where = null;
		String orderBy = ID + " ASC";
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(APPRECD_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(), CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				AppRecdInfo recdInfo = new AppRecdInfo();
				recdInfo.setName(mCursor.getString(mCursor.getColumnIndex(APPRECD_NAME)));
				recdInfo.setCode(mCursor.getString(mCursor.getColumnIndex(APPRECD_CODE)));
				recdInfo.setType(mCursor.getString(mCursor.getColumnIndex(APPRECD_TYPE)));
				recdInfo.setImage(mCursor.getString(mCursor.getColumnIndex(APPRECD_IMAGE)));
				recdList.add(recdInfo);
			}
		} 
		
		if (mCursor != null) 
		{
		  	mCursor.close();
			mCursor = null;
		}
		return recdList;
	}
	
	/**
	 * 
	 * @Title: getOldRecord
	 * @Description: 获取记录
	 * @return
	 * @return: AppRecdInfo
	 */
	public static AppRecdInfo getOldRecord()
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		AppRecdInfo recdInfo = null;
		Cursor mCursor = null;
		String where = null;
		String orderBy = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		orderBy = ID + " ASC";
		mCursor = db.query(APPRECD_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			mCursor.moveToPosition(0);
			recdInfo = new AppRecdInfo();
			recdInfo.setName(mCursor.getString(mCursor.getColumnIndex(APPRECD_NAME)));
			recdInfo.setCode(mCursor.getString(mCursor.getColumnIndex(APPRECD_CODE)));
			recdInfo.setType(mCursor.getString(mCursor.getColumnIndex(APPRECD_TYPE)));
			recdInfo.setImage(mCursor.getString(mCursor.getColumnIndex(APPRECD_IMAGE)));
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return recdInfo;
	}
	
	/**
	 * 
	 * @Title: queryAppRecd
	 * @Description: 根据资源编码查询该首页推荐
	 * @param code
	 * @return
	 * @return: AppRecdInfo
	 */
	public static AppRecdInfo queryAppRecd(String code)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		AppRecdInfo recdInfo = null;
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = APPRECD_CODE + "=\"" + code + "\" ";
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		mCursor = db.query(APPRECD_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			if (n > 0)
			{
				mCursor.moveToPosition(0);
				recdInfo = new AppRecdInfo();
				recdInfo.setName(mCursor.getString(mCursor.getColumnIndex(APPRECD_NAME)));
				recdInfo.setCode(mCursor.getString(mCursor.getColumnIndex(APPRECD_CODE)));
				recdInfo.setType(mCursor.getString(mCursor.getColumnIndex(APPRECD_TYPE)));
				recdInfo.setImage(mCursor.getString(mCursor.getColumnIndex(APPRECD_IMAGE)));
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return recdInfo;
	}
	
	/**
	 * 
	 * @Title: insertAppRecd
	 * @Description: 插入首页推荐
	 * @param appRecd
	 * @return
	 * @return: boolean
	 */
	public static boolean insertAppRecd(AppRecdInfo appRecd)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		AppRecdInfo appRecdInfo = appRecd;
		if(queryAppRecd(appRecdInfo.getCode()) != null)
		{
			deleteAppRecd(appRecdInfo.getCode());
			
			ContentValues values = new ContentValues();
			values.put(APPRECD_NAME, appRecdInfo.getName());
			values.put(APPRECD_CODE, appRecdInfo.getCode());
			values.put(APPRECD_TYPE, appRecdInfo.getType());
			values.put(APPRECD_IMAGE, appRecdInfo.getImage());
			db.insert(APPRECD_TABLE, null, values);
		}
		else
		{
			int count = queryAllAppRecd().size();
			int maxCount = 2;
			
			if(count >= maxCount)
			{
				AppRecdInfo info2 = getOldRecord();
				
				if(info2 != null)
				{
					deleteAppRecd(info2.getCode());
				}
			}
			
			ContentValues values = new ContentValues();
			values.put(APPRECD_NAME, appRecdInfo.getName());
			values.put(APPRECD_CODE, appRecdInfo.getCode());
			values.put(APPRECD_TYPE, appRecdInfo.getType());
			values.put(APPRECD_IMAGE, appRecdInfo.getImage());
			db.insert(APPRECD_TABLE, null, values);
		}
		return true;
	}
	
	/**
	 * 
	 * @Title: deleteAppRecd
	 * @Description: 删除首页推荐
	 * @param code
	 * @return
	 * @return: boolean
	 */
	public static boolean deleteAppRecd(String code)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = APPRECD_CODE + "=\"" + code + "\" ";
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(APPRECD_TABLE, where, null);
		return true;
	}	
}
