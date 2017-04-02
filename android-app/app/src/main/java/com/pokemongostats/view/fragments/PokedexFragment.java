package com.pokemongostats.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.activities.FragmentSwitcherFragment;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;
import com.pokemongostats.view.fragments.switcher.pokedex.PokedexFragmentSwitcher;

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
