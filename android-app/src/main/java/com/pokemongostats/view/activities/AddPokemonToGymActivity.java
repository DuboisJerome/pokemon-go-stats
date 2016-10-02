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

	private AddPokemonToGymFragmentSwitcher switcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_pokemon_to_gym_activity);

		// if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if (savedInstanceState != null) {
			return;
		}

		switcher = new AddPokemonToGymFragmentSwitcher(this);
	}
}
