package com.pokemongostats.controller.db.gym;

import static com.pokemongostats.model.table.AbstractTable.ID;
import static com.pokemongostats.model.table.GymDescriptionTable.DESCRIPTION;
import static com.pokemongostats.model.table.GymDescriptionTable.LATITUDE;
import static com.pokemongostats.model.table.GymDescriptionTable.LONGITUDE;
import static com.pokemongostats.model.table.GymDescriptionTable.NAME;
import static com.pokemongostats.model.table.GymTable.DATE;
import static com.pokemongostats.model.table.GymTable.GYM_DESCRIPTION_ID;
import static com.pokemongostats.model.table.GymTable.LEVEL;
import static com.pokemongostats.model.table.GymTable.POKEMON_IDS;
import static com.pokemongostats.model.table.GymTable.TABLE_NAME;
import static com.pokemongostats.model.table.GymTable.TEAM;

import java.util.Date;
import java.util.List;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.controller.utils.DateUtils;
import com.pokemongostats.model.bean.Gym;
import com.pokemongostats.model.bean.GymDescription;
import com.pokemongostats.model.bean.Location;
import com.pokemongostats.model.bean.Pokemon;
import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.table.GymDescriptionTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GymTableDAO extends TableDAO<com.pokemongostats.model.bean.Gym> {

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
	protected Gym convert(Cursor c) {
		// gym desc

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
		GymDescription gymDesc = new GymDescription();
		gymDesc.setName(name);
		gymDesc.setId(DBHelper.getLongCheckNullColumn(c, GYM_DESCRIPTION_ID));
		gymDesc.setLocation(location);
		gymDesc.setDescription(description);

		// id
		long id = DBHelper.getLongCheckNullColumn(c, ID);
		// level
		int level = DBHelper.getIntCheckNullColumn(c, LEVEL);
		// date
		Date date = DateUtils.parse(DBHelper.getStringCheckNullColumn(c, DATE));
		// team
		Team team = Team
				.valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TEAM));
		// pokemons impl ids
		String idsStr = DBHelper.getStringCheckNullColumn(c, POKEMON_IDS);
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
		List<Pokemon> pokemons = new PkmnTableDAO(getContext()).selectAllIn(ID,
				false, ids);

		Gym g = new Gym();
		g.setGymDesc(gymDesc);
		g.setLevel(level);
		g.setDate(date);
		g.setTeam(team);
		g.setPokemons(pokemons);
		g.setId(id);
		return g;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSelectAllQuery(final String whereClause) {
		return super.getSelectAllQuery(whereClause) + " JOIN "
			+ GymDescriptionTable.TABLE_NAME + " ON " + TABLE_NAME + "."
			+ GYM_DESCRIPTION_ID + "= " + GymDescriptionTable.TABLE_NAME + "."
			+ GymDescriptionTable.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ContentValues getContentValues(final Gym gym) {
		Long gymDescId = gym.getGymDesc() == null
				? null
				: DBHelper.getIdForDB(gym.getGymDesc());
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
		initialValues.put(ID, DBHelper.getIdForDB(gym));
		initialValues.put(GYM_DESCRIPTION_ID, gymDescId);
		initialValues.put(LEVEL, level);
		initialValues.put(DATE, date);
		initialValues.put(TEAM, team);
		initialValues.put(POKEMON_IDS, pokemonIds);

		return initialValues;
	}

	@Override
	public int removeFromObjects(Gym... objects) {
		// TODO Auto-generated method stub
		return 0;
	}

}