package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.utils.FilterUtils;
import com.pokemongostats.databinding.CardViewPkmnMoveBinding;
import com.pokemongostats.model.bean.PkmnMoveComplet;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.rows.AbstractMoveRow;
import com.pokemongostats.view.viewholder.LstPkmnMoveViewHolder;

import java.util.List;
import java.util.function.Predicate;

import fr.commons.generique.ui.AbstractGeneriqueAdapter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Zapagon
 */
public class PkmnMoveAdapter extends AbstractGeneriqueAdapter<PkmnMoveComplet,LstPkmnMoveViewHolder> {

	// pokemon who own those moves
	@Getter
	@Setter
	private PkmnMoveComplet pkmnMoveComplet;

	public PkmnMoveAdapter() {
		super();
	}

	public PkmnMoveAdapter(List<PkmnMoveComplet> l) {
		super(l);
	}

	@Override
	protected Predicate<PkmnMoveComplet> getPredicateFilter(String s) {
		MoveFilterInfo filterInfo = new MoveFilterInfo();
		filterInfo.fromFilter(s);
		String name = FilterUtils.nameForFilter(filterInfo.getName());
		return pm -> FilterUtils.isTypeMatch(filterInfo.getType(), pm.getMove().getType()) &&
				FilterUtils.isNameMatch(name, pm.getMove().getName());
	}

	@NonNull
	@Override
	public LstPkmnMoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardViewPkmnMoveBinding binding = CardViewPkmnMoveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new LstPkmnMoveViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull LstPkmnMoveViewHolder viewHolder, int i) {
		viewHolder.getBinding().setPm(this.pkmnMoveComplet);
		super.onBindViewHolder(viewHolder, i);
	}
}