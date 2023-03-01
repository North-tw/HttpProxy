package com.nv.commons.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.nv.commons.annotation.HttpUpdate;
import com.nv.commons.constant.Setting;
import com.nv.expandUtil.exception.AccessDeniedException;
import com.nv.util.RequestParser;

public class SystemSettingBO {

	/**
	 * 比對欄位名稱做修改，主要給updateSetting.jsp使用，所以必須檢查HttpUpdate
	 *
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static boolean updateSettingByField(String key, String value) throws Exception {
		return updateSettingByField(key, value, HttpUpdate.class);
	}

	/**
	 * 比對欄位名稱做修改，物件內使用
	 *
	 * @param key
	 * @param value
	 * @param annotationCheck
	 * @return
	 * @throws Exception
	 */
	private static boolean updateSettingByField(String key, String value,
		Class<? extends Annotation> annotationCheck) throws Exception {
		Class<Setting> clazz = Setting.class;
		// check field
		Field field = clazz.getField(key);
		if (field == null) {
			return false;
		}

		// 判斷參數是否有宣告 "@HttpUpdate"，有宣告才能修改
		if (annotationCheck != null && !field.isAnnotationPresent(annotationCheck)) {
			throw new AccessDeniedException();
		}

		String type = field.getType().toString();
		if ("class java.lang.String".equals(type)) {
			if (value == null || 0 == value.length()) {
				throw new NoSuchFieldException();
			}
			field.set(clazz, value);
		} else if ("boolean".equals(type)) {
			field.setBoolean(clazz, RequestParser.getBooleanParameter(value));
		} else if ("int".equals(type)) {
			field.setInt(clazz, RequestParser.getIntParameter(value));
		} else {
			return false;
		}

		return true;

	}
}
