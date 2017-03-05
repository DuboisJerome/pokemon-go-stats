package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;
import com.pokemongostats.view.rows.TypeRowView;

/**
 * Created by Zapagon on 05/03/2017.
 */

public class FilterPokemonView extends LinearLayoutCompat {
    private AppCompatAutoCompleteTextView name;
    private TypeRowView type1, type2;

    public FilterPokemonView(Context context) {
        super(context);
        initializeViews(null);
    }

    public FilterPokemonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public FilterPokemonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(final AttributeSet attrs) {
        if (attrs != null) {
        }

        View view = inflate(getContext(), R.layout.view_filter_pokemon, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        name = (AppCompatAutoCompleteTextView) view.findViewById(R.id.value_name);

        type1 = (TypeRowView) view.findViewById(R.id.value_type_1) ;
        type1.setShowEvenIfEmpty(true);
        type1.update();

        type2 = (TypeRowView) view.findViewById(R.id.value_type_2) ;
        type2.setShowEvenIfEmpty(true);
        type2.update();
    }

    public PokemonDescFilterInfo getFilterInfos() {
        PokemonDescFilterInfo infos = new PokemonDescFilterInfo();
        infos.setType1(type1.getType());
        infos.setType2(type2.getType());
        infos.setName(name.getText().toString());
        return infos;
    }

}
