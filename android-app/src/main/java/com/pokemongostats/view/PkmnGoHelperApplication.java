/**
 * 
 */
package com.pokemongostats.view;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;

import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author Zapagon
 *
 */
public class PkmnGoHelperApplication extends Application {

	private List<PokemonDescription> pokedex = new ArrayList<PokemonDescription>();
	private List<Move> attacks = new ArrayList<Move>();

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

		// Initialize the singletons so their instances
		// are bound to the application process.
		initSingletons();
	}

	protected void initSingletons() {
		pokedex.clear();
		pokedex.addAll(
				new PokedexTableDAO(getApplicationContext()).selectAll());
		attacks.clear();
		attacks.addAll(new MoveTableDAO(getApplicationContext()).selectAll());
		Log.e("RIEN", "RIEN3");
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
		return attacks;
	}
}
