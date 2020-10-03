package com.pokemongostats.controller.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
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
import java.util.stream.Collectors;

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
    public Map<String, PkmnDesc> getMapPkmnDesc(){
        if (MapUtils.isEmpty(mapPkmnDesc)) {
            mapPkmnDesc = new HashMap<>();
            final PkmnTableDAO dao = new PkmnTableDAO(context);
            List<PkmnDesc> list = dao.selectAll();
            for(PkmnDesc p : list){
                mapPkmnDesc.put(p.getPokedexNum() + p.getForm(), p);
            }
        }
        return new HashMap<>(mapPkmnDesc);
    }

    public PkmnDesc getPokemonWithId(final long id, final String form) {
        return getMapPkmnDesc().get(id + form);
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

    public List<Evolution> getListEvolutionFor(PkmnDesc p) {
        return getListEvolution().stream().filter(e -> e.isFrom(p)).collect(Collectors.toList());
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
    public List<Evolution> findBasesPokemons(final long pokedexNum, final String form) {
        List<Evolution> listId = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getEvolutionId() && form.equals(ev.getEvolutionForm())) {
                listId.addAll(findBasesPokemons(ev.getBasePkmnId(), ev.getBasePkmnForm()));
                listId.add(ev);
                break;
            }
        }
        return listId;
    }

    @NonNull
    public List<Evolution> findEvolutionsPokemons(final long pokedexNum, final String form) {
        final List<Evolution> listIds = new ArrayList<>();
        for (Evolution ev : getListEvolution()) {
            if (pokedexNum == ev.getBasePkmnId() && form.equals(ev.getBasePkmnForm())) {
                listIds.add(ev);
                listIds.addAll(findEvolutionsPokemons(ev.getEvolutionId(),ev.getEvolutionForm()));
            }
        }
        return listIds;
    }

    public List<Move> getListMoveFor(final PkmnDesc p){
        final List<PkmnMove> lpm = getListPkmnMove();
        final List<Move> lm = new ArrayList<>();
        for(PkmnMove pm : lpm){
            if(pm.getPokedexNum() == p.getPokedexNum() && pm.getForm().equals(p.getForm())){
                // peut etre null si la langue n'existe pas
                Move found = getMapMove().get(pm.getMoveId());
                if(found != null){
                    lm.add(found);
                } else {
                    Log.i("MISSING", "Move "+pm.getMoveId()+" doesn't exist");
                }
            }
        }
        return lm;
    }

    public List<PkmnDesc> getListPkmnFor(final Move m){
        List<PkmnDesc> results = new ArrayList<>();
        Map<String, PkmnDesc> map = getMapPkmnDesc();
        for (PkmnMove pm : getListPkmnMove()) {
            if (m.getId() == pm.getMoveId()) {
                PkmnDesc pkmn = map.get(pm.getPokedexNum() + pm.getForm());
                if(pkmn != null){
                    results.add(pkmn);
                } else {
                    Log.i("MISSING", "Pkmn "+pm.getPokedexNum() + pm.getForm()+" doesn't exist");
                }
            }
        }
        return results;
    }
}
