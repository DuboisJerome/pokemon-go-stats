/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.commons.TypeViewUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class PokemonDescArrayAdapter extends BaseAdapter implements Filterable {

	private List<PokemonDescription> mFilteredList = new ArrayList<PokemonDescription>();

	private List<PokemonDescription> mFullList = new ArrayList<PokemonDescription>();

	private Context mContext;

	public PokemonDescArrayAdapter(Context context) {
		super();
		this.mContext = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		return getViewAtPosition(position, v, parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent) {
		return getViewAtPosition(position, v, parent);
	}

	@Override
	public int getCount() {
		return mFilteredList.size();
	}

	@Override
	public PokemonDescription getItem(int position) {
		return mFilteredList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Change text view color & text from Trainer at position
	 * 
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private View getViewAtPosition(int position, View v, ViewGroup parent) {

		v = LayoutInflater.from(mContext)
				.inflate(R.layout.pokemon_desc_listview, parent, false);

		TextView nameView = (TextView) v.findViewById(R.id.name);
		TextView type1View = (TextView) v.findViewById(R.id.type1);
		TextView type2View = (TextView) v.findViewById(R.id.type2);

		PokemonDescription p = getItem(position);
		if (p != null) {
			nameView.setText(p.getName());
			nameView.setTextColor(
					mContext.getResources().getColor(android.R.color.white));
			TypeViewUtils.toTypeView(type1View, mContext, p.getType1());
			if (p.getType2() != null) {
				TypeViewUtils.toTypeView(type2View, mContext, p.getType2());
			} else {
				type2View.setVisibility(View.INVISIBLE);
			}
		}

		return v;
	}

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

			// copy full list
			ArrayList<PokemonDescription> values = new ArrayList<PokemonDescription>(
					mFullList);

			// if no text in filter
			if (constraint == null || constraint.length() == 0) {
				results.values = values;
				results.count = values.size();
				// return original values
			} else {
				ArrayList<PokemonDescription> suggestions = new ArrayList<PokemonDescription>();
				// iterate over original values
				for (PokemonDescription p : values) {

					String nfdNormalizedString = Normalizer
							.normalize(p.getName(), Normalizer.Form.NFD);
					Pattern pattern = Pattern
							.compile("\\p{InCombiningDiacriticalMarks}+");
					String normalizedName = pattern.matcher(nfdNormalizedString)
							.replaceAll("");
					normalizedName = normalizedName.replaceAll("œ", "oe");
					Log.d("TEST", normalizedName);
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
			// noinspection unchecked
			mFilteredList = (List<PokemonDescription>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	};

	/**
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection
	 *            The Collection to add at the end of the array.
	 */
	public void addAll(Collection<? extends PokemonDescription> collection) {
		mFullList.addAll(collection);
	}

	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		mFullList.clear();
	}
}