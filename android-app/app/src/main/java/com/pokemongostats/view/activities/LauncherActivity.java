package com.pokemongostats.view.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import com.pokemongostats.BuildConfig;
import com.pokemongostats.R;
import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.controller.utils.AppUpdateUtils;
import com.pokemongostats.model.bean.AppUpdate;

/**
 * @author Zapagon
 */
public class LauncherActivity extends Activity {

    public static final String ACTION_SHOW_UPDATE_DIALOG = "com.pokemongostats.SHOW_UPDATE_DIALOG";
    private final BroadcastReceiver showUpdateDialog = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BuildConfig.DEBUG && !isNetworkAvailable()) {
                throw new AssertionError("Assertion failed");
            }
            AppUpdate update = intent.getParcelableExtra(AppUpdateUtils.UPDATE_EXTRA);
            assert update != null;
            if (update.getStatus() == AppUpdate.UPDATE_AVAILABLE && !isApplicationBeingUpdated(context)) {
                createNotification(update);
            }
        }
    };

    /**
     * Remote update
     */

    public static boolean isApplicationBeingUpdated(Context context) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager == null) return false;
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterByStatus(DownloadManager.STATUS_RUNNING);
        Cursor c = downloadManager.query(q);
        if (c.moveToFirst()) {
            String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
            return fileName.equals(DownloadUpdateService.DOWNLOAD_UPDATE_TITLE);
        }
        return false;
    }

    public static Intent createUpdateDialogIntent(AppUpdate update) {
        Intent updateIntent = new Intent(ACTION_SHOW_UPDATE_DIALOG);
        updateIntent.putExtra(AppUpdateUtils.UPDATE_EXTRA, update);
        return updateIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FIXME LocalBroadcastManager.getInstance(this).registerReceiver(showUpdateDialog, new IntentFilter(ACTION_SHOW_UPDATE_DIALOG));

        AppUpdateUtils.checkForUpdate(getApplicationContext());

        Intent intent = new Intent(this, OverlayService.class);
        // Try to stop the service if it is already running
        // Otherwise start the service
        if (!stopService(intent)) {
            startService(intent);
        }
        launchMenu();
        finish();
    }

    private void launchMenu() {
        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities c = connectivityManager.getNetworkCapabilities(nw);
        if (c == null) return false;
        return c.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }

    private void createNotification(AppUpdate update) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
        intent.putExtra(AppUpdateUtils.UPDATE_EXTRA, update);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = getString(R.string.update_available);
        String message = getString(R.string.update_dialog_message, getString(R.string.app_name), update.getVersion(),
                update.getChangelog());

        // Build notification
        Notification noti = new Notification.Builder(getApplicationContext(), "CHANNEL").setContentTitle(title)
                .setContentText(message).setSmallIcon(R.drawable.ic_app).setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        assert notificationManager != null;
        notificationManager.notify(0, noti);
    }
}