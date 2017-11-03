/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: PlayUriRet.java
 * @Prject: AndroidPlayer
 * @Package: com.bananatv.custom.player
 * @Description: 播放器回调错误码查询，仅限ppp的视频
 * @author: lijungang 
 * @date: 2014-1-24 下午4:03:49
 */
package com.bananatv.custom.player;

public class PlayUriRet {
	public static final int EC_UNKNOWN_ERROR            =-1;/*-1*///unknown error
	public static final int EC_AUTH_SYS_BUSY            =0x1001;/*4097*//*auth service too busy*/
	public static final int EC_AUTH_NET_BUSY            =0x1002;/*4098*//*commnunicated fail with tracker service*/
	public static final int EC_AUTH_BAD_DATA            =0x1011;/*4113*//*invalid auth-data*/
	public static final int EC_AUTH_FORBIDDEN           =0x1012;/*4114*//*user is not allow*/
	public static final int EC_AUTH_VALID_FAIL          =0x1013;/*4115*//*bad user-name or password*/
	public static final int EC_AUTH_INVALID_CHN         =0x1014;/*4116*//*channel not exists*/
	public static final int EC_AUTH_INVALID_MAN         =0x1015;/*4117*//*invalid manufactor*/
	public static final int EC_AUTH_INVALID_SID         =0x1016;/*4118*//*invalid system id*/
	public static final int EC_AUTH_SEND_FAIL           =0x1017;/*4119*//*send message fail*/
	public static final int EC_AUTH_RECV_FAIL           =0x1018;/*4120*//*recv message fail*/
	public static final int EC_AUTH_INVALID_TS          =0x1019;/*4121*//*invalid timestamp*/
	public static final int EC_AUTH_INVALID_LEN         =0x101A;/*4122*//*invalid response length*/
	public static final int EC_AUTH_INVALID_MSGID       =0x101B;/*4123*//*invalid response message id*/
	public static final int EC_AUTH_INVALID_SEQ         =0x101C;/*4124*//*invalid response message seq*/
	public static final int EC_AUTH_EMPTY_TRACKER       =0x101D;/*4125*//*invalid response message seq*/
	public static final int EC_AUTH_CRYPTO              =0x1101;/*4353*//*encrypto or decrypto fail*/
	public static final int EC_AUTH_CONNECT             =0x1102;/*4354*//*connect to auth service fail*/
	public static final int EC_AUTH_NETWORK             =0x1103;/*4355*//*network io fail*/
	public static final int EC_INIT_EMPTY_AUTH          =0x1200;/*4608*//*no auth_server get from addr_server*/
	public static final int EC_SYS_NOT_INIT             =0x2001;/*8193*//*init was not called*/
	public static final int EC_SYS_NO_MEM               =0x2002;/*8194*//*out of memory*/
	public static final int EC_SYS_FAIL_UDP             =0x2003;/*8195*//*fail to startup udp transfer*/
	public static final int EC_SYS_TIMESYNC             =0x2004;/*8196*//*timesync fail with tracker*/
	public static final int EC_SYS_FETCH_CH_INFO        =0x2005;/*8197*//*fetch channel info fail from tracker*/
	public static final int EC_SYS_NO_MEM_BLK           =0x2006;/*8198*//*no more memory block*/
	public static final int EC_SYS_INVALID_CACHE_FILE   =0x2007;/*8199*//*cache file exists,but it is not validate*/
	public static final int EC_SYS_CACHE_MEM_TOO_SMALL  =0x2008;/*8200*//*memory cache is too small*/
	public static final int EC_SYS_CACHE_INIT           =0x2009;/*8201*//*memory cache init fail*/
	public static final int EC_SYS_CACHE_NOT_COMPLETE   =0x200A;/*8202*//**/
	public static final int EC_SYS_MEM_EXCEPTION        =0x200B;/*8203*//*an exception was throw for memory alloc*/
	public static final int EC_SYS_CHANNEL_REMOVED      =0x200C;/*8204*//*channel was removed when adding*/
	public static final int EC_SYS_DISK_CACHE_FAIL      =0x200D;/*8205*//*allocate disk cache fail,maybe out of disk-space*/
	public static final int EC_SYS_CREATE_CACHE_FAIL    =0x200E;
    public static final int EC_SYS_OUT_OF_DISK_SPACE    =0x200F;
    public static final int EC_SYS_CREATE_SOCKET_FAIL   =0x2010;
}
