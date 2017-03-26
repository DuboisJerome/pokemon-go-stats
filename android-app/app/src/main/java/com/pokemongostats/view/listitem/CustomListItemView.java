/**
 *
 */
package com.pokemongostats.view.listitem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.commons.CustomListView;

/**
 * @author Zapagon
 */
public abstract class CustomListItemView<T> extends CustomListView {

    protected OnItemClickListener<T> onItemClickListener;
    private int oddRowColor;
    private int evenRowColor;

    public CustomListItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomListItemView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        oddRowColor = getContext().getResources().getColor(R.color.odd_row);
        evenRowColor = getContext().getResources().getColor(R.color.even_row);
    }

    protected void recolorEvenOddRows(int start, int end) {
        // repaint odd/even row
        for (int i = start; i < end; ++i) {
            int color = (i + 1) % 2 == 0 ? evenRowColor : oddRowColor;
            this.getChildAt(i).setBackgroundColor(color);
        }
    }

    /**
     * @return
     */
    public OnItemClickListener<T> getOnItemClickListener() {
        return this.onItemClickListener;
    }

    /**
     * @param l
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
