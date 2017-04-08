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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.controller.utils.MoveUtils;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.PkmnDescView;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.listitem.MoveListItemView;
import com.pokemongostats.view.listitem.TypeListItemView;
import com.pokemongostats.view.utils.KeyboardUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class PkmnFragment extends HistorizedFragment<PkmnDesc>
        implements
        HasMoveSelectable,
        HasTypeSelectable,
        HasPkmnDescSelectable {

    private static final String PKMN_SELECTED_KEY = "PKMN_SELECTED_KEY";

    // pokedex
    private AutoCompleteTextView searchPkmnDesc;
    private PkmnDescAdapter pkmnDescAdapter;
    private final OnItemClickListener onPkmnSelectedListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position != AdapterView.INVALID_POSITION) {
                showItem(pkmnDescAdapter.getItem(position));
            }
        }
    };
    // selected pkmn
    private PkmnDescView selectedPkmnView;
    private MoveAdapter adapterQuickMoves;
    private MoveAdapter adapterChargeMoves;
    // super weaknesses adapter
    private TypeAdapter adapterSuperWeakness;
    // weaknesses adapter
    private TypeAdapter adapterWeakness;
    // resistances adapter
    private TypeAdapter adapterResistance;
    // super resistances adapter
    private TypeAdapter adapterSuperResistance;
    private SelectedVisitor<Type> mCallbackType;
    private SelectedVisitor<Move> mCallbackMove;
    private SelectedVisitor<PkmnDesc> mCallbackPkmnDesc;
    private com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Type> onTypeClicked;
    private com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Move> onMoveClicked;
    private PokedexDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // don't show keyboard on activity start
        KeyboardUtils.initKeyboard(getActivity());

        dao = new PokedexDAO(getActivity());
        pkmnDescAdapter = new PkmnDescAdapter(getActivity());
        pkmnDescAdapter.addAll(dao.getListPkmnDesc());
        //
        adapterQuickMoves = new MoveAdapter(getActivity());
        adapterQuickMoves.setPPSVisible(true);
        adapterQuickMoves.setPowerVisible(true);
        //
        adapterChargeMoves = new MoveAdapter(getActivity());
        adapterChargeMoves.setPPSVisible(true);
        adapterChargeMoves.setPowerVisible(true);
        //
        adapterSuperWeakness = new TypeAdapter(getActivity(),
                android.R.layout.simple_spinner_item);
        //
        adapterWeakness = new TypeAdapter(getActivity(),
                android.R.layout.simple_spinner_item);
        //
        adapterResistance = new TypeAdapter(getActivity(),
                android.R.layout.simple_spinner_item);
        //
        adapterSuperResistance = new TypeAdapter(getActivity(),
                android.R.layout.simple_spinner_item);

        onTypeClicked = new com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Type>() {
            @Override
            public void onItemClick(Type item) {
                if (mCallbackType == null) {
                    return;
                }
                mCallbackType.select(item);
            }
        };
        onMoveClicked = new com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Move>() {
            @Override
            public void onItemClick(Move item) {
                if (mCallbackMove == null) {
                    return;
                }
                mCallbackMove.select(item);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_pokedex, container, false);

        // search view
        searchPkmnDesc = (AutoCompleteTextView) currentView
                .findViewById(R.id.search_pokemon);
        searchPkmnDesc.setAdapter(pkmnDescAdapter);
        searchPkmnDesc.setOnItemClickListener(onPkmnSelectedListener);

        //
        selectedPkmnView = (PkmnDescView) currentView
                .findViewById(R.id.selected_pkmn);
        selectedPkmnView.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);

        MoveListItemView quickMoves = (MoveListItemView) currentView
                .findViewById(R.id.pkmn_desc_quickmoves);
        quickMoves.setAdapter(adapterQuickMoves);
        quickMoves.setOnItemClickListener(onMoveClicked);

        MoveListItemView chargeMoves = (MoveListItemView) currentView
                .findViewById(R.id.pkmn_desc_chargemoves);
        chargeMoves.setAdapter(adapterChargeMoves);
        chargeMoves.setOnItemClickListener(onMoveClicked);

        // super weaknesses
        TypeListItemView listSuperWeakness = (TypeListItemView) currentView
                .findViewById(R.id.list_super_weaknesses);
        listSuperWeakness.setAdapter(adapterSuperWeakness);
        listSuperWeakness.setOnItemClickListener(onTypeClicked);

        // weaknesses
        TypeListItemView listWeakness = (TypeListItemView) currentView
                .findViewById(R.id.list_weaknesses);
        listWeakness.setAdapter(adapterWeakness);
        listWeakness.setOnItemClickListener(onTypeClicked);

        // resistances
        TypeListItemView listResistance = (TypeListItemView) currentView
                .findViewById(R.id.list_resistances);
        listResistance.setAdapter(adapterResistance);
        listResistance.setOnItemClickListener(onTypeClicked);

        // super resistances
        TypeListItemView listSuperResistance = (TypeListItemView) currentView
                .findViewById(R.id.list_super_resistances);
        listSuperResistance.setAdapter(adapterSuperResistance);
        listSuperResistance.setOnItemClickListener(onTypeClicked);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && currentItem == null) {
            currentItem = savedInstanceState.getParcelable(PKMN_SELECTED_KEY);
            updateView();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putParcelable(PKMN_SELECTED_KEY,
                    new PclbPkmnDesc(currentItem));
        }
    }

    @Override
    protected void updateViewImpl() {
        final PkmnDesc pkmn = currentItem;
        adapterQuickMoves.setOwner(pkmn);
        adapterChargeMoves.setOwner(pkmn);
        if (pkmn != null) {
            adapterWeakness.setNotifyOnChange(false);
            adapterSuperWeakness.setNotifyOnChange(false);
            adapterResistance.setNotifyOnChange(false);
            adapterSuperResistance.setNotifyOnChange(false);
            // reset previous
            adapterWeakness.clear();
            adapterSuperWeakness.clear();
            adapterResistance.clear();
            adapterSuperResistance.clear();

            for (Type t : Type.values()) {
                Effectiveness eff = EffectivenessUtils.getTypeEffOnPokemon(t, pkmn);
                switch (eff) {
                    case NOT_VERY_EFFECTIVE:
                        adapterResistance.add(t);
                        break;
                    case REALLY_NOT_VERY_EFFECTIVE:
                        adapterSuperResistance.add(t);
                        break;
                    case REALLY_SUPER_EFFECTIVE:
                        adapterSuperWeakness.add(t);
                        break;
                    case SUPER_EFFECTIVE:
                        adapterWeakness.add(t);
                        break;
                    case NORMAL:
                    default:
                        break;
                }
            }

            // notify
            adapterWeakness.notifyDataSetChanged();
            adapterSuperWeakness.notifyDataSetChanged();
            adapterResistance.notifyDataSetChanged();
            adapterSuperResistance.notifyDataSetChanged();

            selectedPkmnView.setPkmnDesc(pkmn);

            adapterQuickMoves.setNotifyOnChange(false);
            adapterChargeMoves.setNotifyOnChange(false);
            adapterQuickMoves.clear();
            adapterChargeMoves.clear();
            Map<Move.MoveType, List<Move>> map = MoveUtils.getMovesMap(dao.getListMove(), pkmn.getMoveIds());
            adapterChargeMoves.addAll(map.get(Move.MoveType.CHARGE));
            adapterQuickMoves.addAll(map.get(Move.MoveType.QUICK));
            Comparator<Move> comparatorMove = MoveComparators.getComparatorByPps(pkmn);
            adapterQuickMoves.sort(comparatorMove);
            adapterChargeMoves.sort(comparatorMove);
            adapterQuickMoves.notifyDataSetChanged();
            adapterChargeMoves.notifyDataSetChanged();
        }

        searchPkmnDesc.setText("");
        KeyboardUtils.hideKeyboard(getActivity());
    }

    /********************
     * LISTENERS / CALLBACK
     ********************/

    @Override
    public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor) {
        this.mCallbackType = visitor;
    }

    @Override
    public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
        this.mCallbackMove = visitor;
    }

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor) {
        this.mCallbackPkmnDesc = visitor;
    }
}
