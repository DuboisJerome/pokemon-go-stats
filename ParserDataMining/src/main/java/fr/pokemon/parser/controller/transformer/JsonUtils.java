package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static int getAsInt(JsonObject obj, String field) {
        JsonElement elem = obj.get(field);
        return getAsInt(elem);
    }

    private static int getAsInt(JsonElement elem) {
        return elem == null ? -1 : elem.getAsInt();
    }

    public static double getAsDouble(JsonObject obj, String field) {
        JsonElement elem = obj.get(field);
        return getAsDouble(elem);
    }

    private static double getAsDouble(JsonElement elem) {
        return elem == null ? -1D : elem.getAsDouble();
    }
}
