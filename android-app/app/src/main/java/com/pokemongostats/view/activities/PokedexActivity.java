/**
 * 
 */
package com.pokemongostats.view.activities;

import android.os.Bundle;

import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.pokedex.PokedexFragmentSwitcher;

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
