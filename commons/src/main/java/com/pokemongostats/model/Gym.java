package com.pokemongostats.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Business object representing a Gym at a specific date
 * 
 * @author Zapagon
 *
 */
public class Gym implements HasID {

	private long id = HasID.NO_ID;

	private GymDescription description;

	private int level;

	private Date date;

	private Team team;

	private List<Pokemon> pokemons;

	public Gym(final GymDescription description, final int level,
			final Date date, final Team team, final List<Pokemon> pokemons) {
		this.description = description;
		this.level = level;
		this.date = date;
		this.pokemons = pokemons == null ? new ArrayList<Pokemon>() : pokemons;
	}

	/**
	 * @return the description
	 */
	public GymDescription getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(GymDescription description) {
		this.description = description;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return the pokemons
	 */
	public List<Pokemon> getPokemons() {
		return pokemons;
	}

	/**
	 * @param pokemons
	 *            the pokemons to set
	 */
	public void setPokemons(List<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setId(long id) {
		this.id = id;
	}

}
