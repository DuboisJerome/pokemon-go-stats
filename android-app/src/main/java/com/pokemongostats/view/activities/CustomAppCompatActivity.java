/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.R;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author Zapagon
 *
 */
public abstract class CustomAppCompatActivity extends AppCompatActivity {

	protected FragmentSwitcher switcher;
	protected LinearLayout mContainer;

	/***
	 * The ActionMode callBack
	 */
	protected Callback actionModeCallBack;

	public CustomAppCompatActivity() {
		switcher = createSwitcher();
	}

	protected abstract FragmentSwitcher createSwitcher();

	public FragmentSwitcher getSwitcher() {
		return switcher;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_fragment_activity);
		mContainer = (LinearLayout) findViewById(R.id.fragment_container);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initializeActionModeCallBack();

		switcher.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
		app.setCurrentActivity(this);
		app.setCurrentActivityIsVisible(true);
		switcher.onResume();
	}

	@Override
	protected void onPause() {
		switcher.onPause();
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
		if (this.equals(app.getCurrentActivity())) {
			app.setCurrentActivityIsVisible(false);
		}
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		switcher.onSaveInstanceState(bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		switcher.onRestoreInstanceState(bundle);
	}

	@Override
	protected void onDestroy() {
		clearReferences();
		super.onDestroy();
	}

	private void clearReferences() {
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
		if (this.equals(app.getCurrentActivity())) {
			app.setCurrentActivity(null);
			app.setCurrentActivityIsVisible(false);
		}
	}

	@Override
	public void onBackPressed() {
		switcher.onBackPressed();
	}

	/**
	 * @param v
	 *            The content of the current view to set (toolbar exclude)
	 * @return content inflated
	 */
	public View initContent(final int layoutId) {
		View v = View.inflate(this, layoutId, mContainer);
		return v;
	}

	private void initializeActionModeCallBack() {
		actionModeCallBack = new android.support.v7.view.ActionMode.Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				getMenuInflater().inflate(R.menu.mainmenu, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				Toast.makeText(CustomAppCompatActivity.this,
						"Got click: " + item, Toast.LENGTH_SHORT).show();
				mode.finish();
				return true;
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		tb.inflateMenu(R.menu.mainmenu);
		tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return onOptionsItemSelected(item);
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_style_choose :
				Toast.makeText(this, "Action style selected",
						Toast.LENGTH_SHORT).show();
				String[] choices = getResources()
						.getStringArray(R.array.style_list);
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setTitle("Choose style").setSingleChoiceItems(choices,
								0, new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int position) {
										switch (position) {
											case 0 :// round
												break;
											case 1 :// flat
												break;
											default :
												break;
										}
										dialog.dismiss();
									}
								})
						.setCancelable(true);
				builder.create().show();
				return true;
			default :
				break;
		}

		return super.onOptionsItemSelected(item);
	}
}
