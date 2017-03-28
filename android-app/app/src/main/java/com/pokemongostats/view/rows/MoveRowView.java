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
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.parcalables.PclbMove;
import com.pokemongostats.view.parcalables.PclbPkmnDesc;

/**
 * @author Zapagon
 */
public class MoveRowView extends LinearLayout implements ItemView<Move> {

    private TextView nameView;
    private TypeRowView typeView;
    private TextView powerView;
    private TextView dpsView;
    private TextView speedView;

    private Move move;
    private PkmnDesc owner;

    public MoveRowView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public MoveRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public MoveRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        View.inflate(getContext(), R.layout.view_row_move, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        nameView = (TextView) findViewById(R.id.move_name);
        typeView = (TypeRowView) findViewById(R.id.move_type);
        powerView = (TextView) findViewById(R.id.move_power);
        dpsView = (TextView) findViewById(R.id.move_dps);
        speedView = (TextView) findViewById(R.id.move_duration);
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
    public PkmnDesc getPkmnOwner() {
        return owner;
    }

    public void setPkmnMove(PkmnDesc owner, Move m) {
        this.owner = owner;
        this.move = m;
    }

    /**
     * @param isDPSVisible the isDPSVisible to set
     */
    public void setDPSVisible(boolean isDPSVisible) {
        this.dpsView.setVisibility(isDPSVisible ? VISIBLE : GONE);
    }

    /**
     * @param isPowerVisible the isPowerVisible to set
     */
    public void setPowerVisible(boolean isPowerVisible) {
        this.powerView.setVisibility(isPowerVisible ? VISIBLE : GONE);
    }

    /**
     * @param isSpeedVisible the isSpeedVisible to set
     */
    public void setSpeedVisible(boolean isSpeedVisible) {
        this.speedView.setVisibility(isSpeedVisible ? VISIBLE : GONE);
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

    @Override
    public void update() {
        if (move == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            Type type = move.getType();
            nameView.setText(move.getName());

            typeView.setType(type);
            typeView.update();

            // if owner print stab if necessary
            int dpsColorId = android.R.color.white;
            double dps = Math.floor(FightUtils.computeDPS(move, owner) * 100) / 100;
            if (FightUtils.isSTAB(move, owner)) {
                dpsColorId = R.color.stab_dps;
            }

            powerView.setText(String.valueOf(move.getPower()));

            dpsView.setText(String.valueOf(dps));
            dpsView.setTextColor(
                    getContext().getResources().getColor(dpsColorId));

            speedView.setText(String.valueOf(move.getDuration()));
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(Move m) {
        setPkmnMove(null, m);
        update();
    }

    protected static class MoveRowViewSavedState extends BaseSavedState {

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
        private Move move;
        private PkmnDesc owner;

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
                        PclbPkmnDesc.class.getClassLoader());
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
                out.writeParcelable(new PclbPkmnDesc(owner), 0);
            }
        }
    }
}
