package com.pokemongostats.controller.db.pokemon;

import static com.pokemongostats.model.table.AbstractTable.LANG;
import static com.pokemongostats.model.table.PokedexTable.BASE_ATTACK;
import static com.pokemongostats.model.table.PokedexTable.BASE_DEFENSE;
import static com.pokemongostats.model.table.PokedexTable.BASE_STAMINA;
import static com.pokemongostats.model.table.PokedexTable.CANDY_TO_EVOLVE;
import static com.pokemongostats.model.table.PokedexTable.DESCRIPTION;
import static com.pokemongostats.model.table.PokedexTable.FAMILY;
import static com.pokemongostats.model.table.PokedexTable.KMS_PER_CANDY;
import static com.pokemongostats.model.table.PokedexTable.KMS_PER_EGG;
import static com.pokemongostats.model.table.PokedexTable.MAX_CP;
import static com.pokemongostats.model.table.PokedexTable.NAME;
import static com.pokemongostats.model.table.PokedexTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PokedexTable.TABLE_NAME;
import static com.pokemongostats.model.table.PokedexTable.TABLE_NAME_I18N;
import static com.pokemongostats.model.table.PokedexTable.TYPE1;
import static com.pokemongostats.model.table.PokedexTable.TYPE2;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.PokemonMove;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.PkmnGoStatsApplication;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PokedexTableDAO extends TableDAO<PokemonDescription> {

	private List<Evolution> allEvolutions = null;
	private List<PokemonMove> allPkmnMoves = null;

	public PokedexTableDAO(Context pContext) {
		super(pContext);
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) pContext
				.getApplicationContext());
		this.allEvolutions = app.getAllEvolutions();
		this.allPkmnMoves = app.getAllPkmnMoves();
	}

	public PokedexTableDAO(Context pContext, List<Evolution> evolutions,
			List<PokemonMove> pkmnMoves) {
		super(pContext);
		this.allEvolutions = evolutions;
		this.allPkmnMoves = pkmnMoves;
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

		double kmsPerCandy = DBHelper.getDoubleCheckNullColumn(c,
				KMS_PER_CANDY);

		double kmsPerEgg = DBHelper.getDoubleCheckNullColumn(c, KMS_PER_EGG);

		double baseAtt = DBHelper.getDoubleCheckNullColumn(c, BASE_ATTACK);
		double baseDef = DBHelper.getDoubleCheckNullColumn(c, BASE_DEFENSE);
		double baseStamina = DBHelper.getDoubleCheckNullColumn(c, BASE_STAMINA);

		int candyToEvolve = DBHelper.getIntCheckNullColumn(c, CANDY_TO_EVOLVE);

		double maxCP = DBHelper.getDoubleCheckNullColumn(c, MAX_CP);

		// i18n
		String family = DBHelper.getStringCheckNullColumn(c, FAMILY);
		String description = DBHelper.getStringCheckNullColumn(c, DESCRIPTION);

		// retrieve evolutions
		List<Long> evolutionIds = new ArrayList<Long>();
		if (allEvolutions != null && !allEvolutions.isEmpty()) {
			for (Evolution ev : allEvolutions) {
				if (pokedexNum == ev.getPokedexNum()) {
					evolutionIds.add(ev.getEvolutionId());
				}
			}
		}

		// retrieve moves
		List<Long> moveIds = new ArrayList<Long>();
		if (allPkmnMoves != null && !allPkmnMoves.isEmpty()) {
			for (PokemonMove pm : allPkmnMoves) {
				if (pokedexNum == pm.getPokedexNum()) {
					moveIds.add(pm.getMoveId());
				}
			}
		}

		PokemonDescription p = new PokemonDescription();
		p.setPokedexNum(pokedexNum);
		p.setName(name);
		p.setType1(type1);
		p.setType2(type2);
		p.setFamily(family);
		p.setDescription(description);
		p.setEvolutionIds(evolutionIds);
		p.setMoveIds(moveIds);
		p.setKmsPerCandy(kmsPerCandy);
		p.setKmsPerEgg(kmsPerEgg);
		p.setBaseAttack(baseAtt);
		p.setBaseDefense(baseDef);
		p.setBaseStamina(baseStamina);
		p.setCandyToEvolve(candyToEvolve);
		p.setMaxCP(maxCP);

		return p;
	}

	@Override
	public Long[] insertOrReplace(final PokemonDescription... bos) {
		List<Long> result = new ArrayList<Long>();
		return result.toArray(new Long[result.size()]);
	}

	@Override
	public int removeFromObjects(PokemonDescription... objects) {
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
		b.append(" FROM ").append(TABLE_NAME);

		// join pokedex lang
		b.append(" JOIN ").append(TABLE_NAME_I18N).append(" ON ");
		b.append(" ( ");
		b.append(TABLE_NAME).append(".").append(POKEDEX_NUM);
		b.append("=");
		b.append(TABLE_NAME_I18N).append(".").append(POKEDEX_NUM);
		b.append(" AND ");
		b.append(TABLE_NAME_I18N).append(".").append(LANG);
		b.append(" LIKE ").append(DBHelper.toStringWithQuotes(Constants.FR));
		b.append(" ) ");

		if (whereClause != null && !whereClause.isEmpty()) {
			b.append(" WHERE ").append(whereClause);
		}

		Log.i("SELECT_ALL", b.toString());
		return b.toString();
	}
}