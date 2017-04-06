package com.pokemongostats.view.rows;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.utils.PreferencesUtils;

public class TypeRowView extends RelativeLayout implements ItemView<Type> {

    private Type mType;
    private TextView mTextView;
    private Drawable backgroundDrawable;

    private boolean isShowEvenIfEmpty = false;

    public TypeRowView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public TypeRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public TypeRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    public static int getNameId(final Type type) {
        if (type == null) {
            return R.string.none;
        }
        switch (type) {
            case BUG:
                return R.string.bug;
            case DARK:
                return R.string.dark;
            case DRAGON:
                return R.string.dragon;
            case ELECTRIC:
                return R.string.electric;
            case FAIRY:
                return R.string.fairy;
            case FIGHTING:
                return R.string.fighting;
            case FIRE:
                return R.string.fire;
            case FLYING:
                return R.string.flying;
            case GHOST:
                return R.string.ghost;
            case GRASS:
                return R.string.grass;
            case GROUND:
                return R.string.ground;
            case ICE:
                return R.string.ice;
            case POISON:
                return R.string.poison;
            case PSYCHIC:
                return R.string.psychic;
            case ROCK:
                return R.string.rock;
            case STEEL:
                return R.string.steel;
            case WATER:
                return R.string.water;
            case NORMAL:
                return R.string.normal;
            default:
                return R.string.none;
        }
    }

    public void setShowEvenIfEmpty(boolean showEvenIfEmpty) {
        isShowEvenIfEmpty = showEvenIfEmpty;
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_row_type, this);

        mTextView = (TextView) findViewById(R.id.type_name);
        int savedDrawableId = PreferencesUtils.getStyleId(getContext());
        backgroundDrawable = ContextCompat.getDrawable(
                getContext(), savedDrawableId);
        backgroundDrawable.mutate();

        setBackground(backgroundDrawable);
        setVisibility(View.GONE);
    }

    /**
     * @return the type
     */
    public Type getType() {
        return mType;
    }

    /**
     * @param t the type to set
     */
    public void setType(Type t) {
        mType = t;
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        TypeRowViewSavedState savedState = new TypeRowViewSavedState(
                superState);
        // end
        savedState.type = this.mType;
        Log.d(TagUtils.SAVE, "onSaveInstanceState " + this.mType);

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof TypeRowViewSavedState)) {
            super.onRestoreInstanceState(state);
            Log.d(TagUtils.SAVE, "onRestoreInstanceState state not instance of TypeRowViewSavedState");
            return;
        }

        TypeRowViewSavedState savedState = (TypeRowViewSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end

        // !!! save state depends de l'id de la view
        if (this.mType == null) {
            this.mType = savedState.type;
        }

        Log.d(TagUtils.SAVE, "onRestoreInstanceState " + this.mType + " " + this.getId());
    }

    @Override
    public void update() {
        if (mType == null) {
            if (isShowEvenIfEmpty) {
                mTextView.setText(getContext().getString(R.string.none));
                updateBackground();
                setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.GONE);
            }
        } else {
            setVisibility(View.VISIBLE);

            mTextView.setText(getNameId(mType));

            updateBackground();
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(Type t) {
        setType(t);
        update();
    }

    protected static class TypeRowViewSavedState extends BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<TypeRowViewSavedState> CREATOR = new Parcelable.Creator<TypeRowViewSavedState>() {
            @Override
            public TypeRowViewSavedState createFromParcel(Parcel in) {
                return new TypeRowViewSavedState(in);
            }

            @Override
            public TypeRowViewSavedState[] newArray(int size) {
                return new TypeRowViewSavedState[size];
            }
        };
        private Type type = null;

        TypeRowViewSavedState(Parcelable superState) {
            super(superState);
        }

        protected TypeRowViewSavedState(Parcel in) {
            super(in);
            this.type = Type.valueOfIgnoreCase(in.readString());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(type == null ? "" : type.name());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateBackground();
    }

    private void updateBackground() {
        if (backgroundDrawable == null) {
            return;
        }
        if (backgroundDrawable instanceof GradientDrawable) {
            int color;
            if (isEnabled()) {
                color = getContext().getResources().getColor(PreferencesUtils.getColorId(mType));
            } else {
                color = getContext().getResources().getColor(android.R.color.transparent);
            }
            ((GradientDrawable) backgroundDrawable).setColor(color);
        } else {
            mTextView.setText("No Gradient");
        }
    }
}
