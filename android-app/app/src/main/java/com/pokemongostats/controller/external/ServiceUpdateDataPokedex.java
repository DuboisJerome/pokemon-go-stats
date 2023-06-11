package com.pokemongostats.controller.external;

import android.content.Context;

import com.pokemongostats.controller.TaskRunner;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.external.json.ParserJsonInputStream;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.view.dialog.UpdateProgressionDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;

public class ServiceUpdateDataPokedex {
	private static final String REMOTE_FILE_URL = "https://raw.githubusercontent.com/PokeMiners/game_masters/master/latest/latest.json";

	private static InputStream getInputStream(Context c) throws IOException {
		return new URL(REMOTE_FILE_URL).openStream();
	}

	public static void getPokedexDataAsync(UpdateProgressionDialogFragment dialog) {
		Callable<PokedexData> callable = () -> {
			try (InputStream in = getInputStream(dialog.getContext())) {
				IExternalDataPokedex<InputStream> externalDataPokedex = new ParserJsonInputStream();
				return externalDataPokedex.readDatas(in, dialog.getStatus());
			} catch (IOException | ParserException e) {
				Log.error("Error updating data", e);
			}
			return null;
		};

		TaskRunner.Callback<PokedexData> callback = new TaskRunner.Callback<>() {
			@Override
			public void onComplete(PokedexData pokedexData) {
				dialog.getStatus().finnish();
				logSql(pokedexData);
				Log.debug("END update : SUCCESS");
				dialog.onCompleteGetPokedexData(pokedexData);
			}

			@Override
			public void onError(Exception e) {
				Log.error("END update : FAILURE", e);
			}
		};

		Log.debug("Start retrieve data from inputstream");
		Future<?> future = TaskRunner.executeNewRunnerAsync(callable, callback);
		dialog.addAnnulerListener(() -> {
			if (!future.isDone() && !future.isCancelled()) {
				future.cancel(true);
			}
		});
	}

	public static void logSql(PokedexData data) {
		logSql(data.getDataPkmn(), PkmnTableDAO.getInstance());
		logSql(data.getDataMove(), MoveTableDAO.getInstance());
		logSql(data.getDataPkmnMove(), PkmnMoveTableDAO.getInstance());
		logSql(data.getDataEvol(), EvolutionTableDAO.getInstance());
	}

	private static <T extends IObjetBdd> void logSql(PokedexData.Data<T> datas, TableDAO<T> dao) {
		for (T t : datas.getLstToCreate()) {
			String req = dao.buildReqInsert(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
			}
		}
		for (var entry : datas.getLstToUpdate().entrySet()) {
			T tOld = entry.getKey();
			T tNew = entry.getValue();
			String req = dao.buildReqUpdate(tOld, tNew);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
			}
		}
		for (T t : datas.getLstToDelete()) {
			String req = dao.buildReqDelete(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
			}
		}
	}

	public static void processDatas(PokedexData data) {
		processDatas(data.getDataPkmn(), PkmnTableDAO.getInstance());
		processDatas(data.getDataMove(), MoveTableDAO.getInstance());
		processDatas(data.getDataPkmnMove(), PkmnMoveTableDAO.getInstance());
		processDatas(data.getDataEvol(), EvolutionTableDAO.getInstance());
		PokedexDAO.getInstance().reset();
	}

	private static <T extends IObjetBdd> void processDatas(PokedexData.Data<T> datas, TableDAO<T> dao) {
		for (T t : datas.getLstToCreate()) {
			String req = dao.buildReqInsert(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.i("SQL", req);
				dao.creer(t);
			}
		}
		for (var entry : datas.getLstToUpdate().entrySet()) {
			T tOld = entry.getKey();
			T tNew = entry.getValue();
			String req = dao.buildReqUpdate(tOld, tNew);
			if (req != null && !req.isEmpty()) {
				android.util.Log.i("SQL", req);
				dao.update(tOld, tNew);
			}
		}
		for (T t : datas.getLstToDelete()) {
			String req = dao.buildReqDelete(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.i("SQL", req);
				dao.delete(t);
			}
		}
	}
}