package com.pokemongostats.view.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.core.content.res.ResourcesCompat;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

public class TextViewTypeBackground extends androidx.appcompat.widget.AppCompatTextView {

	protected Type type;

	public TextViewTypeBackground(Context context) {
		super(context);
		initializeViews(context);
	}

	public TextViewTypeBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context);
	}

	public TextViewTypeBackground(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context);
	}

	private void initializeViews(Context context) {

		setGravity(Gravity.CENTER);
		setBackgroundDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.type_bg, context.getTheme()));
		setTextAppearance(context, R.style.TypeTextViewStyle);
		setTextColor(ResourcesCompat.getColorStateList(context.getResources(), R.color.type_bg, context.getTheme()));
	}

	public void setType(Type t) {
		this.type = t;
		refreshIfVisible();
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		refreshIfVisible();
	}

	protected void refreshIfVisible() {
		if (getVisibility() == VISIBLE) refreshDrawableState();
	}

	// Constructors, view loading etc...
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		int state = stateForType(this.getType());
		if (state != -1) {
			mergeDrawableStates(drawableState, new int[]{state});
		}
		return drawableState;
	}

	public static int stateForType(Type t) {
		int r = -1;
		if (t == null) return r;

		switch (t) {
			case BUG:
				r = R.attr.state_bug;
				break;
			case ICE:
				r = R.attr.state_ice;
				break;
			case DARK:
				r = R.attr.state_dark;
				break;
			case FIRE:
				r = R.attr.state_fire;
				break;
			case ROCK:
				r = R.attr.state_rock;
				break;
			case FAIRY:
				r = R.attr.state_fairy;
				break;
			case GHOST:
				r = R.attr.state_ghost;
				break;
			case GRASS:
				r = R.attr.state_grass;
				break;
			case STEEL:
				r = R.attr.state_steel;
				break;
			case WATER:
				r = R.attr.state_water;
				break;
			case DRAGON:
				r = R.attr.state_dragon;
				break;
			case FLYING:
				r = R.attr.state_flying;
				break;
			case GROUND:
				r = R.attr.state_ground;
				break;
			case NORMAL:
				r = R.attr.state_normal;
				break;
			case POISON:
				r = R.attr.state_poison;
				break;
			case PSYCHIC:
				r = R.attr.state_psychic;
				break;
			case ELECTRIC:
				r = R.attr.state_electric;
				break;
			case FIGHTING:
				r = R.attr.state_fighting;
				break;
		}
		return r;
	}
}