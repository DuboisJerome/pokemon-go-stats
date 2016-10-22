/**
 * 
 */
package com.pokemongostats.view;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonMoveTableDAO;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.PokemonMove;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * @author Zapagon
 *
 */
public class PkmnGoStatsApplication extends Application {

	private List<Evolution> allEvolutions = new ArrayList<Evolution>();
	private List<PokemonMove> allPkmnMoves = new ArrayList<PokemonMove>();
	private TreeMap<Long, PokemonDescription> pokedexMap = new TreeMap<Long, PokemonDescription>();
	private TreeMap<Long, Move> movesMap = new TreeMap<Long, Move>();

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
		List<PokemonDescription> pokedex = new PokedexTableDAO(c).selectAll();
		for (PokemonDescription p : pokedex) {
			pokedexMap.put(p.getPokedexNum(), p);
		}
		List<Move> moves = new MoveTableDAO(c).selectAll();
		for (Move m : moves) {
			movesMap.put(m.getId(), m);
		}
	}

	/**
	 * @return the pokedex
	 */
	public List<PokemonDescription> getPokedex() {
		return new ArrayList<PokemonDescription>(pokedexMap.values());
	}

	/**
	 * @return the pokedex
	 */
	public PokemonDescription getPokemonWithId(final long id) {
		return pokedexMap.get(id);
	}

	/**
	 * @return the list of moves
	 */
	public List<Move> getMoves() {
		return new ArrayList<Move>(movesMap.values());
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

}
