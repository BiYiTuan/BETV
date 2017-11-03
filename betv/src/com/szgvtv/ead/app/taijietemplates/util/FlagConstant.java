/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: FlagConstant.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 标记常量
 * @author: zhaoqy
 * @date: 2014-8-12 下午2:38:03
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import com.szgvtv.ead.app.taijietemplates_betvytv.R;

public class FlagConstant 
{
	public static final String TAG = "taijietemplates_betvytv";   //打印标签
	
	//视频加载失败错误码
	public static final String ERROR_PLAYER = "P";  //播放器
	public static final String ERROR_LIBRARY = "L"; //流媒体库
	public static final String ERROR_TIMEOUT = "T"; //加载超时
	public static final String ERROR_NETWORK = "N"; //网络异常
	
	//问卷调查类型
	public static final int QUESTIONNAIRE_SINGLE_CHOICE = 1; //单选
	public static final int QUESTIONNAIRE_MULTI_CHOICE = 2;  //多选
	public static final int QUESTIONNAIRE_SHORT_ANSWER = 3;  //简答
	
	//播放类型
	public static final int ACTIVITY_PLAY_LIVE = 1;
	public static final int ACTIVITY_PLAY_VIDEO = 2;
	public static final int ACTIVITY_PLAY_BACK = 3;
	
	//鉴权
	public static final int HOME_AUTH_TIMEOUT = 1;           //鉴权超时
	public static final int HOME_STARTUP_FINISH = 2;         //应用启动页完成
	public static final int HOME_REQUEST_VIDEO = 3;          //请求资源详情
	public static final int RECORD_REQUEST_VIDEO = 4;        //请求资源详情
	public static final int HOME_BITMAP = 5;                 //Bitmap生成完成
	public static final int HOME_WANT_EXIT_APP = 6;          //想退出应用
	public static final int AUTH_COUNTDOWN = 7;              //鉴权倒计时
	
	//收藏
	public static final int DETAIL_FAVORITE_FINISH = 1;      //收藏完成
	
	//播放状态
	public static final int PLAY_TIMEOUT = 1;                //加载超时
	public static final int PLAY_ERROR = 2;                  //加载失败
	public static final int PLAY_SET_URL = 3;                //设置URL
	public static final int PLAY_COMPLETE = 4;               //播放完成
	public static final int PLAY_START = 5;                  //开始播放
	public static final int PLAY_SEEKING = 6;                //快进或快退(进行中)
	public static final int PLAY_SEEKED = 7;                 //快进或快退(完成)
	public static final int PLAY_AD_END = 8;                 //广告播放结束
	public static final int PLAY_SWITCH = 9;                 //数字键切换频道
	public static final int PLAY_TASK = 10;                  //开始任务
	public static final int PLAY_RETASK = 11;                //重新开始任务
	public static final int PLAY_START_PPP_PRESERVE = 12;    //开始维护
	public static final int PLAY_COMPLATE_PPP_PRESERVE = 13; //完成维护
	public static final int PLAY_HIDE_PPP_PRESERVE = 14;     //隐藏维护界面
	public static final int PLAY_CHECK_PREVIEW = 15;         //检测频道预告
	public static final int PLAY_REQUEST_CHANNEL = 16;       //取直播频道
	public static final int PLAY_REQUEST_PREVIEW = 17;       //取频道预告
	public static final int PLAY_WANT_EXIT_PLAY = 18;        //想退出播放
	
	//GridView生成标志
	public static final int FINISHED = 2;                    //GridView或ListView生成完成
	
	//刷新详情页面
	public static final int NAVIGATION_FINISHED = 1;         //剧集导航栏生成
	public static final int DRAMA_FINISHED = 2;              //剧集生成
	public static final int SWITCH_FINISHED = 3;             //剧集导航栏切换完成
	public static final int SWITCH_LEFT_FINISHED = 4;        //剧集导航栏切换完成
	public static final int SWITCH_RIGHT_FINISHED = 5;       //剧集导航栏切换完成
	public static final int SWITCH_PAGE_UP_FINISHED = 6;     //剧集导航栏切换完成
	public static final int SWITCH_PAGE_DOWN_FINISHED = 7;   //剧集导航栏切换完成
	
	public static final int SCALE_HOME_BIG_ANIMS = R.anim.scale_home_big_action;
	public static final int SCALE_HOME_SMALL_ANIMS = R.anim.scale_home_small_action;
	public static final int SCALE_SPECIAL_BIG_ANIMS = R.anim.scale_special_big_action;
	public static final int SCALE_SPECIAL_SMALL_ANIMS = R.anim.scale_special_small_action;
	public static final int SCALE_VOD_BIG_ANIMS = R.anim.scale_vod_big_action;
	public static final int SCALE_VOD_SMALL_ANIMS = R.anim.scale_vod_small_action;
	public static final int SCALE_DRAMA_BIG_ANIMS = R.anim.scale_drama_big_action;
	public static final int SCALE_DRAMA_SMALL_ANIMS = R.anim.scale_drama_small_action;
	public static final int SCALE_PLAYBACK_BIG_ANIMS = R.anim.scale_playback_big_action;
	public static final int SCALE_PLAYBACK_SMALL_ANIMS = R.anim.scale_playback_small_action;
	public static final int TRANSLATE_SPECIAL_LEFT_ANIMS = R.anim.translate_special_left;
	public static final int TRANSLATE_SPECIAL_RIGHT_ANIMS = R.anim.translate_special_right;
		
	public static final String ClassifyCodeKey = "classify_code_key";
	public static final String HotWordKey = "hotword_key";
	public static final String ToActivityPlaybackProgramKey = "to_activity_playback_program_key"; 
	public static final String ToActivityPlaybackProgramIndexKey = "to_activity_playback_program_index_key"; 
	public static final String ToActivityLivePlaybacItemkKey = "to_activity_live_playback_item_key"; 
	public static final String ToActivityLiveChannelItemKey = "to_activity_live_channel_item_key"; 
	public static final String ToActivitySpecialVideoKey = "to_activity_special_video_key"; 
	public static final String ToActivityDetailFilmKey = "to_activity_detail_film_key"; 
	public static final String ToActivityDetailVideoKey = "to_activity_detail_video_key"; 
	public static final String ToActivityDetailRecordKey = "to_activity_detail_record_key";  
	public static final String ToActivityDramaTVKey = "to_activity_drama_tv_key"; 
	public static final String ToActivityDramaOtherKey = "to_activity_drama_other_key"; 
	public static final String ToActivityPlayVideoKey = "to_activity_play_video_key"; 
	public static final String ToActivityPlayTimeKey = "to_activity_play_time_key"; 
	public static final String DramaIndexKey = "drama_index_key";
}
