package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.filters.PokemonDescFilter;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.rows.PkmnDescRow;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zapagon
 */
public class PkmnDescAdapter extends ItemAdapter<PkmnDesc> {

    private final Filter pkmnFilter = new PokemonDescFilter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();

            PokedexDAO dao = new PokedexDAO(getContext());
            // General preferences
            boolean isLastEvolOnly = PreferencesUtils.getInstance().isLastEvolutionOnly(getContext());
            boolean isWithMega = PreferencesUtils.getInstance().isWithMega(getContext());
            boolean isWithLegendary = PreferencesUtils.getInstance().isWithLegendary(getContext());

            List<PkmnDesc> resultList = mFullList.stream().filter(p -> {
                boolean isOk = true;
                // if evol && only mega (mega = id base == id evol)
                isOk = !isLastEvolOnly || dao.getListEvolutionFor(p).stream().allMatch(e -> e.getBasePkmnId() == e.getEvolutionId());
                if(isOk){
                    isOk = isWithMega || !p.getForm().contains("MEGA");
                }
                if(isOk) {
                    isOk = isWithLegendary || !p.isLegendary();
                }
                return isOk;
            }).collect(Collectors.toList());

            // Specific preferences

            // if no text in filter
            if (charSequence == null || charSequence.length() == 0) {
                results.values = resultList;
                results.count = resultList.size();
                // return original values
            } else {
                this.updateFrom(charSequence);
                List<PkmnDesc> suggestions = new ArrayList<>();
                // iterate over original values
                for (PkmnDesc item : resultList) {
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
            if (results != null && results.count > 0) {
                mFilteredList.clear();
                mFilteredList.addAll((List<PkmnDesc>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
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
            if(v == null){
                v = new PkmnDescRow(getContext());
            }
            return v;
        }

        final PkmnDescRow view;
        if (!(v instanceof PkmnDescRow)) {
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

    public void filter(){
        filter("");
    }

    public void filter(CharSequence constraint){
        getFilter().filter(constraint);
    }
}