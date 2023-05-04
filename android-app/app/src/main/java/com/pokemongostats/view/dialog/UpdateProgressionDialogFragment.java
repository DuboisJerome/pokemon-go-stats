package com.pokemongostats.view.dialog;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;

import com.pokemongostats.R;
import com.pokemongostats.databinding.DialogUpdateStatusBinding;
import com.pokemongostats.model.bean.UpdateStatus;

import java.util.Objects;

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
		if(item.getProgressionGlobale() >= 100){
			item.setNomEtape("Finnish");
			Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
				this.binding.progressbarSecondaire.setVisibility(View.GONE);
				this.binding.textViewSecondaryProgress.setVisibility(View.GONE);
			});
		}
	}
}