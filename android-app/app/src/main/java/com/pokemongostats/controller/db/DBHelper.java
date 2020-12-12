package com.pokemongostats.controller.db;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.ConstantsUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.HasID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";

    private static String DB_NAME = "";

    private final Context mContext;

    public DBHelper(Context context) {
        super(context, context.getString(R.string.db_name), null,
                context.getResources().getInteger(R.integer.db_version));
        DB_NAME = context.getString(R.string.db_name);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
        //String s = null; s.toString();
        this.createDB();
    }

    /******** STATIC ********/

    public static String arrayToDelemiteString(final Object[] params,
                                               final boolean encapsulate) {
        if (params != null && params.length > 0) {
            final StringBuilder b = new StringBuilder();
            for (Object t : params) {
                if (t != null) {
                    final String objectToString;
                    if (encapsulate) {
                        objectToString = String.valueOf(t);
                    } else {
                        objectToString = toStringWithQuotes(t);
                    }
                    b.append(objectToString);
                    b.append(ConstantsUtils.COMMA);
                }
            }
            int idxLastComa = b.lastIndexOf(ConstantsUtils.COMMA);
            if (idxLastComa >= 0) {
                b.replace(idxLastComa, idxLastComa + 1, "");
            }
            return b.toString();
        }

        return "";
    }

    /**
     * Add quotes to string for sql request
     *
     * @param o
     * @return
     */
    public static String toStringWithQuotes(final Object o) {
        return "'" + o + "'";
    }

    /**
     * @param hasID
     * @return return null if (hasID == null || hasID.getId() == HasID.NO_ID)
     * else hasID.getID()
     */
    public static Long getIdForDB(final HasID hasID) {
        return (hasID == null || hasID.getId() == HasID.NO_ID)
                ? null
                : hasID.getId();
    }

    public static String getStringCheckNullColumn(final Cursor c,
                                                  final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return null;
        }
        return c.getString(columnIndex);
    }

    public static int getIntCheckNullColumn(final Cursor c,
                                            final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return -1;
        }
        return c.getInt(columnIndex);
    }

    public static long getLongCheckNullColumn(final Cursor c,
                                              final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return -1L;
        }
        return c.getLong(columnIndex);
    }

    public static double getDoubleCheckNullColumn(final Cursor c,
                                                  final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return -1D;
        }
        return c.getDouble(columnIndex);
    }

    public static float getFloatCheckNullColumn(final Cursor c,
                                                final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return -1F;
        }
        return c.getFloat(columnIndex);
    }

    public static float getShortCheckNullColumn(final Cursor c,
                                                final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return -1;
        }
        return c.getShort(columnIndex);
    }

    public static byte[] getBlobCheckNullColumn(final Cursor c,
                                                final String columnName) {
        int columnIndex = c.getColumnIndex(columnName);
        if (columnIndex == -1 || c.isNull(columnIndex)) {
            return null;
        }
        return c.getBlob(columnIndex);
    }

    public static String getLang(Context c) {
        String[] supportLang = c.getResources().getStringArray(R.array.support_lang);
        List<String> listSupportLang = Arrays.asList(supportLang);

        String locale = Locale.getDefault().toString();
        // find if current locale is supported
        boolean isLocalSupported = listSupportLang.contains(locale);

        String result = null;
        if (!isLocalSupported) {
            // if not supported find code for others country
            // e.g : en_GB not found => display en_US if exist
            for (String l : listSupportLang) {
                if (l.startsWith(Locale.getDefault().getLanguage())) {
                    result = l;
                    break;
                }
            }
        } else {
            result = locale;
        }

        if (result == null || result.isEmpty()) {
            result = c.getString(R.string.default_lang);
        }

        return result;
    }

    /**
     * If the database does not exist, copy it from the assets.
     */
    public void createDB() throws Error {
        if (!isDataBaseExist()) {
            SQLiteDatabase db = this.getReadableDatabase();
            Log.d(TagUtils.DB, "SqLiteDatabase create with version " + db.getVersion());
            this.close();
            // database not found copy the database from assests
            try {
                createDBFromAssets();
            } catch (IOException ioe) {
                throw new Error("ErrorCopyingDataBase", ioe);
            }
        }
    }

    /**
     * Check that the database exists here:
     * /data/data/PACKAGE/databases/DATABASENAME
     */
    private boolean isDataBaseExist() {
        return new File(DB_PATH + DB_NAME).exists();
    }

    /**
     * Copy the database from assets
     */
    private void createDBFromAssets() throws IOException {
        // create input stream
        InputStream mInput = mContext.getAssets().open(DB_NAME);

        // open output stream
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        Log.d(TagUtils.DB, "SqLiteDatabase copy database to " + outFileName);

        // copy db from assets to real location
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }

        // close streams
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /**
     * Open the database, so we can query it
     */
    public SQLiteDatabase openDB() throws SQLException {
        File file = new File(DB_PATH + DB_NAME);
        return SQLiteDatabase.openOrCreateDatabase(file, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TagUtils.DB, "onCreate");
        db.beginTransaction();
        this.createDB();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TagUtils.DB, "onUpgrade() from " + oldVersion + " to " + newVersion);
        // NOTE: This switch statement is designed to handle cascading database
        // updates, starting at the current version and falling through to all
        // future upgrade cases. Only use "break;" when you want to drop and
        // recreate the entire database.

        for (int ov = oldVersion; ov < newVersion; ov++) {
            final String fileName = getUpdateDBFileName(ov);
            Log.d(TagUtils.DB, "Sql upgrade file name " + fileName);
            try {
                updateFromFile(db, fileName);
            } catch (NotFoundException e) {
                Log.e(TagUtils.DB, fileName + " not found", e);
            } catch (Exception e) {
                Log.e(TagUtils.DB, "Exception while upgrading database", e);
            }
        }
        db.setVersion(newVersion);
    }

    private String getUpdateDBFileName(final int oldVersion) {
        return "database_version_"
                + oldVersion + "_to_"
                + (oldVersion + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TagUtils.DB, "Downgrade from " + oldVersion + " to " + newVersion);
        db.setVersion(newVersion);
    }

    /**
     * Update database with sql file located by given filename
     *
     * @param db       Database to upgrade
     * @param fileName Path of sql file
     * @throws IOException       ioexception
     * @throws NotFoundException if sql file is not found
     * @throws SQLException      sqlexception
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

    public static String buildWhereIn(final String col, List<String> values, boolean isStringType) {
        return " " + col + " IN (" + arrayToDelemiteString(values.toArray(), isStringType) + ") ";
    }

    public static String buildWhere(final String col, final String value) {
        return " " + col + "=" + value + " ";
    }

    public static String buildWhereLike(final String col, final String value) {
        return " " + col + " LIKE %" + value + "% ";
    }


}
