/**
 * 
 */
package com.pokemongostats.view;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.BuildConfig;
import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonMoveTableDAO;
import com.pokemongostats.controller.services.DownloadUpdateService;
import com.pokemongostats.controller.utils.AppUpdate;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.PokemonMove;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;

/**
 * @author Zapagon
 *
 */
public class PkmnGoHelperApplication extends Application {

	private List<Evolution> allEvolutions = new ArrayList<Evolution>();
	private List<PokemonMove> allPkmnMoves = new ArrayList<PokemonMove>();
	private List<PokemonDescription> pokedex = new ArrayList<PokemonDescription>();
	private List<Move> moves = new ArrayList<Move>();

	private FragmentActivity mCurrentActivity = null;

	/**
	 * @return mCurrentActivity
	 */
	public FragmentActivity getCurrentActivity() {
		return mCurrentActivity;
	}

	/**
	 * @param mCurrentActivity
	 */
	public void setCurrentActivity(FragmentActivity mCurrentActivity) {
		this.mCurrentActivity = mCurrentActivity;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		final Context c = getApplicationContext();
		allPkmnMoves.addAll(new PokemonMoveTableDAO(c).selectAll());
		allEvolutions.addAll(new EvolutionTableDAO(c).selectAll());
		pokedex.addAll(new PokedexTableDAO(c).selectAll());
		moves.addAll(new MoveTableDAO(c).selectAll());
	}

	/**
	 * @return the pokedex
	 */
	public List<PokemonDescription> getPokedex() {
		return pokedex;
	}

	/**
	 * @return the list of moves
	 */
	public List<Move> getMoves() {
		return moves;
	}

	/**
	 * @return the allEvolutions
	 */
	public List<Evolution> getAllEvolutions() {
		return allEvolutions;
	}

	/**
	 * @return the allPkmnMoves
	 */
	public List<PokemonMove> getAllPkmnMoves() {
		return allPkmnMoves;
	}

	/** Remote update */

	public static boolean isApplicationBeingUpdated(Context context) {

		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Query q = new DownloadManager.Query();
		q.setFilterByStatus(DownloadManager.STATUS_RUNNING);
		Cursor c = downloadManager.query(q);
		if (c.moveToFirst()) {
			String fileName = c
					.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
			if (fileName.equals(
					DownloadUpdateService.DOWNLOAD_UPDATE_TITLE)) { return true; }

		}
		return false;
	}

	private final BroadcastReceiver showUpdateDialog = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			assert BuildConfig.isInternetAvailable;
			AppUpdate update = intent.getParcelableExtra("update");
			if (update.getStatus() == AppUpdate.UPDATE_AVAILABLE
				&& !isApplicationBeingUpdated(context)) {
				AlertDialog updateDialog = AppUpdateUtil
						.getAppUpdateDialog(MainActivity.this, update);
				updateDialog.show();
			}
		}
	};

}
