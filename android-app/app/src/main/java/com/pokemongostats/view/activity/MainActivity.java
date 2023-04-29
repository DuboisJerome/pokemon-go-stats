package com.pokemongostats.view.activity;

import static android.app.NotificationManager.IMPORTANCE_LOW;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pokemongostats.R;
import com.pokemongostats.controller.ServiceNotification;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.controller.utils.AppUpdateUtils;
import com.pokemongostats.controller.utils.PkmnTags;
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

		ServiceNotification.createNotificationChannel(getApplicationContext());
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
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.MEGA));
		menu.findItem(R.id.action_is_with_legendary)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.LEGENDAIRE));
		menu.findItem(R.id.action_is_with_mythic)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.MYTHIQUE));
		menu.findItem(R.id.action_is_with_ultra_beast)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.ULTRA_CHIMERE));
		menu.findItem(R.id.action_is_with_in_game)
				.setChecked(PreferencesUtils.getInstance().isOnlyInGame());
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
			PreferencesUtils.getInstance().setWithTag(PkmnTags.MEGA, item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_legendary) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setWithTag(PkmnTags.LEGENDAIRE, item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_mythic) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setWithTag(PkmnTags.MYTHIQUE, item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_ultra_beast) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setWithTag(PkmnTags.ULTRA_CHIMERE, item.isChecked());
		}

		if (item.getItemId() == R.id.action_is_with_in_game) {
			item.setChecked(!item.isChecked());
			PreferencesUtils.getInstance().setOnlyInGame(item.isChecked());
		}


		if (item.getItemId() == R.id.action_minimize) {
			creerNotifBackToApp();
			startNewActivity(getApplicationContext(), "com.nianticlabs.pokemongo");
		}

		if(item.getItemId() == android.R.id.home){
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void creerNotifBackToApp() {

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

		String title = "Test Titre";
		String message = "Test Msg";
		Notification notification = new NotificationCompat.Builder(this, ServiceNotification.SILENT_CHANNEL)
				.setOngoing(true)
				.setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(R.drawable.ic_app)
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setContentIntent(pendingIntent)
				.setPriority(NotificationCompat.PRIORITY_MIN)
				.setOnlyAlertOnce(true)
				.build();


		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
		notificationManager.notify(0, notification);
	}

	public void startNewActivity(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent == null) {
			// Bring user to the market or let them choose an app?
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + packageName));
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
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