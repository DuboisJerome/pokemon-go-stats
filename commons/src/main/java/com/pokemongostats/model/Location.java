/**
 * 
 */
package com.pokemongostats.model;

/**
 * @author Zapagon
 *
 */
public class Location {

	private double latitude;

	private double longitude;

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
		b.append(this.latitude).append(",").append(this.longitude);
		return b.toString();
	}
}
