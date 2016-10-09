/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.R;
import com.pokemongostats.view.fragments.switcher.PokedexFragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author Zapagon
 *
 */
public class PokedexActivity extends FragmentActivity {

	private PokedexFragmentSwitcher switcher = new PokedexFragmentSwitcher(
			this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_fragment_activity);

		switcher.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		switcher.onSaveInstanceState(outState);
	}
}
