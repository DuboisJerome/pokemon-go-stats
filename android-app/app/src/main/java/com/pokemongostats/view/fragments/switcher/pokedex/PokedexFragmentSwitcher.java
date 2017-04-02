package com.pokemongostats.view.fragments.switcher.pokedex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.FragmentSwitcherFragment;
import com.pokemongostats.view.activities.MainActivity;
import com.pokemongostats.view.fragments.HistorizedFragment;
import com.pokemongostats.view.fragments.MoveFragment;
import com.pokemongostats.view.fragments.MoveListFragment;
import com.pokemongostats.view.fragments.PkmnListFragment;
import com.pokemongostats.view.fragments.PkmnFragment;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.fragments.switcher.ViewPagerFragmentSwitcher;
import com.pokemongostats.view.listeners.SelectedVisitor;

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
        if (!getHistoryService().back()) {
            // no back available, put application to background
            MainActivity a = getParentFragment().getMainActivity();
            if(a != null && a.getService() != null){
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
        return new PokedexFragmentPagerAdapter(fm);
    }

    /**
     * @param newIndex
     * @param newItem
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> void doTransitionToWithItem(int newIndex, T newItem) {
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

    public class PokedexFragmentPagerAdapter
            extends
            SmartFragmentStatePagerAdapter {

        final SelectedVisitor<Move> moveClickedVisitor = new SelectedVisitor<Move>() {

            @Override
            public void select(final Move newItem) {
                if (newItem == null) {
                    return;
                }
                doTransitionToWithItem(Page.MOVE_FRAGMENT.getIndex(), newItem);
            }
        };
        final SelectedVisitor<Type> typeClickedVisitor = new SelectedVisitor<Type>() {

            @Override
            public void select(Type newItem) {
                if (newItem == null) {
                    return;
                }

                doTransitionToWithItem(Page.PKMN_TYPE_FRAGMENT.getIndex(),
                        newItem);
            }
        };
        final SelectedVisitor<PkmnDesc> pkmnDescClickedVisitor = new SelectedVisitor<PkmnDesc>() {

            @Override
            public void select(final PkmnDesc newItem) {
                if (newItem == null) {
                    return;
                }
                doTransitionToWithItem(Page.POKEDEX_FRAGMENT.getIndex(),
                        newItem);
            }
        };

        public PokedexFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
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

            return f;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Page p = Page.getPageFromPosition(position);
            return (p != null) ? p.getTitle(getParentFragment().getActivity()) : "";
        }
    }

    public class ChangeFragmentItemCommand<T> implements CompensableCommand {

        private int position;
        private T lastItem, newItem;

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
        @Override
        public String toString() {
            return "ChangeFragmentItemCommand [position=" + position
                    + ", lastItem=" + lastItem + ", newItem=" + newItem + "]";
        }
    }
}
