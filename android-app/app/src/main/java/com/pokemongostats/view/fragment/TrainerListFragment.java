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
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.dao.TrainerDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.comparators.TrainerComparators;
import com.pokemongostats.view.adapters.TrainerAdapter;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.old.dialog.AddTrainerDialog;

import java.util.Comparator;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class TrainerListFragment extends HistorizedFragment<TrainerListFragment.SortChoice> implements Observer {

    private static final String TRAINER_LIST_ITEM_KEY = "TRAINER_LIST_ITEM_KEY";
    // view
    private Spinner spinnerSortChoice;
    private ImageButton addTrainerBtn;
    private AddTrainerDialog addTrainerDialog;
    // controler
    private ArrayAdapter<TrainerListFragment.SortChoice> adapterSortChoice;
    private final AdapterView.OnItemSelectedListener onItemSortSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (position != AdapterView.INVALID_POSITION) {
                showItem(adapterSortChoice.getItem(position));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    };
    private TrainerAdapter adapterTrainers;
    private TrainerDAO dao;
    private SelectedVisitor<Trainer> mCallbackTrainer;
    // model

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new TrainerDAO(getActivity());
        adapterSortChoice = new ArrayAdapter<TrainerListFragment.SortChoice>(getActivity(),
                android.R.layout.simple_spinner_item, TrainerListFragment.SortChoice.values()) {

            /**
             * {@inheritDoc}
             */
            @Override
            @NonNull
            public View getView(int position, View convertView,
                                @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return initText(position, v);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                return initText(position, v);
            }

            private View initText(int position, View v) {
                try {
                    TextView text = (TextView) v;
                    TrainerListFragment.SortChoice sortChoice = getItem(position);
                    if (sortChoice != null) {
                        text.setText(getString(sortChoice.idLabel));
                    }
                } catch (Exception e) {
                    Log.e(TagUtils.DEBUG,
                            "Error spinner sort choice", e);
                }
                return v;
            }
        };
        adapterSortChoice.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        adapterTrainers = new TrainerAdapter(getActivity());
        adapterTrainers.addAll(dao.getListTrainers());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_trainer_list, container,
                false);

        spinnerSortChoice = (Spinner) currentView.findViewById(R.id.list_sort_choice);
        spinnerSortChoice.setAdapter(adapterSortChoice);
        spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

        TextView emptyView = (TextView) currentView.findViewById(R.id.empty_list_textview);

        ListView listViewTrainers = (ListView) currentView
                .findViewById(R.id.list_items_found);
        listViewTrainers.setAdapter(adapterTrainers);
        listViewTrainers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCallbackTrainer == null) {
                    return;
                }
                mCallbackTrainer.select(adapterTrainers.getItem(i));
            }
        });
        listViewTrainers.setEmptyView(emptyView);

        addTrainerBtn = (ImageButton) currentView.findViewById(R.id.add_trainer_btn);
        addTrainerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrainerDialog = new AddTrainerDialog();
                addTrainerDialog.show(getFragmentManager(), AddTrainerDialog.class.getName());
            }
        });

        return currentView;
    }

    @Override
    public void update(Observable o) {
        if (o == null) {
            return;
        }
        if (o.equals(addTrainerDialog)) {//
            adapterTrainers.add(addTrainerDialog.getTrainer());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (currentItem == null) {
            if (savedInstanceState != null) {
                String saved = savedInstanceState.getString(TRAINER_LIST_ITEM_KEY);
                currentItem = TrainerListFragment.SortChoice.valueOf(saved);
                spinnerSortChoice.setSelection(adapterSortChoice.getPosition(currentItem), false);
            }
            updateViewImpl();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putString(TRAINER_LIST_ITEM_KEY, currentItem.name());
        }
    }

    @Override
    protected void updateViewImpl() {
        if (currentItem == null) {
            currentItem = SortChoice.COMPARE_BY_NAME;
            spinnerSortChoice.setSelection(adapterSortChoice.getPosition(currentItem), false);
        }
        final TrainerListFragment.SortChoice sortChoice = currentItem;
        final Comparator<Trainer> c;
        switch (sortChoice) {
            case COMPARE_BY_NAME:
                c = TrainerComparators.getComparatorByName();
                break;
            case COMPARE_BY_LVL:
                c = TrainerComparators.getComparatorByLvl();
                break;
            default:
                Log.e(TagUtils.DEBUG,
                        "SortChoice not found : " + sortChoice);
                c = null;
                break;
        }

        adapterTrainers.sort(c); // include notify data set changed
    }

    public enum SortChoice {

        //
        COMPARE_BY_NAME(R.string.sort_by_name),
        //
        COMPARE_BY_LVL(R.string.sort_by_lvl);

        private int idLabel;

        SortChoice(final int idLabel) {
            this.idLabel = idLabel;
        }
    }
}
