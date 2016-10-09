/**
 * 
 */
package com.pokemongostats.model.bean;

import java.io.Serializable;

/**
 * @author Zapagon
 *
 */
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 904907406111839342L;

	private double latitude = 0d;

	private double longitude = 0d;

	public Location() {
	}

	public Location(final double lat, final double lon) {
		this.latitude = lat;
		this.longitude = lon;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return 'latitude, longitude'
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("(").append(this.latitude).append(",").append(this.longitude)
				.append(")");;
		return b.toString();
	}
}
