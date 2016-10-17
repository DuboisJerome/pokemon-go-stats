/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class MoveView extends LinearLayout {

	private TextView nameView;
	private TypeView typeView;
	private TextView dpsView;

	private Move move;

	public MoveView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public MoveView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public MoveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		if (attrs != null) {
		}

		inflate(getContext(), R.layout.view_move, this);
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		nameView = (TextView) findViewById(R.id.move_name);
		typeView = (TypeView) findViewById(R.id.move_type);
		dpsView = (TextView) findViewById(R.id.move_dps);
		setVisibility(View.GONE);
	}

	/**
	 * @return the pkmnDesc
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * @param pkmnDesc
	 *            the pkmnDesc to set
	 */
	public void setMove(Move m) {
		move = m;
		if (m == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			int white = getContext().getResources()
					.getColor(android.R.color.white);
			double timeInSecond = m.getDuration() / 1000.0;
			double dps = m.getPower() / timeInSecond;
			dps = Math.floor(dps * 100) / 100;
			nameView.setText(m.getName());
			nameView.setTextColor(white);
			typeView.setType(m.getType());
			dpsView.setText(String.valueOf(dps));
			dpsView.setTextColor(white);
		}
	}

	public static MoveView create(Context context, Move m) {
		MoveView v = new MoveView(context);
		v.setMove(m);
		return v;
	}
}
