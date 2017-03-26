package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

import java.util.List;

/**
 * @param <T>
 * @author Zapagon
 */
public abstract class SelectAsyncTask<T> extends AsyncTask<String, Void, List<T>> {

    @Override
    public abstract void onPostExecute(final List<T> businessObjectsFound);

}
