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
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.commons.FilterMoveView;
import com.pokemongostats.view.commons.FilterPkmnView;
import com.pokemongostats.view.dialogs.FilterMoveDialogFragment;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveHeaderView;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public class MoveListFragment
        extends
        HistorizedFragment<MoveListFragment.SortChoice>
        implements
        HasMoveSelectable, Observer {

    private static final String MOVE_LIST_FRAGMENT_KEY = "MOVE_LIST_FRAGMENT_KEY";
    private Spinner spinnerSortChoice;
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
    private MoveHeaderView chargeMovesHeader;
    private ListView listViewChargeMoves;
    private MoveAdapter adapterChargeMoves;

    private MoveHeaderView quickMovesHeader;
    private ListView listViewQuickMoves;
    private MoveAdapter adapterQuickMoves;

    private SelectedVisitor<Move> mCallbackMove;
    private FilterMoveView filterMoveView;

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
                    if(sortChoice != null){
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

        PkmnGoStatsApplication app = (PkmnGoStatsApplication) getActivity()
                .getApplicationContext();
        adapterChargeMoves = new MoveAdapter(getActivity());
        adapterQuickMoves = new MoveAdapter(getActivity());

        for (Move m : app.getMoves()) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_move_list, container,
                false);

        spinnerSortChoice = (Spinner) currentView
                .findViewById(R.id.list_sort_choice);
        spinnerSortChoice.setAdapter(adapterSortChoice);
        spinnerSortChoice.setSelection(0, false);
        spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

        filterMoveView = (FilterMoveView) currentView.findViewById(R.id.filter_move_view);
        filterMoveView.registerObserver(this);

        AdapterView.OnItemClickListener onMoveClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mCallbackMove == null){
                    return;
                }
                mCallbackMove.select((Move)adapterView.getItemAtPosition(i));
            }
        };

        TextView emptyViewCharge = (TextView) currentView.findViewById(R.id.empty_charge_list_view);
        chargeMovesHeader = (MoveHeaderView) currentView.findViewById(R.id.movelist_chargemoves_header);
        listViewChargeMoves = (ListView) currentView
                .findViewById(R.id.list_chargemove_found);
        listViewChargeMoves.setAdapter(adapterChargeMoves);
        listViewChargeMoves.setOnItemClickListener(onMoveClicked);
        listViewChargeMoves.setEmptyView(emptyViewCharge);


        TextView emptyViewQuick = (TextView) currentView.findViewById(R.id.empty_quick_list_view);
        quickMovesHeader = (MoveHeaderView) currentView.findViewById(R.id.movelist_quickmoves_header);
        listViewQuickMoves = (ListView) currentView
                .findViewById(R.id.list_quickmove_found);
        listViewQuickMoves.setAdapter(adapterQuickMoves);
        listViewChargeMoves.setOnItemClickListener(onMoveClicked);
        listViewQuickMoves.setEmptyView(emptyViewQuick);

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (currentItem == null) {
            if (savedInstanceState != null) {
                String saved = savedInstanceState.getString(MOVE_LIST_FRAGMENT_KEY);
                currentItem = SortChoice.valueOf(saved);
            }
            if (currentItem == null) {
                currentItem = SortChoice.COMPARE_BY_DPS;
            }
            updateViewImpl();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putString(MOVE_LIST_FRAGMENT_KEY, currentItem.name());
        }
    }

    @Override
    protected void updateViewImpl() {
        if (currentItem == null) {
            currentItem = SortChoice.COMPARE_BY_NAME;
        }
        final SortChoice sortChoice = currentItem;
        boolean isDPSVisible = false;
        boolean isPowerVisible = false;
        boolean isSpeedVisible = false;
        final Comparator<Move> c;
        switch (sortChoice) {
            case COMPARE_BY_DPS:
                c = MoveComparators.getComparatorByDps();
                isDPSVisible = true;
                break;
            case COMPARE_BY_POWER:
                c = MoveComparators.getComparatorByPower();
                isPowerVisible = true;
                break;
            case COMPARE_BY_DURATION:
                c = MoveComparators.getComparatorBySpeed();
                isSpeedVisible = true;
                break;
            case COMPARE_BY_NAME:
                c = MoveComparators.getComparatorByName();
                break;
            default:
                Log.e(TagUtils.DEBUG,
                        "SortChoice not found : " + sortChoice);
                c = null;
                break;
        }
        chargeMovesHeader.setDPSVisible(isDPSVisible);
        chargeMovesHeader.setPowerVisible(isPowerVisible);
        chargeMovesHeader.setSpeedVisible(isSpeedVisible);

        adapterChargeMoves.setDPSVisible(isDPSVisible);
        adapterChargeMoves.setPowerVisible(isPowerVisible);
        adapterChargeMoves.setSpeedVisible(isSpeedVisible);
        adapterChargeMoves.sort(c); // include notify data set changed

        quickMovesHeader.setDPSVisible(isDPSVisible);
        quickMovesHeader.setPowerVisible(isPowerVisible);
        quickMovesHeader.setSpeedVisible(isSpeedVisible);

        adapterQuickMoves.setDPSVisible(isDPSVisible);
        adapterQuickMoves.setPowerVisible(isPowerVisible);
        adapterQuickMoves.setSpeedVisible(isSpeedVisible);
        adapterQuickMoves.sort(c); // include notify data set changed
    }

    @Override
    public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
        this.mCallbackMove = visitor;
    }

    @Override
    public void update(Observable o) {
        if(o == null){ return;}
        if(o.equals(filterMoveView)){
            MoveFilterInfo infos = filterMoveView.getFilterInfos();
            final Filter.FilterListener filterListener = new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int i) {
                    // TODO hide waiting popup
                }
            };
            // TODO show waiting popup
            adapterChargeMoves.getFilter().filter(infos.toStringFilter(), filterListener);
            adapterQuickMoves.getFilter().filter(infos.toStringFilter(), filterListener);
        }
    }

    public enum SortChoice {
        COMPARE_BY_DPS(R.string.sort_by_dps),
        //
        COMPARE_BY_POWER(R.string.sort_by_power),
        //
        COMPARE_BY_DURATION(R.string.sort_by_duration),
        //
        COMPARE_BY_NAME(R.string.sort_by_name);
        //
        // COMPARE_BY_ENERGY(R.string.sort_by_max_cp);

        private int idLabel;

        SortChoice(final int idLabel) {
            this.idLabel = idLabel;
        }
    }
}
