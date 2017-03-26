/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class PkmnDescHeaderView extends LinearLayoutCompat {

    private TextView baseAttackView;
    private TextView baseDefenseView;
    private TextView baseStaminaView;
    private TextView maxCPView;

    public PkmnDescHeaderView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public PkmnDescHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public PkmnDescHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(context, R.layout.view_header_pkmn_desc, this);
        // setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        baseAttackView = (TextView) findViewById(R.id.pkmn_desc_header_base_attack);
        baseDefenseView = (TextView) findViewById(R.id.pkmn_desc_header_base_defense);
        baseStaminaView = (TextView) findViewById(R.id.pkmn_desc_header_base_stamina);
        maxCPView = (TextView) findViewById(R.id.pkmn_desc_header_max_cp);
    }

    /**
     * @param isBaseAttVisible the isBaseAttVisible to set
     */
    public void setBaseAttVisible(boolean isBaseAttVisible) {
        baseAttackView.setVisibility(isBaseAttVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseDefVisible the isBaseDefVisible to set
     */
    public void setBaseDefVisible(boolean isBaseDefVisible) {
        baseDefenseView.setVisibility(isBaseDefVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseStaminaVisible the isBaseStaminaVisible to set
     */
    public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
        baseStaminaView.setVisibility(isBaseStaminaVisible ? VISIBLE : GONE);
    }

    /**
     * @param isMaxCPVisible the isMaxCPVisible to set
     */
    public void setMaxCPVisible(boolean isMaxCPVisible) {
        maxCPView.setVisibility(isMaxCPVisible ? VISIBLE : GONE);
    }
}
