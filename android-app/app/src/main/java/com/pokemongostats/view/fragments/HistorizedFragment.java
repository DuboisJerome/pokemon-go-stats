package com.pokemongostats.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.model.commands.CompensableCommand;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 *            Bean representing all information of this fragment
 */
public abstract class HistorizedFragment<T> extends Fragment {

	protected T currentItem;

	protected View currentView;

	/**
	 * @return the currentItem
	 */
	public T getCurrentItem() {
		return currentItem;
	}

	/**
	 * @param currentItem
	 *            the currentItem to set
	 */
	public void setCurrentItem(T item) {
		currentItem = item;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("STATE", "onSaveInstanceState " + this.getClass().getName()
			+ " item: " + currentItem);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("STATE", "onActivityCreated " + this.getClass().getName()
			+ " item: " + currentItem);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("STATE", "onCreate " + this.getClass().getName() + " item: "
			+ currentItem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("STATE", "onCreateView " + this.getClass().getName() + " item: "
			+ currentItem);
		return v;
	}

	/**
	 * Change visible view with given item
	 * 
	 * @param item
	 */
	public void showItem(final T item) {
		if (item == null) { return; }

		CompensableCommand cmd = new ChangeItemCommand(item);
		Log.d("STATE", "showItem: " + cmd);
		cmd.execute();
		HistoryService.INSTANCE.add(cmd);

		updateView();
	}

	/**
	 * Update current view
	 */
	public final void updateView() {
		if (currentView == null) {
			Log.d("STATE", "updateView FAIL " + this.getClass().getName());
			return;
		} else {
			Log.d("STATE", "updateView OK " + this.getClass().getName());
		}
		updateViewImpl();
	}

	/**
	 * Update current view
	 */
	protected abstract void updateViewImpl();

	public class ChangeItemCommand implements CompensableCommand {
		private T lastItem, newItem;

		public ChangeItemCommand(T newItem) {
			this.lastItem = currentItem;
			this.newItem = newItem;
		}

		@Override
		public void execute() {
			Log.d("STATE", "=== Before execute " + this);
			currentItem = newItem;
			Log.d("STATE", "=== After execute " + this);
		}

		@Override
		public void compensate() {
			Log.d("STATE", "=== Before compensate " + this);
			currentItem = lastItem;
			Log.d("STATE", "=== After compensate " + this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ChangeItemCommand [lastItem=" + lastItem + ", newItem="
				+ newItem + "]";
		}

	}
}
