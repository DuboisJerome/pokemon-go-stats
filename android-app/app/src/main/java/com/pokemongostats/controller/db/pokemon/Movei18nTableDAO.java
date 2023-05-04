package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.PkmnMoveTable.IS_ELITE;
import static com.pokemongostats.model.table.PkmnMoveTable.MOVE_ID;
import static com.pokemongostats.model.table.PkmnMoveTable.POKEMON_ID;
import static com.pokemongostats.model.table.PkmnMoveTable.TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;

import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.MoveI18N;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.table.MoveTable;
import com.pokemongostats.model.table.PkmnMoveTable;

import java.util.ArrayList;
import java.util.List;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;

public class Movei18nTableDAO extends TableDAO<MoveI18N> {

	private static Movei18nTableDAO instance;

	private Movei18nTableDAO() {
	}

	public static Movei18nTableDAO getInstance() {
		if (instance == null) {
			instance = new Movei18nTableDAO();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return MoveTable.TABLE_NAME_I18N;
	}

	@Override
	protected ContentValues getContentValues(MoveI18N m) {
		ContentValues cv = getKeyValues(m);
		cv.put(MoveTable.NAME, DatabaseUtils.toStringWithQuotes(m.getName()));
		return cv;
	}

	@Override
	protected ContentValues getKeyValues(MoveI18N m) {
		ContentValues cv = new ContentValues();
		cv.put(MoveTable.ID, m.getId());
		cv.put(MoveTable.LANG, DatabaseUtils.toStringWithQuotes(m.getLang()));
		return cv;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MoveI18N convert(Cursor c) {
		MoveI18N m = new MoveI18N();
		m.setId(DatabaseUtils.getLongCheckNullColumn(c, MoveTable.ID));
		m.setLang(DatabaseUtils.getStringCheckNullColumn(c, MoveTable.LANG));
		m.setName(DatabaseUtils.getStringCheckNullColumn(c, MoveTable.NAME));
		return m;
	}

}