package com.figstreet.core;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil
{
	public static final String DATE_FORMAT_YYYYMMDD = "YYYYMMDD";
	public static final String DATE_FORMAT_MMDDYY = "MM/dd/yy";
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	private static final String DATE_FORMAT_LONG = "MMMM d, yyyy";
	public static final String TIME_FORMAT = "h:mm a";
	private static final String BOTH_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
	public static final String ISO8601_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final long MILLIS_PER_DAY = 86400000;
	public static final long MILLIS_PER_HOUR = 3600000;
	public static final long MILLIS_PER_MINUTE = 60000;
	public static final long MILLIS_PER_SECOND = 1000;
	private static final double ONE_HALF = 0.5;
	public static final int ZERO_YEAR = 1970;
	private static final int ZERO_MONTH = 0;
	private static final int ZERO_DAY = 1;
	private static final String MAX_FUTURE_DATE = "12/31/9999";

	public static Date getDate(int pYear, int pMonth, int pDay)
	{
		if (pDay > 0 && pDay < 32 && pMonth >= 0 && pMonth < 12 && pYear > 1900 && pYear < 2500)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DATE, pDay);
			cal.set(pYear, pMonth, pDay, 0, 0, 0);
			return new Date(cal.getTimeInMillis());
		}
		return null;
	}

	public static Date today()
	{
		return dateOnly(now());
	}

	public static Timestamp now()
	{
		return new Timestamp((new java.util.Date()).getTime());
	}

	public static String formatDate(java.util.Date pDate, String pFormat)
	{
		if (pDate == null)
			return null;
		if (pFormat == null)
			return (new SimpleDateFormat(DATE_FORMAT)).format(pDate);
		return (new SimpleDateFormat(pFormat)).format(pDate);
	}

	public static String formatBoth(java.util.Date pDate)
	{
		return formatDate(pDate, BOTH_FORMAT);
	}

	public static String formatDate(java.util.Date pDate)
	{
		return formatDate(pDate, null);
	}

	public static String formatDate(Timestamp pTimestamp)
	{
		return formatDate(pTimestamp, null);
	}

	public static String formatTimestamp(Timestamp pTimestamp, String pFormat)
	{
		return formatDate(pTimestamp, pFormat);
	}

	public static String formatDateLong(java.util.Date pDate)
	{
		return formatDate(pDate, DATE_FORMAT_LONG);
	}

	public static Date parseDate(String pDateString, String pFormat)
	{
		return parseDate(pDateString, pFormat, null);
	}

	private static Date parseDate(String pDateString, String pFormat, Boolean pLenient, ParsePosition pParsePosition)
	{
		if (pDateString == null)
			return null;

		String parseMe = pDateString.trim();
		if (parseMe.length() == 0)
			return null;

		ParsePosition pp = new ParsePosition(0);
		if (pParsePosition != null)
			pp = pParsePosition;
		if (pFormat == null)
			pFormat = DATE_FORMAT;
		if (pFormat.contains("/"))
		{
			parseMe = parseMe.replace('.', '/');
			parseMe = parseMe.replace('-', '/');
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pFormat);
		if (pLenient != null)
			sdf.setLenient(pLenient);
		java.util.Date temp = sdf.parse(parseMe, pp);
		java.util.Date futureDate = null;
		try
		{
			futureDate = sdf.parse(MAX_FUTURE_DATE);
		}
		catch (ParseException pe)
		{
			/*If future date is exceeded parsing will fail*/
		}
		if (temp != null && futureDate != null && temp.after(futureDate))
			temp = null;

		if (temp == null)
			return null;
		return new Date(temp.getTime());
	}

	public static Date parseDate(String pDateString, String pFormat, Boolean pLenient)
	{
		return parseDate(pDateString, pFormat, pLenient, null);
	}

	public static boolean validateDate(String pDateString, String pFormat, Boolean pLenient)
	{
		ParsePosition pp = new ParsePosition(0);
		Date parsed = parseDate(pDateString, pFormat, pLenient, pp);

		if (parsed == null || pDateString.trim().length() != pp.getIndex())
		{
			return false;
		}
		return true;
	}

	public static boolean validateTime(String pTimeString, String pFormat, Boolean pLenient)
	{
		ParsePosition pp = new ParsePosition(0);
		Time parsed = parseTime(pTimeString, pFormat, pLenient, pp);

		if (parsed == null || pTimeString.trim().length() != pp.getIndex())
		{
			return false;
		}
		return true;
	}

	public static Date parseDate(String pDateString)
	{
		return parseDate(pDateString, null);
	}

	public static String formatTime(java.util.Date pTime, String pFormat)
	{
		if (pTime == null)
			return null;
		if (pFormat == null)
			return (new SimpleDateFormat(TIME_FORMAT)).format(pTime);
		return (new SimpleDateFormat(pFormat)).format(pTime);
	}

	public static String formatTime(java.util.Date pTime)
	{
		return formatTime(pTime, null);
	}

	public static String formatTime(Timestamp pTimestamp)
	{
		return formatTime(pTimestamp, null);
	}

	public static String formatTime(Time pTime)
	{
		return formatTime(pTime, null);
	}

	public static Time parseTime(String pTimeString, String pFormat)
	{
		return parseTime(pTimeString, pFormat, null);
	}

	public static Time parseTime(String pTimeString, String pFormat, Boolean pLenient)
	{
		return parseTime(pTimeString, pFormat, pLenient, null);
	}

	private static Time parseTime(String pTimeString, String pFormat, Boolean pLenient, ParsePosition pParsePosition)
	{
		if (pTimeString == null)
			return null;

		String parseMe = pTimeString.trim();
		if (parseMe.length() == 0)
			return null;

		ParsePosition pp = new ParsePosition(0);
		if (pParsePosition != null)
			pp = pParsePosition;
		SimpleDateFormat sdf;
		if (pFormat == null)
			sdf = new SimpleDateFormat(TIME_FORMAT);
		else
			sdf = new SimpleDateFormat(pFormat);
		if (pLenient != null)
			sdf.setLenient(pLenient);
		java.util.Date temp = sdf.parse(parseMe, pp);
		if (temp == null)
			return null;
		return new Time(temp.getTime());
	}

	public static Timestamp parseTimestamp(String pTimestampString, String pFormat)
	{
		return parseTimestamp(pTimestampString, pFormat, null, null);
	}

	public static Timestamp parseTimestamp(String pTimestampString, String pFormat, Boolean pLenient,
		ParsePosition pParsePosition)
	{
		if (pTimestampString == null)
			return null;

		String parseMe = pTimestampString.trim();
		if (parseMe.length() == 0)
			return null;

		ParsePosition pp = new ParsePosition(0);
		if (pParsePosition != null)
			pp = pParsePosition;
		SimpleDateFormat sdf;
		if (pFormat == null)
			sdf = new SimpleDateFormat(BOTH_FORMAT);
		else
			sdf = new SimpleDateFormat(pFormat);
		if (pLenient != null)
			sdf.setLenient(pLenient);
		java.util.Date temp = sdf.parse(parseMe, pp);
		if (temp == null)
			return null;
		return new Timestamp(temp.getTime());
	}

	public static Time parseTime(String pTimeString)
	{
		return parseTime(pTimeString, null);
	}

	public static Date dateOnly(java.util.Date pDate)
	{
		if (pDate == null)
			return null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		if ((year == ZERO_YEAR) && (month == ZERO_MONTH) && (day == ZERO_DAY))
			return null;

		cal.clear();
		cal.set(year, month, day);

		return new Date(cal.getTime().getTime());
	}

	public static Time timeOnly(java.util.Date pDate)
	{
		if (pDate == null)
			return null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millis = cal.get(Calendar.MILLISECOND);

		if ((hour == 0) && (minute == 0) && (second == 0) && (millis == 0))
			return null;

		cal.clear();
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return new Time(cal.getTime().getTime());
	}

	public static Timestamp combineDateTime(java.util.Date pDate, java.util.Date pTime)
	{
		if ((pDate == null) && (pTime == null))
			return null;
		if (pDate == null)
		{
			Time timeOnly = timeOnly(pTime);
			if (timeOnly != null)
				return new Timestamp(timeOnly.getTime());
			return null;
		}
		if (pTime == null)
		{
			Date dateOnly = dateOnly(pDate);
			if (dateOnly != null)
				return new Timestamp(dateOnly.getTime());
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.clear();
		cal.setTime(pTime);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return new Timestamp(cal.getTime().getTime());
	}

	public static double daysDiff(java.util.Date pFrom, java.util.Date pTo)
	{
		return (pTo.getTime() - pFrom.getTime()) / (double) MILLIS_PER_DAY;
	}

	public static int fullDaysDiff(java.util.Date pFrom, java.util.Date pTo, boolean roundNearestDay)
	{
		return (int) Math.floor(daysDiff(pFrom, pTo) + (roundNearestDay ? ONE_HALF : 0.0));
	}

	public static int fullDaysDiff(java.util.Date pFrom, java.util.Date pTo)
	{
		return fullDaysDiff(pFrom, pTo, true);
	}

	public static Date addDays(java.util.Date pFromDate, int pNumDays)
	{
		if (pNumDays == 0)
			return new Date(pFromDate.getTime());

		Calendar cal = Calendar.getInstance();
		cal.setTime(pFromDate);
		cal.add(Calendar.DAY_OF_MONTH, pNumDays);
		return new Date(cal.getTime().getTime());
	}

	public static Date addMonths(java.util.Date pFromDate, int pNumMonths)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(pFromDate);
		cal.add(Calendar.MONTH, pNumMonths);
		return new Date(cal.getTime().getTime());
	}

	public static Timestamp addHours(java.util.Date pFromDate, int pNumHours)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(pFromDate);
		cal.add(Calendar.HOUR, pNumHours);
		return new Timestamp(cal.getTime().getTime());
	}

	public static Timestamp addSeconds(java.util.Date pFromDate, int pNumSecs)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(pFromDate);
		cal.add(Calendar.SECOND, pNumSecs);
		return new Timestamp(cal.getTime().getTime());
	}

	public static Date getSqlDate(java.util.Date pDate)
	{
		return new Date(pDate.getTime());
	}

	public static long timeOnlyMillis(java.util.Date pDate)
	{
		if(pDate == null)
			return 0;

		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int millis = cal.get(Calendar.MILLISECOND);

		return (hour * MILLIS_PER_HOUR) + (minute * MILLIS_PER_MINUTE) + (second * MILLIS_PER_SECOND) + millis;
	}
}
