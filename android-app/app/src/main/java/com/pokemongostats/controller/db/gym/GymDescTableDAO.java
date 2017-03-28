package com.pokemongostats.controller.db.gym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.model.bean.GymDesc;
import com.pokemongostats.model.bean.Location;
import com.pokemongostats.model.table.GymDescTable;

import static com.pokemongostats.model.table.AbstractTable.ID;
import static com.pokemongostats.model.table.GymDescTable.DESCRIPTION;
import static com.pokemongostats.model.table.GymDescTable.LATITUDE;
import static com.pokemongostats.model.table.GymDescTable.LONGITUDE;
import static com.pokemongostats.model.table.GymDescTable.NAME;

public class GymDescTableDAO extends TableDAO<GymDesc> {

    public GymDescTableDAO(Context pContext) {
        super(pContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return GymDescTable.TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GymDesc convert(Cursor c) {
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

        GymDesc g = new GymDesc();
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
    protected ContentValues getContentValues(final GymDesc gymDesc) {
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
    public int removeFromObjects(GymDesc... objects) {
        // TODO Auto-generated method stub
        return 0;
    }
}