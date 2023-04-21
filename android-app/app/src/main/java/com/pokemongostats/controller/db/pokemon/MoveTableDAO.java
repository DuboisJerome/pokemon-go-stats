package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.AbstractTable.LANG;
import static com.pokemongostats.model.table.MoveTable.CRITICAL_CHANCE;
import static com.pokemongostats.model.table.MoveTable.DURATION;
import static com.pokemongostats.model.table.MoveTable.DURATION_PVP;
import static com.pokemongostats.model.table.MoveTable.ENERGY_DELTA;
import static com.pokemongostats.model.table.MoveTable.ENERGY_PVP;
import static com.pokemongostats.model.table.MoveTable.ID;
import static com.pokemongostats.model.table.MoveTable.MOVE_TYPE;
import static com.pokemongostats.model.table.MoveTable.NAME;
import static com.pokemongostats.model.table.MoveTable.POWER;
import static com.pokemongostats.model.table.MoveTable.POWER_PVP;
import static com.pokemongostats.model.table.MoveTable.STAMINA_LOSS_SCALAR;
import static com.pokemongostats.model.table.MoveTable.TABLE_NAME;
import static com.pokemongostats.model.table.MoveTable.TABLE_NAME_I18N;
import static com.pokemongostats.model.table.MoveTable.TYPE;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Type;

import java.util.List;

import fr.commons.generique.controller.dao.AbstractObjetBddAvecIdDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;

public class MoveTableDAO extends AbstractObjetBddAvecIdDAO<Move> {

	private static MoveTableDAO instance;

	private MoveTableDAO() {
	}

	public static MoveTableDAO getInstance() {
		if (instance == null) {
			instance = new MoveTableDAO();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return TABLE_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Move convert(Cursor c) {
		// num dex
		long id = DatabaseUtils.getLongCheckNullColumn(c, ID);
		// name
		String name = DatabaseUtils.getStringCheckNullColumn(c, NAME);
		// type
		Type type = Type
				.valueOfIgnoreCase(DatabaseUtils.getStringCheckNullColumn(c, TYPE));
		// move type
		MoveType moveType = Move.MoveType.valueOfIgnoreCase(
				DatabaseUtils.getStringCheckNullColumn(c, MOVE_TYPE));

		double criticalChance = DatabaseUtils.getDoubleCheckNullColumn(c,
				CRITICAL_CHANCE);
		int duration = DatabaseUtils.getIntCheckNullColumn(c, DURATION);
		double staminaLossScalar = DatabaseUtils.getDoubleCheckNullColumn(c,
				STAMINA_LOSS_SCALAR);
		int energyDelta = DatabaseUtils.getIntCheckNullColumn(c, ENERGY_DELTA);
		int power = DatabaseUtils.getIntCheckNullColumn(c, POWER);
		int powerPvp = DatabaseUtils.getIntCheckNullColumn(c, POWER_PVP);
		int energyPvp = DatabaseUtils.getIntCheckNullColumn(c, ENERGY_PVP);
		int durationPvp = DatabaseUtils.getIntCheckNullColumn(c, DURATION_PVP);

		Move m = new Move();
		m.setId(id);
		m.setType(type);
		m.setMoveType(moveType);
		m.setCriticalChance(criticalChance);
		m.setDuration(duration);
		m.setEnergyDelta(energyDelta);
		m.setName(name);
		m.setPower(power);
		m.setStaminaLossScalar(staminaLossScalar);
		m.setPowerPvp(powerPvp);
		m.setEnergyPvp(energyPvp);
		m.setDurationPvp(durationPvp);

		return m;
	}

	/**
	 * @param whereClause
	 * @return select query
	 */
	@Override
	public String getSelectAllQuery(String whereClause) {
		StringBuilder b = new StringBuilder();
		b.append("SELECT *");
		b.append(" FROM ").append(TABLE_NAME);

		// join pokedex lang
		b.append(" JOIN ").append(TABLE_NAME_I18N).append(" ON ");
		b.append(TABLE_NAME).append(".").append(ID);
		b.append("=");
		b.append(TABLE_NAME_I18N).append(".").append(ID);

		b.append(" WHERE ");
		b.append(TABLE_NAME_I18N).append(".").append(LANG);
		b.append(" LIKE ").append(DatabaseUtils.toStringWithQuotes(DatabaseUtils.getLang()));

		if (whereClause != null && !whereClause.isEmpty()) {
			b.append(" AND ").append(whereClause);
		}

		Log.i(TagUtils.DB, b.toString());
		return b.toString();
	}

	public Move getMove(long id) {
		List<Move> results = selectAll(getSelectAllQuery(ID + "=" + id));
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	protected ContentValues getContentValues(Move bo) {
		throw new UnsupportedOperationException("Pas d'insertion");
	}
}