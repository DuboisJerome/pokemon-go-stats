package com.pokemongostats.view.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.utils.ImageHelperUtils;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public abstract class CustomAppCompatActivity extends AppCompatActivity {

    private ViewGroup mContainer;
    private View mFragmentContent;
    private OverlayService service;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            OverlayService.OverlayServiceBinder b = (OverlayService.OverlayServiceBinder) binder;
            service = b.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            service = null;
        }
    };

    public OverlayService getService() {
        return service;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one_fragment);
        mContainer = (ViewGroup) findViewById(R.id.fragment_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
        app.setCurrentActivity(this);
        app.setCurrentActivityIsVisible(true);
        Intent intent = new Intent(this, OverlayService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
        if (this.equals(app.getCurrentActivity())) {
            app.setCurrentActivityIsVisible(false);
        }
        unbindService(mConnection);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
        if (this.equals(app.getCurrentActivity())) {
            app.setCurrentActivity(null);
            app.setCurrentActivityIsVisible(false);
        }
    }

    public View initFragmentContent(final int layoutId) {
        mFragmentContent = View.inflate(this, layoutId, mContainer);
        return mFragmentContent;
    }

    public View getFragmentContent() {
        return mFragmentContent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.main_menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        tb.getMenu().findItem(R.id.action_is_last_evolution_only)
                .setChecked(PreferencesUtils.isLastEvolutionOnly(getApplicationContext()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_style_choose:

                int style_number = 0;
                final int POSITION_ROUND = style_number++;
                final int POSITION_FLAT = style_number++;

                CharSequence[] choices = new CharSequence[style_number];
                choices[POSITION_ROUND] = getString(R.string.style_round);
                choices[POSITION_FLAT] = getString(R.string.style_flat);

                // retrieve checked style
                int checkedStyleId = PreferencesUtils.getStyleId(getApplicationContext());
                final int checkedPosition;
                switch (checkedStyleId) {
                    case R.drawable.type_round:
                        checkedPosition = POSITION_ROUND;
                        break;
                    case R.drawable.type_flat:
                        checkedPosition = POSITION_FLAT;
                        break;
                    default:
                        checkedPosition = POSITION_FLAT;
                        break;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Choose style")
                        .setSingleChoiceItems(choices, checkedPosition, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                if (POSITION_ROUND == position) { // round
                                    PreferencesUtils.setStyleId(getApplicationContext(), R.drawable.type_round);
                                } else if (POSITION_FLAT == position) { // flat
                                    PreferencesUtils.setStyleId(getApplicationContext(), R.drawable.type_flat);
                                }
                                dialog.dismiss();
                            }
                        }).setCancelable(true);
                builder.create().show();
                return true;
            case R.id.action_is_last_evolution_only:
                item.setChecked(!item.isChecked());
                PreferencesUtils.setLastEvolutionOnly(getApplication(), item.isChecked());
                break;
            case R.id.action_minimize:
                if (getService() != null) {
                    getService().minimize();
                }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
