package com.pokemongostats.view.fragments.switcher;

import java.util.Stack;

import com.pokemongostats.R;
import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.commons.PagerSlidingTabStrip;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragments.StackFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

public abstract class ViewPagerFragmentSwitcher extends FragmentSwitcher {

	protected ViewPager mViewPager;
	protected SmartFragmentStatePagerAdapter mAdapterViewPager;
	protected Stack<Integer> historyOfPositions = new Stack<Integer>();
	private int lastPosition = 0;
	private boolean isBacking = false;

	public ViewPagerFragmentSwitcher(final CustomAppCompatActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View content = mFragmentActivity
				.initContent(R.layout.view_pager_fragment_activity);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

		mAdapterViewPager = new StackFragmentPagerAdapter(fm);

		mViewPager = (ViewPager) content.findViewById(R.id.vpPager);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				if (position < 0 || position >= getPageCount()) { return; }
				if (!isBacking) {
					getPageAt(lastPosition).addNewHistory();
					historyOfPositions.add(lastPosition);
				}
				lastPosition = position;
				mCurrentFragment = getPageAt(position);
			}
		});

		PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) content
				.findViewById(R.id.pager_header);
		mViewPager.setAdapter(mAdapterViewPager);
		pagerSlidingTabStrip.setViewPager(mViewPager);
	}

	private class StackFragmentPagerAdapter
			extends
				SmartFragmentStatePagerAdapter {

		public StackFragmentPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return getPageCount();
		}

		// Returns the fragment to display for that page
		@Override
		public StackFragment<?> getItem(int position) {
			return getPageAt(position);
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return getPageTitleAt(position);
		}
	}

	public abstract int getPageCount();

	public abstract StackFragment<?> getPageAt(int position);

	public abstract CharSequence getPageTitleAt(int position);

	@Override
	public void onBackPressed() {
		isBacking = true;
		StackFragment<?> stackFragment = getPageAt(mViewPager.getCurrentItem());
		if (stackFragment != null) {
			Stack<?> fragmentHistory = stackFragment.getCurrentHistory();
			if (fragmentHistory != null && !fragmentHistory.isEmpty()) {
				// back on same view
				stackFragment.back();
			} else if (!historyOfPositions.isEmpty()) {
				// back to other tab
				Integer position = historyOfPositions.pop();
				mViewPager.setCurrentItem(position);
				getPageAt(position).popHistory();
			} else {
				// last fragment, back application to background
				mFragmentActivity.moveTaskToBack(true);
			}
		} else {
			// should not happened
			Log.e("BACK",
					"getFragmentPageAt(mViewPager.getCurrentItem()) return null");
		}
		isBacking = false;
	}

}
