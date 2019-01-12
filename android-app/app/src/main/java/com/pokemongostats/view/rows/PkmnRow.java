/**
 *
 */
package com.pokemongostats.view.rows;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public class PkmnRow extends LinearLayout
        implements
        ItemView<Pkmn> {

    private static final int cacheSize = 8 * 1024 * 1024;
    private static final LruCache<String, Drawable> cachedPkmnDrawables = new LruCache<>(
            cacheSize);
    private TextView nameView;
    private ImageView imgView;
    private TypeRow type1View;
    private TypeRow type2View;
    private TextView realAttackView;
    private TextView realDefenseView;
    private TextView realHPView;
    private TextView CPView;
    private Pkmn pkmn;
    private PkmnDesc pkmnDesc;

    public PkmnRow(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public PkmnRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public PkmnRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        if (attrs != null) {
        }

        inflate(context, R.layout.view_row_pkmn_desc, this);
        // setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        nameView = (TextView) findViewById(R.id.pkmn_name);
        imgView = (ImageView) findViewById(R.id.pkmn_img);
        type1View = (TypeRow) findViewById(R.id.pkmn_type_1);
        type2View = (TypeRow) findViewById(R.id.pkmn_type_2);
        realAttackView = (TextView) findViewById(R.id.pkmn_base_attack);
        realDefenseView = (TextView) findViewById(R.id.pkmn_base_defense);
        realHPView = (TextView) findViewById(R.id.pkmn_base_stamina);
        CPView = (TextView) findViewById(R.id.pkmn_desc_max_cp);
        setVisibility(View.GONE);
    }

    /**
     * @param isBaseAttVisible the isBaseAttVisible to set
     */
    public void setBaseAttVisible(boolean isBaseAttVisible) {
        realAttackView.setVisibility(isBaseAttVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseDefVisible the isBaseDefVisible to set
     */
    public void setBaseDefVisible(boolean isBaseDefVisible) {
        realDefenseView.setVisibility(isBaseDefVisible ? VISIBLE : GONE);
    }

    /**
     * @param isBaseStaminaVisible the isBaseStaminaVisible to set
     */
    public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
        realHPView.setVisibility(isBaseStaminaVisible ? VISIBLE : GONE);
    }

    /**
     * @param isMaxCPVisible the isMaxCPVisible to set
     */
    public void setMaxCPVisible(boolean isMaxCPVisible) {
        CPView.setVisibility(isMaxCPVisible ? VISIBLE : GONE);
    }

    /**
     * @return the pkmn
     */
    public Pkmn getPkmn() {
        return pkmn;
    }

    // Save/Restore State

    /**
     * @param p the pkmn to set
     */
    public void setPkmn(Pkmn p) {
        pkmn = p;
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

        // FIXME
//        PkmnDescRowSavedState savedState = new PkmnDescRowSavedState(
//                superState);
//        // end
//        savedState.pkmnDesc = this.pkmn;
//        Log.d(TagUtils.SAVE, "onSaveInstanceState " + this.pkmn);
//      return savedState;
        return superState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        // FIXME
//        if (!(state instanceof PkmnDescRowSavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//
//        PkmnDescRowSavedState savedState = (PkmnDescRowSavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        // end
//
//        this.pkmn = savedState.pkmnDesc;
        Log.d(TagUtils.SAVE, "onRestoreInstanceState " + this.pkmn);
    }

    @Override
    public void update() {
        if (pkmn == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            nameView.setText(pkmnDesc.getName());
            new AsyncTask<Object, Object, Drawable>() {
                private Context c;
                private int imgRes;
                private String id;

                @Override
                protected void onPreExecute() {
                    id = pkmnDesc.getPokedexNum()+ "_" + pkmnDesc.getForme();
                    // pre execute
                    String uri = "@drawable/pokemon_" + id;
                    c = getContext();
                    String packageName = c.getPackageName();
                    imgRes = getResources().getIdentifier(uri, null, packageName);

                    Drawable d = cachedPkmnDrawables.get(id);

                    if (d == null) {
                        try {
                            d = ContextCompat.getDrawable(c, imgRes);
                        } catch (Resources.NotFoundException e){
                            d = ContextCompat.getDrawable(c, getResources().getIdentifier("@drawable/pokeball_close", null, packageName));
                        }
                        // post execute
                        cachedPkmnDrawables.put(id, d);
                    }
                }

                @Override
                protected Drawable doInBackground(Object[] objects) {
                    return cachedPkmnDrawables.get(id);
                }

                @Override
                protected void onPostExecute(Drawable d) {
                    if (id.equals(pkmnDesc.getPokedexNum()+ "_" + pkmnDesc.getForme())) {
                        imgView.setImageDrawable(d);
                    }
                }
            }.execute();

            Type newType1 = pkmnDesc.getType1();
            if (newType1 != null) {
                type1View.updateWith(newType1);
            } else {
                Log.e(TagUtils.DEBUG, "Type 1 null pour " + pkmn);
                type1View.setVisibility(INVISIBLE);
            }

            Type newType2 = pkmnDesc.getType2();
            if (newType2 == null) {
                type2View.setVisibility(View.INVISIBLE);
            } else {
                type2View.setVisibility(View.VISIBLE);
                type2View.updateWith(pkmnDesc.getType2());
            }
            /*double att = FightUtils.computeAttack(pkmn);
            double def = FightUtils.computeDefense(pkmn);
            double hp = FightUtils.computeHP(pkmn);
            // base att
            realAttackView
                    .setText(toNoZeroRoundIntString(att));
            // base def
            realDefenseView
                    .setText(toNoZeroRoundIntString(def));
            // base stamina
            realHPView
                    .setText(toNoZeroRoundIntString(hp));*/
            // max cp
            CPView.setText(toNoZeroRoundIntString((double)pkmn.getCP()));
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void updateWith(final Pkmn p) {
        setPkmn(p);
        update();
    }
}
