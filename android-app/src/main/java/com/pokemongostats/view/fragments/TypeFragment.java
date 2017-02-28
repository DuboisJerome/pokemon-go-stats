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
import com.pokemongostats.view.commons.ChooseTypeView;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.commons.PreferencesUtils;
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

		expandablePkmnsWithType = (PkmnDescExpandable) currentView
				.findViewById(R.id.pokemons_with_type);

		expandableQuickMovesWithType = (MoveExpandable) currentView
				.findViewById(R.id.type_quickmoves);

		expandableChargeMovesWithType = (MoveExpandable) currentView
				.findViewById(R.id.type_chargemoves);

		// super weaknesses
		expandableSuperWeaknesses = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_super_weaknesses);

		// weaknesses
		expandableWeaknesses = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_weaknesses);

		// resistances
		expandableResistances = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_resistances);

		// super resistances
		expandableSuperResistances = (PkmnDescExpandable) currentView
				.findViewById(R.id.expandable_super_resistances);
		return currentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null && currentItem == null) {
			String type = savedInstanceState.getString(TYPE_SELECTED_KEY);
			currentItem = (type == null || type.isEmpty())
					? null
					: Type.valueOfIgnoreCase(type);
		}
		super.onActivityCreated(savedInstanceState);
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

		/** pokemons */
		expandablePkmnsWithType.clear();
		expandableResistances.clear();
		expandableSuperResistances.clear();
		expandableSuperWeaknesses.clear();
		expandableWeaknesses.clear();

		for (PokemonDescription p : app.getPokedex(
				PreferencesUtils.isLastEvolutionOnly(getActivity()))) {
			switch (PokemonUtils.getTypeEffOnPokemon(currentItem, p)) {
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
			if (currentItem.equals(p.getType1())
				|| currentItem.equals(p.getType2())) {
				expandablePkmnsWithType.add(p,
						new OnClickItemListener<PokemonDescription>(
								mCallbackPkmn, p));
			}
		}

		/** moves */
		expandableQuickMovesWithType.clear();
		expandableChargeMovesWithType.clear();
		for (Move m : app.getMoves()) {
			if (currentItem.equals(m.getType())) {
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
