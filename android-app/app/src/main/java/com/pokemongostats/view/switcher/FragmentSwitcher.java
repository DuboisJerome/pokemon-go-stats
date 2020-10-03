package com.pokemongostats.view.switcher;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.fragment.FragmentSwitcherFragment;

public abstract class FragmentSwitcher {

    private FragmentSwitcherFragment mParentFragment;
    private Fragment mCurrentFragment;

    private HistoryService<CompensableCommand> historyService = new HistoryService<>();

    public FragmentSwitcher(final FragmentSwitcherFragment parent) {
        this.setParentFragment(parent);
    }

    public FragmentSwitcherFragment getParentFragment() {
        return mParentFragment;
    }

    public void setParentFragment(FragmentSwitcherFragment fragment) {
        this.mParentFragment = fragment;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(Fragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }

    public abstract void onCreate(Bundle savedInstanceState);

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState);

    public void onStart() {
    }

    public void onRestoreInstanceState(Bundle bundle) {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStop() {
    }

    public void onDestroy() {
        historyService.clear();
    }

    public void onBackPressed(){}

    public HistoryService<CompensableCommand> getHistoryService() {
        return historyService;
    }
}
