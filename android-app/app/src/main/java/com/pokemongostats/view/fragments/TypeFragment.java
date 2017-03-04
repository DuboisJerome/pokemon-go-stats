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
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.ChooseTypeView;
import com.pokemongostats.view.commons.PreferencesUtils;
import com.pokemongostats.view.expandables.CustomExpandableList.OnItemClickListener;
import com.pokemongostats.view.expandables.MoveExpandable;
import com.pokemongostats.view.expandables.PkmnDescExpandable;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class TypeFragment extends HistorizedFragment<Type>
		implements
			HasPkmnDescSelectable,
			HasMoveSelectable {

	private static final String TYPE_SELECTED_KEY = "TYPE_SELECTED_KEY";

	private TypeRowView currentType;

	// moves
	private MoveAdapter adapterQuickMovesWithType;
	private MoveAdapter adapterChargeMovesWithType;

	// pkmns with same type
	private PkmnDescAdapter adapterPkmnsWithType;

	// super weaknesses list
	private PkmnDescAdapter adapterSuperWeaknesses;

	// weaknesses list
	private PkmnDescAdapter adapterWeaknesses;

	// resistances list
	private PkmnDescAdapter adapterResistances;

	// super resistances list
	private PkmnDescAdapter adapterSuperResistances;

	private SelectedVisitor<Move> mCallbackMove;
	private SelectedVisitor<PokemonDescription> mCallbackPkmn;

	private OnItemClickListener<PokemonDescription> onPkmnClicked;

	private OnItemClickListener<Move> onMoveClicked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// moves
		adapterQuickMovesWithType = new MoveAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		adapterQuickMovesWithType.setDPSVisible(true);
		adapterQuickMovesWithType.setPowerVisible(true);
		//
		adapterChargeMovesWithType = new MoveAdapter(getContext(),
				android.R.layout.simple_spinner_item);
		adapterChargeMovesWithType.setDPSVisible(true);
		adapterChargeMovesWithType.setPowerVisible(true);
		// pkmns with same type
		adapterPkmnsWithType = new PkmnDescAdapter(getContext());
		// super weaknesses list
		adapterSuperWeaknesses = new PkmnDescAdapter(getContext());
		// weaknesses list
		adapterWeaknesses = new PkmnDescAdapter(getContext());
		// resistances list
		adapterResistances = new PkmnDescAdapter(getContext());
		// super resistances list
		adapterSuperResistances = new PkmnDescAdapter(getContext());
		//
		onPkmnClicked = new OnItemClickListener<PokemonDescription>() {
			@Override
			public void onItemClick(PokemonDescription item) {
				if (mCallbackPkmn == null) { return; }
				mCallbackPkmn.select(item);
			}
		};
		//
		onMoveClicked = new OnItemClickListener<Move>() {
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
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		currentView = inflater.inflate(R.layout.fragment_type, container,
				false);

		currentType = (TypeRowView) currentView.findViewById(R.id.current_type);
		currentType.setOnClickListener(onClickType);
		//
		PkmnDescExpandable expandablePkmnsWithType = (PkmnDescExpandable) currentView
				.findViewById(R.id.pokemons_with_type);
		expandablePkmnsWithType.setAdapter(adapterPkmnsWithType);
		expandablePkmnsWithType.setOnItemClickListener(onPkmnClicked);
		//
		MoveExpandable expandableQuickMovesWithType = (MoveExpandable) currentView
				.findViewById(R.id.type_quickmoves);
		expandableQuickMovesWithType.setAdapter(adapterQuickMovesWithType);
		expandableQuickMovesWithType.setOnItemClickListener(onMoveClicked);
		//
		MoveExpandable expandableChargeMovesWithType = (MoveExpandable) currentView
				.findViewById(R.id.type_chargemoves);
		expandableChargeMovesWithType.setAdapter(adapterChargeMovesWithType);
		expandableChargeMovesWithType.setOnItemClickListener(onMoveClicked);
		// super weaknesses
		PkmnDescExpandable expandableSuperWeaknesses = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_super_weaknesses);
		expandableSuperWeaknesses.setAdapter(adapterSuperWeaknesses);
		expandableSuperWeaknesses.setOnItemClickListener(onPkmnClicked);
		// weaknesses
		PkmnDescExpandable expandableWeaknesses = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_weaknesses);
		expandableWeaknesses.setAdapter(adapterWeaknesses);
		expandableWeaknesses.setOnItemClickListener(onPkmnClicked);
		// resistances
		PkmnDescExpandable expandableResistances = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_resistances);
		expandableResistances.setAdapter(adapterResistances);
		expandableResistances.setOnItemClickListener(onPkmnClicked);
		// super resistances
		PkmnDescExpandable expandableSuperResistances = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_super_resistances);
		expandableSuperResistances.setAdapter(adapterSuperResistances);
		expandableSuperResistances.setOnItemClickListener(onPkmnClicked);

		return currentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && currentItem == null) {
			String type = savedInstanceState.getString(TYPE_SELECTED_KEY);
			currentItem = (type == null || type.isEmpty())
					? null
					: Type.valueOfIgnoreCase(type);
			updateView();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentItem != null) {
			outState.putString(TYPE_SELECTED_KEY, currentItem.name());
		}
	}

	@Override
	protected void updateViewImpl() {
		if (currentItem == null) {
			currentItem = Type.values()[0];
		}
		currentType.setType(currentItem);
		currentType.update();
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getActivity()
				.getApplication());

		adapterPkmnsWithType.setNotifyOnChange(false);
		adapterSuperWeaknesses.setNotifyOnChange(false);
		adapterWeaknesses.setNotifyOnChange(false);
		adapterResistances.setNotifyOnChange(false);
		adapterSuperResistances.setNotifyOnChange(false);
		/** pokemons */
		adapterPkmnsWithType.clear();
		adapterResistances.clear();
		adapterSuperResistances.clear();
		adapterSuperWeaknesses.clear();
		adapterWeaknesses.clear();

		for (PokemonDescription p : app.getPokedex(
				PreferencesUtils.isLastEvolutionOnly(getActivity()))) {
			switch (PokemonUtils.getTypeEffOnPokemon(currentItem, p)) {
				case NOT_VERY_EFFECTIVE :
					adapterResistances.add(p);
					break;
				case REALLY_NOT_VERY_EFFECTIVE :
					adapterSuperResistances.add(p);
					break;
				case REALLY_SUPER_EFFECTIVE :
					adapterSuperWeaknesses.add(p);
					break;
				case SUPER_EFFECTIVE :
					adapterWeaknesses.add(p);
					break;
				case NORMAL :
				default :
					break;
			}
			if (currentItem.equals(p.getType1())
				|| currentItem.equals(p.getType2())) {
				adapterPkmnsWithType.add(p);
			}
		}

		adapterPkmnsWithType.notifyDataSetChanged();
		adapterResistances.notifyDataSetChanged();
		adapterSuperResistances.notifyDataSetChanged();
		adapterSuperWeaknesses.notifyDataSetChanged();
		adapterWeaknesses.notifyDataSetChanged();

		/** moves */
		adapterQuickMovesWithType.setNotifyOnChange(false);
		adapterChargeMovesWithType.setNotifyOnChange(false);
		adapterQuickMovesWithType.clear();
		adapterChargeMovesWithType.clear();
		for (Move m : app.getMoves()) {
			if (currentItem.equals(m.getType())) {
				switch (m.getMoveType()) {
					case CHARGE :
						adapterChargeMovesWithType.add(m);
						break;
					case QUICK :
						adapterQuickMovesWithType.add(m);
						break;
					default :
						break;
				}
			}
		}
		adapterQuickMovesWithType.notifyDataSetChanged();
		adapterChargeMovesWithType.notifyDataSetChanged();
	}

	private final DialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
	private final OnClickListener onClickType = new OnClickListener() {
		@Override
		public void onClick(View v) {
			chooseTypeDialog.show(getFragmentManager(), "chooseTypeDialog");
		}
	};

	public class ChooseTypeDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// action to execute when click on type in ChooseTypeDialogFragment
			final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
				@Override
				public void select(Type t) {
					// hide dialog
					chooseTypeDialog.dismiss();
					// load view with type
					showItem(t);
				}
			};

			android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};

			final ChooseTypeView chooseTypeView = new ChooseTypeView(
					getContext(), currentItem, visitor);
			return new AlertDialog.Builder(getActivity())
					.setNegativeButton(R.string.cancel, cancelListener)
					.setView(chooseTypeView).create();
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
