package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.R;
import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.model.commands.MacroCompensableCommand;
import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.commons.PagerSlidingTabStrip;
import com.pokemongostats.view.fragments.HistorizedFragment;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

public abstract class ViewPagerFragmentSwitcher extends FragmentSwitcher {

	private ViewPager mViewPager;
	protected SmartFragmentStatePagerAdapter mAdapterViewPager;

	protected class PageHistory implements CompensableCommand {
		// index of page
		private int lastPageIndex, newPageIndex;

		public PageHistory(final int lastPageIndex, final int newPageIndex) {
			this.lastPageIndex = lastPageIndex;
			this.newPageIndex = newPageIndex;
		}

		@Override
		public void execute() {
			// set new fragment
			Log.d("HIST", "go to Page n°" + newPageIndex);
			mViewPager.setCurrentItem(newPageIndex);
		}

		@Override
		public void compensate() {
			// set last fragment
			Log.d("HIST", "back to Page n°" + lastPageIndex);
			mViewPager.setCurrentItem(lastPageIndex);
		}
	}

	public ViewPagerFragmentSwitcher(final CustomAppCompatActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View content = mFragmentActivity.initContent(R.layout.view_pager_fragment_activity);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

		mAdapterViewPager = new HistorizedFragmentPagerAdapter(fm);

		mViewPager = (ViewPager) content.findViewById(R.id.vpPager);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageSelected(int position) {
				if (position < 0 || position >= getPageCount()) { return; }
				mCurrentFragment = getPageAt(position);
			}
		});

		PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) content.findViewById(R.id.pager_header);
		mViewPager.setAdapter(mAdapterViewPager);
		pagerSlidingTabStrip.setViewPager(mViewPager);
	}

	private class HistorizedFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {

		public HistorizedFragmentPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return getPageCount();
		}

		// Returns the fragment to display for that page
		@Override
		public HistorizedFragment<?> getItem(int position) {
			return getPageAt(position);
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return getPageTitleAt(position);
		}
	}

	public abstract int getPageCount();

	public abstract HistorizedFragment<?> getPageAt(int position);

	public HistorizedFragment<?> getCurrentFragment() {
		return getPageAt(mViewPager.getCurrentItem());
	}

	public abstract CharSequence getPageTitleAt(int position);

	@Override
	public void onBackPressed() {
		// try to back on same view
		if (!HistoryService.INSTANCE.back()) {
			// no back available, put application to background
			mFragmentActivity.moveTaskToBack(true);
		}
	}

	public CompensableCommand createTransitionTo(final int newPagePosition,
			final CompensableCommand newPageSettingCmd) {
		final int lastPagePosition = mViewPager.getCurrentItem();

		final CompensableCommand transition;
		if (newPagePosition != lastPagePosition) {
			MacroCompensableCommand macro = new MacroCompensableCommand();
			// cmd for resetting last fragment
			macro.addCmd(getPageAt(lastPagePosition).createCommand(null));
			// save the transition
			macro.addCmd(new PageHistory(lastPagePosition, newPagePosition));
			// cmd for setting new fragment
			macro.addCmd(newPageSettingCmd);

			transition = macro;
		} else {
			transition = newPageSettingCmd;
		}

		return transition;
	}
}
