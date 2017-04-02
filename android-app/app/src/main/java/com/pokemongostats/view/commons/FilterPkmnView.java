package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;

/**
 * Created by Zapagon on 05/03/2017.
 * FilterPokemonView
 */
public class FilterPkmnView extends LinearLayout implements Observable {

    private final Observable observableImpl = new ObservableImpl(this);
    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    private final OnClickListener onClickType2 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                type2.updateWith(t);
                notifyObservers();
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
    private TypeRowView type1, type2;
    private final OnClickListener onClickType1 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                type1.updateWith(t);
                notifyObservers();
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

    public FilterPkmnView(Context context) {
        super(context);
        initializeViews(null);
    }

    public FilterPkmnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public FilterPkmnView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(final AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_filter_pokemon, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        type1 = (TypeRowView) view.findViewById(R.id.value_type_1);
        type1.setShowEvenIfEmpty(true);
        type1.update();
        type1.setOnClickListener(onClickType1);

        type2 = (TypeRowView) view.findViewById(R.id.value_type_2);
        type2.setShowEvenIfEmpty(true);
        type2.update();
        type2.setOnClickListener(onClickType2);
    }

    public PkmnDescFilterInfo getFilterInfos() {
        PkmnDescFilterInfo infos = new PkmnDescFilterInfo();
        infos.setType1(type1.getType());
        infos.setType2(type2.getType());
        return infos;
    }

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
}
