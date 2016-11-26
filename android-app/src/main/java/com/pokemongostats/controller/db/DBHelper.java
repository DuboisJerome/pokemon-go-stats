package com.pokemongostats.controller.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.Constants;
import com.pokemongostats.model.bean.HasID;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBHelper";

	private static String DB_PATH = "";

	private static String DB_NAME = "";

	private final Context mContext;

	public DBHelper(Context context) {
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
		SQLiteDatabase db = this.getReadableDatabase();
		if (!checkDataBase()) {
			// database not found copy the database from assests
			try {
				copyDataBase();
			} catch (IOException ioe) {
				throw new Error("ErrorCopyingDataBase", ioe);
			}
		}
		Log.d(TAG, "SqLiteDatabase create with version " + db.getVersion());
		db.close();
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
		File file = new File(DB_PATH + DB_NAME);
		return SQLiteDatabase.openOrCreateDatabase(file, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		this.createDataBase();
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
		// NOTE: This switch statement is designed to handle cascading database
		// updates, starting at the current version and falling through to all
		// future upgrade cases. Only use "break;" when you want to drop and
		// recreate the entire database.

		switch (oldVersion) {
			case 1 :
				// Version 2 update base attack/defense and max CP of pokemons
				final String fileName = "database_version_"
					+ String.valueOf(oldVersion) + "_to_"
					+ String.valueOf(newVersion);
				Log.d(TAG, "Sql upgrade file name " + fileName);
				try {
					updateFromFile(db, fileName);
				} catch (NotFoundException e) {
					Log.e(TAG, fileName + " not found", e);
				} catch (SQLException e) {
					Log.e(TAG, "Exception while upgrading database", e);
				} catch (IOException e) {
					Log.e(TAG, "Exception while upgrading database", e);
				}
		}
		db.setVersion(newVersion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Downgrade from " + oldVersion + " to " + newVersion);
		db.setVersion(newVersion);
	}

	/**
	 * Update database with sql file located by given filename
	 * 
	 * @param db
	 *            Database to upgrade
	 * @param fileName
	 *            Path of sql file
	 * @throws IOException
	 *             ioexception
	 * @throws NotFoundException
	 *             if sql file is not found
	 * @throws SQLException
	 *             sqlexception
	 */
	public void updateFromFile(SQLiteDatabase db, String fileName)
			throws IOException, NotFoundException, SQLException {
		db.beginTransaction();
		// Open the resource
		InputStream in = mContext.getResources()
				.openRawResource(mContext.getResources().getIdentifier(fileName,
						"raw", mContext.getPackageName()));
		BufferedReader inReader = new BufferedReader(new InputStreamReader(in));

		// Iterate through lines
		while (inReader.ready()) {
			String stmt = inReader.readLine();
			db.execSQL(stmt);
		}

		inReader.close();
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/******** STATIC ********/

	/**
	 * Array to delemite string
	 * 
	 * @param separator
	 * @param params
	 * @return
	 */
	public static String arrayToDelemiteString(final Object[] params,
			final boolean encapsulate) {
		if (params != null && params.length > 0) {
			final StringBuilder b = new StringBuilder("");
			for (Object t : params) {
				if (t != null) {
					final String objectToString;
					if (encapsulate) {
						objectToString = String.valueOf(t);
					} else {
						objectToString = toStringWithQuotes(t);
					}
					b.append(objectToString);
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
	 * TODO
	 * 
	 * @param o
	 * @return
	 */
	public static String toStringWithQuotes(final Object o) {
		return "'" + String.valueOf(o) + "'";
	}

	/**
	 * 
	 * @param hasID
	 * @return return null if (hasID == null || hasID.getId() == HasID.NO_ID)
	 *         else hasID.getID()
	 */
	public static Long getIdForDB(final HasID hasID) {
		return (hasID == null || hasID.getId() == HasID.NO_ID)
				? null
				: hasID.getId();
	}

	public static String getStringCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return null; }
		return c.getString(columnIndex);
	}

	public static int getIntCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return 0; }
		return c.getInt(columnIndex);
	}

	public static long getLongCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return 0; }
		return c.getLong(columnIndex);
	}

	public static double getDoubleCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return 0; }
		return c.getDouble(columnIndex);
	}

	public static float getFloatCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return 0; }
		return c.getFloat(columnIndex);
	}

	public static float getShortCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return 0; }
		return c.getShort(columnIndex);
	}

	public static byte[] getBlobCheckNullColumn(final Cursor c,
			final String columnName) {
		int columnIndex = c.getColumnIndex(columnName);
		if (columnIndex == -1 || c.isNull(columnIndex)) { return null; }
		return c.getBlob(columnIndex);
	}

}
