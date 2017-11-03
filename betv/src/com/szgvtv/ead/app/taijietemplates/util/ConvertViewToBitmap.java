/**
 * 
 * Copyright © 2014GreatVision. All rights reserved.
 *
 * @Title: ConvertViewToBitmap.java
 * @Prject: TaijieTemplates
 * @Package: com.szgvtv.ead.app.taijietemplates.util
 * @Description: 位图处理
 * @author: zhaoqy
 * @date: 2014-8-11 下午2:27:08
 * @version: V1.0
 */

package com.szgvtv.ead.app.taijietemplates.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.View.MeasureSpec;

public class ConvertViewToBitmap 
{
	/**
	 * 
	 * @Title: convertViewToBitmap
	 * @Description: 将View截图转换成BitMap
	 * @param view 输入view
	 * @return
	 * @return: Bitmap
	 */
	public static Bitmap convertViewToBitmap(View view) 
	{
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		
		if (bitmap == null) 
		{
			return null;
		}
		return bitmap;
	}
	
	/**
	 * 
	 * @Title: createReflection
	 * @Description: 
	 * @param originalImage
	 * @param rate_reflection
	 * @param color0  主要改变透明度，如0x7fffffff，为半透明，定义倒影的起始透明度(上)   
	 * @param color1 主要改变透明度，如0x00ffffff，为完全透明，定义倒影的末端透明度(下)
	 * @return
	 * @return: Bitmap
	 */
	public static Bitmap createReflection(Bitmap originalImage, int height_reflection) 
	{
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflection = Bitmap.createBitmap(originalImage, 0, height - height_reflection, width, height_reflection, matrix, false);
		Bitmap reflectionImage = Bitmap.createBitmap(width, height_reflection, Config.ARGB_8888);
		Canvas canvas = new Canvas(reflectionImage);
		canvas.drawBitmap(reflection, 0, 0, null);
		Paint paint = new Paint();
		//透明度根据自己的情况设置, 该应用的倒影的起始透明度都设置成0x1fffffff
		//LinearGradient shader = new LinearGradient(0, 0, 0, height_reflection, 0x7fffffff, 0x00ffffff, TileMode.CLAMP);
		LinearGradient shader = new LinearGradient(0, 0, 0, height_reflection, 0x1fffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, 0, width, height_reflection, paint);
		return reflectionImage;
	}
	
	/**
	 * 
	 * @Title: createReflection
	 * @Description: 
	 * @param originalImage
	 * @param rate_reflection
	 * @param color0  主要改变透明度，如0x7fffffff，为半透明，定义倒影的起始透明度
	 * @param color1 主要改变透明度，如0x00ffffff，为完全透明，定义倒影的末端透明度
	 * @return
	 * @return: Bitmap
	 */
	public static Bitmap createReflection(Bitmap originalImage, int height_reflection, int color0, int color1) 
	{
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflection = Bitmap.createBitmap(originalImage, 0, height - height_reflection, width, height_reflection, matrix, false);
		Bitmap reflectionImage = Bitmap.createBitmap(width, height_reflection, Config.ARGB_8888);
		Canvas canvas = new Canvas(reflectionImage);
		canvas.drawBitmap(reflection, 0, 0, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, 0, 0, height_reflection, color0, color1, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, 0, width, height_reflection, paint);
		return reflectionImage;
	}
	
	/**
	 * 
	 * @Title: getRoundedCornerBitmap
	 * @Description:  获取圆角位图的方法 
	 * @param bitmap: 需要转化成圆角的位图 
	 * @param roundPx:圆角的度数，数值越大，圆角越大 
	 * @return
	 * @return: Bitmap: 处理后的圆角位图
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) 
	{ 
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
	    Canvas canvas = new Canvas(output); 
	    final int color = 0xff424242; 
	    final Paint paint = new Paint(); 
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
	    final RectF rectF = new RectF(rect); 
	    paint.setAntiAlias(true); 
	    canvas.drawARGB(0, 0, 0, 0); 
	    paint.setColor(color); 
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
	    canvas.drawBitmap(bitmap, rect, rect, paint); 
	    return output; 
	} 
}
