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

import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.pokedexdata.PokedexDataFactory;
import com.pokemongostats.databinding.FragmentIncomingDataBinding;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.view.adapter.PokedexDataAdapter;

import java.util.Set;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;

public class IncommingDataFragment extends Fragment {

	private FragmentIncomingDataBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		PokedexData pokedexData = IncommingDataFragmentArgs.fromBundle(getArguments()).getData();

		this.binding = FragmentIncomingDataBinding.inflate(inflater, container, false);
		this.binding.setPkdx(pokedexData);

		PokedexDataAdapter<PkmnDesc> adapter = new PokedexDataAdapter<>();
		adapter.setList(PokedexDataFactory.createLstItem(pokedexData.getDataPkmn()));

		//
		RecyclerView recyclerView = this.binding.incomingDataRecyclerView;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(llm);
		recyclerView.setAdapter(adapter);

		bindOnClickNext(adapter, PkmnTableDAO.getInstance());

		return this.binding.getRoot();
	}

	private <T extends IObjetBdd> void bindOnClickNext(PokedexDataAdapter<T> adapter, TableDAO<T> dao) {
		this.binding.btnNext.setOnClickListener(v -> {
			Set<IPokedexDataItem<T>> lstSelected = adapter.getLstItemSelected();
			for (var item : lstSelected) {
				String req = item.toSql(dao);
				if (req != null && !req.isEmpty()) {
					android.util.Log.d("SQL_SELECTED", req);
				}
			}
			// TODO jouer les requetes ?
		});
	}
}