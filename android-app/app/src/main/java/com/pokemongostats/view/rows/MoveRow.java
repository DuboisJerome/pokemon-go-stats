package com.pokemongostats.view.rows;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbMove;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.utils.ColorUtils;

/**
 * @author Zapagon
 */
public class MoveRow extends AbstractMoveRow implements ItemView<Move> {

    private TextView nameView;
    private Move move;
    private PkmnDesc owner;

    public MoveRow(Context context) {
        super(context);
        initializeViews(null);
    }

    public MoveRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public MoveRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(AttributeSet attrs) {

        inflate(getContext(), R.layout.view_row_move, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        nameView = findViewById(R.id.move_name);

        initializeViewsEnd(attrs);
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
    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        MoveRowSavedState savedState = new MoveRowSavedState(
                superState);
        // end
        savedState.move = this.move;
        savedState.owner = this.owner;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof MoveRowSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        MoveRowSavedState savedState = (MoveRowSavedState) state;
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

            final int typeColor = getResources().getColor(ColorUtils.getTypeColor(type), getContext().getTheme());

            final Drawable bgNameView = nameView.getBackground();
            if (bgNameView instanceof GradientDrawable) {
                GradientDrawable drawable = (GradientDrawable) bgNameView;
                drawable.setStroke(5, typeColor);
            }

            // if owner print stab if necessary
            int ppsColorId = android.R.color.white;
            double pps = Math.floor(FightUtils.computePowerPerSecond(move, owner) * 100) / 100;
            if (FightUtils.isSTAB(move, owner)) {
                ppsColorId = R.color.stab_text_color;
            }

            powerView.setText(String.valueOf(move.getPower()));
            energyView.setText(String.valueOf(move.getEnergyDelta()));
            powerPerSecondView.setText(String.valueOf(pps));
            powerPerSecondView.setTextColor(
                    getContext().getResources().getColor(ppsColorId, getContext().getTheme()));
            durationView.setText(String.valueOf(move.getDuration()));

            int dptXEptColorId = android.R.color.white;
            double dptXEpt = Math.floor(FightUtils.computePowerEnergyPerTurn(move, owner) * 100) / 100;
            if (FightUtils.isSTAB(move, owner)) {
                dptXEptColorId = R.color.stab_text_color;
            }

            energyPvpPerTurnView.setText(String.valueOf(FightUtils.computeEnergyPerTurn(move)));
            powerPvpPerTurnView.setText(String.valueOf(FightUtils.computePowerPerTurn(move)));
            dptXEptView.setText(String.valueOf(dptXEpt));
            dptXEptView.setTextColor(
                    getContext().getResources().getColor(dptXEptColorId, getContext().getTheme()));
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

    protected static class MoveRowSavedState extends BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<MoveRowSavedState> CREATOR = new Parcelable.Creator<MoveRowSavedState>() {
            @Override
            public MoveRowSavedState createFromParcel(Parcel in) {
                return new MoveRowSavedState(in);
            }

            @Override
            public MoveRowSavedState[] newArray(int size) {
                return new MoveRowSavedState[size];
            }
        };
        private Move move;
        private PkmnDesc owner;

        MoveRowSavedState(Parcelable superState) {
            super(superState);
        }

        protected MoveRowSavedState(Parcel in) {
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
