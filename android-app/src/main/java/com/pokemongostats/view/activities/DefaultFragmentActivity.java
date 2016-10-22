/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

/**
 * @author Zapagon
 *
 */
public abstract class DefaultFragmentActivity extends FragmentActivity {

	protected FragmentSwitcher switcher;

	protected PkmnGoStatsApplication mPkmnGoHelperApplication;

	public DefaultFragmentActivity() {
		switcher = createSwitcher();
	}

	protected abstract FragmentSwitcher createSwitcher();

	public FragmentSwitcher getSwitcher() {
		return switcher;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switcher.onCreate(savedInstanceState);

		mPkmnGoHelperApplication = (PkmnGoStatsApplication) this
				.getApplicationContext();
		// don't show keyboard on activity start
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mPkmnGoHelperApplication.setCurrentActivity(this);
		switcher.onResume();
	}

	@Override
	protected void onPause() {
		switcher.onPause();
		clearReferences();
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
		if (this.equals(mPkmnGoHelperApplication.getCurrentActivity())) {
			mPkmnGoHelperApplication.setCurrentActivity(null);
		}
	}

	@Override
	public void onBackPressed() {
		switcher.onBackPressed();
	}
}
