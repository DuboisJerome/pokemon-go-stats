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

function toTextDB(text){
  return "\'"+ text +"\'";
}

var fs = require("fs");
const encoding = "utf8";
const sql_filename = "insert.sql";
// Get content from file
var contents = fs.readFileSync("GAME_DATA_POKEMON.json", encoding);
var pokemons = eval(contents);
var base_insert = "INSERT INTO pokedex (pokedex_num,type1,type2,evolution_id) VALUES ({0},{1},{2},{3});"
var base_insert_i18n = "INSERT INTO pokedex_i18n (pokedex_num,lang,name) VALUES ({0},{1},{2});"
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
