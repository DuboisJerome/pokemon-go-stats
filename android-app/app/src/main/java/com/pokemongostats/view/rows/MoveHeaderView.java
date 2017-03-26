/**
 * 
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.MoveUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.parcalables.PclbMove;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

/**
 * @author Zapagon
 *
 */
public class MoveHeaderView extends LinearLayoutCompat {

	private TextView powerView;
	private TextView dpsView;
	private TextView speedView;

	public MoveHeaderView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public MoveHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context, attrs);
	}

	public MoveHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		if (attrs != null) {
		}

		View.inflate(getContext(), R.layout.view_header_move, this);
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		powerView = (TextView) findViewById(R.id.move_header_power);
		dpsView = (TextView) findViewById(R.id.move_header_dps);
		speedView = (TextView) findViewById(R.id.move_header_duration);
	}

	/**
	 * @param isDPSVisible
	 *            the isDPSVisible to set
	 */
	public void setDPSVisible(boolean isDPSVisible) {
		this.dpsView.setVisibility(isDPSVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isPowerVisible
	 *            the isPowerVisible to set
	 */
	public void setPowerVisible(boolean isPowerVisible) {
		this.powerView.setVisibility(isPowerVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isSpeedVisible
	 *            the isSpeedVisible to set
	 */
	public void setSpeedVisible(boolean isSpeedVisible) {
		this.speedView.setVisibility(isSpeedVisible ? VISIBLE : GONE);
	}
}
