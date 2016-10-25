package com.pokemongostats.model.bean;

import java.io.Serializable;

/**
 * 
 * @author Zapagon
 *
 */
public class Trainer implements HasID, Comparable<Trainer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5127039697387409547L;

	private long id = HasID.NO_ID;

	private String name;

	private int level;

	private Team team;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Trainer)) { return false; }
		Trainer p2 = (Trainer) o;
		return (name == null || name.isEmpty())
				? false
				: name.equals(p2.getName());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + this.name + " - " + this.level;
	}

	/**
	 * @param otherTrainer
	 *            other
	 * @return name.compareTo(otherTrainer.name)
	 */
	@Override
	public int compareTo(Trainer otherTrainer) {
		if (otherTrainer == null
			|| otherTrainer.getName() == null) { return 1; }
		if (name == null) { return -1; }
		return name.toLowerCase()
				.compareTo(otherTrainer.getName().toLowerCase());
	}

}
