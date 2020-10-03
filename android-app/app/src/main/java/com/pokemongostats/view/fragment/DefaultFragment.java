package com.pokemongostats.view.fragment;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.pokemongostats.view.activities.MainActivity;

/**
 * Created by Zapagon on 02/04/2017.
 * Must be used in an Activity extending MainActivity
 */
public abstract class DefaultFragment extends Fragment {

    public DefaultFragment(){
    }

    public MainActivity getMainActivity() {
        Activity a = getActivity();
        if(a instanceof MainActivity){
            return (MainActivity)a;
        }
        return null;
    }

    public abstract void onBackPressed();
}
