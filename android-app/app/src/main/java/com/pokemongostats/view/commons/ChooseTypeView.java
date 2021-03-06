package com.pokemongostats.view.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.listeners.SelectedVisitor;

/**
 * @author Zapagon
 */
public class ChooseTypeView extends RelativeLayout {

    private SelectedVisitor<Type> mCallbackType;

    private Type currentType;

    public ChooseTypeView(Context context) {
        super(context);
        initializeViews(null);
    }

    public ChooseTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public ChooseTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(final AttributeSet attrs) {

        inflate(getContext(), R.layout.view_choose_type, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        final ChooseTypeAdapter adapter = new ChooseTypeAdapter(getContext(),
                Type.values());
        GridView gv = new GridView(getContext());
        gv.setNumColumns(3);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener((parent, view, position, id) -> {
            final Type selectedType = adapter.getItem(position);
            final boolean isAlreadySelected = selectedType == currentType;
            if (position != AdapterView.INVALID_POSITION &&
                    !isAlreadySelected && mCallbackType != null) {
                mCallbackType.select(selectedType);
            }
        });

        this.addView(gv);
    }

    public void setCallbackType(SelectedVisitor<Type> mCallbackType) {
        this.mCallbackType = mCallbackType;
    }

    public Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }

    private class ChooseTypeAdapter extends TypeAdapter {

        ChooseTypeAdapter(Context context,
                          Type[] list) {
            super(context, android.R.layout.simple_spinner_item, list);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NonNull
        public View getView(final int position, final View v,
                            @NonNull final ViewGroup parent) {
            View view = super.getView(position, v, parent);
            view.setPadding(20, 20, 20, 20);
            final boolean isSelected = getItem(position) == currentType;
            view.setEnabled(!isSelected);
            return view;
        }
    }

}
