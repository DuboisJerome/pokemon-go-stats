package com.pokemongostats.view.fragments.switcher;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.asynctask.SelectAsyncTask;
import com.pokemongostats.controller.db.pokemon.PkmnTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.bean.Pokemon;
import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.model.table.PokemonTable;
import com.pokemongostats.model.table.TrainerTable;
import com.pokemongostats.view.activities.CustomAppCompatActivity;
import com.pokemongostats.view.fragments.SelectPkmnFragment;
import com.pokemongostats.view.fragments.SelectTrainerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * @author Zapagon
 *
 */
public class AddPkmnToGymFragmentSwitcher extends FragmentSwitcher
		implements
			SelectTrainerFragment.SelectTrainerFragmentListener,
			SelectPkmnFragment.OnPokemonSelectedListener {

	private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

	private Fragment currentFragment = null;

	public AddPkmnToGymFragmentSwitcher(
			final CustomAppCompatActivity activity) {
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
			SelectTrainerFragment selectTrainerFragment = (SelectTrainerFragment) fm
					.findFragmentByTag(SelectTrainerFragment.class.getName());
			if (selectTrainerFragment == null) {
				selectTrainerFragment = new SelectTrainerFragment(this);
				replaceCurrentFragment(fTransaction, selectTrainerFragment,
						SelectTrainerFragment.class.getName());
			} else {
				// fragment found in current activity or backstack
				// pop back stack then fragment is now in activity
				fm.popBackStackImmediate(SelectTrainerFragment.class.getName(),
						0);
				// show fragment
				fTransaction.show(selectTrainerFragment);
			}

			// populate selectTrainerFragment with all trainers in database
			final SelectTrainerFragment finalSelectTrainerFragment = selectTrainerFragment;
			new GetAllAsyncTask<Trainer>() {

				@Override
				protected List<Trainer> doInBackground(Long... params) {
					final TrainerTableDAO dao = new TrainerTableDAO(
							getFragmentActivity().getApplicationContext());
					if (params == null || params.length <= 0) {
						return dao.selectAll();
					} else {
						return dao.selectAllIn(TrainerTable.ID, false, params);
					}
				}

				@Override
				public void onPostExecute(List<Trainer> list) {
					finalSelectTrainerFragment.updateTrainersSpinner(list);
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

	@Override
	public void onTrainerSelected(final Trainer trainer) {
		if (trainer == null) { return; }

		// switch to SelectPokemonTrainerFragment
		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

		FragmentTransaction fTransaction = fm.beginTransaction();

		// animations
		// fTransaction.setCustomAnimations(R.anim.anim_push_left_in,
		// R.anim.anim_push_left_out,
		// R.anim.anim_push_left_in,
		// R.anim.anim_push_left_out);

		// find fragment
		SelectPkmnFragment selectPokemonFragment = (SelectPkmnFragment) fm
				.findFragmentByTag(SelectPkmnFragment.class.getName());
		// if fragment already exist
		if (selectPokemonFragment == null) {
			selectPokemonFragment = new SelectPkmnFragment(this);
			// show new fragment
			replaceCurrentFragment(fTransaction, selectPokemonFragment,
					SelectPkmnFragment.class.getName());
		} else {
			// fragment found in current activity or backstack
			// pop back stack then fragment is now in activity
			fm.popBackStackImmediate(SelectPkmnFragment.class.getName(), 0);
			fTransaction.show(selectPokemonFragment);
		}

		// add to backstack then commit
		fTransaction.addToBackStack(null);
		fTransaction.commit();

		// retrieve trainer's pokemon
		StringBuilder b = new StringBuilder();
		b.append("SELECT * FROM ");
		b.append(PokemonTable.TABLE_NAME);
		b.append(" WHERE ");
		b.append(PokemonTable.OWNER_ID).append("=").append(trainer.getId());

		final SelectPkmnFragment finalSelectPokemonFragment = selectPokemonFragment;
		new SelectAsyncTask<Pokemon>() {

			@Override
			protected List<Pokemon> doInBackground(String... queries) {
				List<Pokemon> result = new ArrayList<Pokemon>();
				if (queries == null || queries.length <= 0) { return result; }
				PkmnTableDAO dao = new PkmnTableDAO(
						getFragmentActivity().getApplicationContext());

				for (String query : queries) {
					if (query == null) {
						continue;
					}
					final List<Pokemon> list = dao.selectAll(query);
					if (list != null) {
						result.addAll(list);
					}
				}
				return result;
			}

			@Override
			public void onPostExecute(List<Pokemon> pokemons) {
				finalSelectPokemonFragment.updatePokemonSpinner(pokemons);
			}
		}.execute(b.toString());
	}

	@Override
	public void onPokemonSelected(Pokemon p) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void replaceCurrentFragment(final FragmentTransaction fTransaction,
			final Fragment newFragment, final String fragmentTag) {
		fTransaction.replace(R.id.fragment_container, newFragment, fragmentTag);
		currentFragment = newFragment;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}
}
