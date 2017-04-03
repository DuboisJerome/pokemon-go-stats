/**
 *
 */
package com.pokemongostats.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.view.fragments.DefaultFragment;
import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

/**
 * @author Zapagon
 */
public abstract class FragmentSwitcherFragment extends DefaultFragment {

    private FragmentSwitcher switcher;

    public FragmentSwitcherFragment() {
        switcher = createSwitcher();
    }

    protected abstract FragmentSwitcher createSwitcher();

    public FragmentSwitcher getSwitcher() {
        return switcher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switcher.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return switcher.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        switcher.onResume();
    }

    @Override
    public void onPause() {
        switcher.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        switcher.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        switcher.onRestoreInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        switcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        switcher.onStop();
    }

    @Override
    public void onBackPressed() {
        switcher.onBackPressed();
    }
}
