package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.utils.FilterUtils;
import com.pokemongostats.databinding.CardViewMoveBinding;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;
import com.pokemongostats.view.rows.AbstractMoveRow;
import com.pokemongostats.view.viewholder.LstMoveViewHolder;

import java.util.List;
import java.util.function.Predicate;

import fr.commons.generique.ui.AbstractGeneriqueAdapter;

/**
 * @author Zapagon
 */
public class MoveAdapter extends AbstractGeneriqueAdapter<Move,LstMoveViewHolder> {

	private AbstractMoveRow.Config moveRowConfig = new AbstractMoveRow.Config();

	// pokemon who own those moves
	private PkmnDesc owner;

	public MoveAdapter() {
		super();
	}

	public MoveAdapter(List<Move> listMove) {
		super(listMove);
	}

	private static void setVisibility(View v, boolean b) {
		v.setVisibility(b ? View.VISIBLE : View.GONE);
	}

	public void setOwner(PkmnDesc owner) {
		this.owner = owner;
	}

	public void setConfig(AbstractMoveRow.Config c) {
		this.moveRowConfig = c;
	}

	public void setPowerVisible(boolean powerVisible) {
		this.moveRowConfig.setPowerVisible(powerVisible);
	}

	public void setPowerPerSecondVisible(boolean powerPerSecondVisible) {
		this.moveRowConfig.setPowerPerSecondVisible(powerPerSecondVisible);
	}

	public void setDPTxEPTVisible(boolean DPTxEPTVisible) {
		this.moveRowConfig.setDPTxEPTVisible(DPTxEPTVisible);
	}

	@Override
	protected Predicate<Move> getPredicateFilter(String s) {
		MoveFilterInfo filterInfo = new MoveFilterInfo();
		filterInfo.fromFilter(s);
		return move -> FilterUtils.isNameMatch(filterInfo.getName(), move.getName())
				&& FilterUtils.isTypeMatch(filterInfo.getType(), move.getType());
	}

	@NonNull
	@Override
	public LstMoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardViewMoveBinding binding = CardViewMoveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new LstMoveViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull LstMoveViewHolder viewHolder, int i) {
		viewHolder.getBinding().setOwner(this.owner);
		setVisibility(viewHolder.getBinding().movePower, this.moveRowConfig.isPowerVisible());
		setVisibility(viewHolder.getBinding().moveEnergy, this.moveRowConfig.isEnergyVisible());
		setVisibility(viewHolder.getBinding().movePps, this.moveRowConfig.isPowerPerSecondVisible());
		setVisibility(viewHolder.getBinding().moveDuration, this.moveRowConfig.isDurationVisible());
		setVisibility(viewHolder.getBinding().moveDpt, this.moveRowConfig.isDPTVisible());
		setVisibility(viewHolder.getBinding().moveEpt, this.moveRowConfig.isEPTVisible());
		setVisibility(viewHolder.getBinding().moveDptXEpt, this.moveRowConfig.isDPTxEPTVisible());
		super.onBindViewHolder(viewHolder, i);
	}
}