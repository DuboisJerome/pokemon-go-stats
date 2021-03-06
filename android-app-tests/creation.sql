CREATE TABLE IF NOT EXISTS trainer (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, level INTEGER, team TEXT);
CREATE TABLE IF NOT EXISTS pokedex (pokedex_num INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, types TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS pokemon (_id INTEGER PRIMARY KEY AUTOINCREMENT, pokedex_num INTEGER NOT NULL, cp INTEGER, hp INTEGER, defense_iv INTEGER, attack_iv INTEGER, stamina_iv INTEGER, level REAL, owner_id INTEGER, nickname TEXT,  FOREIGN KEY (pokedex_num) REFERENCES pokedex(pokedex_num),  FOREIGN KEY (owner_id) REFERENCES trainer(_id));
CREATE TABLE IF NOT EXISTS gym_description (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, latitude REAL, longitude REAL);
CREATE TABLE IF NOT EXISTS gym (_id INTEGER PRIMARY KEY AUTOINCREMENT, gym_description_id INTEGER NOT NULL, level INTEGER, date TEXT NOT NULL, team TEXT, pokemon_ids TEXT,  FOREIGN KEY (gym_description_id) REFERENCES gym_description(_id));
