/**
 * 
 */
package com.pokemongostats.controller.db;

import java.util.List;

import com.pokemongostats.model.HasID;

import android.os.AsyncTask;

/**
 * @author Zapagon
 *
 */
public abstract class DatabaseAsyncDAO<T extends HasID> {

	private TableDAO<T> tableDAO;

	public DatabaseAsyncDAO(TableDAO<T> tableDAO) {
		this.tableDAO = tableDAO;
	}

	/**
	 * @return the tableDAO
	 */
	protected TableDAO<T> getTableDAO() {
		return tableDAO;
	}

	/**
	 * @param tableDAO
	 *            the tableDAO to set
	 */
	protected void setTableDAO(TableDAO<T> tableDAO) {
		this.tableDAO = tableDAO;
	}
	
	/**
	 * TODO
	 * 
	 * @author Zapagon
	 *
	 */
	public abstract class GetAllAsyncTask<Progress>
			extends
				AsyncTask<Long, Progress, List<T>> {
		@Override
		protected List<T> doInBackground(Long... ids) {
			return DatabaseAsyncDAO.this.tableDAO.selectAll(ids);
		}

		@Override
		protected void onProgressUpdate(Progress... progress) {
		}

	}

	/**
	 * TODO
	 * 
	 * @author Zapagon
	 *
	 */
	public class SaveAsyncTask<Progress> extends AsyncTask<T, Progress, Void> {
		@Override
		protected Void doInBackground(T... params) {
			DatabaseAsyncDAO.this.tableDAO.insertOrReplace(params);
			return null;
		}

		@Override
		protected void onProgressUpdate(Progress... progress) {
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	/**
	 * TODO
	 * 
	 * @author Zapagon
	 *
	 */
	public class DeleteAsyncTask<Progress>
			extends
				AsyncTask<T, Progress, Void> {
		@Override
		protected Void doInBackground(T... objects) {

			for (T t : objects) {
				if (t != null && HasID.NO_ID != t.getId()) { 
					DatabaseAsyncDAO.this.tableDAO.remove(t.getId());}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Progress... progress) {
		}

		@Override
		protected void onPostExecute(Void result) {
		}

	}
}
