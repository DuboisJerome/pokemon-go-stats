package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.view.rows.MoveCombRow;

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
    protected View createViewAtPosition(int position, View v) {
        final MoveCombination moveComb = getItem(position);
        if (moveComb == null) {
            return v;
        }

        final MoveCombRow view;
        if (!(v instanceof MoveCombRow)) {
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