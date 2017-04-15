package com.pokemongostats.controller.filters;

import android.widget.Filter;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Zapagon on 05/03/2017.
 */
public abstract class TrainerFilter extends Filter {

    public void updateFrom(final CharSequence cs) {
    }

    protected boolean isNameOk(final String nameFromItem) {
        return true;
    }
}
