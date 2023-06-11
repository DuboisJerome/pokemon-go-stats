package com.pokemongostats.controller.db.pokemon;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pokemongostats.controller.utils.LangUtils;
import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnDescI18N;
import com.pokemongostats.model.table.PkmnTable;

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

	@Override
	public void creer(PkmnDesc bo) {
		super.creer(bo, SQLiteDatabase.CONFLICT_IGNORE);
		Pkmni18nTableDAO.getInstance().creer(bo.getI18n(), SQLiteDatabase.CONFLICT_IGNORE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNomTable() {
		return PkmnTable.TABLE_NAME;
	}

	@Override
	protected ContentValues getContentValues(PkmnDesc p) {
		ContentValues cv = getKeyValues(p);
		cv.put(PkmnTable.ATTACK, p.getAttack());
		cv.put(PkmnTable.DEFENSE, p.getDefense());
		cv.put(PkmnTable.STAMINA, p.getStamina());
		cv.put(PkmnTable.KMS_PER_CANDY, p.getKmsPerCandy());
		cv.put(PkmnTable.TYPE1, p.getType1() != null ? p.getType1().name() : Type.NORMAL.name());
		cv.put(PkmnTable.TYPE2, p.getType2() != null ? p.getType2().name() : "");
		cv.put(PkmnTable.TAGS, String.join(",", p.getTags()));
		return cv;
	}

	@Override
	protected ContentValues getKeyValues(PkmnDesc p) {
		ContentValues cv = new ContentValues();
		cv.put(PkmnTable.ID, p.getPokedexNum());
		cv.put(PkmnTable.FORM, p.getForm());
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
		// lang
		String lang = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.LANG);
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

		String tagsStr = DatabaseUtils.getStringCheckNullColumn(c, PkmnTable.TAGS);

		PkmnDesc p = new PkmnDesc();
		p.setPokedexNum(pokedexNum);
		p.setForm(form);
		p.setType1(type1);
		p.setType2(type2);
		p.setKmsPerCandy(kmsPerCandy);
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


		PkmnDescI18N i18n = p.getI18n();
		i18n.setPkmn(p);
		i18n.setName(name == null ? "_NONAME_" : name);
		i18n.setLang(lang == null ? LangUtils.LANG_FR : lang);

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
		b.append(" LEFT OUTER JOIN ").append(PkmnTable.TABLE_NAME_I18N).append(" ON ");
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

	@Override
	public String buildReqDelete(PkmnDesc p) {
		if (p.getTags().contains(PkmnTags.NOT_IN_GAME)) {
			return "";
		}
		String valTags = DatabaseUtils.toStringWithQuotes((p.getTags().isEmpty() ? "" : String.join(",", p.getTags()) + ",") + PkmnTags.NOT_IN_GAME);
		return "UPDATE " + getNomTable() + " SET " + PkmnTable.TAGS + "=" + valTags
				+ " WHERE " + getWhereClause(p);
	}
}