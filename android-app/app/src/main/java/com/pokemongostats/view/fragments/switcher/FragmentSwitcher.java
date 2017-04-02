package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.FragmentSwitcherActivity;

public abstract class FragmentSwitcher {

    protected FragmentSwitcherActivity mFragmentActivity;

    private HistoryService<CompensableCommand> historyService = new HistoryService<>();

    public FragmentSwitcher(final FragmentSwitcherActivity activity) {
        this.setFragmentActivity(activity);
    }

    public FragmentSwitcherActivity getFragmentActivity() {
        return mFragmentActivity;
    }

    public void setFragmentActivity(FragmentSwitcherActivity fragmentActivity) {
        this.mFragmentActivity = fragmentActivity;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

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
