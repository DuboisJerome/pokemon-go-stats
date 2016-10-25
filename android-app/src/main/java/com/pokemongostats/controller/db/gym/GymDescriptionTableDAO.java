package com.pokemongostats.controller.db.gym;

import static com.pokemongostats.model.table.AbstractTable.ID;
import static com.pokemongostats.model.table.GymDescriptionTable.DESCRIPTION;
import static com.pokemongostats.model.table.GymDescriptionTable.LATITUDE;
import static com.pokemongostats.model.table.GymDescriptionTable.LONGITUDE;
import static com.pokemongostats.model.table.GymDescriptionTable.NAME;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.model.bean.GymDescription;
import com.pokemongostats.model.bean.Location;
import com.pokemongostats.model.table.GymDescriptionTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GymDescriptionTableDAO extends TableDAO<GymDescription> {

	public GymDescriptionTableDAO(Context pContext) {
		super(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableName() {
		return GymDescriptionTable.TABLE_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GymDescription convert(Cursor c) {
		// id
		long id = DBHelper.getLongCheckNullColumn(c, ID);
		// name
		String name = DBHelper.getStringCheckNullColumn(c, NAME);
		// description
		String description = DBHelper.getStringCheckNullColumn(c, DESCRIPTION);
		// location
		double lat = DBHelper.getDoubleCheckNullColumn(c, LATITUDE);
		double lon = DBHelper.getDoubleCheckNullColumn(c, LONGITUDE);
		Location location = null;
		if (lat != 0 && lon != 0) {
			location = new Location(lat, lon);
		}

		GymDescription g = new GymDescription();
		g.setName(name);
		g.setLocation(location);
		g.setDescription(description);
		g.setId(id);

		return g;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues getContentValues(final GymDescription gymDesc) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, DBHelper.getIdForDB(gymDesc));
		initialValues.put(NAME, gymDesc.getName());
		initialValues.put(DESCRIPTION, gymDesc.getDescription());
		Location location = gymDesc.getLocation();
		if (location != null && location.getLatitude() != 0d
			&& location.getLongitude() != 0d) {
			initialValues.put(LATITUDE, location.getLatitude());
			initialValues.put(LONGITUDE, location.getLongitude());
		}
		return initialValues;
	}

	@Override
	public int removeFromObjects(GymDescription... objects) {
		// TODO Auto-generated method stub
		return 0;
	}
}