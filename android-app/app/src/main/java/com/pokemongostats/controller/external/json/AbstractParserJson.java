package com.pokemongostats.controller.external.json;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.external.AbstractSplitter;
import com.pokemongostats.controller.external.IExternalDataPokedex;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.model.bean.ClePkmn;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.model.bean.UpdateStatus;
import com.pokemongostats.model.bean.UpdateStatusSecondaire;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.external.json.EvolutionParserJson;
import com.pokemongostats.model.external.json.MoveParserJson;
import com.pokemongostats.model.external.json.PkmnDescParserJson;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonEvol;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonForm;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonMoveArene;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonMoveCombat;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonPkmnDesc;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonPkmnMove;
import com.pokemongostats.controller.external.json.splitter.AbstractSplitterJson;
import com.pokemongostats.model.external.json.PkmnMoveParserJson;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractParserJson<T> implements IExternalDataPokedex<T> {

	@Override
	public PokedexData readDatas(T in, UpdateStatus us) throws ParserException {

		// Splitting
		us.startingEtape("Split fichier json");

		AbstractSplitterJson<T> splitter = getSplitter();

		us.updateDescEtape("Split");
		splitter.split(in, new UpdateStatusSecondaire(us));
		us.updateProgressionEtape(100);

		us.finnishEtape(25);

		// Get list
		us.startingEtape("Transform json to bean");

		us.updateDescEtape("Transform pkmn");
		List<PkmnDescParserJson> lstPkmn = splitter.getLstElemTransform(AbstractSplitter.GRP_PKMN, new TransformerJsonPkmnDesc());
		us.updateProgressionEtape(33);

		us.updateDescEtape("Transform move arène");
		List<MoveParserJson> lstMoveArene = splitter.getLstElemTransform(AbstractSplitter.GRP_MOVE_ARENE, new TransformerJsonMoveArene());
		us.updateProgressionEtape(66);

		us.updateDescEtape("Transform move combat");
		List<MoveParserJson> lstMoveCombat = splitter.getLstElemTransform(AbstractSplitter.GRP_MOVE_COMBAT, new TransformerJsonMoveCombat());
		us.updateProgressionEtape(100);

		us.finnishEtape(50);

		// Update
		us.startingEtape("Update bean");

		us.updateDescEtape("Merge move arène et combat");
		List<MoveParserJson> lstMove = TransformerJsonMoveArene.mergeMoveAreneAndCombat(lstMoveArene, lstMoveCombat);
		us.updateProgressionEtape(25);

		us.updateDescEtape("Update lst pkmn move");
		TransformerJsonPkmnMove.updateLstPkmnMove(lstPkmn, lstMove);
		us.updateProgressionEtape(50);

		us.updateDescEtape("Update lst Evol");
		TransformerJsonEvol.updateLstEvol(lstPkmn);
		us.updateProgressionEtape(75);

		us.updateDescEtape("Cleans forms");
		lstPkmn = TransformerJsonForm.cleanForms(lstPkmn);
		us.updateProgressionEtape(100);

		us.finnishEtape(75);

		return buildResultReadDatas(lstPkmn, lstMove, us);
	}

	protected abstract AbstractSplitterJson<T> getSplitter();

	protected final PokedexData buildResultReadDatas(List<PkmnDescParserJson> lstPkmn, List<MoveParserJson> lstMove, UpdateStatus us) {

		PokedexData pokedexData = new PokedexData();
		addResultPkmnDescToPokedexData(pokedexData, lstPkmn, us);
		List<PkmnMoveParserJson> lstPkmnMoveJson = lstPkmn.stream().flatMap(p -> p.getLstPkmnMove().stream()).collect(Collectors.toList());
		addResultMoveToPokedexData(pokedexData, lstMove, lstPkmnMoveJson, us);
		return pokedexData;
	}

	private void addResultPkmnDescToPokedexData(PokedexData pokedexData, List<PkmnDescParserJson> lstPkmn, UpdateStatus us) {

		List<PkmnMove> lstAllPkmnMoveExistant = PkmnMoveTableDAO.getInstance().selectAll();
		List<Evolution> lstAllEvolutionExistante = EvolutionTableDAO.getInstance().selectAll();

		us.startingEtape("Boucle sur les pkmns de l'inputstream");
		int count = lstPkmn.size();
		int cpt = 0;
		for (PkmnDescParserJson pkmnParse : lstPkmn) {
			PkmnDesc pkmnExistant = PokedexDAO.getInstance().getPokemon(new ClePkmn(pkmnParse));
			if (pkmnExistant == null) {
				Log.info("Création du pkmn " + pkmnParse);
				pokedexData.getDataPkmn().addCreate(pkmnParse);
				pokedexData.getDataPkmni18n().addCreate(pkmnParse.getI18n());
			} else {
				if (PkmnTableDAO.getInstance().isNeedUpdate(pkmnExistant, pkmnParse)) {
					Log.debug("Pkmn déjà existant " + pkmnParse);
					pokedexData.getDataPkmn().addUpdate(pkmnExistant, pkmnParse);
				}

				// PkmnMove
				addResultPkmnMoveToPokedexData(pokedexData, pkmnParse, lstAllPkmnMoveExistant);

				// Evol
				addResultEvolToPokedexData(pokedexData, pkmnParse, lstAllEvolutionExistante);
			}

			us.updateProgressionEtape(cpt++, count);
			us.updateDescEtape("Analyse - " + pkmnParse);
		}
		us.finnishEtape(85);

		us.startingEtape("Boucle sur les pkmns de la bdd");
		count = PokedexDAO.getInstance().getNbPkmn();
		cpt = 0;
		for (PkmnDesc pkmnDescExistant : PokedexDAO.getInstance().getListPkmnDesc()) {
			PkmnDesc pkmnNew = CollectionUtils.find(lstPkmn, pNew -> pNew.equals(pkmnDescExistant));
			if (pkmnNew == null) {
				PkmnDesc pUpdate = new PkmnDesc(pkmnDescExistant);
				pUpdate.getTags().add(PkmnTags.NOT_IN_GAME);
				if (PkmnTableDAO.getInstance().isNeedUpdate(pkmnDescExistant, pUpdate)) {
					Log.info("Update pkmn existant " + pkmnDescExistant);
					pokedexData.getDataPkmn().addUpdate(pkmnDescExistant, pUpdate);
				}
			}
			us.updateProgressionEtape(cpt++, count);
			us.updateDescEtape("Analyse - " + pkmnDescExistant);
		}
		us.finnishEtape(90);
	}

	private void addResultPkmnMoveToPokedexData(PokedexData pokedexData, PkmnDescParserJson pkmnParse, List<PkmnMove> lstAllPkmnMoveExistant) {
		List<PkmnMove> lstPkmnMoveExistant = lstAllPkmnMoveExistant.stream().filter(pmExistant -> pmExistant.isFor(pkmnParse)).collect(Collectors.toList());
		for (PkmnMoveParserJson pkmnMoveParse : pkmnParse.getLstPkmnMove()) {
			PkmnMove pkmnMoveExistant = CollectionUtils.find(lstPkmnMoveExistant, pm -> pm.getMoveId() == pkmnMoveParse.getMoveId());
			if (pkmnMoveExistant == null) {
				Log.info("Création du PkmnMove " + pkmnMoveParse);
				pokedexData.getDataPkmnMove().addCreate(pkmnMoveParse);
			} else {
				if (PkmnMoveTableDAO.getInstance().isNeedUpdate(pkmnMoveExistant, pkmnMoveParse)) {
					Log.debug("PkmnMove déjà existant " + pkmnMoveParse);
					pokedexData.getDataPkmnMove().addUpdate(pkmnMoveExistant, pkmnMoveParse);
				}
			}
		}
		for (PkmnMove pkmnMoveExistant : lstPkmnMoveExistant) {
			PkmnMove pkmnMoveNew = CollectionUtils.find(pkmnParse.getLstPkmnMove(), pmNew -> pmNew.equals(pkmnMoveExistant));
			if (pkmnMoveNew == null) {
				Log.info("Delete pkmnmove existant " + pkmnMoveExistant);
				pokedexData.getDataPkmnMove().addDelete(pkmnMoveExistant);
			}
		}
	}

	private void addResultEvolToPokedexData(PokedexData pokedexData, PkmnDescParserJson pkmnParse, List<Evolution> lstAllEvolutionExistante) {
		List<Evolution> lstEvolExistante = lstAllEvolutionExistante.stream().filter(evExistante -> evExistante.isEvolOf(pkmnParse)).collect(Collectors.toList());
		for (EvolutionParserJson evolParse : pkmnParse.getLstEvol()) {
			Evolution evolExistante = CollectionUtils.find(lstEvolExistante, e -> e.equals(evolParse));
			if (evolExistante == null) {
				Log.info("Création de l'évol " + evolParse);
				pokedexData.getDataEvol().addCreate(evolParse);
			} else {
				if (EvolutionTableDAO.getInstance().isNeedUpdate(evolExistante, evolParse)) {
					Log.debug("Evol déjà existante " + evolParse);
					pokedexData.getDataEvol().addUpdate(evolExistante, evolParse);
				}
			}
		}
		for (Evolution evolExistante : lstEvolExistante) {
			Evolution evolNew = CollectionUtils.find(pkmnParse.getLstEvol(), eNew -> eNew.equals(evolExistante));
			if (evolNew == null) {
				PkmnDesc pBase = PokedexDAO.getInstance().getPokemon(ClePkmn.getCleBaseEvol(evolExistante));
				PkmnDesc pEvol = PokedexDAO.getInstance().getPokemon(ClePkmn.getCleEvolEvol(evolExistante));
				if (pBase == null || pEvol == null) {
					Log.info("Delete Evol existante " + evolExistante);
					pokedexData.getDataEvol().addDelete(evolExistante);
				}
			}
		}
	}

	private void addResultMoveToPokedexData(PokedexData pokedexData, List<MoveParserJson> lstMove, List<PkmnMoveParserJson> lstPkmnMoveJson, UpdateStatus us) {

		us.startingEtape("Boucle sur les moves de l'inputstream");
		int count = lstMove.size();
		int cpt = 0;
		for (MoveParserJson moveParse : lstMove) {
			Move moveExistant = PokedexDAO.getInstance().getMove(moveParse.getId());
			if (moveExistant == null) {
				// On vérifie que le move est associé à au moins 1 pkmn
				PkmnMoveParserJson pmParseExistant = CollectionUtils.find(lstPkmnMoveJson, pmParse -> pmParse.getMoveId() == moveParse.getId());
				if (pmParseExistant != null) {
					Log.info("Création du move " + moveParse);
					pokedexData.getDataMove().addCreate(moveParse);
					pokedexData.getDataMovei18n().addCreate(moveParse.getI18n());
				} else {
					Log.debug("Non création du move " + moveParse + " car il n'est associé à aucun pkmn");
				}
			} else {
				if (MoveTableDAO.getInstance().isNeedUpdate(moveExistant, moveParse)) {
					Log.info("Update move existant " + moveExistant + " => " + moveParse);
					pokedexData.getDataMove().addUpdate(moveExistant, moveParse);
				}
			}
			us.updateProgressionEtape(cpt++, count);
			us.updateDescEtape("Analyse - " + moveParse);
		}
		us.finnishEtape(95);

		us.startingEtape("Boucle sur les moves de la bdd");
		count = PokedexDAO.getInstance().getNbMove();
		cpt = 0;
		for (Move moveExistant : PokedexDAO.getInstance().getListMove()) {
			Move moveNew = CollectionUtils.find(lstMove, mNew -> mNew.getId() == moveExistant.getId());
			if (moveNew == null) {
				Log.info("Suppression du move existant " + moveExistant + " car il n'est pas dans le fichier");
				pokedexData.getDataMove().addDelete(moveExistant);
			} else {
				List<PkmnMove> lstPmExistant = PkmnMoveTableDAO.getInstance().selectAllForMove(moveExistant);
				if (lstPmExistant.isEmpty()) {
					Log.info("Suppression du move existant " + moveExistant + " car il n'est associé à aucun pkmn");
					pokedexData.getDataMove().addDelete(moveExistant);
				}
			}
			us.updateProgressionEtape(cpt++, count);
			us.updateDescEtape("Analyse - " + moveExistant);
		}
		us.finnishEtape(100);
	}
}