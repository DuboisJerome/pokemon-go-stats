package com.pokemongostats.controller.asynctask;

import java.util.List;

import android.os.AsyncTask;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 */
public abstract class SelectAsyncTask<T> extends AsyncTask<String, Void, List<T>> {

	@Override
	public abstract void onPostExecute(final List<T> businessObjectsFound);

}
