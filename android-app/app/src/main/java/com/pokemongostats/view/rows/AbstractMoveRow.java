package com.pokemongostats.view.rows;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class AbstractMoveRow extends LinearLayout {

    protected TextView powerView;
    protected TextView energyView;
    protected TextView powerPerSecondView;
    protected TextView durationView;
    // EPT
    protected TextView energyPvpPerTurnView;
    // DPT
    protected TextView powerPvpPerTurnView;
    // EPT x DPT
    protected TextView dptXEptView;

    public AbstractMoveRow(Context context) {
        super(context);
    }

    public AbstractMoveRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractMoveRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initializeViewsEnd(AttributeSet attrs) {
        powerView = findViewById(R.id.move_power);
        energyView = findViewById(R.id.move_energy);
        powerPerSecondView = findViewById(R.id.move_pps);
        durationView = findViewById(R.id.move_duration);
        powerPvpPerTurnView = findViewById(R.id.move_dpt);
        energyPvpPerTurnView = findViewById(R.id.move_ept);
        dptXEptView = findViewById(R.id.move_dpt_x_ept);
    }

    public void setPowerVisible(boolean b) {
        setVisible(this.powerView, b);
    }

    public void setEnergyVisible(boolean b) {
        setVisible(this.energyView, b);
    }

    public void setPowerPerSecondVisible(boolean b) {
        setVisible(this.powerPerSecondView, b);
    }

    public void setDurationVisible(boolean b) {
        setVisible(this.durationView, b);
    }

    public void setDPTVisible(boolean b) {
        setVisible(this.powerPvpPerTurnView, b);
    }

    public void setEPTVisible(boolean b) {
        setVisible(this.energyPvpPerTurnView, b);
    }

    public void setDPTxEPTVisible(boolean b) {
        setVisible(this.dptXEptView, b);
    }

    private static void setVisible(TextView tv, boolean isVisible) {
        tv.setVisibility(isVisible ? VISIBLE : GONE);
    }
}
