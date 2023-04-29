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
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.databinding.FragmentTypeBinding;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.model.filtersinfos.PkmnDescTypeFilterInfo;
import com.pokemongostats.view.adapter.PkmnDescTypeAdapter;
import com.pokemongostats.view.dialog.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class TypeFragment extends Fragment
		implements Observer {

	private static final String TYPE_SELECTED_KEY = "TYPE_SELECTED_KEY";

	private PkmnDescTypeAdapter adapter;

	private FragmentTypeBinding binding;

	private final Map<Double, Button> btnLst = new HashMap<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Comparator<PkmnDesc> c = PkmnDescComparators.getComparatorByMaxCp();
		List<PkmnDesc> lstPkmnDesc = PokedexDAO.getInstance().getListPkmnDesc();
		lstPkmnDesc.sort(c);
		this.adapter = new PkmnDescTypeAdapter(lstPkmnDesc);
		this.adapter.setComparator(c);
		this.adapter.setOnClickItemListener(t -> {
			TypeFragmentDirections.ActionToPkmn action = TypeFragmentDirections.actionToPkmn(t.getUniqueId());
			Navigation.findNavController(getView()).navigate(action);
		});

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
		RecyclerView recList = this.binding.pkmnLst;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
		recList.setAdapter(adapter);

		Set<Double> setEff = EffectivenessUtils.getSetRoundEff();

		btnLst.clear();
		for (double eff : setEff) {
			if (eff == Effectiveness.NORMAL.getMultiplier() || eff == EffectivenessUtils.getRoundedMultiplier(Effectiveness.IMMUNE, Effectiveness.IMMUNE)) {
				continue;
			}

			int buttonStyle = R.style.onglet_type;
			Button btn = new Button(new ContextThemeWrapper(getContext(), buttonStyle), null, buttonStyle);
			btn.setOnClickListener(c -> {
				btnLst.forEach((e,b) -> b.setSelected(b == btn));
				this.binding.setEff(eff);
				filter();
			});
			btn.setTextColor(EffectivenessUtils.getColor(getContext(), eff));
			btn.setText(String.format(getString(R.string.titreEffType), eff));
			btn.setGravity(Gravity.CENTER_VERTICAL);
			btn.setAllCaps(false);
			int nbCols = eff > Effectiveness.NORMAL.getMultiplier() ? 3 : 2;
			GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
			GridLayout.Spec columnSpec = GridLayout.spec(GridLayout.UNDEFINED, nbCols, nbCols);
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
			params.width = 0;
			btn.setLayoutParams(params);
			btnLst.put(eff, btn);
			this.binding.lstEffType.addView(btn);
		}

		double maxEff = setEff.stream().max(Double::compare).orElse(1D);
		this.binding.setEff(maxEff);
		Button btnMax = btnLst.get(maxEff);
		if(btnMax != null){
			btnMax.setSelected(true);
		}

		String typeName = TypeFragmentArgs.fromBundle(getArguments()).getTypeName();
		Type type = Type.valueOfIgnoreCase(typeName);
		setType(type);

		PreferencesUtils.getInstance().registerObserver(this);

		return this.binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		PreferencesUtils.getInstance().unregisterObserver(this);
		super.onDestroyView();
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
		filter();
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getType() != null) {
			outState.putString(TYPE_SELECTED_KEY, getType().name());
		}
	}

	@Override
	public void update(Observable o) {
		if (o == null) {
			return;
		}
		if (o.equals(PreferencesUtils.getInstance())) {
			filter();
		}
	}

	private void filter() {
		PkmnDescTypeFilterInfo filterInfo = new PkmnDescTypeFilterInfo();
		filterInfo.setTypeEff(this.binding.getType());
		filterInfo.setEff(this.binding.getEff());
		this.adapter.filter(filterInfo.toFilter());
	}
}