package com.pokemongostats.controller.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class ErrorUtils {

    public static final String TAG = "CRASH";

    public static void sendLogToAdmin(final Context c) {
        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat.txt");
        String outputFileName = outputFile.getAbsolutePath();

        try {
            // clear file
            PrintWriter writer = new PrintWriter(outputFile);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            Log.e(ErrorUtils.TAG, e.getLocalizedMessage(), e);
        }

        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-f", outputFileName, ErrorUtils.TAG + ":V", "*:D"});
        } catch (IOException e) {
            Log.e(ErrorUtils.TAG, e.getLocalizedMessage(), e);
        }

        MailUtils.sendMailToAdmin(c, "Error in application pokemongostats", "Regarde la pièce jointe !", new String[]{outputFileName});
    }
}
