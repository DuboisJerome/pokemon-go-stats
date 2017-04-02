/**
 *
 */
package com.pokemongostats.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class MainMenuActivity extends DefaultAppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View content = initFragmentContent(R.layout.fragment_main_menu);
        final Button btnPokedex = (Button) content.findViewById(R.id.btn_pokedex);
        btnPokedex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start pokedex activity
                final Intent intent = new Intent(MainMenuActivity.this, PokedexActivity.class);
                startActivity(intent);
            }
        });

        final Button btnSimu = (Button) content.findViewById(R.id.btn_simulator);
        btnSimu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start simulator activity
                final Intent intent = new Intent(MainMenuActivity.this, DmgSimuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (getService() != null) {
            getService().minimize();
        }
    }
}
