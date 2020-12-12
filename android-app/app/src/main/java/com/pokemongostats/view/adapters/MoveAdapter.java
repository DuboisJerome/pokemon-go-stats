package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.controller.filters.MoveFilter;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveRow;

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
    private boolean isPowerVisible = false;
    private boolean isEnergyVisible = false;
    private boolean isPowerPerSecondVisible = false;
    private boolean isDurationVisible = false;
    private boolean isDPTVisible = false;
    private boolean isEPTVisible = false;
    private boolean isDPTxEPTVisible = false;

    private SelectedVisitor<Move> mCallbackMove;
    // pokemon who own those moves
    private PkmnDesc owner;

    public MoveAdapter(Context context) {
        super(context);
    }

    public void setOwner(PkmnDesc owner) {
        this.owner = owner;
    }

    public boolean isPowerVisible() {
        return isPowerVisible;
    }

    public void setPowerVisible(boolean powerVisible) {
        isPowerVisible = powerVisible;
    }

    public boolean isEnergyVisible() {
        return isEnergyVisible;
    }

    public void setEnergyVisible(boolean energyVisible) {
        isEnergyVisible = energyVisible;
    }

    public boolean isPowerPerSecondVisible() {
        return isPowerPerSecondVisible;
    }

    public void setPowerPerSecondVisible(boolean powerPerSecondVisible) {
        isPowerPerSecondVisible = powerPerSecondVisible;
    }

    public boolean isDurationVisible() {
        return isDurationVisible;
    }

    public void setDurationVisible(boolean durationVisible) {
        isDurationVisible = durationVisible;
    }

    public boolean isDPTVisible() {
        return isDPTVisible;
    }

    public void setDPTVisible(boolean DPTVisible) {
        isDPTVisible = DPTVisible;
    }

    public boolean isEPTVisible() {
        return isEPTVisible;
    }

    public void setEPTVisible(boolean EPTVisible) {
        isEPTVisible = EPTVisible;
    }

    public boolean isDPTxEPTVisible() {
        return isDPTxEPTVisible;
    }

    public void setDPTxEPTVisible(boolean DPTxEPTVisible) {
        isDPTxEPTVisible = DPTxEPTVisible;
    }

    @Override
    protected View createViewAtPosition(int position, View v) {
        final Move move = getItem(position);
        if (move == null) {
            return v;
        }

        final MoveRow view;
        if (!(v instanceof MoveRow)) {
            view = new MoveRow(getContext());
            view.setPkmnMove(owner, move);
            view.update();
        } else {
            view = (MoveRow) v;
            if (!move.equals(view.getMove())) {
                view.setPkmnMove(owner, move);
                view.update();
            }
        }
        view.setBackgroundResource(R.drawable.selector_row_item);

        view.setPowerVisible(isPowerVisible);
        view.setEnergyVisible(isEnergyVisible);
        view.setPowerPerSecondVisible(isPowerPerSecondVisible);
        view.setDurationVisible(isDurationVisible);
        view.setDPTVisible(isDPTVisible);
        view.setEPTVisible(isEPTVisible);
        view.setDPTxEPTVisible(isDPTxEPTVisible);

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