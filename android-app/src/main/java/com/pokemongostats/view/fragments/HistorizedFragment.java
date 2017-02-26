package com.pokemongostats.view.fragments;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.model.commands.CompensableCommand;

import android.support.v4.app.Fragment;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 *            Bean representing all information of this fragment
 */
public abstract class HistorizedFragment<T> extends Fragment {

	public class HistorizedFragmentCommand implements CompensableCommand {

		private T lastItem, newItem;

		public HistorizedFragmentCommand(T lastItem, T newItem) {
			this.lastItem = lastItem;
			this.newItem = newItem;
		}

		@Override
		public void execute() {
			// Log.d("HIST", "hide " + currentItem + " show " + newItem);
			currentItem = newItem;
			updateView();
		}

		@Override
		public void compensate() {
			// Log.d("HIST", "hide " + currentItem + " show " + lastItem);
			// back to last item
			currentItem = lastItem;
			updateView();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HistorizedFragmentCommand [lastItem=" + lastItem + ", newItem=" + newItem + "]";
		}

	}

	protected T currentItem;

	public HistorizedFragment() {}

	/**
	 * Change visible view with given item
	 * 
	 * @param item
	 */
	public void showItem(final T item) {
		if (item == null) { return; }

		CompensableCommand cmd = createCommand(item);
		cmd.execute();
		HistoryService.INSTANCE.add(cmd);
	}

	/**
	 * Update current view
	 */
	protected abstract void updateView();

	public CompensableCommand createCommand(final T item) {
		return new HistorizedFragmentCommand(currentItem, item);
	}
}
