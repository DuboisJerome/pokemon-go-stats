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

import java.util.Collections;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controler.utils.PokemonUtils;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.adapters.PokemonDescArrayAdapter;
import com.pokemongostats.view.adapters.TypeArrayAdapter;
import com.pokemongostats.view.commons.TypeViewUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class PokedexFragment extends Fragment {

	public interface PokedexFragmentListener {
	}

	// pokedex
	private AutoCompleteTextView search;
	private PokemonDescArrayAdapter pkmnDescAdapter;
	private TextView selectedPokemonName;
	private TextView selectedPokemonType1;
	private TextView selectedPokemonType2;

	// super weaknesses list
	private ListView listSuperWeakness;
	private TypeArrayAdapter listSuperWeaknessAdapter;

	// weaknesses list
	private ListView listWeakness;
	private TypeArrayAdapter listWeaknessAdapter;

	// resistances list
	private ListView listResistance;
	private TypeArrayAdapter listResistanceAdapter;

	// super resistances list
	private ListView listSuperResistance;
	private TypeArrayAdapter listSuperResistanceAdapter;

	// private ArrayList<ParcelableTrainer> parcelableTrainers;

	private PokedexFragmentListener mCallback;

	public PokedexFragment(final PokedexFragmentListener callback) {
		this.mCallback = callback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pkmnDescAdapter = new PokemonDescArrayAdapter(getActivity());
		// pkmnDescAdapter.setDropDownViewResource(
		// android.R.layout.simple_spinner_dropdown_item);

		listSuperWeaknessAdapter = new TypeArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item);
		listSuperWeaknessAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		listWeaknessAdapter = new TypeArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item);
		listWeaknessAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		listResistanceAdapter = new TypeArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item);
		listResistanceAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		listSuperResistanceAdapter = new TypeArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item);
		listSuperResistanceAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.pokedex_fragment, container,
				false);

		selectedPokemonName = (TextView) view.findViewById(R.id.name);
		selectedPokemonType1 = (TextView) view.findViewById(R.id.type1);
		selectedPokemonType2 = (TextView) view.findViewById(R.id.type2);

		// super weaknesses
		listSuperWeakness = (ListView) view
				.findViewById(R.id.list_super_weakness);
		listSuperWeakness.setAdapter(listSuperWeaknessAdapter);

		// weaknesses
		listWeakness = (ListView) view.findViewById(R.id.list_weakness);
		listWeakness.setAdapter(listWeaknessAdapter);

		// resistances
		listResistance = (ListView) view.findViewById(R.id.list_resistance);
		listResistance.setAdapter(listResistanceAdapter);

		// super resistances
		listSuperResistance = (ListView) view
				.findViewById(R.id.list_super_resistance);
		listSuperResistance.setAdapter(listSuperResistanceAdapter);

		// search view
		search = (AutoCompleteTextView) view.findViewById(R.id.search_pokemon);
		search.setHint(R.string.pokemon_name_hint);
		search.setAdapter(pkmnDescAdapter);
		search.setOnItemClickListener(OnPkmnSelectedListener);

		view.setBackground(
				getResources().getDrawable(R.drawable.bg_no_stretch));

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// if (savedInstanceState != null) {
		// parcelableTrainers = savedInstanceState
		// .getParcelableArrayList(TRAINERS_STATE_KEY);
		// if (parcelableTrainers != null && !parcelableTrainers.isEmpty()) {
		// updateTrainersSpinner(parcelableTrainers);
		// }
		// }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putParcelableArrayList(TRAINERS_STATE_KEY,
		// parcelableTrainers);
	}

	/**
	 * Update trainers spinner
	 */
	public void updatePokedexSpinner(
			final List<? extends PokemonDescription> list) {
		// on result value update Spinner
		pkmnDescAdapter.clear();
		if (list != null && list.size() > 0) {
			Collections.sort(list);
			pkmnDescAdapter.addAll(list);

			// to save state
			// parcelableTrainers = new ArrayList<ParcelableTrainer>();
			// for (Trainer t : list) {
			// parcelableTrainers.add(new ParcelableTrainer(t));
			// }
		} else {
			// parcelableTrainers = null;
		}
	}

	private final OnItemClickListener OnPkmnSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position != AdapterView.INVALID_POSITION) {
				// reset previous
				listWeaknessAdapter.clear();
				listSuperWeaknessAdapter.clear();
				listResistanceAdapter.clear();
				listSuperResistanceAdapter.clear();

				// selected pokemon description
				PokemonDescription p = pkmnDescAdapter.getItem(position);
				if (p != null) {
					for (Type t : Type.values()) {
						Effectiveness eff = PokemonUtils.getTypeEffOnPokemon(t,
								p);
						switch (eff) {
							case NOT_VERY_EFFECTIVE :
								listResistanceAdapter.add(t);
								break;
							case REALLY_NOT_VERY_EFFECTIVE :
								listSuperResistanceAdapter.add(t);
								break;
							case REALLY_SUPER_EFFECTIVE :
								listSuperWeaknessAdapter.add(t);
								break;
							case SUPER_EFFECTIVE :
								listWeaknessAdapter.add(t);
								break;
							case NORMAL :
							default :
								break;
						}
					}

					selectedPokemonName.setText(p.getName());
					selectedPokemonName.setTextColor(getContext().getResources()
							.getColor(android.R.color.white));
					TypeViewUtils.toTypeView(selectedPokemonType1, getContext(),
							p.getType1());
					if (p.getType2() != null) {
						selectedPokemonType2.setVisibility(View.VISIBLE);
						TypeViewUtils.toTypeView(selectedPokemonType2,
								getContext(), p.getType2());
					} else {
						selectedPokemonType2.setVisibility(View.INVISIBLE);
					}
					search.setText("");
					hideKeyboard();
				}
			}
		}
	};

	public void hideKeyboard() {
		InputMethodManager in = (InputMethodManager) getActivity()
				.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(
				getActivity().getCurrentFocus().getWindowToken(), 0);
	}

}
