package com.pokemongostats.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.commons.FilterPokemonView;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;

/**
 * @author Zapagon
 */
public class FilterPokemonDialogFragment extends CustomDialogFragment implements Observer, Observable {

    private final Observable observableImpl = new ObservableImpl(this);
    final android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private FilterPokemonView filterView;

    final android.content.DialogInterface.OnClickListener positiveListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            notifyObservers();
            dialog.dismiss();
        }
    };

    public FilterPokemonDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        filterView = new FilterPokemonView(getActivity());
        filterView.registerObserver(this);
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setPositiveButton(android.R.string.search_go, positiveListener)
                .setView(filterView).create();
    }

    @Override
    public void update(Observable o) {
        if(o == null){
            return;
        }
        if(o.equals(filterView)){
            notifyObservers();
        }
    }

    @NonNull
    public PkmnDescFilterInfo getFilterInfos() {
        return filterView == null ? new PkmnDescFilterInfo() : filterView.getFilterInfos();
    }

    @Override
    public void registerObserver(Observer o) {
        observableImpl.registerObserver(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observableImpl.unregisterObserver(o);
    }

    @Override
    public void notifyObservers() {
        observableImpl.notifyObservers();
    }
}
