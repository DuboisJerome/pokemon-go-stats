package com.pokemongostats.controller.external;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelStoreOwner;

import com.pokemongostats.controller.TaskRunner;
import com.pokemongostats.controller.external.json.ParserJsonInputStream;
import com.pokemongostats.model.bean.UpdateStatus;
import com.pokemongostats.view.dialog.UpdateProgressionDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

public class ServiceUpdateDataPokedex {
	private static final String REMOTE_FILE_URL = "https://raw.githubusercontent.com/PokeMiners/game_masters/master/latest/latest.json";

	private static InputStream getInputStream(Context c) throws IOException {
		boolean isTestEnabled = true;
		InputStream in;
		if (isTestEnabled) {
			AssetManager assetManager = c.getAssets();
			in = assetManager.open("latest.json");
		} else {
			in = new URL(REMOTE_FILE_URL).openStream();
		}
		return in;
	}
	public static void updateData(Context c, FragmentManager fm) {
		final UpdateStatus updateStatus = new UpdateStatus();
		final UpdateProgressionDialogFragment dialog = new UpdateProgressionDialogFragment(updateStatus);
		dialog.setCancelable(false);
		dialog.show(fm, "UPATE_PROGRESS");

		Callable<Boolean> callable = () -> {
			boolean isOk = true;
			IExternalDataPokedex<InputStream> externalDataPokedex = new ParserJsonInputStream();
			try (InputStream in = getInputStream(c)) {
				externalDataPokedex.readDatas(in, updateStatus);
			} catch (IOException | ParserException e) {
				Log.error("Error updating data", e);
				isOk = false;
			}
			return isOk;
		};

		Log.info("Start update" );
		TaskRunner.Callback<Boolean> callback = new TaskRunner.Callback<>() {
			@Override
			public void onComplete(Boolean result) {
				updateStatus.finnish();
				Log.info("END update : SUCCESS" );
			}

			@Override
			public void onError(Exception e) {
				Log.error("END update : FAILURE" , e);
			}
		};

		TaskRunner taskRunner = new TaskRunner();
		taskRunner.executeAsync(callable, callback);

	}
}