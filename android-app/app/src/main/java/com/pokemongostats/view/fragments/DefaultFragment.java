package com.pokemongostats.view.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.fragments.switcher.HasBack;

/**
 * Created by Zapagon on 02/04/2017.
 * Must be used in an Activity extending MainActivity
 */
public abstract class DefaultFragment extends Fragment implements HasBack {

    public DefaultFragment(){
    }

    public MainActivity getMainActivity() {
        Activity a = getActivity();
        if(a instanceof MainActivity){
            return (MainActivity)a;
        }
        return null;
    }
}
