package com.pokemongostats.controller.utils;

import static com.pokemongostats.model.bean.Type.BUG;
import static com.pokemongostats.model.bean.Type.DARK;
import static com.pokemongostats.model.bean.Type.DRAGON;
import static com.pokemongostats.model.bean.Type.ELECTRIC;
import static com.pokemongostats.model.bean.Type.FAIRY;
import static com.pokemongostats.model.bean.Type.FIGHTING;
import static com.pokemongostats.model.bean.Type.FIRE;
import static com.pokemongostats.model.bean.Type.FLYING;
import static com.pokemongostats.model.bean.Type.GHOST;
import static com.pokemongostats.model.bean.Type.GRASS;
import static com.pokemongostats.model.bean.Type.GROUND;
import static com.pokemongostats.model.bean.Type.ICE;
import static com.pokemongostats.model.bean.Type.NORMAL;
import static com.pokemongostats.model.bean.Type.POISON;
import static com.pokemongostats.model.bean.Type.PSYCHIC;
import static com.pokemongostats.model.bean.Type.ROCK;
import static com.pokemongostats.model.bean.Type.STEEL;
import static com.pokemongostats.model.bean.Type.WATER;

import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * @author Zapagon
 */
public final class EffectivenessUtils {

	public static final double EFF = 1.4d;

	private static final Map<Type,TypeEff> mapEff = new HashMap<>();

	private static TypeEff getTypeEff(Type t) {
		return mapEff.computeIfAbsent(t, TypeEff::new);
	}

	static {
		for (Type t : Type.values()) {
			mapEff.put(t, new TypeEff(t));
		}
		// //////////////////////////////////
		// / ACIER
		// fort
		getTypeEff(STEEL).setSuperEff(ICE, ROCK, FAIRY);
		// faible
		getTypeEff(STEEL).setPasTresEff(FIRE, ELECTRIC, STEEL, WATER);
		// inefficace

		// //////////////////////////////////
		// / COMBAT
		// fort
		getTypeEff(FIGHTING).setSuperEff(NORMAL, ROCK, ICE, STEEL, DARK);
		// faible
		getTypeEff(FIGHTING).setPasTresEff(BUG, FLYING, POISON, PSYCHIC, FAIRY);
		// inefficace
		getTypeEff(FIGHTING).setImmune(GHOST);

		// //////////////////////////////////
		// / DRAGON
		// fort
		getTypeEff(DRAGON).setSuperEff(DRAGON);
		// faible
		getTypeEff(DRAGON).setPasTresEff(STEEL);
		// inefficace
		getTypeEff(DRAGON).setImmune(FAIRY);

		// //////////////////////////////////
		// / EAU
		// fort
		getTypeEff(WATER).setSuperEff(FIRE, ROCK, GROUND);
		// faible
		getTypeEff(WATER).setPasTresEff(WATER, GRASS, DRAGON);
		// inefficace

		// //////////////////////////////////
		// / ELECTRIK
		// fort
		getTypeEff(ELECTRIC).setSuperEff(WATER, FLYING);
		// faible
		getTypeEff(ELECTRIC).setPasTresEff(GRASS, ELECTRIC, DRAGON);
		// inefficace
		getTypeEff(ELECTRIC).setImmune(GROUND);

		// //////////////////////////////////
		// / FEE
		// fort
		getTypeEff(FAIRY).setSuperEff(DRAGON, FIGHTING, DARK);
		// faible
		getTypeEff(FAIRY).setPasTresEff(FIRE, POISON, STEEL);
		// inefficace

		// //////////////////////////////////
		// / FEU
		// fort
		getTypeEff(FIRE).setSuperEff(GRASS, STEEL, BUG, ICE);
		// faible
		getTypeEff(FIRE).setPasTresEff(FIRE, WATER, ROCK, DRAGON);
		// inefficace

		// //////////////////////////////////
		// / GLACE
		// fort
		getTypeEff(ICE).setSuperEff(GROUND, FLYING, GRASS, DRAGON);
		// faible
		getTypeEff(ICE).setPasTresEff(STEEL, FIRE, WATER, ICE);
		// inefficace

		// //////////////////////////////////
		// / INSECTE
		// fort
		getTypeEff(BUG).setSuperEff(GRASS, PSYCHIC, DARK);
		// faible
		getTypeEff(BUG).setPasTresEff(STEEL, FIGHTING, FAIRY, FIRE, POISON, GHOST, FLYING);
		// inefficace

		// //////////////////////////////////
		// / NORMAL
		// fort
		// faible
		getTypeEff(NORMAL).setPasTresEff(STEEL, ROCK);
		// inefficace
		getTypeEff(NORMAL).setImmune(GHOST);

		// //////////////////////////////////
		// / PLANTE
		// fort
		getTypeEff(GRASS).setSuperEff(WATER, ROCK, GROUND);
		// faible
		getTypeEff(GRASS).setPasTresEff(FIRE, GRASS, STEEL, DRAGON, FLYING, BUG, POISON);
		// inefficace

		// //////////////////////////////////
		// / POISON
		// fort
		getTypeEff(POISON).setSuperEff(GRASS, FAIRY);
		// faible
		getTypeEff(POISON).setPasTresEff(POISON, ROCK, GROUND, GHOST);
		// inefficace
		getTypeEff(POISON).setImmune(STEEL);

		// //////////////////////////////////
		// / PSY
		// fort
		getTypeEff(PSYCHIC).setSuperEff(FIGHTING, POISON);
		// faible
		getTypeEff(PSYCHIC).setPasTresEff(PSYCHIC, STEEL);
		// inefficace
		getTypeEff(PSYCHIC).setImmune(DARK);

		// //////////////////////////////////
		// / ROCHE
		// fort
		getTypeEff(ROCK).setSuperEff(FIRE, FLYING, ICE, BUG);
		// faible
		getTypeEff(ROCK).setPasTresEff(GROUND, STEEL, FIGHTING);
		// inefficace

		// //////////////////////////////////
		// / SOL
		// fort
		getTypeEff(GROUND).setSuperEff(ELECTRIC, STEEL, POISON, FIRE, ROCK);
		// faible
		getTypeEff(GROUND).setPasTresEff(BUG, GRASS);
		// inefficace
		getTypeEff(GROUND).setImmune(FLYING);

		// //////////////////////////////////
		// / SPECTRE
		// fort
		getTypeEff(GHOST).setSuperEff(GHOST, PSYCHIC);
		// faible
		getTypeEff(GHOST).setPasTresEff(DARK);
		// inefficace
		getTypeEff(GHOST).setImmune(NORMAL);

		// //////////////////////////////////
		// / TENEBRE
		// fort
		getTypeEff(DARK).setSuperEff(PSYCHIC, GHOST);
		// faible
		getTypeEff(DARK).setPasTresEff(FIGHTING, DARK, FAIRY);
		// inefficace

		// //////////////////////////////////
		// / VOL
		// fort
		getTypeEff(FLYING).setSuperEff(GRASS, FIGHTING, BUG);
		// faible
		getTypeEff(FLYING).setPasTresEff(ELECTRIC, STEEL, ROCK);
		// inefficace
	}

	private EffectivenessUtils() {
	}

	/**
	 * @param typeAtt Type of attacker
	 * @param pkmnDef Pkmn defending
	 * @return effectiveness of type attack on defensing pkmn
	 */
	public static double getTypeEffOnPokemon(Type typeAtt, PkmnDesc pkmnDef) {
		Effectiveness eff1 = getTypeEffOnType(typeAtt, pkmnDef.getType1());
		Effectiveness eff2 = getTypeEffOnType(typeAtt, pkmnDef.getType2());

		return getEffectivenessAddition(eff1, eff2);
	}

	/**
	 * Get effectiveness of type attaque on type defense
	 *
	 * @param typeAtt Type of attacker
	 * @param typeDef Type of defenser
	 * @return Effectiveness
	 */
	public static Effectiveness getTypeEffOnType(Type typeAtt, Type typeDef) {
		return getTypeEff(typeAtt).getEff(typeDef);
	}

	/**
	 * @param eff1 Effectiveness 1
	 * @param eff2 Effectiveness 1
	 * @return additional effectiveness
	 */
	public static double getEffectivenessAddition(Effectiveness eff1, Effectiveness eff2) {
		if (eff1 == null && eff2 == null) {
			return 1;
		}
		if (eff1 == null) {
			return eff2.getMultiplier();
		}
		if (eff2 == null) {
			return eff1.getMultiplier();
		}

		return eff1.getMultiplier() * eff2.getMultiplier();
	}

	private static class TypeEff {
		@Getter
		private final Type type;
		private final Map<Type,Effectiveness> map = new HashMap<>();

		TypeEff(Type t) {
			this.type = t;
		}

		void setSuperEff(Type... types) {
			setEff(Effectiveness.SUPER_EFFECTIVE, types);
		}

		void setPasTresEff(Type... types) {
			setEff(Effectiveness.NOT_VERY_EFFECTIVE, types);
		}

		void setImmune(Type... types) {
			setEff(Effectiveness.IMMUNE, types);
		}

		Effectiveness getEff(Type typeDef) {
			return this.map.getOrDefault(typeDef, Effectiveness.NORMAL);
		}

		private void setEff(Effectiveness eff, Type... types) {
			Arrays.stream(types).forEach(t -> this.map.put(t, eff));
		}
	}
}