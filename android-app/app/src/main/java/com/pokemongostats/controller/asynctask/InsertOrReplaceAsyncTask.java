package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

import java.util.List;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 */
public abstract class InsertOrReplaceAsyncTask<T> extends AsyncTask<T, Void, List<T>> {

	@Override
	public abstract void onPostExecute(List<T> inserted);
}
