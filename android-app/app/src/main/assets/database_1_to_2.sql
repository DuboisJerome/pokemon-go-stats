UPDATE move_i18n SET lang='fr_FR' WHERE lang='fr-fr';
UPDATE move_i18n SET lang='en_US' WHERE lang='en-us';
UPDATE move_i18n SET lang='de_DE' WHERE lang='de-de';
UPDATE pokedex_i18n SET lang='fr_FR' WHERE lang='fr-fr';
UPDATE pokedex_i18n SET lang='en_US' WHERE lang='en-us';
UPDATE pokedex_i18n SET lang='de_DE' WHERE lang='de-de';
-- delete move not assigned to any pokemon. e.g: 'Hydro pump blastoise'
DELETE FROM move WHERE (move.id IN (SELECT move.id FROM move WHERE move.id NOT IN (SELECT pokedex_move.move_id FROM pokedex_move)));
-- La trad de 'Counter' est 'Riposte' et non 'Contre'
UPDATE move_i18n SET name='Riposte' WHERE (lang='fr_FR' AND id=243);
