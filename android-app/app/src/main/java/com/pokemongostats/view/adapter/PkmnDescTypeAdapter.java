package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.databinding.CardViewPkmnDescBinding;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.filtersinfos.PkmnDescTypeFilterInfo;
import com.pokemongostats.view.viewholder.LstPkmnDescViewHolder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.commons.generique.controller.utils.Log;

/**
 * @author Zapagon
 */
public class PkmnDescTypeAdapter extends PkmnDescAdapter {

	public PkmnDescTypeAdapter(List<PkmnDesc> l) {
		super(l);
	}

	@Override
	protected Predicate<PkmnDesc> getPredicateFilter(String s) {
		Predicate<PkmnDesc> defaultPredicate = super.getPredicateFilter(s);
		return defaultPredicate.and(predicateParEfficacite(s));
	}

	private Predicate<? super PkmnDesc> predicateParEfficacite(String s) {
		try {
			PkmnDescTypeFilterInfo filterInfo = new PkmnDescTypeFilterInfo();
			filterInfo.fromFilter(s);
			Type type = filterInfo.getTypeEff();
			double effFiltre = filterInfo.getEff();
			if (type == null) {
				Log.warn("Pas de type pour le filtre, on exclu tous les pokemons");
				return p -> false;
			}

			List<Function<Double,Boolean>> lstCond = EffectivenessUtils.getSetRoundEff()
					.stream()
					.map(PkmnDescTypeAdapter::isMemeMultiplier)
					.collect(Collectors.toList());

			Function<Double,Boolean> condPredicate = lstCond.stream().filter(c -> c.apply(effFiltre)).findFirst().orElse(e -> false);

			return pkmnDesc -> {
				double eff = EffectivenessUtils.getTypeEffOnPokemon(type, pkmnDesc);
				eff = EffectivenessUtils.roundEff(eff);
				return condPredicate.apply(eff);
			};
		} catch (Exception e) {
			Log.error("Impossible de lire le filtre", e);
		}
		return p -> false;
	}

	private static Function<Double,Boolean> isMemeMultiplier(double multiplier) {
		return eff -> eff == multiplier || Math.abs(eff - multiplier) < 0.001;
	}

	@NonNull
	@Override
	public LstPkmnDescViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardViewPkmnDescBinding binding = CardViewPkmnDescBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new LstPkmnDescViewHolder(binding);
	}
}