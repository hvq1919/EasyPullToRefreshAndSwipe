package com.nexle.android_libs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class DateUtils {
	public static final double MS_IN_DAY = 86400000; // 24 * 60 * 60 * 1000
	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

	public static String getDisplayShortTime(final long time) {
		// SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
		// DateFormatSymbols symbols = new DateFormatSymbols();
		// symbols.setAmPmStrings(new String[] { "am", "pm" });
		// sdf.setDateFormatSymbols(symbols);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setTimeZone(TimeZone.getDefault());
		return sdf.format(new Date(time));
	}

	public static String getDisplayShortDate(final long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(new Date(time));
	}

	public static long getTimestampbyTZ(long unix_timestamp) {
		int gmtOffset = TimeZone.getDefault().getRawOffset();
		return (unix_timestamp * 1000L) + gmtOffset;
	}

	public static long convertToServerTime(long unix_timestamp) {
		int gmtOffset = TimeZone.getDefault().getRawOffset();
		Log.e("offset", gmtOffset + "");
		return (unix_timestamp * 1000L) - gmtOffset;
	}

	public static String getDisplayServerShortTime(long unix_timestamp) {
		Date date = new Date(unix_timestamp * (long) 1000L);
		// EEE MMM dd HH:mm:ss zzz yyyy a
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return sdf.format(date);
	}

	public static String getDisplayShortTimeInSeconds(final long totalSecs) {
		return getDisplayShortTime(secondsToTimestamp(totalSecs));
	}

	public static long getTimestampInSeconds(long timestamp) {
		Time dtNow = new Time(Time.getCurrentTimezone());
		dtNow.set(timestamp);
		return (dtNow.hour * 3600) + (dtNow.minute * 60);
	}

	public static long secondsToTimestamp(long totalSecs) {
		if (totalSecs != 0) {
			Time dtNow = new Time(Time.getCurrentTimezone());
			dtNow.set(System.currentTimeMillis());
			dtNow.hour = (int) totalSecs / 3600;
			dtNow.minute = (int) (totalSecs % 3600) / 60;
			totalSecs = dtNow.toMillis(false);
		}
		return totalSecs;
	}

	public static String FormatShortDate(long milisecond, Context context) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy", context
				.getResources().getConfiguration().locale);
		return sdf.format(milisecond);
	}

	public static String FormatTime(long milisecond, Context context) {
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm", context
				.getResources().getConfiguration().locale);
		return stf.format(milisecond);
	}

	public static boolean timeCheckUp(long totalSecs) {
		Time dtNow = new Time(Time.getCurrentTimezone());
		dtNow.set(System.currentTimeMillis());
		int diffInMs = (dtNow.hour * 3600) + (dtNow.minute * 60);
		return totalSecs > diffInMs;
	}

	public static long getMilisecondPeriodTime(String hour, String minute) {
		try {
			int h = Integer.parseInt(hour);
			int m = Integer.parseInt(minute);
			return (h * 60 + m) * 60 * 1000;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param milisecond
	 * @return
	 */
	public static long getStartDate(long milisecond) {
		Time dtNow = new Time(Time.getCurrentTimezone());
		dtNow.set(milisecond);
		long time = (dtNow.hour * 60 + dtNow.minute) * 60 * 1000 + dtNow.second
				* 1000;
		return milisecond - time;
	}

	public static String getTimeAgo(Date date) {
		long days = (new Date().getTime() - date.getTime()) / 86400000;

		if (days == 0) {
			return "Today";
		} else if (days == 1)
			return "Yesterday";
		else
			return days + " days";
	}

	public static String getTimeAgo(long time) {
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + "minutes Ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "an hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + "hrs Ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "yesterday";
		} else {
			return diff / DAY_MILLIS + "days Ago";
		}
	}

	public static String getDisplayNameOfMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
		String month_name = month_date.format(cal.getTime());
		return month_name;
	}

	public static int getNumberOfDayInMonth(int year, int month) {
		int result = 0;
		Calendar cal = new GregorianCalendar(year, month, 1);
		result = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return result;
	}
	
//	public static long getMilisecondByString(String sTime) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//		dtNow.set(timestamp);
//		return (dtNow.hour * 3600) + (dtNow.minute * 60);
//	}

}