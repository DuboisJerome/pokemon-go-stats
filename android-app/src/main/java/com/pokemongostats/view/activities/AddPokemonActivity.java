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

package com.pokemongostats.view.activities;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.db.pokemon.PokedexAsyncDAO;
import com.pokemongostats.controller.db.trainer.TrainerAsyncDAO;
import com.pokemongostats.model.PokemonDescription;
import com.pokemongostats.model.Trainer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class AddPokemonActivity extends Activity {

	/** Spinner displaying pokemons' names */
	private Spinner pokedex;

	/** */
	private EditText cpEditText;

	/** Spinner displaying trainers */
	private Spinner trainers;

	/** Pokedex adapter */
	private ArrayAdapter<PokemonDescription> pokedexAdapter;

	/** Trainers adapter */
	private ArrayAdapter<Trainer> trainersAdapter;

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_pokemon_activity);

		// instances
		pokedex = (Spinner) findViewById(R.id.pokedex);
		cpEditText = (EditText) findViewById(R.id.cpEditText);
		trainers = (Spinner) findViewById(R.id.trainers);

		//
		pokedexAdapter = new ArrayAdapter<PokemonDescription>(
				AddPokemonActivity.this, android.R.layout.simple_spinner_item);
		pokedexAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		pokedex.setAdapter(pokedexAdapter);
		//
		trainersAdapter = new ArrayAdapter<Trainer>(AddPokemonActivity.this,
				android.R.layout.simple_spinner_item);
		trainersAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		trainers.setAdapter(trainersAdapter);

		//
		updatePokedexSpinner();
		updateTrainersSpinner();
	}

	/**
	 * Update pokedex spinner
	 */
	public void updatePokedexSpinner() {

		PokedexAsyncDAO asyncDAO = new PokedexAsyncDAO(getApplicationContext());
		asyncDAO.new GetAllAsyncTask<Void>() {

			@Override
			public void onPostExecute(List<PokemonDescription> list) {
				// on result value update Spinner
				pokedexAdapter.clear();
				pokedexAdapter.addAll(list);
			}
		}.execute();
	}

	/**
	 * Update trainers spinner
	 */
	public void updateTrainersSpinner() {

		TrainerAsyncDAO asyncDAO = new TrainerAsyncDAO(getApplicationContext());
		asyncDAO.new GetAllAsyncTask<Void>() {

			@Override
			public void onPostExecute(List<Trainer> list) {
				// on result value update Spinner
				trainersAdapter.clear();
				trainersAdapter.addAll(list);
			}
		}.execute();
	}

	/**
	 * Called when the activity is about to start interacting with the user.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// retrieve gyms name
	}

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	/**
	 * Called right before your activity's option menu is displayed.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	/**
	 * A call-back for when the user click validate
	 */
	private OnClickListener onClickValidatePokemon = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO
		}
	};

	/**
	 * Get selected team
	 * 
	 * @return
	 */
	private String getSelectedPokemonDescription() {
		return ""; // TODO
	}
}
