/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.fight.Fight;
import com.pokemongostats.model.parcalables.PclbMove;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;

/**
 * @author Zapagon
 */
public class MoveCombRow extends LinearLayout implements ItemView<MoveCombination> {

    private TextView nameView;
    private TextView ppsView;

    private MoveCombination moveComb;
    private boolean isDefender;

    public MoveCombRow(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public MoveCombRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public MoveCombRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    public MoveCombination getMoveComb() {
        return moveComb;
    }

    public void setMoveComb(MoveCombination moveComb) {
        this.moveComb = moveComb;
    }

    public boolean isDefender() {
        return isDefender;
    }

    public void setDefender(boolean defender) {
        isDefender = defender;
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        View.inflate(getContext(), R.layout.view_row_move_comb, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        nameView = (TextView) findViewById(R.id.move_name);
        ppsView = (TextView) findViewById(R.id.move_pps);
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        MoveCombinationRowSavedState savedState = new MoveCombinationRowSavedState(
                superState);
        // end
// FIXME
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof MoveCombinationRowSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        MoveCombinationRowSavedState savedState = (MoveCombinationRowSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end
// FIXME
    }

    @Override
    public void update() {
        if (moveComb == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);

            nameView.setText(moveComb.getQuickMove().getName() + "/" + moveComb.getChargeMove().getName());

            double pps = isDefender ? moveComb.getDefPPS() : moveComb.getAttPPS();

            ppsView.setText(String.valueOf(pps));
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(MoveCombination m) {
        setMoveComb(m);
        update();
    }

    protected static class MoveCombinationRowSavedState extends BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Creator<MoveCombinationRowSavedState> CREATOR = new Creator<MoveCombinationRowSavedState>() {
            @Override
            public MoveCombinationRowSavedState createFromParcel(Parcel in) {
                return new MoveCombinationRowSavedState(in);
            }

            @Override
            public MoveCombinationRowSavedState[] newArray(int size) {
                return new MoveCombinationRowSavedState[size];
            }
        };
        private MoveCombination moveComb;

        MoveCombinationRowSavedState(Parcelable superState) {
            super(superState);
        }

        protected MoveCombinationRowSavedState(Parcel in) {
            super(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }
    }
}
