package com.pokemongostats.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.controller.utils.AppUpdateUtils;
import com.pokemongostats.model.bean.AppUpdate;
import com.pokemongostats.view.utils.ImageUtils;

import java.util.Objects;

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
			final String key = AppUpdateUtils.UPDATE_EXTRA;
			if (extras.containsKey(key)) {
				showDialog(Objects.requireNonNull(intent.getParcelableExtra(key)));
			}
		}
	}

	private void showDialog(AppUpdate update) {
		String title = getString(R.string.update_available);
		String message = getString(R.string.update_dialog_message,
				getString(R.string.app_name), update.getVersion(),
				update.getChangelog());

		Drawable icon = ImageUtils
				.resize(ContextCompat.getDrawable(getApplication(), R.drawable.ic_app), this);

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(title).setMessage(message).setIcon(icon)
				.setPositiveButton(getString(R.string.update),
						(dialogInterface, i) -> {
							dialogInterface.dismiss();
							Intent startDownloadIntent = new Intent(
									DownloadActivity.this,
									DownloadUpdateService.class);
							startDownloadIntent.putExtra(
									DownloadUpdateService.KEY_DOWNLOAD_URL,
									update.getAssetUrl());
							startService(startDownloadIntent);
							finish();
						})
				.setNegativeButton(getString(R.string.cancel),
						(dialogInterface, i) -> {
							dialogInterface.dismiss();
							DownloadActivity.this.finish();
						})
				.setCancelable(true);
		builder.create().show();
	}

}