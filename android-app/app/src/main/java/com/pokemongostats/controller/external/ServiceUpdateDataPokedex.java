package com.pokemongostats.controller.external;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.fragment.app.FragmentManager;

import com.pokemongostats.controller.TaskRunner;
import com.pokemongostats.controller.db.pokemon.EvolutionTableDAO;
import com.pokemongostats.controller.db.pokemon.MoveTableDAO;
import com.pokemongostats.controller.db.pokemon.Movei18nTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnMoveTableDAO;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.db.pokemon.Pkmni18nTableDAO;
import com.pokemongostats.controller.external.json.ParserJsonInputStream;
import com.pokemongostats.model.bean.PokedexData;
import com.pokemongostats.model.bean.UpdateStatus;
import com.pokemongostats.view.dialog.UpdateProgressionDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;

public class ServiceUpdateDataPokedex {
	private static final String REMOTE_FILE_URL = "https://raw.githubusercontent.com/PokeMiners/game_masters/master/latest/latest.json";

	private static InputStream getInputStream(Context c) throws IOException {
		boolean isTestEnabled = false;
		InputStream in;
		if (isTestEnabled) {
			AssetManager assetManager = c.getAssets();
			in = assetManager.open("latest.json");
		} else {
			in = new URL(REMOTE_FILE_URL).openStream();
		}
		return in;
	}

	public static void updateData(Context c, FragmentManager fm) {
		final UpdateStatus updateStatus = new UpdateStatus();
		final UpdateProgressionDialogFragment dialog = new UpdateProgressionDialogFragment(updateStatus);
		dialog.setCancelable(false);
		dialog.show(fm, "UPATE_PROGRESS");

		Callable<Boolean> callable = () -> {
			boolean isOk = true;
			try (InputStream in = getInputStream(c)) {
				IExternalDataPokedex<InputStream> externalDataPokedex = new ParserJsonInputStream();
				PokedexData pokedexData = externalDataPokedex.readDatas(in, updateStatus);
				generateSql(pokedexData);
			} catch (IOException | ParserException e) {
				Log.error("Error updating data", e);
				isOk = false;
			}
			return isOk;
		};

		Log.info("Start update");
		TaskRunner.Callback<Boolean> callback = new TaskRunner.Callback<>() {
			@Override
			public void onComplete(Boolean result) {
				updateStatus.finnish();
				Log.info("END update : SUCCESS");
			}

			@Override
			public void onError(Exception e) {
				Log.error("END update : FAILURE", e);
			}
		};

		TaskRunner taskRunner = new TaskRunner();
		taskRunner.executeAsync(callable, callback);

	}

	private static void generateSql(PokedexData data) {
		generateSql(data.getDataPkmn(), PkmnTableDAO.getInstance());
		generateSql(data.getDataPkmni18n(), Pkmni18nTableDAO.getInstance());
		generateSql(data.getDataMove(), MoveTableDAO.getInstance());
		generateSql(data.getDataMovei18n(), Movei18nTableDAO.getInstance());
		generateSql(data.getDataPkmnMove(), PkmnMoveTableDAO.getInstance());
		generateSql(data.getDataEvol(), EvolutionTableDAO.getInstance());
	}

	private static <T extends IObjetBdd> PokedexData.Data<String> generateSql(PokedexData.Data<T> datas, TableDAO<T> dao) {
		PokedexData.Data<String> results = new PokedexData.Data<>();
		for (T t : datas.getLstToCreate()) {
			String req = dao.buildReqInsert(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
				results.addCreate(req);
			}
		}
		for (var entry : datas.getLstToUpdate().entrySet()) {
			T tOld = entry.getKey();
			T tNew = entry.getValue();
			String req = dao.buildReqUpdate(tOld, tNew);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
				results.addUpdate("", req);
			}
		}
		for (T t : datas.getLstToDelete()) {
			String req = dao.buildReqDelete(t);
			if (req != null && !req.isEmpty()) {
				android.util.Log.d("SQL", req);
				results.addDelete(req);
			}
		}
		return results;
	}
}