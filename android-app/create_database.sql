DROP TABLE IF EXISTS trainer;
DROP TABLE IF EXISTS pokemon;
DROP TABLE IF EXISTS evolution;
DROP TABLE IF EXISTS pokedex_i18n;
DROP TABLE IF EXISTS pokedex;
DROP TABLE IF EXISTS gym;
DROP TABLE IF EXISTS gym_description;

CREATE TABLE gym_description (
 _id INTEGER PRIMARY KEY AUTOINCREMENT,
 name TEXT NOT NULL UNIQUE,
 latitude REAL,
 longitude REAL
);

CREATE TABLE gym (
 _id INTEGER PRIMARY KEY AUTOINCREMENT,
 gym_description_id INTEGER NOT NULL,
 level INTEGER,
 date_creation TEXT NOT NULL,
 team TEXT,
 pokemon_ids TEXT,
 FOREIGN KEY(gym_description_id) REFERENCES gym_description(_id)
);

CREATE TABLE pokedex (
 pokedex_num INTEGER PRIMARY KEY,
 type1 TEXT NOT NULL,
 type2 TEXT
);

CREATE TABLE evolution (
 evolution_id INTEGER PRIMARY KEY,
 pokedex_num INTEGER,
 FOREIGN KEY(pokedex_num) REFERENCES pokedex(pokedex_num),
 FOREIGN KEY(evolution_id) REFERENCES pokedex(pokedex_num)
);

CREATE TABLE pokedex_i18n (
 pokedex_num INTEGER NOT NULL,
 lang TEXT NOT NULL,
 name TEXT NOT NULL,
 family TEXT,
 description TEXT,
 PRIMARY KEY(pokedex_num,lang),
 FOREIGN KEY(pokedex_num) REFERENCES pokedex(pokedex_num)
);

CREATE TABLE pokemon (
 _id INTEGER PRIMARY KEY AUTOINCREMENT,
 pokedex_num INTEGER NOT NULL,
 cp INTEGER,
 hp INTEGER,
 defense_iv INTEGER,
 attack_iv INTEGER,
 stamina_iv INTEGER,
 level REAL,
 owner_id INTEGER,
 nickname TEXT,
 FOREIGN KEY(pokedex_num) REFERENCES pokedex(pokedex_num),
 FOREIGN KEY(owner_id) REFERENCES trainer(_id)
);

CREATE TABLE trainer (
 _id INTEGER PRIMARY KEY AUTOINCREMENT,
 name TEXT UNIQUE,
 level INTEGER,
 team TEXT
);

