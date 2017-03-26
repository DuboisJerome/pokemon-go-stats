/**
 *
 */
package com.pokemongostats.view.activities;

import android.os.Bundle;

import com.pokemongostats.view.fragments.switcher.AddPkmnToGymFragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

/**
 * @author Zapagon
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
