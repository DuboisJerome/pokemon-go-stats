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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.InsertOrReplaceAsyncTask;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.view.utils.HasRequiredField;

import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public abstract class AddTrainerDialog extends CustomDialogFragment
		implements
			HasRequiredField {

	private EditText trainerNameEditText;

	private EditText trainerLevelEditText;

	private RadioGroup trainerTeamRadioGroup;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater
				.from(getActivity().getApplicationContext())
				.inflate(R.layout.dialog_add_trainer, null);

		// name
		trainerNameEditText = (EditText) form
				.findViewById(R.id.trainerNameEditText);
		trainerNameEditText.addTextChangedListener(onEditTextChanged);
		// level
		trainerLevelEditText = (EditText) form
				.findViewById(R.id.trainerLevelEditText);
		trainerLevelEditText.addTextChangedListener(onEditTextChanged);

		// team
		trainerTeamRadioGroup = (RadioGroup) form
				.findViewById(R.id.teamsRadioGroup);
		trainerTeamRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group,
							int checkedId) {
						// TODO Auto-generated method stub

						checkAllField();
					}
				});

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
				Trainer trainer = new Trainer();
				trainer.setName(name);
				trainer.setLevel(level);
				trainer.setTeam(team);

				// call database async
				new InsertOrReplaceAsyncTask<Trainer>() {

					@Override
					protected List<Trainer> doInBackground(Trainer... params) {
						return new TrainerTableDAO(
								getActivity().getApplicationContext())
										.insertOrReplaceThenSelectAll(params);
					}

					@Override
					public void onPostExecute(List<Trainer> result) {
						onTrainerAdded(result != null && result.size() > 0
								? result.get(0)
								: null);
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
	 * {@inheritDoc}
	 */
	@Override
	public void onStart() {
		super.onStart();
		AlertDialog a = ((AlertDialog) getDialog());
		Button b = a.getButton(AlertDialog.BUTTON_POSITIVE);
		b.setEnabled(checkAllField());
	}

	/**
	 * Override this method to make an action when async add action end
	 * 
	 * @param trainerAdded
	 *            may be null
	 */
	public abstract void onTrainerAdded(Trainer addedTrainer);

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
				return Team.INSTINCT;
			default :
				return null;
		}
	}

	@Override
	public boolean checkAllField() {

		String name = trainerNameEditText.getText().toString();
		boolean nameOk = name != null && !name.isEmpty();

		String lvl = trainerLevelEditText.getText().toString();
		boolean levelOk = lvl != null && !lvl.isEmpty();

		boolean teamOk = getSelectedTeam(
				trainerTeamRadioGroup.getCheckedRadioButtonId()) != null;

		boolean ok = nameOk && levelOk && teamOk;

		((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
				.setEnabled(ok);

		return ok;

	}

	private TextWatcher onEditTextChanged = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			checkAllField();
		}
	};
}
