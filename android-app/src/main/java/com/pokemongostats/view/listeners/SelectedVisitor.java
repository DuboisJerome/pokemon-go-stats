package com.pokemongostats.view.listeners;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 */
public interface SelectedVisitor<T> {
	public void select(final T newItem);
}
