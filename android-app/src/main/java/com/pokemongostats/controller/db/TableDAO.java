/**
 * 
 */
package com.pokemongostats.controller.db;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.utils.DBHelper;
import com.pokemongostats.model.HasID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Zapagon
 *
 */
public abstract class TableDAO<T extends HasID> {

	public static final long NOT_INSERTED = -1L;

	public static final String ID = "id";

	private DBHandler handler;

	private Context context;

	public TableDAO(Context context) {
		this.setContext(context);
		this.handler = new DBHandler(context);
	}

	/**
	 * @return the handler
	 */
	public DBHandler getHandler() {
		return handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(DBHandler handler) {
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
	public SQLiteDatabase open() {
		return handler.getWritableDatabase();
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
	 * @return id column name, default is 'id'
	 */
	public String getIdColumnName() {
		return ID;
	}

	/**
	 * Add business object to database
	 * 
	 * @param t
	 */
	public abstract List<Long> insertOrReplace(T... t);

	/**
	 * Remove database entry with given id
	 * 
	 * @param t
	 * 
	 * @return nb row delete
	 */
	public int remove(Long... ids) {
		int nbRowDelete = 0;
		String idsStr = DBHelper.arrayToStringWithSeparators(ids);
		if (idsStr != null && !idsStr.isEmpty()) {
			nbRowDelete = this.open().delete(this.getTableName(),
					this.getIdColumnName() + " IN (" + idsStr + ")", null);
			this.close();
		}
		return nbRowDelete;
	}

	/**
	 * Remove database entry with given object
	 * 
	 * @param t
	 */
	public int remove(T... objects) {
		int size = objects.length;
		Long[] ids = new Long[size];
		for (int i = 0; i < size; i++) {
			T object = objects[i];
			if (object != null) {
				ids[i] = objects[i].getId();
			}
		}

		return remove(ids);
	}

	/**
	 * TODO
	 * 
	 * @param id
	 * @return
	 */
	public T select(Long id) {
		List<T> all = this.selectAll(id);
		return (all == null || all.isEmpty()) ? null : all.get(0);
	}

	/**
	 * Get business objects with ids, if ids is empty return all entries from
	 * current table
	 * 
	 * @param ids
	 * @return list of business objects found or empty list if none found
	 */
	public List<T> selectAll(Long... ids) {
		// get all pokemons with id in ids' array
		String query = this.getSelectAllQuery(ids);
		// call database
		Cursor c = this.open().rawQuery(query, null);

		List<T> results = new ArrayList<T>();

		// foreach entries
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			results.add(this.convert(c));
		}

		c.close();
		this.close();

		return results;
	}

	/**
	 * TODO
	 * 
	 * @param query
	 * @return
	 */
	public List<T> select(final String query) {
		// call database
		Cursor c = this.open().rawQuery(query, null);

		List<T> results = new ArrayList<T>();

		// foreach entries
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			results.add(this.convert(c));
		}

		c.close();
		this.close();

		return results;
	}

	/**
	 * TODO
	 * 
	 * @param ids
	 * @return
	 */
	protected String getSelectAllQuery(Long... ids) {
		final String query = "SELECT * FROM %s t";
		final String formattedQuery;
		String idsStr = DBHelper.arrayToStringWithSeparators(ids);
		if (idsStr != null && !idsStr.isEmpty()) {
			formattedQuery = String.format(query + " WHERE " + ID + " IN (%s)",
					this.getTableName(), idsStr);
		} else {
			formattedQuery = String.format(query, this.getTableName());
		}

		return formattedQuery;
	}

	/**
	 * Convert an entry at position of the given cursor to object
	 * 
	 * @param c
	 * @return model object convert from entry at cursor position
	 */
	protected abstract T convert(Cursor c);

}
