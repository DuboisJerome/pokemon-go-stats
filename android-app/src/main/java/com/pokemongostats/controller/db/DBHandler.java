package com.pokemongostats.controller.db;

import com.pokemongostats.R;
import com.pokemongostats.controller.db.gym.GymDescriptionTableDAO;
import com.pokemongostats.controller.db.gym.GymTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler(Context context) {
		super(context, context.getString(R.string.db_name), null,
				context.getResources().getInteger(R.integer.db_version));
	}

	/**
	 * exect sql query return by getCreateTableQuery()
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();

		db.execSQL(GymDescriptionTableDAO.TABLE_CREATE);
		db.execSQL(GymTableDAO.TABLE_CREATE);
		db.execSQL(TrainerTableDAO.TABLE_CREATE);
		db.execSQL(PokedexTableDAO.TABLE_CREATE);
		db.execSQL(PokemonTableDAO.TABLE_CREATE);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		final String dropBaseQuery = "DROP TABLE IF EXISTS ";

		// drop pokemon table
		db.execSQL(dropBaseQuery + PokemonTableDAO.TABLE_NAME + ";");
		// drop pokedex table
		db.execSQL(dropBaseQuery + PokedexTableDAO.TABLE_NAME + ";");
		// drop trainer table
		db.execSQL(dropBaseQuery + TrainerTableDAO.TABLE_NAME + ";");
		// drop gym_desc table
		db.execSQL(dropBaseQuery + GymDescriptionTableDAO.TABLE_NAME + ";");
		// drop gym table
		db.execSQL(dropBaseQuery + GymTableDAO.TABLE_NAME + ";");

		db.setTransactionSuccessful();
		db.endTransaction();

		onCreate(db);
	}
}
