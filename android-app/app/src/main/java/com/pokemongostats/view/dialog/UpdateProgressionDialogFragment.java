package com.pokemongostats.view.dialog;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;

import com.pokemongostats.R;
import com.pokemongostats.controller.external.ServiceUpdateDataPokedex;
import com.pokemongostats.databinding.DialogUpdateStatusBinding;
import com.pokemongostats.model.bean.UpdateStatus;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;

import fr.commons.generique.ui.AbstractItemDialogFragment;
import lombok.Getter;

public class UpdateProgressionDialogFragment extends AbstractItemDialogFragment<PokedexData> {

	private DialogUpdateStatusBinding binding;
	@Getter
	private final UpdateStatus status;

	public UpdateProgressionDialogFragment(@NonNull PokedexData pokedexData) {
		super(pokedexData);
		this.status = new UpdateStatus();
		this.status.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
			@Override
			public void onPropertyChanged(Observable sender, int propertyId) {
				// RAS
			}
		});
	}

	@Override
	protected void onClickValider(View v) {
		if (this.status.getProgressionGlobale() == 0) {
			this.btnValider.setEnabled(false);
			this.btnValider.setTextColor(Color.GRAY);
			this.btnValider.invalidate();
			this.binding.progressbar.setVisibility(View.VISIBLE);
			this.binding.progressbarSecondaire.setVisibility(View.VISIBLE);
			this.binding.textViewSecondaryProgress.setVisibility(View.VISIBLE);
			ServiceUpdateDataPokedex.getPokedexDataAsync(UpdateProgressionDialogFragment.this);
		} else {
			super.onClickValider(v);
		}
	}


	@Override
	protected ViewDataBinding createBinding() {
		this.binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
				R.layout.dialog_update_status, null, false);
		this.status.setNomEtape(getString(R.string.askDownload));
		this.binding.setStatus(this.status);
		return this.binding;
	}

	public void onCompleteGetPokedexData(PokedexData p) {
		this.item = p;
		this.status.setNomEtape("Finnish");
		this.btnValider.setEnabled(true);
		this.btnValider.setTextColor(Color.GREEN);
		this.btnValider.invalidate();
		this.btnValider.performClick();
	}

}