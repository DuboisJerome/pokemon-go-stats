/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.expandables.CustomExpandable;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class PkmnDescView extends RelativeLayout {

	protected PkmnDescRowView pkmnDescView;
	protected CustomExpandable seeMore;

	private PokemonDescription pkmnDesc;
	private TextView kmPerCandy;
	private TextView kmPerEgg;
	private TextView family;
	private LinearLayout evolutions;
	private TextView description;

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
		if (attrs != null) {}

		inflate(getContext(), R.layout.view_pkmn_desc, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		pkmnDescView = (PkmnDescRowView) findViewById(R.id.pkmn_desc_row);
		seeMore = (CustomExpandable) findViewById(R.id.pkmn_desc_see_more);

		RelativeLayout relativeLayout = (RelativeLayout) View.inflate(getContext(), R.layout.view_pkmn_desc_see_more,
				null);

		// family
		kmPerCandy = (TextView) relativeLayout.findViewById(R.id.pkmn_desc_km_per_candy);
		// family
		kmPerEgg = (TextView) relativeLayout.findViewById(R.id.pkmn_desc_km_per_egg);
		// family
		family = (TextView) relativeLayout.findViewById(R.id.pkmn_desc_family);
		// evolution
		evolutions = (LinearLayout) relativeLayout.findViewById(R.id.pkmn_desc_evolutions);
		// description
		description = (TextView) relativeLayout.findViewById(R.id.pkmn_desc_description);

		TextWatcher kmSuffix = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				String km = " " + PkmnDescView.this.getContext().getString(R.string.km);
				if (!s.toString().contains(km)) {
					s.append(km);
				}
			}
		};
		kmPerCandy.addTextChangedListener(kmSuffix);
		kmPerEgg.addTextChangedListener(kmSuffix);

		seeMore.addToInnerLayout(relativeLayout);

		setVisibility(View.GONE);
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
		if (p == null) {
			setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);
			pkmnDescView.setPkmnDesc(p);
			double kmPerCandyDouble = p.getKmsPerCandy();
			String kmPerCandyStr;
			if (kmPerCandyDouble > 0d) {
				kmPerCandyStr = String.valueOf(kmPerCandyDouble);
			} else {
				kmPerCandyStr = getContext().getString(R.string.unknown);
			}
			kmPerCandy.setText(kmPerCandyStr);

			double kmPerEggDouble = p.getKmsPerEgg();
			String kmPerEggStr;
			if (kmPerEggDouble > 0d) {
				kmPerEggStr = String.valueOf(kmPerEggDouble);
			} else {
				kmPerEggStr = getContext().getString(R.string.unknown);
			}
			kmPerEgg.setText(kmPerEggStr);

			family.setText(p.getFamily());
			evolutions.removeAllViews();
			PkmnGoStatsApplication app = (PkmnGoStatsApplication) getContext().getApplicationContext();
			for (long id : p.getEvolutionIds()) {
				PkmnDescRowView evolution = new PkmnDescRowView(getContext());
				evolution.setPkmnDesc(app.getPokemonWithId(id));
				evolutions.addView(evolution);
			}
			description.setText(p.getDescription());
		}
	}

	public static PkmnDescView create(Context context, PokemonDescription p) {
		PkmnDescView v = new PkmnDescView(context);
		v.setPkmnDesc(p);
		return v;
	}
}
