/**
 *
 */
package com.pokemongostats.model.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Zapagon
 *
 */
public enum Type {
	STEEL, FIGHTING, DRAGON, WATER, ELECTRIC, FAIRY, FIRE, ICE, BUG, NORMAL, GRASS, POISON, PSYCHIC, ROCK, GROUND, GHOST, DARK, FLYING;

	static {
		// //////////////////////////////////
		// / ACIER
		// fort
		STEEL.superEfficace.add(ICE);
		STEEL.superEfficace.add(ROCK);
		STEEL.superEfficace.add(FAIRY);
		// faible
		STEEL.pasTresEfficace.add(FIRE);
		STEEL.pasTresEfficace.add(ELECTRIC);
		STEEL.pasTresEfficace.add(STEEL);
		STEEL.pasTresEfficace.add(WATER);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / COMBAT
		// fort
		FIGHTING.superEfficace.add(NORMAL);
		FIGHTING.superEfficace.add(ROCK);
		FIGHTING.superEfficace.add(ICE);
		FIGHTING.superEfficace.add(STEEL);
		FIGHTING.superEfficace.add(DARK);
		// faible
		FIGHTING.pasTresEfficace.add(BUG);
		FIGHTING.pasTresEfficace.add(FLYING);
		FIGHTING.pasTresEfficace.add(POISON);
		FIGHTING.pasTresEfficace.add(PSYCHIC);
		FIGHTING.pasTresEfficace.add(FAIRY);
		// inefficace
		FIGHTING.pasTresEfficace.add(GHOST);
		// //////////////////////////////////

		// //////////////////////////////////
		// / DRAGON
		// fort
		DRAGON.superEfficace.add(DRAGON);
		// faible
		DRAGON.pasTresEfficace.add(STEEL);
		// inefficace
		DRAGON.inefficace.add(FAIRY);
		// //////////////////////////////////

		// //////////////////////////////////
		// / EAU
		// fort
		WATER.superEfficace.add(FIRE);
		WATER.superEfficace.add(ROCK);
		WATER.superEfficace.add(GROUND);
		// faible
		WATER.pasTresEfficace.add(WATER);
		WATER.pasTresEfficace.add(GRASS);
		WATER.pasTresEfficace.add(DRAGON);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / ELECTRIK
		// fort
		ELECTRIC.superEfficace.add(WATER);
		ELECTRIC.superEfficace.add(FLYING);
		// faible
		ELECTRIC.pasTresEfficace.add(GRASS);
		ELECTRIC.pasTresEfficace.add(ELECTRIC);
		ELECTRIC.pasTresEfficace.add(DRAGON);
		// inefficace
		ELECTRIC.inefficace.add(GROUND);
		// //////////////////////////////////

		// //////////////////////////////////
		// / FEE
		// fort
		FAIRY.superEfficace.add(DRAGON);
		FAIRY.superEfficace.add(FIGHTING);
		FAIRY.superEfficace.add(DARK);
		// faible
		FAIRY.pasTresEfficace.add(FIRE);
		FAIRY.pasTresEfficace.add(POISON);
		FAIRY.pasTresEfficace.add(STEEL);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / FEU
		// fort
		FIRE.superEfficace.add(GRASS);
		FIRE.superEfficace.add(STEEL);
		FIRE.superEfficace.add(BUG);
		FIRE.superEfficace.add(ICE);
		// faible
		FIRE.pasTresEfficace.add(FIRE);
		FIRE.pasTresEfficace.add(WATER);
		FIRE.pasTresEfficace.add(ROCK);
		FIRE.pasTresEfficace.add(DRAGON);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / GLACE
		// fort
		ICE.superEfficace.add(GROUND);
		ICE.superEfficace.add(FLYING);
		ICE.superEfficace.add(GRASS);
		ICE.superEfficace.add(DRAGON);
		// faible
		ICE.pasTresEfficace.add(STEEL);
		ICE.pasTresEfficace.add(FIRE);
		ICE.pasTresEfficace.add(WATER);
		ICE.pasTresEfficace.add(ICE);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / INSECTE
		// fort
		BUG.superEfficace.add(GRASS);
		BUG.superEfficace.add(PSYCHIC);
		BUG.superEfficace.add(DARK);
		// faible
		BUG.pasTresEfficace.add(STEEL);
		BUG.pasTresEfficace.add(FIGHTING);
		BUG.pasTresEfficace.add(FAIRY);
		BUG.pasTresEfficace.add(FIRE);
		BUG.pasTresEfficace.add(POISON);
		BUG.pasTresEfficace.add(GHOST);
		BUG.pasTresEfficace.add(FLYING);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / NORMAL
		// fort
		// faible
		NORMAL.pasTresEfficace.add(STEEL);
		NORMAL.pasTresEfficace.add(ROCK);
		// inefficace
		NORMAL.inefficace.add(GHOST);
		// //////////////////////////////////

		// //////////////////////////////////
		// / PLANTE
		// fort
		GRASS.superEfficace.add(WATER);
		GRASS.superEfficace.add(ROCK);
		GRASS.superEfficace.add(GROUND);
		// faible
		GRASS.pasTresEfficace.add(FIRE);
		GRASS.pasTresEfficace.add(GRASS);
		GRASS.pasTresEfficace.add(STEEL);
		GRASS.pasTresEfficace.add(DRAGON);
		GRASS.pasTresEfficace.add(FLYING);
		GRASS.pasTresEfficace.add(BUG);
		GRASS.pasTresEfficace.add(POISON);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / POISON
		// fort
		POISON.superEfficace.add(GRASS);
		POISON.superEfficace.add(FAIRY);
		// faible
		POISON.pasTresEfficace.add(POISON);
		POISON.pasTresEfficace.add(ROCK);
		POISON.pasTresEfficace.add(GROUND);
		POISON.pasTresEfficace.add(GHOST);
		// inefficace
		POISON.inefficace.add(STEEL);
		// //////////////////////////////////

		// //////////////////////////////////
		// / PSY
		// fort
		PSYCHIC.superEfficace.add(FIGHTING);
		PSYCHIC.superEfficace.add(POISON);
		// faible
		PSYCHIC.pasTresEfficace.add(PSYCHIC);
		PSYCHIC.pasTresEfficace.add(STEEL);
		// inefficace
		PSYCHIC.inefficace.add(DARK);
		// //////////////////////////////////

		// //////////////////////////////////
		// / ROCHE
		// fort
		ROCK.superEfficace.add(FIRE);
		ROCK.superEfficace.add(FLYING);
		ROCK.superEfficace.add(ICE);
		ROCK.superEfficace.add(BUG);
		// faible
		ROCK.pasTresEfficace.add(GROUND);
		ROCK.pasTresEfficace.add(STEEL);
		ROCK.pasTresEfficace.add(FIGHTING);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / SOL
		// fort
		GROUND.superEfficace.add(ELECTRIC);
		GROUND.superEfficace.add(STEEL);
		GROUND.superEfficace.add(POISON);
		GROUND.superEfficace.add(FIRE);
		GROUND.superEfficace.add(ROCK);
		// faible
		GROUND.pasTresEfficace.add(BUG);
		GROUND.pasTresEfficace.add(GRASS);
		// inefficace
		GROUND.inefficace.add(FLYING);
		// //////////////////////////////////

		// //////////////////////////////////
		// / SPECTRE
		// fort
		GHOST.superEfficace.add(GHOST);
		GHOST.superEfficace.add(PSYCHIC);
		// faible
		GHOST.pasTresEfficace.add(DARK);
		// inefficace
		GHOST.inefficace.add(NORMAL);
		// //////////////////////////////////

		// //////////////////////////////////
		// / TENEBRE
		// fort
		DARK.superEfficace.add(PSYCHIC);
		DARK.superEfficace.add(GHOST);
		// faible
		DARK.pasTresEfficace.add(FIGHTING);
		DARK.pasTresEfficace.add(DARK);
		DARK.pasTresEfficace.add(FAIRY);
		// inefficace
		// //////////////////////////////////

		// //////////////////////////////////
		// / VOL
		// fort
		FLYING.superEfficace.add(GRASS);
		FLYING.superEfficace.add(FIGHTING);
		FLYING.superEfficace.add(BUG);
		// faible
		FLYING.pasTresEfficace.add(ELECTRIC);
		FLYING.pasTresEfficace.add(STEEL);
		FLYING.pasTresEfficace.add(ROCK);
		// inefficace
		// //////////////////////////////////
	}

	private final Set<Type> superEfficace;

	private final Set<Type> pasTresEfficace;

	private final Set<Type> inefficace;

	Type() {
		this.superEfficace = new HashSet<>();
		this.pasTresEfficace = new HashSet<>();
		this.inefficace = new HashSet<>();
	}

	/**
	 * @return the superEfficace
	 */
	public Set<Type> getSuperEfficace() {
		return new HashSet<>(this.superEfficace);
	}

	/**
	 * @return the pasTresEfficace
	 */
	public Set<Type> getPasTresEfficace() {
		return new HashSet<>(this.pasTresEfficace);
	}

	/**
	 * @return the inefficace
	 */
	public Set<Type> getInefficace() {
		return new HashSet<>(this.inefficace);
	}

	public static Type valueOfIgnoreCase(String type) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		return Type.valueOf(type.toUpperCase());
	}
}