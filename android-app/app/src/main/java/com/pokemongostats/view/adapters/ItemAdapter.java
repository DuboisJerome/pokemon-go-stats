package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Zapagon
 */
public abstract class ItemAdapter<I> extends BaseAdapter implements Filterable {

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called
     * whenever {@link #mFullList} is modified.
     */
    protected boolean mNotifyOnChange = true;

    protected List<I> mFilteredList;

    protected List<I> mFullList;

    protected Context mContext;

    public ItemAdapter(Context context) {
        super();
        this.mContext = context;
        this.mFilteredList = new ArrayList<I>();
        this.mFullList = new ArrayList<I>();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View created = createViewAtPosition(position, v, parent);


        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View v, ViewGroup parent) {
        View created = createViewAtPosition(position, v, parent);

        return created;
    }

    protected abstract View createViewAtPosition(int position, View v,
                                                 ViewGroup parent);

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I getItem(int position) {
        return mFilteredList.size() > 0 ? mFilteredList.get(position) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        setNotifyOnChange(true);
    }

    public void add(I item) {
        mFullList.add(item);
        mFilteredList.clear();
        mFilteredList.addAll(mFullList);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends I> collection) {
        mFullList.addAll(collection);
        mFilteredList.clear();
        mFilteredList.addAll(mFullList);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        mFullList.clear();
        mFilteredList.clear();
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * @return all filtered items
     */
    public List<? extends I> getAll() {
        return mFilteredList;
    }

    /**
     * Sort
     *
     * @param c
     */
    public void sort(final Comparator<? super I> c) {
        Collections.sort(mFullList, c);
        Collections.sort(mFilteredList, c);
        if (mNotifyOnChange) notifyDataSetChanged();
    }
}