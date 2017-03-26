/**
 *
 */
package com.pokemongostats.view.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pokemongostats.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class CustomExpandableView extends LinearLayoutCompat {

    private TextView titleTextView;
    private String title;
    private View expandableView = null;
    private boolean isExpand = false;
    private boolean keepExpand = false;
    private List<ExpandableListener> mListeners = new ArrayList<>();
    protected OnClickListener onClickExpandListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isExpand && !keepExpand) {
                retract();
            } else {
                expand();
            }
        }
    };

    public CustomExpandableView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public CustomExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public CustomExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        title = "";
        int titleStyle = -1;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    new int[]{R.attr.title, R.attr.titleStyle}, 0, 0);
            try {
                title = typedArray.getString(R.styleable.CustomExpandableView_title);
                title = title == null ? "" : title;
                titleStyle = typedArray.getResourceId(R.styleable.CustomExpandableView_titleStyle, android.R.style.TextAppearance_DeviceDefault);

            } finally {
                typedArray.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_custom_expandable,
                this);

        setOrientation(VERTICAL);

        // title text view
        titleTextView = (TextView) this.findViewById(R.id.title);
        titleTextView.setText(title);
        titleTextView.setTextAppearance(getContext(), titleStyle);
        titleTextView.setOnClickListener(onClickExpandListener);
        titleTextView.setCompoundDrawables(getArrowDown(), null, getArrowDown(),
                null);
    }

    public void setExpandableView(View expandableView) {
        this.expandableView = expandableView;
        if (this.expandableView != null) {
            this.expandableView.setVisibility(isExpand || keepExpand ? VISIBLE : GONE);
        }
    }

    /**
     * @param keepExpand the keepExpand to set
     */
    public void setKeepExpand(boolean keepExpand) {
        this.keepExpand = keepExpand;
        if (keepExpand) {
            titleTextView.setCompoundDrawables(null, null, null, null);
        }
    }

    public void retract() {
        if (!isExpand || keepExpand) {
            return;
        }
        isExpand = false;
        titleTextView.setCompoundDrawables(getArrowDown(), null, getArrowDown(),
                null);
        if (expandableView != null) {
            expandableView.setVisibility(GONE);
            for (ExpandableListener l : mListeners) {
                l.onRetract();
            }
        }
    }

    public void expand() {
        if (isExpand) {
            return;
        }
        isExpand = true;
        titleTextView.setCompoundDrawables(getArrowUp(), null, getArrowUp(),
                null);
        if (expandableView != null) {
            expandableView.setVisibility(VISIBLE);
            for (ExpandableListener l : mListeners) {
                l.onExpand();
            }
        }
    }

    private Drawable getArrowDown() {
        return getDropDownIcons(R.drawable.pokeball_close);
    }

    private Drawable getArrowUp() {
        return getDropDownIcons(R.drawable.pokeball_open);
    }

    private Drawable getDropDownIcons(final int id) {
        Drawable d = ContextCompat.getDrawable(getContext(), id);
        d.setBounds(0, 0, 50, 50);
        return d;
    }

    public void addExpandableListener(final ExpandableListener l) {
        this.mListeners.add(l);
    }

    public void removeExpandableListener(final ExpandableListener l) {
        this.mListeners.remove(l);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        CustomExpandableSavedState savedState = new CustomExpandableSavedState(
                superState);
        // end

        savedState.isExpand = this.isExpand;
        savedState.keepExpand = this.keepExpand;

        return savedState;
    }

    // Save/Restore State

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof CustomExpandableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        CustomExpandableSavedState savedState = (CustomExpandableSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end

        boolean isExpandSaved = savedState.isExpand;
        if (isExpandSaved) {
            this.expand();
        } else {
            this.retract();
        }
        this.setKeepExpand(savedState.keepExpand);
    }

    interface ExpandableListener {
        void onExpand();

        void onRetract();
    }

    protected static class CustomExpandableSavedState extends BaseSavedState {
        // required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<CustomExpandableSavedState> CREATOR = new Parcelable.Creator<CustomExpandableSavedState>() {
            @Override
            public CustomExpandableSavedState createFromParcel(Parcel in) {
                return new CustomExpandableSavedState(in);
            }

            @Override
            public CustomExpandableSavedState[] newArray(int size) {
                return new CustomExpandableSavedState[size];
            }
        };
        private boolean isExpand;
        private boolean keepExpand;

        CustomExpandableSavedState(Parcelable superState) {
            super(superState);
        }

        protected CustomExpandableSavedState(Parcel in) {
            super(in);
            this.isExpand = in.readByte() != 0;
            this.keepExpand = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (isExpand ? 1 : 0));
            out.writeByte((byte) (keepExpand ? 1 : 0));
        }
    }
}