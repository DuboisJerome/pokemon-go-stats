/**
 * 
 */
package com.pokemongostats.controller.db.pokemon;

import com.pokemongostats.controller.db.DatabaseAsyncDAO;
import com.pokemongostats.model.PokemonDescription;

import android.content.Context;

/**
 * @author Zapagon
 *
 */
public class PokedexAsyncDAO extends DatabaseAsyncDAO<PokemonDescription> {

	public PokedexAsyncDAO(final Context context) {
		super(new PokedexTableDAO(context));
	}

}
