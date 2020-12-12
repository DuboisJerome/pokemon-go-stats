package com.pokemongostats.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.model.parcalables.PclbMoveFilterInfo;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.commons.FilterMoveView;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveHeader;
import com.pokemongostats.view.utils.ComparatorUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Zapagon
 */
public class MoveListFragment
        extends
        HistorizedFragment<MoveListFragment.SortChoice>
        implements
        HasMoveSelectable, Observer {

    private static final String MOVE_LIST_ITEM_KEY = "MOVE_LIST_ITEM_KEY";
    private static final String MOVE_LIST_FILTER_KEY = "MOVE_LIST_FILTER_KEY";
    private FilterMoveView filterMoveView;
    private MoveHeader chargeMovesHeader;
    private MoveHeader quickMovesHeader;
    // controler
    private ArrayAdapter<SortChoice> adapterSortChoice;
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
    private MoveAdapter adapterChargeMoves;
    private MoveAdapter adapterQuickMoves;
    private SelectedVisitor<Move> mCallbackMove;
    // model
    private MoveFilterInfo moveFilterInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapterSortChoice = new ArrayAdapter<SortChoice>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item, SortChoice.values()) {

            /**
             * {@inheritDoc}
             */
            @NonNull
            @Override
            public View getView(int position, View convertView,
                                @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return initText(position, v);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                return initText(position, v);
            }

            private View initText(int position, View v) {
                try {
                    TextView text = (TextView) v;
                    SortChoice sortChoice = getItem(position);
                    if (sortChoice != null) {
                        text.setText(getString(sortChoice.idLabel));
                    }
                } catch (Exception e) {
                    Log.e(TagUtils.DEBUG,
                            "Error spinner sort choice", e);
                }
                return v;
            }
        };
        adapterSortChoice.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        PkmnGoStatsApplication app = (PkmnGoStatsApplication) Objects.requireNonNull(getActivity())
                .getApplicationContext();
        adapterChargeMoves = new MoveAdapter(getActivity());
        adapterQuickMoves = new MoveAdapter(getActivity());

        List<Move> list = new PokedexDAO(getActivity()).getListMove();
        for (Move m : list) {
            if (Move.MoveType.CHARGE.equals(m.getMoveType())) {
                adapterChargeMoves.add(m);
            } else if (Move.MoveType.QUICK.equals(m.getMoveType())) {
                adapterQuickMoves.add(m);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_move_list, container,
                false);

        // view
        Spinner spinnerSortChoice = (Spinner) currentView
                .findViewById(R.id.list_sort_choice);
        spinnerSortChoice.setAdapter(adapterSortChoice);
        spinnerSortChoice.setSelection(0, false);
        spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

        filterMoveView = (FilterMoveView) currentView.findViewById(R.id.filter_move_view);
        filterMoveView.registerObserver(this);

        AdapterView.OnItemClickListener onMoveClicked = (adapterView, view, i, l) -> {
            if (mCallbackMove == null) {
                return;
            }
            mCallbackMove.select((Move) adapterView.getItemAtPosition(i));
        };

        TextView emptyViewCharge = (TextView) currentView.findViewById(R.id.empty_charge_list_view);
        chargeMovesHeader = (MoveHeader) currentView.findViewById(R.id.movelist_chargemoves_header);
        ListView listViewChargeMoves = (ListView) currentView
                .findViewById(R.id.list_chargemove_found);
        listViewChargeMoves.setAdapter(adapterChargeMoves);
        listViewChargeMoves.setOnItemClickListener(onMoveClicked);
        listViewChargeMoves.setEmptyView(emptyViewCharge);


        TextView emptyViewQuick = (TextView) currentView.findViewById(R.id.empty_quick_list_view);
        quickMovesHeader = (MoveHeader) currentView.findViewById(R.id.movelist_quickmoves_header);
        ListView listViewQuickMoves = (ListView) currentView
                .findViewById(R.id.list_quickmove_found);
        listViewQuickMoves.setAdapter(adapterQuickMoves);
        listViewQuickMoves.setOnItemClickListener(onMoveClicked);
        listViewQuickMoves.setEmptyView(emptyViewQuick);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (currentItem == null) {
            if (savedInstanceState != null) {
                String saved = savedInstanceState.getString(MOVE_LIST_ITEM_KEY);
                currentItem = SortChoice.valueOf(saved);
            }
            if (currentItem == null) {
                currentItem = SortChoice.COMPARE_BY_PPS;
            }
            updateViewImpl();
        }
        if (moveFilterInfo == null && savedInstanceState != null) {
            moveFilterInfo = savedInstanceState.getParcelable(MOVE_LIST_FILTER_KEY);
        }
        filterMoveView.updateWith(moveFilterInfo);
        filter();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putString(MOVE_LIST_ITEM_KEY, currentItem.name());
        }
        if (moveFilterInfo != null) {
            outState.putParcelable(MOVE_LIST_FILTER_KEY, new PclbMoveFilterInfo(moveFilterInfo));
        }
    }

    @Override
    protected void updateViewImpl() {
        if (currentItem == null) {
            currentItem = SortChoice.COMPARE_BY_NAME;
        }
        final SortChoice sortChoice = currentItem;
        boolean isPowerVisible = false;
        boolean isEnergyVisible = false;
        boolean isPowerPerSecondVisible = false;
        boolean isDurationVisible = false;
        boolean isDPTVisible = false;
        boolean isEPTVisible = false;
        boolean isDPTxEPTVisible = false;
        final Comparator<Move> c;
        switch (sortChoice) {
            case COMPARE_BY_NAME:
                c = ComparatorUtils.createComparatorNullCheck(Move::getName);
                break;
            case COMPARE_BY_POWER:
                c = ComparatorUtils.createComparatorNullCheck(Move::getPower).reversed();
                isPowerVisible = true;
                break;
            case COMPARE_BY_ENERGY:
                c = ComparatorUtils.createComparatorNullCheck(Move::getEnergyDelta).reversed();
                isEnergyVisible = true;
                break;
            case COMPARE_BY_PPS:
                c = MoveComparators.getComparatorByPps().reversed();
                isPowerPerSecondVisible = true;
                break;
            case COMPARE_BY_DURATION:
                c = ComparatorUtils.createComparatorNullCheck(Move::getDuration);
                isDurationVisible = true;
                break;
            case COMPARE_BY_DPT:
                c = ComparatorUtils.createComparatorNullCheck(FightUtils::computePowerPerTurn).reversed();
                isDPTVisible = true;
                break;
            case COMPARE_BY_EPT:
                c = ComparatorUtils.createComparatorNullCheck(FightUtils::computeEnergyPerTurn).reversed();
                isEPTVisible = true;
                break;
            case COMPARE_BY_DPTxEPT:
                c = ComparatorUtils.createComparatorNullCheck(FightUtils::computePowerEnergyPerTurn).reversed();
                isDPTxEPTVisible = true;
                break;
            default:
                Log.e(TagUtils.DEBUG,
                        "SortChoice not found : " + sortChoice);
                c = ComparatorUtils.createComparatorNullCheck(Move::getName);
                break;
        }
        chargeMovesHeader.setPowerVisible(isPowerVisible);
        chargeMovesHeader.setEnergyVisible(isEnergyVisible);
        chargeMovesHeader.setPowerPerSecondVisible(isPowerPerSecondVisible);
        chargeMovesHeader.setDurationVisible(isDurationVisible);
        chargeMovesHeader.setDPTVisible(isDPTVisible);
        chargeMovesHeader.setEPTVisible(isEPTVisible);
        chargeMovesHeader.setDPTxEPTVisible(isDPTxEPTVisible);

        adapterChargeMoves.setPowerVisible(isPowerVisible);
        adapterChargeMoves.setEnergyVisible(isEnergyVisible);
        adapterChargeMoves.setPowerPerSecondVisible(isPowerPerSecondVisible);
        adapterChargeMoves.setDurationVisible(isDurationVisible);
        adapterChargeMoves.setDPTVisible(isDPTVisible);
        adapterChargeMoves.setEPTVisible(isEPTVisible);
        adapterChargeMoves.setDPTxEPTVisible(isDPTxEPTVisible);
        adapterChargeMoves.sort(c); // include notify data set changed

        quickMovesHeader.setPowerVisible(isPowerVisible);
        quickMovesHeader.setEnergyVisible(isEnergyVisible);
        quickMovesHeader.setPowerPerSecondVisible(isPowerPerSecondVisible);
        quickMovesHeader.setDurationVisible(isDurationVisible);
        quickMovesHeader.setDPTVisible(isDPTVisible);
        quickMovesHeader.setEPTVisible(isEPTVisible);
        quickMovesHeader.setDPTxEPTVisible(isDPTxEPTVisible);

        adapterQuickMoves.setPowerVisible(isPowerVisible);
        adapterQuickMoves.setEnergyVisible(isEnergyVisible);
        adapterQuickMoves.setPowerPerSecondVisible(isPowerPerSecondVisible);
        adapterQuickMoves.setDurationVisible(isDurationVisible);
        adapterQuickMoves.setDPTVisible(isDPTVisible);
        adapterQuickMoves.setEPTVisible(isEPTVisible);
        adapterQuickMoves.setDPTxEPTVisible(isDPTxEPTVisible);
        adapterQuickMoves.sort(c); // include notify data set changed
    }

    @Override
    public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
        this.mCallbackMove = visitor;
    }

    @Override
    public void update(Observable o) {
        if (o == null) {
            return;
        }
        if (o.equals(filterMoveView)) {
            moveFilterInfo = filterMoveView.getFilterInfos();
            filter();
        }
    }

    private void filter() {
        if (moveFilterInfo != null) {
            final Filter.FilterListener filterListener = i -> {
                // TODO hide waiting popup
            };
            // TODO show waiting popup
            adapterChargeMoves.getFilter().filter(moveFilterInfo.toStringFilter(), filterListener);
            adapterQuickMoves.getFilter().filter(moveFilterInfo.toStringFilter(), filterListener);
        }
    }

    public enum SortChoice {
        COMPARE_BY_PPS(R.string.sort_by_pps),
        //
        COMPARE_BY_POWER(R.string.sort_by_power),
        //
        COMPARE_BY_DURATION(R.string.sort_by_duration),
        //
        COMPARE_BY_NAME(R.string.sort_by_name),
        //
        COMPARE_BY_ENERGY(R.string.sort_by_energy),
        //
        COMPARE_BY_DPT(R.string.sort_by_dpt),
        //
        COMPARE_BY_EPT(R.string.sort_by_ept),
        //
        COMPARE_BY_DPTxEPT(R.string.sort_by_dpt_x_ept);
        private final int idLabel;

        SortChoice(final int idLabel) {
            this.idLabel = idLabel;
        }
    }
}
