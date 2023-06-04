package com.pokemongostats.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pokemongostats.MobileNavigationDirections;
import com.pokemongostats.R;
import com.pokemongostats.controller.ServiceNotification;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.databinding.ActivityOneFragmentBinding;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.dialog.FiltrePrefPkmnDialogFragment;
import com.pokemongostats.view.dialog.UpdateProgressionDialogFragment;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityOneFragmentBinding binding = ActivityOneFragmentBinding.inflate(getLayoutInflater());
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

	private void setupNotificationChannels() {
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
	public boolean onCreateOptionsMenu(
			@NonNull Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		MenuCompat.setGroupDividerEnabled(menu, true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getGroupId() == R.id.group_menu_pref_filtre) {
			if (item.getItemId() == R.id.action_filtre_pkmn) {
				FiltrePrefPkmnDialogFragment dialog = new FiltrePrefPkmnDialogFragment(PreferencesUtils.getInstance().getFiltrePkmn());
				dialog.addOnItemValideListener(PreferencesUtils.getInstance()::updateFiltrePkmn);
				dialog.show(getSupportFragmentManager(), "FILTRE_PKMN");
			}
			return true;
		}

		if (item.getItemId() == R.id.action_minimize) {
			ServiceNotification.creerNotifBackToApp(this);
			ouvrirAppPokemonGo(this);
			return true;
		}

		if (item.getItemId() == R.id.update_from_external) {

			UpdateProgressionDialogFragment dialog = new UpdateProgressionDialogFragment(new PokedexData());
			dialog.addOnItemValideListener(pokedexData -> {
				Log.info("Redirection vers IncomingDataFragment");
				MobileNavigationDirections.ActionToIncomingData action = MobileNavigationDirections.actionToIncomingData(pokedexData);
				Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigate(action);
			});
			dialog.setCancelable(false);
			dialog.show(getSupportFragmentManager(), "UPATE_PROGRESS");
			return true;
		}

		if (item.getItemId() == R.id.action_about_us) {
			showAboutUs();
			return true;
		}

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void ouvrirAppPokemonGo(Context ctx) {
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