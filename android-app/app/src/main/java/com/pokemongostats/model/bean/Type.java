package com.pokemongostats.model.bean;

/**
 * @author Zapagon
 */
public enum Type {
	STEEL, FIGHTING, DRAGON, WATER, ELECTRIC, FAIRY, FIRE, ICE, BUG, NORMAL, GRASS, POISON, PSYCHIC, ROCK, GROUND, GHOST, DARK, FLYING;

	public static Type valueOfIgnoreCase(String type) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		return Type.valueOf(type.toUpperCase());
	}
}