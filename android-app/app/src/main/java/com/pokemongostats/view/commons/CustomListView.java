package com.pokemongostats.view.commons;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;

/**
 * Created by Zapagon on 24/03/2017.
 * Lazy loading
 */
public class CustomListView extends LinearLayoutCompat{

    protected Adapter mAdapter = null;

    protected final DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            if(getVisibility() == VISIBLE) refreshViewsFromAdapter();
        }

        @Override
        public void onInvalidated() {
            CustomListView.this.setVisibility(GONE);
        }
    };

    public CustomListView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
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
     * initialize views using adapter getView method
     */
    protected void initViewsFromAdapter() {
        this.removeAllViews();
        if (mAdapter != null && getVisibility() == VISIBLE) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                this.addView(getOrCreateChildView(i, null), i);
            }
        }
    }

    /**
     * refresh views form adapter, typically when some data where add or remove
     * to the adapter
     */
    protected void refreshViewsFromAdapter() {
        // number of child in the expandable view
        int childCount = this.getChildCount();
        // number of data in adapter
        int adapterSize = mAdapter.getCount();
        // number of reusable view
        int reuseCount = Math.min(childCount, adapterSize);

        // for all reusable view, getView (which may update content of given
        // view)
        for (int i = 0; i < reuseCount; i++) {
            // update existing view
            View oldView = this.getChildAt(i);
            View updatedView = getOrCreateChildView(i, oldView);
            updatedView.setVisibility(getVisibility());
            // if getView create a new view, delete the old and add the new one
            if (oldView == null
                    || oldView.getId() != updatedView.getId()) {
                this.removeViewAt(i);
                this.addView(updatedView, i);
            }
        }

        // if there is actually less views than data => create new views
        if (childCount < adapterSize) {
            for (int i = childCount; i < adapterSize; i++) {
                View newView = getOrCreateChildView(i, null);
                newView.setVisibility(getVisibility());
                this.addView(newView, i);
            }
            // else if there is more views than data, hide exceeding views
        } else if (childCount > adapterSize) {
            for (int i = adapterSize; i < childCount; i++) {
                View child = this.getChildAt(i);
                child.setVisibility(GONE);
            }
        }
        // else if same amount of visible view and data, nothing to do
    }

    protected View getOrCreateChildView(int position, View convertView) {
        return mAdapter.getView(position, convertView, this);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == VISIBLE && mAdapter != null){
            refreshViewsFromAdapter();
        }
    }
}
