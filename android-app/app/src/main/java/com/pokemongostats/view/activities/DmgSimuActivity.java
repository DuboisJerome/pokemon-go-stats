package com.pokemongostats.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;

/**
 * @author Zapagon
 */
public class DmgSimuActivity extends CustomAppCompatActivity {
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View content = initFragmentContent(R.layout.fragment_dmg_simu);
        final Button btnSimulate = (Button) content.findViewById(R.id.btn_simulate);
        final TextView resultText = (TextView) content.findViewById(R.id.result_text);
        final EditText powerInput = (EditText) content.findViewById(R.id.input_power);
        final EditText attInput = (EditText) content.findViewById(R.id.input_att);
        final EditText defInput = (EditText) content.findViewById(R.id.input_def);
        final EditText stabMInput = (EditText) content.findViewById(R.id.input_stabm);
        final EditText effMInput = (EditText) content.findViewById(R.id.input_effm);
        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double power = doubleFromInput(powerInput);
                double att = doubleFromInput(attInput);
                double def = doubleFromInput(defInput);
                double stabM = doubleFromInput(stabMInput);
                double effM = doubleFromInput(effMInput);

                double dmg = FightUtils.computeDamage(power, att, def, stabM, effM);
                resultText.setText(String.valueOf(dmg));
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
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private double doubleFromInput(final EditText et) {
        String str = et.getText().toString();
        return (str == null || str.isEmpty()) ?  0.0 :Double.parseDouble(str);
    }
}
