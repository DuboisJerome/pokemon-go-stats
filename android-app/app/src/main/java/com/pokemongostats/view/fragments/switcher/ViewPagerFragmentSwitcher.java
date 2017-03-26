package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.commons.PagerSlidingTabStripView;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;

public abstract class ViewPagerFragmentSwitcher extends FragmentSwitcher {

    protected ViewPager mViewPager;
    protected SmartFragmentStatePagerAdapter mAdapterViewPager;

    public ViewPagerFragmentSwitcher(final CustomAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View content = mFragmentActivity
                .initContent(R.layout.view_pager_fragment_activity);

        FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

        mAdapterViewPager = createAdapterViewPager(fm);

        mViewPager = (ViewPager) content.findViewById(R.id.vpPager);
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            private int lastPageIndex = 0;

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

                if (!HistoryService.INSTANCE.isBacking()
                        && lastPageIndex != newPageIndex) {
                    HistoryService.INSTANCE.add(new PageHistory(mViewPager,
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
        if (!HistoryService.INSTANCE.back()) {
            // no back available, put application to background
            mFragmentActivity.moveTaskToBack(true);
        }
    }

    protected abstract SmartFragmentStatePagerAdapter createAdapterViewPager(
            FragmentManager fm);
}
