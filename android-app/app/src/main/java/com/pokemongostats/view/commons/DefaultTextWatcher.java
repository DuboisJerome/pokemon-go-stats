package com.pokemongostats.view.commons;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Zapagon on 01/04/2017.
 * Default text watch who do nothing on text change
 */
public class DefaultTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
