package com.pokemongostats.view.activities;

import com.pokemongostats.view.OverlayService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * @author Zapagon
 *
 */
public class OverlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toggleService();
		finish(); // Just close the Activity after the toggle
	}

	private void toggleService() {
		Intent intent = new Intent(this, OverlayService.class);
		// Try to stop the service if it is already running
		// Otherwise start the service
		if (!stopService(intent)) {
			startService(intent);
		}
	}
}