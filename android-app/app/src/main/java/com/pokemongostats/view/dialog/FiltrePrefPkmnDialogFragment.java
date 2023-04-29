package com.pokemongostats.view.dialog;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.pokemongostats.R;
import com.pokemongostats.databinding.DialogFiltrePrefPkmnBinding;
import com.pokemongostats.model.bean.FiltrePrefPkmn;

import fr.commons.generique.ui.AbstractItemDialogFragment;

public class FiltrePrefPkmnDialogFragment extends AbstractItemDialogFragment<FiltrePrefPkmn> {

	public FiltrePrefPkmnDialogFragment(@NonNull FiltrePrefPkmn item) {
		super(item);
	}

	@Override
	protected ViewDataBinding createBinding() {
		DialogFiltrePrefPkmnBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
				R.layout.dialog_filtre_pref_pkmn, null, false);
		binding.setPref(this.item);
		return binding;
	}
}