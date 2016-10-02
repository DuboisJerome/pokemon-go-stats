package com.pokemongostats.controller.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.pokemongostats.R;
import com.pokemongostats.controller.db.gym.GymDescriptionTableDAO;
import com.pokemongostats.controller.db.gym.GymTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.model.HasID;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "";

	private static String DB_NAME = "";

	private final Context mContext;

	public DataBaseHelper(Context context) {
		super(context, context.getString(R.string.db_name), null,
				context.getResources().getInteger(R.integer.db_version));
		DB_NAME = context.getString(R.string.db_name);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		this.mContext = context;

		this.createDataBase();
	}

	/** If the database does not exist, copy it from the assets. */
	public void createDataBase() throws Error {

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
			} catch (IOException ioe) {
				throw new Error("ErrorCopyingDataBase", ioe);
			}
		}
	}

	/**
	 * Check that the database exists here:
	 * /data/data/PACKAGE/databases/DATABASENAME
	 */
	private boolean checkDataBase() {
		return new File(DB_PATH + DB_NAME).exists();
	}

	/** Copy the database from assets */
	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	/** Open the database, so we can query it */
	public SQLiteDatabase openDataBase() throws SQLException {
		return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	/**
	 * {@inheritDoc}
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

	/******** STATIC ********/

	/**
	 * Ids to string
	 * 
	 * @param separator
	 * @param params
	 * @return
	 */
	public static <T> String arrayToStringWithSeparators(final T... params) {
		if (params != null && params.length > 0) {
			final StringBuilder b = new StringBuilder("");
			for (T t : params) {
				if (t != null) {
					b.append(String.valueOf(t));
					b.append(Constants.SEPARATOR);
				}
			}
			int idxLastComa = b.lastIndexOf(Constants.SEPARATOR);
			if (idxLastComa >= 0) {
				b.replace(idxLastComa, idxLastComa + 1, "");
			}
			return b.toString();
		}

		return "";
	}

	/**
	 * 
	 * @param hasID
	 * @return return null if (hasID == null || hasID.getId() == HasID.NO_ID)
	 *         else hasID.getID()
	 */
	public static Long getIdForDB(final HasID hasID) {
		return (hasID == null || hasID.getId() == HasID.NO_ID) ? null : hasID.getId();
	}
}
