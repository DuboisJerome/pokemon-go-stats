package com.pokemongostats.view.fragments.switcher;

import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.table.PokedexTable;
import com.pokemongostats.view.fragments.PokedexFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * @author Zapagon
 *
 */
public class PokedexFragmentSwitcher extends FragmentSwitcher
		implements
			PokedexFragment.PokedexFragmentListener {

	private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

	private Fragment currentFragment = null;

	public PokedexFragmentSwitcher(final FragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
		FragmentTransaction fTransaction = fm.beginTransaction();
		if (savedInstanceState != null) {
			// Restore the fragment's instance
			currentFragment = fm.getFragment(savedInstanceState,
					CURRENT_FRAGMENT_KEY);

			replaceCurrentFragment(fTransaction, currentFragment,
					currentFragment.getTag());
		} else {
			// find if fragment is in backtack
			PokedexFragment pokedexFragment = (PokedexFragment) fm
					.findFragmentByTag(PokedexFragment.class.getName());
			if (pokedexFragment == null) {
				pokedexFragment = new PokedexFragment(this);
				replaceCurrentFragment(fTransaction, pokedexFragment,
						PokedexFragment.class.getName());
			} else {
				// fragment found in current activity or backstack
				// pop back stack then fragment is now in activity
				fm.popBackStackImmediate(PokedexFragment.class.getName(), 0);
				// show fragment
				fTransaction.show(pokedexFragment);
			}

			// populate selectTrainerFragment with all trainers in database
			final PokedexFragment finalPokedexFragment = pokedexFragment;
			new GetAllAsyncTask<PokemonDescription>() {

				@Override
				protected List<PokemonDescription> doInBackground(
						Long... params) {
					final PokedexTableDAO dao = new PokedexTableDAO(
							getFragmentActivity().getApplicationContext());
					if (params == null || params.length <= 0) {
						return dao.selectAll();
					} else {
						return dao.selectAllIn(PokedexTable.ID, false, params);
					}
				}

				@Override
				public void onPostExecute(List<PokemonDescription> list) {
					finalPokedexFragment.updatePokedexSpinner(list);
				}
			}.execute();
		}

		fTransaction.commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
		fm.putFragment(outState, CURRENT_FRAGMENT_KEY, currentFragment);
	}

	/**
	 * 
	 */
	private void replaceCurrentFragment(final FragmentTransaction fTransaction,
			final Fragment newFragment, final String fragmentTag) {
		fTransaction.replace(R.id.fragment_container, newFragment, fragmentTag);
		currentFragment = newFragment;
	}
}
