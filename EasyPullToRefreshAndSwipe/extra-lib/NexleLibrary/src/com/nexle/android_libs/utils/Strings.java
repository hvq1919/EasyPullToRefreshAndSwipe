package com.nexle.android_libs.utils;

import java.util.Collection;
import java.util.regex.Pattern;

public class Strings {
	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public static <T> String join(Collection<T> coll, String separator, String terminator) {
		return join(coll.toArray(new Object[coll.size()]), separator, terminator);
	}

	public static String join(Object[] arr, String separator, String terminator) {
		StringBuilder sb = new StringBuilder(arr.length * 2);
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i < arr.length - 1) {
				sb.append(separator);
			} else if (terminator != null && arr.length > 0) {
				sb.append(terminator);
			}
		}
		return sb.toString();
	}

	public static boolean checkEmail(String email) {
		Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	
	public static String convertFName(final String string) {
		assert string != null;
		final int i = string.lastIndexOf(' ');
		if (i == -1)
			return string;
		String first = string.substring(0, i);
		if (first.length() > 1)
			first = first.substring(0, 1).toUpperCase();
		String last = string.substring(i + 1).toUpperCase();
		return first + ". " + last;
	}
}