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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.pokemongostats.view.dialog.CustomDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.utils.HasRequiredField;

import java.util.List;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class EditTrainerDialog extends CustomDialogFragment
        implements
        HasRequiredField, Observable {

    private EditText trainerNameEditText;
    private EditText trainerLevelEditText;
    private RadioGroup trainerTeamRadioGroup;

    private Trainer mTrainer;
    private final Object mTrainerLock = new Object();

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
            validate();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // dialog form
        final View form = LayoutInflater
                .from(getActivity().getApplicationContext())
                .inflate(R.layout.dialog_add_trainer, null);

        final boolean isEditMode = mTrainer != null;

        // name
        trainerNameEditText = (EditText) form
                .findViewById(R.id.trainer_name);
        trainerNameEditText.addTextChangedListener(onEditTextChanged);
        // level
        trainerLevelEditText = (EditText) form
                .findViewById(R.id.trainer_lvl);
        trainerLevelEditText.addTextChangedListener(onEditTextChanged);

        // team
        trainerTeamRadioGroup = (RadioGroup) form
                .findViewById(R.id.trainer_team);
        trainerTeamRadioGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        validate();
                    }
                });

        if(isEditMode){
            trainerNameEditText.setText(mTrainer.getName());
            trainerLevelEditText.setText(String.valueOf(mTrainer.getLevel()));
            int radio = getTeamRadioId(mTrainer.getTeam());
            if(radio >= 0){
                trainerTeamRadioGroup.check(radio);
            }
        }

        // buttons listeners
        OnClickListener onClickEdit = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // name
                String name = trainerNameEditText.getText().toString();

                // level
                String levelStr = trainerLevelEditText.getText().toString();
                int level = 0;
                if (!levelStr.isEmpty()) {
                    level = Integer.parseInt(levelStr);
                }

                // team
                Team team = getSelectedTeam(
                        trainerTeamRadioGroup.getCheckedRadioButtonId());

                // create business object
                if(!isEditMode){
                    mTrainer = new Trainer();
                }
                mTrainer.setName(name);
                mTrainer.setLevel(level);
                mTrainer.setTeam(team);

                // call database async
                new InsertOrReplaceAsyncTask<Trainer>() {
                    private Context context;
                    public void onPreExecute(){
                        this.context = getActivity();
                    }

                    @Override
                    protected List<Trainer> doInBackground(Trainer... params) {
                        return new TrainerTableDAO(context).insertOrReplace(params);
                    }

                    @Override
                    public void onPostExecute(List<Trainer> result) {
                        if(result != null && result.size() > 0){
                            synchronized (mTrainerLock){
                                notifyObservers();
                            }
                        }
                    }
                }.execute(mTrainer);
            }
        };

        OnClickListener onClickCancel = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditTrainerDialog.this.getDialog().cancel();
            }
        };

        /// build add gym dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(form);
        builder.setNegativeButton(android.R.string.cancel, onClickCancel);
        if(isEditMode) {
            builder.setTitle(R.string.edit_trainer_dialog_title);
            builder.setPositiveButton(R.string.edit, onClickEdit);
        } else{
            builder.setTitle(R.string.add_trainer_dialog_title);
            builder.setPositiveButton(R.string.add, onClickEdit);
        }


        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveBtn.setEnabled(checkAllField());
            }
        });
        return dialog;
    }

    /**
     * @return selected team
     */
    private Team getSelectedTeam(int teamId) {
        switch (teamId) {
            case R.id.radio_valor:
                return Team.VALOR;
            case R.id.radio_mystic:
                return Team.MYSTIC;
            case R.id.radio_instinct:
                return Team.INSTINCT;
            default:
                return null;
        }
    }

    private int getTeamRadioId(Team team){
        if(team == null){
            return -1;
        }
        switch (team){
            case INSTINCT: return R.id.radio_instinct;
            case MYSTIC: return R.id.radio_mystic;
            case VALOR: return R.id.radio_valor;
            default: return -1;
        }
    }

    @Override
    public boolean checkAllField() {

        String name = trainerNameEditText.getText().toString();
        boolean nameOk = !name.isEmpty();

        String lvl = trainerLevelEditText.getText().toString();
        boolean levelOk = !lvl.isEmpty();

        boolean teamOk = getSelectedTeam(
                trainerTeamRadioGroup.getCheckedRadioButtonId()) != null;



        return nameOk && levelOk && teamOk;
    }

    @Override
    public void validate(){
        if(checkAllField()){
            AlertDialog d = (AlertDialog)getDialog();
            if(d != null){
                Button positiveBtn = d.getButton(AlertDialog.BUTTON_POSITIVE);
                if(positiveBtn != null){
                    positiveBtn.setEnabled(checkAllField());
                }
            }
        }
    }

    private final Observable observableImpl = new ObservableImpl(this);
    @Override
    public void registerObserver(Observer o) {
        observableImpl.registerObserver(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observableImpl.unregisterObserver(o);
    }

    @Override
    public void notifyObservers() {
        observableImpl.notifyObservers();
    }

    public Trainer getTrainer() {
        return mTrainer;
    }
    public void setTrainer(Trainer t) {
        this.mTrainer=t;
    }
}
