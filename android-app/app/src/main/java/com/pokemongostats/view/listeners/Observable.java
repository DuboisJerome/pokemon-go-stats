package com.pokemongostats.view.listeners;

/**
 * Created by Zapagon on 01/04/2017.
 * Observable interface
 */
public interface Observable {
    /**
     * @param o Observer to register
     */
    void registerObserver(final Observer o);

    /**
     * @param o Observer to unregisters
     */
    void unregisterObserver(final Observer o);

    /**
     * notify all observers
     */
    void notifyObservers();
}
