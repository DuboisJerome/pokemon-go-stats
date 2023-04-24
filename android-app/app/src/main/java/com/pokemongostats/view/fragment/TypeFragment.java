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
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.databinding.CardViewPkmnDescHeaderBinding;
import com.pokemongostats.databinding.EmptyListTextviewBinding;
import com.pokemongostats.databinding.FragmentTypeBinding;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.adapter.PkmnDescAdapter;
import com.pokemongostats.view.commons.CustomExpandableView;
import com.pokemongostats.view.dialog.ChooseTypeDialogFragment;

import java.util.Comparator;

import fr.commons.generique.ui.AbstractGeneriqueAdapter;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class TypeFragment extends Fragment {

	private static final String TYPE_SELECTED_KEY = "TYPE_SELECTED_KEY";

	// super weaknesses list
	private PkmnDescAdapter adapterSW;
	// weaknesses list
	private PkmnDescAdapter adapterW;
	// resistances list
	private PkmnDescAdapter adapterR;
	// super resistances list
	private PkmnDescAdapter adapterSR;

	private FragmentTypeBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Comparator<PkmnDesc> c = PkmnDescComparators.getComparatorByMaxCp();
		// super weaknesses list
		this.adapterSW = new PkmnDescAdapter();
		this.adapterSW.setComparator(c);
		// weaknesses list
		this.adapterW = new PkmnDescAdapter();
		this.adapterW.setComparator(c);
		// resistances list
		this.adapterR = new PkmnDescAdapter();
		this.adapterR.setComparator(c);
		// super resistances list
		this.adapterSR = new PkmnDescAdapter();
		this.adapterSR.setComparator(c);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.binding = FragmentTypeBinding.inflate(inflater, container, false);

		this.binding.currentType.setOnClickListener(v -> {
			ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment(this::setType);
			chooseTypeDialog.addCurrentType(getType());
			chooseTypeDialog.show(getParentFragmentManager(), "chooseTypeDialog");
		});

		// super weaknesses
		initEffectiveness(this.binding.emptySwContent, this.binding.pkmnSwListitem, this.binding.expandableSw, this.adapterSW, this.binding.pkmnSwHeader);

		// weaknesses
		initEffectiveness(this.binding.emptyWContent, this.binding.pkmnWListitem, this.binding.expandableW, this.adapterW, this.binding.pkmnWHeader);

		// resistances
		initEffectiveness(this.binding.emptyRContent, this.binding.pkmnRListitem, this.binding.expandableR, this.adapterR, this.binding.pkmnRHeader);

		// super resistances
		initEffectiveness(this.binding.emptySrContent, this.binding.pkmnSrListitem, this.binding.expandableSr, this.adapterSR, this.binding.pkmnSrHeader);


		String typeName = TypeFragmentArgs.fromBundle(getArguments()).getTypeName();
		Type type = Type.valueOfIgnoreCase(typeName);
		setType(type);

		return this.binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		this.binding.expandableSw.getObservers().clear();
		this.binding.expandableW.getObservers().clear();
		this.binding.expandableR.getObservers().clear();
		this.binding.expandableSr.getObservers().clear();
		super.onDestroyView();
	}

	private void initEffectiveness(EmptyListTextviewBinding emptyView, RecyclerView recyclerView, CustomExpandableView expandable, AbstractGeneriqueAdapter<PkmnDesc,?> a, CardViewPkmnDescHeaderBinding header) {

		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(llm);
		recyclerView.setAdapter(a);
		a.setOnClickItemListener(t -> {
			TypeFragmentDirections.ActionToPkmn action = TypeFragmentDirections.actionToPkmn(t.getUniqueId());
			Navigation.findNavController(getView()).navigate(action);
		});

		Runnable updateExpandable = () -> {
			if (expandable.isExpand()) {
				header.layoutHeaderPkmnDesc.setVisibility(a.isEmpty() ? View.GONE : View.VISIBLE);
				recyclerView.setVisibility(a.isEmpty() ? View.GONE : View.VISIBLE);
				emptyView.emptyListTextview.setVisibility(a.isEmpty() ? View.VISIBLE : View.GONE);
			}
		};

		expandable.addExpandableView(header.layoutHeaderPkmnDesc);
		expandable.addExpandableView(recyclerView);
		expandable.addExpandableView(emptyView.emptyListTextview);
		expandable.registerObserver(o -> {
			if (o != null && o.equals(expandable)) {
				updateExpandable.run();
			}
		});

		a.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

			@Override
			public void onChanged() {
				updateExpandable.run();
			}
		});
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		if (this.binding.getType() == null) {
			if (savedInstanceState != null) {
				String type = savedInstanceState.getString(TYPE_SELECTED_KEY);
				setType((type == null || type.isEmpty())
						? null
						: Type.valueOfIgnoreCase(type));
			}
			// if currentItem still null, default = Normal
			if (this.binding.getType() == null) {
				setType(Type.NORMAL);
			}
		}
		super.onViewCreated(view, savedInstanceState);
	}

	public Type getType() {
		return this.binding.getType();
	}

	public void setType(Type t) {
		if (t == null) {
			t = Type.NORMAL;
		}
		this.binding.setType(t);

		setNotifyAdapters(false);
		clearAdapters();

		for (PkmnDesc p : PokedexDAO.getInstance().getListPkmnDesc()) {
			double eff = EffectivenessUtils.getTypeEffOnPokemon(t, p);
			if (eff > Effectiveness.SUPER_EFFECTIVE.getMultiplier()) {
				this.adapterSW.addItem(p);
			} else if (eff > 1.0 && eff <= Effectiveness.SUPER_EFFECTIVE.getMultiplier()) {
				this.adapterW.addItem(p);
			} else if (eff < 1.0 && eff >= Effectiveness.NOT_VERY_EFFECTIVE.getMultiplier()) {
				this.adapterR.addItem(p);
			} else if (eff < Effectiveness.NOT_VERY_EFFECTIVE.getMultiplier()) {
				this.adapterSR.addItem(p);
			}
		}

		setNotifyAdapters(true);
		this.adapterR.notifyDataSetChanged();
		this.adapterSR.notifyDataSetChanged();
		this.adapterSW.notifyDataSetChanged();
		this.adapterW.notifyDataSetChanged();

	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getType() != null) {
			outState.putString(TYPE_SELECTED_KEY, getType().name());
		}
	}

	private void setNotifyAdapters(boolean n) {
		this.adapterSW.setNotify(n);
		this.adapterW.setNotify(n);
		this.adapterR.setNotify(n);
		this.adapterSR.setNotify(n);
	}

	private void clearAdapters() {
		this.adapterR.clear();
		this.adapterSR.clear();
		this.adapterSW.clear();
		this.adapterW.clear();
	}
}