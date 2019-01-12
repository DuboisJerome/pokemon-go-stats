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
import com.pokemongostats.model.table.PokedexTable;

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
    private static Map<String, PkmnDesc> mapPkmnDesc;
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
        Map<String, PkmnDesc> map = new HashMap<>(getMapPkmnDesc());
        if(isOnlyLastEvolutions){
            for(Evolution e : getListEvolution()){
                map.remove(e.getPokedexNum() + e.getForme());
            }
        }
        return new ArrayList<>(map.values());
    }

    @NonNull
    public Map<String, PkmnDesc> getMapPkmnDesc(){
        if (MapUtils.isEmpty(mapPkmnDesc)) {
            mapPkmnDesc = new HashMap<>();
            final PokedexTableDAO dao = new PokedexTableDAO(context);
            List<PkmnDesc> list = dao.selectAll();
            for(PkmnDesc p : list){
                mapPkmnDesc.put(p.getPokedexNum() + p.getForme(), p);
            }
        }
        return new HashMap<>(mapPkmnDesc);
    }

    public PkmnDesc getPokemonWithId(final long id, final String forme) {
        return getMapPkmnDesc().get(id + forme);
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

    @NonNull
    public List<Evolution> findBasesPokemons(final long pokedexNum, final String forme) {
        List<Evolution> listId = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getEvolutionId() && forme.equals(ev.getForme())) {
                listId.add(0, ev);
                listId.addAll(findBasesPokemons(ev.getPokedexNum(), ev.getForme()));
                break;
            }
        }
        return listId;
    }

    @NonNull
    public List<Evolution> findEvolutionsPokemons(final long pokedexNum, final String forme) {
        final List<Evolution> listIds = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getPokedexNum() && forme.equals(ev.getForme())) {
                listIds.add(ev);
                listIds.addAll(findEvolutionsPokemons(ev.getEvolutionId(),ev.getForme()));
            }
        }
        return listIds;
    }

    public List<Move> getListMoveFor(final PkmnDesc p){
        final List<PkmnMove> lpm = getListPkmnMove();
        final List<Move> lm = new ArrayList<>();
        for(PkmnMove pm : lpm){
            if(pm.getPokedexNum() == p.getPokedexNum() && pm.getForme().equals(p.getForme())){
                lm.add(getMapMove().get(pm.getMoveId()));
            }
        }
        return lm;
    }

    public List<PkmnDesc> getListPkmnFor(final Move m){
        List<PkmnDesc> results = new ArrayList<>();
        Map<String, PkmnDesc> map = getMapPkmnDesc();
        for (PkmnMove pm : getListPkmnMove()) {
            if (m.getId() == pm.getMoveId()) {
                results.add(map.get(pm.getPokedexNum() + pm.getForme()));
            }
        }
        return results;
    }
}
