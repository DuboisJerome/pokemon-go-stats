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
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveRowView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class MoveAdapter extends ItemAdapter<Move> {

    private final Filter moveFilter = new MoveFilter() {

        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            // if no text in filter
            if (charSequence == null) {
                results.values = mFullList;
                results.count = mFullList.size();
                // return original values
            } else {
                this.updateFrom(charSequence);
                ArrayList<Move> suggestions = new ArrayList<>();
                // iterate over original values
                for (Move item : mFullList) {
                    if (!isNameOk(item.getName())) {
                        continue;
                    }
                    if (!isTypeOk(item.getType())) {
                        continue;
                    }
                    suggestions.add(item);
                }
                results.values = suggestions;
                results.count = suggestions.size();
                // return filtered values
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mFilteredList.clear();
            if (results.count > 0) {
                mFilteredList.addAll((List<Move>) results.values);
            }
            notifyDataSetChanged();
        }
    };
    private boolean isPPSVisible;
    private boolean isPowerVisible;
    private boolean isSpeedVisible;
    private SelectedVisitor<Move> mCallbackMove;
    // pokemon who own those moves
    private PkmnDesc owner;

    public MoveAdapter(Context context) {
        super(context);
    }

    public void setOwner(PkmnDesc owner) {
        this.owner = owner;
    }

    /**
     * @return the isPPSVisible
     */
    public boolean isPPSVisible() {
        return isPPSVisible;
    }

    /**
     * @param isPPSVisible the isPPSVisible to set
     */
    public void setPPSVisible(boolean isPPSVisible) {
        this.isPPSVisible = isPPSVisible;
    }

    /**
     * @return the isPowerVisible
     */
    public boolean isPowerVisible() {
        return isPowerVisible;
    }

    /**
     * @param isPowerVisible the isPowerVisible to set
     */
    public void setPowerVisible(boolean isPowerVisible) {
        this.isPowerVisible = isPowerVisible;
    }

    /**
     * @return the isSpeedVisible
     */
    public boolean isSpeedVisible() {
        return isSpeedVisible;
    }

    /**
     * @param isSpeedVisible the isSpeedVisible to set
     */
    public void setSpeedVisible(boolean isSpeedVisible) {
        this.isSpeedVisible = isSpeedVisible;
    }

    @Override
    protected View createViewAtPosition(int position, View v, ViewGroup parent) {
        final Move move = getItem(position);
        if (move == null) {
            return v;
        }

        final MoveRowView view;
        if (v == null || !(v instanceof MoveRowView)) {
            view = new MoveRowView(getContext());
            view.setPkmnMove(owner, move);
            view.update();
        } else {
            view = (MoveRowView) v;
            if (!move.equals(view.getMove())) {
                view.setPkmnMove(owner, move);
                view.update();
            }
        }
        view.setBackgroundResource(R.drawable.selector_row_item);

        view.setPPSVisible(isPPSVisible);
        view.setPowerVisible(isPowerVisible);
        view.setSpeedVisible(isSpeedVisible);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilter() {
        return moveFilter;
    }
}