package cn.hub.jackeroo.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
	        "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 根据固定的格式，将字符串转化为Date
	 * 
	 * @param str
	 * @param ftm
	 * @return
	 */
	public static Date parseDate(String str, String ftm) {
		if (str == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(ftm).parse(str);
		}
		catch (ParseException e) {
			return null;
		}

	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数，Date类型
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取两个日期之间的天数，字符串格式
	 * 
	 * @param before
	 * @param after
	 * @param fmt
	 *            : 字符串的日期格式
	 * @return
	 */
	public static double getDistanceOfTwoDate(String before, String after, String fmt) {
		Date beforeD = parseDate(before, fmt);
		Date afterD = parseDate(after, fmt);

		return getDistanceOfTwoDate(beforeD, afterD);
	}

	/**
	 * @Description: 获取两个日期之间的小时数
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistHoursOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60);
	}

	/**
	 * @Description: 获取两个时间相差的分钟数
	 * @param start
	 * @param end
	 * @return
	 */
	public static Integer diffMinute(Date start, Date end) {
		return (int) Math.ceil((double) (end.getTime() - start.getTime()) / (1000 * 60));
	}

	/**
	 * 通过毫秒时间戳获取小时数和分钟数
	 * 
	 * @param time
	 * @return
	 */
	public static String getHourAndMinute(long time) {
		int minute = (int) Math.ceil((double) (time) / (1000 * 60));
		int hours = (int) Math.floor((double) minute / 60);
		minute = minute % 60;
		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(hours).append("小时");
		}
		if (minute > 0) {
			sb.append(minute).append("分");
		}
		return sb.toString();
	}

}
