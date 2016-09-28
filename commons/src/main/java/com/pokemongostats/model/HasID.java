package com.pokemongostats.model;

/**
 * 
 * @author Zapagon
 *
 */
public interface HasID {

	public static final long NO_ID = -1L;

	public long getId();

	public void setId(long id);
}
