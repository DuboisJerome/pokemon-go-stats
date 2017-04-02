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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.db.gym.GymDescTableDAO;
import com.pokemongostats.model.bean.GymDesc;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.table.GymDescTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class AddGymActivity extends DefaultAppCompatActivity {

    /**
     * Spinner displaying gym desc
     */
    private Spinner gymDescSpinner;

    /**
     * Array adapter for gym desc
     */
    private ArrayAdapter<GymDesc> gymDescAdapter;

    /**
     * Spinner displaying gym levels
     */
    private Spinner gymLevels;

    /**
     * Team choice
     */
    private RadioGroup teamsRadioGroup;

    /**
     * List view representing the list of pokemons in gym
     */
    private ListView gymPokemonsListView;

    /**
     * Array adapter for gym desc
     */
    private ArrayAdapter<Pkmn> gymPokemonsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_gym);

        // gym desc
        gymDescSpinner = (Spinner) findViewById(R.id.gymDescriptions);
        gymDescAdapter = new ArrayAdapter<>(AddGymActivity.this,
                android.R.layout.simple_spinner_item);
        gymDescAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        gymDescSpinner.setAdapter(gymDescAdapter);
        updateGymDescSpinner();

        // level
        gymLevels = (Spinner) findViewById(R.id.gymLevels);
        gymLevels.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item,
                new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));

        // team
        teamsRadioGroup = (RadioGroup) findViewById(R.id.teamsRadioGroup);

        // List of pokemons

        gymPokemonsListView = (ListView) findViewById(R.id.gymPokemons);
        gymPokemonsAdapter = new ArrayAdapter<>(AddGymActivity.this,
                android.R.layout.simple_spinner_item);
        gymPokemonsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        gymPokemonsListView.setAdapter(gymPokemonsAdapter);

        // Validate activity
    }

    /**
     * A call-back call when the user click "+" next to gyms names
     */
    public void onClickAddGymDesc(final View v) {
        // show add gym description dialog
//		new AddGymDescDialog() {
//
//			@Override
//			public void onGymDescAdded(final GymDesc gymDescAdded) {
//				if (gymDescAdded != null) {
//					gymDescAdapter.add(gymDescAdded);
//					// TODO set gymDescAdded selected
//				}
//			}
//		}.show(getSupportFragmentManager(),
//				AddGymDescDialog.class.getName());
    }

    /**
     * A call-back call when the user click "+" next to pokemons list
     */
    public void onClickAddGymPokemon(final View v) {
        Intent intent = new Intent(this, AddPkmnToGymActivity.class);
        startActivity(intent);
    }

    /**
     * A call-back call when the user click "x" next to pokemons list
     */
    public void onClickRemoveGymPokemon(final View v) {
    }

    /**
     * A call-back call when the user click validate
     */
    public void onClickValidateGym(final View v) {
        Toast.makeText(getApplicationContext(),
                "Gym level selected : " + getGymLevelSelected()
                        + " with team : " + getSelectedTeam(),
                Toast.LENGTH_LONG).show();

        // retrieve gym description from dropdown list
        GymDesc description = new GymDesc();
        String name = null;
        int level = 0;
        Date date = new Date();
        Team team = null;
        List<Pkmn> pokemons = new ArrayList<Pkmn>();
    }

    /**************************************************************************/

    /**
     * Show select trainer dialog
     */
    private void showSelectTrainerDialog() {

        //
        // Intent intent = new Intent(AddGymActivity.this, new
        // SelectTrainerActivity() {
        // @Override
        // public void onTrainerSelected(Trainer selectedTrainer) {
        // showSelectPokemonTrainerDialog(selectedTrainer);
        // }
        //
        // }.getClass());
        // startActivity(intent);
    }

    /**
     * Show select pokemon trainer dialog
     *
     * @param selectedTrainer
     */
    private void showSelectPokemonTrainerDialog(Trainer selectedTrainer) {
        // new SelectPokemonTrainerFragment() {
        // @Override
        // public void onPokemonSelected(final Pokemon selectedPokemon) {
        // gymPokemonsAdapter.add(selectedPokemon);
        // }
        // }.show(getFragmentManager(),
        // SelectPokemonTrainerFragment.class.getName());
    }

    /**
     * TODO
     */
    private void updateGymDescSpinner() {

        // retrieve values asynchronously
        new GetAllAsyncTask<GymDesc>() {

            @Override
            protected List<GymDesc> doInBackground(Long... params) {
                final GymDescTableDAO dao = new GymDescTableDAO(
                        getApplicationContext());
                if (params == null || params.length <= 0) {
                    return dao.selectAll();
                } else {
                    return dao.selectAllIn(GymDescTable.ID, false,
                            params);
                }
            }

            @Override
            public void onPostExecute(List<GymDesc> list) {
                // on result value update Spinner
                gymDescAdapter.clear();
                gymDescAdapter.addAll(list);
            }

        }.execute();
    }

    /**
     * Get selected gym level value
     *
     * @return id gym selected
     */
    private int getGymLevelSelected() {
        Object o = gymLevels.getSelectedItem();
        if (o == null) {
            return 0;
        }
        return Integer.valueOf(o.toString().trim());
    }

    /**
     * @return selected team
     */
    private String getSelectedTeam() {
        int teamId = teamsRadioGroup.getCheckedRadioButtonId();
        switch (teamId) {
            case R.id.radio_valor:
                return getString(R.string.valor);
            case R.id.radio_mystic:
                return getString(R.string.mystic);
            case R.id.radio_instinct:
                return getString(R.string.instinct);
            default:
                return getString(R.string.none);
        }
    }
}
