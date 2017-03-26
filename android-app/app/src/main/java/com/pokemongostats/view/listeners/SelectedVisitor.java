package com.pokemongostats.view.listeners;

/**
 * @param <T>
 * @author Zapagon
 */
public interface SelectedVisitor<T> {
    public void select(final T newItem);
}
