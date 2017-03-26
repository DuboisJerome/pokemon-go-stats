package com.pokemongostats.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.commons.FilterMoveView;

/**
 * @author Zapagon
 */
public class FilterMoveDialogFragment extends CustomDialogFragment {

    final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private FilterMoveView filterMoveView;

    private OnFilterMove onFilterMove;
    final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (filterMoveView != null && onFilterMove != null) {
                onFilterMove.onFilter(filterMoveView.getFilterInfos());
            }
            dialog.dismiss();
        }
    };

    public FilterMoveDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        filterMoveView = new FilterMoveView(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setPositiveButton(android.R.string.search_go, positiveListener)
                .setView(filterMoveView).create();
    }

    public void setOnFilterMove(OnFilterMove onFilterMove) {
        this.onFilterMove = onFilterMove;
    }

    public interface OnFilterMove {
        void onFilter(final MoveFilterInfo infos);
    }
}
