package com.pokemongostats.controller.db.pokemon;

import android.content.ContentValues;
import android.database.Cursor;

import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.table.EvolutionTable;

import java.util.List;

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
		ContentValues cv = new ContentValues();
		cv.put(EvolutionTable.EVOLUTION_ID, evolution.getEvolutionId());
		cv.put(EvolutionTable.EVOLUTION_FORM, DatabaseUtils.toStringWithQuotes(evolution.getEvolutionForm()));
		cv.put(EvolutionTable.BASE_PKMN_ID, evolution.getBasePkmnId());
		cv.put(EvolutionTable.BASE_PKMN_FORM, DatabaseUtils.toStringWithQuotes(evolution.getBasePkmnForm()));
		cv.put(EvolutionTable.CANDY_TO_EVOLVE, evolution.getCandyToEvolve());
		cv.put(EvolutionTable.OBJECT_TO_EVOLVE, DatabaseUtils.toStringWithQuotes(""));
		cv.put(EvolutionTable.IS_TEMPORAIRE, evolution.isTemporaire());
		return cv;
	}

	@Override
	protected ContentValues getKeyValues(Evolution evolution) {
		ContentValues cv = new ContentValues();
		cv.put(EvolutionTable.EVOLUTION_ID, evolution.getEvolutionId());
		cv.put(EvolutionTable.EVOLUTION_FORM, DatabaseUtils.toStringWithQuotes(evolution.getEvolutionForm()));
		cv.put(EvolutionTable.BASE_PKMN_ID, evolution.getBasePkmnId());
		cv.put(EvolutionTable.BASE_PKMN_FORM, DatabaseUtils.toStringWithQuotes(evolution.getBasePkmnForm()));
		return cv;
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

	public List<Evolution> getBasesEtEvols(long pokedexNum, String form) {
		String whereBase = EvolutionTable.BASE_PKMN_ID + "=" + pokedexNum + " AND " + EvolutionTable.BASE_PKMN_FORM + "='" + form + "'";
		String whereEvol = EvolutionTable.EVOLUTION_ID + "=" + pokedexNum + " AND " + EvolutionTable.EVOLUTION_FORM + "='" + form + "'";
		return selectAll(getSelectAllQuery("((" + whereBase + ") OR (" + whereEvol + "))"));
	}

	public List<Evolution> getBases(long pokedexNum, String form) {
		String whereEvol = EvolutionTable.EVOLUTION_ID + "=" + pokedexNum + " AND " + EvolutionTable.EVOLUTION_FORM + "='" + form + "'";
		return selectAll(getSelectAllQuery("(" + whereEvol + ")"));
	}

	public List<Evolution> getEvols(long pokedexNum, String form) {
		String whereBase = EvolutionTable.BASE_PKMN_ID + "=" + pokedexNum + " AND " + EvolutionTable.BASE_PKMN_FORM + "='" + form + "'";
		return selectAll(getSelectAllQuery("(" + whereBase + ")"));
	}

}