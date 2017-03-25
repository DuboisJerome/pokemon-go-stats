/**
 * 
 */
package com.pokemongostats.view.expandables;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.parcalables.PclbType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 *
 */
public class TypeListItemView extends CustomListItemView<Type> {

	public TypeListItemView(Context context) {
		super(context);
	}

	public TypeListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TypeListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		TypeExpandableSavedState savedState = new TypeExpandableSavedState(
				superState);
		// end
		// savedState.mList = this.mListItem;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof TypeExpandableSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		TypeExpandableSavedState savedState = (TypeExpandableSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end
		// this.mListItem = savedState.mList;

	}

	protected static class TypeExpandableSavedState
			extends
			BaseSavedState {

		private List<Type> mList;

		TypeExpandableSavedState(Parcelable superState) {
			super(superState);
		}

		protected TypeExpandableSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				// ArrayList of PclbMove (PclbMove extends Move)
				mList.clear();
				ArrayList<PclbType> arrayList = in
						.createTypedArrayList(PclbType.CREATOR);
				for (PclbType p : arrayList) {
					mList.add(p.getType());
				}
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			boolean isListEmpty = (mList == null || mList.isEmpty());
			out.writeByte((byte) (!isListEmpty ? 1 : 0));
			if (!isListEmpty) {
				ArrayList<PclbType> arrayList = new ArrayList<PclbType>();
				for (Type type : mList) {
					arrayList.add(new PclbType(type));
				}
				out.writeTypedList(arrayList);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<TypeExpandableSavedState> CREATOR = new Parcelable.Creator<TypeExpandableSavedState>() {
			@Override
			public TypeExpandableSavedState createFromParcel(Parcel in) {
				return new TypeExpandableSavedState(in);
			}

			@Override
			public TypeExpandableSavedState[] newArray(int size) {
				return new TypeExpandableSavedState[size];
			}
		};
	}
}
