package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.activities.FragmentSwitcherActivity;

public abstract class FragmentSwitcher {

    protected FragmentSwitcherActivity mFragmentActivity;

    public FragmentSwitcher(final FragmentSwitcherActivity activity) {
        this.setFragmentActivity(activity);
    }

    public CustomAppCompatActivity getFragmentActivity() {
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
    }

    public abstract void onBackPressed();

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
