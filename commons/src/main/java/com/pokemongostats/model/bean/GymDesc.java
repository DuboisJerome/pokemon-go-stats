package com.pokemongostats.model.bean;

import java.io.Serializable;

/**
 * Description of a Gym
 * 
 * @author Zapagon
 *
 */
public class GymDesc
		implements
			HasLocation,
			HasID,
			Comparable<GymDesc>,
			Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7269378839602722170L;

	private long id = HasID.NO_ID;

	private String name;

	private String description;

	private Location location;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the location
	 */
	@Override
	public Location getLocation() {
		return location;
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
		StringBuilder b = new StringBuilder();
		b.append(this.name).append(' ');
		if (this.location != null && this.location.getLatitude() != 0d
			&& this.location.getLongitude() != 0d) {
			b.append(this.location);
		}
		return b.toString();
	}

	/**
	 * @param other
	 * @return compareTo on gym desc name
	 */
	@Override
	public int compareTo(GymDesc other) {
		if (other == null || other.getName() == null) { return 1; }
		if (name == null) { return -1; }
		return name.toLowerCase().compareTo(other.getName().toLowerCase());
	}
}