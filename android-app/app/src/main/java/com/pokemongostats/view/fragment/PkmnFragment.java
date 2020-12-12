package com.pokemongostats.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.MoveUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.MoveCombAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.PkmnDescView;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.listitem.MoveCombListItemView;
import com.pokemongostats.view.listitem.MoveListItemView;
import com.pokemongostats.view.listitem.TypeListItemView;
import com.pokemongostats.view.rows.MoveHeader;
import com.pokemongostats.view.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class PkmnFragment extends HistorizedFragment<PkmnDesc>
        implements
        HasMoveSelectable,
        HasTypeSelectable,
        HasPkmnDescSelectable {

    private static final String PKMN_SELECTED_KEY = "PKMN_SELECTED_KEY";

    // pokedex
    private AutoCompleteTextView searchPkmnDesc;
    private PkmnDescAdapter pkmnDescAdapter;
    private final OnItemClickListener onPkmnSelectedListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position != AdapterView.INVALID_POSITION) {
                showItem(pkmnDescAdapter.getItem(position));
            }
        }
    };
    // selected pkmn
    private PkmnDescView selectedPkmnView;
    private MoveAdapter adapterQuickMoves;
    private MoveAdapter adapterChargeMoves;

    // adapter
    private final Map<Double, TypeAdapter> mapTypeEffectivenessAdapter = new TreeMap<>(Collections.reverseOrder());
    // liste view
    private final Map<Double, View> mapTypeEffectivenessText = new TreeMap<>(Collections.reverseOrder());
    private final Map<Double, View> mapTypeEffectivenessListView = new TreeMap<>(Collections.reverseOrder());

    private SelectedVisitor<Type> mCallbackType;
    private SelectedVisitor<Move> mCallbackMove;
    private SelectedVisitor<PkmnDesc> mCallbackPkmnDesc;
    private com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Type> onTypeClicked;
    private com.pokemongostats.view.listitem.CustomListItemView.OnItemClickListener<Move> onMoveClicked;
    private PokedexDAO dao;

    private static double roundEff(final double eff) {
        return Math.round(eff * 1000.0) / 1000.0;
    }

    private void addAdapter(final double eff, final int color) {
        final double roundEff = roundEff(eff);

        final TypeAdapter typeAdapter = new TypeAdapter(getActivity(),
                android.R.layout.simple_spinner_item);
        mapTypeEffectivenessAdapter.put(roundEff, typeAdapter);

        final LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextView txt = new TextView(getActivity());
        txt.setLayoutParams(txtParams);
        txt.setText("Dégâts x " + roundEff);
        txt.setTextColor(color);
        mapTypeEffectivenessText.put(roundEff, txt);

        final TypeListItemView listWeak = new TypeListItemView(getActivity(), 3);
        listWeak.setAdapter(typeAdapter);
        listWeak.setOnItemClickListener(onTypeClicked);

        final LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(txt);
        linearLayout.addView(listWeak);

        mapTypeEffectivenessListView.put(roundEff, linearLayout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // don't show keyboard on activity start
        KeyboardUtils.initKeyboard(getActivity());

        dao = new PokedexDAO(getActivity());
        pkmnDescAdapter = new PkmnDescAdapter(getActivity());
        pkmnDescAdapter.addAll(dao.getListPkmnDesc());
        //
        adapterQuickMoves = new MoveAdapter(getActivity());
        adapterQuickMoves.setPowerVisible(false);
        adapterQuickMoves.setPowerPerSecondVisible(true);
        adapterQuickMoves.setDPTxEPTVisible(true);
        //
        adapterChargeMoves = new MoveAdapter(getActivity());
        adapterChargeMoves.setPowerVisible(false);
        adapterChargeMoves.setPowerPerSecondVisible(true);
        adapterChargeMoves.setDPTxEPTVisible(true);
        //
        onTypeClicked = item -> {
            if (mCallbackType == null) {
                return;
            }
            mCallbackType.select(item);
        };

        mapTypeEffectivenessAdapter.clear();
        mapTypeEffectivenessText.clear();
        mapTypeEffectivenessListView.clear();
        final double eff = EffectivenessUtils.EFF;
        Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
        // double efficace
        addAdapter(eff * eff, getResources().getColor(R.color.super_weakness, theme));
        // simple efficace
        addAdapter(eff, getResources().getColor(R.color.weakness, theme));
        // normal
        //addAdapter(1);
        // simple resistance
        addAdapter(1 / eff, getResources().getColor(R.color.resistance, theme));
        // double resistance ou simple immune
        addAdapter(1 / (eff * eff), getResources().getColor(R.color.super_resistance, theme));
        // simple resistance et simple immune
        addAdapter(1 / (eff * eff * eff), getResources().getColor(R.color.super_resistance, theme));
        // double immune
        addAdapter(1 / (eff * eff * eff * eff), getResources().getColor(R.color.super_resistance, theme));

        onMoveClicked = item -> {
            if (mCallbackMove == null) {
                return;
            }
            mCallbackMove.select(item);
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_pkmn_desc, container, false);

        // search view
        searchPkmnDesc = (AutoCompleteTextView) currentView
                .findViewById(R.id.search_pokemon);
        searchPkmnDesc.setAdapter(pkmnDescAdapter);
        searchPkmnDesc.setOnItemClickListener(onPkmnSelectedListener);

        //
        selectedPkmnView = (PkmnDescView) currentView
                .findViewById(R.id.selected_pkmn);
        selectedPkmnView.acceptSelectedVisitorPkmnDesc(mCallbackPkmnDesc);

        MoveHeader quickMoveHeaderView = (MoveHeader) currentView.findViewById(R.id.pkmn_desc_quickmoves_header);
        quickMoveHeaderView.setPowerVisible(false);

        MoveHeader chargeMoveHeaderView = (MoveHeader) currentView.findViewById(R.id.pkmn_desc_chargemoves_header);
        chargeMoveHeaderView.setPowerVisible(false);

        MoveListItemView quickMoves = (MoveListItemView) currentView
                .findViewById(R.id.pkmn_desc_quickmoves);
        quickMoves.setAdapter(adapterQuickMoves);
        quickMoves.setOnItemClickListener(onMoveClicked);

        MoveListItemView chargeMoves = (MoveListItemView) currentView
                .findViewById(R.id.pkmn_desc_chargemoves);
        chargeMoves.setAdapter(adapterChargeMoves);
        chargeMoves.setOnItemClickListener(onMoveClicked);

        // weaknesses
        LinearLayout layoutWeakness = (LinearLayout) currentView
                .findViewById(R.id.type_weaknesses);
        for (View list : mapTypeEffectivenessListView.values()) {
            layoutWeakness.addView(list);
        }

        return currentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && currentItem == null) {
            currentItem = savedInstanceState.getParcelable(PKMN_SELECTED_KEY);
            updateView();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentItem != null) {
            outState.putParcelable(PKMN_SELECTED_KEY,
                    new PclbPkmnDesc(currentItem));
        }
    }

    @Override
    protected void updateViewImpl() {
        final PkmnDesc pkmn = currentItem;
        adapterQuickMoves.setOwner(pkmn);
        adapterChargeMoves.setOwner(pkmn);
        if (pkmn != null) {
            for (Map.Entry<Double, TypeAdapter> entry : mapTypeEffectivenessAdapter.entrySet()) {

                final double baseEff = entry.getKey();
                final TypeAdapter adapter = entry.getValue();

                adapter.setNotifyOnChange(false);
                // reset previous
                adapter.clear();

                for (Type t : Type.values()) {
                    double eff = roundEff(EffectivenessUtils.getTypeEffOnPokemon(t, pkmn));
                    if (eff == baseEff) {
                        Log.i("TYPE", "add " + t + " to " + eff);
                        adapter.add(t);
                    }
                }

                final View txt = mapTypeEffectivenessText.get(baseEff);
                if (txt == null) {
                    Log.e("ERR", "txt est null");
                } else {
                    if (adapter.getCount() <= 0) {
                        Log.i("ERR", "Gone Eff*" + baseEff);
                        txt.setVisibility(View.GONE);
                    } else {
                        Log.i("ERR", "Visible Eff*" + baseEff);
                        txt.setVisibility(View.VISIBLE);
                    }
                }

                // notify
                adapter.notifyDataSetChanged();
            }
            selectedPkmnView.setPkmnDesc(pkmn);

            adapterQuickMoves.setNotifyOnChange(false);
            adapterChargeMoves.setNotifyOnChange(false);
            adapterQuickMoves.clear();
            adapterChargeMoves.clear();
            Map<Move.MoveType, List<Move>> map = MoveUtils.getMovesMap(dao.getListMoveFor(pkmn));
            List<Move> listQuickMove = map.get(Move.MoveType.QUICK);
            if (listQuickMove == null) {
                listQuickMove = new ArrayList<>();
            }
            List<Move> listChargeMove = map.get(Move.MoveType.CHARGE);
            if (listChargeMove == null) {
                listChargeMove = new ArrayList<>();
            }
            adapterChargeMoves.addAll(listChargeMove);
            adapterQuickMoves.addAll(listQuickMove);
            Comparator<Move> comparatorMove = MoveComparators.getComparatorByPps(pkmn).reversed();
            adapterQuickMoves.sort(comparatorMove);
            adapterChargeMoves.sort(comparatorMove);
            adapterQuickMoves.notifyDataSetChanged();
            adapterChargeMoves.notifyDataSetChanged();
        }

        searchPkmnDesc.setText("");
        KeyboardUtils.hideKeyboard(getActivity());
    }

    /********************
     * LISTENERS / CALLBACK
     ********************/

    @Override
    public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor) {
        this.mCallbackType = visitor;
    }

    @Override
    public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
        this.mCallbackMove = visitor;
    }

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor) {
        this.mCallbackPkmnDesc = visitor;
    }
}
