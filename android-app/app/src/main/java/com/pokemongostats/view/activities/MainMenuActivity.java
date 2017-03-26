/**
 *
 */
package com.pokemongostats.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class MainMenuActivity extends CustomAppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View content = initFragmentContent(R.layout.fragment_main_menu);
        Button btnPokedex = (Button) content.findViewById(R.id.btnPokedex);
        btnPokedex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Start pokedex activity", Toast.LENGTH_SHORT).show();
                // TODO start pokedex activity
                final Intent intent = new Intent(getApplicationContext(),
                        PokedexActivity.class);

                MainMenuActivity.this.startActivity(intent);
            }
        });
    }

}
