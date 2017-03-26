package com.pokemongostats.controller.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Zapagon on 22/03/2017.
 */

public class MailUtils {

    private static final String TO = "pokemongostats.adm@gmail.com";
    private static final String CC = "jerome.dubois.etu@gmail.com";

    public static void sendMailToAdmin(final Context c, final String subject, final String body, final String[] fileNameList) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{TO});
        i.putExtra(Intent.EXTRA_CC, new String[]{CC});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , body);
        if(fileNameList != null){
            for(String fileName : fileNameList) {
                i.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file://" + fileName));
            }
        }
        try {
            Intent mailClientChooser = Intent.createChooser(i, "Envoyer le rapport d'erreur...");
            mailClientChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(mailClientChooser);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(c, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            Log.e(TagUtils.MAIL, "There are no email clients installed.", ex);
        }
        Log.i(TagUtils.MAIL, "OK");
    }
}
