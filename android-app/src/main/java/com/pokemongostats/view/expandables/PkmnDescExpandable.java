/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public class PkmnDescExpandable
		extends
			CustomExpandableList<PokemonDescription> {

	public PkmnDescExpandable(Context context) {
		super(context);
	}

	public PkmnDescExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public PkmnDescExpandable(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View buildView(PokemonDescription p) {
		return PkmnDescRowView.create(getContext(), p);
	}

	@Override
	public int compare(PokemonDescription p1, PokemonDescription p2) {
		if (p1 == null && p2 == null) { return 0; }
		if (p2 == null) { return 1; }
		if (p1 == null) { return -1; }

		String nameP1 = p1.getName();
		String nameP2 = p2.getName();

		if (nameP1 == null && nameP2 == null) { return 0; }
		if (nameP2 == null) { return 1; }
		if (nameP1 == null) { return -1; }

		return nameP1.compareTo(nameP2);
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		PkmnDescExpandableSavedState savedState = new PkmnDescExpandableSavedState(
				superState);
		// end
		savedState.mList = this.mListItem;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof PkmnDescExpandableSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		PkmnDescExpandableSavedState savedState = (PkmnDescExpandableSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.mListItem = savedState.mList;

	}

	protected static class PkmnDescExpandableSavedState
			extends
				CustomExpandableSavedState {

		private List<PokemonDescription> mList;

		PkmnDescExpandableSavedState(Parcelable superState) {
			super(superState);
		}

		protected PkmnDescExpandableSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				// ArrayList of PclbMove (PclbMove extends Move)
				mList.clear();
				mList.addAll(in
						.createTypedArrayList(PclbPokemonDescription.CREATOR));
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			boolean isListEmpty = (mList == null || mList.isEmpty());
			out.writeByte((byte) (!isListEmpty ? 1 : 0));
			if (!isListEmpty) {
				ArrayList<PclbPokemonDescription> arrayList = new ArrayList<PclbPokemonDescription>();
				for (PokemonDescription p : mList) {
					arrayList.add(new PclbPokemonDescription(p));
				}
				out.writeTypedList(arrayList);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<PkmnDescExpandableSavedState> CREATOR = new Parcelable.Creator<PkmnDescExpandableSavedState>() {
			@Override
			public PkmnDescExpandableSavedState createFromParcel(Parcel in) {
				return new PkmnDescExpandableSavedState(in);
			}
			@Override
			public PkmnDescExpandableSavedState[] newArray(int size) {
				return new PkmnDescExpandableSavedState[size];
			}
		};
	}
}
