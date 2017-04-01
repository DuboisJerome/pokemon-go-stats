package com.pokemongostats.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.commons.FilterMoveView;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;

/**
 * @author Zapagon
 */
public class FilterMoveDialogFragment extends CustomDialogFragment implements Observable,Observer {

    private final Observable observableImpl = new ObservableImpl(this);

    private final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private FilterMoveView filterView;

    private final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            notifyObservers();
            dialog.dismiss();
        }
    };

    public FilterMoveDialogFragment() {
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        filterView = new FilterMoveView(getActivity());
        filterView.registerObserver(this);
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setPositiveButton(android.R.string.search_go, positiveListener)
                .setView(filterView).create();
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
    public MoveFilterInfo getFilterInfos(){
        return filterView == null ? new MoveFilterInfo() : filterView.getFilterInfos();
    }
}
