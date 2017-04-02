-- Request to get all moves without pokemons
SELECT move_i18n.id,move_i18n.name FROM move_i18n WHERE (move_i18n.lang = 'fr_FR' AND move_i18n.id IN (SELECT move.id FROM move WHERE move.id NOT IN (SELECT pokedex_move.move_id FROM pokedex_move)));
-- Request to get all pokemon info + real name
SELECT pokedex_i18n.name, pokedex.* from pokedex JOIN pokedex_i18n ON pokedex.pokedex_num = pokedex_i18n.pokedex_num WHERE pokedex_i18n.lang = 'fr-fr';