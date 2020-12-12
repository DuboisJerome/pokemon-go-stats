package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public interface HasTypeSelectable {
    void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor);
}
