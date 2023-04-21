package com.pokemongostats.model.filtersinfos;

import android.util.Log;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Type;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zapagon on 05/03/2017.
 */
public class MoveFilterInfo implements MultipleFilterInfo {

	private static final String NAME_KEY = "NAME_KEY";
	private static final String TYPE_KEY = "TYPE_KEY";

	private String name;
	private Type type;

	public MoveFilterInfo() {
		this.clear();
	}

	@Override
	public void clear() {
		this.name = null;
		this.type = null;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public CharSequence toFilter() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(NAME_KEY, this.name);
			jsonObject.put(TYPE_KEY, this.type == null ? null : this.type.name());
		} catch (JSONException e) {
			Log.e(TagUtils.DEBUG, "Error parsing MoveFilterInfo json", e);
		}
		return jsonObject.toString();
	}

	@Override
	public void fromFilter(CharSequence stringFilter) {
		try {
			JSONObject jsonObject = new JSONObject(stringFilter.toString());
			this.name = jsonObject.optString(NAME_KEY, "");
			String typeVal = jsonObject.optString(TYPE_KEY, "");
			if (!typeVal.isEmpty()) {
				this.type = Type.valueOf(typeVal);
			} else {
				this.type = null;
			}
		} catch (JSONException e) {
			this.name = stringFilter.toString();
		}
	}

}