package com.pokemongostats.model.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Business object representing a Gym at a specific date
 * 
 * @author Zapagon
 *
 */
public class Gym implements HasID, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1089173948832741453L;

	private long id = HasID.NO_ID;

	private GymDescription gymDesc;

	private int level;

	private Date date;

	private Team team;

	private List<Pokemon> pokemons;

	/**
	 * @return the gymDesc
	 */
	public GymDescription getGymDesc() {
		return gymDesc;
	}

	/**
	 * @param gymDesc
	 *            the gymDesc to set
	 */
	public void setGymDesc(GymDescription gymDesc) {
		this.gymDesc = gymDesc;
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
	@Override
	public long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

}
