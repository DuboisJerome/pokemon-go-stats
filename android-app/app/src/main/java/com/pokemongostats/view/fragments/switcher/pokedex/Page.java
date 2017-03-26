package com.pokemongostats.view.fragments.switcher.pokedex;

import android.content.Context;

import com.pokemongostats.R;

public enum Page {
    POKEDEX_FRAGMENT(0, R.string.pokedex),
    //
    PKMN_LIST_FRAGMENT(1, R.string.pkmn_sorted_list),
    //
    PKMN_TYPE_FRAGMENT(2, R.string.types),
    //
    MOVE_FRAGMENT(3, R.string.moves),
    //
    MOVE_LIST_FRAGMENT(4, R.string.move_sorted_list);

    private int index;

    private int titleId;

    private Page(final int index, final int titleId) {
        this.index = index;
        this.titleId = titleId;
    }

    public static Page getPageFromPosition(final int pos) {
        for (Page i : values()) {
            if (pos == i.index) {
                return i;
            }
        }
        return null;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the title R.id
     */
    public int getTitleId() {
        return titleId;
    }

    /**
     * @return find title in given context
     */
    public String getTitle(final Context c) {
        if (c == null) {
            return "";
        }
        return c.getString(titleId);
    }
}
