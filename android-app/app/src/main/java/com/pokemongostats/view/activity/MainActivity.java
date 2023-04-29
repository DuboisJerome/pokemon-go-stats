package com.pokemongostats.view.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pokemongostats.R;
import com.pokemongostats.controller.ServiceNotification;
import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.databinding.ActivityOneFragmentBinding;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		com.pokemongostats.databinding.ActivityOneFragmentBinding binding = ActivityOneFragmentBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		// Liste des menus du bas
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_lst_pkmn, R.id.navigation_lst_move, R.id.navigation_type)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView, navController);
		binding.navView.setItemIconTintList(null);

		// Setup les channels des notifs
		setupNotificationChannels();

	}

	private void setupNotificationChannels(){
		ServiceNotification.creerChannelNotifBackToApp(getApplicationContext());
		ServiceNotification.creerChannelNotifSilencieux(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
		app.setCurrentActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		clearReferences();
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
		MenuCompat.setGroupDividerEnabled(menu, true);

		menu.findItem(R.id.action_is_last_evolution_only)
				.setChecked(PreferencesUtils.getInstance().isLastEvolutionOnly());
		menu.findItem(R.id.action_is_with_mega_evolution)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.MEGA));
		menu.findItem(R.id.action_is_with_legendary)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.LEGENDAIRE));
		menu.findItem(R.id.action_is_with_mythic)
				.setChecked(PreferencesUtils.getInstance().isWithTag(PkmnTags.FABULEUX));
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

		if(item.getGroupId() == R.id.group_menu_pref_filtre){
			item.setChecked(!item.isChecked());
			String tag = "";
			if (item.getItemId() == R.id.action_is_with_mega_evolution) {
				tag = PkmnTags.MEGA;
			}
			if (item.getItemId() == R.id.action_is_with_legendary) {
				tag = PkmnTags.LEGENDAIRE;
			}
			if (item.getItemId() == R.id.action_is_with_mythic) {
				tag = PkmnTags.FABULEUX;
			}
			if (item.getItemId() == R.id.action_is_with_ultra_beast) {
				tag = PkmnTags.ULTRA_CHIMERE;
			}
			if (item.getItemId() == R.id.action_is_last_evolution_only) {
				tag = PreferencesUtils.LAST_EVOLUTION_ONLY;
			}
			if (item.getItemId() == R.id.action_is_with_in_game) {
				tag = PreferencesUtils.ONLY_IN_GAME;
			}
			if(!tag.isEmpty()){
				PreferencesUtils.getInstance().setWithTag(tag, item.isChecked());
			}
			return true;
		}

		if (item.getItemId() == R.id.action_minimize) {
			ServiceNotification.creerNotifBackToApp(this);
			ouvrirAppPokemonGo(this);
			return true;
		}

		if(item.getItemId() == android.R.id.home){
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void ouvrirAppPokemonGo(Context ctx){
		String packageName = "com.nianticlabs.pokemongo";
		Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent == null) {
			// Bring user to the market or let them choose an app?
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + packageName));
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
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