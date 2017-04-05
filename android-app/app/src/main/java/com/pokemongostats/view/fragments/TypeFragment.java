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

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.CustomExpandableView;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
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
    private PkmnDescAdapter adapterSW;
    // weaknesses list
    private PkmnDescAdapter adapterW;
    // resistances list
    private PkmnDescAdapter adapterR;
    // super resistances list
    private PkmnDescAdapter adapterSR;

    private SelectedVisitor<PkmnDesc> mCallbackPkmn;
    private AdapterView.OnItemClickListener onPkmnClicked;
    private PokedexDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new PokedexDAO(getActivity());
        // super weaknesses list
        adapterSW = new PkmnDescAdapter(getActivity());
        adapterSW.setNotifyOnChange(false);
        // weaknesses list
        adapterW = new PkmnDescAdapter(getActivity());
        adapterW.setNotifyOnChange(false);
        // resistances list
        adapterR = new PkmnDescAdapter(getActivity());
        adapterR.setNotifyOnChange(false);
        // super resistances list
        adapterSR = new PkmnDescAdapter(getActivity());
        adapterSR.setNotifyOnChange(false);

        onPkmnClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PkmnDesc item = (PkmnDesc) adapterView.getItemAtPosition(i);
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
        currentType.setType(currentItem);
        currentType.update();

        // super weaknesses
        initEffectiveness(R.id.empty_sw_content, R.id.pkmn_sw_listitem, R.id.expandable_sw, adapterSW);

        // weaknesses
        initEffectiveness(R.id.empty_w_content, R.id.pkmn_w_listitem, R.id.expandable_w, adapterW);

        // resistances
        initEffectiveness(R.id.empty_r_content, R.id.pkmn_r_listitem, R.id.expandable_r, adapterR);

        // super resistances
        initEffectiveness(R.id.empty_sr_content, R.id.pkmn_sr_listitem, R.id.expandable_sr, adapterSR);

        return currentView;
    }

    private void initEffectiveness(final int emptyId, final int listId, final int expandableId, final ListAdapter a) {
        final TextView empty = (TextView) currentView.findViewById(emptyId);
        final ListView listView = (ListView) currentView.findViewById(listId);
        listView.setAdapter(a);
        listView.setOnItemClickListener(onPkmnClicked);

        final CustomExpandableView expandable = (CustomExpandableView) currentView
                .findViewById(expandableId);
        expandable.addExpandableView(listView);
        expandable.addExpandableView(empty);
        expandable.registerObserver(new Observer() {
            @Override
            public void update(Observable o) {
                if (o == null) {
                    return;
                }
                if (o.equals(expandable)) {
                    if (expandable.isExpand()) {
                        listView.setVisibility(a.isEmpty() ? View.GONE : View.VISIBLE);
                        empty.setVisibility(a.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }
            }
        });

        a.registerDataSetObserver(new DataSetObserver() {

            private void updateExpandable() {
                if (expandable.isExpand()) {
                    listView.setVisibility(a.isEmpty() ? View.GONE : View.VISIBLE);
                    empty.setVisibility(a.isEmpty() ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onChanged() {
                updateExpandable();
            }

            @Override
            public void onInvalidated() {
                updateExpandable();
            }
        });
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

        adapterSW.setNotifyOnChange(false);
        adapterW.setNotifyOnChange(false);
        adapterR.setNotifyOnChange(false);
        adapterSR.setNotifyOnChange(false);
        /** pokemons */
        adapterR.clear();
        adapterSR.clear();
        adapterSW.clear();
        adapterW.clear();

        for (PkmnDesc p : dao.getListPkmnDesc(
                PreferencesUtils.isLastEvolutionOnly(getActivity()))) {
            switch (EffectivenessUtils.getTypeEffOnPokemon(currentItem, p)) {
                case NOT_VERY_EFFECTIVE:
                    adapterR.add(p);
                    break;
                case REALLY_NOT_VERY_EFFECTIVE:
                    adapterSR.add(p);
                    break;
                case REALLY_SUPER_EFFECTIVE:
                    adapterSW.add(p);
                    break;
                case SUPER_EFFECTIVE:
                    adapterW.add(p);
                    break;
                case NORMAL:
                default:
                    break;
            }
        }

        Comparator<PkmnDesc> comparatorPkmn = PkmnDescComparators.getComparatorByMaxCp();
        adapterR.sort(comparatorPkmn);
        adapterSR.sort(comparatorPkmn);
        adapterSW.sort(comparatorPkmn);
        adapterW.sort(comparatorPkmn);

        adapterR.notifyDataSetChanged();
        adapterSR.notifyDataSetChanged();
        adapterSW.notifyDataSetChanged();
        adapterW.notifyDataSetChanged();
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
