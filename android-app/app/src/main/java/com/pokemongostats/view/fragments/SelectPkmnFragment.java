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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Pokemon;

import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class SelectPkmnFragment extends Fragment {

	public interface OnPokemonSelectedListener {
		public void onPokemonSelected(final Pokemon p);
	}

	private Spinner pokemonSpinner;

	private ArrayAdapter<Pokemon> pokemonAdapter;

	private Button prevBtn;

	private Button nextBtn;
	private OnPokemonSelectedListener mCallback;

	public SelectPkmnFragment(){}

	public void setCallback(OnPokemonSelectedListener mCallback) {
		this.mCallback = mCallback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_select_pkmn,
				container, false);

		// pokemons spinner
		pokemonSpinner = (Spinner) view.findViewById(R.id.pokemons);
		pokemonAdapter = new ArrayAdapter<Pokemon>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		pokemonAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		pokemonSpinner.setAdapter(pokemonAdapter);

		// edit button
		view.findViewById(R.id.editPokemonBtn)
				.setOnClickListener(onClickEditTrainer);

		// add button
		view.findViewById(R.id.addPokemonBtn)
				.setOnClickListener(onClickAddTrainer);

		// prev button
		prevBtn = (Button) view.findViewById(R.id.prevBtn);
		prevBtn.setOnClickListener(onClickBack);

		// next button
		nextBtn = (Button) view.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(onClickSelectTrainer);

		return view;
	}

	public void updatePokemonSpinner(final List<Pokemon> pokemons) {
		pokemonAdapter.clear();
		if (pokemons != null) {
			pokemonAdapter.addAll(pokemons);
		}
	}

	/**
	 * A call-back call when the user click edit icon
	 */
	private OnClickListener onClickEditTrainer = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	/**
	 * A call-back call when the user click add icon
	 */
	private OnClickListener onClickAddTrainer = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			new AddPkmnDialog() {
//
//				@Override
//				public void onPokemonAdded(final Pokemon addedPokemon) {
//					mCallback.onPokemonSelected(addedPokemon);
//
//				}
//			}.show(getFragmentManager(), AddTrainerDialog.class.getName());
		}
	};

	/**
	 * A call-back call when the user click select
	 */
	private OnClickListener onClickSelectTrainer = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = pokemonSpinner.getSelectedItemPosition();
			if (position != AdapterView.INVALID_POSITION) {
				// selected trainer
				mCallback.onPokemonSelected(pokemonAdapter.getItem(position));
			} else {
				// TODO message in string.xml
				Log.e(getClass().getName(), "You must select a pokemon");
				Toast.makeText(getActivity(), "You must select a pokemon",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	/**
	 * A call-back call when the user click back
	 */
	private OnClickListener onClickBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getActivity().onBackPressed();
		}
	};
}
