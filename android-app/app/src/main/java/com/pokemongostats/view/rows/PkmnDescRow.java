package com.pokemongostats.view.rows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.utils.PkmnDrawableCache;

import fr.commons.generique.controller.utils.TagUtils;

/**
 * @author Zapagon
 */
public class PkmnDescRow extends LinearLayout
		implements
		ItemView<PkmnDesc> {

	private TextView nameView;
	private ImageView imgView;
	private TypeRow type1View;
	private TypeRow type2View;
	private TextView baseAttackView;
	private TextView baseDefenseView;
	private TextView baseStaminaView;
	private TextView maxCPView;
	private PkmnDesc pkmnDesc;

	public PkmnDescRow(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public PkmnDescRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context, attrs);
	}

	public PkmnDescRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {

		inflate(context, R.layout.view_row_pkmn_desc, this);
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		this.nameView = findViewById(R.id.pkmn_name);
		this.imgView = findViewById(R.id.pkmn_img);
		this.type1View = findViewById(R.id.pkmn_type_1);
		this.type2View = findViewById(R.id.pkmn_type_2);
		this.baseAttackView = findViewById(R.id.pkmn_base_attack);
		this.baseDefenseView = findViewById(R.id.pkmn_base_defense);
		this.baseStaminaView = findViewById(R.id.pkmn_base_stamina);
		this.maxCPView = findViewById(R.id.pkmn_desc_max_cp);
	}

	/**
	 * @param isBaseAttVisible the isBaseAttVisible to set
	 */
	public void setBaseAttVisible(boolean isBaseAttVisible) {
		this.baseAttackView.setVisibility(isBaseAttVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isBaseDefVisible the isBaseDefVisible to set
	 */
	public void setBaseDefVisible(boolean isBaseDefVisible) {
		this.baseDefenseView.setVisibility(isBaseDefVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isBaseStaminaVisible the isBaseStaminaVisible to set
	 */
	public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
		this.baseStaminaView.setVisibility(isBaseStaminaVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isMaxCPVisible the isMaxCPVisible to set
	 */
	public void setMaxCPVisible(boolean isMaxCPVisible) {
		this.maxCPView.setVisibility(isMaxCPVisible ? VISIBLE : GONE);
	}

	/**
	 * @return the pkmnDesc
	 */
	public PkmnDesc getPkmnDesc() {
		return this.pkmnDesc;
	}

	// Save/Restore State

	/**
	 * @param p the pkmnDesc to set
	 */
	public void setPkmnDesc(PkmnDesc p) {
		this.pkmnDesc = p;
	}

	private String toNoZeroRoundIntString(Double d) {
		return (d != null && d > 0)
				? String.valueOf(d.intValue())
				: getContext().getString(R.string.unknown);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		PkmnDescRowSavedState savedState = new PkmnDescRowSavedState(
				superState);
		// end
		savedState.pkmnDesc = this.pkmnDesc;
		Log.d(TagUtils.DEBUG, "onSaveInstanceState " + this.pkmnDesc);

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof PkmnDescRowSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		PkmnDescRowSavedState savedState = (PkmnDescRowSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.pkmnDesc = savedState.pkmnDesc;
		Log.d(TagUtils.DEBUG, "onRestoreInstanceState " + this.pkmnDesc);
	}

	@Override
	public void update() {
		if (this.pkmnDesc == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			String name = "# " + this.pkmnDesc.getPokedexNum() + System.getProperty("line.separator") + this.pkmnDesc.getName();
			this.nameView.setText(name);
			PkmnDrawableCache.getAsync(getContext(), this.pkmnDesc, d -> this.imgView.setImageDrawable(d));

			Type newType1 = this.pkmnDesc.getType1();
			if (newType1 != null) {
				this.type1View.updateWith(newType1);
			} else {
				Log.e(TagUtils.DEBUG, "Type 1 null pour " + this.pkmnDesc);
				this.type1View.setVisibility(INVISIBLE);
			}

			Type newType2 = this.pkmnDesc.getType2();
			if (newType2 == null) {
				this.type2View.setVisibility(View.INVISIBLE);
			} else {
				this.type2View.setVisibility(View.VISIBLE);
				this.type2View.updateWith(this.pkmnDesc.getType2());
			}
			// base att
			this.baseAttackView
					.setText(toNoZeroRoundIntString(this.pkmnDesc.getBaseAttack()));
			// base def
			this.baseDefenseView
					.setText(toNoZeroRoundIntString(this.pkmnDesc.getBaseDefense()));
			// base stamina
			this.baseStaminaView
					.setText(toNoZeroRoundIntString(this.pkmnDesc.getBaseStamina()));
			// max cp
			this.maxCPView.setText(toNoZeroRoundIntString(this.pkmnDesc.getMaxCP()));
		}
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void updateWith(PkmnDesc p) {
		setPkmnDesc(p);
		update();
	}

	private static class PkmnDescRowSavedState extends BaseSavedState {

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<PkmnDescRowSavedState> CREATOR = new Parcelable.Creator<PkmnDescRowSavedState>() {
			@Override
			public PkmnDescRowSavedState createFromParcel(Parcel in) {
				return new PkmnDescRowSavedState(in);
			}

			@Override
			public PkmnDescRowSavedState[] newArray(int size) {
				return new PkmnDescRowSavedState[size];
			}
		};
		private PkmnDesc pkmnDesc;

		PkmnDescRowSavedState(Parcelable superState) {
			super(superState);
		}

		private PkmnDescRowSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				this.pkmnDesc = in.readParcelable(
						PclbPkmnDesc.class.getClassLoader());
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (this.pkmnDesc != null ? 1 : 0));
			if (this.pkmnDesc != null) {
				out.writeParcelable(new PclbPkmnDesc(this.pkmnDesc), 0);
			}
		}
	}
}