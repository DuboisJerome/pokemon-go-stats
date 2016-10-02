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

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.model.Trainer;
import com.pokemongostats.view.adapter.TrainerArrayAdapter;
import com.pokemongostats.view.dialogs.AddTrainerDialog;

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

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class SelectTrainerFragment extends Fragment {

	public interface SelectTrainerFragmentListener {
		public void onTrainerSelected(final Trainer t);
	}

	private Spinner trainersSpinner;

	private TrainerArrayAdapter trainersAdapter;

	private Button nextBtn;

	private SelectTrainerFragmentListener mCallback;

	public SelectTrainerFragment(final SelectTrainerFragmentListener callback) {
		this.mCallback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.select_trainer_fragment, container, false);

		// trainers spinner
		trainersSpinner = (Spinner) view.findViewById(R.id.trainers);
		trainersAdapter = new TrainerArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
		trainersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		trainersSpinner.setAdapter(trainersAdapter);

		// edit button
		view.findViewById(R.id.editTrainer).setOnClickListener(onClickEditTrainer);

		// add button
		view.findViewById(R.id.addTrainer).setOnClickListener(onClickAddTrainer);

		// cancel button
		view.findViewById(R.id.cancelBtn).setOnClickListener(onClickCancel);

		// next button
		nextBtn = (Button) view.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(onClickSelectTrainer);

		return view;
	}

	/**
	 * Update trainers spinner
	 */
	public void updateTrainersSpinner(final List<Trainer> list) {
		// on result value update Spinner
		trainersAdapter.clear();
		if (list != null && list.size() > 0) {
			trainersAdapter.addAll(list);
			nextBtn.setEnabled(true);
		} else {
			nextBtn.setEnabled(false);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		nextBtn.setEnabled(false);
	}

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
			new AddTrainerDialog() {
				@Override
				public void onTrainerAdded(final Trainer addedTrainer) {
					mCallback.onTrainerSelected(addedTrainer);
				}
			}.show(getFragmentManager(), AddTrainerDialog.class.getName());
		}
	};

	/**
	 * A call-back call when the user click select
	 */
	private OnClickListener onClickSelectTrainer = new OnClickListener() {

		@Override
		public void onClick(View v) {

			int position = trainersSpinner.getSelectedItemPosition();
			if (position != AdapterView.INVALID_POSITION) {
				// selected trainer
				mCallback.onTrainerSelected(trainersAdapter.getItem(position));
			} else {
				// TODO message in string.xml
				Log.e(getClass().getName(), "You must select a trainer");
				Toast.makeText(getActivity(), "You must select a trainer", Toast.LENGTH_LONG);
			}
		}
	};

	/**
	 * A call-back call when the user click cancel
	 */
	private OnClickListener onClickCancel = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
}
