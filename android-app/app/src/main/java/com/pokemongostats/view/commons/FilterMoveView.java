package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;

/**
 * Created by Zapagon on 05/03/2017.
 */
public class FilterMoveView extends LinearLayout implements Observable {

    private final Observable observableImpl = new ObservableImpl(this);
    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    private TypeRowView type;
    private final OnClickListener onClickType = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                type.updateWith(t);
                notifyObservers();
            }
        };

        @Override
        public void onClick(View v) {
            FragmentActivity currentActivity = ((FragmentActivity) getContext());
            if (currentActivity != null) {
                chooseTypeDialog.setVisitor(visitor);
                chooseTypeDialog.setCurrentType(type == null ? null : type.getType());
                chooseTypeDialog.show(currentActivity.getSupportFragmentManager(), "chooseTypeDialog");
            }
        }
    };

    public FilterMoveView(Context context) {
        super(context);
        initializeViews(null);
    }

    public FilterMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public FilterMoveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(final AttributeSet attrs) {
        if (attrs != null) {
        }

        View view = inflate(getContext(), R.layout.view_filter_move, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        type = (TypeRowView) view.findViewById(R.id.value_type);
        type.setShowEvenIfEmpty(true);
        type.update();
        type.setOnClickListener(onClickType);
    }

    public MoveFilterInfo getFilterInfos() {
        MoveFilterInfo infos = new MoveFilterInfo();
        infos.setType(type.getType());
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
