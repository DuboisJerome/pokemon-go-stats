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

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.PokemonDescription;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public abstract class AddPokemonTrainerDialog extends CustomDialogFragment {

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
				.inflate(R.layout.select_pokemon_fragment, null);

		// instances
		pokedex = (Spinner) form.findViewById(R.id.pokedex);
		pokedexAdapter = new ArrayAdapter<PokemonDescription>(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		pokedexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pokedex.setAdapter(pokedexAdapter);
		updatePokedexSpinner();

		cpEditText = (EditText) form.findViewById(R.id.cpEditText);

		// buttons listeners
		OnClickListener onClickAdd = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				// int position = pokemonsSpinner.getSelectedItemPosition();
				// if (position != AdapterView.INVALID_POSITION) {
				// // selected trainer
				// Pokemon pokemon = pokemonAdapter.getItem(position);
				// onPokemonSelected(pokemon);
				// } else {
				// // TODO message "you must select a trainer"
				// }
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
		builder.setTitle(R.string.select_trainer_dialog_title);
		builder.setPositiveButton(R.string.select, onClickAdd);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Update pokedex spinner
	 */
	public void updatePokedexSpinner() {
		new GetAllAsyncTask<PokemonDescription>() {

			@Override
			protected List<PokemonDescription> doInBackground(Long... params) {
				return new PokedexTableDAO(getActivity().getApplicationContext()).selectAll(params);
			}

			@Override
			public void onPostExecute(List<PokemonDescription> list) {
				// on result value update Spinner
				pokedexAdapter.clear();
				if (list != null) {
					pokedexAdapter.addAll(list);
				}
			}
		}.execute();
	}

	/**
	 * Override this method to make an action when async add action end
	 */
	public abstract void onPokemonAdded(final Pokemon selectedPokemon);
}
