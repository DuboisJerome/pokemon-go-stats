package com.pokemongostats.controller.external.json;

import com.pokemongostats.controller.external.AbstractSplitter;
import com.pokemongostats.controller.external.IExternalDataPokedex;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.model.bean.UpdateStatus;
import com.pokemongostats.model.external.json.MoveParserJson;
import com.pokemongostats.model.external.json.PkmnDescParserJson;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonEvol;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonForm;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonMoveArene;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonMoveCombat;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonPkmnDesc;
import com.pokemongostats.controller.external.json.transformer.TransformerJsonPkmnMove;
import com.pokemongostats.controller.external.json.splitter.AbstractSplitterJson;

import java.util.List;

public abstract class AbstractParserJson<T> implements IExternalDataPokedex<T> {

	@Override
	public void readDatas(T in, UpdateStatus updateStatus) throws ParserException {

		updateStatus.publishMainProgress("Split fichier json", 5);

		updateStatus.publishSecondaryProgress("Créer splitter",0);
		AbstractSplitterJson<T> splitter = getSplitter();
		updateStatus.publishSecondaryProgress("Split en groupes",10);
		splitter.split(in);
		updateStatus.finnishSecondary();

		// Get list
		updateStatus.publishMainProgress("Transform json to bean", 25);

		updateStatus.publishSecondaryProgress("Transform pkmn",0);
		List<PkmnDescParserJson> lstPkmn = splitter.getLstElemTransform(AbstractSplitter.GRP_PKMN, new TransformerJsonPkmnDesc());

		updateStatus.publishSecondaryProgress("Transform move arène",33);
		List<MoveParserJson> lstMoveArene = splitter.getLstElemTransform(AbstractSplitter.GRP_MOVE_ARENE, new TransformerJsonMoveArene());

		updateStatus.publishSecondaryProgress("Transform move combat",66);
		List<MoveParserJson> lstMoveCombat= splitter.getLstElemTransform(AbstractSplitter.GRP_MOVE_COMBAT, new TransformerJsonMoveCombat());

		updateStatus.finnishSecondary();

		// Update
		updateStatus.publishMainProgress("Update bean", 50);

		updateStatus.publishSecondaryProgress("Merge move arène et combat",0);
		List<MoveParserJson> lstMove = TransformerJsonMoveArene.mergeMoveAreneAndCombat(lstMoveArene, lstMoveCombat);

		updateStatus.publishSecondaryProgress("Cleans forms",25);
		lstPkmn = TransformerJsonForm.cleanForms(lstPkmn);

		updateStatus.publishSecondaryProgress("Update lst pkmn move",50);
		TransformerJsonPkmnMove.updateLstPkmnMove(lstPkmn, lstMove);

		updateStatus.publishSecondaryProgress("Update lst Evol",75);
		TransformerJsonEvol.updateLstEvol(lstPkmn);

		updateStatus.finnishSecondary();

		// Create sql
		// TODO verifier les objets avant d'aller plus loin
	}

	protected abstract AbstractSplitterJson<T> getSplitter();

}