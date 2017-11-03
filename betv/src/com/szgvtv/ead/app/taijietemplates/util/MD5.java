/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: MD5.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: MD5值
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:32:08
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5 
{
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  'a', 'b', 'c', 'd', 'e', 'f' };  
	   
	/**
	 * 
	 * @Title: toHexString
	 * @Description: 转化成十六进制
	 * @param b
	 * @return
	 * @return: String
	 */
	public static String toHexString(byte[] b) 
	{  
        StringBuilder sb = new StringBuilder(b.length * 2);  
        for (int i = 0; i < b.length; i++) 
        {  
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
            sb.append(HEX_DIGITS[b[i] & 0x0f]);  
        }  
        return sb.toString();  
    } 
		  
	/**
	 * 
	 * @Title: md5sum
	 * @Description: 获取md5值
	 * @param filename: 文件名(可以包含路径)
	 * @return
	 * @return: String
	 */
	public static String md5sum(String filename) 
	{  
        InputStream fis;  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        MessageDigest md5;  
        
        try
        {  
            fis = new FileInputStream(filename);  
            md5 = MessageDigest.getInstance("MD5");  
            while((numRead=fis.read(buffer)) > 0) 
            {  
                md5.update(buffer,0,numRead);  
            }  
            fis.close();  
            return toHexString(md5.digest());     
        } 
        catch (Exception e) 
        {  
            return null;  
        }  
    }  
}
