package com.pokemongostats.model.filtersinfos;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.utils.PreferencesUtils;

import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Zapagon on 05/03/2017.
 */
@Getter
@Setter
public class PkmnDescFilterInfo extends ViewModel implements MultipleFilterInfo {

	private static final String NAME_KEY = "NAME_KEY";
	private static final String TYPE_1_KEY = "TYPE_1_KEY";
	private static final String TYPE_2_KEY = "TYPE_2_KEY";
	private static final String MIN_CP_KEY = "MIN_CP_KEY";
	private static final String MIN_ATT_KEY = "MIN_ATT_KEY";
	private static final String MIN_DEF_KEY = "MIN_DEF_KEY";
	private static final String MIN_STA_KEY = "MIN_STA_KEY";
	private static final String MOVE_KEY = "MOVE_KEY";


	private static final double NULL_DOUBLE = -1;

	private String name;
	private Type type1, type2;
	private double minCP, minAtt, minDef, minSta;
	private Move move;

	public PkmnDescFilterInfo() {
		this.clear();
	}

	@Override
	public void clear() {
		this.name = null;
		this.type1 = this.type2 = null;
		this.minCP = this.minAtt = this.minDef = this.minSta = NULL_DOUBLE;
		this.move = null;
	}

	@Override
	public CharSequence toFilter() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(NAME_KEY, this.name);
			jsonObject.put(TYPE_1_KEY, this.type1 == null ? null : this.type1.name());
			jsonObject.put(TYPE_2_KEY, this.type2 == null ? null : this.type2.name());
			jsonObject.put(MIN_CP_KEY, this.minCP);
			jsonObject.put(MIN_ATT_KEY, this.minAtt);
			jsonObject.put(MIN_DEF_KEY, this.minDef);
			jsonObject.put(MIN_STA_KEY, this.minSta);
			jsonObject.put(MOVE_KEY, this.move == null ? null : this.move.getId());
			PreferencesUtils.getInstance().addPrefToJson(jsonObject);
		} catch (Exception e) {
			Log.e(TagUtils.DEBUG, "Error parsing PkmnDescFilterInfo json", e);
		}
		return jsonObject.toString();
	}

	@Override
	public void fromFilter(CharSequence stringFilter) {
		try {
			JSONObject jsonObject = new JSONObject(stringFilter.toString());
			this.name = jsonObject.optString(NAME_KEY, "");
			String type1Val = jsonObject.optString(TYPE_1_KEY, "");
			this.type1 = "".equals(type1Val) ? null : Type.valueOf(type1Val);
			String type2Val = jsonObject.optString(TYPE_2_KEY, "");
			this.type2 = "".equals(type2Val) ? null : Type.valueOf(type2Val);
			this.minCP = jsonObject.optDouble(MIN_CP_KEY, NULL_DOUBLE);
			this.minAtt = jsonObject.optDouble(MIN_ATT_KEY, NULL_DOUBLE);
			this.minDef = jsonObject.optDouble(MIN_DEF_KEY, NULL_DOUBLE);
			this.minSta = jsonObject.optDouble(MIN_STA_KEY, NULL_DOUBLE);
			long moveId = jsonObject.optLong(MOVE_KEY, -1);
			this.move = moveId == -1 ? null : PokedexDAO.getInstance().getMapMove().get(moveId);
			// Inutile de lire les préférences ici, c'est juste pour voir si elles ont changés dans le filter de l'adapter
		} catch (Exception e) {
			this.name = stringFilter.toString();
		}
	}

}