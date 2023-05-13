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
		return switch (type) {
			case BUG -> R.string.bug;
			case DARK -> R.string.dark;
			case DRAGON -> R.string.dragon;
			case ELECTRIC -> R.string.electric;
			case FAIRY -> R.string.fairy;
			case FIGHTING -> R.string.fighting;
			case FIRE -> R.string.fire;
			case FLYING -> R.string.flying;
			case GHOST -> R.string.ghost;
			case GRASS -> R.string.grass;
			case GROUND -> R.string.ground;
			case ICE -> R.string.ice;
			case POISON -> R.string.poison;
			case PSYCHIC -> R.string.psychic;
			case ROCK -> R.string.rock;
			case STEEL -> R.string.steel;
			case WATER -> R.string.water;
			case NORMAL -> R.string.normal;
		};
	}

}