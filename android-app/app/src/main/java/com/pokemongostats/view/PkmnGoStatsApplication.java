/**
 *
 */
package com.pokemongostats.view;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.pokemon.PokemonMoveTableDAO;
import com.pokemongostats.controller.utils.ExceptionHandler;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.PokemonMove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zapagon
 */
public class PkmnGoStatsApplication extends Application {

    private List<Evolution> allEvolutions = new ArrayList<>();
    private List<PokemonMove> allPkmnMoves = new ArrayList<>();
    private Map<Long, PokemonDescription> pokedexMap = new HashMap<>();
    private Map<Long, Move> movesMap = new HashMap<>();

    private FragmentActivity mCurrentActivity = null;
    private boolean mCurrentActivityIsVisible = false;

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

    /**
     * @return the mCurrentActivityIsVisible
     */
    public boolean isCurrentActivityIsVisible() {
        return mCurrentActivityIsVisible;
    }

    /**
     * @param mCurrentActivityIsVisible the mCurrentActivityIsVisible to set
     */
    public void setCurrentActivityIsVisible(boolean mCurrentActivityIsVisible) {
        this.mCurrentActivityIsVisible = mCurrentActivityIsVisible;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

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
        return getPokedex(false);
    }

    public List<PokemonDescription> getPokedex(boolean isOnlyLastEvolutions) {
        if (isOnlyLastEvolutions) {
            Map<Long, PokemonDescription> map = new HashMap<Long, PokemonDescription>(pokedexMap);
            for (Evolution ev : allEvolutions) {
                map.remove(ev.getPokedexNum());
            }
            return new ArrayList<>(map.values());
        } else {
            return new ArrayList<>(pokedexMap.values());
        }
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
        return new ArrayList<>(movesMap.values());
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

    /**
     * Get both base and next evolutions ids
     *
     * @param pokedexNum
     * @return
     */
    public List<Long> getFamillePokemon(final long pokedexNum) {
        List<Long> evolutionIds = new ArrayList<>();
        if (allEvolutions != null && !allEvolutions.isEmpty()) {
            findBasesPokemons(pokedexNum, evolutionIds);
            evolutionIds.add(pokedexNum);
            findEvolutionsPokemons(pokedexNum, evolutionIds);
        }
        return evolutionIds;
    }

    /**
     * For each evolution, if given pokedex num == evolution id return base
     * pokedex num
     *
     * @param pokedexNum
     * @return base evolution or NO_ID (only one base evolution)
     */
    public void findBasesPokemons(final long pokedexNum, final List<Long> evolutionsIds) {
        if (allEvolutions != null) {
            for (Evolution ev : allEvolutions) {
                if (pokedexNum == ev.getEvolutionId()) {
                    evolutionsIds.add(0, ev.getPokedexNum());
                    findBasesPokemons(ev.getPokedexNum(), evolutionsIds);
                    break;
                }
            }
        }
    }

    /**
     * @param pokedexNum
     * @return list of pokemon id. Some pkmns may have multiple evolutions (ex :
     * Eevee)
     */
    public void findEvolutionsPokemons(final long pokedexNum, final List<Long> evolutionsIds) {
        if (allEvolutions != null) {
            for (Evolution ev : allEvolutions) {
                if (pokedexNum == ev.getPokedexNum()) {
                    evolutionsIds.add(ev.getEvolutionId());
                    findEvolutionsPokemons(ev.getEvolutionId(), evolutionsIds);
                }
            }

        }
    }
}
