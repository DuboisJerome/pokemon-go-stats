package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.R;
import com.pokemongostats.controller.HistoryService;
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
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.listeners.SelectedVisitor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * 
 * @author Zapagon
 *
 */
public class PokedexFragmentSwitcher extends ViewPagerFragmentSwitcher {

	private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

	public enum PAGE {
		POKEDEX_FRAGMENT(0, R.string.pokedex),
		//
		PKMN_LIST_FRAGMENT(1, R.string.pkmn_sorted_list),
		//
		PKMN_TYPE_FRAGMENT(2, R.string.types),
		//
		MOVE_FRAGMENT(3, R.string.moves),
		//
		MOVE_LIST_FRAGMENT(4, R.string.move_sorted_list);

		private int index;

		private int titleId;

		private PAGE(final int index, final int titleId) {
			this.index = index;
			this.titleId = titleId;
		}

		public static PAGE getPageFromPosition(final int pos) {
			for (PAGE i : values()) {
				if (pos == i.index) { return i; }
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
			if (c == null) { return ""; }
			return c.getString(titleId);
		}
	}

	public PokedexFragmentSwitcher(final CustomAppCompatActivity activity) {
		super(activity);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
		if (mCurrentFragment != null) {
			fm.putFragment(outState, CURRENT_FRAGMENT_KEY, mCurrentFragment);
		}
	}

	final SelectedVisitor<Type> typeClickedVisitor = new SelectedVisitor<Type>() {

		@Override
		public void select(Type type) {
			if (type == null) { return; }

			CompensableCommand cmd = createTransitionTo(PAGE.PKMN_TYPE_FRAGMENT.index,
					getPkmnTypeFragment().createCommand(type));
			// execute cmd
			cmd.execute();
			// add to history
			HistoryService.INSTANCE.add(cmd);
		}
	};

	final SelectedVisitor<PokemonDescription> pkmnDescClickedVisitor = new SelectedVisitor<PokemonDescription>() {

		@Override
		public void select(final PokemonDescription pkmn) {
			if (pkmn == null) { return; }

			CompensableCommand cmd = createTransitionTo(PAGE.POKEDEX_FRAGMENT.index,
					getPokedexFragment().createCommand(pkmn));
			// execute cmd
			cmd.execute();
			// add to history
			HistoryService.INSTANCE.add(cmd);
		}
	};

	final SelectedVisitor<Move> moveClickedVisitor = new SelectedVisitor<Move>() {

		@Override
		public void select(final Move m) {
			if (m == null) { return; }

			CompensableCommand cmd = createTransitionTo(PAGE.MOVE_FRAGMENT.index, getMoveFragment().createCommand(m));
			// execute cmd
			cmd.execute();
			// add to history
			HistoryService.INSTANCE.add(cmd);
		}
	};

	@Override
	public int getPageCount() {
		return PAGE.values().length;
	}

	@Override
	public HistorizedFragment<?> getPageAt(int position) {
		final HistorizedFragment<?> f;
		PAGE p = PAGE.getPageFromPosition(position);
		switch (p) {
		case POKEDEX_FRAGMENT:
			f = getPokedexFragment();
			break;
		case PKMN_LIST_FRAGMENT:
			f = getPkmnListFragment();
			break;
		case PKMN_TYPE_FRAGMENT:
			f = getPkmnTypeFragment();
			break;
		case MOVE_FRAGMENT:
			f = getMoveFragment();
			break;
		case MOVE_LIST_FRAGMENT:
			f = getMoveListFragment();
			break;
		default:
			f = null;
			break;
		}
		return f;
	}

	@Override
	public CharSequence getPageTitleAt(int position) {
		PAGE p = PAGE.getPageFromPosition(position);
		if (p != null) { return p.getTitle(getFragmentActivity()); }
		return "";
	}

	/**
	 * @return PokedexFragment
	 */
	private PokedexFragment getPokedexFragment() {
		PokedexFragment pkmnFragment = (PokedexFragment) mAdapterViewPager
				.getRegisteredFragment(PAGE.POKEDEX_FRAGMENT.index);
		// if not found
		if (pkmnFragment == null) {
			pkmnFragment = new PokedexFragment();
			pkmnFragment.acceptSelectedVisitorMove(moveClickedVisitor);
			pkmnFragment.acceptSelectedVisitorType(typeClickedVisitor);
			pkmnFragment.acceptSelectedVisitorPkmnDesc(pkmnDescClickedVisitor);
		}
		return pkmnFragment;
	}

	/**
	 * @return TypeFragment
	 */
	private TypeFragment getPkmnTypeFragment() {
		TypeFragment typeFragment = (TypeFragment) mAdapterViewPager
				.getRegisteredFragment(PAGE.PKMN_TYPE_FRAGMENT.index);
		// if not found
		if (typeFragment == null) {
			typeFragment = new TypeFragment();
			typeFragment.acceptSelectedVisitorMove(moveClickedVisitor);
			typeFragment.acceptSelectedVisitorPkmnDesc(pkmnDescClickedVisitor);
		}
		return typeFragment;
	}

	/**
	 * @return MoveFragment
	 */
	private MoveFragment getMoveFragment() {
		MoveFragment moveFragment = (MoveFragment) mAdapterViewPager.getRegisteredFragment(PAGE.MOVE_FRAGMENT.index);
		// if not found
		if (moveFragment == null) {
			moveFragment = new MoveFragment();
			moveFragment.acceptSelectedVisitorPkmnDesc(pkmnDescClickedVisitor);
			moveFragment.acceptSelectedVisitorType(typeClickedVisitor);
		}
		return moveFragment;
	}

	/**
	 * @return PkmnListFragment
	 */
	private PkmnListFragment getPkmnListFragment() {
		PkmnListFragment pkmnListFragment = (PkmnListFragment) mAdapterViewPager
				.getRegisteredFragment(PAGE.PKMN_LIST_FRAGMENT.index);
		// if not found
		if (pkmnListFragment == null) {
			pkmnListFragment = new PkmnListFragment();
			pkmnListFragment.acceptSelectedVisitorPkmnDesc(pkmnDescClickedVisitor);
		}
		return pkmnListFragment;
	}

	/**
	 * @return MoveListFragment
	 */
	private MoveListFragment getMoveListFragment() {
		MoveListFragment moveListFragment = (MoveListFragment) mAdapterViewPager
				.getRegisteredFragment(PAGE.MOVE_LIST_FRAGMENT.index);
		// if not found
		if (moveListFragment == null) {
			moveListFragment = new MoveListFragment();
			moveListFragment.acceptSelectedVisitorMove(moveClickedVisitor);
		}
		return moveListFragment;
	}
}
