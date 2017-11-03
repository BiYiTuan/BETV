/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: HttpEngineManager.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.dataprovider.http
 * @Description: 下载管理器
 * @author: zhaoqy
 * @date: 2014-8-12 下午2:53:17
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.dataprovider.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.inpacket.InPacket;
import com.szgvtv.ead.app.taijietemplates.dataprovider.packet.outpacket.OutPacket;
import android.content.Context;

public class HttpEngineManager 
{
	private final static List<HttpEngine> mEngineList = new ArrayList<HttpEngine>(0);

	private HttpEngineManager() 
	{
	}

	public static void cancelAll() 
	{
		Iterator<HttpEngine> it = mEngineList.iterator();
		while (it.hasNext()) 
		{
			HttpEngine engine = it.next();
			engine.cancel(Boolean.TRUE);
		}
		mEngineList.clear();
	}

	public static HttpEngine getHttpEng(int id) 
	{
		Iterator<HttpEngine> it = mEngineList.iterator();
		while (it.hasNext()) 
		{
			HttpEngine engine = it.next();
			if (engine.mId == id)
			{
				return engine;
			}
		}
		return null;
	}

	/**
	 * Attempts to cancel execution of this task. This attempt will fail if the
	 * task has already completed, already been cancelled, or could not be
	 * cancelled for some other reason. If successful, and this task has not
	 * started when cancel is called, this task should never run. If the task
	 * has already started, then the mayInterruptIfRunning parameter determines
	 * whether the thread executing this task should be interrupted in an
	 * attempt to stop the task.
	 * 
	 * Calling this method will result in onCancelled(Object) being invoked on
	 * the UI thread after doInBackground(Object[]) returns. Calling this method
	 * guarantees that onPostExecute(Object) is never invoked. After invoking
	 * this method, you should check the value returned by isCancelled()
	 * periodically from doInBackground(Object[]) to finish the task as early as
	 * possible.
	 * 
	 * @param mayInterruptIfRunning true if the thread executing this task should be interrupted;
	 *            				  otherwise, in-progress tasks are allowed to complete.
	 * @param id 需要取消的网络请求的id
	 * @return false if the task could not be cancelled, typically because it
	 *         has already completed normally; true otherwise
	 */
	public static boolean cancelById(int id, boolean mayInterruptIfRunning) 
	{
		HttpEngine engine = getHttpEng(id);
		if (engine != null) 
		{
			return engine.cancel(mayInterruptIfRunning);
		}
		return Boolean.FALSE;
	}

	/**
	 * 网络请求结束时会调用此方法清除
	 * @author 李英英 
	 * @param id Http网络请求的唯一id
	 */
	protected static void removerWhenEnd(int id) 
	{
		Iterator<HttpEngine> it = mEngineList.iterator();
		while (it.hasNext()) 
		{
			HttpEngine engine = it.next();
			if (engine.mId == id) 
			{
				it.remove();
				return;
			}
		}
	}

	/**
	 * 创建并执行网络请求
	 * @author zhaoqy
	 * @param out 外发的网络请求包
	 * @param in 收到的网络请求包
	 * @param context Android上下文环境 
	 * @return 网络请求的唯一ID
	 */
	public static int createHttpEngine(OutPacket out, InPacket in, Context context) 
	{
		HttpEngine engine = new HttpEngine(out, in, context);
		mEngineList.add(engine);
		engine.execute();
		return engine.getId();
	}

	@Override
	protected void finalize() throws Throwable 
	{
		if (mEngineList.size() > 0) 
		{
			mEngineList.clear();
		}
		super.finalize();
	}
}
