package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Transformer {

    public static final List<String> listLang = List.of("fr_FR");

    public static <T> T tryTransform(JsonObject bloc, Function<JsonObject, T> fcntTransform) throws RuntimeException {
        try {
            return fcntTransform.apply(bloc);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de transformer " + bloc.toString(), e);
        }
    }

    public static <T> T find(List<T> l, Predicate<T> p) {
        return l.stream().filter(p).findAny().orElse(null);
    }


}


