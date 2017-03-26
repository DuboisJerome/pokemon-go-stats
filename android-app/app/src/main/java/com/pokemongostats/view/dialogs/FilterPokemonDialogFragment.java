package com.pokemongostats.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.commons.FilterPokemonView;

/**
 * @author Zapagon
 */
public class FilterPokemonDialogFragment extends CustomDialogFragment {

    final android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private FilterPokemonView filterPokemonView;

    private OnFilterPokemon onFilterPokemon;
    final android.content.DialogInterface.OnClickListener positiveListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (filterPokemonView != null && onFilterPokemon != null) {
                onFilterPokemon.onFilter(filterPokemonView.getFilterInfos());
            }
            dialog.dismiss();
        }
    };

    public FilterPokemonDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        filterPokemonView = new FilterPokemonView(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setPositiveButton(android.R.string.search_go, positiveListener)
                .setView(filterPokemonView).create();
    }

    public void setOnFilterPokemon(OnFilterPokemon onFilterPokemon) {
        this.onFilterPokemon = onFilterPokemon;
    }

    public interface OnFilterPokemon {
        void onFilter(final PkmnDescFilterInfo infos);
    }
}
