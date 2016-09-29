package com.pokemongostats.controller.asynctask;

import java.util.List;

import android.os.AsyncTask;

public abstract class InsertOrReplaceAsyncTask<T>
		extends
			AsyncTask<T, Void, List<Long>> {

	@Override
	public abstract void onPostExecute(List<Long> insertedIds);
}
