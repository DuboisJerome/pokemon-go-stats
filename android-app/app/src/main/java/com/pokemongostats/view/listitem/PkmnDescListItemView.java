/**
 *
 */
package com.pokemongostats.view.listitem;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.view.parcalables.PclbPkmnDesc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class PkmnDescListItemView
        extends
        CustomListItemView<PkmnDesc> {

    public PkmnDescListItemView(Context context) {
        super(context);
    }

    public PkmnDescListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PkmnDescListItemView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        PkmnDescExpandableSavedState savedState = new PkmnDescExpandableSavedState(
                superState);
        // end
        // savedState.mList = this.mListItem;

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

        // this.mListItem = savedState.mList;

    }

    protected static class PkmnDescExpandableSavedState
            extends
            BaseSavedState {

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
        private List<PkmnDesc> mList;

        PkmnDescExpandableSavedState(Parcelable superState) {
            super(superState);
        }

        protected PkmnDescExpandableSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                // ArrayList of PclbMove (PclbMove extends Move)
                mList.clear();
                mList.addAll(in
                        .createTypedArrayList(PclbPkmnDesc.CREATOR));
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            boolean isListEmpty = (mList == null || mList.isEmpty());
            out.writeByte((byte) (!isListEmpty ? 1 : 0));
            if (!isListEmpty) {
                ArrayList<PclbPkmnDesc> arrayList = new ArrayList<PclbPkmnDesc>();
                for (PkmnDesc p : mList) {
                    arrayList.add(new PclbPkmnDesc(p));
                }
                out.writeTypedList(arrayList);
            }
        }
    }
}
