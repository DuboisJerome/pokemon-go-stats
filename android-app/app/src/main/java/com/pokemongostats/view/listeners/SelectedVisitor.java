package com.pokemongostats.view.listeners;

/**
 * @param <T>
 * @author Zapagon
 */
public interface SelectedVisitor<T> {
    void select(final T newItem);
}
