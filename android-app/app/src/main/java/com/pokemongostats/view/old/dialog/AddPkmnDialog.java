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

package com.pokemongostats.view.old.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.dialog.CustomDialogFragment;
import com.pokemongostats.view.utils.HasRequiredField;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public abstract class AddPkmnDialog extends CustomDialogFragment implements HasRequiredField {

    /**
     * Spinner displaying pokemons' names
     */
    private Spinner pokedex;

    /** */
    private EditText cpEditText;

    /**
     * Pokedex adapter
     */
    private ArrayAdapter<PkmnDesc> pokedexAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // dialog form
        final View form = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.fragment_select_pkmn, null);

        // instances
        pokedex = (Spinner) form.findViewById(R.id.pokedex);
        pokedexAdapter = new ArrayAdapter<PkmnDesc>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item);
        pokedexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pokedexAdapter.addAll(new PokedexDAO(getActivity()).getListPkmnDesc());

        pokedex.setAdapter(pokedexAdapter);

        cpEditText = (EditText) form.findViewById(R.id.cpEditText);

        // buttons listeners
        OnClickListener onClickAdd = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                int position = pokedex.getSelectedItemPosition();
                if (position != AdapterView.INVALID_POSITION) {
                    PkmnDesc pokemonDesc = pokedexAdapter.getItem(position);
                    // TODO ...
                    Pkmn p = new Pkmn();
                    // p.setPokedexNum(pokedexNum);
                    // p.setCP(cp);
                    // p.setHP(hp);
                    // p.setAttackIV(attackIV);
                    // p.setDefenseIV(defenseIV);
                    // p.setStaminaIV(staminaIV);
                    // p.setLevel(level);
                    // p.setNickname(nickname);
                    // p.setOwner(ownerID);

                    // insert into TODO

                    onPokemonAdded(p);
                } else {
                    // TODO message "you must select a pokemon in pokedex"
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
        builder.setTitle(R.string.add_pokemon_dialog_title);
        builder.setPositiveButton(R.string.select, onClickAdd);
        builder.setNegativeButton(android.R.string.cancel, onClickCancel);

        return builder.create();
    }

    /**
     * Override this method to make an action when async add action end
     */
    public abstract void onPokemonAdded(final Pkmn selectedPokemon);

    @Override
    public boolean checkAllField() {
        return true;
    }
}
