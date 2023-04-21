package com.pokemongostats.controller.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.pokemongostats.model.bean.AppUpdate;
import com.pokemongostats.model.bean.SemVer;
import com.pokemongostats.view.activity.LauncherActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppUpdateUtils {

	public static final String GITHUB_RELEASES_URL = "https://api.github.com/repos/DuboisJerome/pokemon-go-stats/releases/latest";
	public static final String UPDATE_EXTRA = "update";

	public static void checkForUpdate(Context context) {
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
				// FIXME LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {

				try {

					assert response.body() != null;
					JSONObject releaseInfo = new JSONObject(
							response.body().string());
					JSONArray releaseAssets = releaseInfo
							.getJSONArray("assets");
					String changeLog = releaseInfo.getString("body");

					String latestRemoteVer = null;
					String latestRemoteDLUrl = null;
					for (int i = 0; i < releaseAssets.length(); i++) {
						JSONObject asset = releaseAssets.getJSONObject(i);
						String assetName = asset.getString("name");
						if (!assetName.contains(".apk")) {
							continue;
						}
						String currentRemoteVer = assetName
								.replace("pokemongostats_v", "")
								.replace(".apk", "");

						SemVer currentSemVer = SemVer.parse(currentRemoteVer);
						SemVer latestSemVer = SemVer.parse(latestRemoteVer);

						// If latest version is smaller than current version
						if (latestSemVer.compareTo(currentSemVer) < 0) {
							latestRemoteVer = currentRemoteVer;
							latestRemoteDLUrl = asset
									.getString("browser_download_url");
						}
					}

					// no remote apk find
					if (latestRemoteVer == null || latestRemoteVer.isEmpty()
							|| latestRemoteDLUrl.isEmpty()) {
						return;
					}

					AppUpdate update = new AppUpdate(latestRemoteDLUrl,
							latestRemoteVer, changeLog, AppUpdate.UP_TO_DATE);

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
					// FIXME LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
				} catch (Exception e) {
					Log.e("ERROR", "", e);
				}
			}
		});
	}
}