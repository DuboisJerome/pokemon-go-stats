/**
 * 
 */
package com.pokemongostats.controller.db.gym;

import com.pokemongostats.controller.db.DatabaseAsyncDAO;
import com.pokemongostats.model.GymDescription;

import android.content.Context;

/**
 * @author Zapagon
 *
 */
public class GymDescriptionAsyncDAO extends DatabaseAsyncDAO<GymDescription> {

	public GymDescriptionAsyncDAO(final Context context) {
		super(new GymDescriptionTableDAO(context));
	}

}
