package com.pokemongostats.model.bean;

public enum Team {
	MYSTIC, VALOR, INSTINCT;

	public static Team valueOfIgnoreCase(final String team) {
		if (team == null || team.isEmpty()) {
			return null;
		}
		return Team.valueOf(team.toUpperCase());
	}
}
