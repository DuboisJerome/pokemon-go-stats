package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

public abstract class DeleteAsyncTask<T> extends AsyncTask<T, Void, Integer> {

	@Override
	public abstract void onPostExecute(Integer nbRowsDeleted);
}
