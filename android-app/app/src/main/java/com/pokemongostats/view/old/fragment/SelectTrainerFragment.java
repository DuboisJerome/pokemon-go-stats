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

package com.pokemongostats.view.old.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.parcalables.PclbTrainer;
import com.pokemongostats.view.adapters.TrainerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class SelectTrainerFragment extends Fragment {

    private static final String TRAINERS_STATE_KEY = "trainers";
    private Spinner trainersSpinner;
    private TrainerAdapter trainersAdapter;
    private ArrayList<PclbTrainer> pclbTrainers;
    private Button prevBtn;
    private Button nextBtn;
    private SelectTrainerFragmentListener mCallback;
    /**
     * A call-back call when the user click edit icon
     */
    private OnClickListener onClickEditTrainer = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };
    /**
     * A call-back call when the user click add icon
     */
    private OnClickListener onClickAddTrainer = new OnClickListener() {

        @Override
        public void onClick(View v) {
//			new EditTrainerDialog() {
//				@Override
//				public void onTrainerAdded(final Trainer addedTrainer) {
//					mCallback.onTrainerSelected(addedTrainer);
//				}
//			}.show(getFragmentManager(), EditTrainerDialog.class.getName());
        }
    };
    /**
     * A call-back call when the user click select
     */
    private OnClickListener onClickNext = new OnClickListener() {

        @Override
        public void onClick(View v) {

            int position = trainersSpinner.getSelectedItemPosition();
            if (position != AdapterView.INVALID_POSITION) {
                // selected trainer
                mCallback.onTrainerSelected(trainersAdapter.getItem(position));
            } else {
                // TODO message in string.xml
                Log.e(TagUtils.DEBUG, "You must select a trainer");
                Toast.makeText(getActivity(), "You must select a trainer",
                        Toast.LENGTH_LONG).show();
            }
        }
    };
    /**
     * A call-back call when the user click back
     */
    private OnClickListener onClickBack = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    public SelectTrainerFragment() {
    }

    public void setCallback(SelectTrainerFragmentListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trainersAdapter = new TrainerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_trainer,
                container, false);
        // trainers spinner
        trainersSpinner = (Spinner) view.findViewById(R.id.trainers);
        trainersSpinner.setAdapter(trainersAdapter);

        // edit button
        view.findViewById(R.id.editTrainerBtn)
                .setOnClickListener(onClickEditTrainer);

        // add button
        view.findViewById(R.id.addTrainerBtn)
                .setOnClickListener(onClickAddTrainer);

        // prev button
        prevBtn = (Button) view.findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(onClickBack);

        // next button
        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(onClickNext);
        nextBtn.setEnabled(
                trainersAdapter != null && trainersAdapter.getCount() >= 0);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            pclbTrainers = savedInstanceState
                    .getParcelableArrayList(TRAINERS_STATE_KEY);
            if (pclbTrainers != null && !pclbTrainers.isEmpty()) {
                updateTrainersSpinner(pclbTrainers);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TRAINERS_STATE_KEY, pclbTrainers);
    }

    /**
     * Update trainers spinner
     */
    public void updateTrainersSpinner(final List<? extends Trainer> list) {
        // on result value update Spinner
        trainersAdapter.clear();
        if (list != null && list.size() > 0) {
            Collections.sort(list);
            trainersAdapter.addAll(list);
            nextBtn.setEnabled(true);

            // to save state
            pclbTrainers = new ArrayList<>();
            for (Trainer t : list) {
                pclbTrainers.add(new PclbTrainer(t));
            }
        } else {
            nextBtn.setEnabled(false);
            pclbTrainers = null;
        }
    }

    public interface SelectTrainerFragmentListener {
        void onTrainerSelected(final Trainer t);
    }
}
