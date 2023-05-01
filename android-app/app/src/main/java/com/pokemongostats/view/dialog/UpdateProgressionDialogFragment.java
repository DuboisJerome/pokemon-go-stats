package com.pokemongostats.view.dialog;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;

import com.pokemongostats.R;
import com.pokemongostats.databinding.DialogUpdateStatusBinding;
import com.pokemongostats.model.bean.UpdateStatus;

import fr.commons.generique.ui.AbstractItemDialogFragment;

public class UpdateProgressionDialogFragment extends AbstractItemDialogFragment<UpdateStatus> {
	private DialogUpdateStatusBinding binding;

	public UpdateProgressionDialogFragment(@NonNull UpdateStatus status) {
		super(status);
	}

	@Override
	protected ViewDataBinding createBinding() {
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
				R.layout.dialog_update_status, null, false);
		binding.setStatus(this.item);
		this.item.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
			@Override
			public void onPropertyChanged(Observable sender, int propertyId) {
				updateProgressBar();
			}
		});
		return binding;
	}

	private void updateProgressBar(){
		if(item.getProgress() >= 100){
			item.setMainMsg("Finnish");
			item.setSecondaryMsg("");
		}
	}
}