package com.pokemongostats.view.rows;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class MoveHeader extends AbstractMoveRow {

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

		inflate(getContext(), R.layout.card_view_move_header, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setBackgroundColor(getContext().getResources().getColor(R.color.tab_header, getContext().getTheme()));
		setMinimumHeight(60);
		setDividerDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical));
		setShowDividers(SHOW_DIVIDER_MIDDLE);

		initializeViewsEnd(attrs);
	}

}