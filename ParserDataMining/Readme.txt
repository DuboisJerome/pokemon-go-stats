Récupérer lastest.json depuis le git et le coller sur src/main/resources/input
https://github.com/PokeMiners/game_masters/blob/master/latest/latest.json

Lancer le Main depuis IntellJ et NON Android Studio
Celui ci va générer des fichiers SQL dans un répertoire
pour la version actuelle (date du jour).
Ce répertoire ce décompose en 2 sous répertoires
- all : Répertoire qui contient l'intégralité
    des scripts sql générés à partir du json en input
- diff : Répertoires qui contient des fichiers sql étant
    = 'currentVersion/all' - 'lastVersion/all'
    i.e seulement les requetes qui n'ont pas déjà été jouées

Les requetes doivents etre jouées de préférences dans l'ordre :
- pokemon.sql : Insertion ou mise à jour des stats des pokemons
- pokemon_18n.sql : Insert de la lang par défaut des pokemons, ne contient que des créations pas d'update pour ne pas perdre l'i18n manuel
- move.sql : Insert ou mise à jour des attaques
- move_i18n : Insert de la lang par défaut des attaques, ne contient que des créations pas d'update pour ne pas perdre l'i18n manuel
- move_std.sql : Assocation attaques/pokemons
- evolution.sql : Association entre un pokemon et ses evolutions
- move_evol_temp.sql : Association attaques/pokemons méga évolué => Les pokemons méga évolués ont les memes attaques que leur forme de base,
    - à voir si possible de fusionner avec le fichier move_std.sql