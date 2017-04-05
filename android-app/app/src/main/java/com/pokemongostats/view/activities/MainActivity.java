package com.pokemongostats.view.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.fragments.DefaultFragment;
import com.pokemongostats.view.fragments.DmgSimuFragment;
import com.pokemongostats.view.fragments.PokedexFragment;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class MainActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    public static String TAG_POKEDEX = PokedexFragment.class.getSimpleName();
    public static String TAG_SIMULATOR = DmgSimuFragment.class.getSimpleName();
    public static String NEXT_TAG = TAG_POKEDEX;
    public static String LAST_TAG = "";

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private Handler mHandler;

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

    public OverlayService getService() {
        return service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_fragment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_slideshow);
            supportActionBar.setHomeActionContentDescription(R.string.home_btn_desc);
        }

        mHandler = new Handler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            NEXT_TAG = TAG_POKEDEX;
            loadFragment();
        }
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
//        tb.inflateMenu(R.menu.main_menu);
//        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return onOptionsItemSelected(item);
//            }
//        });
//        tb.getMenu().findItem(R.id.action_is_last_evolution_only)
//                .setChecked(PreferencesUtils.isLastEvolutionOnly(getApplicationContext()));

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        mCurrentFragment.onBackPressed();
    }

    public DefaultFragment getPokedexFragment(final FragmentTransaction ft) {
        String tag = TAG_POKEDEX;
        FragmentManager fm = getSupportFragmentManager();
        PokedexFragment f = (PokedexFragment) fm.findFragmentByTag(tag);
        if (null == f) {
            // create Fragment
            f = new PokedexFragment();
            ft.addToBackStack(tag);
            Log.i(TagUtils.DEBUG, "Create PokedexFragment");
        } else {
            Log.i(TagUtils.DEBUG, "Pop PokedexFragment");
        }
        return f;
    }

    public DefaultFragment getSimulatorFragment(final FragmentTransaction ft) {
        String tag = TAG_SIMULATOR;
        FragmentManager fm = getSupportFragmentManager();
        DmgSimuFragment f = (DmgSimuFragment) fm.findFragmentByTag(tag);
        if (null == f) {
            // create Fragment
            f = new DmgSimuFragment();
            ft.addToBackStack(tag);
            Log.i(TagUtils.DEBUG, "Create DmgSimuFragment");
        } else {
            Log.i(TagUtils.DEBUG, "Pop DmgSimuFragment");
        }
        return f;
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation mDrawerLayout
        if (LAST_TAG.equals(NEXT_TAG)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                final FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction fTransaction = fm.beginTransaction();
                final DefaultFragment f = getOrCreateFragment(fTransaction);
                fTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fTransaction.replace(R.id.fragment_container, f, NEXT_TAG);
                fTransaction.show(f);
                fTransaction.commitAllowingStateLoss();
                mCurrentFragment = f;
            }
        };

        mHandler.post(mPendingRunnable);

        //Closing mDrawerLayout on item click
        mDrawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private DefaultFragment getOrCreateFragment(final FragmentTransaction ft) {
        DefaultFragment f;
        switch (navItemIndex) {
            case 0:
                // pokedex
                f = getPokedexFragment(ft);
                break;
            case 1:
                // simulator
                f = getSimulatorFragment(ft);
                break;
            default:
                f = getPokedexFragment(ft);
        }
        return f;
    }

    private void setToolbarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }
    }

    private void selectNavMenu() {
        mNavigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_pokedex:
                        navItemIndex = 0;
                        NEXT_TAG = TAG_POKEDEX;
                        break;
                    case R.id.nav_simulator:
                        navItemIndex = 1;
                        NEXT_TAG = TAG_SIMULATOR;
                        break;
                    case R.id.nav_about_us:
                        showAboutUs();
                        mDrawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the mDrawerLayout closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the mDrawerLayout open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to mDrawerLayout layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void showAboutUs(){
        final Spanned s = Html.fromHtml(getString(R.string.about_content));

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.about))
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
    }
}
