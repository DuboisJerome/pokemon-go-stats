package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.PkmnMoveTable.FORM;
import static com.pokemongostats.model.table.PkmnMoveTable.POKEMON_ID;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.table.PkmnTable;

import java.util.List;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.controller.utils.DatabaseUtils;
import fr.commons.generique.controller.utils.TagUtils;


public class PkmnTableDAO extends TableDAO<PkmnDesc> {

	private static PkmnTableDAO instance;

	private PkmnTableDAO() {
	}

	public static PkmnTableDAO getInstance() {
		if (instance == null) {
			instance = new PkmnTableDAO();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return PkmnTable.TABLE_NAME;
	}

	@Override
	protected ContentValues getContentValues(PkmnDesc bo) {
		throw new UnsupportedOperationException("Pas d'insertion");
	}

	@Override
	protected ContentValues getKeyValues(PkmnDesc bo) {
		ContentValues cv = new ContentValues();
		cv.put(POKEMON_ID, bo.getPokedexNum());
		cv.put(FORM, DatabaseUtils.toStringWithQuotes(bo.getForm()));
		return cv;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PkmnDesc convert(Cursor c) {
		// num dex
		long pokedexNum = DatabaseUtils.getLongCheckNullColumn(c, PkmnTable.ID);

		String form = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.FORM);
		// name
		String name = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.NAME);
		// type1
		Type type1 = Type
				.valueOfIgnoreCase(DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.TYPE1));
		// type2
		Type type2 = Type
				.valueOfIgnoreCase(DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.TYPE2));

		int physicalAttack = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.PHYSICAL_ATTACK);
		int physicalDefense = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.PHYSICAL_DEFENSE);
		int specialAttack = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.SPECIAL_ATTACK);
		int specialDefense = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.SPECIAL_DEFENSE);
		int pv = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.PV);
		int speed = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.SPEED);

		int stamina = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.STAMINA);
		int attack = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.ATTACK);
		int defense = DatabaseUtils.getIntCheckNullColumn(c, PkmnTable.DEFENSE);

		double kmsPerCandy = DatabaseUtils.getDoubleCheckNullColumn(c,
				PkmnTable.KMS_PER_CANDY);

		double kmsPerEgg = DatabaseUtils.getDoubleCheckNullColumn(c, PkmnTable.KMS_PER_EGG);

		String tagsStr = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.TAGS);

		// i18n
		//String family = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.FAMILY);
		//String description = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.DESCRIPTION);

		PkmnDesc p = new PkmnDesc();
		p.setPokedexNum(pokedexNum);
		p.setForm(form);
		p.setName(name);
		p.setType1(type1);
		p.setType2(type2);
		//p.setFamily(family);
		//p.setDescription(description);
		p.setKmsPerCandy(kmsPerCandy);
		p.setKmsPerEgg(kmsPerEgg);
		p.setTags(tagsStr);

		p.setStamina(stamina);
		p.setAttack(attack);
		p.setDefense(defense);

		p.setPhysicalAttack(physicalAttack);
		p.setPhysicalDefense(physicalDefense);
		p.setSpecialAttack(specialAttack);
		p.setSpecialDefense(specialDefense);
		p.setPv(pv);
		p.setSpeed(speed);

		return p;
	}

	/**
	 * @param whereClause w
	 * @return select query
	 */
	@Override
	public String getSelectAllQuery(String whereClause) {
		StringBuilder b = new StringBuilder();
		b.append("SELECT *");
		b.append(" FROM ").append(PkmnTable.TABLE_NAME);

		// join pokedex lang
		b.append(" JOIN ").append(PkmnTable.TABLE_NAME_I18N).append(" ON ");
		b.append(" ( ");
		b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.ID);
		b.append("=");
		b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.ID);
		b.append(" AND ");
		b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.FORM);
		b.append("=");
		b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.FORM);
		b.append(" AND ");
		b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.LANG);
		b.append(" LIKE ").append(DatabaseUtils.toStringWithQuotes(DatabaseUtils.getLang()));
		b.append(" ) ");

		if (whereClause != null && !whereClause.isEmpty()) {
			b.append(" WHERE ").append(whereClause);
		}
		b.append(" ORDER BY ");
		b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.ID).append(",");
		b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.FORM).append(" DESC");

		Log.i(TagUtils.DEBUG, b.toString());
		return b.toString();
	}

	public PkmnDesc getPkmnDesc(long pokedexNum) {
		List<PkmnDesc> results = selectAll(getSelectAllQuery(PkmnTable.ID + "=" + pokedexNum));
		return results.isEmpty() ? null : results.get(0);
	}

}