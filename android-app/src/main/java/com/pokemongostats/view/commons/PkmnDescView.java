/**
 * 
 */
package com.pokemongostats.view.commons;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.expandables.CustomExpandable;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.parcalables.PclbPokemonDescription;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class PkmnDescView extends RelativeLayout
		implements
			HasPkmnDescSelectable {

	protected PkmnDescRowView mPkmnDescView;
	protected CustomExpandable mSeeMore;

	private PokemonDescription mPkmnDesc;
	private TableLabelTextFieldView mFamily;
	private TableLabelTextFieldView mKmPerCandy;
	private TableLabelTextFieldView mCandyToEvolve;
	private TableLabelTextFieldView mKmPerEgg;
	private TableLabelTextFieldView mMaxCP;
	private TableLabelTextFieldView mBaseAtt;
	private TableLabelTextFieldView mBaseDef;
	private TableLabelTextFieldView mBaseStamina;

	private TextView mEvolutionFamilyTitle;
	private LinearLayout mLayoutEvolutionFamily;

	private TableLabelTextFieldView mDescription;
	private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

	public PkmnDescView(Context context) {
		super(context);
		initializeViews(null);
	}

	public PkmnDescView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(attrs);
	}

	public PkmnDescView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(attrs);
	}

	private void initializeViews(final AttributeSet attrs) {
		if (attrs != null) {
		}

		inflate(getContext(), R.layout.view_pkmn_desc, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		mPkmnDescView = (PkmnDescRowView) findViewById(R.id.pkmn_desc_row);
		mSeeMore = (CustomExpandable) findViewById(R.id.pkmn_desc_see_more);

		ViewGroup seeMoreContent = (ViewGroup) View.inflate(getContext(),
				R.layout.view_pkmn_desc_see_more, null);

		// candies
		mKmPerCandy = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_km_per_candy);
		mCandyToEvolve = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_candy_to_evolve);
		//
		mKmPerEgg = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_km_per_egg);
		// max cp
		mMaxCP = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_max_cp);
		// caracteristics
		mBaseAtt = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_base_attack);
		mBaseDef = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_base_defense);
		mBaseStamina = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_base_stamina);
		// family
		mFamily = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_family);
		// evolutions
		mEvolutionFamilyTitle = (TextView) seeMoreContent
				.findViewById(R.id.pkmn_desc_evolutions_title);
		mLayoutEvolutionFamily = (LinearLayout) seeMoreContent
				.findViewById(R.id.pkmn_desc_evolutions);
		// description
		mDescription = (TableLabelTextFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_description);

		TextWatcher kmSuffix = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String km = " "
					+ PkmnDescView.this.getContext().getString(R.string.km);
				if (!s.toString().contains(km)) {
					s.append(km);
				}
			}
		};
		mKmPerCandy.addTextChangedListener(kmSuffix);
		mKmPerEgg.addTextChangedListener(kmSuffix);

		mSeeMore.addToInnerLayout(seeMoreContent);

		setVisibility(View.GONE);
	}

	/**
	 * @return the pkmnDesc
	 */
	public PokemonDescription getPkmnDesc() {
		return mPkmnDesc;
	}

	/**
	 * @param mPkmnDesc
	 *            the pkmnDesc to set
	 */
	public void setPkmnDesc(PokemonDescription p) {
		mPkmnDesc = p;
		if (p == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			//
			mPkmnDescView.setPkmnDesc(p);
			mFamily.setFieldText(p.getFamily());
			mKmPerCandy.setFieldText(toNoZeroRoundIntString(p.getKmsPerCandy()));

			// set candy to evolve
			if (p.getCandyToEvolve() > 0) {
				mCandyToEvolve.setVisibility(View.VISIBLE);
				mCandyToEvolve
						.setFieldText(String.valueOf(p.getCandyToEvolve()));
			} else {
				mCandyToEvolve.setVisibility(View.INVISIBLE);
			}

			mKmPerEgg.setFieldText(toNoZeroRoundIntString(p.getKmsPerEgg()));
			mMaxCP.setFieldText(toNoZeroRoundIntString(p.getMaxCP()));
			mBaseAtt.setFieldText(toNoZeroRoundIntString(p.getBaseAttack()));
			mBaseDef.setFieldText(toNoZeroRoundIntString(p.getBaseDefense()));
			mBaseStamina.setFieldText(toNoZeroRoundIntString(p.getBaseStamina()));

			List<Long> evolIds = p.getEvolutionIds();
			// default evolIds contains p.pokedexNum then size == 1
			if (evolIds != null && evolIds.size() > 1) {
				// list of evolutions
				mEvolutionFamilyTitle.setVisibility(View.VISIBLE);
				mLayoutEvolutionFamily.setVisibility(View.VISIBLE);
				mLayoutEvolutionFamily.removeAllViews();
				PkmnGoStatsApplication app = (PkmnGoStatsApplication) getContext()
						.getApplicationContext();
				for (long id : p.getEvolutionIds()) {
					PkmnDescRowView evolution = new PkmnDescRowView(
							getContext());
					final PokemonDescription pkmnFound = app
							.getPokemonWithId(id);
					evolution.setPkmnDesc(pkmnFound);
					if (id == p.getPokedexNum()) {
						evolution.setBackgroundColor(getContext().getResources()
								.getColor(R.color.even_row));
					} else {
						evolution.setOnClickListener(
								new OnClickItemListener<PokemonDescription>(
										mCallbackPkmnDesc, pkmnFound));
					}

					mLayoutEvolutionFamily.addView(evolution);
				}
			} else {
				mEvolutionFamilyTitle.setVisibility(View.GONE);
				mLayoutEvolutionFamily.setVisibility(View.GONE);
			}

			mDescription.setFieldText(p.getDescription());
		}
	}

	public static PkmnDescView create(Context context, PokemonDescription p) {
		PkmnDescView v = new PkmnDescView(context);
		v.setPkmnDesc(p);
		return v;
	}

	private String toNoZeroRoundIntString(final Double d) {
		return (d != null && d > 0)
				? String.valueOf(d.intValue())
				: getContext().getString(R.string.unknown);
	}

	// Save/Restore State

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		PkmnDescViewSavedState savedState = new PkmnDescViewSavedState(
				superState);
		// end
		savedState.pkmnDesc = this.mPkmnDesc;

		return savedState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof PkmnDescViewSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		PkmnDescViewSavedState savedState = (PkmnDescViewSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		this.mPkmnDesc = savedState.pkmnDesc;
	}

	protected static class PkmnDescViewSavedState extends BaseSavedState {

		private PokemonDescription pkmnDesc;

		PkmnDescViewSavedState(Parcelable superState) {
			super(superState);
		}

		protected PkmnDescViewSavedState(Parcel in) {
			super(in);
			if (in.readByte() != 0) {
				this.pkmnDesc = in.readParcelable(
						PclbPokemonDescription.class.getClassLoader());
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (pkmnDesc != null ? 1 : 0));
			if (pkmnDesc != null) {
				out.writeParcelable(new PclbPokemonDescription(pkmnDesc),
						Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
			}
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<PkmnDescViewSavedState> CREATOR = new Parcelable.Creator<PkmnDescViewSavedState>() {
			@Override
			public PkmnDescViewSavedState createFromParcel(Parcel in) {
				return new PkmnDescViewSavedState(in);
			}
			@Override
			public PkmnDescViewSavedState[] newArray(int size) {
				return new PkmnDescViewSavedState[size];
			}
		};
	}

	@Override
	public void acceptSelectedVisitorPkmnDesc(
			final SelectedVisitor<PokemonDescription> visitor) {
		this.mCallbackPkmnDesc = visitor;
	}

}
