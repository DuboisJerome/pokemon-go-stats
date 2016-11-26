/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveRowView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Zapagon
 *
 */
public class MoveAdapter extends ArrayAdapter<Move> implements HasMoveSelectable {

	private boolean isDPSVisible;
	private boolean isPowerVisible;
	private boolean isSpeedVisible;

	private SelectedVisitor<Move> mCallbackMove;

	public MoveAdapter(Context context, int textViewResourceId, Move[] list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context context, int textViewResourceId, List<Move> list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context applicationContext, int simpleSpinnerItem) {
		super(applicationContext, simpleSpinnerItem);
	}

	/**
	 * @return the isDPSVisible
	 */
	public boolean isDPSVisible() {
		return isDPSVisible;
	}

	/**
	 * @param isDPSVisible
	 *            the isDPSVisible to set
	 */
	public void setDPSVisible(boolean isDPSVisible) {
		this.isDPSVisible = isDPSVisible;
	}

	/**
	 * @return the isPowerVisible
	 */
	public boolean isPowerVisible() {
		return isPowerVisible;
	}

	/**
	 * @param isPowerVisible
	 *            the isPowerVisible to set
	 */
	public void setPowerVisible(boolean isPowerVisible) {
		this.isPowerVisible = isPowerVisible;
	}

	/**
	 * @return the isSpeedVisible
	 */
	public boolean isSpeedVisible() {
		return isSpeedVisible;
	}

	/**
	 * @param isSpeedVisible
	 *            the isSpeedVisible to set
	 */
	public void setSpeedVisible(boolean isSpeedVisible) {
		this.isSpeedVisible = isSpeedVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		return createViewAtPosition(position, v, parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent) {
		return createViewAtPosition(position, v, parent);
	}

	/**
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private View createViewAtPosition(int position, View v, ViewGroup parent) {
		Move move = getItem(position);
		if (move == null) { return v; }

		final MoveRowView view;
		if (v == null || !(v instanceof MoveRowView)) {
			view = MoveRowView.create(getContext(), move);
		} else {
			view = (MoveRowView) v;
			if (!move.equals(view.getMove())) {
				view.setMove(move);
				if (mCallbackMove != null) {
					view.setOnClickListener(new OnClickItemListener<Move>(mCallbackMove, move));
				}
			}
		}
		view.setDPSVisible(isDPSVisible);
		view.setPowerVisible(isPowerVisible);
		view.setSpeedVisible(isSpeedVisible);

		return view;
	}

	@Override
	public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
		this.mCallbackMove = visitor;
	}
}