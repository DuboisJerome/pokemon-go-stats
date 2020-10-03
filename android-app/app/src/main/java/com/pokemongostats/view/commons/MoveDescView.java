/**
 *
 */
package com.pokemongostats.view.commons;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbMove;
import com.pokemongostats.view.listeners.HasTypeSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.TypeRow;

/**
 * @author Zapagon
 */
public class MoveDescView extends RelativeLayout implements HasTypeSelectable {

    private Move mMove;

    private TableLabelTextFieldView mFieldName;
    private TableLabelFieldView mFieldType;
    private TableLabelTextFieldView mFieldMoveType;
    private TableLabelTextFieldView mFieldPower;
    private TableLabelTextFieldView mFieldDuration;
    private TableLabelTextFieldView mFieldPPS;
    private TableLabelTextFieldView mFieldEnergieDelta;

    private SelectedVisitor<Type> mCallbackType;

    public MoveDescView(Context context) {
        super(context);
        initializeViews(null);
    }

    public MoveDescView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public MoveDescView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    public static MoveDescView create(Context context, Move m) {
        MoveDescView v = new MoveDescView(context);
        v.setMove(m);
        return v;
    }

    private void initializeViews(final AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(getContext(), R.layout.view_move_desc, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        mFieldName = (TableLabelTextFieldView) findViewById(
                R.id.move_name_field);
        mFieldType = (TableLabelFieldView) findViewById(R.id.move_type_field);
        mFieldMoveType = (TableLabelTextFieldView) findViewById(
                R.id.move_move_type_field);
        mFieldPower = (TableLabelTextFieldView) findViewById(
                R.id.move_power_field);
        mFieldDuration = (TableLabelTextFieldView) findViewById(
                R.id.move_duration_field);
        mFieldPPS = (TableLabelTextFieldView) findViewById(R.id.move_pps_field);
        mFieldEnergieDelta = (TableLabelTextFieldView) findViewById(
                R.id.move_energy_delta_field);

        setVisibility(View.GONE);
    }

    /**
     * @return the move
     */
    public Move getMove() {
        return mMove;
    }

    public void setMove(final Move m) {
        mMove = m;
        if (m == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            //
            TypeRow type = new TypeRow(getContext());
            type.setType(m.getType());
            type.update();
            type.setVisibility(View.VISIBLE);
            type.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallbackType == null) {
                        return;
                    }
                    mCallbackType.select(m.getType());
                }
            });
            // pps
            double pps = Math.floor(FightUtils.computePPS(m) * 100) / 100;

            String moveTypeStr = "";
            MoveType moveType = m.getMoveType();
            String energyTitle = "";
            if (moveType != null) {
                switch (moveType) {
                    case CHARGE:
                        moveTypeStr = getResources()
                                .getString(R.string.chargemove);
                        energyTitle = getResources()
                                .getString(R.string.energy_cost);
                        break;
                    case QUICK:
                        moveTypeStr = getResources()
                                .getString(R.string.quickmove);
                        energyTitle = getResources()
                                .getString(R.string.energy_gain);
                        break;
                    default:
                        break;
                }
            }

            mFieldName.setFieldText(m.getName());
            mFieldType.setField(type);
            mFieldMoveType.setFieldText(moveTypeStr);
            mFieldPower.setFieldText(String.valueOf(m.getPower()));
            mFieldDuration.setFieldText(String.valueOf(m.getDuration()));
            mFieldPPS.setFieldText(String.valueOf(pps));
            mFieldEnergieDelta.setLabelText(energyTitle);
            mFieldEnergieDelta
                    .setFieldText(String.valueOf(Math.abs(m.getEnergyDelta())));
        }
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        MoveDescViewSavedState savedState = new MoveDescViewSavedState(
                superState);
        // end
        savedState.move = this.mMove;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof MoveDescViewSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        MoveDescViewSavedState savedState = (MoveDescViewSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end

        this.mMove = savedState.move;
    }

    @Override
    public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor) {
        this.mCallbackType = visitor;
    }

    protected static class MoveDescViewSavedState extends BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<MoveDescViewSavedState> CREATOR = new Parcelable.Creator<MoveDescViewSavedState>() {
            @Override
            public MoveDescViewSavedState createFromParcel(Parcel in) {
                return new MoveDescViewSavedState(in);
            }

            @Override
            public MoveDescViewSavedState[] newArray(int size) {
                return new MoveDescViewSavedState[size];
            }
        };
        private Move move;

        MoveDescViewSavedState(Parcelable superState) {
            super(superState);
        }

        protected MoveDescViewSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                this.move = in.readParcelable(PclbMove.class.getClassLoader());
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (move != null ? 1 : 0));
            if (move != null) {
                out.writeParcelable(new PclbMove(move),
                        Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            }
        }
    }
}
