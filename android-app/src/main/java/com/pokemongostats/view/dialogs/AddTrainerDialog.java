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

import com.pokemongostats.R;
import com.pokemongostats.controller.db.trainer.TrainerAsyncDAO;
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
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class AddTrainerDialog extends DialogFragment {

	private RadioGroup trainerTeamRadioGroup;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater
				.from(getActivity().getApplicationContext())
				.inflate(R.layout.add_trainer_dialog, null);
		final EditText trainerNameEditText = (EditText) form
				.findViewById(R.id.trainerNameEditText);
		final EditText trainerLevelEditText = (EditText) form
				.findViewById(R.id.trainerLevelEditText);
		this.trainerTeamRadioGroup = (RadioGroup) form
				.findViewById(R.id.teamsRadioGroup);

		// buttons listeners
		OnClickListener onClickAdd = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// name
				String name = trainerNameEditText.getText().toString();

				// level
				String levelStr = trainerLevelEditText.getText().toString();
				int level = 0;
				if (levelStr != null && !levelStr.isEmpty()) {
					level = Integer.parseInt(levelStr);
				}

				// team
				Team team = getSelectedTeam(
						trainerTeamRadioGroup.getCheckedRadioButtonId());

				// create business object
				Trainer trainer = new Trainer(name, level, team);

				// call database async
				TrainerAsyncDAO dao = new TrainerAsyncDAO(
						getActivity().getApplicationContext());
				dao.new SaveAsyncTask<Void>() {
					@Override
					protected void onPostExecute(Void result) {
						onTrainerAdded();
					}
				}.execute(trainer);
			}
		};

		OnClickListener onClickCancel = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				AddTrainerDialog.this.getDialog().cancel();
			}
		};

		/// build add gym dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(form);
		builder.setTitle(R.string.add_trainer_dialog_title);
		builder.setPositiveButton(R.string.add, onClickAdd);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Override this method to make an action when async add action end
	 */
	public void onTrainerAdded() {
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
