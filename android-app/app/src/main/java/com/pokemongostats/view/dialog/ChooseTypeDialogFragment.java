package com.pokemongostats.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.commons.ChooseTypeView;
import com.pokemongostats.view.listeners.SelectedVisitor;

import java.util.Objects;

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
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogInterface.OnClickListener cancelListener = (dialog, which) -> dialog.dismiss();
        DialogInterface.OnClickListener noneListener = (dialog, which) -> {
            visitor.select(null);
            dialog.dismiss();
        };

        final ChooseTypeView chooseTypeView = new ChooseTypeView(
                getActivity());
        chooseTypeView.setCurrentType(currentType);
        chooseTypeView.setCallbackType(visitor);
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setNeutralButton(R.string.none, noneListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setView(chooseTypeView).create();
    }
}