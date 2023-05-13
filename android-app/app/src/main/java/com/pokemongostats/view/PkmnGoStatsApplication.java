package com.pokemongostats.view;

import android.app.Application;

import androidx.fragment.app.FragmentActivity;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.ExceptionHandler;
import com.pokemongostats.view.utils.PreferencesUtils;

import fr.commons.generique.controller.db.DBHelper;

/**
 * @author Zapagon
 */
public class PkmnGoStatsApplication extends Application {


	private FragmentActivity mCurrentActivity = null;

	/**
	 * @return mCurrentActivity
	 */
	public FragmentActivity getCurrentActivity() {
		return this.mCurrentActivity;
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
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		DBHelper db = new DBHelper(getApplicationContext(), getApplicationContext().getString(R.string.db_name), getApplicationContext().getResources().getInteger(R.integer.db_version));
		DBHelper.setInstance(db);
		PreferencesUtils.initInstance(getApplicationContext());
	}

}