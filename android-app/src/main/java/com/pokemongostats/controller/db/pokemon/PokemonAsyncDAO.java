/**
 * 
 */
package com.pokemongostats.controller.db.pokemon;

import com.pokemongostats.controller.db.DatabaseAsyncDAO;
import com.pokemongostats.model.Pokemon;

import android.content.Context;

/**
 * @author Zapagon
 *
 */
public class PokemonAsyncDAO extends DatabaseAsyncDAO<Pokemon> {

	public PokemonAsyncDAO(final Context context) {
		super(new PokemonTableDAO(context));
	}

}
