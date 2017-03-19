package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;

/**
 * Created by Zapagon on 05/03/2017.
 */

public class FilterPokemonView extends LinearLayoutCompat {
    private AppCompatAutoCompleteTextView name;
    private TypeRowView type1, type2;

    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    private final OnClickListener onClickType1 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                if(type1 != null){
                    type1.updateWith(t);
                }
            }
        };

        @Override
        public void onClick(View v) {
            FragmentActivity currentActivity = ((FragmentActivity) getContext());
            if (currentActivity != null) {
                chooseTypeDialog.setVisitor(visitor);
                chooseTypeDialog.setCurrentType(type1 == null ? null : type1.getType());
                chooseTypeDialog.show(currentActivity.getSupportFragmentManager(), "chooseTypeDialog");
            }
        }
    };
    private final OnClickListener onClickType2 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                if(type2 != null){
                    type2.updateWith(t);
                }
            }
        };

        @Override
        public void onClick(View v) {
            FragmentActivity currentActivity = ((FragmentActivity) getContext());
            if (currentActivity != null) {
                chooseTypeDialog.setVisitor(visitor);
                chooseTypeDialog.setCurrentType(type2 == null ? null : type2.getType());
                chooseTypeDialog.show(currentActivity.getSupportFragmentManager(), "chooseTypeDialog");
            }
        }
    };

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
        type1.setOnClickListener(onClickType1);

        type2 = (TypeRowView) view.findViewById(R.id.value_type_2) ;
        type2.setShowEvenIfEmpty(true);
        type2.update();
        type2.setOnClickListener(onClickType2);
    }

    public PokemonDescFilterInfo getFilterInfos() {
        PokemonDescFilterInfo infos = new PokemonDescFilterInfo();
        infos.setType1(type1.getType());
        infos.setType2(type2.getType());
        infos.setName(name.getText().toString());
        return infos;
    }

}
