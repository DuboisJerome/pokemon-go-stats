package fr.pokemon.parser.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Getter
public class Splitter {

    public static String GRP_MOVE_PVP = "MOVE_PVP";
    public static String GRP_MOVE_PVE = "MOVE_PVE";
    public static String GRP_MOVE_FORMS = "FORMS";
    public static String GRP_MOVE_PKMN = "PKMN";

    private static final String UNUSED = "UNUSED";

    private final List<Grp> listGrp = new ArrayList<>();

    @Getter
    @Setter
    public static class Grp {
        String name;
        Predicate<JsonObject> funcFilter;
        final List<JsonObject> lst = new ArrayList<>();
    }

    private void addGrp(String name, Predicate<JsonObject> funcFilter) {
        var grp = new Grp();
        grp.name = name;
        grp.funcFilter = funcFilter;
        listGrp.add(grp);
    }

    private Predicate<JsonObject> funcTemplateIdStartWith(String prefix) {
        return bloc -> bloc.get("templateId").getAsString().startsWith(prefix);
    }

    private Predicate<JsonObject> funcUnused() {
        return bloc ->
                listGrp.stream()
                        .filter(grp -> !grp.name.equals(UNUSED))
                        .noneMatch(grp -> grp.funcFilter.test(bloc));
    }

    private void initGrps() {
        // Move
        addGrp(GRP_MOVE_PVP, funcTemplateIdStartWith("COMBAT_V"));
        var regexMovePve = "^V\\d{4}_MOVE_.*";
        Pattern patternMovePve = Pattern.compile(regexMovePve);
        addGrp(GRP_MOVE_PVE, bloc -> {
            String txt = bloc.get("templateId").getAsString();
            return patternMovePve.matcher(txt).matches();
        });
        // List forms
        addGrp(GRP_MOVE_FORMS, funcTemplateIdStartWith("FORMS_V"));
        // Pkmn + Pkmn move + evolutionBranch + candy + buddy
        var regexPkmn = "^V\\d{4}_POKEMON_.*";
        Pattern patternPkmn = Pattern.compile(regexPkmn);
        addGrp(GRP_MOVE_PKMN, bloc -> {
            String txt = bloc.get("templateId").getAsString();
            return patternPkmn.matcher(txt).matches();
        });
        // Not in any other grp
        //addGrp(UNUSED, funcUnused());
        //"AVATAR","BADGE","CHARACTER","camera","sequence","COMBAT_LEAGUE","SPAWN_V",
    }

    private boolean tryAdd(JsonObject bloc, Grp grp) {
        boolean test = grp.funcFilter.test(bloc);
        if (test) {
            if (!grp.name.equals(UNUSED)) {
                //Logger.info(grp.name, " | ", bloc.toString());
            }
            try {
                JsonElement data = bloc.get("data");
                if (data.isJsonObject()) {
                    JsonObject blocJson = data.getAsJsonObject();
                    grp.lst.add(blocJson);
                } else {
                    Logger.info("Data n'est pas un obj json");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return test;
    }

    public void split(File inputFileJson) {
        Logger.info("Start");
        Logger.info("1. Adding groups");
        initGrps();
        // read GAME_MASTER
        Logger.info("2. Parsing ", inputFileJson.getName());
        if (inputFileJson.exists()) {
            readJsonStream(inputFileJson);
        } else {
            Logger.info(inputFileJson.getAbsolutePath() + " n'existe pas");
        }
        Logger.info("End");
    }

    private void readJsonStream(File f) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath()), StandardCharsets.UTF_8))) {
            reader.beginArray();
            Gson gson = new Gson();
            while (reader.hasNext()) {
                JsonObject bloc = gson.fromJson(reader, JsonObject.class);
                listGrp.forEach((grp) -> tryAdd(bloc, grp));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}