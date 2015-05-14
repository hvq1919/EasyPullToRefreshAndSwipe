package com.nexle.android_libs.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * A utility class that has helper methods for device configuration.
 */
public class DeviceAdmin {
	private static Object mAdminName;
	private static DevicePolicyManager mDPM;

	public static void setActiveAdmin(Context context, Class<?> cls) {
		try {
			// Initiate DevicePolicyManager.
			mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			// Set DeviceAdminDemo Receiver for active the component with
			// different option
			mAdminName = new ComponentName(context, cls);

			// if (!mDPM.isAdminActive(mAdminName)) {
			// try to become active
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			// intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
			// mAdminName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			        "Click on Activate button to secure your application.");
			// startActivityForResult(intent, REQUEST_CODE);
			// }
			// else
			{
				// Already is a device administrator, can do security operations
				// now.
				mDPM.lockNow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}