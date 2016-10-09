package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.AbstractTable.LANG;
import static com.pokemongostats.model.table.PokedexTable.DESCRIPTION;
import static com.pokemongostats.model.table.PokedexTable.FAMILY;
import static com.pokemongostats.model.table.PokedexTable.NAME;
import static com.pokemongostats.model.table.PokedexTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PokedexTable.TABLE_NAME;
import static com.pokemongostats.model.table.PokedexTable.TYPE1;
import static com.pokemongostats.model.table.PokedexTable.TYPE2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.table.EvolutionTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class PokedexTableDAO extends TableDAO<PokemonDescription> {

	public static final String TABLE_NAME_I18N = TABLE_NAME + "_i18n";

	public PokedexTableDAO(Context pContext) {
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
	public List<PokemonDescription> selectAll(final String query) {
		List<PokemonDescription> l = super.selectAll(query);

		Map<Long, PokemonDescription> map = new HashMap<Long, PokemonDescription>();
		for (PokemonDescription p : l) {
			PokemonDescription found = map.get(p.getPokedexNum());
			if (found != null) {
				found.getEvolutionIds().addAll(p.getEvolutionIds());
			} else {
				map.put(p.getPokedexNum(), p);
			}
		}

		return new ArrayList<PokemonDescription>(map.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PokemonDescription convert(Cursor c) {
		// num dex
		long pokedexNum = DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM);
		// name
		String name = DBHelper.getStringCheckNullColumn(c, NAME);
		// type1
		Type type1 = Type
				.valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TYPE1));
		// type2
		Type type2 = Type
				.valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TYPE2));

		String family = DBHelper.getStringCheckNullColumn(c, FAMILY);
		String description = DBHelper.getStringCheckNullColumn(c, DESCRIPTION);
		long evolutionId = DBHelper.getLongCheckNullColumn(c,
				EvolutionTable.EVOLUTION_ID);

		PokemonDescription p = new PokemonDescription(pokedexNum, name, type1,
				type2);
		p.setFamily(family);
		p.setDescription(description);
		p.addEvolutionId(evolutionId);

		return p;
	}

	@Override
	public Long[] insertOrReplace(final PokemonDescription... bos) {
		List<Long> result = new ArrayList<Long>();

		if (bos != null && bos.length > 0) {
			SQLiteDatabase db = this.open();
			db.beginTransaction();
			try {
				for (PokemonDescription p : bos) {
					if (p != null) {
						// id
						Long pokedexNum = DBHelper.getIdForDB(p);

						ContentValues commonValues = new ContentValues();
						commonValues.put(POKEDEX_NUM, pokedexNum);
						// commonValues.put(EVOLUTION_ID, p.getEvolutionNum());
						commonValues.put(TYPE1, p.getType1() == null
								? null
								: p.getType1().name());
						commonValues.put(TYPE2, p.getType2() == null
								? null
								: p.getType2().name());

						long id = db.replaceOrThrow(TABLE_NAME, null,
								commonValues);
						if (id == -1) { throw new SQLiteException(
								"Error inserting following object : " + p); }

						ContentValues i18nValues = new ContentValues();
						i18nValues.put(POKEDEX_NUM, pokedexNum);
						i18nValues.put(LANG, Constants.FR); // TODO android
															// constant
						i18nValues.put(NAME, p.getName());
						i18nValues.put(FAMILY, p.getFamily());
						i18nValues.put(DESCRIPTION, p.getDescription());
						id = db.replaceOrThrow(TABLE_NAME_I18N, null,
								i18nValues);
						if (id == -1) { throw new SQLiteException(
								"Error inserting following object i18n : "
									+ p); }

						result.add(id);
					}
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			} finally {
				db.close();
			}
		}

		return result.toArray(new Long[result.size()]);
	}

	@Override
	public int removeFromObjects(PokemonDescription... objects) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param whereClause
	 * @return select query
	 */
	@Override
	protected String getSelectAllQuery(final String whereClause) {
		StringBuilder b = new StringBuilder();
		b.append("SELECT *");
		b.append(", COALESCE(");
		b.append(EvolutionTable.TABLE_NAME).append(".")
				.append(EvolutionTable.POKEDEX_NUM);
		b.append(",");
		b.append(TABLE_NAME).append(".").append(POKEDEX_NUM);
		b.append(",");
		b.append(TABLE_NAME_I18N).append(".").append(POKEDEX_NUM);
		b.append(") as ").append(POKEDEX_NUM);
		b.append(" FROM ").append(TABLE_NAME);

		// join lang
		b.append(" JOIN ").append(TABLE_NAME_I18N).append(" ON ");
		b.append(TABLE_NAME).append(".").append(POKEDEX_NUM);
		b.append("=");
		b.append(TABLE_NAME_I18N).append(".").append(POKEDEX_NUM);

		// join evolution
		b.append(" LEFT JOIN ").append(EvolutionTable.TABLE_NAME)
				.append(" ON ");
		b.append(TABLE_NAME).append(".").append(POKEDEX_NUM);
		b.append("=");
		b.append(EvolutionTable.TABLE_NAME).append(".")
				.append(EvolutionTable.POKEDEX_NUM);

		// get lang
		b.append(" WHERE ").append(TABLE_NAME_I18N).append(".").append(LANG);
		b.append(" LIKE ").append(DBHelper.toStringWithQuotes(Constants.FR));

		Log.e("SELECT_ALL", b.toString());
		return b.toString();
	}
}