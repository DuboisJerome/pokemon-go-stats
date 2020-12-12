package com.pokemongostats.view.switcher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.adapters.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.commons.PagerSlidingTabStripView;
import com.pokemongostats.view.fragment.FragmentSwitcherFragment;
import com.pokemongostats.view.fragment.HistorizedFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
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
            if (a != null) {
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

    protected class ChangeFragmentItemCommand<T> implements CompensableCommand {

        private final int position;
        private final T lastItem;
        private final T newItem;

        /**
         * @param position Position of the fragment in the ViewPager
         * @param lastItem LastItem shown in the fragment
         * @param newItem  NewItem to show in the fragment
         */
        public ChangeFragmentItemCommand(int position, T lastItem, T newItem) {
            this.position = position;
            this.lastItem = lastItem;
            this.newItem = newItem;
        }

        @Override
        public void execute() {
            Log.d(TagUtils.HIST, "=== Before execute " + this);
            changeFragmentItem(newItem);
            Log.d(TagUtils.HIST, "=== After execute " + this);
        }

        @Override
        public void compensate() {
            Log.d(TagUtils.HIST, "=== Before compensate " + this);
            changeFragmentItem(lastItem);
            Log.d(TagUtils.HIST, "=== After compensate " + this);
        }

        private void changeFragmentItem(T item) {
            Fragment f = mAdapterViewPager.getRegisteredFragment(position);
            if (f != null) {
                try {
                    @SuppressWarnings("unchecked")
                    HistorizedFragment<T> hf = (HistorizedFragment<T>) f;
                    hf.setCurrentItem(item);
                } catch (ClassCastException cce) {
                    Log.e(TagUtils.DEBUG, cce.getLocalizedMessage(), cce);
                }
            } else {
                Log.e(TagUtils.DEBUG, "Fragment at " + position
                        + " is null or not yet created");
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @NonNull
        @Override
        public String toString() {
            return "ChangeFragmentItemCommand [position=" + position
                    + ", lastItem=" + lastItem + ", newItem=" + newItem + "]";
        }
    }
}
