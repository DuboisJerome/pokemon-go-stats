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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

		// TODO i18n
		EtapeWorkflow<PkmnDesc> etapePkmn = new EtapeWorkflow<>("Pokémons", pokedexDataIn, pokedexDataOut, PokedexData::getDataPkmn);
		EtapeWorkflow<Move> etapeMove = new EtapeWorkflow<>("Attaques", pokedexDataIn, pokedexDataOut, PokedexData::getDataMove);
		EtapeWorkflow<PkmnMove> etapePkmnMove = new EtapeWorkflow<>("Association Pokémon/Attaques", pokedexDataIn, pokedexDataOut, PokedexData::getDataPkmnMove);
		EtapeWorkflow<Evolution> etapeEvol = new EtapeWorkflow<>("Evolutions", pokedexDataIn, pokedexDataOut, PokedexData::getDataEvol);

		etapePkmn.setNext(etapeMove);
		etapeMove.setNext(etapePkmnMove);
		etapePkmnMove.setNext(() -> {
			this.binding.btnNext.setText("Finnish");
			etapeEvol.start();
		});
		etapeEvol.setNext(() -> {
			Log.w("SQL_SELECTED", "Ci après les requetes qui seront jouez");
			ServiceUpdateDataPokedex.logSql(pokedexDataOut);
			this.binding.btnNext.setOnClickListener(v -> {
				// Unbind
			});
			this.binding.btnNext.setVisibility(View.GONE);
			// Ne pas faire ça ça ne revient pas sur le fragment précédent. getParentFragmentManager().popBackStack();
		});

		etapePkmn.start();

		return this.binding.getRoot();
	}

	private class EtapeWorkflow<T extends IObjetBdd> {
		private final String titre;
		private final PokedexData.Data<T> datasIn;
		private final PokedexData.Data<T> datasOut;

		private Runnable nextEtape;

		EtapeWorkflow(String titre, PokedexData pkdxIn, PokedexData pkdxOut, Function<PokedexData,PokedexData.Data<T>> getter) {
			this.titre = titre;
			this.datasIn = getter.apply(pkdxIn);
			this.datasOut = getter.apply(pkdxOut);
		}

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

		<A extends IObjetBdd> void setNext(EtapeWorkflow<A> etapeSuivante) {
			setNext(etapeSuivante::start);
		}

		void setNext(Runnable next) {
			this.nextEtape = next;
		}
	}
}