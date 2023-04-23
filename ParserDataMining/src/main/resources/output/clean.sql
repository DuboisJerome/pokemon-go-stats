--------------------------------------------------
-- Suppression des formes d'évenements ou qui impact que l'esthètique

-- select  p._id, p.form, l.name, p.type1, p.type2 from pokemon p
--   join pokemon_i18n l on p._id = l._id and p.form = l.form
--   where l.lang = 'fr_FR' and p._id in (
--  	select p2._id from pokemon p2
--  	where form not like 'MEGA%'
--  	and form != 'ALOLA' and form != 'HISUIAN' and form != 'GALARIAN'
--  	group by p2._id having count(*) > 1)
--   order by p._id;

-- pikachu
delete from pokemon where _id in (25) and form != 'NORMAL';
delete from pokemon_i18n where _id in (25) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (25) and form != 'NORMAL';
-- Cheniti on en garde qu'un (! il evolue en cheniselle 413 ou papilord 414)
INSERT OR IGNORE INTO pokemon_i18n (_id,form,lang,name) VALUES (412,'NORMAL','fr_FR','Cheniti');
update pokemon set form = 'NORMAL' where _id in (412) and form = 'PLANT';
update pokemon_move set form = 'NORMAL' where pokemon_id in (412) and form = 'PLANT';
delete from pokemon where _id in (412) and form != 'NORMAL';
delete from pokemon_i18n where _id in (412) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (412) and form != 'NORMAL';
update evolution set base_pkmn_form = 'NORMAL' where base_pkmn_id in (412);

-- ! Ne pas faire génésect ses attaques changent selon son type caché (le vrai type ne change pas)

-- Vivaldaim(+evol) on en garde qu'un
INSERT OR IGNORE INTO pokemon_i18n (_id,form,lang,name) VALUES (585,'NORMAL','fr_FR','Vivaldaim');
INSERT OR IGNORE INTO pokemon_i18n (_id,form,lang,name) VALUES (586,'NORMAL','fr_FR','Haydaim');
update pokemon set form = 'NORMAL' where _id in (585, 586) and form = 'SPRING';
update pokemon_move set form = 'NORMAL' where pokemon_id in (585, 586) and form = 'SPRING';
update evolution set base_pkmn_form = 'NORMAL' where base_pkmn_id in (585, 586) and base_pkmn_form = 'SPRING';
update evolution set evolution_form = 'NORMAL' where evolution_id in (585, 586) and evolution_form = 'SPRING';
delete from pokemon where _id in (585, 586) and form != 'NORMAL';
delete from pokemon_i18n where _id in (585, 586) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (585, 586) and form != 'NORMAL';
delete from evolution where (base_pkmn_id in (585, 586) and base_pkmn_form != 'NORMAL') or (evolution_id in (585, 586) and evolution_form != 'NORMAL') ;
-- Lépidonille(+evol) +
delete from pokemon where _id in (664, 665, 666) and form != 'NORMAL';
delete from pokemon_i18n where _id in (664, 665, 666) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (664, 665, 666) and form != 'NORMAL';
delete from evolution where (base_pkmn_id in (664, 665, 666) and base_pkmn_form != 'NORMAL') or (evolution_id in (664, 665, 666) and evolution_form != 'NORMAL') ;
-- Flabébé(+evol)
delete from pokemon where _id in (669, 670, 671) and form != 'NORMAL';
delete from pokemon_i18n where _id in (669, 670, 671) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (669, 670, 671) and form != 'NORMAL';
delete from evolution where (base_pkmn_id in (669, 670, 671) and base_pkmn_form != 'NORMAL') or (evolution_id in (669, 670, 671) and evolution_form != 'NORMAL') ;
-- Coafarel
delete from pokemon where _id in (676) and form != 'NORMAL';
delete from pokemon_i18n where _id in (676) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (676) and form != 'NORMAL';
-- Pitrouille(+evol) le seul cas où le NORMAL n'existe pas. Les stats changent selon la taille petit, moyen, super, large
delete from pokemon where _id in (710,711) and form = 'NORMAL';
delete from pokemon_i18n where _id in (710,711) and form = 'NORMAL';
delete from pokemon_move where pokemon_id in (710,711) and form = 'NORMAL';

-- !  Ne pas faire lougaroc ses attaques changent selon la forme

-- Météno
INSERT OR IGNORE INTO pokemon_i18n (_id,form,lang,name) VALUES (774,'NORMAL','fr_FR','Météno');
update pokemon set form = 'NORMAL' where _id in (774) and form = 'BLUE';
update pokemon_move set form = 'NORMAL' where pokemon_id in (774) and form = 'BLUE';
delete from pokemon where _id in (774) and form != 'NORMAL';
delete from pokemon_i18n where _id in (774) and form != 'NORMAL';
delete from pokemon_move where pokemon_id in (774) and form != 'NORMAL';

-- Suppression des formes obscurs (légendaire)
delete from pokemon where form = 'S';
delete from pokemon_i18n where form = 'S';
delete from pokemon_move where form = 'S';