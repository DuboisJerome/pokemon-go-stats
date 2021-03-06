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
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.utils.PkmnDrawableCache;

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

        nameView = (TextView) findViewById(R.id.pkmn_name);
        imgView = (ImageView) findViewById(R.id.pkmn_img);
        type1View = (TypeRow) findViewById(R.id.pkmn_type_1);
        type2View = (TypeRow) findViewById(R.id.pkmn_type_2);
        baseAttackView = (TextView) findViewById(R.id.pkmn_base_attack);
        baseDefenseView = (TextView) findViewById(R.id.pkmn_base_defense);
        baseStaminaView = (TextView) findViewById(R.id.pkmn_base_stamina);
        maxCPView = (TextView) findViewById(R.id.pkmn_desc_max_cp);
    }

    /**
     * @param isBaseAttVisible the isBaseAttVisible to set
     */
    public void setBaseAttVisible(boolean isBaseAttVisible) {
        baseAttackView.setVisibility(isBaseAttVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseDefVisible the isBaseDefVisible to set
     */
    public void setBaseDefVisible(boolean isBaseDefVisible) {
        baseDefenseView.setVisibility(isBaseDefVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseStaminaVisible the isBaseStaminaVisible to set
     */
    public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
        baseStaminaView.setVisibility(isBaseStaminaVisible ? VISIBLE : GONE);
    }

    /**
     * @param isMaxCPVisible the isMaxCPVisible to set
     */
    public void setMaxCPVisible(boolean isMaxCPVisible) {
        maxCPView.setVisibility(isMaxCPVisible ? VISIBLE : GONE);
    }

    /**
     * @return the pkmnDesc
     */
    public PkmnDesc getPkmnDesc() {
        return pkmnDesc;
    }

    // Save/Restore State

    /**
     * @param p the pkmnDesc to set
     */
    public void setPkmnDesc(PkmnDesc p) {
        pkmnDesc = p;
    }

    private String toNoZeroRoundIntString(final Double d) {
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
        Log.d(TagUtils.SAVE, "onSaveInstanceState " + this.pkmnDesc);

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
        Log.d(TagUtils.SAVE, "onRestoreInstanceState " + this.pkmnDesc);
    }

    @Override
    public void update() {
        if (pkmnDesc == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            String name = "# " + pkmnDesc.getPokedexNum() + System.getProperty("line.separator") + pkmnDesc.getName();
            nameView.setText(name);
            PkmnDrawableCache.getAsync(getContext(), pkmnDesc, d -> imgView.setImageDrawable(d));

            Type newType1 = pkmnDesc.getType1();
            if (newType1 != null) {
                type1View.updateWith(newType1);
            } else {
                Log.e(TagUtils.DEBUG, "Type 1 null pour " + pkmnDesc);
                type1View.setVisibility(INVISIBLE);
            }

            Type newType2 = pkmnDesc.getType2();
            if (newType2 == null) {
                type2View.setVisibility(View.INVISIBLE);
            } else {
                type2View.setVisibility(View.VISIBLE);
                type2View.updateWith(pkmnDesc.getType2());
            }
            // base att
            baseAttackView
                    .setText(toNoZeroRoundIntString(pkmnDesc.getBaseAttack()));
            // base def
            baseDefenseView
                    .setText(toNoZeroRoundIntString(pkmnDesc.getBaseDefense()));
            // base stamina
            baseStaminaView
                    .setText(toNoZeroRoundIntString(pkmnDesc.getBaseStamina()));
            // max cp
            maxCPView.setText(toNoZeroRoundIntString(pkmnDesc.getMaxCP()));
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(final PkmnDesc p) {
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
            out.writeByte((byte) (pkmnDesc != null ? 1 : 0));
            if (pkmnDesc != null) {
                out.writeParcelable(new PclbPkmnDesc(pkmnDesc), 0);
            }
        }
    }
}
