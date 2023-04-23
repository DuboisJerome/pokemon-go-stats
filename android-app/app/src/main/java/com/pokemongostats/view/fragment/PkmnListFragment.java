package com.pokemongostats.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.databinding.FragmentPkmnListBinding;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.model.parcalables.PclbPkmnDescFilterInfo;
import com.pokemongostats.view.adapter.PkmnDescAdapter;
import com.pokemongostats.view.dialog.ChooseTypeDialogFragment;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;
import com.pokemongostats.view.rows.PkmnDescHeaderHandler;
import com.pokemongostats.view.utils.PreferencesUtils;

import java.util.List;

/**
 * @author Zapagon
 */
public class PkmnListFragment
		extends
		Fragment
		implements Observer {

	private static final String PKMN_LIST_FILTER_KEY = "PKMN_LIST_FILTER_KEY";

	private PkmnDescAdapter adapterPkmns;
	private FragmentPkmnListBinding binding;
	PkmnDescFilterInfo infos;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PokedexDAO dao = PokedexDAO.getInstance();
		List<PkmnDesc> lstPkmnDesc = dao.getListPkmnDesc();
		lstPkmnDesc.sort(PkmnDescComparators.getComparatorById());
		this.adapterPkmns = new PkmnDescAdapter(lstPkmnDesc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		this.binding = FragmentPkmnListBinding.inflate(inflater, container, false);

		this.adapterPkmns.setOnClickItemListener(t -> {
			PkmnListFragmentDirections.ActionToPkmn action = PkmnListFragmentDirections.actionToPkmn(t.getUniqueId());
			Navigation.findNavController(this.binding.getRoot()).navigate(action);
		});

		this.binding.filterPkmnView.search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
			                              int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
			                          int before, int count) {
				PkmnListFragment.this.infos.setName(s.toString());
				filter();
			}
		});

		this.binding.filterPkmnView.valueType1.setOnClickListener(v -> {
			ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment(t -> {
				// load view with type
				this.binding.filterPkmnView.setType1(t);
				this.infos.setType1(t);
				filter();
			});
			chooseTypeDialog.addCurrentType(this.binding.filterPkmnView.getType1());
			chooseTypeDialog.addCurrentType(this.binding.filterPkmnView.getType2());

			chooseTypeDialog.show(getParentFragmentManager(), "chooseTypeDialog1");
		});

		this.binding.filterPkmnView.valueType2.setOnClickListener(v -> {
			ChooseTypeDialogFragment chooseTypeDialog = new ChooseTypeDialogFragment(t -> {
				// load view with type
				this.binding.filterPkmnView.setType2(t);
				this.infos.setType2(t);
				filter();
			});
			chooseTypeDialog.addCurrentType(this.binding.filterPkmnView.getType1());
			chooseTypeDialog.addCurrentType(this.binding.filterPkmnView.getType2());
			chooseTypeDialog.show(getParentFragmentManager(), "chooseTypeDialog2");
		});

		RecyclerView recList = this.binding.listCardView;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);

		recList.setAdapter(this.adapterPkmns);
		this.binding.pkmnListPkmnsHeader.setAdapter(this.adapterPkmns);
		this.binding.pkmnListPkmnsHeader.setHandlers(new PkmnDescHeaderHandler());

		PreferencesUtils.getInstance().registerObserver(this);


		String typeArg = PkmnListFragmentArgs.fromBundle(getArguments()).getType1();
		Type type = Type.valueOfIgnoreCase(typeArg);
		if (type != null) {
			this.binding.filterPkmnView.setType1(type);
			this.binding.pkmnListPkmnsHeader.pkmnDescMaxCp.setSelected(true);
			// Vide le comparateur pour ne pas avoir un tri CP "asc" s'il était déjà "desc"
			this.adapterPkmns.setComparator(null);
			this.binding.pkmnListPkmnsHeader.getHandlers().onClickCPMax(
					this.binding.pkmnListPkmnsHeader.pkmnDescMaxCp
					, this.adapterPkmns);
		} else {
			this.binding.pkmnListPkmnsHeader.pkmnDescImg.setSelected(true);
		}

		return this.binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		PreferencesUtils.getInstance().unregisterObserver(this);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.infos = savedInstanceState.getParcelable(PKMN_LIST_FILTER_KEY);
		}
		if (this.infos == null) {
			PkmnDescFilterInfo p = new PkmnDescFilterInfo();
			p.setName(this.binding.filterPkmnView.getName());
			p.setType1(this.binding.filterPkmnView.getType1());
			p.setType2(this.binding.filterPkmnView.getType2());
			this.infos = p;
		}
		this.binding.filterPkmnView.setName(this.infos.getName());
		this.binding.filterPkmnView.setType1(this.infos.getType1());
		this.binding.filterPkmnView.setType2(this.infos.getType2());
		filterSyncNoNotif();
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(PKMN_LIST_FILTER_KEY, new PclbPkmnDescFilterInfo(this.infos));
	}

	@Override
	public void update(Observable o) {
		if (o == null) {
			return;
		}
		if (o.equals(PreferencesUtils.getInstance())) {
			filter();
		}
	}

	private void filter() {
		this.adapterPkmns.filter(this.infos.toFilter());
	}

	private void filterSyncNoNotif() {
		this.adapterPkmns.filterSyncNoNotif(this.infos.toFilter());
	}
}