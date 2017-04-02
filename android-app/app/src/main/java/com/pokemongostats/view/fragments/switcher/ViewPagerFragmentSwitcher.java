package com.pokemongostats.view.fragments.switcher;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.R;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.activities.FragmentSwitcherFragment;
import com.pokemongostats.view.commons.PagerSlidingTabStripView;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.utils.KeyboardUtils;

public abstract class ViewPagerFragmentSwitcher extends FragmentSwitcher {

    private static final String PAGE_NUM_KEY = "PAGE_NUM_KEY";

    protected ViewPager mViewPager;
    protected SmartFragmentStatePagerAdapter mAdapterViewPager;

    private int lastPageIndex = 0;

    public ViewPagerFragmentSwitcher(final FragmentSwitcherFragment fragment) {
        super(fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FragmentManager fm = getParentFragment().getChildFragmentManager();

        mAdapterViewPager = createAdapterViewPager(fm);
        lastPageIndex = savedInstanceState == null ? 0 : savedInstanceState.getInt(PAGE_NUM_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_view_pager, container,
                false);

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

                if (lastPageIndex != newPageIndex) {
                    if (!getHistoryService().isBacking()) {
                        getHistoryService().add(new PageHistory(ViewPagerFragmentSwitcher.this,
                                lastPageIndex, newPageIndex));
                    }
                    KeyboardUtils.hideKeyboard(getParentFragment().getActivity());
                }
                lastPageIndex = newPageIndex;
                setCurrentFragment(mAdapterViewPager.getItem(newPageIndex));
            }
        });

        PagerSlidingTabStripView pagerSlidingTabStrip = (PagerSlidingTabStripView) content
                .findViewById(R.id.pager_header);
        pagerSlidingTabStrip.setViewPager(mViewPager);

        return content;
    }

    @Override
    public void onBackPressed() {
        // try to back on same view
        if (!getHistoryService().back()) {
            MainActivity a = getParentFragment().getMainActivity();
            if(a != null){
                if (a.getService() != null) {
                    a.getService().minimize();
                }
            }
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

    public void setCurrentItem(int newPageIndex) {
        mViewPager.setCurrentItem(newPageIndex);
    }
}
