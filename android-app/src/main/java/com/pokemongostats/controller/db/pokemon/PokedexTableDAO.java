package com.pokemongostats.controller.db.pokemon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.controller.utils.DBHelper;
import com.pokemongostats.model.PokemonDescription;
import com.pokemongostats.model.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PokedexTableDAO extends TableDAO<PokemonDescription> {

	// table name
	public static final String TABLE_NAME = "pokedex";

	// columns names
	public static final String POKEDEX_NUM = "pokedex_num";
	public static final String NAME = "name";
	public static final String TYPES = "types";

	// create query
	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
		+ TABLE_NAME + " (" + POKEDEX_NUM + " INTEGER PRIMARY KEY, " + NAME
		+ " TEXT NOT NULL UNIQUE, " + TYPES + " TEXT NOT NULL);";

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
	public List<Long> insertOrReplace(PokemonDescription... pokemonsDesc) {
		if (pokemonsDesc == null) { return new ArrayList<Long>(); }
		List<Long> returnIds = new ArrayList<Long>(pokemonsDesc.length);
		SQLiteDatabase db = this.open();
		db.beginTransaction();
		for (PokemonDescription p : pokemonsDesc) {
			// id
			Long pokedexNum = DBHelper.getIdForDB(p);
			// name
			String name = p.getName();
			// types
			Set<Type> types = p.getTypes();
			String typesText = null;
			if (types != null) {
				typesText = DBHelper
						.arrayToStringWithSeparators(types.toArray());
			}

			ContentValues initialValues = new ContentValues();
			initialValues.put(POKEDEX_NUM, pokedexNum);
			initialValues.put(NAME, name);
			initialValues.put(TYPES, typesText);
			long id = db.replace(TABLE_NAME, null, initialValues);
			returnIds.add(id);
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		db.close();
		return returnIds;
	}

	/**
	 * @return POKEDEX_NUM table column name
	 */
	@Override
	public String getIdColumnName() {
		return POKEDEX_NUM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PokemonDescription convert(Cursor c) {
		// num dex
		int numPokedex = c.getInt(c.getColumnIndex(POKEDEX_NUM));
		// name
		String name = c.getString(c.getColumnIndex(NAME));
		// types
		String typesStr = c.getString(c.getColumnIndex(TYPES));
		Set<Type> types = new HashSet<Type>();
		if (typesStr != null && !typesStr.isEmpty()) {
			String[] listTypeStr = typesStr.split(Constants.SEPARATOR);
			for (String typeStr : listTypeStr) {
				types.add(Type.valueOfIgnoreCase(typeStr));
			}
		}

		return new PokemonDescription(numPokedex, name, types);
	}
}