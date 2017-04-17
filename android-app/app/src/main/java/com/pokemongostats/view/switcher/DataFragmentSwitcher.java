package com.pokemongostats.view.switcher;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.fragment.FragmentSwitcherFragment;
import com.pokemongostats.view.fragment.HistorizedFragment;
import com.pokemongostats.view.fragment.PkmnListFragment;
import com.pokemongostats.view.adapters.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragment.TrainerListFragment;
import com.pokemongostats.view.fragment.TypeFragment;

/**
 * @author Zapagon
 */
public class DataFragmentSwitcher extends ViewPagerFragmentSwitcher {

    public DataFragmentSwitcher(final FragmentSwitcherFragment fragment) {
        super(fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // try to back on same view
        if (!getHistoryService().back()) {
            // no back available, put application to background
            MainActivity a = getParentFragment().getMainActivity();
            if (a != null && a.getService() != null) {
                a.getService().minimize();
            } else {
                getParentFragment().getActivity().moveTaskToBack(true);
            }
        } else {
            HistorizedFragment<?> nextFragment = (HistorizedFragment<?>) mAdapterViewPager
                    .getRegisteredFragment(mViewPager.getCurrentItem());
            if (nextFragment != null) {
                nextFragment.updateView();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SmartFragmentStatePagerAdapter createAdapterViewPager(
            FragmentManager fm) {
        return new DataFragmentPagerAdapter(fm);
    }

    /**
     * @param newIndex New index
     * @param newItem  New item
     */
    @SuppressWarnings("unchecked")
    private <T> void doTransitionToWithItem(int newIndex, T newItem) {
        final int lastPagePosition = mViewPager.getCurrentItem();

        // Start macro
        getHistoryService().startMacro();

        if (newIndex != lastPagePosition) {
            // setCurrentItem will create new PageHistory
            mViewPager.setCurrentItem(newIndex);
        }

        // get new fragment
        HistorizedFragment<T> newFragment = (HistorizedFragment<T>) mAdapterViewPager
                .getRegisteredFragment(newIndex);

        // cmd for setting new fragment
        final CompensableCommand enterNewPageCmd = new ChangeFragmentItemCommand<T>(
                newIndex, newFragment.getCurrentItem(), newItem);
        getHistoryService().add(enterNewPageCmd);
        enterNewPageCmd.execute();

        // STOP macro
        CompensableCommand transition = getHistoryService().stopMacro();
        getHistoryService().add(transition);

        // refresh view
        newFragment.updateView();
    }

    private class DataFragmentPagerAdapter
            extends
            SmartFragmentStatePagerAdapter {

        DataFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return Page.values().length;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            Fragment f = this.getRegisteredFragment(position);
            if (f != null) {
                return f;
            }
            Page p = Page.getPageFromPosition(position);
            if (p != null) {
                Log.i(TagUtils.DEBUG, "= getItem " + position + " PAGE " + p);
                switch (p) {
                    case TRAINER_FRAGMENT:
                        TrainerListFragment trainerListFragment = new TrainerListFragment();
                        trainerListFragment.setHistoryService(getHistoryService());
                        f = trainerListFragment;
                        break;
                    case PKMN_LIST_FRAGMENT:
                        PkmnListFragment pkmnListFragment = new PkmnListFragment();
                        pkmnListFragment.setHistoryService(getHistoryService());
                        f = pkmnListFragment;
                        break;
                    case PKMN_DETAIL_FRAGMENT:
                        TypeFragment typeFragment = new TypeFragment();
                        typeFragment.setHistoryService(getHistoryService());

                        f = typeFragment;
                        break;
                    default:
                        f = null;
                        break;
                }
            }

            return f;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Page p = Page.getPageFromPosition(position);
            return (p != null) ? p.getTitle(getParentFragment().getActivity()) : "";
        }
    }

    private enum Page {
        TRAINER_FRAGMENT(0, R.string.trainer),
        //
        PKMN_LIST_FRAGMENT(1, R.string.pkmn_sorted_list),
        //
        PKMN_DETAIL_FRAGMENT(2, R.string.detail);

        private int index;

        private int titleId;

        Page(final int index, final int titleId) {
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
}