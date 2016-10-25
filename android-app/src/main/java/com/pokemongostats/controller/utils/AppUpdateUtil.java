package com.pokemongostats.controller.utils;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.pokemongostats.view.activities.LauncherActivity;

import android.content.Context;
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

	public static final String GITHUB_RELEASES_URL = "https://api.github.com/repos/DuboisJerome/pokemon-go-stats/releases/latest";
	public static final String UPDATE_EXTRA = "update";

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
					String apkName = releaseAssets.getString("name");
					String latestRemoteVersionStr = apkName
							.replace("pokemongostats_v", "")
							.replace(".apk", "");
					AppUpdate update = new AppUpdate(
							releaseAssets.getString("browser_download_url"),
							latestRemoteVersionStr,
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
}
