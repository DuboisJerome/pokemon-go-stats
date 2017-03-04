package com.pokemongostats.view.adapters;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * @author Zapagon
 *
 */
public abstract class ItemAdapter<I> extends BaseAdapter implements Filterable {

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called
	 * whenever {@link #mFullList} is modified.
	 */
	private boolean mNotifyOnChange = true;

	private List<I> mFilteredList;

	private List<I> mFullList;

	private Context mContext;

	private boolean isFiltering;

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
		return createViewAtPosition(position, v, parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent) {
		return createViewAtPosition(position, v, parent);
	}

	protected abstract View createViewAtPosition(int position, View v,
			ViewGroup parent);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter getFilter() {
		return nameFilter;
	}

	private final Filter nameFilter = new Filter() {

		@Override
		public String convertResultToString(Object resultValue) {
			return itemToString((I) resultValue);
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			isFiltering = (constraint != null && constraint.length() > 0);
			// if no text in filter
			if (!isFiltering) {
				results.values = mFullList;
				results.count = mFullList.size();
				// return original values
			} else {
				ArrayList<I> suggestions = new ArrayList<I>();
				// iterate over original values
				for (I item : mFullList) {

					String nfdNormalizedString = Normalizer
							.normalize(itemToString(item), Normalizer.Form.NFD);
					Pattern pattern = Pattern
							.compile("\\p{InCombiningDiacriticalMarks}+");
					String normalizedName = pattern.matcher(nfdNormalizedString)
							.replaceAll("");
					normalizedName = normalizedName.replaceAll("œ", "oe");
					if (normalizedName.toLowerCase()
							.startsWith(constraint.toString().toLowerCase())) {
						suggestions.add(item);
					}
				}
				results.values = suggestions;
				results.count = suggestions.size();
				// return filtered values
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mFilteredList.clear();
			mFilteredList.addAll((List<I>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

	protected abstract String itemToString(I item);

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
		return mFilteredList.get(position);
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
	 * @param collection
	 *            The Collection to add at the end of the array.
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