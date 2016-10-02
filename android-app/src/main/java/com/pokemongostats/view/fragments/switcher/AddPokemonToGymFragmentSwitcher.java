package com.pokemongostats.view.fragments.switcher;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.controller.asynctask.GetAllAsyncTask;
import com.pokemongostats.controller.asynctask.SelectAsyncTask;
import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.Trainer;
import com.pokemongostats.view.fragments.SelectPokemonFragment;
import com.pokemongostats.view.fragments.SelectTrainerFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * @author Zapagon
 *
 */
public class AddPokemonToGymFragmentSwitcher extends FragmentSwitcher
		implements SelectTrainerFragment.SelectTrainerFragmentListener, SelectPokemonFragment.OnPokemonSelectedListener {

	public AddPokemonToGymFragmentSwitcher(final FragmentActivity activity) {
		super(activity);

		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();
		// find if fragment is in backtack
		SelectTrainerFragment selectTrainerFragment = (SelectTrainerFragment) fm
				.findFragmentByTag(SelectTrainerFragment.class.getName());

		FragmentTransaction fTransaction = fm.beginTransaction();
		if (selectTrainerFragment == null) {
			selectTrainerFragment = new SelectTrainerFragment(this);
			selectTrainerFragment.setArguments(getFragmentActivity().getIntent().getExtras());
			fTransaction.add(R.id.fragment_container, selectTrainerFragment).commit();
		} else {
			// fragment found in current activity or backstack
			// pop back stack then fragment is now in activity
			fm.popBackStackImmediate(SelectTrainerFragment.class.getName(), 0);
			// show fragment
			fTransaction.show(selectTrainerFragment);
		}

		// populate selectTrainerFragment with all trainers in database
		final SelectTrainerFragment finalSelectTrainerFragment = selectTrainerFragment;
		new GetAllAsyncTask<Trainer>() {

			@Override
			protected List<Trainer> doInBackground(Long... params) {
				return new TrainerTableDAO(getFragmentActivity()).selectAll(params);
			}

			@Override
			public void onPostExecute(List<Trainer> list) {
				finalSelectTrainerFragment.updateTrainersSpinner(list);
			}
		}.execute();
	}

	@Override
	public void onTrainerSelected(final Trainer trainer) {
		if (trainer == null) {
			return;
		}

		// switch to SelectPokemonTrainerFragment
		FragmentManager fm = getFragmentActivity().getSupportFragmentManager();

		FragmentTransaction fTransaction = fm.beginTransaction();

		// animations
		// fTransaction.setCustomAnimations(R.anim.anim_push_left_in,
		// R.anim.anim_push_left_out,
		// R.anim.anim_push_left_in,
		// R.anim.anim_push_left_out);

		// find fragment
		SelectPokemonFragment selectPokemonFragment = (SelectPokemonFragment) fm
				.findFragmentByTag(SelectPokemonFragment.class.getName());
		// if fragment already exist
		if (selectPokemonFragment == null) {
			selectPokemonFragment = new SelectPokemonFragment(this);
			selectPokemonFragment.setArguments(getFragmentActivity().getIntent().getExtras());
			// show new fragment
			fTransaction.replace(R.id.fragment_container, selectPokemonFragment, SelectPokemonFragment.class.getName());
		} else {
			// fragment found in current activity or backstack
			// pop back stack then fragment is now in activity
			fm.popBackStackImmediate(SelectPokemonFragment.class.getName(), 0);
			// Le fragment existe déjà, il vous suffit de l'afficher
			fTransaction.show(selectPokemonFragment);
		}

		// add to backstack then commit
		fTransaction.addToBackStack(null);
		fTransaction.commit();

		// retrieve trainer's pokemon
		StringBuilder b = new StringBuilder();
		b.append("SELECT * FROM ");
		b.append(PokemonTableDAO.TABLE_NAME);
		b.append(" WHERE ");
		b.append(PokemonTableDAO.OWNER_ID).append("=").append(trainer.getId());

		final SelectPokemonFragment finalSelectPokemonFragment = selectPokemonFragment;
		new SelectAsyncTask<Pokemon>() {

			@Override
			protected List<Pokemon> doInBackground(String... queries) {
				List<Pokemon> result = new ArrayList<Pokemon>();
				if (queries == null || queries.length <= 0) {
					return result;
				}
				PokemonTableDAO dao = new PokemonTableDAO(getFragmentActivity().getApplicationContext());

				for (String query : queries) {
					if (query == null) {
						continue;
					}
					final List<Pokemon> list = dao.select(query);
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

}
