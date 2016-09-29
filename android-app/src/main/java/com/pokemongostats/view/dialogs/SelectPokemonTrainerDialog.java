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

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.SelectAsyncTask;
import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.Pokemon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class SelectPokemonTrainerDialog extends DialogFragment {

	/** trainers */
	private Spinner pokemonsSpinner;

	/** TODO */
	private ArrayAdapter<Pokemon> pokemonAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater
				.from(getActivity().getApplicationContext())
				.inflate(R.layout.select_pokemon_trainer_dialog, null);
		pokemonsSpinner = (Spinner) form.findViewById(R.id.pokemons);

		//
		pokemonAdapter = new ArrayAdapter<Pokemon>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		pokemonAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		pokemonsSpinner.setAdapter(pokemonAdapter);

		// buttons listeners
		OnClickListener onClickSelect = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				int position = pokemonsSpinner.getSelectedItemPosition();
				if (position != AdapterView.INVALID_POSITION) {
					// selected trainer
					Pokemon pokemon = pokemonAdapter.getItem(position);
					onPokemonSelected(pokemon);
				} else {
					// TODO message "you must select a trainer"
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
		builder.setTitle(R.string.select_trainer_dialog_title);
		builder.setPositiveButton(R.string.select, onClickSelect);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Update trainers spinner
	 */
	public void updatePokemonSpinner() {
		StringBuilder b = new StringBuilder();
		b.append("SELECT * FROM ");
		b.append(PokemonTableDAO.TABLE_NAME).append(" t1 ");
		b.append("JOIN ");
		b.append(TrainerTableDAO.TABLE_NAME).append(" t2 ");
		b.append("WHERE ");
		b.append("t1.").append(PokemonTableDAO.OWNER_ID);
		b.append("=");
		b.append("t2.").append(TrainerTableDAO.ID);

		new SelectAsyncTask<Pokemon>() {

			@Override
			protected List<Pokemon> doInBackground(String... queries) {
				PokemonTableDAO dao = new PokemonTableDAO(
						getActivity().getApplicationContext());
				List<Pokemon> list = new ArrayList<Pokemon>();
				if (queries != null && queries.length > 0) {
					for (String query : queries) {
						if (query != null) {
							dao.select(query);
						}
					}
				}
				return list;
			}

			@Override
			public void onPostExecute(List<Pokemon> list) {
				// on result value update Spinner
				pokemonAdapter.clear();
				pokemonAdapter.addAll(list);
			}
		}.execute(b.toString());
	}

	/**
	 * Override this method to make an action when async add action end
	 */
	public void onPokemonSelected(final Pokemon selectedPokemon) {
	}
}
