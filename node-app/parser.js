var fs = require("fs");
const encoding = "utf8";

function toTextDB(text){
  return "\'"+ text +"\'";
}

String.format = function(format) {
  var args = Array.prototype.slice.call(arguments, 1);
  return format.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
    ? args[number]
    : match
    ;
  });
};

function getTypeToDB(type) {
  return toTextDB(getType(type));
}

function getType(type) {
  switch (type) {
    case 1: return "NORMAL";
    case 2: return "FIRE";
    case 3: return "WATER";
    case 4: return "ELECTRIC";
    case 5: return "GRASS";
    case 6: return "ICE";
    case 7: return "FIGHTING";
    case 8: return "POISON";
    case 9: return "GROUND";
    case 10: return "FLYING";
    case 11: return "PSYCHIC";
    case 12: return "BUG";
    case 13: return "ROCK";
    case 14: return "GHOST";
    case 15: return "DRAGON";
    case 16: return "DARK";
    case 17: return "STEEL";
    case 18: return "FAIRY";
    default: return "null";
  }
}

function parsePkmn(){
  const sql_filename = "insert_pkmn_desc.sql";
  // Get content from file
  var contents = fs.readFileSync("GAME_DATA_POKEMON.json", encoding);
  var pokemons = eval(contents);
  var base_insert = "INSERT OR REPLACE INTO pokedex (pokedex_num,type1,type2,evolution_id) VALUES ({0},{1},{2},{3});"
  var base_insert_i18n = "INSERT OR REPLACE INTO pokedex_i18n (pokedex_num,lang,name) VALUES ({0},{1},{2});"
  fs.writeFileSync(sql_filename, "", encoding);
  // for each pokemons
  for (var i = 0; i < pokemons.length; i++) {
    var p = pokemons[i];
    var types = eval(p.Types);
    var type1 = getTypeToDB(types[0]);
    var type2 = types.length > 1 ? getTypeToDB(types[1]) : "null";
    var query = String.format(base_insert, p.ID, type1, type2, p.Evolution);
    var query_i18n_en = String.format(base_insert_i18n, p.ID, toTextDB("en-us"), toTextDB(p.Name));
    var query_i18n_fr = String.format(base_insert_i18n, p.ID, toTextDB("fr-fr"), toTextDB(p.Name));
    var query_i18n_de = String.format(base_insert_i18n, p.ID, toTextDB("de-de"), toTextDB(p.Name));
    fs.appendFileSync(sql_filename, query + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_fr + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_en + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_de + "\n", encoding);
  }
}

function parseMove(){
  const sql_filename = "insert_move.sql";
  // Get content from file
  var contents = fs.readFileSync("GAME_DATA_MOVES.json", encoding);
  var pokemons = eval(contents);
  var base_insert = "INSERT OR REPLACE INTO moves (id, move_type, type, power, stamina_loss_scalar, duration, energy_delta, criticalChance)"
  base_insert += " VALUES ({0}, {1}, {2}, {3}, {4}, {5}, {6}, {7});"
  var base_insert_i18n = "INSERT OR REPLACE INTO move_i18n (id,lang,name) VALUES ({0},{1},{2});"
  fs.writeFileSync(sql_filename, "", encoding);
  // for each pokemons
  for (var i = 0; i < pokemons.length; i++) {
    var p = pokemons[i];
    var type = getTypeToDB(p["Type"]);
    var query = String.format(base_insert, p["ID"], toTextDB(p["Move Type"].toUpperCase()), type, p["Power"], p["Stamina Loss Scalar"], p["Duration (ms)"], p["Energy Delta"], p["Critical Chance"]);
    var query_i18n_en = String.format(base_insert_i18n, p["ID"], toTextDB("en-us"), toTextDB(p["Name"]));
    var query_i18n_fr = String.format(base_insert_i18n, p["ID"], toTextDB("fr-fr"), toTextDB(p["Name"]));
    var query_i18n_de = String.format(base_insert_i18n, p["ID"], toTextDB("de-de"), toTextDB(p["Name"]));
    fs.appendFileSync(sql_filename, query + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_fr + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_en + "\n", encoding);
    fs.appendFileSync(sql_filename, query_i18n_de + "\n", encoding);
  }
}

function parsePkmnMove(){
  const sql_filename = "insert_pkmn_move.sql";
  // Get content from file
  var contents = fs.readFileSync("GAME_DATA_POKEMON.json", encoding);
  var pokemons = eval(contents);
  var base_insert = "INSERT OR REPLACE INTO pokedex_move (pokedex_num, move_id) VALUES ({0},{1});"
  fs.writeFileSync(sql_filename, "", encoding);
  // for each pokemons
  for (var i = 0; i < pokemons.length; i++) {
    var p = pokemons[i];
    var quick_moves = eval(p["Quick Moves"]);
    for(var q = 0; q < quick_moves.length; ++q){
      var query = String.format(base_insert, p["ID"], quick_moves[q]);
      fs.appendFileSync(sql_filename, query + "\n", encoding);
    }
    var charge_moves = eval(p["Cinematic Moves"]);
    for(var c = 0; c < charge_moves.length; ++c){
        var query = String.format(base_insert, p["ID"], charge_moves[c]);
        fs.appendFileSync(sql_filename, query + "\n", encoding);
    }
  }
}

parsePkmnMove();
