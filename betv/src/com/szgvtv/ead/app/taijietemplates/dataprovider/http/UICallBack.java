/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: UICallBack.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.http
 * @Description: UI层通知回调接口类
 * @author: zhaoqy
 * @date: 2014-8-12 下午2:55:19
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.http;

import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;

public interface UICallBack 
{
	/**
	 * 当任务取消时被调用,在主线程中
	 * @author zhaoqy
	 * @param out 外发的数据包
	 * @param token HTTP请求的标识，由调用者传入
	 */
	public void onCancel(OutPacket out, int token);

	/**
	 * 请求成功时调用
	 * @author zhaoqy
	 * @param in 请求成功后，解析的网络数据结构,因为有不同的数据结构,所以以Object形式出现,用时应强制转换(InPacket in)
	 * @param token
	 */
	public void onSuccessful(Object in, int token);  

	/**
	 * 请求失败时调用
	 * @author zhaoqy
	 * @param responseCode Http请求的返回码, 如200, 404等
	 * @param errorDesc 错误描述
	 * @param out 外发的数据包
	 * @param token HTTP请求的标识，由调用者传入
	 */
	public void onNetError(int responseCode, String errorDesc, OutPacket out, int token);
}
