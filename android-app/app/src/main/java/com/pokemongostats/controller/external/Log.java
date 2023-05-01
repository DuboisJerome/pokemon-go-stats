package com.pokemongostats.controller.external;

public class Log {
	public static String TAG_EXTERNAL_DATA = "EXTERNAL_DATA";

	public static void error(String msg, Throwable t) {
		android.util.Log.e(TAG_EXTERNAL_DATA, msg, t);
	}

	public static void warn(String msg) {
		android.util.Log.w(TAG_EXTERNAL_DATA, msg);
	}

	public static void info(String msg) {
		android.util.Log.i(TAG_EXTERNAL_DATA, msg);
	}

	public static void debug(String msg) {
		android.util.Log.d(TAG_EXTERNAL_DATA, msg);
	}
}