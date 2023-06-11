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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.R;
import com.pokemongostats.controller.external.ServiceUpdateDataPokedex;
import com.pokemongostats.controller.pokedexdata.PokedexDataFactory;
import com.pokemongostats.databinding.FragmentIncomingDataBinding;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.view.adapter.PokedexDataAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import fr.commons.generique.model.db.IObjetBdd;

public class IncomingDataFragment extends Fragment {

	private FragmentIncomingDataBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		PokedexData pokedexDataIn = IncomingDataFragmentArgs.fromBundle(getArguments()).getData();

		this.binding = FragmentIncomingDataBinding.inflate(inflater, container, false);

		//
		RecyclerView recyclerView = this.binding.incomingDataRecyclerView;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(llm);

		PokedexData pokedexDataOut = new PokedexData();

		EtapeWorkflowObjetBdd<PkmnDesc> etapePkmn = new EtapeWorkflowObjetBdd<>(getString(R.string.inc_data_pkmn), pokedexDataIn, pokedexDataOut, PokedexData::getDataPkmn);
		EtapeWorkflowObjetBdd<Move> etapeMove = new EtapeWorkflowObjetBdd<>(getString(R.string.inc_data_move), pokedexDataIn, pokedexDataOut, PokedexData::getDataMove);
		EtapeWorkflowObjetBdd<PkmnMove> etapePkmnMove = new EtapeWorkflowObjetBdd<>(getString(R.string.inc_data_pkmnmove), pokedexDataIn, pokedexDataOut, PokedexData::getDataPkmnMove);
		EtapeWorkflowObjetBdd<Evolution> etapeEvol = new EtapeWorkflowObjetBdd<>(getString(R.string.inc_data_evol), pokedexDataIn, pokedexDataOut, PokedexData::getDataEvol);
		EtapeWorkflowObjetBdd<Evolution> etapeFin = new EtapeWorkflowObjetBdd<>(getString(R.string.inc_data_evol), pokedexDataIn, pokedexDataOut, PokedexData::getDataEvol);


		etapePkmn.setNext(etapeMove);
		etapeMove.setNext(etapePkmnMove);
		etapePkmnMove.setNext(etapeEvol);
		etapeEvol.setNext(() -> {
			this.binding.btnNext.setText(getString(R.string.finnish));

			IncomingDataFragment.this.binding.titre.setVisibility(View.GONE);
			IncomingDataFragment.this.binding.incomingDataRecyclerView.setVisibility(View.GONE);
			IncomingDataFragment.this.binding.btnNext.setOnClickListener(v -> {
				ServiceUpdateDataPokedex.processDatas(pokedexDataOut);
				Objects.requireNonNull(getActivity()).onBackPressed();
			});

		});

		etapePkmn.start();

		return this.binding.getRoot();
	}


	private static abstract class EtapeWorkflow {
		protected final String titre;

		protected Runnable nextEtape;

		EtapeWorkflow(String titre) {
			this.titre = titre;
		}

		abstract void start();

		void setNext(Runnable next) {
			this.nextEtape = next;
		}
	}

	private class EtapeWorkflowObjetBdd<T extends IObjetBdd> extends EtapeWorkflow {
		private final PokedexData.Data<T> datasIn;
		private final PokedexData.Data<T> datasOut;

		EtapeWorkflowObjetBdd(String titre, PokedexData pkdxIn, PokedexData pkdxOut, Function<PokedexData,PokedexData.Data<T>> getter) {
			super(titre);
			this.datasIn = getter.apply(pkdxIn);
			this.datasOut = getter.apply(pkdxOut);
		}

		@Override
		void start() {
			List<IPokedexDataItem<T>> items = PokedexDataFactory.createLstItem(this.datasIn);
			if (items.isEmpty()) {
				this.nextEtape.run();
				return;
			}
			IncomingDataFragment.this.binding.titre.setText(this.titre);
			PokedexDataAdapter<T> adapter = new PokedexDataAdapter<>();
			adapter.setList(items);
			adapter.selectAll();
			IncomingDataFragment.this.binding.incomingDataRecyclerView.setAdapter(adapter);
			IncomingDataFragment.this.binding.btnNext.setOnClickListener(v -> {
				Set<IPokedexDataItem<T>> lstSelected = adapter.getLstItemSelected();
				for (var item : lstSelected) {
					item.addToData(this.datasOut);
				}
				this.nextEtape.run();
			});
		}

		<A extends IObjetBdd> void setNext(EtapeWorkflowObjetBdd<A> etapeSuivante) {
			setNext(etapeSuivante::start);
		}
	}
}