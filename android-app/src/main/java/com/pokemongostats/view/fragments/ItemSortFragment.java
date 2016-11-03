/**
 * 
 */
package com.pokemongostats.view.fragments;

import com.pokemongostats.R;
import com.pokemongostats.view.commons.SpinnerInteractionListener;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author Zapagon
 *
 */
public class ItemSortFragment
		extends
			StackFragment<ItemSortFragment.ItemSortSavedState> {

	// array-item R TODO
	private static final String[] arrayItemType = {"Pokemon descrption",
			"Moves"};

	private Spinner spinnerItemType;
	private ArrayAdapter<String> adapterItemType;

	private Spinner spinnerComparatorItem;
	private ArrayAdapter<?> adapterComparatorItem;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapterItemType = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arrayItemType);
		adapterItemType.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_itemsort, container,
				false);

		spinnerItemType = (Spinner) view.findViewById(R.id.types_spinner);
		spinnerItemType.setAdapter(adapterItemType);
		spinnerItemType.setOnItemSelectedListener(onItemSortSelectedListener);
		spinnerItemType.setOnTouchListener(onItemSortSelectedListener);

		spinnerComparatorItem = (Spinner) view.findViewById(R.id.types_spinner);
		spinnerComparatorItem
				.setOnItemSelectedListener(onItemSortSelectedListener);
		spinnerComparatorItem.setOnTouchListener(onItemSortSelectedListener);

		return view;
	}

	@Override
	protected void updateView(final ItemSortSavedState item) {
		// TODO Auto-generated method stub

	}

	private final SpinnerInteractionListener onItemSortSelectedListener = new SpinnerInteractionListener() {

		@Override
		protected void onItemSelectedFromCode(AdapterView<?> parent, View view,
				int pos, long id) {
		}

		@Override
		protected void onItemSelectedFromUser(AdapterView<?> parent, View view,
				int pos, long id) {
			String str = adapterItemType.getItem(pos);
			adapterComparatorItem.clear();// TODO set list comparator
		}
	};

	protected static class ItemSortSavedState extends BaseSavedState {

		ItemSortSavedState(Parcelable superState) {
			super(superState);
		}

		protected ItemSortSavedState(Parcel in) {
			super(in);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<ItemSortSavedState> CREATOR = new Parcelable.Creator<ItemSortSavedState>() {
			@Override
			public ItemSortSavedState createFromParcel(Parcel in) {
				return new ItemSortSavedState(in);
			}
			@Override
			public ItemSortSavedState[] newArray(int size) {
				return new ItemSortSavedState[size];
			}
		};
	}

}
