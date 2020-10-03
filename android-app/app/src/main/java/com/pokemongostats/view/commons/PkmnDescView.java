package com.pokemongostats.view.commons;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.PkmnDescRow;
import com.pokemongostats.view.rows.TypeRow;
import com.pokemongostats.view.utils.PkmnDrawableCache;

import java.util.List;

/**
 * @author Zapagon
 */
public class PkmnDescView extends LinearLayout
        implements
        HasPkmnDescSelectable {

    private PokedexDAO dao;

    private PkmnDesc mPkmnDesc;

    private TextView nameView;
    private ImageView imgView;
    private TypeRow type1View;
    private TypeRow type2View;

    private ImageTextFieldView mBaseAtt;
    private ImageTextFieldView mBaseDef;
    private ImageTextFieldView mBaseStamina;
    private ImageTextFieldView mKmPerEgg;
    private ImageTextFieldView mKmPerCandy;
    private ImageTextFieldView mMaxCP;

    private TextView mEvolutionFamilyTitle;
    private LinearLayout mLayoutEvolutionFamily;

    private TableLabelTextFieldView mFamily;
    private TableLabelTextFieldView mDescription;
    private SelectedVisitor<PkmnDesc> mCallbackPkmnDesc;

    public PkmnDescView(Context context) {
        super(context);
        initializeViews(null);
    }

    public PkmnDescView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(attrs);
    }

    public PkmnDescView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(attrs);
    }

    private void initializeViews(final AttributeSet attrs) {
        dao = new PokedexDAO(getContext());

        inflate(getContext(), R.layout.view_pkmn_desc, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        nameView = (TextView) findViewById(R.id.pkmn_name);
        imgView = (ImageView) findViewById(R.id.pkmn_img);
        type1View = (TypeRow) findViewById(R.id.pkmn_type_1);
        type2View = (TypeRow) findViewById(R.id.pkmn_type_2);

        // caracteristics
        mBaseAtt = findViewById(R.id.pkmn_desc_field_base_attack);
        mBaseDef = findViewById(R.id.pkmn_desc_field_base_defense);
        mBaseStamina = findViewById(R.id.pkmn_desc_field_base_stamina);
        //
        mKmPerEgg = findViewById(R.id.pkmn_desc_field_km_per_egg);
        // candies
        mKmPerCandy = findViewById(R.id.pkmn_desc_km_per_candy);
        // max cp
        mMaxCP = findViewById(R.id.pkmn_desc_field_max_cp);
        // evolutions
        mEvolutionFamilyTitle = (TextView) findViewById(R.id.pkmn_desc_evolutions_title);
        mLayoutEvolutionFamily = (LinearLayout) findViewById(R.id.pkmn_desc_evolutions);
        // family
        mFamily = findViewById(R.id.pkmn_desc_family);
        // description
        mDescription = (TableLabelTextFieldView) findViewById(R.id.pkmn_desc_field_description);

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
        mKmPerCandy.getField().addTextChangedListener(kmSuffix);
        mKmPerEgg.getField().addTextChangedListener(kmSuffix);

        setVisibility(View.GONE);
    }

    /**
     * @return the pkmnDesc
     */
    public PkmnDesc getPkmnDesc() {
        return mPkmnDesc;
    }

    /**
     * @param p the pkmnDesc to set
     */
    public void setPkmnDesc(PkmnDesc p) {
        mPkmnDesc = p;
        if (p == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            //
            String name = "# " + p.getPokedexNum() + System.getProperty("line.separator") + p.getName();
            nameView.setText(name);
            PkmnDrawableCache.getAsync(getContext(), p, d -> imgView.setImageDrawable(d));

            Type newType1 = p.getType1();
            if (newType1 != null) {
                type1View.updateWith(newType1);
            } else {
                Log.e(TagUtils.DEBUG, "Type 1 null pour " + p);
                type1View.setVisibility(INVISIBLE);
            }

            Type newType2 = p.getType2();
            if (newType2 == null) {
                type2View.setVisibility(View.INVISIBLE);
            } else {
                type2View.setVisibility(View.VISIBLE);
                type2View.updateWith(p.getType2());
            }

            mBaseAtt.setFieldText(toNoZeroRoundIntString(p.getBaseAttack()));
            mBaseDef.setFieldText(toNoZeroRoundIntString(p.getBaseDefense()));
            mBaseStamina
                    .setFieldText(toNoZeroRoundIntString(p.getBaseStamina()));

            mKmPerCandy
                    .setFieldText(toNoZeroRoundIntString(p.getKmsPerCandy()));
            mKmPerEgg.setFieldText(toNoZeroRoundIntString(p.getKmsPerEgg()));
            mMaxCP.setFieldText(toNoZeroRoundIntString(p.getMaxCP()));

            final List<Evolution> basesEvol = dao.findBasesPokemons(p.getPokedexNum(), p.getForm());
            final List<Evolution> nextEvol = dao.findEvolutionsPokemons(p.getPokedexNum(), p.getForm());
            // default evolIds contains p.pokedexNum then size == 1
            if (!basesEvol.isEmpty() || !nextEvol.isEmpty()) {
                // list of evolutions
                mEvolutionFamilyTitle.setVisibility(View.VISIBLE);
                mLayoutEvolutionFamily.setVisibility(View.VISIBLE);
                mLayoutEvolutionFamily.removeAllViews();

                for (Evolution ev: basesEvol) {
                    final PkmnDesc pkmnFound = dao.getPokemonWithId(ev.getBasePkmnId(), ev.getBasePkmnForm());
                    addEvol(pkmnFound, p);
                }

                addEvol(p, p);

                for (Evolution ev: nextEvol) {
                    final PkmnDesc pkmnFound = dao.getPokemonWithId(ev.getEvolutionId(), ev.getEvolutionForm());
                    addEvol(pkmnFound, p);
                }
            } else {
                mEvolutionFamilyTitle.setVisibility(View.GONE);
                mLayoutEvolutionFamily.setVisibility(View.GONE);
            }

            mFamily.setFieldText(p.getFamily());
            mDescription.setFieldText(p.getDescription());
        }
    }

    private void addEvol(final PkmnDesc pkmnFound, final PkmnDesc currentPkmn){
        PkmnDescRow evolution = new PkmnDescRow(getContext());
        evolution.updateWith(pkmnFound);
        if (pkmnFound.getPokedexNum() == currentPkmn.getPokedexNum() && pkmnFound.getForm().equals(currentPkmn.getForm())) {
            evolution.setBackgroundColor(getContext().getResources().getColor(R.color.row_item_focus, getContext().getTheme()));
        } else {
            evolution.setOnClickListener(v -> {
                if (mCallbackPkmnDesc == null) {
                    return;
                }
                mCallbackPkmnDesc.select(pkmnFound);
            });
        }
        mLayoutEvolutionFamily.addView(evolution);
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

    @Override
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor) {
        this.mCallbackPkmnDesc = visitor;
    }

    protected static class PkmnDescViewSavedState extends BaseSavedState {

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
        private PkmnDesc pkmnDesc;

        PkmnDescViewSavedState(Parcelable superState) {
            super(superState);
        }

        protected PkmnDescViewSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                this.pkmnDesc = in.readParcelable(
                        PclbPkmnDesc.class.getClassLoader());
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (pkmnDesc != null ? 1 : 0));
            if (pkmnDesc != null) {
                out.writeParcelable(new PclbPkmnDesc(pkmnDesc),
                        Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            }
        }
    }

}
