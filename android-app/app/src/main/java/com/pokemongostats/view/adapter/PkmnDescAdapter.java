package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.FilterUtils;
import com.pokemongostats.databinding.CardViewPkmnDescBinding;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;
import com.pokemongostats.view.utils.PreferencesUtils;
import com.pokemongostats.view.viewholder.LstPkmnDescViewHolder;

import java.util.List;
import java.util.function.Predicate;

import fr.commons.generique.ui.AbstractGeneriqueAdapter;

/**
 * @author Zapagon
 */
public class PkmnDescAdapter extends AbstractGeneriqueAdapter<PkmnDesc,LstPkmnDescViewHolder> {

	public PkmnDescAdapter() {
		super();
	}

	public PkmnDescAdapter(List<PkmnDesc> l) {
		super(l);
	}

	@Override
	protected Predicate<PkmnDesc> getPredicateFilter(String s) {
		PkmnDescFilterInfo filterInfo = new PkmnDescFilterInfo();
		filterInfo.fromFilter(s);
		PokedexDAO dao = PokedexDAO.getInstance();
		// General preferences
		boolean isLastEvolOnly = PreferencesUtils.getInstance().isLastEvolutionOnly();
		boolean isWithMega = PreferencesUtils.getInstance().isWithMega();
		boolean isWithLegendary = PreferencesUtils.getInstance().isWithLegendary();
		boolean isCheckMove = filterInfo.getMove() != null;
		List<PkmnDesc> l = isCheckMove ? PokedexDAO.getInstance().getListPkmnFor(filterInfo.getMove()) : null;
		return p -> {
			// if (avec evol) OU (evol de même form (ex : mega => id base == id evol))
			boolean isOk = !isLastEvolOnly || dao.getListEvolutionFor(p).stream().allMatch(e -> e.getBasePkmnId() == e.getEvolutionId());
			if (isOk) {
				isOk = isWithMega || !p.getForm().contains("MEGA");
			}
			if (isOk) {
				isOk = isWithLegendary || !p.isLegendary();
			}
			if (isOk) {
				isOk = FilterUtils.isNameMatch(filterInfo.getName(), p.getName());
			}
			if (isOk) {
				isOk = FilterUtils.isTypeMatch(filterInfo.getType1(), filterInfo.getType2(), p.getType1(), p.getType2());
			}
			if (isOk && isCheckMove) {
				isOk = l.contains(p);
			}
			return isOk;
		};
	}

	@NonNull
	@Override
	public LstPkmnDescViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardViewPkmnDescBinding binding = CardViewPkmnDescBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new LstPkmnDescViewHolder(binding);
	}

	public void filter(Move move) {
		PkmnDescFilterInfo filter = new PkmnDescFilterInfo();
		filter.setMove(move);
		filter(filter.toFilter());
	}
}