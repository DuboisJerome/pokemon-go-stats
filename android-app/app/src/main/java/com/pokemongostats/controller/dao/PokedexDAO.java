package com.pokemongostats.controller.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Zapagon on 05/04/2017.
 * Pokedex dao
 */
public class PokedexDAO {

	private final Map<String,PkmnDesc> mapPkmnDesc = new ConcurrentHashMap<>();
	private final Map<Long,Move> mapMove = new ConcurrentHashMap<>();
	private final List<PkmnMove> listPkmnMove = new ArrayList<>();
	private final List<Evolution> listEvolution = new ArrayList<>();

	private static PokedexDAO instance;

	private PokedexDAO() {
		// Init PkmnDesc
		List<PkmnDesc> lstPkmnDesc = PkmnTableDAO.getInstance().selectAll();
		for (PkmnDesc p : lstPkmnDesc) {
			this.mapPkmnDesc.put(p.getUniqueId(), p);
		}
		// Init Move
		List<Move> lstMove = MoveTableDAO.getInstance().selectAll();
		for (Move m : lstMove) {
			this.mapMove.put(m.getId(), m);
		}
		// Init PkmnMove
		this.listPkmnMove.addAll(PkmnMoveTableDAO.getInstance().selectAll());
		// Init Evolution
		this.listEvolution.addAll(EvolutionTableDAO.getInstance().selectAll());
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

	@NonNull
	public Map<String,PkmnDesc> getMapPkmnDesc() {
		return this.mapPkmnDesc;
	}

	public PkmnDesc getPokemonWithId(long id, String form) {
		return getMapPkmnDesc().get(id + "_" + form);
	}

	public PkmnDesc getPokemonWithId(String uniqueId) {
		return getMapPkmnDesc().get(uniqueId);
	}


	@NonNull
	public synchronized Map<Long,Move> getMapMove() {
		return this.mapMove;
	}

	@NonNull
	public List<Move> getListMove() {
		return new ArrayList<>(getMapMove().values());
	}

	/**
	 * @return the listEvolution
	 */
	@NonNull
	public synchronized List<Evolution> getListEvolution() {
		return this.listEvolution;
	}

	public List<Evolution> getListEvolutionFor(PkmnDesc p) {
		return getListEvolution().stream().filter(e -> e.isFrom(p)).collect(Collectors.toList());
	}

	/**
	 * @return the listPkmnMove
	 */
	@NonNull
	public synchronized List<PkmnMove> getListPkmnMove() {
		return this.listPkmnMove;
	}

	@NonNull
	public List<Evolution> findBasesPokemons(long pokedexNum, String form) {
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
	public List<Evolution> findEvolutionsPokemons(long pokedexNum, String form) {
		List<Evolution> listIds = new ArrayList<>();
		for (Evolution ev : getListEvolution()) {
			if (pokedexNum == ev.getBasePkmnId() && form.equals(ev.getBasePkmnForm())) {
				listIds.add(ev);
				listIds.addAll(findEvolutionsPokemons(ev.getEvolutionId(), ev.getEvolutionForm()));
			}
		}
		return listIds;
	}

	public List<Move> getListMoveFor(PkmnDesc p) {
		List<PkmnMove> lpm = getListPkmnMove();
		List<Move> lm = new ArrayList<>();
		for (PkmnMove pm : lpm) {
			if (pm.getUniquePkmnId().equals(p.getUniqueId())) {
				// peut etre null si la langue n'existe pas
				Move found = getMapMove().get(pm.getMoveId());
				if (found != null) {
					lm.add(found);
				} else {
					Log.i("MISSING", "Move " + pm.getMoveId() + " doesn't exist");
				}
			}
		}
		return lm;
	}

	public PkmnMove getPkmnMoveFor(PkmnDesc p, Move m) {
		if (p == null || m == null) return null;
		return getListPkmnMove().stream().filter(pm -> pm.getUniquePkmnId().equals(p.getUniqueId()) && pm.getMoveId() == m.getId()).findAny().orElse(null);
	}

	public List<PkmnDesc> getListPkmnFor(Move m) {
		List<PkmnDesc> results = new ArrayList<>();
		Map<String,PkmnDesc> map = getMapPkmnDesc();
		for (PkmnMove pm : getListPkmnMove()) {
			if (m.getId() == pm.getMoveId()) {
				PkmnDesc pkmn = map.get(pm.getUniquePkmnId());
				if (pkmn != null) {
					results.add(pkmn);
				} else {
					Log.i("MISSING", "Pkmn " + pm.getUniquePkmnId() + " doesn't exist");
				}
			}
		}
		return results;
	}
}