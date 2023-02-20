package com.nv.util;

import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * Mexico-05-GA211230022
	 * 
	 * Mexico,Bavet 爲5位以上的純英文
	 * 05 桌號2位以上，不足補0
	 * GA211230022 GA+shoe+round，shoe+round爲5位以上，round(至多4位，不足補零)
	 */
	public static final Pattern ROUND_ID_PATTERN = Pattern.compile("^[A-Za-z]{5,}-\\d{2,}-GA\\d{5,}");

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
}