package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.PokemonMoveTable.MOVE_ID;
import static com.pokemongostats.model.table.PokemonMoveTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PokemonMoveTable.TABLE_NAME;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.model.bean.PokemonMove;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PokemonMoveTableDAO extends TableDAO<PokemonMove> {

	public PokemonMoveTableDAO(Context pContext) {
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
	protected PokemonMove convert(Cursor c) {
		PokemonMove pm = new PokemonMove();
		pm.setMoveId(DBHelper.getLongCheckNullColumn(c, MOVE_ID));
		pm.setPokedexNum(DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM));

		return pm;
	}

	/**
	 * @param whereClause
	 * @return select query
	 */
	@Override
	protected String getSelectAllQuery(final String whereClause) {
		StringBuilder b = new StringBuilder();
		b.append("SELECT *");
		b.append(" FROM ").append(TABLE_NAME);

		if (whereClause != null && !whereClause.isEmpty()) {
			b.append(" WHERE ").append(whereClause);
		}

		Log.i("SELECT_ALL", b.toString());
		return b.toString();
	}
}