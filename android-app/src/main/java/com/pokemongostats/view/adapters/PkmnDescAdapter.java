/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * @author Zapagon
 *
 */
public class PkmnDescAdapter extends BaseAdapter implements Filterable {

	private List<PokemonDescription> mFilteredList;

	private List<PokemonDescription> mFullList;

	private Context mContext;

	private boolean isFiltering;

	public PkmnDescAdapter(Context context) {
		super();
		this.mContext = context;
		this.mFilteredList = new ArrayList<PokemonDescription>();
		this.mFullList = new ArrayList<PokemonDescription>();
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
		return PkmnDescRowView.create(mContext, getItem(position));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter getFilter() {
		return nameFilter;
	}

	Filter nameFilter = new Filter() {

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

					String nfdNormalizedString = Normalizer
							.normalize(p.getName(), Normalizer.Form.NFD);
					Pattern pattern = Pattern
							.compile("\\p{InCombiningDiacriticalMarks}+");
					String normalizedName = pattern.matcher(nfdNormalizedString)
							.replaceAll("");
					normalizedName = normalizedName.replaceAll("œ", "oe");
					if (normalizedName.toLowerCase()
							.startsWith(constraint.toString().toLowerCase())) {
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
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mFilteredList.clear();
			mFilteredList.addAll((List<PokemonDescription>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

	public int getPosition(PokemonDescription item) {
		int result = AdapterView.INVALID_POSITION;
		if (item != null && mFilteredList != null && !mFilteredList.isEmpty()) {
			ArrayList<PokemonDescription> cpy = new ArrayList<PokemonDescription>(
					mFilteredList);
			for (PokemonDescription pkmn : cpy) {
				if (item.getPokedexNum() == pkmn
						.getPokedexNum()) { return mFilteredList
								.indexOf(pkmn); }
			}
		}

		return result;
	}

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
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection
	 *            The Collection to add at the end of the array.
	 */
	public <P extends PokemonDescription> void add(P p) {
		mFullList.add(p);
		mFilteredList.clear();
		mFilteredList.addAll(mFullList);
		notifyDataSetChanged();
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
		notifyDataSetChanged();
	}

	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		mFullList.clear();
		mFilteredList.clear();
		notifyDataSetChanged();
	}

	/**
	 * @return all filtered items
	 */
	public List<? extends PokemonDescription> getAll() {
		return mFilteredList;
	}
}