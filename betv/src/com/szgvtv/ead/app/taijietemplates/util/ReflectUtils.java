/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ReflectUtils.java
 * @Prject: TaijieTemplates1_gytv
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 映射Util
 * @author: zhaoqy
 * @date: 2015-5-25 上午9:42:07
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import java.lang.reflect.Method;
import android.widget.VideoView;

public class ReflectUtils 
{
	/**
	 * 
	 * @Title: getMethod
	 * @Description: 获取类的特定的public方法
	 * @param clazz
	 * @param name
	 * @param parameterTypes
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		Method method = null;
		try 
		{
			method = clazz.getMethod(name, parameterTypes);
		} 
		catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * 
	 * @Title: existSetParameterMethod
	 * @Description: 是否新的sdkVideoView带setParameter函数
	 * @return
	 * @return: boolean
	 */
	public static boolean existSetParameterMethod()
	{
		boolean nRet = false;
		if(ReflectUtils.getMethod(VideoView.class, "setParameter",int.class)!=null)
		{
			nRet = true;
		}
		return nRet;
	}
}
