package com.pokemongostats.controller.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.model.bean.ClePkmn;
import com.pokemongostats.model.bean.PkmnMoveComplet;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Created by Zapagon on 05/04/2017.
 * Pokedex dao
 */
public class PokedexDAO {

	private final Map<ClePkmn,PkmnDesc> mapPkmnDesc = new ConcurrentHashMap<>();
	private final Map<Long,Move> mapMove = new ConcurrentHashMap<>();

	private static PokedexDAO instance;

	private PokedexDAO() {
		reset();
	}

	public void reset() {
		// Init PkmnDesc
		List<PkmnDesc> lstPkmnDesc = PkmnTableDAO.getInstance().selectAll();
		List<Evolution> lstEvol = EvolutionTableDAO.getInstance().selectAll();

		this.mapPkmnDesc.clear();
		for (PkmnDesc p : lstPkmnDesc) {
			this.mapPkmnDesc.put(new ClePkmn(p), p);
			// S'il existe encore une evol et ce n'est pas une méga
			List<Evolution> lstEvolPkmn = lstEvol.stream().filter(e -> e.isEvolOf(p)).collect(Collectors.toList());
			p.setLastEvol(lstEvolPkmn.stream().allMatch(e -> e.getBasePkmnId() == e.getEvolutionId()));
		}
		// Init Move
		this.mapMove.clear();
		List<Move> lstMove = MoveTableDAO.getInstance().selectAll();
		for (Move m : lstMove) {
			this.mapMove.put(m.getId(), m);
		}
	}

	public static synchronized PokedexDAO getInstance() {
		if (instance == null) {
			instance = new PokedexDAO();
		}
		return instance;
	}

	@NonNull
	public List<PkmnDesc> getListPkmnDesc() {
		return new ArrayList<>(getMapPkmnDesc().values());
	}

	public int getNbPkmn() {
		return getMapPkmnDesc().size();
	}

	public int getNbMove() {
		return getMapMove().size();
	}

	@NonNull
	public Map<ClePkmn,PkmnDesc> getMapPkmnDesc() {
		return this.mapPkmnDesc;
	}

	public PkmnDesc getPokemon(long id, String form) {
		return getPokemon(new ClePkmn(id, form));
	}

	public PkmnDesc getPokemon(ClePkmn cle) {
		return getMapPkmnDesc().get(cle);
	}

	public PkmnDesc getPokemon(PkmnMove pm) {
		return getPokemon(pm.getPokedexNum(), pm.getForm());
	}

	@NonNull
	public synchronized Map<Long,Move> getMapMove() {
		return this.mapMove;
	}

	@NonNull
	public List<Move> getListMove() {
		return new ArrayList<>(getMapMove().values());
	}

	public Move getMove(long idMove) {
		return this.mapMove.get(idMove);
	}

	public Move getMove(PkmnMove pm) {
		return this.mapMove.get(pm.getMoveId());
	}

	@NonNull
	public List<Evolution> findBasesPokemons(long pokedexNum, String form) {
		List<Evolution> listId = new ArrayList<>();
		List<Evolution> lstBasesCurrentPkmn = EvolutionTableDAO.getInstance().getBases(pokedexNum, form);
		for (Evolution ev : lstBasesCurrentPkmn) {
			listId.addAll(findBasesPokemons(ev.getBasePkmnId(), ev.getBasePkmnForm()));
			listId.add(ev);
		}
		return listId;
	}

	@NonNull
	public List<Evolution> findEvolutionsPokemons(long pokedexNum, String form) {
		List<Evolution> listIds = new ArrayList<>();
		List<Evolution> lstEvolsCurrentPkmn = EvolutionTableDAO.getInstance().getEvols(pokedexNum, form);
		for (Evolution ev : lstEvolsCurrentPkmn) {
			listIds.add(ev);
			listIds.addAll(findEvolutionsPokemons(ev.getEvolutionId(), ev.getEvolutionForm()));
		}
		return listIds;
	}

	public Map<String,List<Evolution>> findBasesEtEvolsPokemons(long pokedexNum, String form) {
		Map<String,List<Evolution>> map = new HashMap<>();
		List<Evolution> lstBasesEtEvolsCurrentPkmn = EvolutionTableDAO.getInstance().getBasesEtEvols(pokedexNum, form);
		// Bases
		List<Evolution> listId = new ArrayList<>();
		// lst des bases pour laquelle le pkmn courant est l'évolution
		Evolution baseEv = lstBasesEtEvolsCurrentPkmn.stream().filter(ev -> pokedexNum == ev.getEvolutionId() && form.equals(ev.getEvolutionForm())).findFirst().orElse(null);
		if (baseEv != null) {
			// Récupère le pkmn de base du pkmn de base de façon récursive
			listId.addAll(findBasesPokemons(baseEv.getBasePkmnId(), baseEv.getBasePkmnForm()));
			listId.add(baseEv);
		}
		map.put("BASE", listId);
		listId = new ArrayList<>();
		for (Evolution ev : lstBasesEtEvolsCurrentPkmn) {
			if (pokedexNum == ev.getBasePkmnId() && form.equals(ev.getBasePkmnForm())) {
				listId.add(ev);
				// Récupère l'évolution de l'évolution de façon récursive
				listId.addAll(findEvolutionsPokemons(ev.getEvolutionId(), ev.getEvolutionForm()));
			}
		}
		map.put("EVOL", listId);
		return map;
	}

	public List<PkmnMoveComplet> getLstPkmnMoveCompletFor(PkmnDesc p) {
		List<PkmnMove> l = PkmnMoveTableDAO.getInstance().selectAllForPkmn(p);
		List<PkmnMoveComplet> results = new ArrayList<>();
		for (PkmnMove pm : l) {
			PkmnMoveComplet pmc = new PkmnMoveComplet(pm);
			pmc.setOwner(getPokemon(new ClePkmn(pmc)));
			pmc.setMove(getMapMove().get(pmc.getMoveId()));
			results.add(pmc);
		}
		return results;
	}

	public List<PkmnDesc> getListPkmnFor(Move m) {

		List<ClePkmn> lst = PkmnMoveTableDAO.getInstance().getListPkmnIdFor(m);
		List<PkmnDesc> results = new ArrayList<>();
		Map<ClePkmn,PkmnDesc> map = getMapPkmnDesc();
		for (ClePkmn idPkmn : lst) {
			PkmnDesc pkmn = map.get(idPkmn);
			if (pkmn != null) {
				results.add(pkmn);
			} else {
				Log.i("MISSING", "Pkmn " + idPkmn + " doesn't exist");
			}
		}
		return results;
	}
}