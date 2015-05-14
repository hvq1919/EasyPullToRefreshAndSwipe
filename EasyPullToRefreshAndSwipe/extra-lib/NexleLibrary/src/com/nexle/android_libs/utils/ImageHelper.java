package com.nexle.android_libs.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nexle.android_libs.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageHelper {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * @param bitmap
	 * @return converting bitmap and return a string
	 */
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	/**
	 * @param encodedString
	 * @return bitmap (from given string)
	 */
	public static Bitmap stringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public static Bitmap getBitmapFromFile(Context context, String pPath) {
		FileInputStream in;
		BufferedInputStream buf = null;
		try {
			in = new FileInputStream(pPath);
			buf = new BufferedInputStream(in);
			byte[] bMapArray = new byte[buf.available()];
			buf.read(bMapArray);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Config.ARGB_8888;
			opts.inPurgeable = true;
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			opts.inDensity = dm.densityDpi;
			Bitmap bitmap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length, opts);
			if (in != null) {
				in.close();
			}
			if (buf != null) {
				buf.close();
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap resizeBitmap(Bitmap input, int destWidth, int destHeight) {
		int srcWidth = input.getWidth();
		int srcHeight = input.getHeight();
		boolean needsResize = false;
		float p;
		if (srcWidth > destWidth || srcHeight > destHeight) {
			needsResize = true;
			if (srcWidth > srcHeight && srcWidth > destWidth) {
				p = (float) destWidth / (float) srcWidth;
				destHeight = (int) (srcHeight * p);
			} else {
				p = (float) destHeight / (float) srcHeight;
				destWidth = (int) (srcWidth * p);
			}
		} else {
			destWidth = srcWidth;
			destHeight = srcHeight;
		}
		if (needsResize) {
			Bitmap output = Bitmap.createScaledBitmap(input, destWidth, destHeight, true);
			return output;
		} else {
			return input;
		}
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newWidth, int newHeight) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	public static Bitmap decodeBitmap(String path, int maxWidth, int maxHeight) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth == 0 && options.outHeight == 0)
			throw new FileNotFoundException("Couldn't open " + path);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSize(maxWidth, maxHeight, options.outWidth, options.outHeight);
		return BitmapFactory.decodeFile(path, options);
	}

	// public static Bitmap decodeBitmap(File path, int maxWidth, int maxHeight)
	// {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(path, options);
	// options.inJustDecodeBounds = false;
	// options.inSampleSize = calculateInSampleSize(maxWidth, maxHeight,
	// options.outWidth, options.outHeight);
	// return BitmapFactory.decodeFile(path, options);
	// }

	private static int calculateInSampleSize(int maxWidth, int maxHeight, int width, int height) {
		double widthRatio = (double) width / maxWidth;
		double heightRatio = (double) height / maxHeight;
		double ratio = Math.min(widthRatio, heightRatio);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return (int) n;
	}

	public static int getRotationForImage(String path) {
		int rotation = 0;

		try {
			ExifInterface exif = new ExifInterface(path);
			rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rotation;
	}

	public static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	public Bitmap trimImage(Bitmap source, int height) {
		// int trimY = 80; // Whatever you want to cut off the top
		Bitmap bmOverlay = Bitmap.createBitmap(source.getWidth(), source.getHeight() - height, source.getConfig());
		Canvas c = new Canvas(bmOverlay);

		// Source and destination Rects based on sprite animation.
		Rect srcRect = new Rect(0, height, source.getWidth(), source.getHeight());
		Rect dstRect = new Rect(0, 0, source.getWidth(), source.getHeight());
		c.drawBitmap(source, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

		return bmOverlay;
	}

	public static String getRealPathFromURI1(Context context, Uri contentURI) {
		Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}

	public static String getRealPathFromURI(Context context, Uri contentURI) {
		String filePath = contentURI.toString();
		final String scheme = contentURI.getScheme();
		try {
			if (scheme.compareTo("content") == 0) {
				Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
				if (cursor.moveToFirst()) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					filePath = cursor.getString(column_index);
				}
			} else if (scheme.compareTo("file") == 0)
				filePath = contentURI.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public static String getFileNameByUri(Context context, Uri uri) {
		String fileName = "unknown";// default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// Instead of "MediaStore.Images.Media.DATA" can be used "_data"
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment();
		}
		return fileName;
	}

	public static void loadImageCached(String url, ImageView iv) {
		if (TextUtils.isEmpty(url) || iv == null)
			return;

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(url, iv, options);
	}

	public static void loadImageCachedWithLoading(String url, ImageView iv, SimpleImageLoadingListener listener) {
		if (TextUtils.isEmpty(url) || iv == null)
			return;

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(url, iv, options, listener);
	}
}