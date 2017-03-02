/**
 * 
 */
package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 *
 */
public interface HasTypeSelectable {
	public void acceptSelectedVisitorType(final SelectedVisitor<Type> visitor);
}
