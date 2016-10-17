DROP TABLE IF EXISTS trainer;
DROP TABLE IF EXISTS pokemon;
DROP TABLE IF EXISTS evolution;
DROP TABLE IF EXISTS pokedex_i18n;
DROP TABLE IF EXISTS pokedex;
DROP TABLE IF EXISTS gym;
DROP TABLE IF EXISTS gym_description;
DROP TABLE IF EXISTS move;
DROP TABLE IF EXISTS move_i18n;
DROP TABLE IF EXISTS pokedex_move;

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

 CREATE TABLE move (
	 id INTEGER PRIMARY KEY,
	 move_type TEXT NOT NULL,
	 type TEXT NOT NULL,
	 power INTEGER NOT NULL,
	 stamina_loss_scalar REAL NOT NULL,
	 duration REAL NOT NULL,
	 energy_delta INTEGER NOT NULL,
	 critical_chance REAL
 );

 CREATE TABLE move_i18n (
	 id INTEGER NOT NULL,
	 name TEXT NOT NULL,
	 lang TEXT NOT NULL,
	 PRIMARY KEY(id, lang),
	 FOREIGN KEY(id) REFERENCES move(id)
 );
 
CREATE TABLE pokedex_move (
 move_id INTEGER NOT NULL,
 pokedex_num INTEGER NOT NULL,
 FOREIGN KEY(pokedex_num) REFERENCES pokedex(pokedex_num),
 FOREIGN KEY(move_id) REFERENCES move(id),
 PRIMARY KEY(move_id, pokedex_num)
);

