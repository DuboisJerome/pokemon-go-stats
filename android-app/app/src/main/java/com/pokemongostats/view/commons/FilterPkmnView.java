package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.dialog.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.ItemView;
import com.pokemongostats.view.rows.TypeRow;

/**
 * Created by Zapagon on 05/03/2017.
 * FilterPokemonView
 */
public class FilterPkmnView extends LinearLayout implements Observable, ItemView<PkmnDescFilterInfo> {

    private final Observable observableImpl = new ObservableImpl(this);
    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    private final OnClickListener onClickType2 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                type2View.updateWith(t);
                infos.setType2(t);
                notifyObservers();
            }
        };

        @Override
        public void onClick(View v) {
            FragmentActivity currentActivity = ((FragmentActivity) getContext());
            if (currentActivity != null) {
                chooseTypeDialog.setVisitor(visitor);
                chooseTypeDialog.setCurrentType(type2View == null ? null : type2View.getType());
                chooseTypeDialog.show(currentActivity.getSupportFragmentManager(), "chooseTypeDialog");
            }
        }
    };
    private TypeRow type1View, type2View;
    private final OnClickListener onClickType1 = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                type1View.updateWith(t);
                infos.setType1(t);
                notifyObservers();
            }
        };

        @Override
        public void onClick(View v) {
            FragmentActivity currentActivity = ((FragmentActivity) getContext());
            if (currentActivity != null) {
                chooseTypeDialog.setVisitor(visitor);
                chooseTypeDialog.setCurrentType(type1View == null ? null : type1View.getType());
                chooseTypeDialog.show(currentActivity.getSupportFragmentManager(), "chooseTypeDialog");
            }
        }
    };
    private PkmnDescFilterInfo infos = new PkmnDescFilterInfo();

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

        type1View = (TypeRow) view.findViewById(R.id.value_type_1);
        type1View.setShowEvenIfEmpty(true);
        type1View.update();
        type1View.setOnClickListener(onClickType1);

        type2View = (TypeRow) view.findViewById(R.id.value_type_2);
        type2View.setShowEvenIfEmpty(true);
        type2View.update();
        type2View.setOnClickListener(onClickType2);
    }

    public PkmnDescFilterInfo getFilterInfos() {
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

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void update() {
        if(infos != null){
            type1View.updateWith(infos.getType1());
            type2View.updateWith(infos.getType2());
        }
    }

    @Override
    public void updateWith(PkmnDescFilterInfo infos) {
        if(infos != null){
            this.infos.setName(infos.getName());
            this.infos.setType1(infos.getType1());
            this.infos.setType2(infos.getType2());
        }
        update();
    }
}
