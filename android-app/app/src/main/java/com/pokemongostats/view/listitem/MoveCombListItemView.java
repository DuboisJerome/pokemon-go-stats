/**
 *
 */
package com.pokemongostats.view.listitem;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.model.parcalables.PclbMove;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class MoveCombListItemView extends CustomListItemView<MoveCombination> {

    public MoveCombListItemView(Context context) {
        super(context);
    }

    public MoveCombListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveCombListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        MoveExpandableSavedState savedState = new MoveExpandableSavedState(
                superState);
        // end
        // savedState.mList = this.mListItem;

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

        // this.mListItem = savedState.mList;

    }

    protected static class MoveExpandableSavedState
            extends
            BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Creator<MoveExpandableSavedState> CREATOR = new Creator<MoveExpandableSavedState>() {
            @Override
            public MoveExpandableSavedState createFromParcel(Parcel in) {
                return new MoveExpandableSavedState(in);
            }

            @Override
            public MoveExpandableSavedState[] newArray(int size) {
                return new MoveExpandableSavedState[size];
            }
        };
        private List<MoveCombination> mList;

        MoveExpandableSavedState(Parcelable superState) {
            super(superState);
        }

        protected MoveExpandableSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                // ArrayList of PclbMove (PclbMove extends Move)
                mList.clear();
                // FIXME mList.addAll(in.createTypedArrayList(PclbMove.CREATOR));
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            boolean isListEmpty = (mList == null || mList.isEmpty());
            out.writeByte((byte) (!isListEmpty ? 1 : 0));
            if (!isListEmpty) {
                ArrayList<PclbMove> arrayList = new ArrayList<PclbMove>();
//                FIXME for (Move m : mList) {
//                    arrayList.add(new PclbMove(m));
//                }
                out.writeTypedList(arrayList);
            }
        }
    }
}
