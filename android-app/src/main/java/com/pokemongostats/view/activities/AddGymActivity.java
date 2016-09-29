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

package com.pokemongostats.view.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.db.gym.GymDescriptionTableDAO;
import com.pokemongostats.model.Gym;
import com.pokemongostats.model.GymDescription;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.Team;
import com.pokemongostats.model.Trainer;
import com.pokemongostats.view.dialogs.AddGymDescriptionDialog;
import com.pokemongostats.view.dialogs.SelectTrainerDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity to add a gym at the current date to the database
 * 
 * @author Zapagon
 *
 */
public class AddGymActivity extends Activity {

	/** Spinner displaying gym desc */
	private Spinner gymDescSpinner;

	/** Spinner displaying gym levels */
	private Spinner gymLevels;

	/** Team choice */
	private RadioGroup teamsRadioGroup;

	/** TODO */
	private ArrayAdapter<GymDescription> gymDescNameAdapter;

	public AddGymActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_gym_activity);

		// gym desc
		gymDescSpinner = (Spinner) findViewById(R.id.gymDescriptions);
		gymDescNameAdapter = new ArrayAdapter<GymDescription>(
				AddGymActivity.this, android.R.layout.simple_spinner_item);
		gymDescNameAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		gymDescSpinner.setAdapter(gymDescNameAdapter);
		updateGymDescSpinner();

		// level
		gymLevels = (Spinner) findViewById(R.id.gymLevels);
		gymLevels.setAdapter(new ArrayAdapter<Integer>(getApplicationContext(),
				android.R.layout.simple_spinner_item,
				new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));

		// team
		teamsRadioGroup = (RadioGroup) findViewById(R.id.teamsRadioGroup);

		// listeners
		View view;
		// "+" add gym name
		view = findViewById(R.id.addGymDesc);
		view.setOnClickListener(onClickAddGymDesc);
		// "+" add pokemon to gym
		view = findViewById(R.id.addPokemonGym);
		view.setOnClickListener(onClickAddPokemonToGym);
		// "-" remove pokemon to gym
		view = findViewById(R.id.removePokemonGym);
		view.setOnClickListener(onClickRemovePokemonToGym);
		// Validate activity
		view = findViewById(R.id.addGymStatus);
		view.setOnClickListener(onClickValidateGym);
	}

	/**
	 * TODO
	 */
	public void updateGymDescSpinner() {

		// retrieve values asynchronously
		new GetAllAsyncTask<GymDescription>() {

			@Override
			protected List<GymDescription> doInBackground(Long... params) {
				return new GymDescriptionTableDAO(getApplicationContext())
						.selectAll(params);
			}

			@Override
			public void onPostExecute(List<GymDescription> list) {
				// on result value update Spinner
				gymDescNameAdapter.clear();
				gymDescNameAdapter.addAll(list);
			}

		}.execute();

	}

	/**
	 * Called when the activity is about to start interacting with the user.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// retrieve gyms name
	}

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	/**
	 * Called right before your activity's option menu is displayed.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A call-back for when the user click "+" next to gyms names
	 */
	private OnClickListener onClickAddGymDesc = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// show add gym description dialog
			AddGymDescriptionDialog dialog = new AddGymDescriptionDialog() {

				@Override
				public void onGymAdded() {
					AddGymActivity.this.updateGymDescSpinner();
				}
			};
			dialog.show(AddGymActivity.this.getFragmentManager(),
					"AddGymDescriptionDialog");
		}
	};

	/**
	 * A call-back for when the user click "+" next to pokemons list
	 */
	private OnClickListener onClickAddPokemonToGym = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// show pokemon form
			Intent intent = new Intent(AddGymActivity.this,
					AddPokemonActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * A call-back for when the user click "x" next to pokemons list
	 */
	private OnClickListener onClickRemovePokemonToGym = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),
					"Remove pokemon to gym clicked !", Toast.LENGTH_SHORT)
					.show();

			// show add gym description dialog
			SelectTrainerDialog dialog = new SelectTrainerDialog() {

				@Override
				public void onTrainerSelected(final Trainer selectedTrainer) {
					// AddGymActivity.this.updateGymDescSpinner();
				}
			};
			dialog.show(AddGymActivity.this.getFragmentManager(),
					"SelectTrainerDialog");
		}
	};

	/**
	 * A call-back for when the user click validate
	 */
	private OnClickListener onClickValidateGym = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),
					"Gym level selected : " + getGymLevelSelected()
						+ " with team : " + getSelectedTeam(),
					Toast.LENGTH_LONG).show();

			// retrieve gym description from dropdown list
			GymDescription description = new GymDescription(null);
			int level = 0;
			Date date = new Date();
			Team team = null;
			List<Pokemon> pokemons = new ArrayList<Pokemon>();
			Gym gym = new Gym(description, level, date, team, pokemons);
		}
	};

	/**
	 * Get selected gym level value
	 * 
	 * @return
	 */
	private int getGymLevelSelected() {
		Object o = gymLevels.getSelectedItem();
		if (o == null) { return 0; }
		return Integer.valueOf(o.toString().trim());
	}

	/**
	 * @return selected team
	 */
	private String getSelectedTeam() {
		int teamId = teamsRadioGroup.getCheckedRadioButtonId();
		switch (teamId) {
			case R.id.radio_valor :
				return getString(R.string.valor);
			case R.id.radio_mystic :
				return getString(R.string.mystic);
			case R.id.radio_instinct :
				return getString(R.string.instinct);
			default :
				return getString(R.string.none);
		}
	}
}
