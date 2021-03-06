package com.pokemongostats.view.switcher;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.commands.CompensableCommand;

public class PageHistory implements CompensableCommand {
    // index of page
    private final int lastPageIndex;
    private final int newPageIndex;
    private final ViewPagerFragmentSwitcher viewPager;

    public PageHistory(final ViewPagerFragmentSwitcher viewPagerHolder, final int lastPageIndex,
                       final int newPageIndex) {
        this.viewPager = viewPagerHolder;
        this.lastPageIndex = lastPageIndex;
        this.newPageIndex = newPageIndex;
    }

    @Override
    public void execute() {
        // set new fragment
        Log.d(TagUtils.HIST, "=== Before execute " + this);
        if (viewPager != null) {
            viewPager.setCurrentItem(newPageIndex);
        }
        Log.d(TagUtils.HIST, "=== After execute " + this);
    }

    @Override
    public void compensate() {
        // set last fragment
        Log.d(TagUtils.HIST, "=== Before compensate " + this);
        if (viewPager != null) {
            viewPager.setCurrentItem(lastPageIndex);
        }
        Log.d(TagUtils.HIST, "=== After compensate " + this);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @NonNull
    @Override
    public String toString() {
        return "PageHistory [lastPageIndex=" + lastPageIndex + ", newPageIndex="
                + newPageIndex + "]";
    }
}