package com.pokemongostats.view.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.R;
import com.pokemongostats.controller.services.OverlayService;
import com.pokemongostats.view.PkmnGoStatsApplication;

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
            if (service != null) {
                service.maximize();
            }
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
}
