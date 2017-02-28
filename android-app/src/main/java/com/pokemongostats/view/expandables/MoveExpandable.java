/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.parcalables.PclbMove;
import com.pokemongostats.view.rows.MoveRowView;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public class MoveExpandable extends CustomExpandableList<Move, MoveRowView> {

	public MoveExpandable(Context context) {
		super(context);
	}

	public MoveExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public MoveExpandable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public MoveRowView buildViewImpl() {
		return new MoveRowView(getContext());
	}

	public void add(Move m, PokemonDescription p, OnClickListener l) {
		MoveRowView v = buildOrGetView();
		v.setPkmnMove(p, m);
		v.update();

		mListItem.add(m);
		Collections.sort(mListItem, this);
		int index = mListItem.indexOf(m);

		View view = v.getView();
		view.setVisibility(isExpand() ? VISIBLE : GONE);
		view.setOnClickListener(l);
		if (view.getParent() == null) {
			layout.addView(view, index);
		}

		recolorEvenOddRows(index, layout.getChildCount());
	}

	@Override
	public int compare(Move m1, Move m2) {
		if (m1 == null && m2 == null) { return 0; }
		if (m2 == null) { return 1; }
		if (m1 == null) { return -1; }

		String nameM1 = m1.getName();
		String nameM2 = m2.getName();

		if (nameM1 == null && nameM2 == null) { return 0; }
		if (nameM2 == null) { return 1; }
		if (nameM1 == null) { return -1; }

		return nameM1.compareTo(nameM2);
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		MoveExpandableSavedState savedState = new MoveExpandableSavedState(superState);
		// end
		savedState.mList = this.mListItem;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof MoveExpandableSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		MoveExpandableSavedState savedState = (MoveExpandableSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.mListItem = savedState.mList;

	}

	protected static class MoveExpandableSavedState extends CustomExpandableSavedState {

		private List<Move> mList;

		MoveExpandableSavedState(Parcelable superState) {
			super(superState);
		}

		protected MoveExpandableSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				// ArrayList of PclbMove (PclbMove extends Move)
				mList.clear();
				mList.addAll(in.createTypedArrayList(PclbMove.CREATOR));
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			boolean isListEmpty = (mList == null || mList.isEmpty());
			out.writeByte((byte) (!isListEmpty ? 1 : 0));
			if (!isListEmpty) {
				ArrayList<PclbMove> arrayList = new ArrayList<PclbMove>();
				for (Move m : mList) {
					arrayList.add(new PclbMove(m));
				}
				out.writeTypedList(arrayList);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<MoveExpandableSavedState> CREATOR = new Parcelable.Creator<MoveExpandableSavedState>() {
			@Override
			public MoveExpandableSavedState createFromParcel(Parcel in) {
				return new MoveExpandableSavedState(in);
			}

			@Override
			public MoveExpandableSavedState[] newArray(int size) {
				return new MoveExpandableSavedState[size];
			}
		};
	}
}
