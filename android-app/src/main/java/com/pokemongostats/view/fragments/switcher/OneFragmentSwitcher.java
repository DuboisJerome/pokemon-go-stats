package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class OneFragmentSwitcher extends FragmentSwitcher {

	protected Fragment mCurrentFragment = null;

	public OneFragmentSwitcher(final FragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentActivity.setContentView(R.layout.one_fragment_activity);
	}
}
