package com.pokemongostats.view.fragment;

import android.os.Bundle;

import com.pokemongostats.view.switcher.FragmentSwitcher;
import com.pokemongostats.view.switcher.PokedexFragmentSwitcher;

/**
 * @author Zapagon
 */
public class PokedexFragment extends FragmentSwitcherFragment {
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

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}
