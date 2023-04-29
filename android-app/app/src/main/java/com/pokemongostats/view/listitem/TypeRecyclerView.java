package com.pokemongostats.view.listitem;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class TypeRecyclerView extends RecyclerView {

	public TypeRecyclerView(@NonNull Context context) {
		this(context, 1);
	}

	public TypeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public TypeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TypeRecyclerView(Context context, int nbColonnes) {
		super(context);
		setNbColonnes(nbColonnes);
	}

	public void setNbColonnes(int nbColonnes){
		setLayoutManager(new GridLayoutManager(getContext(), nbColonnes));
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		// end
		// savedState.mList = this.mListItem;

		return new TypeExpandableSavedState(
				superState);
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

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<TypeExpandableSavedState> CREATOR = new Parcelable.Creator<>() {
			@Override
			public TypeExpandableSavedState createFromParcel(Parcel in) {
				return new TypeExpandableSavedState(in);
			}

			@Override
			public TypeExpandableSavedState[] newArray(int size) {
				return new TypeExpandableSavedState[size];
			}
		};
		private List<Type> mList;

		TypeExpandableSavedState(Parcelable superState) {
			super(superState);
		}

		protected TypeExpandableSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				// ArrayList of PclbMove (PclbMove extends Move)
				this.mList.clear();
				ArrayList<PclbType> arrayList = in
						.createTypedArrayList(PclbType.CREATOR);
				assert arrayList != null;
				for (PclbType p : arrayList) {
					this.mList.add(p.getType());
				}
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			boolean isListEmpty = (this.mList == null || this.mList.isEmpty());
			out.writeByte((byte) (!isListEmpty ? 1 : 0));
			if (!isListEmpty) {
				ArrayList<PclbType> arrayList = new ArrayList<>();
				for (Type type : this.mList) {
					arrayList.add(new PclbType(type));
				}
				out.writeTypedList(arrayList);
			}
		}
	}

}