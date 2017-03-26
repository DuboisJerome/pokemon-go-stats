/**
 *
 */
package com.pokemongostats.view.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pokemongostats.view.fragments.switcher.FragmentSwitcher;

/**
 * @author Zapagon
 */
public abstract class FragmentSwitcherActivity extends CustomAppCompatActivity {

    protected FragmentSwitcher switcher;

    public FragmentSwitcherActivity() {
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
    protected void onResume() {
        super.onResume();
        switcher.onResume();
    }

    @Override
    protected void onPause() {
        switcher.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        switcher.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        switcher.onRestoreInstanceState(bundle);
    }

    @Override
    public void onBackPressed() {
        switcher.onBackPressed();
    }

}
