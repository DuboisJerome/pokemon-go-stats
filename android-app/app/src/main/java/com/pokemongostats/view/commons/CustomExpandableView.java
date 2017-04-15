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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.ObservableImpl;
import com.pokemongostats.view.listeners.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class CustomExpandableView extends LinearLayout implements Observable {

    private TextView titleTextView;
    private String title;
    private List<View> listExpandableView = new ArrayList<>();
    private boolean isExpand = false;
    private boolean keepExpand = false;
    private Drawable icClosed, icOpened;
    private OnClickListener onClickExpandListener = new OnClickListener() {

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

        LayoutInflater.from(context).inflate(R.layout.view_header_dropdown,
                this);
        setOrientation(HORIZONTAL);

        icClosed = ContextCompat.getDrawable(getContext(), R.drawable.pokeball_close);
        icClosed.setBounds(0, 0, 50, 50);

        icOpened = ContextCompat.getDrawable(getContext(), R.drawable.pokeball_open);
        icOpened.setBounds(0, 0, 50, 50);

        // title text view
        titleTextView = (TextView) this.findViewById(R.id.title);
        titleTextView.setText(title);
        titleTextView.setTextAppearance(getContext(), titleStyle);
        titleTextView.setOnClickListener(onClickExpandListener);
        titleTextView.setCompoundDrawables(icClosed, null, icClosed,
                null);

    }

    public void addExpandableView(View v) {
        if (v == null) {
            return;
        }
        this.listExpandableView.add(v);
        v.setVisibility(isExpand || keepExpand ? VISIBLE : GONE);
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
        titleTextView.setCompoundDrawables(icClosed, null, icClosed,
                null);
        for (View v : listExpandableView) {
            v.setVisibility(GONE);
        }
        notifyObservers();
    }

    public void expand() {
        if (isExpand) {
            return;
        }
        isExpand = true;
        titleTextView.setCompoundDrawables(icOpened, null, icOpened,
                null);
        for (View v : listExpandableView) {
            v.setVisibility(VISIBLE);
        }
        notifyObservers();
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

    public boolean isExpand() {
        return isExpand;
    }

    private final Observable observableImpl = new ObservableImpl(this);
    @Override
    public void registerObserver(Observer o) {
        observableImpl.registerObserver(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observableImpl.unregisterObserver(o);
    }

    @Override
    public void notifyObservers() {
        observableImpl.notifyObservers();
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