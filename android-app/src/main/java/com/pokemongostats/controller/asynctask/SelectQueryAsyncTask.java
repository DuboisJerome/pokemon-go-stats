package com.pokemongostats.controller.asynctask;

import java.util.List;

import android.os.AsyncTask;

public abstract class SelectQueryAsyncTask<T>
		extends
			AsyncTask<Long, Void, List<T>> {

	@Override
	public abstract void onPostExecute(List<T> list);
}
