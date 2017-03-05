package com.pokemongostats.model.filtersinfos;

import android.util.Log;

import com.pokemongostats.model.bean.Type;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zapagon on 05/03/2017.
 */
public class PokemonDescFilterInfo implements MultipleFilterInfo {

    private static final String NAME_KEY = "NAME_KEY";
    private static final String TYPE_1_KEY = "TYPE_1_KEY";
    private static final String TYPE_2_KEY = "TYPE_2_KEY";
    private static final String MIN_CP_KEY = "MIN_CP_KEY";
    private static final String MIN_ATT_KEY = "MIN_ATT_KEY";
    private static final String MIN_DEF_KEY = "MIN_DEF_KEY";
    private static final String MIN_STA_KEY = "MIN_STA_KEY";

    private static final double NULL_DOUBLE = -1;

    private String name;
    private Type type1, type2;
    private double minCP, minAtt, minDef, minSta;

    public PokemonDescFilterInfo(){
        this.reset();
    }

    @Override
    public void reset(){
        name = null;
        type1 = type2 = null;
        minCP = minAtt = minDef = minSta = NULL_DOUBLE;
    }

    public Type getType1() {
        return type1;
    }

    public void setType1(Type type1) {
        this.type1 = type1;
    }

    public Type getType2() {
        return type2;
    }

    public void setType2(Type type2) {
        this.type2 = type2;
    }

    public double getMinCP() {
        return minCP;
    }

    public void setMinCP(double minCP) {
        this.minCP = minCP;
    }

    public double getMinAtt() {
        return minAtt;
    }

    public void setMinAtt(double minAtt) {
        this.minAtt = minAtt;
    }

    public double getMinDef() {
        return minDef;
    }

    public void setMinDef(double minDef) {
        this.minDef = minDef;
    }

    public double getMinSta() {
        return minSta;
    }

    public void setMinSta(double minSta) {
        this.minSta = minSta;
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
            jsonObject.put(TYPE_1_KEY, type1 == null ? null : type1.name());
            jsonObject.put(TYPE_2_KEY, type2 == null ? null : type2.name());
            jsonObject.put(MIN_CP_KEY, minCP);
            jsonObject.put(MIN_ATT_KEY, minAtt);
            jsonObject.put(MIN_DEF_KEY, minDef);
            jsonObject.put(MIN_STA_KEY, minSta);
        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
        }
        return jsonObject.toString();
    }

    @Override
    public void updateFromStringFilter(final CharSequence stringFilter) {
        try {
            JSONObject jsonObject = new JSONObject(stringFilter.toString());
            name = jsonObject.optString(NAME_KEY, null);
            String type1Val = jsonObject.optString(TYPE_1_KEY, null);
            type1 = type1Val==null ? null : Type.valueOf(type1Val);
            String type2Val = jsonObject.optString(TYPE_2_KEY, null);
            type2 = type2Val==null ? null : Type.valueOf(type2Val);
            minCP = jsonObject.optDouble(MIN_CP_KEY, NULL_DOUBLE);
            minAtt = jsonObject.optDouble(MIN_ATT_KEY, NULL_DOUBLE);
            minDef = jsonObject.optDouble(MIN_DEF_KEY, NULL_DOUBLE);
            minSta = jsonObject.optDouble(MIN_STA_KEY, NULL_DOUBLE);
        } catch (JSONException e) {
            name = stringFilter.toString();
        }
    }

    @Override
    public boolean isEmpty() {
        return (name == null || name.isEmpty()) && type1 == null && type2 == null && minCP == NULL_DOUBLE && minAtt == NULL_DOUBLE && minDef == NULL_DOUBLE && minSta == NULL_DOUBLE;
    }
}
