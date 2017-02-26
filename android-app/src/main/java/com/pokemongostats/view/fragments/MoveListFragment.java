/**
 * 
 */
package com.pokemongostats.view.fragments;

import java.util.Comparator;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.adapters.MoveAdapter;
import com.pokemongostats.view.commons.HasTitle;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class MoveListFragment extends HistorizedFragment<MoveListFragment.SortChoice> implements HasMoveSelectable {

	public static enum SortChoice implements HasTitle {
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

		adapterSortChoice = new ArrayAdapter<SortChoice>(getActivity(), android.R.layout.simple_spinner_item,
				SortChoice.values()) {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				return initText(position, v);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				return initText(position, v);
			}

			private View initText(int position, View v) {
				try {
					TextView text = (TextView) v;
					SortChoice sortChoice = getItem(position);
					text.setText(getString(sortChoice.idLabel));
				} catch (Exception e) {
					Log.e(MoveListFragment.class.getName(), "Error spinner sort choice", e);
				}
				return v;
			}
		};
		adapterSortChoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		PkmnGoStatsApplication app = (PkmnGoStatsApplication) getContext().getApplicationContext();
		adapterMoves = new MoveAdapter(getActivity(), android.R.layout.simple_spinner_item);
		adapterMoves.addAll(app.getMoves());
		adapterMoves.acceptSelectedVisitorMove(mCallbackMove);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_item_list, container, false);

		spinnerSortChoice = (Spinner) view.findViewById(R.id.list_sort_choice);
		spinnerSortChoice.setAdapter(adapterSortChoice);
		spinnerSortChoice.setSelection(0, false);
		spinnerSortChoice.setOnItemSelectedListener(onItemSortSelectedListener);

		listViewMoves = (ListView) view.findViewById(R.id.list_items_found);
		listViewMoves.setAdapter(adapterMoves);
		updateView();

		return view;
	}

	@Override
	protected void updateView() {
		if (currentItem == null) {
			currentItem = SortChoice.COMPARE_BY_NAME;
		}
		final SortChoice sortChoice = currentItem;
		boolean isDPSVisible = false;
		boolean isPowerVisible = false;
		boolean isSpeedVisible = false;
		final Comparator<Move> c;
		switch (sortChoice) {
		case COMPARE_BY_DPS:
			c = MoveComparators.COMPARATOR_BY_DPS;
			isDPSVisible = true;
			break;
		case COMPARE_BY_POWER:
			c = MoveComparators.COMPARATOR_BY_POWER;
			isPowerVisible = true;
			break;
		case COMPARE_BY_DURATION:
			c = MoveComparators.COMPARATOR_BY_SPEED;
			isSpeedVisible = true;
			break;
		case COMPARE_BY_NAME:
			c = MoveComparators.COMPARATOR_BY_NAME;
			break;
		default:
			Log.e(MoveListFragment.class.getName(), "SortChoice not found : " + sortChoice);
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
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (position != AdapterView.INVALID_POSITION) {
				showItem(adapterSortChoice.getItem(position));
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}

	};

	@Override
	public void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor) {
		this.mCallbackMove = visitor;
	}
}
