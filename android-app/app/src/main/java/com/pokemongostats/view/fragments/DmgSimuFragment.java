package com.pokemongostats.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.filters.InputFilterMinMax;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.MoveUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.fight.Fight;
import com.pokemongostats.model.bean.fight.Fighter;
import com.pokemongostats.model.bean.fight.OnAttackListener;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.rows.PkmnDescRowView;
import com.pokemongostats.view.utils.HasRequiredField;
import com.pokemongostats.view.utils.KeyboardUtils;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Zapagon
 */
public class DmgSimuFragment extends DefaultFragment implements HasRequiredField {

    private Move quickMoveAtt, chargeMoveAtt, quickMoveDef, chargeMoveDef;
    private PkmnDesc attDesc, defDesc;
    private PkmnDescAdapter adapterPkmns;
    private PokedexDAO dao;

    private MoveAdapter adapterQuickMoveAtt, adapterChargeMoveAtt, adapterQuickMoveDef, adapterChargeMoveDef;

    private View mView, viewAtt, viewDef;
    private Button btnSimulate;
    private Spinner quickMoveAttSpinner, chargeMoveAttSpinner, quickMoveDefSpinner, chargeMoveDefSpinner;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new PokedexDAO(getActivity());
        final Context ctx = getActivity().getApplicationContext();
        adapterPkmns = new PkmnDescAdapter(ctx);
        adapterPkmns.addAll(dao.getListPkmnDesc(
                PreferencesUtils.isLastEvolutionOnly(ctx)));
        adapterPkmns.sort(PkmnDescComparators.getComparatorByName());
        adapterQuickMoveAtt = new MoveAdapter(ctx);
        adapterQuickMoveAtt.clear();
        adapterChargeMoveAtt = new MoveAdapter(ctx);
        adapterChargeMoveAtt.clear();
        adapterQuickMoveDef = new MoveAdapter(ctx);
        adapterQuickMoveDef.clear();
        adapterChargeMoveDef = new MoveAdapter(ctx);
        adapterChargeMoveDef.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dmg_simu, container, false);
        viewAtt = mView.findViewById(R.id.pkmn_att);
        viewDef = mView.findViewById(R.id.pkmn_def);
        btnSimulate = (Button) mView.findViewById(R.id.btn_simulate);

        // MOVE
        initQuickMoveAtt();
        initChargeMoveAtt();
        initQuickMoveDef();
        initChargeMoveDef();

        // ATT
        final PkmnDescRowView selectedAtt = (PkmnDescRowView) viewAtt.findViewById(R.id.pkmn_desc);
        final AutoCompleteTextView searchPkmnAtt = (AutoCompleteTextView) mView.findViewById(R.id.dmg_simu_search_att);
        searchPkmnAtt.setAdapter(adapterPkmns);
        searchPkmnAtt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                attDesc = adapterPkmns.getItem(i);
                selectedAtt.updateWith(attDesc);
                boolean attOk = attDesc != null;
                quickMoveAttSpinner.setEnabled(attOk);
                chargeMoveAttSpinner.setEnabled(attOk);
                if (attOk) {
                    Map<Move.MoveType, List<Move>> map = MoveUtils.getMovesMap(dao.getListMove(), attDesc.getMoveIds());

                    // QUICK
                    adapterQuickMoveAtt.clear();
                    adapterQuickMoveAtt.addAll(map.get(Move.MoveType.QUICK));
                    quickMoveAttSpinner.setSelection(0);
                    quickMoveAtt = adapterQuickMoveAtt.getItem(0);

                    // CHARGE
                    adapterChargeMoveAtt.clear();
                    adapterChargeMoveAtt.addAll(map.get(Move.MoveType.CHARGE));
                    chargeMoveAttSpinner.setSelection(0);
                    chargeMoveAtt = adapterChargeMoveAtt.getItem(0);
                }
                searchPkmnAtt.setText("");
                KeyboardUtils.hideKeyboard(getActivity());
                btnSimulate.setEnabled(checkAllField());
            }
        });
        InputFilterMinMax lvlFilter = new InputFilterMinMax(getActivity(),1.0, 40.0);
        InputFilterMinMax ivFilter = new InputFilterMinMax(getActivity(),0, 15);
        final EditText lvlInputAtt = (EditText) viewAtt.findViewById(R.id.pkmn_lvl);
        lvlInputAtt.setFilters(new InputFilter[]{lvlFilter});
        final EditText attIVInputAtt = (EditText) viewAtt.findViewById(R.id.pkmn_att_iv);
        attIVInputAtt.setFilters(new InputFilter[]{ivFilter});
        final EditText defIVInputAtt = (EditText) viewAtt.findViewById(R.id.pkmn_def_iv);
        defIVInputAtt.setFilters(new InputFilter[]{ivFilter});
        final EditText staIVInputAtt = (EditText) viewAtt.findViewById(R.id.pkmn_sta_iv);
        staIVInputAtt.setFilters(new InputFilter[]{ivFilter});

        // DEF
        final PkmnDescRowView selectedDef = (PkmnDescRowView) viewDef.findViewById(R.id.pkmn_desc);
        final AutoCompleteTextView searchPkmnDef = (AutoCompleteTextView) mView.findViewById(R.id.dmg_simu_search_def);
        searchPkmnDef.setAdapter(adapterPkmns);
        searchPkmnDef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                defDesc = adapterPkmns.getItem(i);
                selectedDef.updateWith(defDesc);
                boolean defOk = defDesc != null;
                quickMoveDefSpinner.setEnabled(defOk);
                chargeMoveDefSpinner.setEnabled(defOk);
                if (defOk) {
                    Map<Move.MoveType, List<Move>> map = MoveUtils.getMovesMap(dao.getListMove(), defDesc.getMoveIds());

                    // QUICK
                    adapterQuickMoveDef.clear();
                    adapterQuickMoveDef.addAll(map.get(Move.MoveType.QUICK));
                    quickMoveDefSpinner.setSelection(0);
                    quickMoveDef = adapterQuickMoveDef.getItem(0);

                    // CHARGE
                    adapterChargeMoveDef.clear();
                    adapterChargeMoveDef.addAll(map.get(Move.MoveType.CHARGE));
                    chargeMoveDefSpinner.setSelection(0);
                    chargeMoveDef = adapterChargeMoveDef.getItem(0);
                }
                searchPkmnDef.setText("");
                KeyboardUtils.hideKeyboard(getActivity());
                btnSimulate.setEnabled(checkAllField());
            }
        });

        final EditText lvlInputDef = (EditText) viewDef.findViewById(R.id.pkmn_lvl);
        lvlInputDef.setFilters(new InputFilter[]{lvlFilter});
        final EditText attIVInputDef = (EditText) viewDef.findViewById(R.id.pkmn_att_iv);
        attIVInputDef.setFilters(new InputFilter[]{ivFilter});
        final EditText defIVInputDef = (EditText) viewDef.findViewById(R.id.pkmn_def_iv);
        defIVInputDef.setFilters(new InputFilter[]{ivFilter});
        final EditText staIVInputDef = (EditText) viewDef.findViewById(R.id.pkmn_sta_iv);
        staIVInputDef.setFilters(new InputFilter[]{ivFilter});

        final TextView results = (TextView) mView.findViewById(R.id.logs);

        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results.setText("");

                Pkmn att = new Pkmn();
                att.setAttackIV(intFromInput(attIVInputAtt));
                att.setDefenseIV(intFromInput(defIVInputAtt));
                att.setStaminaIV(intFromInput(staIVInputAtt));
                att.setLevel(doubleFromInput(lvlInputAtt));
                att.setDesc(attDesc);
                att.setQuickMove(quickMoveAtt);
                att.setChargeMove(chargeMoveAtt);

                Pkmn def = new Pkmn();
                def.setAttackIV(intFromInput(attIVInputDef));
                def.setDefenseIV(intFromInput(defIVInputDef));
                def.setStaminaIV(intFromInput(staIVInputDef));
                def.setLevel(doubleFromInput(lvlInputDef));
                def.setDesc(defDesc);
                def.setQuickMove(quickMoveDef);
                def.setChargeMove(chargeMoveDef);

                Fight f = new Fight();
                f.addOnAttackListener(new OnAttackListener() {
                    @Override
                    public void onAttack(int i, Fighter att, Fighter def, Move move) {
                        String attName = att.getPkmn().getDesc().getName();
                        String defName = def.getPkmn().getDesc().getName();
                        double dmg = FightUtils.computeDamage(move.getMoveType(), att.getPkmn(), def.getPkmn());
                        StringBuilder sb = new StringBuilder();
                        sb.append(getString(R.string.on_attack, attName, (int) dmg, defName, move.getName()));
                        sb.append(System.getProperty("line.separator"));
                        sb.append(getString(R.string.hp_remaining, defName, (int) def.getHP()));
                        sb.append(System.getProperty("line.separator"));
                        sb.append(getString(R.string.nrj_remaining, attName, (int) att.getEnergy()));
                        sb.append(System.getProperty("line.separator"));

                        Log.i(TagUtils.DEBUG, sb.toString());
                        results.append(sb.toString());
                    }
                });
                f.simulate(att, def);
            }
        });

        return mView;
    }

    private void initChargeMoveDef() {
        chargeMoveDefSpinner = (Spinner) viewDef.findViewById(R.id.chargemove_spinner);
        chargeMoveDefSpinner.setAdapter(adapterChargeMoveDef);
        chargeMoveDefSpinner.setEnabled(false);
        chargeMoveDefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chargeMoveDef = adapterChargeMoveDef.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chargeMoveDef = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });
    }

    private void initQuickMoveDef() {
        quickMoveDefSpinner = (Spinner) viewDef.findViewById(R.id.quickmove_spinner);
        quickMoveDefSpinner.setAdapter(adapterQuickMoveDef);
        quickMoveDefSpinner.setEnabled(false);
        quickMoveDefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quickMoveDef = adapterQuickMoveDef.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                quickMoveDef = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });
    }

    private void initChargeMoveAtt() {
        chargeMoveAttSpinner = (Spinner) viewAtt.findViewById(R.id.chargemove_spinner);
        chargeMoveAttSpinner.setAdapter(adapterChargeMoveAtt);
        chargeMoveAttSpinner.setEnabled(false);
        chargeMoveAttSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chargeMoveAtt = adapterChargeMoveAtt.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chargeMoveAtt = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });
    }

    private void initQuickMoveAtt() {
        quickMoveAttSpinner = (Spinner) viewAtt.findViewById(R.id.quickmove_spinner);
        quickMoveAttSpinner.setAdapter(adapterQuickMoveAtt);
        quickMoveAttSpinner.setEnabled(false);
        quickMoveAttSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quickMoveAtt = adapterQuickMoveAtt.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                quickMoveAtt = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });
    }

    private int intFromInput(final EditText et) {
        String str = et.getText().toString();
        return (str.isEmpty()) ? 0 : Integer.parseInt(str);
    }

    private double doubleFromInput(final EditText et) {
        String str = et.getText().toString();
        return (str.isEmpty()) ? 0 : Double.parseDouble(str);
    }

    @Override
    public boolean checkAllField() {
        boolean attOk = attDesc != null;
        boolean attQuickMoveOk = quickMoveAtt != null;
        boolean attChargeMoveOk = chargeMoveAtt != null;
        boolean defOk = defDesc != null;
        boolean defQuickMoveOk = quickMoveDef != null;
        boolean defChargeMoveOk = chargeMoveDef != null;
        return attOk && attQuickMoveOk && attChargeMoveOk && defOk && defQuickMoveOk && defChargeMoveOk;
    }

    @Override
    public void onBackPressed() {
        MainActivity a = getMainActivity();
        if (a != null) {
            if (a.getService() != null) {
                a.getService().minimize();
            }
        }
    }
}
