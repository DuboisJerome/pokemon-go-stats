package com.pokemongostats.model;

public enum Team {
	MYSTIC, VALOR, INSCTINCT;

	public static Team valueOfIgnoreCase(final String team) {
		if (team == null || team.isEmpty()) { return null; }
		return Team.valueOf(team.toUpperCase());
	}
}
