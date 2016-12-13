package zte.hx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * java日期api的功能类。<br />
 * 说明1：日期（date）指年、月、日，时间（time）指时、分、秒。日期时间指前两者合成<br />
 * 说明2：本类包含日期时间（java.util.Date）和特定格式的字符串之间的互相转换。<br/>
 * 说明3：本类可作为项目的日期时间操作接口，并对其格式的进行统一约定。
 * 
 * @author 肖汉
 * @author hx（修改方法名，简化实现，增加注释）
 * 
 */
public class TimeUtil {
	private static final SimpleDateFormat longFormat = new SimpleDateFormat(// 单例
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Calendar defaultCalendar = Calendar.getInstance();

	public static long getSecondsPassedFrom(String dateTimeString) {
		Date date = parseDateTime(dateTimeString);
		long secondsPassed = getSecondsPassed(date);
		return secondsPassed;
	}

	public static String getDateString(Date date) {
		return shortFormat.format(date);
	}

	public static String getDateTimeString(Date date) {
		return longFormat.format(date);
	}

	public static String getDateTimeString(long secondsPassed) {
		Date date = new Date(secondsPassed * 1000);
		return longFormat.format(date);
	}

	public static String getNowDateTimeString() {
		return getDateTimeString(new Date());
	}

	public static String getNowDateString() {
		return getDateString(new Date());
	}

	public static String getLastTodayDate() {
		return getDateString(addDay(new Date(), -7));
	}

	/**
	 * 通过date:Date得到逝去的秒数。
	 * 
	 * @param date
	 *            日期
	 * @return 以统一格林时间为基准的逝去的秒数
	 */
	public static long getSecondsPassed(Date date) {
		return date.getTime() / 1000;
	}

	public static long getSecondsPassed(String dateString) {
		Date date = parseDate(dateString);
		return getSecondsPassed(date);
	}

	public static Date parseDate(long secondsPassed) {
		return new Date(secondsPassed * 1000);
	}

	public static long getNowMillisecondsPassed() {
		return new Date().getTime();
	}

	public static long getMillisecondsPassed(Date date) {
		return date.getTime();
	}

	/**
	 * 解析日期字符串。
	 * 
	 * @param dateString
	 *            格式为"yyyy-MM-dd"的日期字符串。
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date result = null;
		try {
			result = shortFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date parseDateTime(String dateString) {
		Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			result = sdf.parse(dateString);
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date addDay(Date date, int number) {
		defaultCalendar.setTime(date);
		defaultCalendar.add(Calendar.DATE, number);
		return defaultCalendar.getTime();
	}

	public static String addDay(String dateString, int number) {
		Date originalDate = parseDateTime(dateString);
		Date objectiveDate = addDay(originalDate, number);
		return getDateTimeString(objectiveDate);
	}

	public static Date addDay(long orignalDate, int number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(orignalDate));
		cal.add(Calendar.DATE, number);
		Date newDate = cal.getTime();
		return newDate;
	}

	public static Date addMinute(Date orignalDate, int number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(orignalDate);
		cal.add(Calendar.MINUTE, number);
		Date newDate = cal.getTime();
		return newDate;
	}

	public static Date addMinute(long orignalDate, int number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(orignalDate));
		cal.add(Calendar.MINUTE, number);
		Date newDate = cal.getTime();
		return newDate;
	}

	public static String addSeconds(String dateTime, double seconds) {
		return getDateTimeString(new Date(parseDateTime(dateTime).getTime() + ((long) (seconds * 1000))));
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param beginm
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 天数
	 */
	public static int getIntervalDaysCount(Date startDate, Date endDate) {
		int betweenDays = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(startDate);
		c2.setTime(endDate);
		if (c1.after(c2)) {
			c1 = c2;
			c2.setTime(startDate);
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.DAY_OF_YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	public static int getMinutesBetween(String startDateTime, String endDateTime) {
		Date startDate = parseDateTime(startDateTime);
		Date endDate = parseDateTime(endDateTime);
		return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60);
	}

	public static double getSecondsBetween(String startDateTime, String endDateTime) {
		Date startDate = parseDateTime(startDateTime);
		Date endDate = parseDateTime(endDateTime);
		return (int) ((endDate.getTime() - startDate.getTime()) / 1000);
	}

	/**
	 * 得到日期之间的间隔。
	 * 
	 * @param startDateString
	 *            起始日期字符串
	 * @param toDateString
	 *            结束日期字符串
	 * @return 天数
	 */
	public static int getIntervalDaysCount(String startDateString, String toDateString) {
		Date startDate = parseDateTime(startDateString);
		Date toDate = parseDateTime(toDateString);
		return getIntervalDaysCount(startDate, toDate);
	}

	public static List<Date> getDatesBetween(Date startDate, Date endDate) {
		int length = getIntervalDaysCount(startDate, endDate);
		Calendar gc = Calendar.getInstance();
		List<Date> result = new ArrayList<Date>();
		for (int i = 0; i < length; i++) {
			gc.setTime(startDate);
			gc.add(Calendar.DAY_OF_MONTH, i);
			result.add(gc.getTime());
		}
		return result;
	}

	public static String getDateTimeNumber() {
		return getNowDateTimeString().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
	}
}
