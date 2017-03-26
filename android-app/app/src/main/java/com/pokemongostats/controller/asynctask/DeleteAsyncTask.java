package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

/**
 * @param <T>
 * @author Zapagon
 */
public abstract class DeleteAsyncTask<T> extends AsyncTask<T, Void, Integer> {

    @Override
    public abstract void onPostExecute(Integer nbRowsDeleted);
}
