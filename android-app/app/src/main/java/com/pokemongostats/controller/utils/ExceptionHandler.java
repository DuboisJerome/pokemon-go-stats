package com.pokemongostats.controller.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    public ExceptionHandler(final Context c){
        this.mContext = c;
        try{
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        } catch (IOException ioe){
            Log.e(ErrorUtils.TAG, "Unable to clear logcat");
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(ErrorUtils.TAG, "UncaughtException", throwable);
        ErrorUtils.sendLogToAdmin(mContext);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
