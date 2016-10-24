package com.pokemongostats.view.rows;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TypeRowView extends FrameLayout {

	private Type type;

	private TextView typeTextView;

	public TypeRowView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public TypeRowView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public TypeRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		inflate(getContext(), R.layout.view_row_type, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		typeTextView = (TextView) findViewById(R.id.type_name);
		setVisibility(View.GONE);
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param t
	 *            the type to set
	 */
	public void setType(Type t) {
		type = t;
		if (t == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			typeTextView.setText(getNameId(t));
			((GradientDrawable) typeTextView.getBackground()).setColor(
					getContext().getResources().getColor(getColorId(t)));
		}
	}

	public static int getColorId(final Type type) {
		if (type == null) { return -1; }
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
				return R.color.normal_bg;
			default :
				return -1;
		}
	}

	public static int getNameId(final Type type) {
		if (type == null) { return -1; }
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
				return R.string.normal;
			default :
				return -1;
		}
	}

	public static TypeRowView create(Context context, Type t) {
		TypeRowView v = new TypeRowView(context);
		v.setType(t);
		return v;
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		TypeRowViewSavedState savedState = new TypeRowViewSavedState(
				superState);
		// end
		savedState.type = this.type;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof TypeRowViewSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		TypeRowViewSavedState savedState = (TypeRowViewSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.type = savedState.type;
	}

	protected static class TypeRowViewSavedState extends BaseSavedState {

		private Type type = null;

		TypeRowViewSavedState(Parcelable superState) {
			super(superState);
		}

		protected TypeRowViewSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				this.type = Type.valueOfIgnoreCase(in.readString());
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (type != null ? 1 : 0));
			if (type != null) {
				out.writeString(type.name());
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<TypeRowViewSavedState> CREATOR = new Parcelable.Creator<TypeRowViewSavedState>() {
			@Override
			public TypeRowViewSavedState createFromParcel(Parcel in) {
				return new TypeRowViewSavedState(in);
			}
			@Override
			public TypeRowViewSavedState[] newArray(int size) {
				return new TypeRowViewSavedState[size];
			}
		};
	}
}
