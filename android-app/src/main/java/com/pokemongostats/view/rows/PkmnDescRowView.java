/**
 * 
 */
package com.pokemongostats.view.rows;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class PkmnDescRowView extends LinearLayout implements ItemView<PokemonDescription> {

	private TextView nameView;
	private ImageView imgView;
	private TypeRowView type1View;
	private TypeRowView type2View;
	private TextView baseAttackView;
	private TextView baseDefenseView;
	private TextView baseStaminaView;
	private TextView maxCPView;

	private PokemonDescription pkmnDesc;

	public PkmnDescRowView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public PkmnDescRowView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public PkmnDescRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		if (attrs != null) {}

		inflate(getContext(), R.layout.view_row_pkmn_desc, this);
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		nameView = (TextView) findViewById(R.id.pkmn_desc_name);
		imgView = (ImageView) findViewById(R.id.pkmn_desc_img);
		type1View = (TypeRowView) findViewById(R.id.pkmn_desc_type_1);
		type2View = (TypeRowView) findViewById(R.id.pkmn_desc_type_2);
		baseAttackView = (TextView) findViewById(R.id.pkmn_desc_base_attack);
		baseDefenseView = (TextView) findViewById(R.id.pkmn_desc_base_defense);
		baseStaminaView = (TextView) findViewById(R.id.pkmn_desc_base_stamina);
		maxCPView = (TextView) findViewById(R.id.pkmn_desc_max_cp);
		setVisibility(View.GONE);
	}

	/**
	 * @param isBaseAttVisible
	 *            the isBaseAttVisible to set
	 */
	public void setBaseAttVisible(boolean isBaseAttVisible) {
		baseAttackView.setVisibility(isBaseAttVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isBaseDefVisible
	 *            the isBaseDefVisible to set
	 */
	public void setBaseDefVisible(boolean isBaseDefVisible) {
		baseDefenseView.setVisibility(isBaseDefVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isBaseStaminaVisible
	 *            the isBaseStaminaVisible to set
	 */
	public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
		baseStaminaView.setVisibility(isBaseStaminaVisible ? VISIBLE : GONE);
	}

	/**
	 * @param isMaxCPVisible
	 *            the isMaxCPVisible to set
	 */
	public void setMaxCPVisible(boolean isMaxCPVisible) {
		maxCPView.setVisibility(isMaxCPVisible ? VISIBLE : GONE);
	}

	/**
	 * @return the pkmnDesc
	 */
	public PokemonDescription getPkmnDesc() {
		return pkmnDesc;
	}

	/**
	 * @param pkmnDesc
	 *            the pkmnDesc to set
	 */
	public void setPkmnDesc(PokemonDescription p) {
		pkmnDesc = p;
	}

	private String toNoZeroRoundIntString(final Double d) {
		return (d != null && d > 0) ? String.valueOf(d.intValue()) : getContext().getString(R.string.unknown);
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		PkmnDescRowViewSavedState savedState = new PkmnDescRowViewSavedState(superState);
		// end
		savedState.pkmnDesc = this.pkmnDesc;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof PkmnDescRowViewSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		PkmnDescRowViewSavedState savedState = (PkmnDescRowViewSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.pkmnDesc = savedState.pkmnDesc;
	}

	protected static class PkmnDescRowViewSavedState extends BaseSavedState {

		private PokemonDescription pkmnDesc;

		PkmnDescRowViewSavedState(Parcelable superState) {
			super(superState);
		}

		protected PkmnDescRowViewSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				this.pkmnDesc = in.readParcelable(PclbPokemonDescription.class.getClassLoader());
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (pkmnDesc != null ? 1 : 0));
			if (pkmnDesc != null) {
				out.writeParcelable(new PclbPokemonDescription(pkmnDesc), 0);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<PkmnDescRowViewSavedState> CREATOR = new Parcelable.Creator<PkmnDescRowViewSavedState>() {
			@Override
			public PkmnDescRowViewSavedState createFromParcel(Parcel in) {
				return new PkmnDescRowViewSavedState(in);
			}

			@Override
			public PkmnDescRowViewSavedState[] newArray(int size) {
				return new PkmnDescRowViewSavedState[size];
			}
		};
	}

	@Override
	public void update() {
		if (pkmnDesc == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			nameView.setText(pkmnDesc.getName());

			// imgView.setImageDrawable(ImageHelper.getPkmnDrawable(getContext(),
			// pkmnDesc));

			// where myresource (without the extension) is the file
			Log.d("HIST", pkmnDesc.toString());
			String uri = "@drawable/pokemon_" + pkmnDesc.getPokedexNum();
			int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
			Drawable res = ContextCompat.getDrawable(getContext(), imageResource);
			imgView.setImageDrawable(res);

			Type oldType1 = type1View.getType();
			Type newType1 = pkmnDesc.getType1();
			if (newType1 != null && !newType1.equals(oldType1)) {
				type1View.setType(newType1);
				type1View.update();
			}

			Type oldType2 = type2View.getType();
			Type newType2 = pkmnDesc.getType2();
			if (newType2 == null) {
				type2View.setVisibility(View.INVISIBLE);
			} else if (!newType2.equals(oldType2)) {
				type2View.setVisibility(View.VISIBLE);
				type2View.setType(pkmnDesc.getType2());
				type2View.update();
			}
			// base att
			baseAttackView.setText(toNoZeroRoundIntString(pkmnDesc.getBaseAttack()));
			// base def
			baseDefenseView.setText(toNoZeroRoundIntString(pkmnDesc.getBaseDefense()));
			// base stamina
			baseStaminaView.setText(toNoZeroRoundIntString(pkmnDesc.getBaseStamina()));
			// max cp
			maxCPView.setText(toNoZeroRoundIntString(pkmnDesc.getMaxCP()));
		}
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void updateWith(final PokemonDescription p) {
		setPkmnDesc(p);
		update();
	}
}
