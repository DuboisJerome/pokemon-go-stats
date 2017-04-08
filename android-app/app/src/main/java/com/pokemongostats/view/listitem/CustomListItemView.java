/**
 *
 */
package com.pokemongostats.view.listitem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.commons.CustomListView;

/**
 * @author Zapagon
 */
public abstract class CustomListItemView<T> extends CustomListView {

    protected OnItemClickListener<T> onItemClickListener;

    public CustomListItemView(Context context) {
        super(context);
    }

    public CustomListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListItemView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param l OnItemClickListener
     */
    public void setOnItemClickListener(final OnItemClickListener<T> l) {
        this.onItemClickListener = l;
    }

    @Override
    protected View getOrCreateChildView(final int position, View convertView) {
        final View view = super.getOrCreateChildView(position, convertView);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        T item = (T) mAdapter.getItem(position);
                        onItemClickListener.onItemClick(item);
                    } catch (Exception e) {
                        Log.e(TagUtils.DEBUG, "Erreur while clicking item view", e);
                    }
                }
            }
        });
        return view;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }
}
