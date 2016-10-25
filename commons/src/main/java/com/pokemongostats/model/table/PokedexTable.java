/**
 * 
 */
package com.pokemongostats.model.table;

/**
 * @author Zapagon
 *
 */
public class PokedexTable extends AbstractTable {

	// table name
	public static final String TABLE_NAME = "pokedex";

	// columns names
	public static final String POKEDEX_NUM = "pokedex_num";
	public static final String TYPE1 = "type1";
	public static final String TYPE2 = "type2";
	public static final String KMS_PER_CANDY = "kms_per_candy";
	public static final String KMS_PER_EGG = "kms_per_egg";
	public static final String BASE_ATTACK = "base_attack";
	public static final String BASE_DEFENSE = "base_defense";
	public static final String BASE_STAMINA = "base_stamina";
	public static final String CANDY_TO_EVOLVE = "candy_to_evolve";
	public static final String MAX_CP = "max_cp";

	// i18n
	public static final String TABLE_NAME_I18N = TABLE_NAME + "_i18n";
	public static final String NAME = "name";
	public static final String FAMILY = "family";
	public static final String DESCRIPTION = "description";
}
