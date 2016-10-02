/**
 * 
 */
package com.pokemongostats.controller.db;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.model.HasID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * 
 * @author Zapagon
 *
 */
public abstract class TableDAO<BusinessObject extends HasID> {

	public static final long NOT_INSERTED = -1L;

	public static final String ID = "_id";

	private DataBaseHelper handler;

	private Context context;

	public TableDAO(Context context) {
		this.setContext(context);
		this.handler = new DataBaseHelper(context);
	}

	/**
	 * @return the handler
	 */
	public DataBaseHelper getHandler() {
		return handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(DataBaseHelper handler) {
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
	 * @return id column name, default is 'id'
	 */
	public String getIdColumnName() {
		return ID;
	}

	/**
	 * Add business objects to database
	 * 
	 * @param bos
	 *            business objects
	 */
	public abstract List<Long> insertOrReplace(BusinessObject... bos);

	/**
	 * TODO
	 * 
	 * @param bos
	 *            business objects
	 * @return
	 */
	public List<BusinessObject> insertOrReplaceThenSelectAll(BusinessObject... bos) {
		List<Long> insertedIds = this.insertOrReplace(bos);
		List<BusinessObject> newBos = new ArrayList<BusinessObject>();
		if (insertedIds != null) {
			newBos.addAll(this.selectAll(insertedIds.toArray(new Long[insertedIds.size()])));
		}
		return newBos;
	}

	/**
	 * Remove database entry with given id
	 * 
	 * @param t
	 * 
	 * @return nb row delete
	 */
	public int remove(Long... ids) {
		int nbRowDelete = 0;
		String idsStr = DataBaseHelper.arrayToStringWithSeparators(ids);
		if (idsStr != null && !idsStr.isEmpty()) {
			nbRowDelete = this.open().delete(this.getTableName(), this.getIdColumnName() + " IN (" + idsStr + ")",
					null);
			this.close();
		}
		return nbRowDelete;
	}

	/**
	 * Remove database entry with given object
	 * 
	 * @param t
	 */
	public int remove(BusinessObject... objects) {
		int size = objects.length;
		Long[] ids = new Long[size];
		for (int i = 0; i < size; i++) {
			BusinessObject object = objects[i];
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
	public final BusinessObject select(Long id) {
		List<BusinessObject> all = this.selectAll(id);
		return (all == null || all.isEmpty()) ? null : all.get(0);
	}

	/**
	 * Get business objects with ids, if ids is empty return all entries from
	 * current table
	 * 
	 * @param ids
	 * @return list of business objects found or empty list if none found
	 */
	public final List<BusinessObject> selectAll(Long... ids) {
		return select(this.getSelectAllQuery(ids));
	}

	/**
	 * TODO
	 * 
	 * @param query
	 * @return
	 */
	public final List<BusinessObject> select(final String query) {
		// call database
		Cursor c = this.open().rawQuery(query, null);

		List<BusinessObject> results = new ArrayList<BusinessObject>();

		// foreach entries
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			BusinessObject bo = this.convert(c);
			if (bo != null) {
				results.add(bo);
			}
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
		String idsStr = DataBaseHelper.arrayToStringWithSeparators(ids);
		if (idsStr != null && !idsStr.isEmpty()) {
			formattedQuery = String.format(query + " WHERE " + ID + " IN (%s)", this.getTableName(), idsStr);
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
	protected abstract BusinessObject convert(Cursor c);

}
