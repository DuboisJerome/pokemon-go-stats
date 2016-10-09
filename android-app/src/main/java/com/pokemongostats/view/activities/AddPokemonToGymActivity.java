/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.R;
import com.pokemongostats.view.fragments.switcher.AddPokemonToGymFragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author Zapagon
 *
 */
public class AddPokemonToGymActivity extends FragmentActivity {

	private AddPokemonToGymFragmentSwitcher switcher = new AddPokemonToGymFragmentSwitcher(
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
