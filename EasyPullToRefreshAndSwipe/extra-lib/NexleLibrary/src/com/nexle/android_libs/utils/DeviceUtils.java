/*
 Project name: GAYM
 File name   : DeviceUtils.java
 
 Author      : Cuong Nguyen(cuongnm@nexlesoft.com)
 Created date: 7/22/2014
 Version     : 1.00
 --------------------------------------------------------------
 Copyright (C) 2014 GAYM. All Rights Reserved.
 --------------------------------------------------------------
 */
package com.nexle.android_libs.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

/**
 * A utility class that has helper methods for device configuration.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class DeviceUtils {
	private static final String TAG = DeviceUtils.class.getSimpleName();

	/**
	 * The minimum width that would classify the device as a tablet.
	 */
	private static final int MINIMUM_TABLET_WIDTH_DP = 600;

	public static long getDeviceId(Context context) {
		String deviceIdStr = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		if (deviceIdStr == null) {
			long rand = Double.doubleToRawLongBits(Math.random() * Long.MAX_VALUE);
			Log.w(TAG, "No device ID found - created random ID " + rand);
			return rand;
		} else {
			return Long.parseLong(deviceIdStr, 16);
		}
	}

	/**
	 * @param context
	 *            Android's context
	 * @return Whether the app is should treat the device as a tablet for
	 *         layout.
	 */
	public static boolean isTablet(Context context) {
		if (isTv(context)) {
			return true;
		}
		int minimumScreenWidthDp = context.getResources().getConfiguration().smallestScreenWidthDp;
		return minimumScreenWidthDp >= MINIMUM_TABLET_WIDTH_DP;
	}

	/**
	 * Checks if the device should be treated as TV. Note that this should be
	 * invoked before {@link #isTablet(Context)} to get the correct result since
	 * they are not orthogonal.
	 * 
	 * @param context
	 *            Android context
	 * @return {@code true} if the device should be treated as TV.
	 */
	public static boolean isTv(Context context) {
		PackageManager manager = context.getPackageManager();
		if (manager != null) {
			return manager.hasSystemFeature(PackageManager.FEATURE_TELEVISION);
		}
		return false;
	}

	public static Point getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics metrics = new DisplayMetrics();
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);

		// since SDK_INT = 1;
		Point size = new Point();
		size.x = metrics.widthPixels;
		size.y = metrics.heightPixels;
		try {
			// used when 17 > SDK_INT >= 14; includes window decorations
			// (statusbar bar/menu bar)
			size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
			size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
		} catch (Exception ignored) {
			// Do nothing
		}
		try {
			// used when SDK_INT >= 17; includes window decorations (statusbar
			// bar/menu bar)
			Point realSize = new Point();
			Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
			size.x = realSize.x;
			size.y = realSize.y;
		} catch (Exception ignored) {
		}
		return size;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static int getNavigationBarHeight(Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	public static boolean hasNavigationBar(Context context) {
		boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if (!hasMenuKey && !hasBackKey) {
			return false;
		}
		return true;
	}

	public static int getHeightContentView(Activity mActivity) {
		Rect rect = new Rect();
		Window window = mActivity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		return window.findViewById(Window.ID_ANDROID_CONTENT).getHeight();
	}
}