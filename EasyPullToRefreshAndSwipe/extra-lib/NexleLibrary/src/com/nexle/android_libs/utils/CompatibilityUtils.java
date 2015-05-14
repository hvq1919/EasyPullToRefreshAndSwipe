/*
 Project name: GAYM
 File name   : CompatibilityUtils.java
 
 Author      : Cuong Nguyen(cuongnm@nexlesoft.com)
 Created date: 7/22/2014
 Version     : 1.00
 --------------------------------------------------------------
 Copyright (C) 2014 GAYM. All Rights Reserved.
 --------------------------------------------------------------
 */
package com.nexle.android_libs.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

public class CompatibilityUtils {
	private CompatibilityUtils() {
	}

	/**
	 * Get the current Android API level.
	 */
	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * Determine if the device is running API level 8 or higher.
	 */
	public static boolean isFroyo() {
		return getSdkVersion() >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Determine if the device is running API level 11 or higher.
	 */
	public static boolean isHoneycomb() {
		return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Determine if the device is a tablet (i.e. it has a large screen).
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Determine if the device is a HoneyComb tablet.
	 * 
	 * @param context
	 *            The calling context.
	 */
	public static boolean isHoneycombTablet(Context context) {
		return isHoneycomb() && isTablet(context);
	}

	/**
	 * Determine if the device is a 7 tablet
	 * 
	 * @param context
	 * @return true if it is >= 6.5 and < 7.6
	 */
	public static boolean is7InchTablet(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();

		float widthInInches = metrics.widthPixels / metrics.xdpi;
		float heightInInches = metrics.heightPixels / metrics.ydpi;

		double sizeInInches = Math.sqrt(Math.pow(widthInInches, 2) + Math.pow(heightInInches, 2));
		return sizeInInches >= 6.5 && sizeInInches < 7.6;
	}
}