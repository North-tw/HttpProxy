package com.nv.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static final Pattern COMMA_PATTERN = Pattern.compile(",");

	public static final Pattern HYPHEN_PATTERN = Pattern.compile("-");

	/**
	 * Mexico-05-GA211230022
	 * 
	 * Mexico,Bavet 爲5位以上的純英文
	 * 05 桌號2位以上，不足補0
	 * GA211230022 GA+shoe+round，shoe+round爲5位以上，round(至多4位，不足補零)
	 */
	public static final Pattern ROUND_ID_PATTERN = Pattern.compile("^[A-Za-z]{5,}-\\d{2,}-GA\\d{5,}");

	public static final String[] EMPTY_STRING_ARRAY = new String[]{};

	public static String replaceChars(String str) {
		String ret = str;
		ret = ret.replace("&", "&amp;"); // must call it in first
		ret = ret.replace("<", "&lt;");
		ret = ret.replace(">", "&gt;");
		ret = ret.replace("\"", "&quot;");
		return ret;
	}

	public static String filter(String input) {
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '<') {
				filtered.append("&lt;");
			} else if (c == '>') {
				filtered.append("&gt;");
			} else if (c == '"') {
				filtered.append("&quot;");
			} else if (c == '“') {
				filtered.append("&quot;");
			} else if (c == '‘') {
				filtered.append("&#39;");
			} else if (c == '\'') {
				filtered.append("&#39;");
			} else if (c == '&') {
				filtered.append("&amp;");
			} else if (c == 10) {
				filtered.append("<br/>");
			} else {
				filtered.append(c);
			}
		}
		return (filtered.toString());
	}

	public static String getSubstring(String originalTxt, int absLength) {
		StringBuffer sb = new StringBuffer();
		int l = 0;
		int i = 0;
		int length = originalTxt.length();
		for (i = 0; i < length; i++) {
			l += countLength(originalTxt.charAt(i));
			if (l > absLength) {
				break;
			}
			sb.append(originalTxt.charAt(i));
		}
		if (i != length) {
			sb.append("...");
		}
		return sb.toString();
	}

	public static String changeCharset(String str, String oldCharset, String newCharset, String isSocket) {
		if (isSocket.equals("Y"))
			return str;
		String rtn = null;
		if (str != null) {
			try {
				byte[] bs = str.getBytes(oldCharset);
				rtn = new String(bs, newCharset);
			} catch (UnsupportedEncodingException e) {
				LogUtils.err.error(e.getMessage(), e);
			}
		}
		return rtn;
	}

	/**
	 * browser type 1:IE browser type 2:firxfox,cgrome,safari,Opera ->need special
	 * process
	 * 
	 * @param transactions
	 * @param request
	 * @return
	 */
	/*
	 * public static String firefoxStringProcess(String transactions,
	 * HttpServletRequest request) { if (transactions == null) return null;
	 * 
	 * String usr_agent = request.getHeader("User-Agent"); if (usr_agent == null)
	 * return null; usr_agent = usr_agent.toLowerCase(); if (Race.isMultiBrowser &&
	 * usr_agent.indexOf("msie") == -1) { transactions = transactions.replace("<",
	 * "{").replace(">", "}");//for firefox process } return transactions;
	 * 
	 * }
	 */

	public static String fillQuestionMark(int count) {
		String rField = "";
		boolean first = true;
		while (count > 0) {
			if (!first)
				rField += ",";
			else
				first = false;
			rField += "?";
			count--;
		}
		return rField;
	}

	public static boolean isNumber(String str) {
		String s = str;
		for (int i = 0; i < s.length(); i++) {
			// If we find a non-digit character we return false.
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}

	public static String removeParenthesis(String _locationName) {// "Macau(Dog)"
																	// convert
																	// to
																	// "Macau"
		if (_locationName != null) {
			int la = _locationName.indexOf("(");
			int lb = _locationName.indexOf(")");
			if (la > -1 && lb > -1 && la < lb) {

				_locationName = _locationName.substring(0, la) + _locationName.substring(lb + 1);
			}
		}
		return _locationName;
	}

	public static String addComma(String strNumber) {
		String retStr = strNumber;
		try {
			DecimalFormat formatter = ThreadLocalUtils.getDecimalFormat("#,###,###,###,###");
			retStr = formatter.format(Double.valueOf(strNumber)); // -1,23
		} catch (Exception e) {
			LogUtils.err.error(e.getMessage(), e);
			retStr = strNumber;
		}
		return retStr;
	}

	public static String notNull(Object obj) {
		return (obj == null ? "" : notNull(obj.toString()));
	}

	/**
	 * 將陣列轉成字串. ex: 陣列:{"1","2","3"}, 間隔符號:逗號(,) => 1,2,3
	 * 
	 * @param str
	 *            陣列
	 * @param sign
	 *            間隔符號
	 * @return
	 */
	public static String toString(String[] str, String sign) {
		if (str == null || sign == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (String s : str) {
			sb.append(sign).append(s);
		}
		sb.delete(0, 1);
		return sb.toString();
	}

	/**
	 * 比對是否在字串內. ex: 來源字串:1,2,3,45,26 要比對的字串:4, 間隔符號:逗號(,) => false
	 * 
	 * @param sourceStr
	 *            來源字串
	 * @param matchStr
	 *            要比對的字串
	 * @param sign
	 *            間隔符號
	 * @return
	 */
	public static boolean matches(String sourceStr, String matchStr, String sign) {
		if (sourceStr == null || sourceStr.trim().length() == 0) {
			return false;
		}
		String pattern = "(\\A||.*[" + sign + "])" + matchStr + "(\\z||[" + sign + "].*)";
		return sourceStr.trim().matches(pattern);
	}

	public static String reverseChars(String s) {
		s = s.replace("&#39;", "\'");
		s = s.replace("&quot;", "\""); // must call it in first
		s = s.replace("&#96;", "`");
		s = s.replace("&#92;", "\\");
		s = s.replace("&#59;", ";");
		return s;
	}

	/**
	 * 縮短字串度為15+3個.以利顯示
	 * 
	 * @param sourceStr
	 *            來源字串
	 * @return 縮短後的字串
	 */
	public static String shortString(String sourceStr) {
		return shortString(sourceStr, 15);
	}

	/**
	 * 縮短字串以利顯示
	 * 
	 * @param sourceStr
	 *            來源字串
	 * @param length
	 *            縮短後的長度（不包含...）
	 * @return 縮短後的字串
	 */
	public static String shortString(String sourceStr, int absLength) {
		if (sourceStr != null && sourceStr.length() > 0) {
			StringBuffer sb = new StringBuffer();
			int l = 0;
			int i = 0;
			int length = sourceStr.length();
			for (i = 0; i < length; i++) {
				l += countLength(sourceStr.charAt(i));
				if (l > absLength) {
					break;
				}
				sb.append(sourceStr.charAt(i));
			}
			if (i != length) {
				sb.append("...");
			}
			return sb.toString();
		}
		return sourceStr;
	}

	private static int countLength(char c) {
		// 單字元
		if ((int) c < 128) {
			return 1; // or < 256
		}
		// 中文字, 雙字元
		return 2;
	}

	public static String verifyString(String str, int maxLen) {
		if (str == null) {
			return null;
		}
		if (str.length() > maxLen) {
			str = str.substring(0, maxLen);
		}
		return str;
	}

	public static String[] verifyStringArray(String[] str, int maxLen) {
		if (str == null) {
			return str;
		}
		for (int i = 0; i < str.length; i++) {
			if (str[i].length() > maxLen) {
				str[i] = str[i].substring(0, maxLen);
			}
		}
		return str;
	}

	public static String join(String[] list, String conjunction) {
		if (list == null || list.length == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : list) {
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append(item);
		}
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String join(Collection list, String conjunction) {
		if (list == null || list.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object item : list) {
			if (item == null) {
				continue;
			}
			sb.append(item).append(conjunction);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String join(int[] list, String conjunction) {
		if (list == null || list.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int item : list) {
			sb.append(item).append(conjunction);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String notNull(String param) {
		return notNull(param, "&nbsp;");
	}

	public static String notNull(String param, String def) {
		if (null == param) {
			return def;
		}
		return param;
	}

	public static String convert2JSArray(String[] datas, String qoute) {
		StringBuilder array = new StringBuilder();
		for (int i = 0; i < datas.length; i++) {
			if (i > 0) {
				array.append(",");
			}
			array.append(qoute).append(datas[i]).append(qoute);
		}
		return "[" + array.toString() + "]";

	}

	public static <O> String convert2JSArray(Collection<O> datas, String qoute) {
		StringBuilder array = new StringBuilder();
		int i = 0;
		for (O o : datas) {
			if (i > 0) {
				array.append(",");
			}
			array.append(qoute).append(o).append(qoute);
			i++;
		}
		return "[" + array.toString() + "]";
	}

	public static boolean isSame(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		} else if (str1 != null && str2 != null) {
			return str1.equals(str2);
		}
		return false;
	}

	public static int countByteArrayLengthOfString(CharSequence sequence) {
		final int len = sequence.length();
		int count = len;
		for (int i = 0; i < len; i++) {
			char ch = sequence.charAt(i);
			if (ch <= 0x7F) {
				// count++;
			} else if (ch <= 0x7FF) {
				count += 1;
			} else if (ch >= 0xD800 && ch <= 0xDBFF) {
				count += 2;
				++i;
			} else {
				count += 2;
			}
		}
		return count;
	}
	
	/**
	 * 將URLEncoder.encode調整為RuntimeException Function(會加入ExpandUtils)
	 */
	public static String URLEncode(String value, String charset) {
		try {
			return URLEncoder.encode(value, charset);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.amendToUncheckedException(e);
		}
	}
}