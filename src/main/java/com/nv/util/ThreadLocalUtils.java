package com.nv.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.collections.keyvalue.MultiKey;

public class ThreadLocalUtils {

	// SimpleDateFormat is not thread-safe
	private static ThreadLocal<HashMap<MultiKey, SimpleDateFormat>> results = new ThreadLocal<HashMap<MultiKey, SimpleDateFormat>>() {

		protected HashMap<MultiKey, SimpleDateFormat> initialValue() {
			return new HashMap<MultiKey, SimpleDateFormat>();
		}
	};

	// DecimalFormat is not thread-safe
	private static ThreadLocal<HashMap<String, DecimalFormat>> decimalFormats = new ThreadLocal<HashMap<String, DecimalFormat>>() {

		protected HashMap<String, DecimalFormat> initialValue() {
			return new HashMap<String, DecimalFormat>();
		}
	};

	public static SimpleDateFormat getSimpleDateFormat(String format, Locale locale) {
		MultiKey key = new MultiKey(format, locale);
		HashMap<MultiKey, SimpleDateFormat> hm = results.get();
		SimpleDateFormat obj = hm.get(key);

		if (obj != null) {
			return obj;
		}
		obj = new SimpleDateFormat(format, locale);
		hm.put(key, obj);
		return obj;
	}

	public static SimpleDateFormat getSimpleDateFormat(String format) {
		MultiKey key = new MultiKey(format, Locale.getDefault());
		HashMap<MultiKey, SimpleDateFormat> hm = results.get();
		SimpleDateFormat obj = hm.get(key);

		if (obj != null) {
			return obj;
		}
		obj = new SimpleDateFormat(format);
		hm.put(key, obj);
		return obj;
	}

	public static SimpleDateFormat getSimpleDateFormat(String format, Locale locale, TimeZone tz) {
		MultiKey key = new MultiKey(format, locale, tz);
		HashMap<MultiKey, SimpleDateFormat> hm = results.get();
		SimpleDateFormat obj = hm.get(key);

		if (obj != null) {
			return obj;
		}
		obj = new SimpleDateFormat(format, locale);
		obj.setTimeZone(tz);
		hm.put(key, obj);
		return obj;
	}

	public static SimpleDateFormat getSimpleDateFormat(String format, TimeZone tz) {
		MultiKey key = new MultiKey(format, Locale.getDefault(), tz);
		HashMap<MultiKey, SimpleDateFormat> hm = results.get();
		SimpleDateFormat obj = hm.get(key);

		if (obj != null) {
			return obj;
		}
		obj = new SimpleDateFormat(format);
		obj.setTimeZone(tz);
		hm.put(key, obj);
		return obj;
	}

	public static DecimalFormat getDecimalFormat(String format) {
		HashMap<String, DecimalFormat> hm = decimalFormats.get();
		DecimalFormat obj = hm.get(format);

		if (obj != null) {
			return obj;
		}
		obj = new DecimalFormat(format);
		hm.put(format, obj);
		return obj;
	}
}
