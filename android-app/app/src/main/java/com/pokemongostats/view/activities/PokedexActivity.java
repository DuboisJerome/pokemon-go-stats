/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.pokedex.PokedexFragmentSwitcher;

import android.os.Bundle;

/**
 * @author Zapagon
 *
 */
public class PokedexActivity extends CustomAppCompatActivity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FragmentSwitcher createSwitcher() {
		return new PokedexFragmentSwitcher(this);
	}
}
