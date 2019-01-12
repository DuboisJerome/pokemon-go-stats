/**
 *
 */
package com.pokemongostats.view.listitem;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.parcalables.PclbType;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.commons.CustomListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public class TypeListItemView extends CustomListItemView<Type> {

    private List<CustomListItemView<Type>> colonnes = new ArrayList<>();

    public TypeListItemView(Context context) {
        this(context, 1);
    }

    public TypeListItemView(Context context, final int nbColonnes) {
        super(context);
        for(int c = 0; c < nbColonnes; ++c){
            final int cFinal = c;
            final CustomListItemView<Type> colonne =  new CustomListItemView<Type>(context){
                /**
                 * initialize views using adapter getView method
                 */
                public void initViewsFromAdapter() {
                    this.removeAllViews();
                    if (mAdapter != null && getVisibility() == VISIBLE) {
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            if(i % nbColonnes != cFinal){
                                continue;
                            }
                            this.addView(getOrCreateChildView(i, null), i);
                        }
                    }
                }

                protected View getOrCreateChildView(int position, View convertView) {
                    View v = super.getOrCreateChildView((position*nbColonnes) + cFinal, convertView);
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.setMargins(5,5,5,5);
                    v.setLayoutParams(lp);
                    return v;
                }
                /**
                 * refresh views form adapter, typically when some data where add or remove
                 * to the adapter
                 */
                public void refreshViewsFromAdapter() {
                    // number of child in the expandable view
                    int childCount = this.getChildCount();
                    // number of data in adapter
                    int modulo = mAdapter.getCount() % nbColonnes;
                    int adapterSize = (mAdapter.getCount() / nbColonnes) + (cFinal < modulo ? 1 : 0);
                    // number of reusable view
                    int reuseCount = Math.min(childCount, adapterSize);

                    // for all reusable view, getView (which may update content of given
                    // view)
                    for (int i = 0; i < reuseCount; i++) {
                        // update existing view
                        View oldView = this.getChildAt(i);
                        View updatedView = getOrCreateChildView(i, oldView);
                        updatedView.setVisibility(getVisibility());
                        // if getView create a new view, delete the old and add the new one
                        if (oldView == null
                                || oldView.getId() != updatedView.getId()) {
                            this.removeViewAt(i);
                            this.addView(updatedView, i);
                        }
                    }

                    // if there is actually less views than data => create new views
                    if (childCount < adapterSize) {
                        for (int i = childCount; i < adapterSize; i++) {
                            View newView = getOrCreateChildView(i, null);
                            newView.setVisibility(getVisibility());
                            this.addView(newView, i);
                        }
                        // else if there is more views than data, hide exceeding views
                    } else if (childCount > adapterSize) {
                        for (int i = adapterSize; i < childCount; i++) {
                            View child = this.getChildAt(i);
                            child.setVisibility(GONE);
                        }
                    }
                    // else if same amount of visible view and data, nothing to do
                }

            };
            colonne.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
            colonne.setPadding(2,3,2,3);
            colonnes.add(colonne);
        }
        setWeightSum(nbColonnes);
        setOrientation(HORIZONTAL);
    }

    // Save/Restore State

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        TypeExpandableSavedState savedState = new TypeExpandableSavedState(
                superState);
        // end
        // savedState.mList = this.mListItem;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof TypeExpandableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        TypeExpandableSavedState savedState = (TypeExpandableSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end
        // this.mListItem = savedState.mList;

    }

    protected static class TypeExpandableSavedState
            extends
            BaseSavedState {

        // required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<TypeExpandableSavedState> CREATOR = new Parcelable.Creator<TypeExpandableSavedState>() {
            @Override
            public TypeExpandableSavedState createFromParcel(Parcel in) {
                return new TypeExpandableSavedState(in);
            }

            @Override
            public TypeExpandableSavedState[] newArray(int size) {
                return new TypeExpandableSavedState[size];
            }
        };
        private List<Type> mList;

        TypeExpandableSavedState(Parcelable superState) {
            super(superState);
        }

        protected TypeExpandableSavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                // ArrayList of PclbMove (PclbMove extends Move)
                mList.clear();
                ArrayList<PclbType> arrayList = in
                        .createTypedArrayList(PclbType.CREATOR);
                for (PclbType p : arrayList) {
                    mList.add(p.getType());
                }
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            boolean isListEmpty = (mList == null || mList.isEmpty());
            out.writeByte((byte) (!isListEmpty ? 1 : 0));
            if (!isListEmpty) {
                ArrayList<PclbType> arrayList = new ArrayList<PclbType>();
                for (Type type : mList) {
                    arrayList.add(new PclbType(type));
                }
                out.writeTypedList(arrayList);
            }
        }
    }

    public void setAdapter(Adapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        this.mAdapter = adapter;
        for(CustomListItemView<Type> colonne : colonnes){
            colonne.setAdapter(adapter);
        }
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        initViewsFromAdapter();
    }

    /**
     * initialize views using adapter getView method
     */
    public void initViewsFromAdapter() {
        final boolean reaffiche = mAdapter != null && getVisibility() == VISIBLE;
        final int nbColonnes = colonnes.size();
        this.removeAllViews();
        for(int c = 0; c < nbColonnes; c++){
            CustomListItemView<Type> colonne = colonnes.get(c);
            colonne.removeAllViews();
            if (reaffiche) {
                this.addView(colonne);
                colonne.initViewsFromAdapter();
            }
        }

    }

    /**
     * refresh views form adapter, typically when some data where add or remove
     * to the adapter
     */
    public void refreshViewsFromAdapter() {
        for(CustomListItemView<Type> colonne : colonnes){
            colonne.refreshViewsFromAdapter();
        }
    }

    /**
     * @param l OnItemClickListener
     */
    public void setOnItemClickListener(final CustomListItemView.OnItemClickListener<Type> l) {
        super.setOnItemClickListener(l);
        for(CustomListItemView<Type> colonne : colonnes){
            colonne.setOnItemClickListener(l);
        }
    }

}
