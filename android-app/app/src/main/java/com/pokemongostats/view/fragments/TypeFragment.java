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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.CustomExpandableView;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.Comparator;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class TypeFragment extends HistorizedFragment<Type>
        implements
        HasPkmnDescSelectable,
        HasMoveSelectable {

    private static final String TYPE_SELECTED_KEY = "TYPE_SELECTED_KEY";
    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    // action to execute when click on type in ChooseTypeDialogFragment
    final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
        @Override
        public void select(Type t) {
            // hide dialog
            chooseTypeDialog.dismiss();
            // load view with type
            TypeFragment.this.showItem(t);
        }
    };
    private final OnClickListener onClickType = new OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseTypeDialog.setVisitor(visitor);
            chooseTypeDialog.setCurrentType(TypeFragment.this.getCurrentItem());
            chooseTypeDialog.show(getFragmentManager(), "chooseTypeDialog");
        }
    };
    private TypeRowView currentType;

    // super weaknesses list
    private PkmnDescAdapter adapterSuperWeaknesses;
    // weaknesses list
    private PkmnDescAdapter adapterWeaknesses;
    // resistances list
    private PkmnDescAdapter adapterResistances;
    // super resistances list
    private PkmnDescAdapter adapterSuperResistances;

    private SelectedVisitor<PkmnDesc> mCallbackPkmn;
    private AdapterView.OnItemClickListener onPkmnClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // super weaknesses list
        adapterSuperWeaknesses = new PkmnDescAdapter(getActivity());
        adapterSuperWeaknesses.setNotifyOnChange(false);
        // weaknesses list
        adapterWeaknesses = new PkmnDescAdapter(getActivity());
        adapterWeaknesses.setNotifyOnChange(false);
        // resistances list
        adapterResistances = new PkmnDescAdapter(getActivity());
        adapterResistances.setNotifyOnChange(false);
        // super resistances list
        adapterSuperResistances = new PkmnDescAdapter(getActivity());
        adapterSuperResistances.setNotifyOnChange(false);

        onPkmnClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PkmnDesc item = (PkmnDesc)adapterView.getItemAtPosition(i);
                if (mCallbackPkmn == null || item == null) {
                    return;
                }
                mCallbackPkmn.select(item);
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

        // super weaknesses
        ListView superWeaknesses = (ListView) currentView.findViewById(R.id.pkmn_super_weaknesses_listitem);
        superWeaknesses.setAdapter(adapterSuperWeaknesses);
        superWeaknesses.setOnItemClickListener(onPkmnClicked);

        CustomExpandableView expandableSuperWeaknesses = (CustomExpandableView) currentView
                .findViewById(R.id.expandable_super_weaknesses);
        expandableSuperWeaknesses.setExpandableView(superWeaknesses);

        // weaknesses
        ListView weaknesses = (ListView) currentView.findViewById(R.id.pkmn_weaknesses_listitem);
        weaknesses.setAdapter(adapterWeaknesses);
        weaknesses.setOnItemClickListener(onPkmnClicked);

        CustomExpandableView expandableWeaknesses = (CustomExpandableView) currentView
                .findViewById(R.id.expandable_weaknesses);
        expandableWeaknesses.setExpandableView(weaknesses);

        // resistances
        ListView resistances = (ListView) currentView.findViewById(R.id.pkmn_resistances_listitem);
        resistances.setAdapter(adapterResistances);
        resistances.setOnItemClickListener(onPkmnClicked);

        CustomExpandableView expandableResistances = (CustomExpandableView) currentView
                .findViewById(R.id.expandable_resistances);
        expandableResistances.setExpandableView(resistances);

        // super resistances
        ListView superResistances = (ListView) currentView.findViewById(R.id.pkmn_super_resistances_listitem);
        superResistances.setAdapter(adapterSuperResistances);
        superResistances.setOnItemClickListener(onPkmnClicked);

        CustomExpandableView expandableSuperResistances = (CustomExpandableView) currentView
                .findViewById(R.id.expandable_super_resistances);
        expandableSuperResistances.setExpandableView(superResistances);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (currentItem == null) {
            if (savedInstanceState != null) {
                String type = savedInstanceState.getString(TYPE_SELECTED_KEY);
                currentItem = (type == null || type.isEmpty())
                        ? null
                        : Type.valueOfIgnoreCase(type);
            }
            // if currentItem still null, default = Normal
            if (currentItem == null) {
                currentItem = Type.NORMAL;
            }
            updateView();
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
            currentItem = Type.NORMAL;
        }
        currentType.setType(currentItem);
        currentType.update();
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getActivity()
                .getApplication());

        adapterSuperWeaknesses.setNotifyOnChange(false);
        adapterWeaknesses.setNotifyOnChange(false);
        adapterResistances.setNotifyOnChange(false);
        adapterSuperResistances.setNotifyOnChange(false);
        /** pokemons */
        adapterResistances.clear();
        adapterSuperResistances.clear();
        adapterSuperWeaknesses.clear();
        adapterWeaknesses.clear();

        for (PkmnDesc p : app.getPokedex(
                PreferencesUtils.isLastEvolutionOnly(getActivity()))) {
            switch (EffectivenessUtils.getTypeEffOnPokemon(currentItem, p)) {
                case NOT_VERY_EFFECTIVE:
                    adapterResistances.add(p);
                    break;
                case REALLY_NOT_VERY_EFFECTIVE:
                    adapterSuperResistances.add(p);
                    break;
                case REALLY_SUPER_EFFECTIVE:
                    adapterSuperWeaknesses.add(p);
                    break;
                case SUPER_EFFECTIVE:
                    adapterWeaknesses.add(p);
                    break;
                case NORMAL:
                default:
                    break;
            }
        }

        Comparator<PkmnDesc> comparatorPkmn = PkmnDescComparators.getComparatorByMaxCp();
        adapterResistances.sort(comparatorPkmn);
        adapterSuperResistances.sort(comparatorPkmn);
        adapterSuperWeaknesses.sort(comparatorPkmn);
        adapterWeaknesses.sort(comparatorPkmn);

        adapterResistances.notifyDataSetChanged();
        adapterSuperResistances.notifyDataSetChanged();
        adapterSuperWeaknesses.notifyDataSetChanged();
        adapterWeaknesses.notifyDataSetChanged();
    }

    /********************
     * LISTENERS / CALLBACK
     ********************/

    @Override
    public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
        //this.mCallbackMove = visitor;
    }

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor) {
        this.mCallbackPkmn = visitor;
    }
}
