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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.PokemonMove;
import com.pokemongostats.view.PkmnGoHelperApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.commons.MoveView;
import com.pokemongostats.view.commons.OnItemCallback;
import com.pokemongostats.view.commons.PkmnExpandable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectableListener;
import com.pokemongostats.view.parcalables.PclbMove;

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
public class MoveFragment extends StackFragment<Move> {

	private static final String MOVE_SELECTED_KEY = "MOVE_SELECTED_KEY";

	private AutoCompleteTextView searchMove;
	private MoveAdapter movesAdapter;

	// selected move
	private MoveView selectedMoveView;

	private PkmnExpandable expandablePkmnsWithMove;

	private HasPkmnDescSelectableListener mCallbackPkmn;

	public MoveFragment(final HasPkmnDescSelectableListener cbPkmnDesc) {
		this.mCallbackPkmn = cbPkmnDesc;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		movesAdapter = new MoveAdapter(getActivity(),
				android.R.layout.simple_spinner_item);
		List<Move> list = ((PkmnGoHelperApplication) getActivity()
				.getApplication()).getMoves();
		movesAdapter.clear();
		if (list != null && list.size() > 0) {
			Collections.sort(list);
			movesAdapter.addAll(list);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_move, container, false);

		// search view
		searchMove = (AutoCompleteTextView) view.findViewById(R.id.search_move);
		searchMove.setHint(R.string.move_name_hint);
		searchMove.setAdapter(movesAdapter);
		searchMove.setOnItemClickListener(onMoveSelectedListener);
		searchMove.setHintTextColor(android.R.color.white);

		//
		selectedMoveView = (MoveView) view.findViewById(R.id.selected_move);

		//
		expandablePkmnsWithMove = (PkmnExpandable) view
				.findViewById(R.id.pokemons_with_move);
		expandablePkmnsWithMove.setOnClickItemListener(pkmnClickCallback);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			currentItem = savedInstanceState.getParcelable(MOVE_SELECTED_KEY);
		}
		// reload selected pokemon
		if (currentItem == null && !movesAdapter.isEmpty()) {
			currentItem = movesAdapter.getItem(0);
		}
		updateView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentItem != null) {
			outState.putParcelable(MOVE_SELECTED_KEY,
					new PclbMove(currentItem));
		}
	}

	private final OnItemClickListener onMoveSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position != AdapterView.INVALID_POSITION) {
				changeViewWithItem(movesAdapter.getItem(position));
			}
		}
	};

	@Override
	protected void updateView(final Move move) {
		if (move != null) {
			PkmnGoHelperApplication app = ((PkmnGoHelperApplication) getActivity()
					.getApplication());

			/** pokemons */
			List<Long> pkmnIdsWithMove = new ArrayList<Long>();
			for (PokemonMove pm : app.getAllPkmnMoves()) {
				if (move.getId() == pm.getMoveId()) {
					pkmnIdsWithMove.add(pm.getPokedexNum());
				}
			}
			expandablePkmnsWithMove.clear();
			for (PokemonDescription p : app.getPokedex()) {
				if (pkmnIdsWithMove.contains(p.getPokedexNum())) {
					expandablePkmnsWithMove.add(p);
				}
			}

			selectedMoveView.setMove(move);

			searchMove.setText("");
			hideKeyboard();
		}
	}

	private void hideKeyboard() {
		Activity a = getActivity();
		if (a == null) { return; }
		View focus = a.getCurrentFocus();
		if (focus == null) { return; }
		InputMethodManager in = (InputMethodManager) a
				.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(focus.getWindowToken(), 0);
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
}
