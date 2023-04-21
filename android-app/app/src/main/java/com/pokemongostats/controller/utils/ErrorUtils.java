package com.pokemongostats.controller.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
 
/**
 * Created by Zapagon on 22/03/2017.
 */

public class ErrorUtils {

	public static void sendLogToAdmin(final Context c) {
//        // save logcat in file
//        File outputFile = new File(Environment.DIRECTORY_DOCUMENTS + "/" + PkmnGoStatsApplication.class.getName(),
//                "logcat.txt");
//
//        if (!outputFile.exists()) {
//            try {
//                boolean isCreate = outputFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String outputFileName = outputFile.getAbsolutePath();
//
//        try (PrintWriter writer = new PrintWriter(outputFile)) {
//            writer.print("");
//
//            try {
//                Process process = Runtime.getRuntime().exec("logcat -df " + outputFileName + " " + TagUtils.CRIT + ":V *:D");
//                process.waitFor();
//            } catch (Throwable e) {
//                Log.e(TagUtils.CRIT, e.getLocalizedMessage(), e);
//            }
//
//            String subject = "Error in application pokemongostats";
//            String body = readFile(outputFileName);
//            if (body.isEmpty()) {
//                body = "Pas de messages disponibles";
//            }
//            // TODO i18n
//            MailUtils.sendMailToAdmin(c, subject, body, new String[]{outputFileName});
//        } catch (Throwable e) {
//            Log.e(TagUtils.CRIT, e.getLocalizedMessage(), e);
//        }

	}

	private static String readFile(final String filename) {
		//Read text from file
		StringBuilder text = new StringBuilder();

		try (FileReader fr = new FileReader(new File(filename)); BufferedReader br = new BufferedReader(fr)) {
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
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