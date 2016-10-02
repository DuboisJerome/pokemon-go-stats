/**
 * 
 */
package com.pokemongostats.model;

/**
 * @author Zapagon
 *
 */
public class Location {

	private Double latitude = null;

	private Double longitude = null;

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return 'latitude, longitude'
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("(").append(this.latitude).append(",").append(this.longitude).append(")");
		;
		return b.toString();
	}
}
