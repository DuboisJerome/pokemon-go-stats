package com.pokemongostats.controller.asynctask;

import java.util.List;

import android.os.AsyncTask;

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
