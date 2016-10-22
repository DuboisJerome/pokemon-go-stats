/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pokemongostats.view.dialogs;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Pokemon;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.commons.HasRequiredField;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public abstract class AddPkmnDialog extends CustomDialogFragment implements HasRequiredField {

	/** Spinner displaying pokemons' names */
	private Spinner pokedex;

	/** */
	private EditText cpEditText;

	/** Pokedex adapter */
	private ArrayAdapter<PokemonDescription> pokedexAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater.from(getActivity().getApplicationContext())
				.inflate(R.layout.fragment_select_pkmn, null);

		// instances
		pokedex = (Spinner) form.findViewById(R.id.pokedex);
		pokedexAdapter = new ArrayAdapter<PokemonDescription>(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		pokedexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pokedexAdapter.addAll(((PkmnGoStatsApplication) getActivity().getApplication()).getPokedex());

		pokedex.setAdapter(pokedexAdapter);

		cpEditText = (EditText) form.findViewById(R.id.cpEditText);

		// buttons listeners
		OnClickListener onClickAdd = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				int position = pokedex.getSelectedItemPosition();
				if (position != AdapterView.INVALID_POSITION) {
					PokemonDescription pokemonDesc = pokedexAdapter.getItem(position);
					// TODO ...
					Pokemon p = new Pokemon();
					// p.setPokedexNum(pokedexNum);
					// p.setCP(cp);
					// p.setHP(hp);
					// p.setAttackIV(attackIV);
					// p.setDefenseIV(defenseIV);
					// p.setStaminaIV(staminaIV);
					// p.setLevel(level);
					// p.setNickname(nickname);
					// p.setOwner(ownerID);

					// insert into TODO

					onPokemonAdded(p);
				} else {
					// TODO message "you must select a pokemon in pokedex"
				}
			}
		};

		OnClickListener onClickCancel = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				getDialog().cancel();
			}
		};

		/// build add gym dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(form);
		builder.setTitle(R.string.add_pokemon_dialog_title);
		builder.setPositiveButton(R.string.select, onClickAdd);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Override this method to make an action when async add action end
	 */
	public abstract void onPokemonAdded(final Pokemon selectedPokemon);

	@Override
	public boolean checkAllField() {
		return true;
	}
}
