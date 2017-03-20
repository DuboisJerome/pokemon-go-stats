package com.pokemongostats.view.commons;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;
import com.pokemongostats.view.dialogs.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRowView;

/**
 * Created by Zapagon on 05/03/2017.
 */
public class FilterMoveView extends LinearLayoutCompat {
    private AppCompatAutoCompleteTextView name;
    private TypeRowView type;

    private final ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment();
    private final OnClickListener onClickType = new OnClickListener() {
        final SelectedVisitor<Type> visitor = new SelectedVisitor<Type>() {
            @Override
            public void select(Type t) {
                // hide dialog
                chooseTypeDialog.dismiss();
                // load view with type
                if(type != null){
                    type.updateWith(t);
                }
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

        name = (AppCompatAutoCompleteTextView) view.findViewById(R.id.value_name);

        type = (TypeRowView) view.findViewById(R.id.value_type) ;
        type.setShowEvenIfEmpty(true);
        type.update();
        type.setOnClickListener(onClickType);
    }

    public MoveFilterInfo getFilterInfos() {
        MoveFilterInfo infos = new MoveFilterInfo();
        infos.setType(type.getType());
        infos.setName(name.getText().toString());
        return infos;
    }

}
