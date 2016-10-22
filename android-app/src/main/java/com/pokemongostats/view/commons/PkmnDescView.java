/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PokemonDescription;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class PkmnDescView extends LinearLayout {

	private TextView nameView;
	private ImageView imgView;
	private TypeView type1View;
	private TypeView type2View;

	private PokemonDescription pkmnDesc;

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
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		nameView = (TextView) findViewById(R.id.pkmn_desc_name);
		imgView = (ImageView) findViewById(R.id.pkmn_desc_img);
		type1View = (TypeView) findViewById(R.id.pkmn_desc_type_1);
		type2View = (TypeView) findViewById(R.id.pkmn_desc_type_2);
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
			nameView.setText(p.getName());
			nameView.setTextColor(getContext().getResources().getColor(android.R.color.white));
			imgView.setImageDrawable(ImageHelper.getPkmnDrawable(getContext(), p));
			type1View.setType(p.getType1());
			if (p.getType2() == null) {
				type2View.setVisibility(View.INVISIBLE);
			} else {
				type2View.setVisibility(View.VISIBLE);
				type2View.setType(p.getType2());
			}
		}
	}

	public static PkmnDescView create(Context context, PokemonDescription p) {
		PkmnDescView v = new PkmnDescView(context);
		v.setPkmnDesc(p);
		return v;
	}
}
