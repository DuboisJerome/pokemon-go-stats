package com.pokemongostats.controller.db.trainer;

import static com.pokemongostats.model.table.AbstractTable.ID;
import static com.pokemongostats.model.table.TrainerTable.LEVEL;
import static com.pokemongostats.model.table.TrainerTable.NAME;
import static com.pokemongostats.model.table.TrainerTable.TABLE_NAME;
import static com.pokemongostats.model.table.TrainerTable.TEAM;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.bean.Trainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TrainerTableDAO extends TableDAO<Trainer> {

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
	protected Trainer convert(Cursor c) {
		// id
		long id = DBHelper.getLongCheckNullColumn(c, ID);
		// name
		String name = DBHelper.getStringCheckNullColumn(c, NAME);
		// level
		int level = DBHelper.getIntCheckNullColumn(c, LEVEL);
		// team
		Team team = Team
				.valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TEAM));

		Trainer t = new Trainer(name, level, team);
		t.setId(id);
		return t;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues getContentValues(final Trainer trainer) {
		String name = trainer.getName();
		Integer level = trainer.getLevel();
		String team = trainer.getTeam() == null
				? null
				: trainer.getTeam().name();

		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, DBHelper.getIdForDB(trainer));
		initialValues.put(NAME, name);
		initialValues.put(LEVEL, level);
		initialValues.put(TEAM, team);

		return initialValues;
	}

	@Override
	public int removeFromObjects(Trainer... objects) {
		// TODO Auto-generated method stub
		return 0;
	}
}