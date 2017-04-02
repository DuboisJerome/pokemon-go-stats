package com.pokemongostats.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.MathUtils;
import com.pokemongostats.controller.utils.MoveUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.TableLabelTextFieldView;
import com.pokemongostats.view.rows.PkmnDescRowView;
import com.pokemongostats.view.utils.HasRequiredField;
import com.pokemongostats.view.utils.KeyboardUtils;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Zapagon
 */
public class DmgSimuActivity extends DefaultAppCompatActivity implements HasRequiredField {

    private Move quickAttMove = null, chargeAttMove = null;
    private PkmnDesc attDesc = null, defDesc = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View content = initFragmentContent(R.layout.fragment_dmg_simu);

        final PkmnGoStatsApplication app = (PkmnGoStatsApplication) getApplicationContext();

        final PkmnDescAdapter adapterPkmns = new PkmnDescAdapter(this);
        adapterPkmns.addAll(app.getPokedex(
                PreferencesUtils.isLastEvolutionOnly(this)));
        adapterPkmns.sort(PkmnDescComparators.getComparatorByName());

        final Button btnSimulate = (Button) content.findViewById(R.id.btn_simulate);
        // MOVE
        final MoveAdapter quickAdapterMoves = new MoveAdapter(this);
        quickAdapterMoves.clear();
        final MoveAdapter chargeAdapterMoves = new MoveAdapter(this);
        chargeAdapterMoves.clear();

        final Spinner quickMoveAttSpinner = (Spinner) content.findViewById(R.id.quickmove_att_spinner);
        quickMoveAttSpinner.setAdapter(quickAdapterMoves);
        quickMoveAttSpinner.setEnabled(false);
        quickMoveAttSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quickAttMove = quickAdapterMoves.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                quickAttMove = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });

        final Spinner chargeMoveAttSpinner = (Spinner) content.findViewById(R.id.chargemove_att_spinner);
        chargeMoveAttSpinner.setAdapter(chargeAdapterMoves);
        chargeMoveAttSpinner.setEnabled(false);
        chargeMoveAttSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chargeAttMove = chargeAdapterMoves.getItem(i);
                btnSimulate.setEnabled(checkAllField());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chargeAttMove = null;
                btnSimulate.setEnabled(checkAllField());
            }
        });

        // ATT
        final PkmnDescRowView selectedAtt = (PkmnDescRowView) content.findViewById(R.id.pkmn_att);
        final AutoCompleteTextView searchPkmnAtt = (AutoCompleteTextView) content.findViewById(R.id.dmg_simu_search_att);
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
                    Map<Move.MoveType, List<Move>> map = MoveUtils.getMovesMap(app.getMoves(), attDesc.getMoveIds());

                    // QUICK
                    quickAdapterMoves.clear();
                    quickAdapterMoves.addAll(map.get(Move.MoveType.QUICK));
                    quickMoveAttSpinner.setSelection(0);
                    quickAttMove = quickAdapterMoves.getItem(0);

                    // CHARGE
                    chargeAdapterMoves.clear();
                    chargeAdapterMoves.addAll(map.get(Move.MoveType.CHARGE));
                    chargeMoveAttSpinner.setSelection(0);
                    chargeAttMove = chargeAdapterMoves.getItem(0);
                }
                btnSimulate.setEnabled(checkAllField());
                searchPkmnAtt.setText("");
                KeyboardUtils.hideKeyboard(DmgSimuActivity.this);
            }
        });

        // DEF
        final PkmnDescRowView selectedDef = (PkmnDescRowView) content.findViewById(R.id.pkmn_def);
        final AutoCompleteTextView searchPkmnDef = (AutoCompleteTextView) content.findViewById(R.id.dmg_simu_search_def);
        searchPkmnDef.setAdapter(adapterPkmns);
        searchPkmnDef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                defDesc = adapterPkmns.getItem(i);
                selectedDef.updateWith(defDesc);
                searchPkmnDef.setText("");
                KeyboardUtils.hideKeyboard(DmgSimuActivity.this);
                btnSimulate.setEnabled(checkAllField());
            }
        });

        final EditText attIVInput = (EditText) content.findViewById(R.id.pkmn_att_iv);
        attIVInput.setVisibility(View.GONE);
        final EditText defIVInput = (EditText) content.findViewById(R.id.pkmn_def_iv);
        defIVInput.setVisibility(View.GONE);

        final TableLabelTextFieldView resultByAttackQuick = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_attack_quick);
        final TableLabelTextFieldView resultBySecondQuick = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_second_quick);
        final TableLabelTextFieldView resultByAttackCharge = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_attack_charge);
        final TableLabelTextFieldView resultBySecondCharge = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_second_charge);

        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pkmn att = new Pkmn();
                att.setAttackIV(intFromInput(attIVInput));
                att.setLevel(30f);
                Pkmn def = new Pkmn();
                def.setDefenseIV(intFromInput(defIVInput));
                def.setLevel(30f);

                // QUICK attack result
                double dmg = FightUtils.computeDamage(quickAttMove, attDesc, att, defDesc, def);
                resultByAttackQuick.setFieldText(String.valueOf(dmg));

                double dmgS = 0d;
                int duration = quickAttMove.getDuration();
                if (duration > 0) {
                    dmgS = MathUtils.round(dmg / (duration / 1000.0), 2);
                }
                resultBySecondQuick.setFieldText(String.valueOf(dmgS));

                // CHARGE attack result
                dmg = FightUtils.computeDamage(chargeAttMove, attDesc, att, defDesc, def);
                resultByAttackCharge.setFieldText(String.valueOf(dmg));

                dmgS = 0d;
                duration = chargeAttMove.getDuration();
                if (duration > 0) {
                    dmgS = MathUtils.round(dmg / (duration / 1000.0), 2);
                }
                resultBySecondCharge.setFieldText(String.valueOf(dmgS));

                Log.i(TagUtils.DEBUG, attDesc.getName() + " on " + defDesc.getName() + " with " + quickAttMove.getName());
                Log.i(TagUtils.DEBUG, attDesc.getName() + " on " + defDesc.getName() + " with " + chargeAttMove.getName());
            }
        });

        setTitle(R.string.simulator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.secondary_menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
            supportActionBar.setHomeActionContentDescription(R.string.home_btn_desc);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Log.i(TagUtils.DEBUG, "Start activity " + MainMenuActivity.class.getName());
            Intent intent = new Intent(this,
                    MainMenuActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private int intFromInput(final EditText et) {
        String str = et.getText().toString();
        return (str.isEmpty()) ? 0 : Integer.parseInt(str);
    }

    @Override
    public boolean checkAllField() {
        boolean attOk = attDesc != null;
        boolean attQuickMoveOk = quickAttMove != null;
        boolean attChargeMoveOk = chargeAttMove != null;
        boolean defOk = defDesc != null;
        return attOk && attQuickMoveOk && attChargeMoveOk && defOk;
    }
}
