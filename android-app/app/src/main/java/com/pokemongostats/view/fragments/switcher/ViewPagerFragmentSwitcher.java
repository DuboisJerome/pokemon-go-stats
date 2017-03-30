package com.pokemongostats.view.fragments.switcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.view.activities.FragmentSwitcherActivity;
import com.pokemongostats.view.activities.MainMenuActivity;
import com.pokemongostats.view.commons.PagerSlidingTabStripView;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;

public abstract class ViewPagerFragmentSwitcher extends FragmentSwitcher {

    private static final String PAGE_NUM_KEY = "PAGE_NUM_KEY";

    protected ViewPager mViewPager;
    protected SmartFragmentStatePagerAdapter mAdapterViewPager;

    private int lastPageIndex = 0;

    public ViewPagerFragmentSwitcher(final FragmentSwitcherActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View content = mFragmentActivity
                .initFragmentContent(R.layout.fragment_view_pager);

        FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

        mAdapterViewPager = createAdapterViewPager(fm);
        lastPageIndex = savedInstanceState == null ? 0 : savedInstanceState.getInt(PAGE_NUM_KEY);

        mViewPager = (ViewPager) content.findViewById(R.id.vpPager);
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(lastPageIndex, false);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int newPageIndex) {
                if (newPageIndex < 0
                        || newPageIndex >= mAdapterViewPager.getCount()) {
                    return;
                }

                if (!getHistoryService().isBacking()
                        && lastPageIndex != newPageIndex) {
                    getHistoryService().add(new PageHistory(mViewPager,
                            lastPageIndex, newPageIndex));
                }
                lastPageIndex = newPageIndex;
            }
        });

        PagerSlidingTabStripView pagerSlidingTabStrip = (PagerSlidingTabStripView) content
                .findViewById(R.id.pager_header);
        pagerSlidingTabStrip.setViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        // try to back on same view
        if (!getHistoryService().back()) {
            mFragmentActivity.startActivity(new Intent(mFragmentActivity, MainMenuActivity.class));
            mFragmentActivity.finish();
        }
    }

    protected abstract SmartFragmentStatePagerAdapter createAdapterViewPager(
            FragmentManager fm);


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(PAGE_NUM_KEY, mViewPager.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }
}
