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
import android.widget.ListView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.model.parcalables.PclbMove;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.MoveDescView;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.utils.KeyboardUtils;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class MoveFragment extends HistorizedFragment<Move>
        implements
        HasPkmnDescSelectable,
        HasTypeSelectable {

    private static final String MOVE_SELECTED_KEY = "MOVE_SELECTED_KEY";

    private AutoCompleteTextView searchMove;
    private MoveAdapter movesAdapter;
    private final OnItemClickListener onMoveSelectedListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position != AdapterView.INVALID_POSITION) {
                showItem(movesAdapter.getItem(position));
            }
        }
    };
    // selected move
    private MoveDescView selectedMoveView;
    private PkmnDescAdapter adapterPkmnsWithMove;
    private SelectedVisitor<PkmnDesc> mCallbackPkmn;
    private SelectedVisitor<Type> mCallbackType;
    private AdapterView.OnItemClickListener onPkmnDescClicked;
    private PokedexDAO dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // don't show keyboard on activity start
        KeyboardUtils.initKeyboard(getActivity());

        dao = new PokedexDAO(getActivity());
        movesAdapter = new MoveAdapter(getActivity());
        movesAdapter.addAll(dao.getListMove());

        adapterPkmnsWithMove = new PkmnDescAdapter(getActivity());

        onPkmnDescClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PkmnDesc item = adapterPkmnsWithMove.getItem(i);
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
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_move, container,
                false);

        // search view
        searchMove = (AutoCompleteTextView) currentView
                .findViewById(R.id.search_move);
        searchMove.setHint(R.string.move_name_hint);
        searchMove.setAdapter(movesAdapter);
        searchMove.setOnItemClickListener(onMoveSelectedListener);

        //
        selectedMoveView = (MoveDescView) currentView
                .findViewById(R.id.move_selected_move);
        selectedMoveView.acceptSelectedVisitorType(mCallbackType);

        //
        ListView expandablePkmnsWithMove = (ListView) currentView
                .findViewById(R.id.move_pokemons_with_move);
        expandablePkmnsWithMove.setAdapter(adapterPkmnsWithMove);
        expandablePkmnsWithMove.setOnItemClickListener(onPkmnDescClicked);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && currentItem == null) {
            currentItem = savedInstanceState.getParcelable(MOVE_SELECTED_KEY);
            updateView();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putParcelable(MOVE_SELECTED_KEY,
                    new PclbMove(currentItem));
        }
    }

    @Override
    protected void updateViewImpl() {
        final Move m = currentItem;
        if (m != null) {

            /** pokemons */
            adapterPkmnsWithMove.setNotifyOnChange(false);
            adapterPkmnsWithMove.clear();
            adapterPkmnsWithMove.addAll(dao.getListPkmnFor(m));
            adapterPkmnsWithMove.sort(PkmnDescComparators.getComparatorByMaxCp());
            adapterPkmnsWithMove.notifyDataSetChanged();

            selectedMoveView.setMove(m);

            searchMove.setText("");
            KeyboardUtils.hideKeyboard(getActivity());
        }
    }

    /********************
     * LISTENERS / CALLBACK
     ********************/

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor) {
        this.mCallbackPkmn = visitor;
    }

    @Override
    public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor) {
        this.mCallbackType = visitor;
    }
}
