package com.pokemongostats.view.fragments.switcher.pokedex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pokemongostats.R;
import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.FragmentSwitcherActivity;
import com.pokemongostats.view.fragments.HistorizedFragment;
import com.pokemongostats.view.fragments.MoveFragment;
import com.pokemongostats.view.fragments.MoveListFragment;
import com.pokemongostats.view.fragments.PkmnListFragment;
import com.pokemongostats.view.fragments.PokedexFragment;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.fragments.switcher.ViewPagerFragmentSwitcher;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.utils.PreferencesUtils;

/**
 * @author Zapagon
 */
public class PokedexFragmentSwitcher extends ViewPagerFragmentSwitcher {

    private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

    public PokedexFragmentSwitcher(final FragmentSwitcherActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
        Fragment currentFragment = mAdapterViewPager
                .getItem(mViewPager.getCurrentItem());
        if (currentFragment != null) {
            fm.putFragment(outState, CURRENT_FRAGMENT_KEY, currentFragment);
        }
    }

    @Override
    public void onBackPressed() {
        // try to back on same view
        if (!HistoryService.INSTANCE.back()) {
            // no back available, put application to background
            mFragmentActivity.moveTaskToBack(true);
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
        HistoryService.INSTANCE.startMacro();

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
        HistoryService.INSTANCE.add(enterNewPageCmd);
        enterNewPageCmd.execute();

        // STOP macro
        CompensableCommand transition = HistoryService.INSTANCE.stopMacro();
        HistoryService.INSTANCE.add(transition);

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

            String n = p == null ? " Not found" : p.name();
            Log.i(TagUtils.DEBUG, "= getItem " + position + " PAGE " + n);
            switch (p) {
                case POKEDEX_FRAGMENT:
                    PokedexFragment pkmnFragment = new PokedexFragment();
                    pkmnFragment.acceptSelectedVisitorMove(moveClickedVisitor);
                    pkmnFragment.acceptSelectedVisitorType(typeClickedVisitor);
                    pkmnFragment.acceptSelectedVisitorPkmnDesc(
                            pkmnDescClickedVisitor);
                    f = pkmnFragment;
                    break;
                case PKMN_LIST_FRAGMENT:
                    PkmnListFragment pkmnListFragment = new PkmnListFragment();
                    pkmnListFragment.acceptSelectedVisitorPkmnDesc(
                            pkmnDescClickedVisitor);
                    f = pkmnListFragment;
                    break;
                case PKMN_TYPE_FRAGMENT:
                    TypeFragment typeFragment = new TypeFragment();
                    typeFragment.acceptSelectedVisitorMove(moveClickedVisitor);
                    typeFragment.acceptSelectedVisitorPkmnDesc(
                            pkmnDescClickedVisitor);

                    f = typeFragment;
                    break;
                case MOVE_FRAGMENT:
                    MoveFragment moveFragment = new MoveFragment();
                    moveFragment.acceptSelectedVisitorPkmnDesc(
                            pkmnDescClickedVisitor);
                    moveFragment.acceptSelectedVisitorType(typeClickedVisitor);
                    f = moveFragment;
                    break;
                case MOVE_LIST_FRAGMENT:
                    MoveListFragment moveListFragment = new MoveListFragment();
                    moveListFragment
                            .acceptSelectedVisitorMove(moveClickedVisitor);

                    f = moveListFragment;
                    break;
                default:
                    f = null;
                    break;
            }
            return f;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Page p = Page.getPageFromPosition(position);
            return (p != null) ? p.getTitle(getFragmentActivity()) : "";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) mFragmentActivity.findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.main_menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        tb.getMenu().findItem(R.id.action_is_last_evolution_only)
                .setChecked(PreferencesUtils.isLastEvolutionOnly(mFragmentActivity.getApplicationContext()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_style_choose:

                int style_number = 0;
                final int POSITION_ROUND = style_number++;
                final int POSITION_FLAT = style_number++;

                CharSequence[] choices = new CharSequence[style_number];
                choices[POSITION_ROUND] = mFragmentActivity.getString(R.string.style_round);
                choices[POSITION_FLAT] = mFragmentActivity.getString(R.string.style_flat);

                // retrieve checked style
                int checkedStyleId = PreferencesUtils.getStyleId(mFragmentActivity.getApplicationContext());
                final int checkedPosition;
                switch (checkedStyleId) {
                    case R.drawable.type_round:
                        checkedPosition = POSITION_ROUND;
                        break;
                    case R.drawable.type_flat:
                        checkedPosition = POSITION_FLAT;
                        break;
                    default:
                        checkedPosition = POSITION_FLAT;
                        break;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentActivity).setTitle("Choose style")
                        .setSingleChoiceItems(choices, checkedPosition, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                if (POSITION_ROUND == position) { // round
                                    PreferencesUtils.setStyleId(mFragmentActivity.getApplicationContext(), R.drawable.type_round);
                                } else if (POSITION_FLAT == position) { // flat
                                    PreferencesUtils.setStyleId(mFragmentActivity.getApplicationContext(), R.drawable.type_flat);
                                }
                                dialog.dismiss();
                            }
                        }).setCancelable(true);
                builder.create().show();
                return true;
            case R.id.action_is_last_evolution_only:
                item.setChecked(!item.isChecked());
                PreferencesUtils.setLastEvolutionOnly(mFragmentActivity.getApplication(), item.isChecked());
                break;
            case R.id.action_minimize:
                if (mFragmentActivity.getService() != null) {
                    mFragmentActivity.getService().minimize();
                }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
