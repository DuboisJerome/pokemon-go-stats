package com.pokemongostats.controller.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class ErrorUtils {

    public static void sendLogToAdmin(final Context c) {
        // save logcat in file
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS),
                "logcat.txt");
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String outputFileName = outputFile.getAbsolutePath();

        PrintWriter writer = null;
        try {
            // clear file
            writer = new PrintWriter(outputFile);
            writer.print("");
        } catch (Throwable e) {
            Log.e(TagUtils.CRIT, e.getLocalizedMessage(), e);
        }

        try {
            Process process = Runtime.getRuntime().exec("logcat -df " + outputFileName + " " + TagUtils.CRIT + ":V *:D");
            process.waitFor();
        } catch (Throwable e) {
            Log.e(TagUtils.CRIT, e.getLocalizedMessage(), e);
        }

        String subject = "Error in application pokemongostats";
        String body = readFile(outputFileName);
        if (body == null || body.isEmpty()) {
            body = "Pas de messages disponibles";
        }
        if (writer != null) {
            writer.close();
        }
        // TODO i18n
        MailUtils.sendMailToAdmin(c, subject, body, new String[]{outputFileName});
    }

    private static String readFile(final String filename) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (Exception e) {
            String err = "Erreur lors de la lecture du fichier " + filename;
            text.append("=========================");
            text.append("\n");
            text.append(err);
            text.append("\n");
            text.append("=========================");
            text.append("\n");
            text.append(e.getLocalizedMessage());
            text.append("\n");
        }

        return text.toString();
    }
}
