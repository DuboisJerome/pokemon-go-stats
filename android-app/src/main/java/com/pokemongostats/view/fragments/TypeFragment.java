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
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.commons.SpinnerInteractionListener;
import com.pokemongostats.view.expandables.MoveExpandable;
import com.pokemongostats.view.expandables.PkmnDescExpandable;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class TypeFragment extends StackFragment<Type>
		implements
			HasPkmnDescSelectable,
			HasMoveSelectable {

	private static final String TYPE_SELECTED_KEY = "TYPE_SELECTED_KEY";

	// pokedex
	private Spinner types;
	private TypeAdapter typesAdapter;

	// moves
	private MoveExpandable expandableQuickMovesWithType;
	private MoveExpandable expandableChargeMovesWithType;

	// super weaknesses list
	private PkmnDescExpandable expandablePkmnsWithType;

	// super weaknesses list
	private PkmnDescExpandable expandableSuperWeaknesses;

	// weaknesses list
	private PkmnDescExpandable expandableWeaknesses;

	// resistances list
	private PkmnDescExpandable expandableResistances;

	// super resistances list
	private PkmnDescExpandable expandableSuperResistances;

	private SelectedVisitor<Move> mCallbackMove;
	private SelectedVisitor<PokemonDescription> mCallbackPkmn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		typesAdapter = new TypeAdapter(getActivity(),
				android.R.layout.simple_spinner_item, Type.values());
		typesAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_type, container, false);

		types = (Spinner) view.findViewById(R.id.types_spinner);
		types.setAdapter(typesAdapter);
		types.setOnItemSelectedListener(onTypeSelectedListener);
		types.setOnTouchListener(onTypeSelectedListener);

		expandablePkmnsWithType = (PkmnDescExpandable) view
				.findViewById(R.id.pokemons_with_type);

		expandableQuickMovesWithType = (MoveExpandable) view
				.findViewById(R.id.type_quickmoves);

		expandableChargeMovesWithType = (MoveExpandable) view
				.findViewById(R.id.type_chargemoves);

		// super weaknesses
		expandableSuperWeaknesses = (PkmnDescExpandable) view
				.findViewById(R.id.expandable_super_weaknesses);

		// weaknesses
		expandableWeaknesses = (PkmnDescExpandable) view
				.findViewById(R.id.expandable_weaknesses);

		// resistances
		expandableResistances = (PkmnDescExpandable) view
				.findViewById(R.id.expandable_resistances);

		// super resistances
		expandableSuperResistances = (PkmnDescExpandable) view
				.findViewById(R.id.expandable_super_resistances);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			String type = savedInstanceState.getString(TYPE_SELECTED_KEY);
			currentItem = (type == null || type.isEmpty())
					? null
					: Type.valueOfIgnoreCase(type);
		}
		updateView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentItem != null) {
			outState.putString(TYPE_SELECTED_KEY, currentItem.name());
		}
	}

	private final SpinnerInteractionListener onTypeSelectedListener = new SpinnerInteractionListener() {

		@Override
		protected void onItemSelectedFromCode(AdapterView<?> parent, View view,
				int pos, long id) {
		}

		@Override
		protected void onItemSelectedFromUser(AdapterView<?> parent, View view,
				int pos, long id) {
			Type t = typesAdapter.getItem(pos);
			changeViewWithItem(t);
		}
	};

	@Override
	protected void updateView(final Type type) {
		types.setSelection(typesAdapter.getPosition(type));
		onTypeSpinnerUpdated(type);
	}

	private void onTypeSpinnerUpdated(final Type type) {
		if (type != null) {
			PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getActivity()
					.getApplication());

			/** pokemons */
			expandablePkmnsWithType.clear();
			expandableResistances.clear();
			expandableSuperResistances.clear();
			expandableSuperWeaknesses.clear();
			expandableWeaknesses.clear();

			for (PokemonDescription p : app.getPokedex()) {
				switch (PokemonUtils.getTypeEffOnPokemon(type, p)) {
					case NOT_VERY_EFFECTIVE :
						expandableResistances.add(p,
								new OnClickItemListener<PokemonDescription>(
										mCallbackPkmn, p));
						break;
					case REALLY_NOT_VERY_EFFECTIVE :
						expandableSuperResistances.add(p,
								new OnClickItemListener<PokemonDescription>(
										mCallbackPkmn, p));
						break;
					case REALLY_SUPER_EFFECTIVE :
						expandableSuperWeaknesses.add(p,
								new OnClickItemListener<PokemonDescription>(
										mCallbackPkmn, p));
						break;
					case SUPER_EFFECTIVE :
						expandableWeaknesses.add(p,
								new OnClickItemListener<PokemonDescription>(
										mCallbackPkmn, p));
						break;
					case NORMAL :
					default :
						break;
				}
				if (type.equals(p.getType1()) || type.equals(p.getType2())) {
					expandablePkmnsWithType.add(p,
							new OnClickItemListener<PokemonDescription>(
									mCallbackPkmn, p));
				}
			}

			/** moves */
			expandableQuickMovesWithType.clear();
			expandableChargeMovesWithType.clear();
			for (Move m : app.getMoves()) {
				if (type.equals(m.getType())) {
					switch (m.getMoveType()) {
						case CHARGE :
							expandableChargeMovesWithType.add(m,
									new OnClickItemListener<Move>(mCallbackMove,
											m));
							break;
						case QUICK :
							expandableQuickMovesWithType.add(m,
									new OnClickItemListener<Move>(mCallbackMove,
											m));
							break;
						default :
							break;
					}
				}
			}
		}
	}

	/******************** LISTENERS / CALLBACK ********************/

	@Override
	public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
		this.mCallbackMove = visitor;
	}

	@Override
	public void acceptSelectedVisitorPkmnDesc(
			final SelectedVisitor<PokemonDescription> visitor) {
		this.mCallbackPkmn = visitor;
	}
}
