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
		if (t == null) return -1;
		return switch (t) {
			case BUG -> R.attr.state_bug;
			case ICE -> R.attr.state_ice;
			case DARK -> R.attr.state_dark;
			case FIRE -> R.attr.state_fire;
			case ROCK -> R.attr.state_rock;
			case FAIRY -> R.attr.state_fairy;
			case GHOST -> R.attr.state_ghost;
			case GRASS -> R.attr.state_grass;
			case STEEL -> R.attr.state_steel;
			case WATER -> R.attr.state_water;
			case DRAGON -> R.attr.state_dragon;
			case FLYING -> R.attr.state_flying;
			case GROUND -> R.attr.state_ground;
			case NORMAL -> R.attr.state_normal;
			case POISON -> R.attr.state_poison;
			case PSYCHIC -> R.attr.state_psychic;
			case ELECTRIC -> R.attr.state_electric;
			case FIGHTING -> R.attr.state_fighting;
		};
	}
}