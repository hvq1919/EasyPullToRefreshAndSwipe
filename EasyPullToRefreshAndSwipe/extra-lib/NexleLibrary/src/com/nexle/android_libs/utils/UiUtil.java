package com.nexle.android_libs.utils;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Utilities for generating view hierarchies without using resources.
 */
public abstract class UiUtil {
	public static final String TAG = "UiUtil";

	public static int dipToPixels(Context ctx, float dip) {
		Resources r = ctx.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}

	public static int ptToPx2(Context ctx, float ptValue) {
		final float dpValue = ptValue * 0.666666666f;
		final float scale = ctx.getResources().getDisplayMetrics().density;
		return Math.round(dpValue * scale);
	}

	public static void limitCharsInText(TextView tv, int maxChar) {
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(maxChar);
		tv.setFilters(filterArray);
	}

	public static void forcusView(View edittext) {
		edittext.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
				MotionEvent.ACTION_DOWN, 0, 0, 0));
		edittext.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
				MotionEvent.ACTION_UP, 0, 0, 0));
	}

	public static View getActionBarView(Activity activity) {
		Window window = activity.getWindow();
		View v = window.getDecorView();
		int resId = activity.getResources().getIdentifier("action_bar_container", "id", "android");
		return v.findViewById(resId);
	}

	public static View getTopRoot(View child) {
		if (child.getParent() == null) {
			return child;
		}
		return getTopRoot((View) child.getParent());
	}

	public static void incrsHitRec(final View view, final int expand) {
		final View parent = (View) view.getParent();
		if (parent == null)
			return;
		parent.post(new Runnable() {

			@Override
			public void run() {
				final Rect rect = new Rect();
				view.getHitRect(rect);
				rect.top -= expand; // increase top hit area
				rect.left -= expand; // increase left hit area
				rect.bottom += expand; // increase bottom hit area
				rect.right += expand; // increase right hit area
				parent.setTouchDelegate(new TouchDelegate(rect, view));
			}
		});
	}

	public static int ptToPx(Context ctx, float ptValue) {
		final float scale = ctx.getResources().getDisplayMetrics().density;
		final float dpValue = ptValue * scale;
		return Math.round(dpValue);
	}

	public static void setCustomFont(View textViewOrButton, Context ctx, AttributeSet attrs, int[] attributeSet, int fontId) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, attributeSet);
		String customFont = a.getString(fontId);
		setCustomFont(textViewOrButton, ctx, customFont);
		a.recycle();
	}

	public static boolean setCustomFont(View textViewOrButton, Context ctx, String asset) {
		if (TextUtils.isEmpty(asset))
			return false;
		Typeface tf = null;
		try {
			tf = getFont(ctx, asset);
			if (textViewOrButton instanceof TextView) {
				((TextView) textViewOrButton).setTypeface(tf);
			} else {
				((Button) textViewOrButton).setTypeface(tf);
			}
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + asset, e);
			return false;
		}

		return true;
	}

	public static void setCustomFontSize(View textViewOrButton, Context ctx, AttributeSet attrs, int[] attributeSet,
			int fontSize) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, attributeSet);
		float fontSizePt = a.getFloat(fontSize, 0.0f);
		if (fontSizePt != 0) {
			if (textViewOrButton instanceof TextView) {
				int fontSizePx = ptToPx(ctx, fontSizePt);
				((TextView) textViewOrButton).setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizePx);
			}
		}
		a.recycle();
	}

	private static final Hashtable<String, SoftReference<Typeface>> fontCache = new Hashtable<String, SoftReference<Typeface>>();

	public static Typeface getFont(Context c, String name) {
		synchronized (fontCache) {
			if (fontCache.get(name) != null) {
				SoftReference<Typeface> ref = fontCache.get(name);
				if (ref.get() != null) {
					return ref.get();
				}
			}

			Typeface typeface = Typeface.createFromAsset(c.getAssets(), "fonts/" + name);
			fontCache.put(name, new SoftReference<Typeface>(typeface));

			return typeface;
		}
	}

	public static Bitmap decodeBitmap(String path, int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSize(maxWidth, maxHeight, options.outWidth, options.outHeight);
		return BitmapFactory.decodeFile(path, options);
	}

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

	public static double getDPFromPixels(Activity context, double pixels) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			pixels = pixels * 0.75;
			break;

		case DisplayMetrics.DENSITY_MEDIUM:
			pixels = pixels * 1;
			break;

		case DisplayMetrics.DENSITY_HIGH:
			pixels = pixels * 1.5;
			break;
		}
		return pixels;
	}

	public static int getActionBarHeight(Context ctx) {
		TypedValue tv = new TypedValue();
		int actionBarHeight = 0;
		if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, ctx.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	/**
	 * Get content view of an activity(this is the root view of any layout) This
	 * method should call after activity view created(onWindowFocusChanged)
	 * 
	 * @param context
	 *            app context
	 * @return root view
	 */
	public static View getContentView(Activity mActivity) {
		Rect rect = new Rect();
		Window window = mActivity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		return window.findViewById(Window.ID_ANDROID_CONTENT);
	}

	@SuppressLint("NewApi")
	public static boolean hasSoftKey(Context ctx) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return !ViewConfiguration.get(ctx).hasPermanentMenuKey();
		}
		return false;
	}

	public static float getScreenDensity() {
		return Resources.getSystem().getDisplayMetrics().density;
	}

	public static int sp2px(float value) {
		Resources r = Resources.getSystem();
		float spvalue = value * r.getDisplayMetrics().scaledDensity;
		return (int) (spvalue + 0.5f);
	}

	public static int px2sp(float value, Context context) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (value / scale + 0.5f);
	}

	public static void ShowKeyBoard(Context context, EditText edittext) throws Exception {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edittext,
				InputMethodManager.SHOW_FORCED);
	}

	public static void ShowKeyBoard(Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public static void HideKeyBoard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}
	public static void HideKeyBoard(Activity activity,EditText text) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(text.getWindowToken(), 0);
		}
	}

	public static boolean isKeyboardVisible(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {
			return true;
		} else {
			return false;
		}
	}

	public static ArrayList<View> getAllChildView(View parent) {
		ArrayList<View> list = new ArrayList<View>();
		if (parent == null)
			return null;
		if (!(parent instanceof ViewGroup)) {
			list.add(parent);
			return list;
		} else if (((ViewGroup) parent).getChildCount() == 0) {
			list.add(parent);
			return list;
		} else {
			ViewGroup group = (ViewGroup) parent;
			for (int i = 0; i < group.getChildCount(); i++) {
				View v = group.getChildAt(i);
				ArrayList<View> temp = getAllChildView(v);
				if (temp != null)
					list.addAll(temp);
			}
		}
		return list;
	}

	public static String getDate(long timeStamp) {
		try {
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date netDate = (new Date(timeStamp));
			return sdf.format(netDate);
		} catch (Exception ex) {
			return "xx";
		}
	}

	public static String getDate(String timeStamp, DateFormat format) {
		try {
			Date netDate = (new Date(Long.parseLong(timeStamp)));
			return format.format(netDate);
		} catch (Exception ex) {
			return " ";
		}
	}

	public static void LoadImage(ImageView iv, String url) {
		if (TextUtils.isEmpty(url) || iv == null)
			return;

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(url, iv, options);
		// imageLoader.displayImage(url, iv, new SimpleImageLoadingListener());
	}

	public interface HandleIamgeLoaded {
		void loadingSuccess(String url, View arg1, Bitmap bmp);

		void loadingFail(String url, View arg1, FailReason arg2);
	}

	public static void loadImageBitmap(String url, final HandleIamgeLoaded handle) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.loadImage(url, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
			}

			@Override
			public void onLoadingFailed(String url, View arg1, FailReason arg2) {
				if (handle != null) {
					handle.loadingFail(url, arg1, arg2);
				}
			}

			@Override
			public void onLoadingComplete(String url, View arg1, Bitmap bmp) {
				if (handle != null) {
					handle.loadingSuccess(url, arg1, bmp);
				}

				if (bmp != null) {
					bmp.recycle();
				}
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

	public static void disableEnableControls(boolean enable, ViewGroup vg) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child = vg.getChildAt(i);
			if (child instanceof ViewGroup) {
				disableEnableControls(enable, (ViewGroup) child);
			} else {
				child.setEnabled(enable);
			}
		}
	}

	public static final int dpToPx(float dp, Resources res) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public static final FrameLayout.LayoutParams createLayoutParams(int width, int height) {
		return new FrameLayout.LayoutParams(width, height);
	}

	public static final FrameLayout.LayoutParams createMatchParams() {
		return createLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	public static final FrameLayout.LayoutParams createWrapParams() {
		return createLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public static final FrameLayout.LayoutParams createWrapMatchParams() {
		return createLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	public static final FrameLayout.LayoutParams createMatchWrapParams() {
		return createLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}
}