package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.PkmnMoveTable.IS_ELITE;
import static com.pokemongostats.model.table.PkmnMoveTable.MOVE_ID;
import static com.pokemongostats.model.table.PkmnMoveTable.POKEMON_ID;
import static com.pokemongostats.model.table.PkmnMoveTable.TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;
import com.pokemongostats.model.table.PkmnMoveTable;

import java.util.ArrayList;
import java.util.List;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;

public class PkmnMoveTableDAO extends TableDAO<PkmnMove> {

	private static PkmnMoveTableDAO instance;

	private PkmnMoveTableDAO() {
	}

	public static PkmnMoveTableDAO getInstance() {
		if (instance == null) {
			instance = new PkmnMoveTableDAO();
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

	@Override
	protected ContentValues getContentValues(PkmnMove bo) {
		return getKeyValues(bo);
	}

	@Override
	protected ContentValues getKeyValues(PkmnMove bo) {
		ContentValues cv = new ContentValues();
		cv.put(MOVE_ID, bo.getMoveId());
		cv.put(POKEMON_ID, bo.getPokedexNum());
		cv.put(PkmnMoveTable.FORM, bo.getForm());
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PkmnMove convert(Cursor c) {
		PkmnMove pm = new PkmnMove();
		pm.setMoveId(DatabaseUtils.getLongCheckNullColumn(c, MOVE_ID));
		pm.setPokedexNum(DatabaseUtils.getLongCheckNullColumn(c, POKEMON_ID));
		pm.setForm(DatabaseUtils.getStringCheckNullColumn(c, PkmnMoveTable.FORM));
		pm.setElite(DatabaseUtils.getBoolCheckNullColumn(c, IS_ELITE));
		return pm;
	}

	@NonNull
	public List<Long> getListMoveIdFor(PkmnDesc p) {
		List<Long> results = new ArrayList<>();
		String query = getSelectAllQuery(PkmnMoveTable.POKEMON_ID + " = " + p.getPokedexNum());
		List<PkmnMove> list = this.selectAll(query);
		for (PkmnMove pm : list) {
			results.add(pm.getMoveId());
		}
		return results;
	}

	@NonNull
	public List<Long> getListPkmnNumFor(Move m) {
		List<Long> results = new ArrayList<>();
		String query = getSelectAllQuery(PkmnMoveTable.MOVE_ID + " = " + m.getId());
		List<PkmnMove> list = this.selectAll(query);
		for (PkmnMove pm : list) {
			results.add(pm.getPokedexNum());
		}
		return results;
	}
}