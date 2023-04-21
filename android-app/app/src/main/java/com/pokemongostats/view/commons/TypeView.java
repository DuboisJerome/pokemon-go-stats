package com.pokemongostats.view.commons;

import android.content.Context;
import android.util.AttributeSet;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

public class TypeView extends TextViewTypeBackground {

	private boolean showEvenIfEmpty = false;

	public TypeView(Context context) {
		super(context);
	}

	public TypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setType(Type t) {
		this.setText(getNameId(t));
		super.setType(t);
	}

	public void setShowEvenIfEmpty(boolean b) {
		this.showEvenIfEmpty = b;
		refreshIfVisible();
	}

	@Override
	protected void refreshIfVisible() {
		boolean isVisible = this.showEvenIfEmpty || this.type != null;
		setVisibility(isVisible ? VISIBLE : INVISIBLE);
		if (isVisible) refreshDrawableState();
	}

	public static int getNameId(Type type) {
		if (type == null) {
			return R.string.none;
		}
		switch (type) {
			case BUG:
				return R.string.bug;
			case DARK:
				return R.string.dark;
			case DRAGON:
				return R.string.dragon;
			case ELECTRIC:
				return R.string.electric;
			case FAIRY:
				return R.string.fairy;
			case FIGHTING:
				return R.string.fighting;
			case FIRE:
				return R.string.fire;
			case FLYING:
				return R.string.flying;
			case GHOST:
				return R.string.ghost;
			case GRASS:
				return R.string.grass;
			case GROUND:
				return R.string.ground;
			case ICE:
				return R.string.ice;
			case POISON:
				return R.string.poison;
			case PSYCHIC:
				return R.string.psychic;
			case ROCK:
				return R.string.rock;
			case STEEL:
				return R.string.steel;
			case WATER:
				return R.string.water;
			case NORMAL:
				return R.string.normal;
			default:
				return R.string.none;
		}
	}

}