package com.pokemongostats.view.listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zapagon on 01/04/2017.
 */
public class ObservableImpl implements Observable {

    private final List<Observer> observers = new ArrayList<>();
    private final Observable observable;

    public ObservableImpl(Observable o){
        this.observable = o;
    }

    @Override
    public void registerObserver(final Observer o){
        observers.add(o);
    }

    @Override
    public void unregisterObserver(final Observer o){
        observers.remove(o);
    }

    @Override
    public void notifyObservers(){
        for(Observer o : observers){
            o.update(this.observable);
        }
    }
}
