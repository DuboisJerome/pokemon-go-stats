package com.pokemongostats.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.commons.ChooseTypeView;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.listeners.SelectedVisitor;

/**
 * Created by Zapagon on 19/03/2017.
 */
public class ChooseTypeDialogFragment extends DialogFragment {

    private SelectedVisitor<Type> visitor;

    private Type currentType;

    public void setVisitor(SelectedVisitor<Type> visitor) {
        this.visitor = visitor;
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        final ChooseTypeView chooseTypeView = new ChooseTypeView(
                getActivity(), currentType, visitor);
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setView(chooseTypeView).create();
    }
}