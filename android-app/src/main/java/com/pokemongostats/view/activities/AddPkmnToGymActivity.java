/**
 * 
 */
package com.pokemongostats.view.activities;

import com.pokemongostats.view.fragments.switcher.AddPkmnToGymFragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

import android.os.Bundle;

/**
 * @author Zapagon
 *
 */
public class AddPkmnToGymActivity extends CustomAppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switcher.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		switcher.onSaveInstanceState(outState);
	}

	@Override
	protected FragmentSwitcher createSwitcher() {
		return new AddPkmnToGymFragmentSwitcher(this);
	}
}
