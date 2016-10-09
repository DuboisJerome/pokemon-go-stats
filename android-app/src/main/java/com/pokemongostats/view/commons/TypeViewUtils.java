/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class TypeViewUtils {
	public static View createTypeView(final Context context, final Type type) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.type_pokemon_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.type);
		toTypeView(tv, context, type);
		return view;
	}

	public static TextView toTypeView(TextView v, final Context context,
			final Type type) {
		v.setText(TypeViewUtils.getNameId(type));
		((GradientDrawable) v.getBackground()).setColor(context.getResources()
				.getColor(TypeViewUtils.getColorId(type)));
		return v;
	}

	public static int getColorId(final Type type) {
		switch (type) {
			case BUG :
				return R.color.bug_bg;
			case DARK :
				return R.color.dark_bg;
			case DRAGON :
				return R.color.dragon_bg;
			case ELECTRIC :
				return R.color.electric_bg;
			case FAIRY :
				return R.color.fairy_bg;
			case FIGHTING :
				return R.color.fighting_bg;
			case FIRE :
				return R.color.fire_bg;
			case FLYING :
				return R.color.flying_bg;
			case GHOST :
				return R.color.ghost_bg;
			case GRASS :
				return R.color.grass_bg;
			case GROUND :
				return R.color.ground_bg;
			case ICE :
				return R.color.ice_bg;
			case POISON :
				return R.color.poison_bg;
			case PSYCHIC :
				return R.color.psychic_bg;
			case ROCK :
				return R.color.rock_bg;
			case STEEL :
				return R.color.steel_bg;
			case WATER :
				return R.color.water_bg;
			case NORMAL :
			default :
				return R.color.normal_bg;
		}
	}

	public static int getNameId(final Type type) {
		switch (type) {
			case BUG :
				return R.string.bug;
			case DARK :
				return R.string.dark;
			case DRAGON :
				return R.string.dragon;
			case ELECTRIC :
				return R.string.electric;
			case FAIRY :
				return R.string.fairy;
			case FIGHTING :
				return R.string.fighting;
			case FIRE :
				return R.string.fire;
			case FLYING :
				return R.string.flying;
			case GHOST :
				return R.string.ghost;
			case GRASS :
				return R.string.grass;
			case GROUND :
				return R.string.ground;
			case ICE :
				return R.string.ice;
			case POISON :
				return R.string.poison;
			case PSYCHIC :
				return R.string.psychic;
			case ROCK :
				return R.string.rock;
			case STEEL :
				return R.string.steel;
			case WATER :
				return R.string.water;
			case NORMAL :
			default :
				return R.string.normal;
		}
	}
}
