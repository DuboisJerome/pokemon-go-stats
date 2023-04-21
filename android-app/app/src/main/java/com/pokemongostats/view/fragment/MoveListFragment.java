package com.pokemongostats.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.databinding.FragmentMoveListBinding;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.model.parcalables.PclbMoveFilterInfo;
import com.pokemongostats.view.adapter.MoveAdapter;
import com.pokemongostats.view.dialog.ChooseTypeDialogFragment;
import com.pokemongostats.view.rows.AbstractMoveRow;
import com.pokemongostats.view.utils.ComparatorUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Zapagon
 */
public class MoveListFragment
		extends
		Fragment {

	private static final String MOVE_LIST_ITEM_KEY = "MOVE_LIST_ITEM_KEY";
	private static final String MOVE_LIST_FILTER_KEY = "MOVE_LIST_FILTER_KEY";

	// controler
	private ArrayAdapter<SortChoice> adapterSortChoice;
	private SortChoice sortChoice = null;
	private final OnItemSelectedListener onItemSortSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
		                           int position, long id) {
			if (position != AdapterView.INVALID_POSITION) {
				MoveListFragment.this.sortChoice = MoveListFragment.this.adapterSortChoice.getItem(position);
				updateViewImpl();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};
	private MoveAdapter adapterChargeMoves;
	private MoveAdapter adapterQuickMoves;
	// model
	private MoveFilterInfo moveFilterInfo;
	private FragmentMoveListBinding binding;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.adapterSortChoice = new ArrayAdapter<SortChoice>(Objects.requireNonNull(getActivity()),
				android.R.layout.simple_spinner_item, SortChoice.values()) {

			/**
			 * {@inheritDoc}
			 */
			@NonNull
			@Override
			public View getView(int position, View convertView,
			                    @NonNull ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				return initText(position, v);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public View getDropDownView(int position, View convertView,
			                            @NonNull ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				return initText(position, v);
			}

			private View initText(int position, View v) {
				try {
					TextView text = (TextView) v;
					SortChoice sortChoice = getItem(position);
					if (sortChoice != null) {
						text.setText(getString(sortChoice.idLabel));
					}
				} catch (Exception e) {
					Log.e(TagUtils.DEBUG,
							"Error spinner sort choice", e);
				}
				return v;
			}
		};
		this.adapterSortChoice.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		List<Move> list = PokedexDAO.getInstance().getListMove();
		List<Move> listMoveCharge = list.stream().filter(m -> Move.MoveType.CHARGE.equals(m.getMoveType())).collect(Collectors.toList());
		this.adapterChargeMoves = new MoveAdapter(listMoveCharge);
		List<Move> listMoveQuick = list.stream().filter(m -> Move.MoveType.QUICK.equals(m.getMoveType())).collect(Collectors.toList());
		this.adapterQuickMoves = new MoveAdapter(listMoveQuick);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		this.binding = FragmentMoveListBinding.inflate(inflater, container, false);

		// view
		Spinner spinnerSortChoice = this.binding.filterMoveView.listSortChoice;
		spinnerSortChoice.setAdapter(this.adapterSortChoice);
		spinnerSortChoice.setSelection(0, false);
		spinnerSortChoice.setOnItemSelectedListener(this.onItemSortSelectedListener);

		// Filter type
		this.binding.filterMoveView.valueType.setOnClickListener(v -> {
			ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment(t -> {
				// load view with type
				this.binding.filterMoveView.valueType.setType(t);
				this.moveFilterInfo.setType(t);
				filter();
			});
			chooseTypeDialog.addCurrentType(this.binding.filterMoveView.getType());

			chooseTypeDialog.show(getParentFragmentManager(), "chooseTypeDialog");
		});

		// Moves

		Consumer<Move> mCallbackMove = m -> {
			MoveListFragmentDirections.ActionToMove action = MoveListFragmentDirections.actionToMove(m.getId());
			Navigation.findNavController(this.binding.getRoot()).navigate(action);
		};
		RecyclerView listViewChargeMoves = this.binding.listChargemoveFound;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		listViewChargeMoves.setLayoutManager(llm);
		listViewChargeMoves.setAdapter(this.adapterChargeMoves);
		this.adapterChargeMoves.setOnClickItemListener(mCallbackMove);

		RecyclerView listViewQuickMoves = this.binding.listQuickmoveFound;
		llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		listViewQuickMoves.setLayoutManager(llm);
		listViewQuickMoves.setAdapter(this.adapterQuickMoves);
		this.adapterQuickMoves.setOnClickItemListener(mCallbackMove);

		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		if (this.sortChoice == null) {
			if (savedInstanceState != null) {
				String saved = savedInstanceState.getString(MOVE_LIST_ITEM_KEY);
				this.sortChoice = SortChoice.valueOf(saved);
			}
			if (this.sortChoice == null) {
				this.sortChoice = SortChoice.COMPARE_BY_PPS;
			}
		}
		if (this.moveFilterInfo == null && savedInstanceState != null) {
			this.moveFilterInfo = savedInstanceState.getParcelable(MOVE_LIST_FILTER_KEY);
		}
		if (this.moveFilterInfo == null) {
			MoveFilterInfo m = new MoveFilterInfo();
			m.setType(this.binding.filterMoveView.getType());
			this.moveFilterInfo = m;
		}
		this.binding.filterMoveView.valueType.setType(this.moveFilterInfo.getType());
		filterSyncNoNotif();
		updateViewImpl();
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.sortChoice != null) {
			outState.putString(MOVE_LIST_ITEM_KEY, this.sortChoice.name());
		}
		if (this.moveFilterInfo != null) {
			outState.putParcelable(MOVE_LIST_FILTER_KEY, new PclbMoveFilterInfo(this.moveFilterInfo));
		}
	}

	protected void updateViewImpl() {
		if (this.sortChoice == null) {
			this.sortChoice = SortChoice.COMPARE_BY_NAME;
		}
		AbstractMoveRow.Config moveRowConfig = new AbstractMoveRow.Config();
		Comparator<Move> c;
		switch (this.sortChoice) {
			case COMPARE_BY_NAME:
				c = ComparatorUtils.createComparatorNullCheck(Move::getName);
				break;
			case COMPARE_BY_POWER:
				c = ComparatorUtils.createComparatorNullCheck(Move::getPower).reversed();
				moveRowConfig.setPowerVisible(true);
				break;
			case COMPARE_BY_ENERGY:
				c = ComparatorUtils.createComparatorNullCheck(Move::getEnergyDelta).reversed();
				moveRowConfig.setEnergyVisible(true);
				break;
			case COMPARE_BY_PPS:
				c = MoveComparators.getComparatorByPps().reversed();
				moveRowConfig.setPowerPerSecondVisible(true);
				break;
			case COMPARE_BY_DURATION:
				c = ComparatorUtils.createComparatorNullCheck(Move::getDuration);
				moveRowConfig.setDurationVisible(true);
				break;
			case COMPARE_BY_DPT:
				c = ComparatorUtils.createComparatorNullCheck(FightUtils::computePowerPerTurn).reversed();
				moveRowConfig.setDPTVisible(true);
				break;
			case COMPARE_BY_EPT:
				c = ComparatorUtils.createComparatorNullCheck(FightUtils::computeEnergyPerTurn).reversed();
				moveRowConfig.setEPTVisible(true);
				break;
			case COMPARE_BY_DPTxEPT:
				c = ComparatorUtils.createComparatorNullCheck(FightUtils::computePowerEnergyPerTurn).reversed();
				moveRowConfig.setDPTxEPTVisible(true);
				break;
			default:
				Log.e(TagUtils.DEBUG,
						"SortChoice not found : " + this.sortChoice);
				c = ComparatorUtils.createComparatorNullCheck(Move::getName);
				break;
		}
		this.binding.chargeMoveHeader.setConfig(moveRowConfig);

		this.adapterChargeMoves.setConfig(moveRowConfig);
		this.adapterChargeMoves.sort(c);
		this.adapterChargeMoves.notifyDataSetChanged();

		this.binding.quickMoveHeader.setConfig(moveRowConfig);

		this.adapterQuickMoves.setConfig(moveRowConfig);
		this.adapterQuickMoves.sort(c);
		this.adapterQuickMoves.notifyDataSetChanged();
	}

	private void filter() {
		if (this.moveFilterInfo != null) {
			Filter.FilterListener filterListener = i -> {
				// TODO hide waiting popup
			};
			// TODO show waiting popup
			this.adapterChargeMoves.getFilter().filter(this.moveFilterInfo.toFilter(), filterListener);
			this.adapterQuickMoves.getFilter().filter(this.moveFilterInfo.toFilter(), filterListener);
		}
	}

	private void filterSyncNoNotif() {
		if (this.moveFilterInfo != null) {
			this.adapterChargeMoves.filterSyncNoNotif(this.moveFilterInfo.toFilter());
			this.adapterQuickMoves.filterSyncNoNotif(this.moveFilterInfo.toFilter());
		}
	}

	public enum SortChoice {
		COMPARE_BY_PPS(R.string.sort_by_pps),
		//
		COMPARE_BY_POWER(R.string.sort_by_power),
		//
		COMPARE_BY_DURATION(R.string.sort_by_duration),
		//
		COMPARE_BY_NAME(R.string.sort_by_name),
		//
		COMPARE_BY_ENERGY(R.string.sort_by_energy),
		//
		COMPARE_BY_DPT(R.string.sort_by_dpt),
		//
		COMPARE_BY_EPT(R.string.sort_by_ept),
		//
		COMPARE_BY_DPTxEPT(R.string.sort_by_dpt_x_ept);
		private final int idLabel;

		SortChoice(int idLabel) {
			this.idLabel = idLabel;
		}
	}
}