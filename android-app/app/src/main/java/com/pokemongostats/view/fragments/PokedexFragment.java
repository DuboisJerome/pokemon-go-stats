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
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.KeyboardUtils;
import com.pokemongostats.view.commons.PkmnDescView;
import com.pokemongostats.view.expandables.MoveExpandable;
import com.pokemongostats.view.expandables.TypeExpandable;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
public class PokedexFragment extends HistorizedFragment<PokemonDescription>
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

	private MoveAdapter adapterQuickMoves;
	private MoveAdapter adapterChargeMoves;

	// super weaknesses adapter
	private TypeAdapter adapterSuperWeakness;

	// weaknesses adapter
	private TypeAdapter adapterWeakness;

	// resistances adapter
	private TypeAdapter adapterResistance;

	// super resistances adapter
	private TypeAdapter adapterSuperResistance;

	private SelectedVisitor<Type> mCallbackType;
	private SelectedVisitor<Move> mCallbackMove;
	private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

	private com.pokemongostats.view.expandables.CustomExpandableList.OnItemClickListener<Type> onTypeClicked;

	private com.pokemongostats.view.expandables.CustomExpandableList.OnItemClickListener<Move> onMoveClicked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// don't show keyboard on activity start
		KeyboardUtils.initKeyboard(getActivity());

		pkmnDescAdapter = new PkmnDescAdapter(getActivity());
		pkmnDescAdapter.addAll(
				((PkmnGoStatsApplication) getActivity().getApplication())
						.getPokedex());
		pkmnDescAdapter.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);
		//
		adapterQuickMoves = new MoveAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		adapterQuickMoves.setDPSVisible(true);
		adapterQuickMoves.setPowerVisible(true);
		//
		adapterChargeMoves = new MoveAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		adapterChargeMoves.setDPSVisible(true);
		adapterChargeMoves.setPowerVisible(true);
		//
		adapterSuperWeakness = new TypeAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		//
		adapterWeakness = new TypeAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		//
		adapterResistance = new TypeAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		//
		adapterSuperResistance = new TypeAdapter(getContext(),
				android.R.layout.simple_spinner_item);

		onTypeClicked = new com.pokemongostats.view.expandables.CustomExpandableList.OnItemClickListener<Type>() {
			@Override
			public void onItemClick(Type item) {
				if (mCallbackType == null) { return; }
				mCallbackType.select(item);
			}
		};
		onMoveClicked = new com.pokemongostats.view.expandables.CustomExpandableList.OnItemClickListener<Move>() {
			@Override
			public void onItemClick(Move item) {
				if (mCallbackMove == null) { return; }
				mCallbackMove.select(item);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		currentView = inflater.inflate(R.layout.fragment_pokedex, null, false);

		// search view
		searchPkmnDesc = (AutoCompleteTextView) currentView
				.findViewById(R.id.search_pokemon);
		searchPkmnDesc.setHint(R.string.pokemon_name_hint);
		searchPkmnDesc.setAdapter(pkmnDescAdapter);
		searchPkmnDesc.setOnItemClickListener(OnPkmnSelectedListener);
		searchPkmnDesc.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

		//
		 selectedPkmnView = (PkmnDescView) currentView
		 .findViewById(R.id.selected_pkmn);
		 selectedPkmnView.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);

		MoveExpandable expandableQuickMoves = (MoveExpandable) currentView
				.findViewById(R.id.pkmn_desc_quickmoves);
		expandableQuickMoves.expand();
		expandableQuickMoves.setKeepExpand(true);
		expandableQuickMoves.setAdapter(adapterQuickMoves);
		expandableQuickMoves.setOnItemClickListener(onMoveClicked);

		MoveExpandable expandableChargeMoves = (MoveExpandable) currentView
				.findViewById(R.id.pkmn_desc_chargemoves);
		expandableChargeMoves.expand();
		expandableChargeMoves.setKeepExpand(true);
		expandableChargeMoves.setAdapter(adapterChargeMoves);
		expandableChargeMoves.setOnItemClickListener(onMoveClicked);

		// super weaknesses
		TypeExpandable listSuperWeakness = (TypeExpandable) currentView
				.findViewById(R.id.list_super_weaknesses);
		listSuperWeakness.expand();
		listSuperWeakness.setKeepExpand(true);
		listSuperWeakness.setAdapter(adapterSuperWeakness);
		listSuperWeakness.setOnItemClickListener(onTypeClicked);

		// weaknesses
		TypeExpandable listWeakness = (TypeExpandable) currentView
				.findViewById(R.id.list_weaknesses);
		listWeakness.expand();
		listWeakness.setKeepExpand(true);
		listWeakness.setAdapter(adapterWeakness);
		listWeakness.setOnItemClickListener(onTypeClicked);

		// resistances
		TypeExpandable listResistance = (TypeExpandable) currentView
				.findViewById(R.id.list_resistances);
		listResistance.expand();
		listResistance.setKeepExpand(true);
		listResistance.setAdapter(adapterResistance);
		listResistance.setOnItemClickListener(onTypeClicked);

		// super resistances
		TypeExpandable listSuperResistance = (TypeExpandable) currentView
				.findViewById(R.id.list_super_resistances);
		listSuperResistance.expand();
		listSuperResistance.setKeepExpand(true);
		listSuperResistance.setAdapter(adapterSuperResistance);
		listSuperResistance.setOnItemClickListener(onTypeClicked);

		return currentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && currentItem == null) {
			currentItem = savedInstanceState.getParcelable(PKMN_SELECTED_KEY);
			updateView();
		}
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
				showItem(pkmnDescAdapter.getItem(position));
			}
		}
	};

	@Override
	protected void updateViewImpl() {
		final PokemonDescription pkmn = currentItem;
		adapterQuickMoves.setOwner(pkmn);
		adapterChargeMoves.setOwner(pkmn);
		if (pkmn != null) {
			adapterWeakness.setNotifyOnChange(false);
			adapterSuperWeakness.setNotifyOnChange(false);
			adapterResistance.setNotifyOnChange(false);
			adapterSuperResistance.setNotifyOnChange(false);
			// reset previous
			adapterWeakness.clear();
			adapterSuperWeakness.clear();
			adapterResistance.clear();
			adapterSuperResistance.clear();

			for (Type t : Type.values()) {
				Effectiveness eff = PokemonUtils.getTypeEffOnPokemon(t, pkmn);
				switch (eff) {
					case NOT_VERY_EFFECTIVE :
						adapterResistance.add(t);
						break;
					case REALLY_NOT_VERY_EFFECTIVE :
						adapterSuperResistance.add(t);
						break;
					case REALLY_SUPER_EFFECTIVE :
						adapterSuperWeakness.add(t);
						break;
					case SUPER_EFFECTIVE :
						adapterWeakness.add(t);
						break;
					case NORMAL :
					default :
						break;
				}
			}

			// notify
			adapterWeakness.notifyDataSetChanged();
			adapterSuperWeakness.notifyDataSetChanged();
			adapterResistance.notifyDataSetChanged();
			adapterSuperResistance.notifyDataSetChanged();

			selectedPkmnView.setPkmnDesc(pkmn);

			PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getActivity()
					.getApplication());

			adapterQuickMoves.setNotifyOnChange(false);
			adapterChargeMoves.setNotifyOnChange(false);
			adapterQuickMoves.clear();
			adapterChargeMoves.clear();
			for (Move m : app.getMoves()) {
				if (pkmn.getMoveIds().contains(m.getId())) {
					switch (m.getMoveType()) {
						case CHARGE :
							adapterChargeMoves.add(m);
							break;
						case QUICK :
							adapterQuickMoves.add(m);
							break;
						default :
							break;
					}
				}
			}

			adapterQuickMoves.notifyDataSetChanged();
			adapterChargeMoves.notifyDataSetChanged();

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
