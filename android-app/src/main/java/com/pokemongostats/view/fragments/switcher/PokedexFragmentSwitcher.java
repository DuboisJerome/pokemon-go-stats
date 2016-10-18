package com.pokemongostats.view.fragments.switcher;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.fragments.MoveFragment;
import com.pokemongostats.view.fragments.PokedexFragment;
import com.pokemongostats.view.fragments.StackFragment;
import com.pokemongostats.view.fragments.TypeFragment;
import com.pokemongostats.view.listeners.HasMoveSelectableListener;
import com.pokemongostats.view.listeners.HasPkmnDescSelectableListener;
import com.pokemongostats.view.listeners.HasTypeSelectableListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 
 * @author Zapagon
 *
 */
public class PokedexFragmentSwitcher extends ViewPagerFragmentSwitcher
		implements
			HasTypeSelectableListener,
			HasPkmnDescSelectableListener,
			HasMoveSelectableListener {

	private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

	private static final int POKEDEX_FRAGMENT_PAGE_INDEX = 0;

	private static final int PKMN_TYPE_FRAGMENT_PAGE_INDEX = 1;

	private static final int MOVE_FRAGMENT_PAGE_INDEX = 2;

	public PokedexFragmentSwitcher(final FragmentActivity activity) {
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

	@Override
	public void onTypeSelected(Type type) {
		if (type == null) { return; }
		mViewPager.setCurrentItem(PKMN_TYPE_FRAGMENT_PAGE_INDEX);
		getPkmnTypeFragment().changeViewWithItem(type);
	}

	@Override
	public void onPkmnDescSelected(PokemonDescription pkmn) {
		if (pkmn == null) { return; }
		mViewPager.setCurrentItem(POKEDEX_FRAGMENT_PAGE_INDEX);
		getPokedexFragment().changeViewWithItem(pkmn);
	}

	@Override
	public void onMoveSelected(Move m) {
		if (m == null) { return; }
		mViewPager.setCurrentItem(MOVE_FRAGMENT_PAGE_INDEX);
		getMoveFragment().changeViewWithItem(m);
	}

	@Override
	public int getPageCount() {
		return 3;
	}

	@Override
	public StackFragment<?> getPageAt(int position) {
		final StackFragment<?> f;
		switch (position) {
			case POKEDEX_FRAGMENT_PAGE_INDEX :
				f = getPokedexFragment();
				break;
			case PKMN_TYPE_FRAGMENT_PAGE_INDEX :
				f = getPkmnTypeFragment();
				break;
			case MOVE_FRAGMENT_PAGE_INDEX :
				f = getMoveFragment();
				break;
			default :
				f = null;
				break;
		}
		return f;
	}

	@Override
	public CharSequence getPageTitleAt(int position) {
		switch (position) {
			case POKEDEX_FRAGMENT_PAGE_INDEX :
				return getFragmentActivity().getString(R.string.pokedex);
			case PKMN_TYPE_FRAGMENT_PAGE_INDEX :
				return getFragmentActivity().getString(R.string.types);
			case MOVE_FRAGMENT_PAGE_INDEX :
				return getFragmentActivity().getString(R.string.moves);
			default :
				return "";
		}
	}

	private PokedexFragment getPokedexFragment() {
		PokedexFragment pkmnFragment = (PokedexFragment) mAdapterViewPager
				.getRegisteredFragment(POKEDEX_FRAGMENT_PAGE_INDEX);
		// if not found
		if (pkmnFragment == null) {
			pkmnFragment = new PokedexFragment(this, this);
		}
		return pkmnFragment;
	}

	private TypeFragment getPkmnTypeFragment() {
		TypeFragment typeFragment = (TypeFragment) mAdapterViewPager
				.getRegisteredFragment(PKMN_TYPE_FRAGMENT_PAGE_INDEX);
		// if not found
		if (typeFragment == null) {
			typeFragment = new TypeFragment(this, this);
		}
		return typeFragment;
	}

	private MoveFragment getMoveFragment() {
		MoveFragment moveFragment = (MoveFragment) mAdapterViewPager
				.getRegisteredFragment(MOVE_FRAGMENT_PAGE_INDEX);
		// if not found
		if (moveFragment == null) {
			moveFragment = new MoveFragment(this);
		}
		return moveFragment;
	}
}
