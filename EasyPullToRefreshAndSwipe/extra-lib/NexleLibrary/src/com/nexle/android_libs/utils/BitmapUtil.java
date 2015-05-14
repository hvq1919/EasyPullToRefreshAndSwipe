package com.nexle.android_libs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.view.View;

public class BitmapUtil {
	public static final String TAG = BitmapUtil.class.getSimpleName();

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}

	public static Bitmap drawViewToBitmap(Bitmap dest, View view, int downSampling) {
		float scale = 1f / downSampling;
		int viewWidth = view.getWidth();
		int viewHeight = view.getHeight();
		int bmpWidth = Math.round(viewWidth * scale);
		int bmpHeight = Math.round(viewHeight * scale);

		if (dest == null || dest.getWidth() != bmpWidth || dest.getHeight() != bmpHeight) {
			dest = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		}

		Canvas c = new Canvas(dest);
		if (downSampling > 1) {
			c.scale(scale, scale);
		}

		view.draw(c);
		return dest;
	}

	public static Bitmap crop(Bitmap srcBmp, View canvasView, int downsampling) {
		float scale = 1f / downsampling;
		return Bitmap.createBitmap(srcBmp, (int) Math.floor((canvasView.getX()) * scale),
				(int) Math.floor((canvasView.getY()) * scale), (int) Math.floor((canvasView.getWidth()) * scale),
				(int) Math.floor((canvasView.getHeight()) * scale));
	}

	public static Bitmap getDefaultBitmap(Context context, int id, int color) {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);

		Config config = bm.getConfig();
		int width = bm.getWidth();
		int height = bm.getHeight();

		Bitmap newImage = Bitmap.createBitmap(width, height, config);
		Canvas c = new Canvas(newImage);
		c.drawBitmap(bm, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		c.drawPaint(paint);
		bm.recycle();

		return newImage;
	}
}