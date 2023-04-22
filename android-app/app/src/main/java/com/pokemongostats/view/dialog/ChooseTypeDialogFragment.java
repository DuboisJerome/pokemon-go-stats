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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by Zapagon on 19/03/2017.
 */
public class ChooseTypeDialogFragment extends DialogFragment {

	private final Consumer<Type> onTypeSelected;

	private final Set<Type> lstCurrentType = new HashSet<>();

	public ChooseTypeDialogFragment(Consumer<Type> onTypeSelected) {
		this.onTypeSelected = onTypeSelected;
	}

	public void addCurrentType(Type currentType) {
		if (currentType != null) {
			this.lstCurrentType.add(currentType);
		}
	}

	public void removeCurrentType(Type currentType) {
		if (currentType != null) {
			this.lstCurrentType.remove(currentType);
		}
	}

	public void clearCurrentType() {
		this.lstCurrentType.clear();
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		DialogInterface.OnClickListener cancelListener = (dialog, which) -> dialog.dismiss();
		DialogInterface.OnClickListener noneListener = (dialog, which) -> {
			// hide dialog
			dialog.dismiss();
			this.onTypeSelected.accept(null);
			dialog.dismiss();
		};

		ChooseTypeView chooseTypeView = new ChooseTypeView(
				getActivity());
		this.lstCurrentType.forEach(chooseTypeView::addCurrentType);
		chooseTypeView.setCallbackType(t -> {
			this.dismiss();
			this.onTypeSelected.accept(t);
		});
		return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
				.setNeutralButton(R.string.none, noneListener)
				.setNegativeButton(android.R.string.cancel, cancelListener)
				.setView(chooseTypeView).create();
	}
}