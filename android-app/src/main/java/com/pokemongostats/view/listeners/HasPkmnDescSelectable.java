/**
 * 
 */
package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.PokemonDescription;

/**
 * @author Zapagon
 *
 */
public interface HasPkmnDescSelectable {
	public void acceptSelectedVisitorPkmnDesc(
			final SelectedVisitor<PokemonDescription> visitor);

}
