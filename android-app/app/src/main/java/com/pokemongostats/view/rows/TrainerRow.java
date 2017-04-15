/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.StringUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.parcalables.PclbTrainer;
import com.pokemongostats.view.utils.ColorUtils;

/**
 * @author Zapagon
 */
public class TrainerRow extends LinearLayout
        implements
        ItemView<Trainer> {
    private Trainer trainer;

    private TextView nameView;
    private TextView lvlView;
    private TextView teamView;

    public TrainerRow(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public TrainerRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public TrainerRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(context, R.layout.view_row_trainer, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        nameView = (TextView) findViewById(R.id.trainer_name);
        lvlView = (TextView) findViewById(R.id.trainer_lvl);
        teamView = (TextView) findViewById(R.id.trainer_team);
    }

    /**
     * @return the trainer
     */
    public Trainer getTrainer() {
        return trainer;
    }

    // Save/Restore State

    /**
     * @param t the trainer to set
     */
    public void setTrainer(Trainer t) {
        trainer = t;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        TrainerRowSavedState savedState = new TrainerRowSavedState(
                superState);
        // end
        savedState.trainer = this.trainer;
        Log.d(TagUtils.SAVE, "onSaveInstanceState " + this.trainer);

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof TrainerRowSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        TrainerRowSavedState savedState = (TrainerRowSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end

        this.trainer = savedState.trainer;
        Log.d(TagUtils.SAVE, "onRestoreInstanceState " + this.trainer);
    }

    @Override
    public void update() {
        if (trainer == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            nameView.setText(trainer.getName());
            lvlView.setText(String.valueOf(trainer.getLevel()));
            teamView.setText(StringUtils.getTeamStringId(trainer.getTeam()));
            int color = getResources().getColor(ColorUtils.getTeamColor(trainer.getTeam()));
            teamView.setTextColor(color);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(final Trainer t) {
        setTrainer(t);
        update();
    }

    private static class TrainerRowSavedState extends BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Creator<TrainerRowSavedState> CREATOR = new Creator<TrainerRowSavedState>() {
            @Override
            public TrainerRowSavedState createFromParcel(Parcel in) {
                return new TrainerRowSavedState(in);
            }

            @Override
            public TrainerRowSavedState[] newArray(int size) {
                return new TrainerRowSavedState[size];
            }
        };
        private Trainer trainer;

        TrainerRowSavedState(Parcelable superState) {
            super(superState);
        }

        private TrainerRowSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                this.trainer = in.readParcelable(
                        PclbTrainer.class.getClassLoader());
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (trainer != null ? 1 : 0));
            if (trainer != null) {
                out.writeParcelable(new PclbTrainer(trainer), 0);
            }
        }
    }
}
