package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class FragmentSwitcher {

	private FragmentActivity mFragmentActivity;

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

	public void onSaveInstanceState(Bundle outState) {
	}
}
