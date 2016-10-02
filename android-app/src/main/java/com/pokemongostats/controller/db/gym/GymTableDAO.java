package com.pokemongostats.controller.db.gym;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pokemongostats.controller.db.DataBaseHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.controller.utils.DateUtils;
import com.pokemongostats.model.Gym;
import com.pokemongostats.model.GymDescription;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.Team;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GymTableDAO extends TableDAO<Gym> {

	// table name
	public static final String TABLE_NAME = "gym";
	// columns names
	public static final String GYM_DESCRIPTION_ID = "gym_description_id";
	public static final String LEVEL = "level";
	public static final String DATE = "date";
	public static final String TEAM = "team";
	public static final String POKEMON_IDS = "pokemon_ids";

	// create query
	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + GYM_DESCRIPTION_ID + " INTEGER NOT NULL, " + LEVEL + " INTEGER, "
			+ DATE + " TEXT NOT NULL, " + TEAM + " TEXT, " + POKEMON_IDS + " TEXT, " + " FOREIGN KEY ("
			+ GYM_DESCRIPTION_ID + ") REFERENCES " + GymDescriptionTableDAO.TABLE_NAME + "(" + GymDescriptionTableDAO.ID
			+ "));";

	public GymTableDAO(Context pContext) {
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
	public List<Long> insertOrReplace(Gym... gyms) {
		if (gyms == null) {
			return new ArrayList<Long>();
		}
		List<Long> returnIds = new ArrayList<Long>(gyms.length);

		SQLiteDatabase db = this.open();
		db.beginTransaction();
		for (Gym gym : gyms) {
			Long gymDescId = gym.getDescription() == null ? null : DataBaseHelper.getIdForDB(gym.getDescription());
			Integer level = gym.getLevel();
			String date = DateUtils.format(gym.getDate());
			String team = gym.getTeam() == null ? null : gym.getTeam().name();

			StringBuilder b = new StringBuilder();
			List<Pokemon> pokemons = gym.getPokemons();
			if (pokemons != null) {
				int size = pokemons.size();
				int lastIndex = size - 1;
				for (int i = 0; i < size; i++) {
					b.append(pokemons.get(i));
					// not the last
					if (i < lastIndex) {
						b.append(Constants.SEPARATOR);
					}
				}
			}
			String pokemonIds = b.toString();

			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, DataBaseHelper.getIdForDB(gym));
			initialValues.put(GYM_DESCRIPTION_ID, gymDescId);
			initialValues.put(LEVEL, level);
			initialValues.put(DATE, date);
			initialValues.put(TEAM, team);
			initialValues.put(POKEMON_IDS, pokemonIds);
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
	protected Gym convert(Cursor c) {
		// name
		final String name = c.getString(c.getColumnIndex(GymDescriptionTableDAO.NAME));
		final GymDescription description = new GymDescription(name);

		// level
		final int level = c.getInt(c.getColumnIndex(LEVEL));
		// date
		final Date date = DateUtils.parse(c.getString(c.getColumnIndex(DATE)));
		// team
		final Team team = Team.valueOfIgnoreCase(c.getString(c.getColumnIndex(TEAM)));
		// pokemons impl ids
		String idsStr = c.getString(c.getColumnIndex(POKEMON_IDS));
		Long[] ids = null;
		if (idsStr != null && !idsStr.isEmpty()) {
			String[] listIdStr = idsStr.split(Constants.SEPARATOR);
			ids = new Long[listIdStr.length];
			String idStr = null;
			for (int i = 0; i < ids.length; i++) {
				try {
					idStr = listIdStr[i];
					if (idStr != null && !idStr.isEmpty()) {
						ids[i] = Long.valueOf(idStr);
					}
				} catch (NumberFormatException e) {
					// do nothing
				}
			}
		}
		// pokemons
		final List<Pokemon> pokemons = new PokemonTableDAO(getContext()).selectAll(ids);

		return new Gym(description, level, date, team, pokemons);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSelectAllQuery(Long... ids) {
		final String query = "SELECT * FROM %s t";
		final String formattedQuery;
		String idsStr = DataBaseHelper.arrayToStringWithSeparators(ids);
		if (idsStr != null && !idsStr.isEmpty()) {
			formattedQuery = String.format(query + " WHERE " + ID + " IN (%s)", this.getTableName(), idsStr);
		} else {
			formattedQuery = String.format(query, this.getTableName());
		}

		return formattedQuery + " JOIN " + GymDescriptionTableDAO.TABLE_NAME + " g ON t." + GYM_DESCRIPTION_ID + "= g."
				+ GymDescriptionTableDAO.ID;
	}

}