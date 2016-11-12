/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.PkmnDescRowView;

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
public class PkmnDescAdapter extends BaseAdapter implements Filterable, HasPkmnDescSelectable {

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called
	 * whenever {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	private List<PokemonDescription> mFilteredList;

	private List<PokemonDescription> mFullList;

	private Context mContext;

	private boolean isFiltering;

	private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

	private boolean isBaseAttVisible;
	private boolean isBaseDefVisible;
	private boolean isBaseStaminaVisible;
	private boolean isMaxCPVisible;

	public PkmnDescAdapter(Context context) {
		super();
		this.mContext = context;
		this.mFilteredList = new ArrayList<PokemonDescription>();
		this.mFullList = new ArrayList<PokemonDescription>();
	}

	/**
	 * @return the isBaseAttVisible
	 */
	public boolean isBaseAttVisible() {
		return isBaseAttVisible;
	}

	/**
	 * @param isBaseAttVisible
	 *            the isBaseAttVisible to set
	 */
	public void setBaseAttVisible(boolean isBaseAttVisible) {
		this.isBaseAttVisible = isBaseAttVisible;
	}

	/**
	 * @return the isBaseDefVisible
	 */
	public boolean isBaseDefVisible() {
		return isBaseDefVisible;
	}

	/**
	 * @param isBaseDefVisible
	 *            the isBaseDefVisible to set
	 */
	public void setBaseDefVisible(boolean isBaseDefVisible) {
		this.isBaseDefVisible = isBaseDefVisible;
	}

	/**
	 * @return the isBaseStaminaVisible
	 */
	public boolean isBaseStaminaVisible() {
		return isBaseStaminaVisible;
	}

	/**
	 * @param isBaseStaminaVisible
	 *            the isBaseStaminaVisible to set
	 */
	public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
		this.isBaseStaminaVisible = isBaseStaminaVisible;
	}

	/**
	 * @return the isMaxCPVisible
	 */
	public boolean isMaxCPVisible() {
		return isMaxCPVisible;
	}

	/**
	 * @param isMaxCPVisible
	 *            the isMaxCPVisible to set
	 */
	public void setMaxCPVisible(boolean isMaxCPVisible) {
		this.isMaxCPVisible = isMaxCPVisible;
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

	/**
	 * Change text view color & text from Trainer at position
	 * 
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private View createViewAtPosition(int position, View v, ViewGroup parent) {
		PokemonDescription p = getItem(position);
		if (p == null) { return v; }

		final PkmnDescRowView view;
		if (v == null || !(v instanceof PkmnDescRowView)) {
			view = PkmnDescRowView.create(mContext, p);
		} else {
			view = (PkmnDescRowView) v;
			if (!p.equals(view.getPkmnDesc())) {
				view.setPkmnDesc(p);
				if (mCallbackPkmnDesc != null) {
					view.setOnClickListener(new OnClickItemListener<PokemonDescription>(mCallbackPkmnDesc, p));
				}
			}
		}
		view.setBaseAttVisible(isBaseAttVisible);
		view.setBaseDefVisible(isBaseDefVisible);
		view.setBaseStaminaVisible(isBaseStaminaVisible);
		view.setMaxCPVisible(isMaxCPVisible);
		return view;
	}

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
			return ((PokemonDescription) resultValue).getName();
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
				ArrayList<PokemonDescription> suggestions = new ArrayList<PokemonDescription>();
				// iterate over original values
				for (PokemonDescription p : mFullList) {

					String nfdNormalizedString = Normalizer.normalize(p.getName(), Normalizer.Form.NFD);
					Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
					String normalizedName = pattern.matcher(nfdNormalizedString).replaceAll("");
					normalizedName = normalizedName.replaceAll("œ", "oe");
					if (normalizedName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
						suggestions.add(p);
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
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mFilteredList.clear();
			mFilteredList.addAll((List<PokemonDescription>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

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
	public PokemonDescription getItem(int position) {
		return mFilteredList.get(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Control whether methods that change the list ({@link #add},
	 * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
	 * {@link #notifyDataSetChanged}. If set to false, caller must manually call
	 * notifyDataSetChanged() to have the changes reflected in the attached
	 * view.
	 *
	 * The default is true, and calling notifyDataSetChanged() resets the flag
	 * to true.
	 *
	 * @param notifyOnChange
	 *            if true, modifications to the list will automatically call
	 *            {@link #notifyDataSetChanged}
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
		mNotifyOnChange = notifyOnChange;
	}

	/**
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection
	 *            The Collection to add at the end of the array.
	 */
	public void add(PokemonDescription p) {
		mFullList.add(p);
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
	public void addAll(Collection<? extends PokemonDescription> collection) {
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
	public List<? extends PokemonDescription> getAll() {
		return mFilteredList;
	}

	/**
	 * Sort
	 * 
	 * @param c
	 */
	public void sort(final Comparator<? super PokemonDescription> c) {
		Collections.sort(mFullList, c);
		Collections.sort(mFilteredList, c);
		if (mNotifyOnChange) notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptSelectedVisitorPkmnDesc(SelectedVisitor<PokemonDescription> visitor) {
		this.mCallbackPkmnDesc = visitor;
	}
}