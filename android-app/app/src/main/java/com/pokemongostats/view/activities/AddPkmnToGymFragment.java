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
public class AddPkmnToGymFragment extends FragmentSwitcherFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwitcher().onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSwitcher().onSaveInstanceState(outState);
    }

    @Override
    protected FragmentSwitcher createSwitcher() {
        return new AddPkmnToGymFragmentSwitcher(this);
    }
}
