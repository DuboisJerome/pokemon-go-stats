/**
 *
 */
package com.pokemongostats.controller.utils;

import static com.pokemongostats.model.bean.Effectiveness.IMMUNE;
import static com.pokemongostats.model.bean.Effectiveness.NORMAL;
import static com.pokemongostats.model.bean.Effectiveness.SUPER_EFFECTIVE;

import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 *
 */
public final class EffectivenessUtils {

	public static final double EFF = 1.4d;

	private EffectivenessUtils() {}

	/**
	 * @param typeAtt
	 *            Type of attacker
	 * @param pkmnDef
	 *            Pkmn defending
	 * @return effectiveness of type attack on defensing pkmn
	 */
	public static double getTypeEffOnPokemon(final Type typeAtt, final PkmnDesc pkmnDef) {
		final Effectiveness eff1 = getTypeEffOnType(typeAtt, pkmnDef.getType1());
		final Effectiveness eff2 = getTypeEffOnType(typeAtt, pkmnDef.getType2());

		return getEffectivenessAddition(eff1, eff2);
	}

	/**
	 * Get effectiveness of type attaque on type defense
	 *
	 * @param typeAtt
	 *            Type of attacker
	 * @param typeDef
	 *            Type of defenser
	 * @return Effectiveness
	 */
	public static Effectiveness getTypeEffOnType(final Type typeAtt, final Type typeDef) {
		if (typeAtt.getInefficace().contains(typeDef)) {
			return IMMUNE;
		} else if (typeAtt.getPasTresEfficace().contains(typeDef)) {
			return Effectiveness.NOT_VERY_EFFECTIVE;
		} else if (typeAtt.getSuperEfficace().contains(typeDef)) { return SUPER_EFFECTIVE; }

		return NORMAL;
	}

	/**
	 * @param eff1
	 *            Effectiveness 1
	 * @param eff2
	 *            Effectiveness 1
	 * @return additional effectiveness
	 */
	public static double getEffectivenessAddition(final Effectiveness eff1, final Effectiveness eff2) {
		if (eff1 == null && eff2 == null) { return 1; }
		if (eff1 == null) { return eff2.getMultiplier(); }
		if (eff2 == null) { return eff1.getMultiplier(); }

		return eff1.getMultiplier() * eff2.getMultiplier();
	}
}
