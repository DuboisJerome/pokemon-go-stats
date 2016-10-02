package com.pokemongostats.model;

/**
 * Description of a Gym
 * 
 * @author Zapagon
 *
 */
public class GymDescription implements HasLocation, HasID {

	private long id = HasID.NO_ID;

	private String name;

	private Location location;

	public GymDescription(final String name) {
		this(name, null);
	}

	public GymDescription(final String name, final Location location) {
		this.setName(name);
		this.setLocation(location);
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
		if (this.location != null && this.location.getLatitude() != null && this.location.getLongitude() != null) {
			b.append(this.location);
		}
		return b.toString();
	}
}