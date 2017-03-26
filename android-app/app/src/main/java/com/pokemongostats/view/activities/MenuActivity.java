/**
 *
 */
package com.pokemongostats.view.activities;

import android.os.Bundle;
import android.view.View;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class MenuActivity extends CustomAppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View content = initFragmentContent(R.layout.fragment_view_pager);
    }

}
