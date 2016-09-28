package com.pokemongostats.model;

/**
 * 
 * @author Zapagon
 *
 */
public class Trainer implements HasID {

	private long id = HasID.NO_ID;

	private String name;

	private int level;

	private Team team;

	public Trainer(final String name, final int level) {
		this(name, level, null);
	}

	public Trainer(final String name, final int level, final Team team) {
		this.name = name;
		this.level = level;
		this.setTeam(team);
	}

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
