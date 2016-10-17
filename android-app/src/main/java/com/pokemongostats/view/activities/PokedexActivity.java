/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.PokedexFragmentSwitcher;

import android.os.Bundle;

/**
 * @author Zapagon
 *
 */
public class PokedexActivity extends DefaultFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected FragmentSwitcher createSwitcher() {
		return new PokedexFragmentSwitcher(this);
	}
}
