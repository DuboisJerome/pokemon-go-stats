package com.pokemongostats.controller.asynctask;

import com.pokemongostats.view.ViewBuilder;

import android.os.AsyncTask;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 */
public class BuildViewAsyncTask extends AsyncTask<ViewBuilder, Void, Void> {

	@Override
	protected Void doInBackground(ViewBuilder... params) {
		for (ViewBuilder vb : params) {
			vb.buildView();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
