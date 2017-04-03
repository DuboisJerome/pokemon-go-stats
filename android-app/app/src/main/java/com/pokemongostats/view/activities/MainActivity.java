package com.pokemongostats.view.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.fragments.DmgSimuFragment;
import com.pokemongostats.view.fragments.PokedexFragment;
import com.pokemongostats.view.fragments.DefaultFragment;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class MainActivity extends AppCompatActivity {

    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private DefaultFragment mCurrentFragment;

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

    private final BroadcastReceiver exitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isFinishing()) {
                MainActivity.this.setResult(RESULT_OK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    MainActivity.this.finishAndRemoveTask();
                } else {
                    MainActivity.this.finish();
                }
            }
        }
    };
    private int lastPosition = -1;
    private CharSequence mTitle, mDrawerTitle;

    public OverlayService getService() {
        return service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_slideshow);
            supportActionBar.setHomeActionContentDescription(R.string.home_btn_desc);
        }

        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mTitle = mDrawerTitle = getTitle();
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, mMenuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        registerReceiver(exitReceiver, new IntentFilter("EXIT"));
        selectMenu(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
        app.setCurrentActivity(this);
        Intent intent = new Intent(this, OverlayService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        unbindService(mConnection);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        unregisterReceiver(exitReceiver);
        super.onDestroy();
    }

    private void clearReferences() {
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplication());
        if (this.equals(app.getCurrentActivity())) {
            app.setCurrentActivity(null);
        }
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
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        Context c = getApplicationContext();
        AlertDialog dialog;
        switch (item.getItemId()) {
            case R.id.action_style_choose:

                int style_number = 0;
                final int POSITION_ROUND = style_number++;
                final int POSITION_FLAT = style_number++;

                CharSequence[] choices = new CharSequence[style_number];
                choices[POSITION_ROUND] = getString(R.string.style_round);
                choices[POSITION_FLAT] = getString(R.string.style_flat);

                // retrieve checked style
                int checkedStyleId = PreferencesUtils.getStyleId(c);
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

                dialog = new AlertDialog.Builder(this).setTitle("Choose style")
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
                        }).setCancelable(true).create();
                dialog.show();
                break;
            case R.id.action_is_last_evolution_only:
                item.setChecked(!item.isChecked());
                PreferencesUtils.setLastEvolutionOnly(c, item.isChecked());
                break;
            case R.id.action_about:
                final Spanned s = Html.fromHtml(c.getString(R.string.about_content));

                dialog = new AlertDialog.Builder(this).setTitle(c.getString(R.string.about))
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setMessage(s).setCancelable(true).create();
                dialog.show();
                // Make the textview clickable. Must be called after show()
                ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                break;
            case R.id.action_minimize:
                if (getService() != null) {
                    getService().minimize();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectMenu(position);
        }
    }

    private void selectMenu(int position) {
        if (lastPosition == position) {
            return;
        }
        switch (position) {
            case 0:
                showPokedexFragment();
                break;
            case 1:
                showSimulatorFragment();
                break;
            default:
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        lastPosition = position;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //

        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        mCurrentFragment.onBackPressed();
    }

    public void showPokedexFragment(){
        String tag = PokedexFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        PokedexFragment f = (PokedexFragment) fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (null == f) {
            // create Fragment
            f = new PokedexFragment();
            fTransaction.addToBackStack(tag);
            Log.i(TagUtils.DEBUG, "Create PokedexFragment");
        } else {
            Log.i(TagUtils.DEBUG, "Pop PokedexFragment");
        }
        fTransaction.replace(R.id.fragment_container, f, tag);
        fTransaction.show(f);
        fTransaction.commit();
        mCurrentFragment = f;
    }

    public void showSimulatorFragment(){
        String tag = DmgSimuFragment.class.getName();
        FragmentManager fm = getSupportFragmentManager();
        DmgSimuFragment f = (DmgSimuFragment) fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (null == f) {
            // create Fragment
            f = new DmgSimuFragment();
            fTransaction.addToBackStack(tag);
            Log.i(TagUtils.DEBUG, "Create DmgSimuFragment");
        } else {
            Log.i(TagUtils.DEBUG, "Pop DmgSimuFragment");
        }
        fTransaction.replace(R.id.fragment_container, f, tag);
        fTransaction.show(f);
        fTransaction.commit();
        mCurrentFragment = f;
    }
}
