package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class FragmentSwitcher {

	protected FragmentActivity mFragmentActivity;

	protected Fragment mCurrentFragment = null;

	public FragmentSwitcher(final FragmentActivity activity) {
		this.setFragmentActivity(activity);
	}

	public FragmentActivity getFragmentActivity() {
		return mFragmentActivity;
	}

	public void setFragmentActivity(FragmentActivity fragmentActivity) {
		this.mFragmentActivity = fragmentActivity;
	}

	public void onCreate(Bundle savedInstanceState) {
	}

	public void onStart() {
	}

	public void onRestoreInstanceState(Bundle bundle) {
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void onSaveInstanceState(Bundle outState) {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public abstract void onBackPressed();
}
