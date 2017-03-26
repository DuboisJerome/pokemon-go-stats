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
        this.reset();
    }

    @Override
    public void reset() {
        name = null;
        type = null;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CharSequence toStringFilter() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NAME_KEY, name);
            jsonObject.put(TYPE_KEY, type == null ? null : type.name());
        } catch (JSONException e) {
            Log.e(TagUtils.DEBUG, "Error parsing MoveFilterInfo json", e);
        }
        return jsonObject.toString();
    }

    @Override
    public void updateFromStringFilter(final CharSequence stringFilter) {
        try {
            JSONObject jsonObject = new JSONObject(stringFilter.toString());
            name = jsonObject.optString(NAME_KEY, null);
            String typeVal = jsonObject.optString(TYPE_KEY, null);
            type = typeVal == null ? null : Type.valueOf(typeVal);
        } catch (JSONException e) {
            name = stringFilter.toString();
        }
    }

    @Override
    public boolean isEmpty() {
        return (name == null || name.isEmpty()) && type == null;
    }
}
