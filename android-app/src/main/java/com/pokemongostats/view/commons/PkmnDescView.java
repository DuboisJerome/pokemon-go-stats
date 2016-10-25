/**
 * 
 */
package com.pokemongostats.view.commons;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.expandables.CustomExpandable;
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
public class PkmnDescView extends RelativeLayout {

	protected PkmnDescRowView mPkmnDescView;
	protected CustomExpandable mSeeMore;

	private PokemonDescription mPkmnDesc;
	private TableLabelFieldView mFamily;
	private TableLabelFieldView mKmPerCandy;
	private TableLabelFieldView mCandyToEvolve;
	private TableLabelFieldView mKmPerEgg;
	private TableLabelFieldView mMaxCP;

	private TextView mEvolutionsTitle;
	private LinearLayout mLayoutEvolutions;

	private TableLabelFieldView mDescription;

	public PkmnDescView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public PkmnDescView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public PkmnDescView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
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
		mKmPerCandy = (TableLabelFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_km_per_candy);
		mCandyToEvolve = (TableLabelFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_candy_to_evolve);
		//
		mKmPerEgg = (TableLabelFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_km_per_egg);
		// max cp
		mMaxCP = (TableLabelFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_max_cp);
		// family
		mFamily = (TableLabelFieldView) seeMoreContent
				.findViewById(R.id.pkmn_desc_family);
		// evolutions
		mEvolutionsTitle = (TextView) seeMoreContent
				.findViewById(R.id.pkmn_desc_evolutions_title);
		mLayoutEvolutions = (LinearLayout) seeMoreContent
				.findViewById(R.id.pkmn_desc_evolutions);
		// description
		mDescription = (TableLabelFieldView) seeMoreContent
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
			String unknown = getContext().getString(R.string.unknown);;
			// retrieve km needed per candy or unknown
			double kmPerCandyDouble = p.getKmsPerCandy();
			String kmPerCandyStr = (kmPerCandyDouble > 0d)
					? String.valueOf(kmPerCandyDouble)
					: unknown;

			// retrieve km needed per egg or unknown
			double kmPerEggDouble = p.getKmsPerEgg();
			String kmPerEggStr = (kmPerEggDouble > 0d)
					? String.valueOf(kmPerEggDouble)
					: unknown;

			// retrieve candy to evolve
			String candyToEvolve = p.getCandyToEvolve() > 0
					? String.valueOf(p.getCandyToEvolve())
					: unknown;

			setVisibility(View.VISIBLE);
			mPkmnDescView.setPkmnDesc(p);
			mFamily.setFieldText(p.getFamily());
			mKmPerCandy.setFieldText(kmPerCandyStr);

			List<Long> evolIds = p.getEvolutionIds();
			if (evolIds != null && !evolIds.isEmpty()) {
				// set candy to evolve
				mCandyToEvolve.setVisibility(View.VISIBLE);
				mCandyToEvolve.setFieldText(candyToEvolve);
				// list of evolutions
				mEvolutionsTitle.setVisibility(View.VISIBLE);
				mLayoutEvolutions.setVisibility(View.VISIBLE);
				mLayoutEvolutions.removeAllViews();
				PkmnGoStatsApplication app = (PkmnGoStatsApplication) getContext()
						.getApplicationContext();
				for (long id : p.getEvolutionIds()) {
					PkmnDescRowView evolution = new PkmnDescRowView(
							getContext());
					evolution.setPkmnDesc(app.getPokemonWithId(id));
					mLayoutEvolutions.addView(evolution);
				}
			} else {
				mCandyToEvolve.setVisibility(View.GONE);
				mEvolutionsTitle.setVisibility(View.GONE);
				mLayoutEvolutions.setVisibility(View.GONE);
			}

			mKmPerEgg.setFieldText(kmPerEggStr);
			mMaxCP.setFieldText(String.valueOf(p.getMaxCP()));
			mDescription.setFieldText(p.getDescription());
		}
	}

	public static PkmnDescView create(Context context, PokemonDescription p) {
		PkmnDescView v = new PkmnDescView(context);
		v.setPkmnDesc(p);
		return v;
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
}
