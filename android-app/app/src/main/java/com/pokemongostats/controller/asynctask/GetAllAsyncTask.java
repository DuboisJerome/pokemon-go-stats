package com.pokemongostats.controller.asynctask;

import android.os.AsyncTask;

import java.util.List;

/**
 * @param <T>
 * @author Zapagon
 */
public abstract class GetAllAsyncTask<T> extends AsyncTask<Long, Void, List<T>> {

    @Override
    public abstract void onPostExecute(List<T> list);
}
