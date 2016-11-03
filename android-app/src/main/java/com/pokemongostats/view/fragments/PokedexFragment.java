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

import com.pokemongostats.R;
import com.pokemongostats.controler.utils.PokemonUtils;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.KeyboardUtils;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.commons.PkmnDescView;
import com.pokemongostats.view.expandables.MoveExpandable;
import com.pokemongostats.view.expandables.TypeExpandable;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class PokedexFragment extends StackFragment<PokemonDescription>
		implements
			HasMoveSelectable,
			HasTypeSelectable,
			HasPkmnDescSelectable {

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

	private SelectedVisitor<Type> mCallbackType;
	private SelectedVisitor<Move> mCallbackMove;
	private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// don't show keyboard on activity start
		KeyboardUtils.initKeyboard(getActivity());

		pkmnDescAdapter = new PkmnDescAdapter(getActivity());
		pkmnDescAdapter.addAll(
				((PkmnGoStatsApplication) getActivity().getApplication())
						.getPokedex());
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
		selectedPkmnView.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);

		expandableQuickMoves = (MoveExpandable) view
				.findViewById(R.id.pkmn_desc_quickmoves);
		expandableQuickMoves.expand();
		expandableQuickMoves.setKeepExpand(true);

		expandableChargeMoves = (MoveExpandable) view
				.findViewById(R.id.pkmn_desc_chargemoves);
		expandableChargeMoves.expand();
		expandableChargeMoves.setKeepExpand(true);

		// super weaknesses
		listSuperWeakness = (TypeExpandable) view
				.findViewById(R.id.list_super_weaknesses);
		listSuperWeakness.expand();
		listSuperWeakness.setKeepExpand(true);

		// weaknesses
		listWeakness = (TypeExpandable) view.findViewById(R.id.list_weaknesses);
		listWeakness.expand();
		listWeakness.setKeepExpand(true);

		// resistances
		listResistance = (TypeExpandable) view
				.findViewById(R.id.list_resistances);
		listResistance.expand();
		listResistance.setKeepExpand(true);

		// super resistances
		listSuperResistance = (TypeExpandable) view
				.findViewById(R.id.list_super_resistances);
		listSuperResistance.expand();
		listSuperResistance.setKeepExpand(true);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			currentItem = savedInstanceState.getParcelable(PKMN_SELECTED_KEY);
		}
		updateView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentItem != null) {
			outState.putParcelable(PKMN_SELECTED_KEY,
					new PclbPokemonDescription(currentItem));
		}
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
						listResistance.add(t, new OnClickItemListener<Type>(
								mCallbackType, t));
						break;
					case REALLY_NOT_VERY_EFFECTIVE :
						listSuperResistance.add(t,
								new OnClickItemListener<Type>(mCallbackType,
										t));
						break;
					case REALLY_SUPER_EFFECTIVE :
						listSuperWeakness.add(t, new OnClickItemListener<Type>(
								mCallbackType, t));
						break;
					case SUPER_EFFECTIVE :
						listWeakness.add(t, new OnClickItemListener<Type>(
								mCallbackType, t));
						break;
					case NORMAL :
					default :
						break;
				}
			}

			selectedPkmnView.setPkmnDesc(pkmn);

			PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getActivity()
					.getApplication());
			expandableQuickMoves.clear();
			expandableChargeMoves.clear();
			for (Move m : app.getMoves()) {
				if (pkmn.getMoveIds().contains(m.getId())) {
					switch (m.getMoveType()) {
						case CHARGE :
							expandableChargeMoves.add(m, pkmn,
									new OnClickItemListener<Move>(mCallbackMove,
											m));
							break;
						case QUICK :
							expandableQuickMoves.add(m, pkmn,
									new OnClickItemListener<Move>(mCallbackMove,
											m));
							break;
						default :
							break;
					}
				}
			}

			searchPkmnDesc.setText("");
			KeyboardUtils.hideKeyboard(getActivity());
		}
	}

	/******************** LISTENERS / CALLBACK ********************/

	@Override
	public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor) {
		this.mCallbackType = visitor;
	}

	@Override
	public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
		this.mCallbackMove = visitor;
	}

	@Override
	public void acceptSelectedVisitorPkmnDesc(
			final SelectedVisitor<PokemonDescription> visitor) {
		this.mCallbackPkmnDesc = visitor;
	}
}
