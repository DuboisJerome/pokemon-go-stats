/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class MoveHeader extends LinearLayoutCompat {

    private TextView powerView;
    private TextView ppsView;
    private TextView speedView;

    public MoveHeader(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public MoveHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public MoveHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(getContext(), R.layout.view_header_move, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setBackgroundColor(getContext().getResources().getColor(R.color.tab_header, getContext().getTheme()));
        setMinimumHeight(60);
        setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical));
        setShowDividers(SHOW_DIVIDER_MIDDLE);

        powerView =  findViewById(R.id.move_header_power);
        ppsView =  findViewById(R.id.move_header_pps);
        speedView =  findViewById(R.id.move_header_duration);
    }

    /**
     * @param isPPSVisible the isPPSVisible to set
     */
    public void setPPSVisible(boolean isPPSVisible) {
        this.ppsView.setVisibility(isPPSVisible ? VISIBLE : GONE);
    }

    /**
     * @param isPowerVisible the isPowerVisible to set
     */
    public void setPowerVisible(boolean isPowerVisible) {
        this.powerView.setVisibility(isPowerVisible ? VISIBLE : GONE);
    }

    /**
     * @param isSpeedVisible the isSpeedVisible to set
     */
    public void setSpeedVisible(boolean isSpeedVisible) {
        this.speedView.setVisibility(isSpeedVisible ? VISIBLE : GONE);
    }
}
