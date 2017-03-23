/**
 * 
 */
package com.pokemongostats.view.fragments;

import java.util.Comparator;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.commons.HasTitle;
import com.pokemongostats.view.dialogs.FilterMoveDialogFragment;
import com.pokemongostats.view.dialogs.FilterPokemonDialogFragment;
import com.pokemongostats.view.listeners.HasMoveSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class MoveListFragment
		extends
			HistorizedFragment<MoveListFragment.SortChoice>
		implements
			HasMoveSelectable {

	private static final String MOVE_LIST_FRAGMENT_KEY = "MOVE_LIST_FRAGMENT_KEY";

	public enum SortChoice implements HasTitle {
		COMPARE_BY_DPS(R.string.sort_by_dps),
		//
		COMPARE_BY_POWER(R.string.sort_by_power),
		//
		COMPARE_BY_DURATION(R.string.sort_by_duration),
		//
		COMPARE_BY_NAME(R.string.sort_by_name);
		//
		// COMPARE_BY_ENERGY(R.string.sort_by_max_cp);

		private int idLabel;

		private SortChoice(final int idLabel) {
			this.idLabel = idLabel;
		}

		@Override
		public String getTitle(Context c) {
			if (c == null) { return ""; }
			return c.getString(idLabel);
		}
	}

	private Spinner spinnerSortChoice;
	private ArrayAdapter<SortChoice> adapterSortChoice;

	private ListView listViewMoves;
	private MoveAdapter adapterMoves;

	private SelectedVisitor<Move> mCallbackMove;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapterSortChoice = new ArrayAdapter<SortChoice>(getActivity(),
				android.R.layout.simple_spinner_item, SortChoice.values()) {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				return initText(position, v);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				return initText(position, v);
			}

			private View initText(int position, View v) {
				try {
					TextView text = (TextView) v;
					SortChoice sortChoice = getItem(position);
					text.setText(getString(sortChoice.idLabel));
				} catch (Exception e) {
					Log.e(MoveListFragment.class.getName(),
							"Error spinner sort choice", e);
				}
				return v;
			}
		};
		adapterSortChoice.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		PkmnGoStatsApplication app = (PkmnGoStatsApplication) getActivity()
				.getApplicationContext();
		adapterMoves = new MoveAdapter(getActivity());
		adapterMoves.addAll(app.getMoves());
		adapterMoves.acceptSelectedVisitorMove(mCallbackMove);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		currentView = inflater.inflate(R.layout.fragment_item_list, container,
				false);

		spinnerSortChoice = (Spinner) currentView
				.findViewById(R.id.list_sort_choice);
		spinnerSortChoice.setAdapter(adapterSortChoice);
		spinnerSortChoice.setSelection(0, false);
		spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

		ImageButton searchBtn = (ImageButton) currentView.findViewById(R.id.search_button) ;
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Filter.FilterListener filterListener = new Filter.FilterListener() {
					@Override
					public void onFilterComplete(int i) {
						// TODO hide waiting popup
					}
				};
                FilterMoveDialogFragment filterDialog = new FilterMoveDialogFragment();
				filterDialog.setOnFilterMove(new FilterMoveDialogFragment.OnFilterMove() {
					@Override
					public void onFilter(final MoveFilterInfo infos) {
						if(infos == null){ return; }
						// TODO show waiting popup
						adapterMoves.getFilter().filter(infos.toStringFilter(), filterListener);
					}
				});
				filterDialog.show(getFragmentManager(), "FilterMove");
			}
		});

		listViewMoves = (ListView) currentView
				.findViewById(R.id.list_items_found);
		listViewMoves.setAdapter(adapterMoves);
		return currentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (currentItem == null) {
			if(savedInstanceState != null){
				String saved = savedInstanceState.getString(MOVE_LIST_FRAGMENT_KEY);
				currentItem = SortChoice.valueOf(saved);
			}
			if(currentItem == null){
				currentItem = SortChoice.COMPARE_BY_DPS;
			}
			updateViewImpl();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (currentItem != null) {
			outState.putString(MOVE_LIST_FRAGMENT_KEY, currentItem.name());
		}
	}

	@Override
	protected void updateViewImpl() {
		if (currentItem == null) {
			currentItem = SortChoice.COMPARE_BY_NAME;
		}
		final SortChoice sortChoice = currentItem;
		boolean isDPSVisible = false;
		boolean isPowerVisible = false;
		boolean isSpeedVisible = false;
		final Comparator<Move> c;
		switch (sortChoice) {
			case COMPARE_BY_DPS :
				c = MoveComparators.getComparatorByDps();
				isDPSVisible = true;
				break;
			case COMPARE_BY_POWER :
				c = MoveComparators.getComparatorByPower();
				isPowerVisible = true;
				break;
			case COMPARE_BY_DURATION :
				c = MoveComparators.getComparatorBySpeed();
				isSpeedVisible = true;
				break;
			case COMPARE_BY_NAME :
				c = MoveComparators.getComparatorByName();
				break;
			default :
				Log.e(MoveListFragment.class.getName(),
						"SortChoice not found : " + sortChoice);
				c = null;
				break;
		}
		adapterMoves.setDPSVisible(isDPSVisible);
		adapterMoves.setPowerVisible(isPowerVisible);
		adapterMoves.setSpeedVisible(isSpeedVisible);
		adapterMoves.sort(c); // include notify data set changed
	}

	private final OnItemSelectedListener onItemSortSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (position != AdapterView.INVALID_POSITION) {
				showItem(adapterSortChoice.getItem(position));
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	@Override
	public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
		this.mCallbackMove = visitor;
	}
}
