package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.FilterUtils;
import com.pokemongostats.controller.utils.PkmnTags;
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
		// General preferences
		boolean isLastEvolOnly = PreferencesUtils.getInstance().isLastEvolutionOnly();
		boolean isWithMega = PreferencesUtils.getInstance().isWithTag(PkmnTags.MEGA);
		boolean isWithLegendary = PreferencesUtils.getInstance().isWithTag(PkmnTags.LEGENDAIRE);
		boolean isWithMyhique = PreferencesUtils.getInstance().isWithTag(PkmnTags.MYTHIQUE);
		boolean isWithUltraChimere = PreferencesUtils.getInstance().isWithTag(PkmnTags.ULTRA_CHIMERE);
		boolean isWithOnlyInGame = PreferencesUtils.getInstance().isOnlyInGame();
		boolean isCheckMove = filterInfo.getMove() != null;
		List<PkmnDesc> l = isCheckMove ? PokedexDAO.getInstance().getListPkmnFor(filterInfo.getMove()) : null;
		String name = FilterUtils.nameForFilter(filterInfo.getName());
		return p -> {
			// if (avec evol) OU (evol de même form (ex : mega => id base == id evol))
			boolean isOk = !isLastEvolOnly || p.isLastEvol();
			if (isOk) {
				isOk = isWithMega || !p.isMega();
			}
			if (isOk) {
				isOk = isWithLegendary || !p.isLegendaire();
			}
			if (isOk) {
				isOk = isWithMyhique || !p.isMythique();
			}
			if (isOk) {
				isOk = isWithUltraChimere || !p.isUltraChimere();
			}
			if (isOk) {
				isOk = !isWithOnlyInGame || p.isInGame();
			}
			if (isOk) {
				isOk = FilterUtils.isTypeMatch(filterInfo.getType1(), filterInfo.getType2(), p.getType1(), p.getType2());
			}
			if (isOk) {
				isOk = FilterUtils.isNameMatch(name, p.getName());
				if (!isOk) {
					isOk = p.hasTag(name.toUpperCase());
				}
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