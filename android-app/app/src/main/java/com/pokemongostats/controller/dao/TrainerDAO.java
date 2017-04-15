package com.pokemongostats.controller.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.controller.utils.MapUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.table.PkmnTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zapagon on 05/04/2017.
 * Pokedex dao
 */
public class TrainerDAO {

    private static List<Trainer> listTrainers;
    private static List<Pkmn> listPkmn;

    private Context context;

    public TrainerDAO(final Context c) {
        this.context = c;
    }

    /**
     * @return the listTrainers
     */
    @NonNull
    public List<Trainer> getListTrainers() {
        if (CollectionUtils.isEmpty(listTrainers)) {
            listTrainers = new ArrayList<>();
            listTrainers.addAll(new TrainerTableDAO(context).selectAll());
        }
        return new ArrayList<>(listTrainers);
    }

    /**
     * @return the listPkmn
     */
    @NonNull
    public List<Pkmn> getListPkmn() {
        if (CollectionUtils.isEmpty(listPkmn)) {
            listPkmn = new ArrayList<>();
            listPkmn.addAll(new PkmnTableDAO(context).selectAll());
        }
        return new ArrayList<>(listPkmn);
    }

    @NonNull
    public List<Pkmn> getListPkmnFor(final Trainer t){
        if(t == null){
            return new ArrayList<>();
        }
        return new PkmnTableDAO(context).selectAllIn(PkmnTable.OWNER_ID, false, new Object[]{t.getId()});
    }
}
