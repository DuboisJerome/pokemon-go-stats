package com.pokemongostats.model.bean;

import android.content.Context;
import android.util.Log;

import com.pokemongostats.controller.utils.ErrorUtils;
import com.pokemongostats.controller.utils.TagUtils;

import java.io.IOException;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    public ExceptionHandler(final Context c) {
        this.mContext = c;
        //clearLogcat();
    }

    private void clearLogcat() {
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        } catch (IOException ioe) {
            Log.e(TagUtils.CRIT, "Unable to clear logcat");
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logCrash(throwable);
        ErrorUtils.sendLogToAdmin(mContext);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void logCrash(final Throwable t) {
        Log.e(TagUtils.CRIT, "UncaughtException", t);
        logCause(t);
    }

    private void logCause(final Throwable t) {
        if (t != null) {
            Throwable cause = t.getCause();
            if (cause != null) {
                Log.e(TagUtils.CRIT, "Caused by", cause);
                logCause(cause);
            }
        }
    }
}
