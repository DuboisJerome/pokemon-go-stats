package com.pokemongostats.view;

import android.app.Application;

import androidx.fragment.app.FragmentActivity;

import com.pokemongostats.model.bean.ExceptionHandler;

/**
 * @author Zapagon
 */
public class PkmnGoStatsApplication extends Application {


    private FragmentActivity mCurrentActivity = null;

    /**
     * @return mCurrentActivity
     */
    public FragmentActivity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * @param mCurrentActivity current activity
     */
    public void setCurrentActivity(FragmentActivity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

}
