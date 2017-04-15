package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.controller.filters.PokemonDescFilter;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.rows.PkmnDescRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class PkmnDescAdapter extends ItemAdapter<PkmnDesc> {

    private final Filter pkmnFilter = new PokemonDescFilter() {

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
                ArrayList<PkmnDesc> suggestions = new ArrayList<>();
                // iterate over original values
                for (PkmnDesc item : mFullList) {
                    if (!isNameOk(item.getName())) {
                        continue;
                    }
                    if (!isTypesOk(item.getType1(), item.getType2())) {
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
                mFilteredList.addAll((List<PkmnDesc>) results.values);
            }
            notifyDataSetChanged();
        }
    };

    public PkmnDescAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createViewAtPosition(int position, View v,
                                        ViewGroup parent) {
        final PkmnDesc p = getItem(position);
        if (p == null) {
            return v;
        }

        final PkmnDescRow view;
        if (v == null || !(v instanceof PkmnDescRow)) {
            view = new PkmnDescRow(getContext());
            view.updateWith(p);
        } else {
            view = (PkmnDescRow) v;
            if (!p.equals(view.getPkmnDesc())) {
                view.updateWith(p);
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
        return pkmnFilter;
    }
}