package com.pokemongostats.model.bean;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import fr.commons.generique.controller.utils.TagUtils;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


	public ExceptionHandler() {
	}

	private void clearLogcat() {
		try {
			Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
		} catch (IOException ioe) {
			Log.e(TagUtils.CRIT, "Unable to clear logcat");
		}
	}

	@Override
	public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
		logCrash(throwable);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

	private void logCrash(Throwable t) {
		Log.e(TagUtils.CRIT, "UncaughtException", t);
		logCause(t);
	}

	private void logCause(Throwable t) {
		if (t != null) {
			Throwable cause = t.getCause();
			if (cause != null) {
				Log.e(TagUtils.CRIT, "Caused by", cause);
				logCause(cause);
			}
		}
	}
}