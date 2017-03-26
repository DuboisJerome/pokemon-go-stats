package com.pokemongostats.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.controller.utils.AppUpdate;
import com.pokemongostats.controller.utils.AppUpdateUtil;
import com.pokemongostats.view.utils.ImageHelperUtils;

/**
 * @author Zapagon
 */
public class DownloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final String key = AppUpdateUtil.UPDATE_EXTRA;
            if (extras.containsKey(key)) {
                showDialog((AppUpdate) intent.getParcelableExtra(key));
            }
        }
    }

    private void showDialog(final AppUpdate update) {
        String title = getString(R.string.update_available);
        String message = getString(R.string.update_dialog_message,
                getString(R.string.app_name), update.getVersion(),
                update.getChangelog());

        Drawable icon = ImageHelperUtils
                .resize(getResources().getDrawable(R.drawable.icon_app), this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title).setMessage(message).setIcon(icon)
                .setPositiveButton(getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                dialogInterface.dismiss();
                                Intent startDownloadIntent = new Intent(
                                        DownloadActivity.this,
                                        DownloadUpdateService.class);
                                startDownloadIntent.putExtra(
                                        DownloadUpdateService.KEY_DOWNLOAD_URL,
                                        update.getAssetUrl());
                                startService(startDownloadIntent);
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                dialogInterface.dismiss();
                                DownloadActivity.this.finish();
                            }
                        })
                .setCancelable(true);
        builder.create().show();
    }

}