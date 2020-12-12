package com.pokemongostats.view.rows;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.MoveCombination;
import com.pokemongostats.model.parcalables.PclbMoveComb;
import com.pokemongostats.view.utils.ColorUtils;

/**
 * @author Zapagon
 */
public class MoveCombRow extends LinearLayout implements ItemView<MoveCombination> {

    private TextView quickNameView;
    private TextView chargeNameView;
    private TextView ppsView;

    private MoveCombination moveComb;
    private boolean isDefender;

    public MoveCombRow(Context context) {
        super(context);
        initializeViews(null);
    }

    public MoveCombRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public MoveCombRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
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

    private void initializeViews(AttributeSet attrs) {

        inflate(getContext(), R.layout.view_row_move_comb, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        quickNameView = (TextView) findViewById(R.id.quick_move_name);
        chargeNameView = (TextView) findViewById(R.id.charge_move_name);
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

        savedState.moveComb = this.moveComb;
        savedState.isDefender = this.isDefender;

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
        this.moveComb = savedState.moveComb;
        this.isDefender = savedState.isDefender;
    }

    @Override
    public void update() {
        if (moveComb == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);

            final Move q = moveComb.getQuickMove();
            quickNameView.setText(q.getName());
            int typeColor = getResources().getColor(ColorUtils.getTypeColor(q.getType()), getContext().getTheme());
            changeStrokeColor(quickNameView.getBackground(), typeColor);

            final Move c = moveComb.getChargeMove();
            chargeNameView.setText(c.getName());
            typeColor = getResources().getColor(ColorUtils.getTypeColor(c.getType()), getContext().getTheme());
            changeStrokeColor(chargeNameView.getBackground(), typeColor);

            double pps = isDefender ? moveComb.getDefPPS() : moveComb.getAttPPS();

            ppsView.setText(String.valueOf(pps));
        }
    }

    private static void changeStrokeColor(final Drawable bg, final int color) {
        if (bg instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) bg;
            drawable.setStroke(5, color);
        } else if (bg instanceof InsetDrawable) {
            InsetDrawable drawable = (InsetDrawable) bg;
            changeStrokeColor(drawable.getDrawable(), color);
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
        private boolean isDefender;

        MoveCombinationRowSavedState(Parcelable superState) {
            super(superState);
        }

        protected MoveCombinationRowSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                this.moveComb = in.readParcelable(PclbMoveComb.class.getClassLoader());
            }
            this.isDefender = in.readByte() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (moveComb != null ? 1 : 0));
            if (moveComb != null) {
                out.writeParcelable(new PclbMoveComb(moveComb), 0);
            }
            out.writeByte((byte) (isDefender ? 1 : 0));
        }
    }
}
