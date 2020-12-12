package com.pokemongostats.view.fragment;

import android.os.Bundle;

import com.pokemongostats.view.switcher.DataFragmentSwitcher;
import com.pokemongostats.view.switcher.FragmentSwitcher;

/**
 * @author Zapagon
 */
public class DataFragment extends FragmentSwitcherFragment {
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
        return new DataFragmentSwitcher(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
