package com.pokemongostats.controller.db.pokemon;

import android.content.ContentValues;
import android.database.Cursor;

import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.table.EvolutionTable;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;

public class EvolutionTableDAO extends TableDAO<Evolution> {

	private static EvolutionTableDAO instance;

	private EvolutionTableDAO() {
	}

	public static EvolutionTableDAO getInstance() {
		if (instance == null) {
			instance = new EvolutionTableDAO();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return EvolutionTable.TABLE_NAME;
	}

	@Override
	protected ContentValues getContentValues(Evolution evolution) {
		return getKeyValues(evolution);
	}

	@Override
	protected ContentValues getKeyValues(Evolution evolution) {
		ContentValues cv = new ContentValues();
		cv.put(EvolutionTable.EVOLUTION_ID, evolution.getEvolutionId());
		cv.put(EvolutionTable.EVOLUTION_FORM, evolution.getEvolutionForm());
		cv.put(EvolutionTable.BASE_PKMN_ID, evolution.getBasePkmnId());
		cv.put(EvolutionTable.BASE_PKMN_FORM, evolution.getBasePkmnForm());
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Evolution convert(Cursor c) {
		Evolution e = new Evolution();
		e.setEvolutionId(DatabaseUtils.getLongCheckNullColumn(c, EvolutionTable.EVOLUTION_ID));
		e.setEvolutionForm(DatabaseUtils.getStringCheckNullColumn(c, EvolutionTable.EVOLUTION_FORM));
		e.setBasePkmnId(DatabaseUtils.getLongCheckNullColumn(c, EvolutionTable.BASE_PKMN_ID));
		e.setBasePkmnForm(DatabaseUtils.getStringCheckNullColumn(c, EvolutionTable.BASE_PKMN_FORM));
		e.setTemporaire(DatabaseUtils.getBoolCheckNullColumn(c, EvolutionTable.IS_TEMPORAIRE));
		e.setCandyToEvolve(DatabaseUtils.getIntCheckNullColumn(c, EvolutionTable.CANDY_TO_EVOLVE));
		return e;
	}
}