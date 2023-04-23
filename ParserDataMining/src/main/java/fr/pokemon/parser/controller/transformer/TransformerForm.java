package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransformerForm {

    private static final String NORMAL_TEMP = "NORMAL_TEMP";

    public static String toForm(String pokemonId, JsonObject obj, String field) {
        JsonElement elem = obj.get(field);
        return toForm(pokemonId, elem);
    }

    private static String toForm(String pokemonId, JsonElement elem) {
        if (elem == null) {
            return NORMAL_TEMP;
        }
        return toForm(pokemonId, elem.getAsString());
    }

    private static String toForm(String pokemonId, String form) {
        if (form == null || form.isEmpty()) {
            return NORMAL_TEMP;
        }
        String res;
        var idx = form.indexOf(pokemonId);
        if (idx < 0) {
            idx = form.indexOf("_");
            if (idx < 0) {
                Logger.error("Forme inconnue _ *0 ", form);
                res = form;
            } else {
                res = form.substring(idx + 1);
                if (res.contains("_")) {
                    Logger.error("Forme inconnue _ *2 ", form);
                    res = form;
                }
            }
            Logger.info("Forme ne contient pas l'id : ", form, pokemonId, ". Forme déterminé : ", res);
        } else {
            res = form.substring(idx + pokemonId.length() + 1);
        }
        return res;
    }

    public static boolean isFormExclu(String form) {
        boolean isExclu = form == null || form.isEmpty() || "SHADOW".equals(form) || "S".equals(form) || "PURIFIED".equals(form) || form.contains("201") || form.contains("202");
        if (isExclu) {
            Logger.debug("form exclu", form);
        }
        return isExclu;
    }

    public static <T> List<T> cleanForms(List<T> lst, Function<T, String> funcId, Function<T, String> getterForm, BiConsumer<T, String> setterForm) {
        Logger.info("=== CleanForms");
        // Filtre les formes qui ne sont pas de type NORMAL_TEMP OU qui le sont mais qu'il n'y a aucun autre element de la liste pour le meme id
        Map<String, List<T>> mapElemById = lst.stream().collect(Collectors.groupingBy(funcId));
        List<T> lstAllConserver = new ArrayList<>();
        mapElemById.forEach((id, lstElemById) -> {
            // Supprime les formes exclues
            lstElemById.forEach(elem -> {
                var form = getterForm.apply(elem);
                if (isFormExclu(form)) {
                    return;
                }
                // La forme récupéré n'est pas NORMAL_TEMP, elle doit forcement passer le filtre
                if (!NORMAL_TEMP.equals(form)) {
                    lstAllConserver.add(elem);
                    return;
                }

                // La forme récupéré est NORMAL_TEMP, on va regarder les autres formes de la liste
                // S'il n'y en a aucune, on conserve la forme normal, sinon on l'exclu

                // Exclue de la liste l'objet courant en cours de lecture
                var lstFormsStream = lstElemById.stream().filter(e -> e != elem);
                // Dans le cas ou il y aurait plusieurs NORMAL_TEMP dans la liste de départ, on exclue les autres
                lstFormsStream = lstFormsStream.filter(e -> !NORMAL_TEMP.equals(getterForm.apply(e)));
                // S'il existe une forme MEGA il faut garder la forme NORMAL_TEMP
                lstFormsStream = lstFormsStream.filter(e -> !getterForm.apply(e).startsWith("MEGA") && !getterForm.apply(e).startsWith("PRIMAL"));

                boolean isDoitConserverNormalTemp = lstFormsStream.map(getterForm).distinct().findAny().isEmpty();
                if (isDoitConserverNormalTemp) {
                    lstAllConserver.add(elem);
                }
            });
        });
        return lstAllConserver.stream().peek(e -> {
            // Les NORMAL_TEMP n'ont pas d'autres formes
            var form = getterForm.apply(e);
            if (NORMAL_TEMP.equals(form)) {
                form = "NORMAL";
            }
            setterForm.accept(e, form);
        }).toList();
    }


}
