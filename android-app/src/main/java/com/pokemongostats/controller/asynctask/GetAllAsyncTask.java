package com.pokemongostats.controller.asynctask;

import java.util.List;

import android.os.AsyncTask;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 */
public abstract class GetAllAsyncTask<T> extends AsyncTask<Long, Void, List<T>> {

	@Override
	public abstract void onPostExecute(List<T> list);
}
