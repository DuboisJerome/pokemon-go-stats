package com.pokemongostats.view.switcher;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.adapters.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragment.FragmentSwitcherFragment;
import com.pokemongostats.view.fragment.HistorizedFragment;
import com.pokemongostats.view.fragment.MoveFragment;
import com.pokemongostats.view.fragment.MoveListFragment;
import com.pokemongostats.view.fragment.PkmnFragment;
import com.pokemongostats.view.fragment.PkmnListFragment;
import com.pokemongostats.view.fragment.TypeFragment;
import com.pokemongostats.view.listeners.SelectedVisitor;

import java.util.Objects;

/**
 * @author Zapagon
 */
public class PokedexFragmentSwitcher extends ViewPagerFragmentSwitcher {

    public PokedexFragmentSwitcher(final FragmentSwitcherFragment fragment) {
        super(fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // try to back on same view
        if (getHistoryService().back()) {
            HistorizedFragment<?> nextFragment = (HistorizedFragment<?>) mAdapterViewPager
                    .getRegisteredFragment(mViewPager.getCurrentItem());
            if (nextFragment != null) {
                nextFragment.updateView();
            }
        } else {
            // no back available, put application to background
            MainActivity a = getParentFragment().getMainActivity();
            if (a != null && a.getService() != null) {
                a.getService().minimize();
            } else {
                Objects.requireNonNull(getParentFragment().getActivity()).moveTaskToBack(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SmartFragmentStatePagerAdapter createAdapterViewPager(
            FragmentManager fm) {
        return new PokedexFragmentPagerAdapter(fm);
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
        final CompensableCommand enterNewPageCmd = new ChangeFragmentItemCommand<>(
                newIndex, newFragment.getCurrentItem(), newItem);
        getHistoryService().add(enterNewPageCmd);
        enterNewPageCmd.execute();

        // STOP macro
        CompensableCommand transition = getHistoryService().stopMacro();
        getHistoryService().add(transition);

        // refresh view
        newFragment.updateView();
    }

    private final SelectedVisitor<Move> moveClickedVisitor = newItem -> {
        if (newItem == null) {
            return;
        }
        doTransitionToWithItem(Page.MOVE_FRAGMENT.getIndex(), newItem);
    };
    private final SelectedVisitor<Type> typeClickedVisitor = newItem -> {
        if (newItem == null) {
            return;
        }

        doTransitionToWithItem(Page.PKMN_TYPE_FRAGMENT.getIndex(),
                newItem);
    };
    private final SelectedVisitor<PkmnDesc> pkmnDescClickedVisitor = newItem -> {
        if (newItem == null) {
            return;
        }
        doTransitionToWithItem(Page.POKEDEX_FRAGMENT.getIndex(),
                newItem);
    };

    private class PokedexFragmentPagerAdapter
            extends
            SmartFragmentStatePagerAdapter {

        PokedexFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return Page.values().length;
        }

        // Returns the fragment to display for that page
        @NonNull
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
                    case POKEDEX_FRAGMENT:
                        PkmnFragment pkmnFragment = new PkmnFragment();
                        pkmnFragment.acceptSelectedVisitorMove(moveClickedVisitor);
                        pkmnFragment.acceptSelectedVisitorType(typeClickedVisitor);
                        pkmnFragment.acceptSelectedVisitorPkmnDesc(
                                pkmnDescClickedVisitor);
                        pkmnFragment.setHistoryService(getHistoryService());
                        f = pkmnFragment;
                        break;
                    case PKMN_LIST_FRAGMENT:
                        PkmnListFragment pkmnListFragment = new PkmnListFragment();
                        pkmnListFragment.acceptSelectedVisitorPkmnDesc(
                                pkmnDescClickedVisitor);
                        pkmnListFragment.setHistoryService(getHistoryService());
                        f = pkmnListFragment;
                        break;
                    case PKMN_TYPE_FRAGMENT:
                        TypeFragment typeFragment = new TypeFragment();
                        typeFragment.acceptSelectedVisitorMove(moveClickedVisitor);
                        typeFragment.acceptSelectedVisitorPkmnDesc(
                                pkmnDescClickedVisitor);
                        typeFragment.setHistoryService(getHistoryService());

                        f = typeFragment;
                        break;
                    case MOVE_FRAGMENT:
                        MoveFragment moveFragment = new MoveFragment();
                        moveFragment.acceptSelectedVisitorPkmnDesc(
                                pkmnDescClickedVisitor);
                        moveFragment.acceptSelectedVisitorType(typeClickedVisitor);
                        moveFragment.setHistoryService(getHistoryService());
                        f = moveFragment;
                        break;
                    case MOVE_LIST_FRAGMENT:
                        MoveListFragment moveListFragment = new MoveListFragment();
                        moveListFragment
                                .acceptSelectedVisitorMove(moveClickedVisitor);
                        moveListFragment.setHistoryService(getHistoryService());

                        f = moveListFragment;
                        break;
                    default:
                        f = null;
                        break;
                }
            }

            assert f != null;
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
        POKEDEX_FRAGMENT(0, R.string.pokedex),
        //
        PKMN_LIST_FRAGMENT(1, R.string.pkmn_sorted_list),
        //
        PKMN_TYPE_FRAGMENT(2, R.string.types),
        //
        MOVE_FRAGMENT(3, R.string.moves),
        //
        MOVE_LIST_FRAGMENT(4, R.string.move_sorted_list);

        private final int index;

        private final int titleId;

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
