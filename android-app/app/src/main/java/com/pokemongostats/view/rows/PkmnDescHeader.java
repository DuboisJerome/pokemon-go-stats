/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class PkmnDescHeader extends LinearLayoutCompat {

    public PkmnDescHeader(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public PkmnDescHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public PkmnDescHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(context, R.layout.view_header_pkmn_desc, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setBackgroundColor(getContext().getResources().getColor(R.color.tab_header, getContext().getTheme()));
        setMinimumHeight(100);
        setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical));
        setShowDividers(SHOW_DIVIDER_MIDDLE);
    }
}
