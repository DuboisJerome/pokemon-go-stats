package com.pokemongostats.controller.utils;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.view.activities.LauncherActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.content.LocalBroadcastManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppUpdateUtil {

	public static final String GITHUB_RELEASES_URL = "TODO";

	public static void checkForUpdate(final Context context) {
		OkHttpClient httpClient = new OkHttpClient();

		Request request = new Request.Builder().url(GITHUB_RELEASES_URL)
				.build();

		httpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				AppUpdate update = new AppUpdate(null, null, null,
						AppUpdate.ERROR);
				Intent updateIntent = LauncherActivity
						.createUpdateDialogIntent(update);
				LocalBroadcastManager.getInstance(context)
						.sendBroadcast(updateIntent);
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {

				try {

					JSONObject releaseInfo = new JSONObject(
							response.body().string());
					JSONObject releaseAssets = releaseInfo
							.getJSONArray("assets").getJSONObject(0);
					if (releaseAssets.getString("name").contains("Offline")) {
						releaseAssets = releaseInfo.getJSONArray("assets")
								.getJSONObject(1);
					}

					AppUpdate update = new AppUpdate(
							releaseAssets.getString("browser_download_url"),
							releaseInfo.getString("tag_name"),
							releaseInfo.getString("body"),
							AppUpdate.UP_TO_DATE);

					PackageManager pm = context.getPackageManager();
					String pn = context.getPackageName();
					PackageInfo pInfo;
					String versionName;
					try {
						pInfo = pm.getPackageInfo(pn, 0);
						versionName = pInfo.versionName;
					} catch (NameNotFoundException e) {
						versionName = "";
					}
					SemVer currentVersion = SemVer.parse(versionName);
					SemVer remoteVersion = SemVer.parse(update.getVersion());

					// If current version is smaller than remote version
					if (currentVersion.compareTo(remoteVersion) < 0) {
						update.setStatus(AppUpdate.UPDATE_AVAILABLE);
					}

					Intent updateIntent = LauncherActivity
							.createUpdateDialogIntent(update);
					LocalBroadcastManager.getInstance(context)
							.sendBroadcast(updateIntent);
				} catch (JSONException je) {
				}
			}
		});
	}

	public static AlertDialog getAppUpdateDialog(final Context context,
			final AppUpdate update) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("Update available")
				.setMessage(context.getString(R.string.app_name) + " v"
					+ update.getVersion() + " " + "is available" + "\n\n"
					+ "Changes:" + "\n\n" + update.getChangelog())
				.setIcon(R.drawable.icon_app).setPositiveButton("Update",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface,
									int i) {
								dialogInterface.dismiss();
								Intent startDownloadIntent = new Intent(context,
										DownloadUpdateService.class);
								startDownloadIntent.putExtra("downloadURL",
										update.getAssetUrl());
								context.startService(startDownloadIntent);
							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface,
									int i) {
								dialogInterface.dismiss();
							}
						})
				.setCancelable(true);
		AlertDialog dialog = builder.create();
		return dialog;
	}
}
