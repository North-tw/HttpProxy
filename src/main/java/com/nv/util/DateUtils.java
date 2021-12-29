package com.nv.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	public static final int END = 1;

	private DateUtils() {
		throw new AssertionError();
	}

	public static Duration between(Date from, Date to) {
		if(from == null || to == null) {
			throw new IllegalArgumentException();
		}
		return Duration.between(getLocalDateTime(from), getLocalDateTime(to));
	}

	public static Duration between(Calendar from, Calendar to) {
		if(from == null || to == null) {
			throw new IllegalArgumentException();
		}
		return Duration.between(getZonedDateTime(from), getZonedDateTime(to));
	}

	public static Duration between(LocalDateTime from, LocalDateTime to) {
		return Duration.between(from, to);
	}

	public static Duration between(String from, String to, String pattern) {
		return Duration.between(getLocalDateTime(from, pattern), getLocalDateTime(to, pattern));
	}

	public static Duration between(ZonedDateTime from, ZonedDateTime to) {
		return Duration.between(from, to);
	}

	public static LocalDateTime getLocalDateTime(String dateStr, String pattern) {
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
		return localDateTime;
	}

	public static LocalDateTime getLocalDateTime(Date date) {
		return  date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime getLocalDateTime(Timestamp timestamp) {
		return timestamp.toLocalDateTime();
	}

	/**
	 * Calendar本身有時區資訊，所以應該採用ZonedDateTime
	 * @param calendar
	 * @return
	 */
	public static ZonedDateTime getZonedDateTime(Calendar calendar) {
		TimeZone timeZone = calendar.getTimeZone();
		ZoneId zoneId = (timeZone == null ? ZoneId.systemDefault() : timeZone.toZoneId());
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(calendar.toInstant(), zoneId);
		return zonedDateTime;
	}

	/**
	 * 將日期轉成字串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		DateFormat df = ThreadLocalUtils.getSimpleDateFormat(pattern);
		return df.format(date);
	}
}
