package com.pokemongostats.view.fragments.switcher.pokedex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.fragments.HistorizedFragment;
import com.pokemongostats.view.fragments.MoveFragment;
import com.pokemongostats.view.fragments.MoveListFragment;
import com.pokemongostats.view.fragments.PkmnListFragment;
import com.pokemongostats.view.fragments.PokedexFragment;
import com.pokemongostats.view.fragments.SmartFragmentStatePagerAdapter;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.fragments.switcher.ViewPagerFragmentSwitcher;
import com.pokemongostats.view.listeners.SelectedVisitor;

/**
 * 
 * @author Zapagon
 *
 */
public class PokedexFragmentSwitcher extends ViewPagerFragmentSwitcher {

	private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

	public PokedexFragmentSwitcher(final CustomAppCompatActivity activity) {
		super(activity);
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

	public class PokedexFragmentPagerAdapter
			extends
				SmartFragmentStatePagerAdapter {

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
			if (f != null) { return f; }
			Page p = Page.getPageFromPosition(position);

			String n = p == null ? " Not found" : p.name();
			Log.i(TagUtils.DEBUG, "= getItem " + position + " PAGE " + n);
			switch (p) {
				case POKEDEX_FRAGMENT :
					PokedexFragment pkmnFragment = new PokedexFragment();
					pkmnFragment.acceptSelectedVisitorMove(moveClickedVisitor);
					pkmnFragment.acceptSelectedVisitorType(typeClickedVisitor);
					pkmnFragment.acceptSelectedVisitorPkmnDesc(
							pkmnDescClickedVisitor);
					f = pkmnFragment;
					break;
				case PKMN_LIST_FRAGMENT :
					PkmnListFragment pkmnListFragment = new PkmnListFragment();
					pkmnListFragment.acceptSelectedVisitorPkmnDesc(
							pkmnDescClickedVisitor);
					f = pkmnListFragment;
					break;
				case PKMN_TYPE_FRAGMENT :
					TypeFragment typeFragment = new TypeFragment();
					typeFragment.acceptSelectedVisitorMove(moveClickedVisitor);
					typeFragment.acceptSelectedVisitorPkmnDesc(
							pkmnDescClickedVisitor);

					f = typeFragment;
					break;
				case MOVE_FRAGMENT :
					MoveFragment moveFragment = new MoveFragment();
					moveFragment.acceptSelectedVisitorPkmnDesc(
							pkmnDescClickedVisitor);
					moveFragment.acceptSelectedVisitorType(typeClickedVisitor);
					f = moveFragment;
					break;
				case MOVE_LIST_FRAGMENT :
					MoveListFragment moveListFragment = new MoveListFragment();
					moveListFragment
							.acceptSelectedVisitorMove(moveClickedVisitor);

					f = moveListFragment;
					break;
				default :
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

		final SelectedVisitor<Move> moveClickedVisitor = new SelectedVisitor<Move>() {

			@Override
			public void select(final Move newItem) {
				if (newItem == null) { return; }
				doTransitionToWithItem(Page.MOVE_FRAGMENT.getIndex(), newItem);
			}
		};

		final SelectedVisitor<Type> typeClickedVisitor = new SelectedVisitor<Type>() {

			@Override
			public void select(Type newItem) {
				if (newItem == null) { return; }

				doTransitionToWithItem(Page.PKMN_TYPE_FRAGMENT.getIndex(),
						newItem);
			}
		};

		final SelectedVisitor<PokemonDescription> pkmnDescClickedVisitor = new SelectedVisitor<PokemonDescription>() {

			@Override
			public void select(final PokemonDescription newItem) {
				if (newItem == null) { return; }
				doTransitionToWithItem(Page.POKEDEX_FRAGMENT.getIndex(),
						newItem);
			}
		};
	}

	public class ChangeFragmentItemCommand<T> implements CompensableCommand {

		private int position;
		private T lastItem, newItem;

		/**
		 * 
		 * @param position
		 *            Position of the fragment in the ViewPager
		 * @param lastItem
		 *            LastItem shown in the fragment
		 * @param newItem
		 *            NewItem to show in the fragment
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

	/**
	 * 
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

}
