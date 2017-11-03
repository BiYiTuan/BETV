/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: DatabaseManager.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 实现数据库管理
 * @author: zhaoqy
 * @date: 2014-8-8 上午9:30:01
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import com.szgvtv.ead.app.taijietemplates.util.Constant;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DatabaseManager extends ContentProvider
{
	public static SQLiteDatabase mSqlDB;     //全局数据库
	public static DatabaseHelper mDbHelper;  //数据库帮助器
	public static Context        mContext;   //应用上下文
	
	@Override
	public boolean onCreate() 
	{
		if (mDbHelper == null) 
		{
			mDbHelper = new DatabaseHelper(getContext());
			mContext = getContext();
		}
		return (mDbHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		qb.setTables(uri.getPathSegments().get(0));

		Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) 
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		return null;
	}

	@Override
	public int delete(Uri uri, String s, String[] as) 
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		return db.delete(uri.getPathSegments().get(0), s, as);
	}

	@Override
	public int update(Uri uri, ContentValues values, String s, String[] as) 
	{
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		return db.update(uri.getPathSegments().get(0), values, s, as);
	}
	
	public static class DatabaseHelper extends SQLiteOpenHelper 
	{
		DatabaseHelper(Context context) 
		{
			super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(DramaInfoTable.getCreateSql());
			db.execSQL(VideoInfoTable.getCreateSql());
			db.execSQL(AppRecdInfoTable.getCreateSql());
			
			db.execSQL(LiveChannelTable.getCreateSql());
			db.execSQL(LivePreviewInfoTable.getCreateSql());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL(DramaInfoTable.getUpgradeSql());
			db.execSQL(VideoInfoTable.getUpgradeSql());
			db.execSQL(AppRecdInfoTable.getUpgradeSql());
			
			db.execSQL(LiveChannelTable.getUpgradeSql());
			db.execSQL(LivePreviewInfoTable.getUpgradeSql());
			onCreate(db);
		}
	}
}
