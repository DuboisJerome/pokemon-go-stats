package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.rows.TypeRow;

import java.util.List;

/**
 * @author Zapagon
 */
public class TypeAdapter extends ArrayAdapter<Type> {

    public TypeAdapter(Context context, int textViewResourceId, Type[] list) {
        super(context, textViewResourceId, list);
    }

    public TypeAdapter(Context context, int textViewResourceId,
                       List<Type> list) {
        super(context, textViewResourceId, list);
    }

    public TypeAdapter(Context applicationContext, int simpleSpinnerItem) {
        super(applicationContext, simpleSpinnerItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public View getView(int position, View v, @NonNull ViewGroup parent) {
        return getTextViewAtPosition(position, v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View v, @NonNull ViewGroup parent) {
        return getTextViewAtPosition(position, v);
    }

    private View getTextViewAtPosition(int position, View v) {
        Type type = getItem(position);
        if (type == null) {
            return v;
        }
        final TypeRow view;
        if (!(v instanceof TypeRow)) {
            view = new TypeRow(getContext());
            view.setType(type);
            view.update();
        } else {
            view = (TypeRow) v;
            if (!type.equals(view.getType())) {
                view.setType(type);
                view.update();
            }
        }
        return view;
    }
}