package com.pokemongostats.view.fragments.switcher;

import android.support.v4.app.FragmentActivity;

public abstract class FragmentSwitcher {

	private FragmentActivity mFragmentActivity;

	public FragmentSwitcher(final FragmentActivity activity) {
		this.setFragmentActivity(activity);
	}

	public FragmentActivity getFragmentActivity() {
		return mFragmentActivity;
	}

	public void setFragmentActivity(FragmentActivity mFragmentActivity) {
		this.mFragmentActivity = mFragmentActivity;
	}
}
