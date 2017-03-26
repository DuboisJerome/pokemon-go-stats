/**
 *
 */
package com.pokemongostats.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.dialogs.FilterPokemonDialogFragment;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.PkmnDescHeaderView;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public class PkmnListFragment
        extends
        HistorizedFragment<PkmnListFragment.SortChoice>
        implements
        HasPkmnDescSelectable {

    private static final String PKMN_LIST_FRAGMENT_KEY = "PKMN_LIST_FRAGMENT_KEY";

    public enum SortChoice {

        COMPARE_BY_ID(R.string.sort_by_id),
        //
        COMPARE_BY_NAME(R.string.sort_by_name),
        //
        COMPARE_BY_ATTACK(R.string.sort_by_base_attaque),
        //
        COMPARE_BY_DEFENSE(R.string.sort_by_base_defense),
        //
        COMPARE_BY_STAMINA(R.string.sort_by_base_stamina),
        //
        COMPARE_BY_MAX_CP(R.string.sort_by_max_cp);

        private int idLabel;

        SortChoice(final int idLabel) {
            this.idLabel = idLabel;
        }
    }

    private Spinner spinnerSortChoice;

    private ArrayAdapter<SortChoice> adapterSortChoice;

    private PkmnDescHeaderView pkmnDescHeader;
    private PkmnDescAdapter adapterPkmns;

    private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapterSortChoice = new ArrayAdapter<SortChoice>(getActivity(),
                android.R.layout.simple_spinner_item, SortChoice.values()) {

            /**
             * {@inheritDoc}
             */
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return initText(position, v);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                return initText(position, v);
            }

            private View initText(int position, View v) {
                try {
                    TextView text = (TextView) v;
                    SortChoice sortChoice = getItem(position);
                    text.setText(getString(sortChoice.idLabel));
                } catch (Exception e) {
                    Log.e(TagUtils.DEBUG,
                            "Error spinner sort choice", e);
                }
                return v;
            }
        };
        adapterSortChoice.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        PkmnGoStatsApplication app = (PkmnGoStatsApplication) getActivity()
                .getApplicationContext();

        adapterPkmns = new PkmnDescAdapter(getActivity());
        adapterPkmns.addAll(app.getPokedex(
                PreferencesUtils.isLastEvolutionOnly(getActivity())));
        adapterPkmns.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_pkmn_list, container,
                false);

        spinnerSortChoice = (Spinner) currentView
                .findViewById(R.id.list_sort_choice);
        spinnerSortChoice.setAdapter(adapterSortChoice);
        spinnerSortChoice.setSelection(0, false);
        spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

        ImageButton searchBtn = (ImageButton) currentView.findViewById(R.id.search_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Filter.FilterListener filterListener = new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        // TODO hide waiting popup
                    }
                };
                FilterPokemonDialogFragment filterDialog = new FilterPokemonDialogFragment();
                filterDialog.setOnFilterPokemon(new FilterPokemonDialogFragment.OnFilterPokemon() {
                    @Override
                    public void onFilter(final PkmnDescFilterInfo infos) {
                        if (infos == null) {
                            return;
                        }
                        // TODO show waiting popup
                        adapterPkmns.getFilter().filter(infos.toStringFilter(), filterListener);
                    }
                });
                filterDialog.show(getFragmentManager(), "FilterPokemon");
            }
        });

        ListView listViewPkmns = (ListView) currentView
                .findViewById(R.id.list_items_found);
        listViewPkmns.setAdapter(adapterPkmns);
        pkmnDescHeader = (PkmnDescHeaderView) currentView.findViewById(R.id.pkmn_list_pkmns_header);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (currentItem == null) {
            if (savedInstanceState != null) {
                String saved = savedInstanceState.getString(PKMN_LIST_FRAGMENT_KEY);
                currentItem = SortChoice.valueOf(saved);
                if (currentItem != null) {
                    spinnerSortChoice.setSelection(adapterSortChoice.getPosition(currentItem), false);
                }
            }
            updateViewImpl();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putString(PKMN_LIST_FRAGMENT_KEY, currentItem.name());
        }
    }

    @Override
    protected void updateViewImpl() {
        if (currentItem == null) {
            currentItem = SortChoice.COMPARE_BY_MAX_CP;
            spinnerSortChoice.setSelection(adapterSortChoice.getPosition(currentItem), false);
        }
        final SortChoice sortChoice = currentItem;
        boolean isBaseAttVisible = false;
        boolean isBaseDefVisible = false;
        boolean isBaseStaminaVisible = false;
        boolean isMaxCPVisible = false;
        final Comparator<PokemonDescription> c;
        switch (sortChoice) {
            case COMPARE_BY_ID:
                c = PkmnDescComparators.getComparatorById();
                break;
            case COMPARE_BY_NAME:
                c = PkmnDescComparators.getComparatorByName();
                break;
            case COMPARE_BY_ATTACK:
                c = PkmnDescComparators.getComparatorByBaseAttack();
                isBaseAttVisible = true;
                break;
            case COMPARE_BY_DEFENSE:
                c = PkmnDescComparators.getComparatorByBaseDefense();
                isBaseDefVisible = true;
                break;
            case COMPARE_BY_STAMINA:
                c = PkmnDescComparators.getComparatorByBaseStamina();
                isBaseStaminaVisible = true;
                break;
            case COMPARE_BY_MAX_CP:
                c = PkmnDescComparators.getComparatorByMaxCp();
                isMaxCPVisible = true;
                break;
            default:
                Log.e(TagUtils.DEBUG,
                        "SortChoice not found : " + sortChoice);
                c = null;
                break;
        }

        pkmnDescHeader.setBaseAttVisible(isBaseAttVisible);
        pkmnDescHeader.setBaseDefVisible(isBaseDefVisible);
        pkmnDescHeader.setBaseStaminaVisible(isBaseStaminaVisible);
        pkmnDescHeader.setMaxCPVisible(isMaxCPVisible);

        adapterPkmns.setBaseAttVisible(isBaseAttVisible);
        adapterPkmns.setBaseDefVisible(isBaseDefVisible);
        adapterPkmns.setBaseStaminaVisible(isBaseStaminaVisible);
        adapterPkmns.setMaxCPVisible(isMaxCPVisible);
        adapterPkmns.sort(c); // include notify data set changed
    }

    private final OnItemSelectedListener onItemSortSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (position != AdapterView.INVALID_POSITION) {
                showItem(adapterSortChoice.getItem(position));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    };

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            SelectedVisitor<PokemonDescription> visitor) {
        this.mCallbackPkmnDesc = visitor;
    }
}
