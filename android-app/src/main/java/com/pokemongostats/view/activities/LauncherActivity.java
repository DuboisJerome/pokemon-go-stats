package com.pokemongostats.view.activities;

import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.controller.utils.AppUpdate;
import com.pokemongostats.controller.utils.AppUpdateUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 
 * @author Zapagon
 *
 */
public class LauncherActivity extends Activity {

	public static final String ACTION_SHOW_UPDATE_DIALOG = "com.pokemongostats.SHOW_UPDATE_DIALOG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toggleService();

		LocalBroadcastManager.getInstance(this).registerReceiver(
				showUpdateDialog, new IntentFilter(ACTION_SHOW_UPDATE_DIALOG));

		// AppUpdateUtil.checkForUpdate(this);
		finish();
	}

	private void toggleService() {
		Intent intent = new Intent(this, OverlayService.class);
		// Try to stop the service if it is already running
		// Otherwise start the service
		if (!stopService(intent)) {
			startService(intent);
		}
	}

	/** Remote update */

	public static boolean isApplicationBeingUpdated(Context context) {

		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Query q = new DownloadManager.Query();
		q.setFilterByStatus(DownloadManager.STATUS_RUNNING);
		Cursor c = downloadManager.query(q);
		if (c.moveToFirst()) {
			String fileName = c
					.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
			if (fileName.equals(
					DownloadUpdateService.DOWNLOAD_UPDATE_TITLE)) { return true; }

		}
		return false;
	}

	private final BroadcastReceiver showUpdateDialog = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			assert isNetworkAvailable();
			AppUpdate update = intent.getParcelableExtra("update");
			if (update.getStatus() == AppUpdate.UPDATE_AVAILABLE
				&& !isApplicationBeingUpdated(context)) {
				AlertDialog updateDialog = AppUpdateUtil
						.getAppUpdateDialog(getApplicationContext(), update);
				updateDialog.show();
			}
		}
	};

	public static Intent createUpdateDialogIntent(AppUpdate update) {
		Intent updateIntent = new Intent(ACTION_SHOW_UPDATE_DIALOG);
		updateIntent.putExtra("update", update);
		return updateIntent;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}