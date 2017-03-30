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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.adapters.PkmnDescAdapter;
import com.pokemongostats.view.commons.TableLabelTextFieldView;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public class DmgSimuActivity extends CustomAppCompatActivity {

    private Move attMove = null;
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

        // MOVE
        final MoveAdapter adapterMoves = new MoveAdapter(this);
        adapterMoves.clear();

        final Spinner moveAttSpinner = (Spinner) content.findViewById(R.id.move_att_spinner);
        moveAttSpinner.setAdapter(adapterMoves);
        moveAttSpinner.setEnabled(false);
        AdapterView.OnItemSelectedListener onMoveSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attMove = adapterMoves.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        moveAttSpinner.setOnItemSelectedListener(onMoveSelected);

        // ATT
        final Spinner pkmnAttSpinner = (Spinner) content.findViewById(R.id.pkmn_att_spinner);
        pkmnAttSpinner.setAdapter(adapterPkmns);
        AdapterView.OnItemSelectedListener onAttSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attDesc = adapterPkmns.getItem(i);
                if(attDesc != null){
                    moveAttSpinner.setEnabled(true);

                    adapterMoves.setNotifyOnChange(false);
                    adapterMoves.clear();
                    for (Move m : app.getMoves()) {
                        if (attDesc.getMoveIds().contains(m.getId())) {
                            adapterMoves.add(m);
                        }
                    }
                    Comparator<Move> comparatorMove = MoveComparators.getComparatorByDps(attDesc);
                    adapterMoves.sort(comparatorMove);
                    adapterMoves.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        pkmnAttSpinner.setOnItemSelectedListener(onAttSelected);

        // DEF
        final Spinner pkmnDefSpinner = (Spinner) content.findViewById(R.id.pkmn_def_spinner);
        pkmnDefSpinner.setAdapter(adapterPkmns);
        AdapterView.OnItemSelectedListener onDefSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                defDesc = adapterPkmns.getItem(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        pkmnDefSpinner.setOnItemSelectedListener(onDefSelected);

        final EditText attIVInput = (EditText) content.findViewById(R.id.pkmn_att_iv);
        attIVInput.setVisibility(View.GONE);
        final EditText defIVInput = (EditText) content.findViewById(R.id.pkmn_def_iv);
        defIVInput.setVisibility(View.GONE);

        final Button btnSimulate = (Button) content.findViewById(R.id.btn_simulate);
        final TableLabelTextFieldView resultByAttack = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_attack);
        final TableLabelTextFieldView resultBySecond = (TableLabelTextFieldView) content.findViewById(R.id.field_damage_second);

        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pkmn att = new Pkmn();
                att.setAttackIV(intFromInput(attIVInput));
                att.setLevel(30);
                Pkmn def = new Pkmn();
                def.setDefenseIV(intFromInput(defIVInput));
                def.setLevel(30);

                double dmg = FightUtils.computeDamage(attMove, attDesc, att, defDesc, def);
                resultByAttack.setFieldText(String.valueOf(dmg));

                double dmgS = attMove.getDuration() > 0 ? dmg/ (attMove.getDuration() / 1000) : 0;
                resultBySecond.setFieldText(String.valueOf(dmgS));
            }
        });
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
        return (str.isEmpty()) ?  0 :Integer.parseInt(str);
    }
}
