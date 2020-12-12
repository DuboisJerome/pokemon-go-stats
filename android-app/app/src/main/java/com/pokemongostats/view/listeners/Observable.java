package com.pokemongostats.view.listeners;

import java.util.List;

/**
 * Created by Zapagon on 01/04/2017.
 * Observable interface
 */
public interface Observable {

    /**
     * @param o Observer to register
     */
    default void registerObserver(final Observer o) {
        getObservers().add(o);
    }

    /**
     * @param o Observer to unregisters
     */
    default void unregisterObserver(final Observer o) {
        getObservers().remove(o);
    }

    /**
     * notify all observers
     */
    default void notifyObservers() {
        for (Observer o : getObservers()) {
            o.update(this);
        }
    }

    List<Observer> getObservers();
}
