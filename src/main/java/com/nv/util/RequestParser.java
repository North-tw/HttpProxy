package com.nv.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import com.nv.expandUtil.exception.ParameterNotFoundException;
import com.nv.expandUtil.exception.ParameterNumberFormatException;

public class RequestParser {

	// 這邊取18長度避免超過long的範圍值
	public static final int MAX_LONG_LENGTH = 18;
	// 檢查XSS攻擊的最小長度
	private static int MIN_XSS_LENGTH = 20;

	public static Function<String, String> replaceComma = (s) -> {
		return s.trim().replace(",", "");
	};

	public static boolean getBooleanParameter(String value)
		throws ParameterNotFoundException, NumberFormatException {
		if (value.equalsIgnoreCase("1") || (value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("on"))
			|| (value.equalsIgnoreCase("yes"))) {
			return true;
		} else if (value.equalsIgnoreCase("0") || (value.equalsIgnoreCase("false"))
			|| (value.equalsIgnoreCase("off")) || (value.equalsIgnoreCase("no"))) {
			return false;
		} else {
			throw new ParameterNumberFormatException(" value " + value + " is not a boolean");
		}
	}

	public static boolean getBooleanParameter(ServletRequest request, String name)
		throws ParameterNotFoundException, NumberFormatException {
		String value = getStringParameter(request, 5, name).toLowerCase();
		return getBooleanParameter(value);
	}

	public static boolean getBooleanParameter(ServletRequest request, String name, boolean def) {
		try {
			return getBooleanParameter(request, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static int getIntParameter(String value) throws NumberFormatException, ParameterNotFoundException {
		return Integer.parseInt(value);
	}

	public static int getIntParameter(ServletRequest request, String name)
		throws NumberFormatException, ParameterNotFoundException {
		try {
			return Integer.parseInt(getPreStringParameter(request, name).trim().replace(",", ""));
		} catch (NumberFormatException e) {
			throw new ParameterNumberFormatException(name + " is incorrect");
		}
	}

	public static int getIntParameter(ServletRequest request, int maxLength, String name, int def) {
		try {
			return Integer.parseInt(getStringParameter(request, maxLength, name).trim().replace(",", ""));
		} catch (Exception e) {
			return def;
		}
	}

	public static int getIntParameter(ServletRequest request, int maxLength, String name) {
		return Integer.parseInt(getStringParameter(request, maxLength, name).trim().replace(",", ""));
	}

	public static int getIntParameter(ServletRequest request, String name, Supplier<Integer> ifNull) {
		try {
			return Integer.parseInt(getPreStringParameter(request, name).trim().replace(",", ""));
		} catch (Exception e) {
			Integer result = ifNull.get();
			if (result == null) {
				throw e;
			}
			return result;
		}
	}

	public static int getIntParameter(ServletRequest request, String name, int def) {
		try {
			return Integer.parseInt(getPreStringParameter(request, name).trim().replace(",", ""));
		} catch (Exception e) {
			return def;
		}
	}

	public static int getIntParameter(ServletRequest request, Function<String, String> preprocessFunction,
		String name) {
		// 如果有指定前置處理函數，則使用前置處理函數，否則呼叫預設的取代逗號
		Function<String, Integer> function = (preprocessFunction == null ? replaceComma : preprocessFunction)
			.andThen(Integer::parseInt);
		return function.apply(getPreStringParameter(request, name));
	}

	public static int[] getIntParameterValues(ServletRequest request, String name)
		throws ParameterNotFoundException {

		String[] values = request.getParameterValues(name);
		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			int[] temp = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				try {
					temp[i] = Integer.parseInt(values[i]);
				} catch (Exception e) {
				}
			}
			return temp;

		}
	}

	public static int[] getIntParameterValues(ServletRequest request, String name, int[] def)
		throws ParameterNotFoundException {
		try {
			return getIntParameterValues(request, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static int[] getIntParameterValues(ServletRequest request, Pattern splitPattern, String name)
		throws ParameterNotFoundException {
		String value = getPreStringParameter(request, name);

		if (value == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (value.length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			return splitPattern.splitAsStream(value).mapToInt(Integer::parseInt).toArray();
		}
	}

	public static int[] getIntParameterValues(ServletRequest request, Pattern splitPattern, String name,
		int[] def) throws ParameterNotFoundException {
		try {
			return getIntParameterValues(request, splitPattern, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static int[] getIntParametersByJson(ServletRequest request, String name) {
		return JSONUtils.jsonToInstance(getPreStringParameter(request, name), int[].class);
	}

	public static int[] getIntParametersByJson(ServletRequest request, String name, int[] def) {
		try {
			return JSONUtils.jsonToInstance(getPreStringParameter(request, name), int[].class);
		} catch (Exception e) {
			return def;
		}
	}

	public static String[] getStringParametersByJson(ServletRequest request, String name) {
		return JSONUtils.jsonToInstance(getPreStringParameter(request, name), String[].class);
	}

	public static String[] getStringParametersByJson(ServletRequest request, String name, String[] def) {
		try {
			return JSONUtils.jsonToInstance(getPreStringParameter(request, name), String[].class);
		} catch (Exception e) {
			return def;
		}
	}

	public static long getLongParameter(ServletRequest request, String name)
		throws ParameterNotFoundException, NumberFormatException {
		try {
			return Long.parseLong(getStringParameter(request, MAX_LONG_LENGTH, name).replace(",", ""));
		} catch (NumberFormatException e) {
			throw new ParameterNumberFormatException(name + " is incorrect");
		}
	}

	public static long getLongParameter(ServletRequest request, String name, long def) {
		try {
			return getLongParameter(request, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static long getLongParameter(ServletRequest request, String name, Supplier<Long> ifNull) {
		try {
			return Long.parseLong(getStringParameter(request, MAX_LONG_LENGTH, name).trim().replace(",", ""));
		} catch (Exception e) {
			Long result = ifNull.get();
			if (result == null) {
				throw e;
			}
			return result;
		}
	}

	public static long getLongParameter(ServletRequest request, Function<String, String> preprocessFunction,
		String name) {
		// 如果有指定前置處理函數，則使用前置處理函數，否則呼叫預設的取代逗號
		Function<String, Long> function = (preprocessFunction == null ? replaceComma : preprocessFunction)
			.andThen(Long::parseLong);
		return function.apply(getStringParameter(request, MAX_LONG_LENGTH, name));
	}

	public static long[] getLongParameterValues(ServletRequest request, String name)
		throws ParameterNotFoundException {

		String[] values = request.getParameterValues(name);
		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			long[] temp = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				try {
					temp[i] = Long.parseLong(values[i]);
				} catch (Exception e) {
				}
			}
			return temp;

		}
	}

	public static long[] getLongParameterValues(ServletRequest request, String name, long[] def)
		throws ParameterNotFoundException {
		try {
			return getLongParameterValues(request, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static long[] getLongParameterValues(ServletRequest request, Pattern splitPattern, String name)
		throws ParameterNotFoundException {
		String value = getPreStringParameter(request, name);

		if (value == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (value.length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			return splitPattern.splitAsStream(value).mapToLong(Long::parseLong).toArray();
		}
	}

	public static long[] getLongParameterValues(ServletRequest request, Pattern splitPattern, String name,
		long[] def) throws ParameterNotFoundException {
		try {
			return getLongParameterValues(request, splitPattern, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static long[] getLongParametersByJson(ServletRequest request, String name) {
		return JSONUtils.jsonToInstance(getPreStringParameter(request, name), long[].class);
	}

	public static long[] getLongParametersByJson(ServletRequest request, String name, long[] def) {
		try {
			return JSONUtils.jsonToInstance(getPreStringParameter(request, name), long[].class);
		} catch (Exception e) {
			return def;
		}
	}

	public static double getDoubleParameter(ServletRequest request, String name)
		throws ParameterNotFoundException, NumberFormatException {
		try {
			return Double.parseDouble(getPreStringParameter(request, name).replace(",", ""));
		} catch (NumberFormatException e) {
			throw new ParameterNumberFormatException(name + " is incorrect.");
		}
	}

	public static double getDoubleParameter(ServletRequest request, String name, double def) {
		try {
			return getDoubleParameter(request, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static double getDoubleParameter(ServletRequest request, String name, Supplier<Double> ifNull) {
		try {
			return Double.parseDouble(getPreStringParameter(request, name).trim().replace(",", ""));
		} catch (Exception e) {
			Double result = ifNull.get();
			if (result == null) {
				throw e;
			}
			return result;
		}
	}

	public static double getDoubleParameter(ServletRequest request,
		Function<String, String> preprocessFunction, String name) {
		// 如果有指定前置處理函數，則使用前置處理函數，否則呼叫預設的取代逗號
		Function<String, Double> function = (preprocessFunction == null ? replaceComma : preprocessFunction)
			.andThen(Double::parseDouble);
		return function.apply(getPreStringParameter(request, name));
	}

	public static String getStringParameter(ServletRequest request, int maxLength, String name)
		throws ParameterNotFoundException {
		String[] values = request.getParameterValues(name);

		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		}
		if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		}

		String result = values[0];
		if (result.length() > maxLength) {
			result = result.substring(0, maxLength);
		}

		if (result.length() > MIN_XSS_LENGTH) {
			result = ValidatorUtils.stripXSS(result);
		}
		return result;
	}

	/**
	 * 僅為private，提供其他類型Parser使用
	 */
	private static String getPreStringParameter(ServletRequest request, String name)
		throws ParameterNotFoundException {
		String[] values = request.getParameterValues(name);

		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			return values[0];
		}
	}

	public static String getStringParameter(ServletRequest request, int maxLength, String name, String def) {
		try {
			return getStringParameter(request, maxLength, name);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 
	 * @param request
	 * @param maxLength
	 *                      每個參數允許的最大長度
	 * @param name
	 * @return
	 * @throws ParameterNotFoundException
	 */
	public static String[] getStringParameterValues(ServletRequest request, int maxLength, String name)
		throws ParameterNotFoundException {

		String[] values = request.getParameterValues(name);
		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {

			for (int i = 0; i < values.length; i++) {
				if (values[i].length() > maxLength) {
					values[i] = values[i].substring(0, maxLength);
				}

				if (values[i].length() > MIN_XSS_LENGTH) {
					values[i] = ValidatorUtils.stripXSS(values[i]);
				}
			}

			return values;
		}
	}

	public static String[] getStringParameterValues(ServletRequest request, int maxLength, String name,
		String[] def) {
		try {
			return getStringParameterValues(request, maxLength, name);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 
	 * @param request
	 * @param splitPattern
	 * @param maxLength
	 *                         切割後，單一元素的最大長度
	 * @param name
	 * @return
	 * @throws ParameterNotFoundException
	 */
	public static String[] getStringParameterValues(ServletRequest request, Pattern splitPattern,
		int maxLength, String name) throws ParameterNotFoundException {
		String value = getPreStringParameter(request, name);

		if (value == null) {
			throw new ParameterNotFoundException(name + " not found");
		} else if (value.length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		} else {
			return splitPattern.splitAsStream(value)
				.map(s -> (s.length() > maxLength ? s.substring(0, maxLength) : s)).toArray(String[]::new);
		}
	}

	/**
	 * 
	 * @param request
	 * @param splitPattern
	 * @param maxLength
	 *                         切割後，單一元素的最大長度
	 * @param name
	 * @param def
	 * @return
	 * @throws ParameterNotFoundException
	 */
	public static String[] getStringParameterValues(ServletRequest request, Pattern splitPattern,
		int maxLength, String name, String[] def) throws ParameterNotFoundException {
		try {
			return getStringParameterValues(request, splitPattern, maxLength, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static <T> T getJsonParameter(ServletRequest request, Class<T> requiredType, String name)
		throws ParameterNotFoundException {
		String[] values = request.getParameterValues(name);

		if (values == null) {
			throw new ParameterNotFoundException(name + " not found");
		}
		if (values[0].length() == 0) {
			throw new ParameterNotFoundException(name + " was empty");
		}

		String result = values[0];
		return JSONUtils.jsonToInstance(result.getBytes(), requiredType);
	}

	public static <T> T getJsonParameter(ServletRequest request, Class<T> requiredType, String name, T def) {
		try {
			return getJsonParameter(request, requiredType, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static Object[][] getJsonObjectArrayParameter(ServletRequest request, String name,
		Object[][] def) {
		try {
			return getJsonParameter(request, Object[][].class, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static <T> List<T> getJsonObjectListParameter(ServletRequest request, String name,
		Class<T> clazz) {
		try {
			String[] values = request.getParameterValues(name);

			if (values == null) {
				throw new ParameterNotFoundException(name + " not found");
			}
			if (values[0].length() == 0) {
				throw new ParameterNotFoundException(name + " was empty");
			}

			String result = values[0];

			return JSONUtils.parseJsonToInstanceList(result, clazz);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public static <K, V> Map<K, V> getJsonMapParameter(ServletRequest request, String name, Class<K> key,
		Class<V> value) {
		try {
			String[] values = request.getParameterValues(name);

			if (values == null) {
				throw new ParameterNotFoundException(name + " not found");
			}
			if (values[0].length() == 0) {
				throw new ParameterNotFoundException(name + " was empty");
			}

			String result = values[0];

			return JSONUtils.jsonToInstance(result, key, value);
		} catch (Exception e) {
			return Collections.emptyMap();
		}
	}

	/**
	 * Date已經慢慢被棄置，但為了相容還是暫時保留，可以的話請儘量使用LocalDateTime
	 * 
	 * @param request
	 * @param pattern
	 * @param name
	 * @param def
	 * @return
	 * @throws ParameterNotFoundException
	 */
	public static Date getDateParameter(ServletRequest request, String pattern, String name, Date def) {
		try {
			return getDateParameter(request, pattern, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static Date getDateParameter(ServletRequest request, String pattern, String name) {
		try {
			String value = getStringParameter(request, 32, name);
			// Java 8的轉換有問題，java 9有修復，所以現在先用舊的方式
			return ThreadLocalUtils.getSimpleDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new DateTimeException(e.getMessage(), e);
		}
	}

	public static Timestamp getTimestampParameter(ServletRequest request, String pattern, String name,
		Timestamp def) {
		try {
			return getTimestampParameter(request, pattern, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static Timestamp getTimestampParameter(ServletRequest request, String pattern, String name) {
		try {
			String value = getStringParameter(request, 32, name);
			// Java 8的轉換有問題，java 9有修復，所以現在先用舊的方式
			Date date = ThreadLocalUtils.getSimpleDateFormat(pattern).parse(value);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			throw new DateTimeException(e.getMessage(), e);
		}
	}

	/**
	 * 時間比較難有預設值，所以當沒有值的時候傳回null
	 *
	 * @param request
	 * @param pattern
	 *                    不含有時區資料的pattern
	 * @param name
	 * @return
	 * @throws ParameterNotFoundException
	 */
	public static LocalDateTime getLocalDateTimeParameter(ServletRequest request, String pattern, String name,
		LocalDateTime def) {
		try {
			return getLocalDateTimeParameter(request, pattern, name);
		} catch (Exception e) {
			return def;
		}
	}

	public static LocalDateTime getLocalDateTimeParameter(ServletRequest request, String pattern,
		String name) {
		try {
			String value = getStringParameter(request, 32, name);
			// Java 8的轉換有問題，java 9有修復，所以現在先用舊的方式
			Date date = ThreadLocalUtils.getSimpleDateFormat(pattern).parse(value);
			return DateTimeBuilder.localDateTime(date).toLocalDateTime();
		} catch (ParseException e) {
			throw new DateTimeException(e.getMessage(), e);
		}
	}
}