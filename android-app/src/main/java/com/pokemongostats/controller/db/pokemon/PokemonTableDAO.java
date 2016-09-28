package com.pokemongostats.controller.db.pokemon;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.controller.utils.DBHelper;
import com.pokemongostats.model.Pokemon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PokemonTableDAO extends TableDAO<Pokemon> {

	// table name
	public static final String TABLE_NAME = "pokemon";
	// columns names
	public static final String ID = "id";
	public static final String POKEDEX_NUM = "pokedex_num";
	public static final String CP = "cp";
	public static final String HP = "hp";
	public static final String DEFENSE_IV = "defense_iv";
	public static final String ATTACK_IV = "attack_iv";
	public static final String STAMINA_IV = "stamina_iv";
	public static final String LEVEL = "level";
	public static final String OWNER_ID = "owner_id";
	public static final String NICKNAME = "nickname";

	// create query
	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
		+ TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ POKEDEX_NUM + " INTEGER NOT NULL, " + CP + " INTEGER, " + HP
		+ " INTEGER, " + DEFENSE_IV + " INTEGER, " + ATTACK_IV + " INTEGER, "
		+ STAMINA_IV + " INTEGER, " + LEVEL + " REAL, " + OWNER_ID
		+ " INTEGER, " + NICKNAME + " TEXT, " + " FOREIGN KEY (" + POKEDEX_NUM
		+ ") REFERENCES " + PokedexTableDAO.TABLE_NAME + "("
		+ PokedexTableDAO.POKEDEX_NUM + "), " + " FOREIGN KEY (" + OWNER_ID
		+ ") REFERENCES " + TrainerTableDAO.TABLE_NAME + "(" + TrainerTableDAO.ID
		+ "));";

	public PokemonTableDAO(Context pContext) {
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
	public List<Long> insertOrReplace(Pokemon... pokemons) {
		if (pokemons == null) { return new ArrayList<Long>(); }
		List<Long> returnIds = new ArrayList<Long>(pokemons.length);

		SQLiteDatabase db = this.open();
		db.beginTransaction();
		for (Pokemon p : pokemons) {
			Long pokedexNum = p.getPokedexNum();
			Integer cp = p.getCP();
			Integer hp = p.getHP();
			Integer defenseIV = p.getDefenseIV();
			Integer attackIV = p.getAttackIV();
			Integer staminaIV = p.getStaminaIV();
			Float level = p.getLevel();
			Long ownerID = DBHelper.getIdForDB(p.getOwner());
			String nickname = p.getNickname();

			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, DBHelper.getIdForDB(p));
			initialValues.put(POKEDEX_NUM, pokedexNum);
			initialValues.put(LEVEL, level);
			initialValues.put(CP, cp);
			initialValues.put(HP, hp);
			initialValues.put(DEFENSE_IV, defenseIV);
			initialValues.put(ATTACK_IV, attackIV);
			initialValues.put(STAMINA_IV, staminaIV);
			initialValues.put(LEVEL, level);
			initialValues.put(OWNER_ID, ownerID);
			initialValues.put(NICKNAME, nickname);
			long id = db.replace(TABLE_NAME, null, initialValues);
			returnIds.add(id);
		}
		db.setTransactionSuccessful();
		db.endTransaction();

		db.close();
		return returnIds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Pokemon convert(Cursor c) {

		// pokemon id
		int pokemonId = c.getInt(c.getColumnIndex(POKEDEX_NUM));

		Pokemon p = new Pokemon(pokemonId);
		// name
		p.setNickname(c.getString(c.getColumnIndex(NICKNAME)));
		// cp
		p.setCP(c.getInt(c.getColumnIndex(CP)));
		// hp
		p.setHP(c.getInt(c.getColumnIndex(HP)));
		// defenseIV
		p.setDefenseIV(c.getInt(c.getColumnIndex(DEFENSE_IV)));
		// attackIV
		p.setAttackIV(c.getInt(c.getColumnIndex(ATTACK_IV)));
		// staminaIV
		p.setStaminaIV(c.getInt(c.getColumnIndex(STAMINA_IV)));
		// level
		p.setLevel(c.getInt(c.getColumnIndex(LEVEL)));

		// owner id
		long ownerId = c.getInt(c.getColumnIndex(OWNER_ID));
		p.setOwner(new TrainerTableDAO(getContext()).select(ownerId));

		return p;
	}

}