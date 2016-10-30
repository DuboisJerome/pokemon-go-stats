/**
 * 
 */
package com.pokemongostats.view.rows;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.parcalables.PclbMove;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class MoveRowView extends LinearLayout {

	private TextView nameView;
	private TypeRowView typeView;
	private TextView powerView;
	private TextView dpsView;

	private Move move;
	private PokemonDescription owner;

	public MoveRowView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public MoveRowView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public MoveRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		if (attrs != null) {
		}

		inflate(getContext(), R.layout.view_row_move, this);
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		nameView = (TextView) findViewById(R.id.move_name);
		typeView = (TypeRowView) findViewById(R.id.move_type);
		powerView = (TextView) findViewById(R.id.move_power);
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
	 * @return the pkmnOwner
	 */
	public PokemonDescription getPkmnOwner() {
		return owner;
	}

	/**
	 * @param pkmnDesc
	 *            the pkmnDesc to set
	 */
	public void setMove(Move m) {
		setPkmnMove(null, m);
	}

	public void setPkmnMove(PokemonDescription owner, Move m) {
		this.owner = owner;
		this.move = m;
		if (m == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			int white = android.R.color.white;
			Type type = m.getType();
			nameView.setText(m.getName());
			nameView.setTextColor(getContext().getResources().getColor(white));

			typeView.setType(type);

			int dpsColorId = android.R.color.white;
			double timeInSecond = m.getDuration() / 1000.0;
			double dps = m.getPower() / timeInSecond;
			if (owner != null) {
				if (type.equals(owner.getType1())
					|| type.equals(owner.getType2())) {
					dps = dps * 1.25;
					dpsColorId = R.color.stab_dps;
				}
			}
			dps = Math.floor(dps * 100) / 100;

			powerView.setText(String.valueOf(m.getPower()));
			powerView.setTextColor(getContext().getResources().getColor(white));

			dpsView.setText(String.valueOf(dps));
			dpsView.setTextColor(
					getContext().getResources().getColor(dpsColorId));
		}
	}

	public static MoveRowView create(Context context, Move m) {
		MoveRowView v = new MoveRowView(context);
		v.setMove(m);
		return v;
	}

	public static MoveRowView create(Context context, Move m,
			PokemonDescription p) {
		MoveRowView v = new MoveRowView(context);
		v.setPkmnMove(p, m);
		return v;
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		MoveRowViewSavedState savedState = new MoveRowViewSavedState(
				superState);
		// end
		savedState.move = this.move;
		savedState.owner = this.owner;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof MoveRowViewSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		MoveRowViewSavedState savedState = (MoveRowViewSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.move = savedState.move;
		this.owner = savedState.owner;
	}

	protected static class MoveRowViewSavedState extends BaseSavedState {

		private Move move;
		private PokemonDescription owner;

		MoveRowViewSavedState(Parcelable superState) {
			super(superState);
		}

		protected MoveRowViewSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				this.move = in.readParcelable(PclbMove.class.getClassLoader());
			}
			if (in.readByte() != 0) {
				this.owner = in.readParcelable(
						PclbPokemonDescription.class.getClassLoader());
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (move != null ? 1 : 0));
			if (move != null) {
				out.writeParcelable(new PclbMove(move), 0);
			}
			out.writeByte((byte) (owner != null ? 1 : 0));
			if (owner != null) {
				out.writeParcelable(new PclbPokemonDescription(owner), 0);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<MoveRowViewSavedState> CREATOR = new Parcelable.Creator<MoveRowViewSavedState>() {
			@Override
			public MoveRowViewSavedState createFromParcel(Parcel in) {
				return new MoveRowViewSavedState(in);
			}
			@Override
			public MoveRowViewSavedState[] newArray(int size) {
				return new MoveRowViewSavedState[size];
			}
		};
	}
}
