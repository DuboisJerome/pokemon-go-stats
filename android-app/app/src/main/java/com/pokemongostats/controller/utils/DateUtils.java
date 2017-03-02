/**
 * 
 */
package com.pokemongostats.controller.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zapagon
 *
 */
public class DateUtils {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final DateFormat FORMATTER = new SimpleDateFormat(
			DATE_FORMAT);

	private DateUtils() {
	}
	/**
	 * Format date
	 * 
	 * @param d
	 * @return
	 */
	public static String format(Date d) {
		if (d == null) { return ""; }
		return FORMATTER.format(d);
	}

	/**
	 * Parse date string
	 * 
	 * @param date
	 * @return
	 */
	public static Date parse(String date) {
		try {
			return FORMATTER.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
