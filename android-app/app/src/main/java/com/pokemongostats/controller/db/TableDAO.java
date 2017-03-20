/**
 * 
 */
package com.pokemongostats.controller.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * 
 * @author Zapagon
 *
 */
public abstract class TableDAO<BusinessObject> {

	private static final String ROWID = "ROWID";

	private DBHelper handler;

	private Context context;

	public TableDAO(Context context) {
		setContext(context);
		handler = new DBHelper(context);
	}

	/**
	 * @return the handler
	 */
	public DBHelper getHandler() {
		return handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(DBHelper handler) {
		this.handler = handler;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Open database
	 * 
	 * @return
	 */
	public SQLiteDatabase open() throws SQLiteException {
		return handler.openDataBase();
	}

	/**
	 * Close database
	 */
	public void close() {
		handler.close();
	}

	/**
	 * @return the table name
	 */
	public abstract String getTableName();

	/**
	 * Add business objects to database
	 * 
	 * @param bos
	 *            business objects
	 * 
	 * @return list of rowid (inserted or replaced)
	 */
	public Long[] insertOrReplace(final BusinessObject... bos) {
		List<Long> result = new ArrayList<Long>();

		if (bos != null && bos.length > 0) {
			SQLiteDatabase db = this.open();
			try {
				for (BusinessObject bo : bos) {
					if (bo != null) {
						long id = db.replaceOrThrow(getTableName(), null,
								getContentValues(bo));
						if (id == -1) { throw new SQLiteException(
								"Error inserting following object : " + bo); }
						result.add(id);
					}
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			} finally {
				db.close();
			}
		}

		return result.toArray(new Long[result.size()]);
	}

	/**
	 * getContentValues from given bo. Use by default to insert or replace
	 * values
	 * 
	 * @param bo
	 * @return
	 */
	protected ContentValues getContentValues(final BusinessObject bo) {
		return new ContentValues();
	}

	/**
	 * InsertOrReplace + SelectAll
	 * 
	 * @param bos
	 *            business objects
	 * @return not null list
	 */
	public List<BusinessObject> insertOrReplaceThenSelectAll(
			BusinessObject... bos) {
		return selectAllFromRowIDs(insertOrReplace(bos));
	}

	/**
	 * Remove database entry with given id
	 * 
	 * @param t
	 * 
	 * @return nb rows deleted
	 */
	public int remove(final String whereClause) {
		int nbRowDeleted = 0;
		if (whereClause != null && !whereClause.isEmpty()) {
			nbRowDeleted = open().delete(getTableName(), whereClause, null);
			close();
		}
		return nbRowDeleted;
	}

	/**
	 * Remove all data base entries. Should never be used
	 * 
	 * @return nb rows deleted
	 */
	public final int removeAll() {
		int nbRowDeleted = open().delete(getTableName(), null, null);
		close();
		return nbRowDeleted;
	}

	/**
	 * Remove database entry with given objects
	 * 
	 * @param t
	 * @return nb objects deleted
	 */
	public int removeFromObjects(final BusinessObject... objects) {
		return 0;
	}

	/**
	 * Select one bo following the given rowid
	 * 
	 * @param rowid
	 * @return
	 */
	public final BusinessObject selectFromRowID(final long rowid) {
		List<BusinessObject> all = this.selectAllFromRowIDs(rowid);
		return (all == null || all.isEmpty()) ? null : all.get(0);
	}

	/**
	 * Get all entry in current table matching given where clause
	 * 
	 * @param whereClause
	 * @return not null list of bos
	 */
	public final List<BusinessObject> selectAllFromRowIDs(
			final Long... rowIDs) {
		return selectAllIn(ROWID, false, rowIDs);
	}

	/**
	 * Get all entry in current table matching given where clause
	 * 
	 * @param whereClause
	 * @return
	 */
	public final List<BusinessObject> selectAllIn(final String columnName,
			final boolean isColumnTypeText, final Object[] objects) {
		if (objects == null
			|| objects.length <= 0) { return new ArrayList<BusinessObject>(); }
		String whereClause = columnName + " in ("
			+ DBHelper.arrayToDelemiteString(objects, isColumnTypeText) + ")";
		return selectAll(getSelectAllQuery(whereClause));
	}

	/**
	 * Get all entry in current table
	 * 
	 * @return
	 */
	public final List<BusinessObject> selectAll() {
		return selectAll(getSelectAllQuery(null));
	}

	/**
	 * Get All entry in current table
	 * 
	 * @param query
	 * @return
	 */
	public List<BusinessObject> selectAll(final String query) {
		// call database
		Cursor c = open().rawQuery(query, null);

		List<BusinessObject> results = new ArrayList<BusinessObject>();

		// foreach entries
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			BusinessObject bo = convert(c);
			if (bo != null) {
				results.add(bo);
			}
		}

		c.close();
		close();

		return results;
	}

	/**
	 * @param whereClause
	 * @return select query
	 */
	protected String getSelectAllQuery(final String whereClause) {
		final String formattedQuery;
		if (whereClause != null && !whereClause.isEmpty()) {
			formattedQuery = String.format("SELECT * FROM %s WHERE %s",
					getTableName(), whereClause);
		} else {
			formattedQuery = String.format("SELECT * FROM %s", getTableName());
		}

		Log.i("SELECT_ALL", formattedQuery);
		return formattedQuery;
	}

	/**
	 * Convert an entry at position of the given cursor to object
	 * 
	 * @param c
	 * @return model object convert from entry at cursor position
	 */
	protected abstract BusinessObject convert(Cursor c);

}