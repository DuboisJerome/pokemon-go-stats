package com.pokemongostats.controller.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.controller.utils.MapUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zapagon on 05/04/2017.
 * Pokedex dao
 */
public class PokedexDAO {

    private static List<Evolution> listEvolution;
    private static List<PkmnMove> listPkmnMove;
    private static Map<Long, PkmnDesc> mapPkmnDesc;
    private static Map<Long, Move> mapMove;

    private Context context;

    public PokedexDAO(final Context c) {
        this.context = c;
    }

    @NonNull
    public List<PkmnDesc> getListPkmnDesc(){
        return new ArrayList<>(getMapPkmnDesc().values());
    }

    @NonNull
    public List<PkmnDesc> getListPkmnDesc(boolean isOnlyLastEvolutions){
        Map<Long, PkmnDesc> map = new HashMap<>(getMapPkmnDesc());
        if(isOnlyLastEvolutions){
            for(Evolution e : getListEvolution()){
                map.remove(e.getPokedexNum());
            }
        }
        return new ArrayList<>(map.values());
    }

    @NonNull
    public Map<Long, PkmnDesc> getMapPkmnDesc(){
        if (MapUtils.isEmpty(mapPkmnDesc)) {
            mapPkmnDesc = new HashMap<>();
            List<PkmnDesc> list = new PokedexTableDAO(context).selectAll();
            for(PkmnDesc p : list){
                mapPkmnDesc.put(p.getPokedexNum(), p);
            }
        }
        return new HashMap<>(mapPkmnDesc);
    }

    public PkmnDesc getPokemonWithId(final long id) {
        return getMapPkmnDesc().get(id);
    }

    @NonNull
    public Map<Long, Move> getMapMove() {
        if (MapUtils.isEmpty(mapMove)) {
            mapMove = new HashMap<>();
            List<Move> list = new MoveTableDAO(context).selectAll();
            for(Move m : list){
                mapMove.put(m.getId(), m);
            }
        }
        return new HashMap<>(mapMove);
    }

    @NonNull
    public List<Move> getListMove() {
        return new ArrayList<>(getMapMove().values());
    }

    /**
     * @return the listEvolution
     */
    @NonNull
    public List<Evolution> getListEvolution() {
        if (CollectionUtils.isEmpty(listEvolution)) {
            listEvolution = new ArrayList<>();
            listEvolution.addAll(new EvolutionTableDAO(context).selectAll());
        }
        return new ArrayList<>(listEvolution);
    }



    /**
     * @return the listPkmnMove
     */
    @NonNull
    public List<PkmnMove> getListPkmnMove() {
        if (CollectionUtils.isEmpty(listPkmnMove)) {
            listPkmnMove = new ArrayList<>();
            listPkmnMove.addAll(new PkmnMoveTableDAO(context).selectAll());
        }
        return new ArrayList<>(listPkmnMove);
    }

    public List<Long> getFamillePokemon(final long pokedexNum) {
        List<Long> evolutionIds = new ArrayList<>();
        evolutionIds.addAll(findBasesPokemons(pokedexNum));
        evolutionIds.add(pokedexNum);
        evolutionIds.addAll(findEvolutionsPokemons(pokedexNum));
        return evolutionIds;
    }

    @NonNull
    private List<Long> findBasesPokemons(final long pokedexNum) {
        List<Long> listId = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getEvolutionId()) {
                listId.add(0, ev.getPokedexNum());
                listId.addAll(findBasesPokemons(ev.getPokedexNum()));
                break;
            }
        }
        return listId;
    }

    @NonNull
    private List<Long> findEvolutionsPokemons(final long pokedexNum) {
        final List<Long> listIds = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getPokedexNum()) {
                listIds.add(ev.getEvolutionId());
                listIds.addAll(findEvolutionsPokemons(ev.getEvolutionId()));
            }
        }
        return listIds;
    }

    public List<Move> getListMoveFor(final PkmnDesc p){
        return null; // FIXME
    }

    public List<PkmnDesc> getListPkmnFor(final Move m){
        List<PkmnDesc> results = new ArrayList<>();
        Map<Long, PkmnDesc> map = getMapPkmnDesc();
        for (PkmnMove pm : getListPkmnMove()) {
            if (m.getId() == pm.getMoveId()) {
                results.add(map.get(pm.getPokedexNum()));
            }
        }
        return results;
    }
}
