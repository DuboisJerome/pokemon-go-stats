package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.controller.filters.TrainerFilter;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.view.rows.PkmnDescRow;
import com.pokemongostats.view.rows.TrainerRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class TrainerAdapter extends ItemAdapter<Trainer> {

    private final Filter trainerFilter = new TrainerFilter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            // if no text in filter
            if (charSequence == null) {
                results.values = mFullList;
                results.count = mFullList.size();
                // return original values
            } else {
                this.updateFrom(charSequence);
                ArrayList<Trainer> suggestions = new ArrayList<>();
                // iterate over original values
                for (Trainer item : mFullList) {
                    if (!isNameOk(item.getName())) {
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
                mFilteredList.addAll((List<Trainer>) results.values);
            }
            notifyDataSetChanged();
        }
    };

    public TrainerAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createViewAtPosition(int position, View v,
                                        ViewGroup parent) {
        final Trainer t = getItem(position);
        if (t == null) {
            return v;
        }

        final TrainerRow view;
        if (v == null || !(v instanceof PkmnDescRow)) {
            view = new TrainerRow(getContext());
            view.updateWith(t);
        } else {
            view = (TrainerRow) v;
            if (!t.equals(view.getTrainer())) {
                view.updateWith(t);
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
        return trainerFilter;
    }
}