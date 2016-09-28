/**
 * 
 */
package com.pokemongostats.controller.db.gym;

import com.pokemongostats.controller.db.DatabaseAsyncDAO;
import com.pokemongostats.model.Gym;

import android.content.Context;

/**
 * @author Zapagon
 *
 */
public class GymAsyncDAO extends DatabaseAsyncDAO<Gym> {

	public GymAsyncDAO(final Context context) {
		super(new GymTableDAO(context));
	}

}
