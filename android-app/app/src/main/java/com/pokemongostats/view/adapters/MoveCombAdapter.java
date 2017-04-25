/**
 *
 */
package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.controller.filters.MoveFilter;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveCombRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class MoveCombAdapter extends ItemAdapter<MoveCombination> {

    private boolean isDefender;

    public MoveCombAdapter(Context context) {
        super(context);
    }

    public boolean isDefender() {
        return isDefender;
    }

    public void setDefender(boolean defender) {
        isDefender = defender;
    }

    @Override
    protected View createViewAtPosition(int position, View v, ViewGroup parent) {
        final MoveCombination moveComb = getItem(position);
        if (moveComb == null) {
            return v;
        }

        final MoveCombRow view;
        if (v == null || !(v instanceof MoveCombRow)) {
            view = new MoveCombRow(getContext());
            view.setMoveComb(moveComb);
            view.setDefender(isDefender);
            view.update();
        } else {
            view = (MoveCombRow) v;
            if (!moveComb.equals(view.getMoveComb())) {
                view.setMoveComb(moveComb);
                view.setDefender(isDefender);
                view.update();
            }
        }
        view.setBackgroundResource(R.drawable.selector_row_item);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilter() {
        return null;
    }
}