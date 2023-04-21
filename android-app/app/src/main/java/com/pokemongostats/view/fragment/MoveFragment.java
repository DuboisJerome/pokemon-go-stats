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

package com.pokemongostats.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.databinding.FragmentMoveBinding;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.model.parcalables.PclbMove;
import com.pokemongostats.view.adapter.MoveAdapter;
import com.pokemongostats.view.adapter.PkmnDescAdapter;
import com.pokemongostats.view.utils.KeyboardUtils;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class MoveFragment extends Fragment {

	private static final String MOVE_SELECTED_KEY = "MOVE_SELECTED_KEY";

	private MoveAdapter movesAdapter;
	// selected move
	private PkmnDescAdapter adapterPkmnsWithMove;
	FragmentMoveBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// don't show keyboard on activity start
		KeyboardUtils.initKeyboard(getActivity());

		this.adapterPkmnsWithMove = new PkmnDescAdapter();
		this.adapterPkmnsWithMove.setComparator(PkmnDescComparators.getComparatorByMaxCp());
		this.adapterPkmnsWithMove.setOnClickItemListener(p -> {
			MoveFragmentDirections.ActionToPkmn action = MoveFragmentDirections.actionToPkmn(p.getUniqueId());
			Navigation.findNavController(getView()).navigate(action);
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		this.binding = FragmentMoveBinding.inflate(inflater, container, false);

		//
		RecyclerView expandablePkmnsWithMove = this.binding.pokemonsRecyclerView;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		expandablePkmnsWithMove.setLayoutManager(llm);
		expandablePkmnsWithMove.setAdapter(this.adapterPkmnsWithMove);

		long idMove = MoveFragmentArgs.fromBundle(getArguments()).getMoveId();
		Move move = PokedexDAO.getInstance().getMapMove().get(idMove);
		setMove(move);

		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		if (savedInstanceState != null && this.binding.getMove() == null) {
			setMove(savedInstanceState.getParcelable(MOVE_SELECTED_KEY));
		}
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.binding.getMove() != null) {
			outState.putParcelable(MOVE_SELECTED_KEY,
					new PclbMove(this.binding.getMove()));
		}
	}

	protected void setMove(Move m) {
		if (m != null) {
			this.binding.setMove(m);
			this.adapterPkmnsWithMove.setList(PokedexDAO.getInstance().getListPkmnFor(m));
		}

		KeyboardUtils.hideKeyboard(getActivity());
	}
}