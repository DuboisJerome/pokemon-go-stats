package com.pokemongostats.model.filtersinfos;

import android.util.Log;

import com.pokemongostats.model.bean.Type;

import org.json.JSONException;
import org.json.JSONObject;

import fr.commons.generique.controller.utils.TagUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Zapagon on 05/03/2017.
 */
@Getter
@Setter
public class PkmnDescTypeFilterInfo extends PkmnDescFilterInfo {

	private static String FILTRE_TYPE_EFF  = "TYPE_EFF";
	private static String FILTRE_EFF = "EFF";

	private Type typeEff;
	private double eff;

	public PkmnDescTypeFilterInfo() {
		this.clear();
	}

	@Override
	protected JSONObject toJsonObject() {
		JSONObject j = super.toJsonObject();
		try {
			j.put(FILTRE_TYPE_EFF, this.typeEff);
			j.put(FILTRE_EFF, this.eff);
		} catch (JSONException e) {
			Log.e(TagUtils.DEBUG, "Error parsing PkmnDescFilterInfo json", e);
		}
		return j;
	}

	@Override
	protected void fromJsonObject(JSONObject jsonObject) {
		super.fromJsonObject(jsonObject);
		String typeVal = jsonObject.optString(FILTRE_TYPE_EFF, "");
		this.typeEff = "".equals(typeVal) ? null : Type.valueOf(typeVal);
		this.eff = jsonObject.optDouble(FILTRE_EFF, 0D);
	}

}