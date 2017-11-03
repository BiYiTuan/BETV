/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: VideoInfoTable.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.db
 * @Description: 操作点播资源数据库 (类型: 0-收藏, 1-播放历史)
 * @author: zhaoqy
 * @date: 2014-8-8 上午9:37:16
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class VideoInfoTable 
{
	public static final String AUTHORITY = "com.szgvtv.ead.app.taijietemplates_betvytv.db.DatabaseManager"; //类名
	public static final String VIDEO_TABLE = "videotable";                                              //表名
	public static final Uri CONTENT_SORT_URI = Uri.parse("content://" + AUTHORITY + "/" + VIDEO_TABLE); //访问uri
	public static final String ID = "_id";                                                              //主键
	public static final String VIDEO_CODE = "video_code";                                               //资源编码
	public static final String VIDEO_NAME = "video_name";                                               //资源名称
	public static final String VIDEO_AREA = "video_area";                                               //区域
	public static final String VIDEO_TYPE = "video_type";                                               //类型
	public static final String VIDEO_DIRECTOR = "video_director";                                       //导演
	public static final String VIDEO_ACTOR = "video_actor";                                             //演员
	public static final String VIDEO_TIME = "video_time";                                               //上映时间
	public static final String VIDEO_SUMMARY = "video_summary";                                         //简介
	public static final String VIDEO_SMALL_PIC = "video_small_pic";                                     //小图
	public static final String VIDEO_BIG_PIC = "video_big_pic";                                         //大图
	public static final String VIDEO_POSTER_PIC = "video_poster_pic";                                   //海報
	public static final String VIDEO_SCORE = "video_score";                                             //评分
	public static final String VIDEO_HOT = "video_hot";  	                                            //热度
	public static final String VIDEO_TOTALDRAMAS = "video_totaldramas";                                 //剧集数
	public static final String VIDEO_RATING = "video_rating";                                           //用户点评结果
	public static final String VIDEO_VODTYPE = "video_vodtype";                                         //类型：1-电影；2-电视剧；3-综艺;
	public static final String VIDEO_DRAMA_CODE = "drama_code";                                         //剧集编码
	public static final String VIDEO_SORT = "drama_sort";                                               //分类（收藏 历史）
	public static final String VIDEO_DRAMA_TIME = "drama_time";                                         //剧集播放到时间	
	public static final int SORT_FAVORITE = 0;                                                          //0-收藏
	public static final int SORT_HISTORY = 1;                                                           //1-播放历史
	public static final int SORT_RECOMMEND = 2;                                                         //2-推荐
	
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
		sb.append(VIDEO_TABLE);
		sb.append("(");
		sb.append(ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb.append(VIDEO_CODE);
		sb.append(" TEXT,");
		sb.append(VIDEO_NAME);
		sb.append(" TEXT,");
		sb.append(VIDEO_AREA);
		sb.append(" TEXT,");
		sb.append(VIDEO_TYPE);
		sb.append(" TEXT,");
		sb.append(VIDEO_DIRECTOR);
		sb.append(" TEXT,");
		sb.append(VIDEO_ACTOR);
		sb.append(" TEXT,");
		sb.append(VIDEO_TIME);
		sb.append(" TEXT,");
		sb.append(VIDEO_SUMMARY);
		sb.append(" TEXT,");
		sb.append(VIDEO_SMALL_PIC);
		sb.append(" TEXT,");
		sb.append(VIDEO_BIG_PIC);
		sb.append(" TEXT,");
		sb.append(VIDEO_POSTER_PIC);
		sb.append(" TEXT,");
		sb.append(VIDEO_SCORE);
		sb.append(" TEXT,");
		sb.append(VIDEO_HOT);
		sb.append(" TEXT,");
		sb.append(VIDEO_TOTALDRAMAS);
		sb.append(" TEXT,");
		sb.append(VIDEO_RATING);
		sb.append(" TEXT,");
		sb.append(VIDEO_VODTYPE);
		sb.append(" TEXT,");
		sb.append(VIDEO_DRAMA_CODE);
		sb.append(" TEXT,");
		sb.append(VIDEO_SORT);
		sb.append(" Integer,");	
		sb.append(VIDEO_DRAMA_TIME);
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
		String string = "DROP TABLE IF EXISTS " + VIDEO_TABLE;
		return string;
	}
	
	/**
	 * @Title: queryAllSortVideos
	 * @Description: 查询所有分类电影
	 * @param sort 类型
	 * @return
	 * @return: ArrayList<VideoInfo>
	 */
	public static ArrayList<VideoInfo> queryAllSortVideos(int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
		Cursor mCursor = null;
		String where = null;
		String orderBy = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		orderBy = ID + " DESC";
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(), CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				VideoInfo videoinfo = new VideoInfo();
				videoinfo.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
				videoinfo.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
				videoinfo.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
				videoinfo.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
				videoinfo.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
				videoinfo.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
				videoinfo.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
				videoinfo.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
				videoinfo.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
				videoinfo.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
				videoinfo.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
				videoinfo.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
				videoinfo.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
				videoinfo.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
				videoinfo.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
				videoinfo.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
				videoinfo.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
				videoinfo.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
				videoinfo.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
				videoinfo.setDramaList(DramaInfoTable.queryAllDramas(videoinfo.getVideoCode(), sort));
				videoList.add(videoinfo);
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return videoList;
	}
	
	public static int queryAllSortVideosCount(int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return 0;
		}
		ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
		Cursor mCursor = null;
		String where = null;
		String orderBy = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		orderBy = ID + " DESC";
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(), CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				VideoInfo videoinfo = new VideoInfo();
				videoinfo.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
				videoinfo.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
				videoinfo.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
				videoinfo.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
				videoinfo.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
				videoinfo.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
				videoinfo.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
				videoinfo.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
				videoinfo.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
				videoinfo.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
				videoinfo.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
				videoinfo.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
				videoinfo.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
				videoinfo.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
				videoinfo.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
				videoinfo.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
				videoinfo.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
				videoinfo.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
				videoinfo.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
				videoinfo.setDramaList(DramaInfoTable.queryAllDramas(videoinfo.getVideoCode(), sort));
				videoList.add(videoinfo);
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return videoList.size();
	}
	
	/**
	 * 
	 * @Title: queryTopRecord
	 * @Description: 查询记录
	 * @param sort
	 * @return
	 * @return: VideoInfo
	 */
	public static VideoInfo queryTopRecord(int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		VideoInfo videoinfo = null;
		Cursor mCursor = null;
		String where = null;
		String orderBy = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		orderBy = ID + " DESC";
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			if(n > 0)
			{
				mCursor.moveToPosition(0);
				videoinfo = new VideoInfo();
				videoinfo.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
				videoinfo.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
				videoinfo.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
				videoinfo.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
				videoinfo.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
				videoinfo.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
				videoinfo.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
				videoinfo.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
				videoinfo.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
				videoinfo.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
				videoinfo.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
				videoinfo.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
				videoinfo.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
				videoinfo.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
				videoinfo.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
				videoinfo.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
				videoinfo.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
				videoinfo.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
				videoinfo.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
				videoinfo.setDramaList(DramaInfoTable.queryAllDramas(videoinfo.getVideoCode(), sort));
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return videoinfo;
	}
	
	/**
	 * @Title: queryVideo
	 * @Description: 根据id和类型查询信息
	 * @param videoId
	 * @param sort
	 * @return
	 * @return: VideoInfo
	 */
	public static VideoInfo queryVideo(String videoCode, int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		VideoInfo info = null;
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				info = new VideoInfo();
				info.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
				info.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
				info.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
				info.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
				info.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
				info.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
				info.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
				info.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
				info.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
				info.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
				info.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
				info.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
				info.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
				info.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
				info.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
				info.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
				info.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
				info.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
				info.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
				info.setDramaList(DramaInfoTable.queryAllDramas(videoCode, sort));
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return info;
	}
	
	/**
	 * @Title: insertVideos
	 * @Description: 插入视频
	 * @param videolist
	 * @return: void
	 */
	public static boolean insertVideo(VideoInfo video)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		
		VideoInfo info = video;
		if(queryVideo(info.getVideoCode(), info.getSort()) != null)
		{
			if(info.getSort() == VideoInfoTable.SORT_HISTORY || info.getSort() == VideoInfoTable.SORT_RECOMMEND)
			{
				deleteVideo(info.getVideoCode(), info.getSort());
				
				ContentValues values = new ContentValues();
				values.put(VIDEO_CODE, info.getVideoCode());
				values.put(VIDEO_NAME, info.getName());
				values.put(VIDEO_AREA, info.getArea());
				values.put(VIDEO_TYPE, info.getType());
				values.put(VIDEO_DIRECTOR, info.getDirector());
				values.put(VIDEO_ACTOR, info.getActor());
				values.put(VIDEO_TIME, info.getTime());
				values.put(VIDEO_SUMMARY, info.getSummary());
				values.put(VIDEO_SMALL_PIC, info.getSmallPic());
				values.put(VIDEO_BIG_PIC, info.getBigPic());
				values.put(VIDEO_POSTER_PIC, info.getPosters());
				values.put(VIDEO_SCORE, info.getScore());
				values.put(VIDEO_HOT, info.getHot());
				values.put(VIDEO_TOTALDRAMAS, info.getTotalDramas());
				values.put(VIDEO_RATING, info.getRatings());
				values.put(VIDEO_VODTYPE, info.getVodtype());
				values.put(VIDEO_DRAMA_CODE, info.getDramaCode());
				values.put(VIDEO_SORT, info.getSort());
				values.put(VIDEO_DRAMA_TIME, info.getPlayTime());	
				db.insert(VIDEO_TABLE, null, values);
				DramaInfoTable.insertDrama(info.getDramaList(), info.getVideoCode(), info.getSort());
			}
		}
		else
		{
			int count = queryAllSortVideos(info.getSort()).size();
			int maxCount = 0;
			
			if(info.getSort() == VideoInfoTable.SORT_FAVORITE)
			{
				maxCount = 8;
			}
			else if(info.getSort() == VideoInfoTable.SORT_HISTORY)
			{
				maxCount = 10;
			}
			else if (info.getSort() == VideoInfoTable.SORT_RECOMMEND)
			{
				maxCount = 2;
			}
			
			if(count >= maxCount)
			{
				VideoInfo info2 = getOldRecord(info.getSort());
				
				if(info2 != null)
				{
					deleteVideo(info2.getVideoCode(), info2.getSort());
				}
			}
			
			ContentValues values = new ContentValues();
			values.put(VIDEO_CODE, info.getVideoCode());
			values.put(VIDEO_NAME, info.getName());
			values.put(VIDEO_AREA, info.getArea());
			values.put(VIDEO_TYPE, info.getType());
			values.put(VIDEO_DIRECTOR, info.getDirector());
			values.put(VIDEO_ACTOR, info.getActor());
			values.put(VIDEO_TIME, info.getTime());
			values.put(VIDEO_SUMMARY, info.getSummary());
			values.put(VIDEO_SMALL_PIC, info.getSmallPic());
			values.put(VIDEO_BIG_PIC, info.getBigPic());
			values.put(VIDEO_POSTER_PIC, info.getPosters());
			values.put(VIDEO_SCORE, info.getScore());
			values.put(VIDEO_HOT, info.getHot());
			values.put(VIDEO_TOTALDRAMAS, info.getTotalDramas());
			values.put(VIDEO_RATING, info.getRatings());
			values.put(VIDEO_VODTYPE, info.getVodtype());
			values.put(VIDEO_DRAMA_CODE, info.getDramaCode());
			values.put(VIDEO_SORT, info.getSort());
			values.put(VIDEO_DRAMA_TIME, info.getPlayTime());	
			db.insert(VIDEO_TABLE, null, values);
			DramaInfoTable.insertDrama(info.getDramaList(), info.getVideoCode(), info.getSort());
		}
		return true;
	}

	/**
	 * @Title: deleteVideo
	 * @Description: 删除记录
	 * @param videoId 视频id
	 * @return: void
	 */
	public static boolean deleteVideo(String videoCode, int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return false;
		}
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		db.delete(VIDEO_TABLE, where, null);
		DramaInfoTable.deleteAllDrama(videoCode, sort);
		return true;
	}	
	
	/**
	 * @Title: existVideo
	 * @Description: 是否存在某个视频被收藏或是追剧
	 * @param videoId
	 * @return 
	 * @return: boolean
	 */
	public static boolean existVideo(String videoCode, int sort)
	{
		boolean nRet = false;
		if (DatabaseManager.mDbHelper == null) 
		{
			return nRet;
		}
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, null);
		if(mCursor != null && mCursor.getCount() > 0)
		{
			nRet =  true;
		}
		else 
		{
			nRet =  false;
		}
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return nRet;
	}
	
	/**
	 * @Title: getPlayHistory
	 * @Description: 取播放历史
	 * @param videoId
	 * @return 
	 * @return: VideoInfo
	 */
	public static VideoInfo getPlayHistory(String videoCode)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		
		VideoInfo info = null;
		Cursor mCursor = null;
		String where = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_CODE + "=\"" + videoCode + "\" " + "AND" + " " + VIDEO_SORT + "=" + SORT_HISTORY;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			int n = mCursor.getCount();
			for (int i = 0; i < n; i++) 
			{
				mCursor.moveToPosition(i);
				info = new VideoInfo();
				info.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
				info.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
				info.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
				info.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
				info.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
				info.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
				info.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
				info.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
				info.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
				info.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
				info.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
				info.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
				info.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
				info.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
				info.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
				info.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
				info.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
				info.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
				info.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
				info.setDramaList(DramaInfoTable.queryAllDramas(videoCode, SORT_HISTORY));
			}
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return info;
	}
	
	/**
	 * @Title: getOldRecord
	 * @Description: 得到最老记录
	 * @param videoId
	 * @param sort
	 * @return
	 * @return: boolean
	 */
	public static VideoInfo getOldRecord(int sort)
	{
		if (DatabaseManager.mDbHelper == null) 
		{
			return null;
		}
		VideoInfo info = null;
		Cursor mCursor = null;
		String where = null;
		String orderBy = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		where = VIDEO_SORT + "=" + sort;
		SQLiteDatabase db = DatabaseManager.mDbHelper.getReadableDatabase();
		qb.setTables(CONTENT_SORT_URI.getPathSegments().get(0));
		orderBy = ID + " ASC";
		mCursor = db.query(VIDEO_TABLE, null, where, null, null, null, orderBy);
		if (mCursor != null) 
		{
			mCursor.setNotificationUri(DatabaseManager.mContext.getContentResolver(),CONTENT_SORT_URI);
			mCursor.moveToPosition(0);
			info = new VideoInfo();
			
			info.setVideoCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_CODE)));
			info.setName(mCursor.getString(mCursor.getColumnIndex(VIDEO_NAME)));
			info.setArea(mCursor.getString(mCursor.getColumnIndex(VIDEO_AREA)));
			info.setType(mCursor.getString(mCursor.getColumnIndex(VIDEO_TYPE)));
			info.setDirector(mCursor.getString(mCursor.getColumnIndex(VIDEO_DIRECTOR)));
			info.setActor(mCursor.getString(mCursor.getColumnIndex(VIDEO_ACTOR)));
			info.setTime(mCursor.getString(mCursor.getColumnIndex(VIDEO_TIME)));
			info.setSummary(mCursor.getString(mCursor.getColumnIndex(VIDEO_SUMMARY)));
			info.setSmallPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_SMALL_PIC)));
			info.setBigPic(mCursor.getString(mCursor.getColumnIndex(VIDEO_BIG_PIC)));
			info.setPosters(mCursor.getString(mCursor.getColumnIndex(VIDEO_POSTER_PIC)));
			info.setScore(mCursor.getString(mCursor.getColumnIndex(VIDEO_SCORE)));
			info.setHot(mCursor.getString(mCursor.getColumnIndex(VIDEO_HOT)));
			info.setTotalDramas(mCursor.getString(mCursor.getColumnIndex(VIDEO_TOTALDRAMAS)));
			info.setRatings(mCursor.getString(mCursor.getColumnIndex(VIDEO_RATING)));
			info.setVodtype(mCursor.getString(mCursor.getColumnIndex(VIDEO_VODTYPE)));
			info.setDramaCode(mCursor.getString(mCursor.getColumnIndex(VIDEO_DRAMA_CODE)));
			info.setSort(mCursor.getInt(mCursor.getColumnIndex(VIDEO_SORT)));
			info.setPlayTime(mCursor.getInt(mCursor.getColumnIndex(VIDEO_DRAMA_TIME)));
			info.setDramaList(DramaInfoTable.queryAllDramas(info.getVideoCode(), sort));
		} 
		
		if (mCursor != null) 
		{
			mCursor.close();
			mCursor = null;
		}
		return info;
	}
}
