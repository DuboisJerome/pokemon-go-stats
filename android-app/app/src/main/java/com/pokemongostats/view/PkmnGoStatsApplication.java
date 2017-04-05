/**
 *
 */
package com.pokemongostats.view;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.model.bean.ExceptionHandler;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
