package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.commands.CompensableCommand;

import android.support.v4.view.ViewPager;
import android.util.Log;

public class PageHistory implements CompensableCommand {
	// index of page
	private int lastPageIndex, newPageIndex;
	private ViewPager viewPager;

	public PageHistory(final ViewPager viewPager, final int lastPageIndex,
			final int newPageIndex) {
		this.viewPager = viewPager;
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
	@Override
	public String toString() {
		return "PageHistory [lastPageIndex=" + lastPageIndex + ", newPageIndex="
			+ newPageIndex + "]";
	}
}