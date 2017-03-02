package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.view.activities.CustomAppCompatActivity;

import android.os.Bundle;

public abstract class FragmentSwitcher {

	protected CustomAppCompatActivity mFragmentActivity;

	public FragmentSwitcher(final CustomAppCompatActivity activity) {
		this.setFragmentActivity(activity);
	}

	public CustomAppCompatActivity getFragmentActivity() {
		return mFragmentActivity;
	}

	public void setFragmentActivity(CustomAppCompatActivity fragmentActivity) {
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
