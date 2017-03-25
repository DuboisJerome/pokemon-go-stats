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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.InsertOrReplaceAsyncTask;
import com.pokemongostats.controller.db.gym.GymDescriptionTableDAO;
import com.pokemongostats.model.bean.GymDescription;
import com.pokemongostats.model.bean.Location;

import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public abstract class AddGymDescriptionDialog extends CustomDialogFragment {

	// TODO limiter latitude/longitude 6 chiffres après la virgule
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// dialog form
		final View form = LayoutInflater
				.from(getActivity().getApplicationContext())
				.inflate(R.layout.dialog_add_gym_desc, null);
		final EditText gymDescNameEditText = (EditText) form
				.findViewById(R.id.gymDescNameEditText);
		final EditText latEditText = (EditText) form
				.findViewById(R.id.gymDescLatitudeEditText);
		final EditText lonEditText = (EditText) form
				.findViewById(R.id.gymDescLongitudeEditText);

		// buttons listeners
		OnClickListener onClickAdd = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// name
				String name = gymDescNameEditText.getText().toString();

				// latitude
				String latStr = latEditText.getText().toString();

				// longitude
				String lonStr = lonEditText.getText().toString();

				// location
				Location location = new Location();
				if (latStr != null && !latStr.isEmpty() && lonStr != null
					&& !lonStr.isEmpty()) {
					location.setLatitude(Double.parseDouble(latStr));
					location.setLongitude(Double.parseDouble(lonStr));
				}

				// create business object
				GymDescription newGymDescription = new GymDescription();
				newGymDescription.setName(name);
				newGymDescription.setLocation(location);

				// call database async
				new InsertOrReplaceAsyncTask<GymDescription>() {
					@Override
					protected List<GymDescription> doInBackground(
							final GymDescription... params) {
						return new GymDescriptionTableDAO(
								getActivity().getApplicationContext())
										.insertOrReplaceThenSelectAll(params);
					}

					@Override
					public void onPostExecute(
							final List<GymDescription> result) {
						onGymDescAdded(result != null && result.size() > 0
								? result.get(0)
								: null);
					}
				}.execute(newGymDescription);
			}
		};

		OnClickListener onClickCancel = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				AddGymDescriptionDialog.this.getDialog().cancel();
			}
		};

		/// build add gym dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(form);
		builder.setTitle(R.string.add_gym_desc_dialog_title);
		builder.setPositiveButton(R.string.add, onClickAdd);
		builder.setNegativeButton(android.R.string.cancel, onClickCancel);

		return builder.create();
	}

	/**
	 * Override this method to make an action when async add action end
	 * 
	 * @param gymDescAdded
	 *            may be null
	 */
	public abstract void onGymDescAdded(GymDescription gymDescAdded);
}
