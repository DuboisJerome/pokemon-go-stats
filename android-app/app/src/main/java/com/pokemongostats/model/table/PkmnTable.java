/**
 *
 */
package com.pokemongostats.model.table;

/**
 * @author Zapagon
 *
 */
public class PkmnTable extends AbstractTable {

	// table name
	public static final String TABLE_NAME = "pokemon";

	// columns names
	public static final String FORM = "form";
	public static final String TYPE1 = "type1";
	public static final String TYPE2 = "type2";
	public static final String KMS_PER_CANDY = "kms_per_candy";
	public static final String KMS_PER_EGG = "kms_per_egg";
	@Deprecated
	public static final String PHYSICAL_ATTACK = "physical_attack";
	@Deprecated
	public static final String PHYSICAL_DEFENSE = "physical_defense";
	@Deprecated
	public static final String SPECIAL_ATTACK = "special_attack";
	@Deprecated
	public static final String SPECIAL_DEFENSE = "special_defense";
	@Deprecated
	public static final String PV = "pv";
	@Deprecated
	public static final String SPEED = "speed";
	public static final String STAMINA = "stamina";
	public static final String ATTACK = "attack";
	public static final String DEFENSE = "defense";
	public static final String TAGS = "tags";

	// i18n
	public static final String TABLE_NAME_I18N = TABLE_NAME + "_i18n";
	public static final String NAME = "name";
	public static final String FAMILY = "family";
	public static final String DESCRIPTION = "description";
}