/*
 Project name: GAYM
 File name   : NetworkUtil.java
 
 Author      : Cuong Nguyen(cuongnm@nexlesoft.com)
 Created date: 6/09/2014
 Version     : 1.00
 --------------------------------------------------------------
 Copyright (C) 2014 GAYM. All Rights Reserved.
 --------------------------------------------------------------
 */
package com.nexle.android_libs.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.widget.Toast;

import com.nexle.android_libs.R;

/*
 * Check device's network connectivity and speed 
 */
public class NetworkUtil {
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkUtil.TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressWarnings("deprecation")
	public static boolean isGpsEnabled(Context context) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			String providers = Secure.getString(context.getContentResolver(), Secure.LOCATION_PROVIDERS_ALLOWED);
			if (TextUtils.isEmpty(providers)) {
				return false;
			}
			return providers.contains(LocationManager.GPS_PROVIDER);
		} else {
			final int locationMode;
			try {
				locationMode = Secure.getInt(context.getContentResolver(), Secure.LOCATION_MODE);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			switch (locationMode) {
			case Secure.LOCATION_MODE_BATTERY_SAVING:
			case Secure.LOCATION_MODE_HIGH_ACCURACY:
			case Secure.LOCATION_MODE_SENSORS_ONLY:
				return true;

			case Secure.LOCATION_MODE_OFF:
			default:
				return false;
			}
		}
	}

	/**
	 * @description: check network is connect or not
	 * @param: No parameter
	 * @exception: Any exception
	 * @return: No return value.
	 * @author duyphh
	 */
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	/**
	 * This snippet allows UI on main thread. Normally it's 2 lines but since
	 * we're supporting 2.x, we need to reflect.
	 */
	public static void disableStrictMode() {
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Class<?> strictModeClass = Class.forName("android.os.StrictMode", true, classLoader);
			Class<?> threadPolicyClass = Class.forName("android.os.StrictMode$ThreadPolicy", true, classLoader);
			Class<?> threadBuilderClass = Class.forName("android.os.StrictMode$ThreadPolicy$Builder", true, classLoader);

			Method setThreadPolicyMethod = strictModeClass.getMethod("setThreadPolicy", threadPolicyClass);
			Method detectAllMethod = threadBuilderClass.getMethod("detectAll");
			Method penaltyMethod = threadBuilderClass.getMethod("penaltyLog");
			Method buildMethod = threadBuilderClass.getMethod("build");

			Constructor<?> threadPolicyBuilderConstructor = threadBuilderClass.getConstructor();
			Object threadPolicyBuilderObject = threadPolicyBuilderConstructor.newInstance();

			Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);
			obj = penaltyMethod.invoke(obj);
			Object threadPolicyObject = buildMethod.invoke(obj);
			setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
