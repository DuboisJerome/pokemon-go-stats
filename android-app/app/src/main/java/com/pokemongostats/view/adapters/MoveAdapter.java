/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.MoveRowView;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Zapagon
 *
 */
public class MoveAdapter extends ArrayAdapter<Move>
		implements
			HasMoveSelectable {

	private boolean isDPSVisible;
	private boolean isPowerVisible;
	private boolean isSpeedVisible;

	private SelectedVisitor<Move> mCallbackMove;

	// pokemon who own those moves
	private PokemonDescription owner;

	public MoveAdapter(Context context, int textViewResourceId, Move[] list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context context, int textViewResourceId,
			List<Move> list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context applicationContext, int simpleSpinnerItem) {
		super(applicationContext, simpleSpinnerItem);
	}

	public void setOwner(PokemonDescription owner) {
		this.owner = owner;
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

	private View createViewAtPosition(int position, View v, ViewGroup parent) {
		final Move move = getItem(position);
		if (move == null) { return v; }

		final MoveRowView view;
		if (v == null || !(v instanceof MoveRowView)) {
			view = new MoveRowView(getContext());
			view.setPkmnMove(owner, move);
			view.update();
		} else {
			view = (MoveRowView) v;
			if (!move.equals(view.getMove())) {
				view.setPkmnMove(owner, move);
				if (mCallbackMove != null) {
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mCallbackMove == null) { return; }
							mCallbackMove.select(move);
						}
					});
				}
				view.update();
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