/**
 *
 */
package com.pokemongostats.view.expandables;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class CustomExpandable extends RelativeLayout {

    protected TextView titleTextView;
    protected String title;
    protected LinearLayout layout;
    protected boolean isExpand = false;
    protected boolean keepExpand = false;
    protected Adapter mAdapter = null;
    protected final DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            if (isExpand) refreshViewsFromAdapter();
        }

        @Override
        public void onInvalidated() {
            layout.setVisibility(GONE);
        }
    };

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

    public CustomExpandable(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public CustomExpandable(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public CustomExpandable(Context context, AttributeSet attrs, int defStyle) {
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
                title = typedArray.getString(R.styleable.CustomExpandable_title);
                title = title == null ? "" : title;
                titleStyle = typedArray.getResourceId(R.styleable.CustomExpandable_titleStyle, android.R.style.TextAppearance_DeviceDefault);

            } finally {
                typedArray.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_custom_expandable,
                this);

        // layout
        layout = (LinearLayout) this.findViewById(R.id.layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        // title text view
        titleTextView = (TextView) this.findViewById(R.id.title);
        titleTextView.setText(title);
        titleTextView.setTextAppearance(getContext(), titleStyle);
        titleTextView.setOnClickListener(onClickExpandListener);
        titleTextView.setCompoundDrawables(getArrowDown(), null, getArrowDown(),
                null);
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
        if (!isExpand) {
            return;
        }
        isExpand = false;
        titleTextView.setCompoundDrawables(getArrowDown(), null, getArrowDown(),
                null);
        refreshViews();
    }

    public void expand() {
        if (isExpand) {
            return;
        }
        isExpand = true;
        titleTextView.setCompoundDrawables(getArrowUp(), null, getArrowUp(),
                null);
        refreshViews();
    }

    public void addToInnerLayout(View v) {
        v.setVisibility(isExpand ? VISIBLE : GONE);
        layout.addView(v);
    }

    private Drawable getArrowDown() {
        return getDropDownIcons(android.R.drawable.arrow_down_float);
    }

    private Drawable getArrowUp() {
        return getDropDownIcons(android.R.drawable.arrow_up_float);
    }

    private Drawable getDropDownIcons(final int id) {
        Drawable d = ContextCompat.getDrawable(getContext(), id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }

    // Save/Restore State

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

    protected static class CustomExpandableSavedState extends BaseSavedState {
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
    }

    public void setAdapter(Adapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        initViewsFromAdapter();
    }

    /**
     * L
     * initialize views using adapter getView method
     */
    protected void initViewsFromAdapter() {
        layout.removeAllViews();
        if (mAdapter != null && isExpand) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                layout.addView(getOrCreateChildView(i, null), i);
            }
        }
    }

    protected void refreshViews(){
        if (mAdapter == null) {
            int visibility = isExpand ? VISIBLE : GONE;
            layout.setVisibility(visibility);
            for(int i=0; i < layout.getChildCount(); ++i){
                View v = layout.getChildAt(i);
                if(v != null){
                    v.setVisibility(visibility);
                }
            }
        } else {
            refreshViewsFromAdapter();
        }
    }

    /**
     * refresh views form adapter, typically when some data where add or remove
     * to the adapter
     */
    protected void refreshViewsFromAdapter() {
        int visibility = isExpand ? VISIBLE : GONE;

        // number of child in the expandable view
        int childCount = layout.getChildCount();
        // number of data in adapter
        int adapterSize = mAdapter.getCount();
        // number of reusable view
        int reuseCount = Math.min(childCount, adapterSize);

        // for all reusable view, getView (which may update content of given
        // view)
        for (int i = 0; i < reuseCount; i++) {
            // update existing view
            View oldView = layout.getChildAt(i);
            View updatedView = getOrCreateChildView(i, oldView);
            updatedView.setVisibility(visibility);
            // if getView create a new view, delete the old and add the new one
            if (oldView == null
                    || oldView.getId() != updatedView.getId()) {
                layout.removeViewAt(i);
                layout.addView(updatedView, i);
            }
        }

        // if there is actually less views than data => create new views
        if (childCount < adapterSize) {
            for (int i = childCount; i < adapterSize; i++) {
                View newView = getOrCreateChildView(i, null);
                newView.setVisibility(visibility);
                layout.addView(newView, i);
            }
            // else if there is more views than data, hide exceeding views
        } else if (childCount > adapterSize) {
            for (int i = adapterSize; i < childCount; i++) {
                View child = layout.getChildAt(i);
                child.setVisibility(GONE);
            }
        }
        // else if same amount of visible view and data, nothing to do
    }

    protected View getOrCreateChildView(int position, View convertView) {
        return mAdapter.getView(position, convertView, layout);
    }
}
