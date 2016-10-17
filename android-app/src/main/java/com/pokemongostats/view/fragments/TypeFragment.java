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
import com.pokemongostats.view.PkmnGoHelperApplication;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.MoveExpandable;
import com.pokemongostats.view.commons.OnItemCallback;
import com.pokemongostats.view.commons.PkmnExpandable;
import com.pokemongostats.view.commons.SpinnerInteractionListener;
import com.pokemongostats.view.listeners.HasMoveSelectableListener;
import com.pokemongostats.view.listeners.HasPkmnDescSelectableListener;

import android.os.Bundle;
import android.util.Log;
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
public class TypeFragment extends StackFragment<Type> {

	// pokedex
	private Spinner types;
	private TypeAdapter typesAdapter;

	// super weaknesses list
	private MoveExpandable expandableMovesWithType;

	// super weaknesses list
	private PkmnExpandable expandablePkmnsWithType;

	// super weaknesses list
	private PkmnExpandable expandableSuperWeaknesses;

	// weaknesses list
	private PkmnExpandable expandableWeaknesses;

	// resistances list
	private PkmnExpandable expandableResistances;

	// super resistances list
	private PkmnExpandable expandableSuperResistances;

	private HasMoveSelectableListener mCallbackMove;
	private HasPkmnDescSelectableListener mCallbackPkmn;

	public TypeFragment(final HasMoveSelectableListener cbMove,
			final HasPkmnDescSelectableListener cbPkmnDesc) {
		this.mCallbackMove = cbMove;
		this.mCallbackPkmn = cbPkmnDesc;
	}

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

		types = (Spinner) view.findViewById(R.id.types);
		types.setAdapter(typesAdapter);
		types.setOnItemSelectedListener(onTypeSelectedListener);
		types.setOnTouchListener(onTypeSelectedListener);

		if (currentItem == null && !typesAdapter.isEmpty()) {
			currentItem = typesAdapter.getItem(0);
		}
		updateView();

		expandablePkmnsWithType = (PkmnExpandable) view
				.findViewById(R.id.pokemons_with_type);
		expandablePkmnsWithType.setOnClickItemListener(pkmnClickCallback);

		expandableMovesWithType = (MoveExpandable) view
				.findViewById(R.id.moves_with_type);
		expandableMovesWithType.setOnClickItemListener(moveClickCallback);

		// super weaknesses
		expandableSuperWeaknesses = (PkmnExpandable) view
				.findViewById(R.id.expandable_super_weaknesses);
		expandableSuperWeaknesses.setOnClickItemListener(pkmnClickCallback);

		// weaknesses
		expandableWeaknesses = (PkmnExpandable) view
				.findViewById(R.id.expandable_weaknesses);
		expandableWeaknesses.setOnClickItemListener(pkmnClickCallback);

		// resistances
		expandableResistances = (PkmnExpandable) view
				.findViewById(R.id.expandable_resistances);
		expandableResistances.setOnClickItemListener(pkmnClickCallback);

		// super resistances
		expandableSuperResistances = (PkmnExpandable) view
				.findViewById(R.id.expandable_super_resistances);
		expandableSuperResistances.setOnClickItemListener(pkmnClickCallback);

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
			PkmnGoHelperApplication app = ((PkmnGoHelperApplication) getActivity()
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
						expandableResistances.add(p);
						break;
					case REALLY_NOT_VERY_EFFECTIVE :
						expandableSuperResistances.add(p);
						break;
					case REALLY_SUPER_EFFECTIVE :
						expandableSuperWeaknesses.add(p);
						break;
					case SUPER_EFFECTIVE :
						expandableWeaknesses.add(p);
						break;
					case NORMAL :
					default :
						break;
				}
				if (type.equals(p.getType1()) || type.equals(p.getType2())) {
					expandablePkmnsWithType.add(p);
				}
			}

			/** moves */
			expandableMovesWithType.clear();
			for (Move m : app.getMoves()) {
				if (type.equals(m.getType())) {
					expandableMovesWithType.add(m);
				}
			}
		}
	}

	/******************** LISTENERS / CALLBACK ********************/

	private OnItemCallback<PokemonDescription> pkmnClickCallback = new OnItemCallback<PokemonDescription>() {
		@Override
		public void onItem(View v, PokemonDescription p) {
			if (p == null) { return; }
			Log.d("STACK", "on click " + p);
			mCallbackPkmn.onPkmnDescSelected(p);
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
