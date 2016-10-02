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

package com.pokemongostats.view.fragments;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.model.Pokemon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class SelectPokemonFragment extends Fragment {

	public interface OnPokemonSelectedListener {
		public void onPokemonSelected(final Pokemon p);
	}

	private Spinner pokemonsSpinner;

	private ArrayAdapter<Pokemon> pokemonAdapter;

	private OnPokemonSelectedListener mCallback;

	public SelectPokemonFragment(final OnPokemonSelectedListener callback) {
		this.mCallback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.select_pokemon_fragment, container, false);

		// pokemons spinner
		pokemonsSpinner = (Spinner) view.findViewById(R.id.pokemons);
		pokemonAdapter = new ArrayAdapter<Pokemon>(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		pokemonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pokemonsSpinner.setAdapter(pokemonAdapter);

		return view;
	}

	/**
	 * Update spinner of pokemons with given pokemons
	 * 
	 * @param trainer
	 */
	public void updatePokemonSpinner(final List<Pokemon> pokemons) {
		pokemonAdapter.clear();
		if (pokemons != null) {
			pokemonAdapter.addAll(pokemons);
		}
	}
}
