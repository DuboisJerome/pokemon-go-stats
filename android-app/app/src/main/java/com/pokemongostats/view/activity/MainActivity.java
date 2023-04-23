package com.pokemongostats.view.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.databinding.ActivityOneFragmentBinding;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class MainActivity extends AppCompatActivity {

	private OverlayService service;
	private final ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			OverlayService.OverlayServiceBinder b = (OverlayService.OverlayServiceBinder) binder;
			MainActivity.this.service = b.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			MainActivity.this.service = null;
		}
	};

	private final BroadcastReceiver exitReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!isFinishing()) {
				MainActivity.this.setResult(RESULT_OK);
				MainActivity.this.finishAndRemoveTask();
			}
		}
	};

	public OverlayService getService() {
		return this.service;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		com.pokemongostats.databinding.ActivityOneFragmentBinding binding = ActivityOneFragmentBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_lst_pkmn, R.id.navigation_lst_move, R.id.navigation_type)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView, navController);

		binding.navView.setItemIconTintList(null);

		registerReceiver(this.exitReceiver, new IntentFilter("EXIT"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
		app.setCurrentActivity(this);
		Intent intent = new Intent(this, OverlayService.class);
		bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		unbindService(this.mConnection);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		clearReferences();
		unregisterReceiver(this.exitReceiver);
		super.onDestroy();
	}

	private void clearReferences() {
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
		if (this.equals(app.getCurrentActivity())) {
			app.setCurrentActivity(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		menu.findItem(R.id.action_is_last_evolution_only)
				.setChecked(PreferencesUtils.getInstance().isLastEvolutionOnly());
		menu.findItem(R.id.action_is_with_mega_evolution)
				.setChecked(PreferencesUtils.getInstance().isWithMega());
		menu.findItem(R.id.action_is_with_legendary)
				.setChecked(PreferencesUtils.getInstance().isWithLegendary());
		menu.findItem(R.id.action_about_us).setOnMenuItemClickListener(mi -> {
			showAboutUs();
			return true;
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_is_last_evolution_only) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setLastEvolutionOnly(item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_mega_evolution) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setWithMega(item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_legendary) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setWithLegendary(item.isChecked());
		}

		if (item.getItemId() == R.id.action_minimize) {
			if (getService() != null) {
				getService().minimize();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private void showAboutUs() {
		Spanned s = Html.fromHtml(getString(R.string.about_content), Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM);

		AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.about))
				.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
				.setMessage(s).setCancelable(true).create();
		dialog.show();
		// Make the textview clickable. Must be called after show()
		((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	}
}