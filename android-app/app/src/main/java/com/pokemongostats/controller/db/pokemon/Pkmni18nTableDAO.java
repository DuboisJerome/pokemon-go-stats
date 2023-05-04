package com.pokemongostats.controller.db.pokemon;

import android.content.ContentValues;
import android.database.Cursor;

import com.pokemongostats.model.bean.bdd.PkmnDescI18N;
import com.pokemongostats.model.table.PkmnTable;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;


public class Pkmni18nTableDAO extends TableDAO<PkmnDescI18N> {

	private static Pkmni18nTableDAO instance;

	private Pkmni18nTableDAO() {
	}

	public static Pkmni18nTableDAO getInstance() {
		if (instance == null) {
			instance = new Pkmni18nTableDAO();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return PkmnTable.TABLE_NAME_I18N;
	}

	@Override
	protected ContentValues getContentValues(PkmnDescI18N p) {
		ContentValues cv = getKeyValues(p);
		cv.put(PkmnTable.NAME, DatabaseUtils.toStringWithQuotes(p.getName()));
		return cv;
	}

	@Override
	protected ContentValues getKeyValues(PkmnDescI18N p) {
		ContentValues cv = new ContentValues();
		cv.put(PkmnTable.ID, p.getId());
		cv.put(PkmnTable.FORM, DatabaseUtils.toStringWithQuotes(p.getForm()));
		cv.put(PkmnTable.LANG, DatabaseUtils.toStringWithQuotes(p.getLang()));
		return cv;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PkmnDescI18N convert(Cursor c) {
		// num dex
		long pokedexNum = DatabaseUtils.getLongCheckNullColumn(c, PkmnTable.ID);

		String form = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.FORM);
		// name
		String name = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.NAME);
		// name
		String lang = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.LANG);

		// i18n
		//String family = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.FAMILY);
		//String description = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.DESCRIPTION);

		PkmnDescI18N p = new PkmnDescI18N();
		p.setId(pokedexNum);
		p.setForm(form);
		p.setName(name);
		p.setLang(lang);

		return p;
	}
}