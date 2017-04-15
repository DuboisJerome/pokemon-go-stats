/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class TrainerHeader extends LinearLayoutCompat {

    public TrainerHeader(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public TrainerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public TrainerHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(context, R.layout.view_header_trainer, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setBackgroundColor(getContext().getResources().getColor(R.color.tab_header));
        setMinimumHeight(100);
        setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical));
        setShowDividers(SHOW_DIVIDER_MIDDLE);
    }
}
