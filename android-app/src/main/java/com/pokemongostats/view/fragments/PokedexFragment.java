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
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.PkmnGoHelperApplication;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.MoveExpandable;
import com.pokemongostats.view.commons.OnItemCallback;
import com.pokemongostats.view.commons.PkmnDescView;
import com.pokemongostats.view.commons.TypeExpandable;
import com.pokemongostats.view.listeners.HasMoveSelectableListener;
import com.pokemongostats.view.listeners.HasTypeSelectableListener;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class PokedexFragment extends StackFragment<PokemonDescription> {

	private static final String PKMN_SELECTED_KEY = "PKMN_SELECTED_KEY";

	// pokedex
	private AutoCompleteTextView searchPkmnDesc;
	private PkmnDescAdapter pkmnDescAdapter;

	// selected pkmn
	private PkmnDescView selectedPkmnView;

	private MoveExpandable expandableQuickMoves;
	private MoveExpandable expandableChargeMoves;

	// super weaknesses list
	private TypeExpandable listSuperWeakness;

	// weaknesses list
	private TypeExpandable listWeakness;

	// resistances list
	private TypeExpandable listResistance;

	// super resistances list
	private TypeExpandable listSuperResistance;

	private HasTypeSelectableListener mCallbackType;
	private HasMoveSelectableListener mCallbackMove;

	public PokedexFragment(final HasTypeSelectableListener cbType,
			final HasMoveSelectableListener cbMove) {
		this.mCallbackType = cbType;
		this.mCallbackMove = cbMove;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pkmnDescAdapter = new PkmnDescAdapter(getActivity());
		List<PokemonDescription> list = ((PkmnGoHelperApplication) getActivity()
				.getApplication()).getPokedex();
		pkmnDescAdapter.clear();
		if (list != null && list.size() > 0) {
			Collections.sort(list);
			pkmnDescAdapter.addAll(list);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_pokedex, null, false);

		// search view
		searchPkmnDesc = (AutoCompleteTextView) view
				.findViewById(R.id.search_pokemon);
		searchPkmnDesc.setHint(R.string.pokemon_name_hint);
		searchPkmnDesc.setAdapter(pkmnDescAdapter);
		searchPkmnDesc.setOnItemClickListener(OnPkmnSelectedListener);
		searchPkmnDesc.setHintTextColor(android.R.color.white);

		//
		selectedPkmnView = (PkmnDescView) view.findViewById(R.id.selected_pkmn);

		expandableQuickMoves = (MoveExpandable) view
				.findViewById(R.id.pkmn_desc_quickmoves);
		expandableQuickMoves.setOnClickItemListener(moveClickCallback);
		expandableQuickMoves.expand();
		expandableQuickMoves.setKeepExpand(true);

		expandableChargeMoves = (MoveExpandable) view
				.findViewById(R.id.pkmn_desc_chargemoves);
		expandableChargeMoves.setOnClickItemListener(moveClickCallback);
		expandableChargeMoves.expand();
		expandableChargeMoves.setKeepExpand(true);

		// super weaknesses
		listSuperWeakness = (TypeExpandable) view
				.findViewById(R.id.list_super_weaknesses);
		listSuperWeakness.setOnClickItemListener(typeClickCallback);
		listSuperWeakness.expand();
		listSuperWeakness.setKeepExpand(true);

		// weaknesses
		listWeakness = (TypeExpandable) view.findViewById(R.id.list_weaknesses);
		listWeakness.setOnClickItemListener(typeClickCallback);
		listWeakness.expand();
		listWeakness.setKeepExpand(true);

		// resistances
		listResistance = (TypeExpandable) view
				.findViewById(R.id.list_resistances);
		listResistance.setOnClickItemListener(typeClickCallback);
		listResistance.expand();
		listResistance.setKeepExpand(true);

		// super resistances
		listSuperResistance = (TypeExpandable) view
				.findViewById(R.id.list_super_resistances);
		listSuperResistance.setOnClickItemListener(typeClickCallback);
		listSuperResistance.expand();
		listSuperResistance.setKeepExpand(true);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// reload selected pokemon
		if (currentItem == null && !pkmnDescAdapter.isEmpty()) {
			currentItem = pkmnDescAdapter.getItem(0);
		}
		updateView();
		if (savedInstanceState != null) {
			PclbPokemonDescription savedPkmn = savedInstanceState
					.getParcelable(PKMN_SELECTED_KEY);
			changeViewWithItem(savedPkmn);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// if (selectedPkmn != null) {
		// outState.putParcelable(PKMN_SELECTED_KEY,
		// new PclbPokemonDescription(selectedPkmn));
		// }
	}

	private final OnItemClickListener OnPkmnSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position != AdapterView.INVALID_POSITION) {
				changeViewWithItem(pkmnDescAdapter.getItem(position));
			}
		}
	};

	@Override
	protected void updateView(final PokemonDescription pkmn) {
		if (pkmn != null) {
			// reset previous
			listWeakness.clear();
			listSuperWeakness.clear();
			listResistance.clear();
			listSuperResistance.clear();

			for (Type t : Type.values()) {
				Effectiveness eff = PokemonUtils.getTypeEffOnPokemon(t, pkmn);
				switch (eff) {
					case NOT_VERY_EFFECTIVE :
						listResistance.add(t);
						break;
					case REALLY_NOT_VERY_EFFECTIVE :
						listSuperResistance.add(t);
						break;
					case REALLY_SUPER_EFFECTIVE :
						listSuperWeakness.add(t);
						break;
					case SUPER_EFFECTIVE :
						listWeakness.add(t);
						break;
					case NORMAL :
					default :
						break;
				}
			}

			selectedPkmnView.setPkmnDesc(pkmn);

			PkmnGoHelperApplication app = ((PkmnGoHelperApplication) getActivity()
					.getApplication());
			expandableQuickMoves.clear();
			expandableChargeMoves.clear();
			for (Move m : app.getMoves()) {
				if (pkmn.getMoveIds().contains(m.getId())) {
					switch (m.getMoveType()) {
						case CHARGE :
							expandableChargeMoves.add(m, pkmn);
							break;
						case QUICK :
							expandableQuickMoves.add(m, pkmn);
							break;
						default :
							break;
					}
				}
			}

			searchPkmnDesc.setText("");
			hideKeyboard();
		}
	}

	public void hideKeyboard() {
		Activity a = getActivity();
		if (a == null) { return; }
		View focus = a.getCurrentFocus();
		if (focus == null) { return; }
		InputMethodManager in = (InputMethodManager) a
				.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(focus.getWindowToken(), 0);
	}
	/******************** LISTENERS / CALLBACK ********************/

	private OnItemCallback<Type> typeClickCallback = new OnItemCallback<Type>() {
		@Override
		public void onItem(View v, Type t) {
			if (t == null) { return; }
			Log.d("STACK", "on click " + t);
			mCallbackType.onTypeSelected(t);
		}
	};
	private OnItemCallback<Move> moveClickCallback = new OnItemCallback<Move>() {
		@Override
		public void onItem(View v, Move m) {
			if (m == null) { return; }
			Log.d("STACK", "on click " + m);
			mCallbackMove.onMoveSelected(m);
		}
	};
}
