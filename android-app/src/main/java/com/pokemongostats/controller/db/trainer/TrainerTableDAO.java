package com.pokemongostats.controller.db.trainer;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.DBHelper;
import com.pokemongostats.model.Team;
import com.pokemongostats.model.Trainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TrainerTableDAO extends TableDAO<Trainer> {

	// table name
	public static final String TABLE_NAME = "trainer";
	// columns name
	public static final String NAME = "name";
	public static final String LEVEL = "level";
	public static final String TEAM = "team";

	// create query
	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
		+ TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
		+ " TEXT UNIQUE, " + LEVEL + " INTEGER, " + TEAM + " TEXT);";

	public TrainerTableDAO(Context pContext) {
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
	public List<Long> insertOrReplace(Trainer... trainers) {
		if (trainers == null) { return new ArrayList<Long>(); }
		List<Long> returnIds = new ArrayList<Long>(trainers.length);

		SQLiteDatabase db = this.open();
		db.beginTransaction();
		for (Trainer trainer : trainers) {
			String name = trainer.getName();
			Integer level = trainer.getLevel();
			String team = trainer.getTeam() == null
					? null
					: trainer.getTeam().name();

			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, DBHelper.getIdForDB(trainer));
			initialValues.put(LEVEL, level);
			initialValues.put(NAME, name);
			initialValues.put(TEAM, team);
			long id = db.replace(TABLE_NAME, null, initialValues);
			returnIds.add(id);
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
	protected Trainer convert(Cursor c) {
		// name
		String name = c.getString(c.getColumnIndex(NAME));
		// level
		int level = c.getInt(c.getColumnIndex(LEVEL));
		// team
		Team team = Team.valueOfIgnoreCase(c.getString(c.getColumnIndex(TEAM)));

		return new Trainer(name, level, team);
	}
}