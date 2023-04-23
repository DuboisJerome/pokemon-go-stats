package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.pokemon.parser.model.Type;

public class TransformerType {

    public static Type toType(JsonObject obj, String field) {
        JsonElement elem = obj.get(field);
        if (elem == null) return null;
        String str = elem.getAsString();
        if (str == null) return null;
        return toType(str);
    }

    private static Type toType(JsonElement elem) {
        if (elem == null) return null;
        return toType(elem.getAsString());
    }

    private static Type toType(String type) {
        if (type == null || type.isEmpty()) return null;
        return Type.valueOf(type.substring("POKEMON_TYPE_".length()));
    }
}
