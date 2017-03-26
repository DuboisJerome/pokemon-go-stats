package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

import java.util.List;

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
