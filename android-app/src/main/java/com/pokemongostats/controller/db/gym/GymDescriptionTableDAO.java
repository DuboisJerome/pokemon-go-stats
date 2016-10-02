package com.pokemongostats.controller.db.gym;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.model.GymDescription;
import com.pokemongostats.model.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GymDescriptionTableDAO extends TableDAO<GymDescription> {

	// table name
	public static final String TABLE_NAME = "gym_description";

	// columns names
	public static final String NAME = "name";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	// create query
	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL UNIQUE, " + LATITUDE + " REAL, "
			+ LONGITUDE + " REAL" + ");";

	public GymDescriptionTableDAO(Context pContext) {
		super(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> insertOrReplace(GymDescription... gymsDescriptions) {
		if (gymsDescriptions == null) {
			return new ArrayList<Long>();
		}
		List<Long> returnIds = new ArrayList<Long>(gymsDescriptions.length);
		SQLiteDatabase db = this.open();
		db.beginTransaction();
		for (GymDescription gymDesc : gymsDescriptions) {
			if (gymDesc != null) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(ID, gymDesc.getId());
				initialValues.put(NAME, gymDesc.getName());
				Location location = gymDesc.getLocation();
				if (location != null && location.getLatitude() != null && location.getLongitude() != null) {
					initialValues.put(LATITUDE, location.getLatitude());
					initialValues.put(LONGITUDE, location.getLongitude());
				}
				long id = db.replace(TABLE_NAME, null, initialValues);
				returnIds.add(id);
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		db.close();
		return returnIds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GymDescription convert(Cursor c) {
		return new GymDescription(c.getString(c.getColumnIndex(NAME)));
	}
}