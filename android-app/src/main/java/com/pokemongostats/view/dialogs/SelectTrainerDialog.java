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

package com.pokemongostats.view.dialogs;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.Team;
import com.pokemongostats.model.Trainer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class SelectTrainerDialog extends DialogFragment {

	/** trainers */
	private Spinner trainersSpinner;

	/** TODO */
	private ArrayAdapter<Trainer> trainersAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater
				.from(getActivity().getApplicationContext())
				.inflate(R.layout.select_trainer_dialog, null);
		trainersSpinner = (Spinner) form.findViewById(R.id.trainers);

		//
		trainersAdapter = new ArrayAdapter<Trainer>(
				getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item);
		trainersAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		trainersSpinner.setAdapter(trainersAdapter);

		// buttons listeners
		OnClickListener onClickSelect = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				int position = trainersSpinner.getSelectedItemPosition();
				if (position != AdapterView.INVALID_POSITION) {
					// selected trainer
					Trainer trainer = trainersAdapter.getItem(position);
					onTrainerSelected(trainer);
				} else {
					// TODO message "you must select a trainer"
				}
			}
		};

		OnClickListener onClickCancel = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				getDialog().cancel();
			}
		};

		/// build add gym dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(form);
		builder.setTitle(R.string.select_trainer_dialog_title);
		builder.setPositiveButton(R.string.select, onClickSelect);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Update trainers spinner
	 */
	public void updateTrainersSpinner() {

		new GetAllAsyncTask<Trainer>() {

			@Override
			protected List<Trainer> doInBackground(Long... params) {
				return new TrainerTableDAO(
						getActivity().getApplicationContext())
								.selectAll(params);
			}

			@Override
			public void onPostExecute(List<Trainer> list) {
				// on result value update Spinner
				trainersAdapter.clear();
				trainersAdapter.addAll(list);
			}
		}.execute();
	}

	/**
	 * Override this method to make an action when async add action end
	 */
	public void onTrainerSelected(final Trainer selectedTrainer) {
	}

	/**
	 * @return selected team
	 */
	private Team getSelectedTeam(int teamId) {
		switch (teamId) {
			case R.id.radio_valor :
				return Team.VALOR;
			case R.id.radio_mystic :
				return Team.MYSTIC;
			case R.id.radio_instinct :
				return Team.INSCTINCT;
			default :
				return null;
		}
	}
}
