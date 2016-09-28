/**
 * 
 */
package com.pokemongostats.controller.db.trainer;

import com.pokemongostats.controller.db.DatabaseAsyncDAO;
import com.pokemongostats.model.Trainer;

import android.content.Context;

/**
 * @author Zapagon
 *
 */
public class TrainerAsyncDAO extends DatabaseAsyncDAO<Trainer> {

	public TrainerAsyncDAO(final Context context) {
		super(new TrainerTableDAO(context));
	}

}
