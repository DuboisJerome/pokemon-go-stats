/**
 * 
 */
package com.pokemongostats.controler.utils;

import static com.pokemongostats.model.bean.Effectiveness.NORMAL;
import static com.pokemongostats.model.bean.Effectiveness.NOT_VERY_EFFECTIVE;
import static com.pokemongostats.model.bean.Effectiveness.REALLY_NOT_VERY_EFFECTIVE;
import static com.pokemongostats.model.bean.Effectiveness.REALLY_SUPER_EFFECTIVE;
import static com.pokemongostats.model.bean.Effectiveness.SUPER_EFFECTIVE;

import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 *
 */
public class PokemonUtils {

	/**
	 * @param typeAtt
	 * @param pkmnDef
	 * @return effectiveness of type attack on defensing pkmn
	 */
	public static Effectiveness getTypeEffOnPokemon(final Type typeAtt,
			final PokemonDescription pkmnDef) {
		final Effectiveness eff1 = PokemonUtils.getTypeEffOnType(typeAtt,
				pkmnDef.getType1());
		final Effectiveness eff2 = PokemonUtils.getTypeEffOnType(typeAtt,
				pkmnDef.getType2());

		return getEffectivenessAddition(eff1, eff2);
	}

	/**
	 * Get effectiveness of type attaque on type defense
	 * 
	 * @param typeAtt
	 * @param typeDef
	 * @return
	 */
	public static Effectiveness getTypeEffOnType(final Type typeAtt,
			final Type typeDef) {
		if (typeAtt.getInefficace().contains(typeDef)
			|| typeAtt.getPasTresEfficace().contains(typeDef)) {
			return NOT_VERY_EFFECTIVE;
		} else if (typeAtt.getSuperEfficace()
				.contains(typeDef)) { return SUPER_EFFECTIVE; }

		return NORMAL;
	}

	/**
	 * @param eff1
	 * @param eff2
	 * @return additional effectiveness
	 */
	public static Effectiveness getEffectivenessAddition(
			final Effectiveness eff1, final Effectiveness eff2) {
		if (eff1 == null && eff2 == null) { return NORMAL; }
		if (eff1 == null) { return eff2; }
		if (eff2 == null) { return eff1; }

		int totalEffWeight = eff1.getWeight() + eff2.getWeight();

		Effectiveness result = NORMAL;
		if (totalEffWeight >= REALLY_SUPER_EFFECTIVE.getWeight()) {
			result = REALLY_SUPER_EFFECTIVE;
		} else if (totalEffWeight == SUPER_EFFECTIVE.getWeight()) {
			result = SUPER_EFFECTIVE;
		} else if (totalEffWeight == NOT_VERY_EFFECTIVE.getWeight()) {
			result = NOT_VERY_EFFECTIVE;
		} else if (totalEffWeight <= REALLY_NOT_VERY_EFFECTIVE.getWeight()) {
			result = REALLY_NOT_VERY_EFFECTIVE;
		}

		return result;
	}
}
