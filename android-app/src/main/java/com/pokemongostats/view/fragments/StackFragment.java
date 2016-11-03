package com.pokemongostats.view.fragments;

import java.util.Stack;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * 
 * @author Zapagon
 *
 * @param <T>
 *            Bean representing all information of this fragment
 */
public abstract class StackFragment<T> extends Fragment {

	protected T currentItem;

	// list of successively visited items of this stack fragment
	// ex: show item A -> show item B -> back = show item A
	protected Stack<T> currentHistory = new Stack<T>();
	// list of list of successively visited items of this stack fragment
	// ex: show item A -> show item B -> change fragment
	// (fragmentStateHistory.add(currentHistory) -> back = show item B (last top
	// history item)
	protected Stack<Stack<T>> fragmentStateHistory = new Stack<Stack<T>>();

	/** @return the history */
	public Stack<T> getCurrentHistory() {
		return currentHistory;
	}

	/** @return current visible item */
	public T getCurrentItem() {
		return currentItem;
	}

	/**
	 * Change visible view with given item
	 * 
	 * @param item
	 */
	public void changeViewWithItem(final T item) {
		if (item == null) { return; }
		if (currentItem != null) {
			currentHistory.add(currentItem);
			Log.d("STACK", "changeViewWithItem : " + currentHistory);
		}
		currentItem = item;
		if (getView() != null) {
			updateView(item);
		}
	}

	/**
	 * same as calling updateView(currentItem)
	 */
	protected void updateView() {
		if (currentItem != null && getView() != null) {
			updateView(currentItem);
		}
	}

	/**
	 * Update current view with given item
	 * 
	 * @param item
	 */
	protected abstract void updateView(final T item);

	/**
	 * Back on currentHistory
	 * 
	 * @return isbacked (false if current history is empty)
	 */
	public boolean back() {
		if (currentHistory.isEmpty()) { return false; }
		Log.d("STACK", "Back from " + currentItem);
		currentItem = currentHistory.pop();
		Log.d("STACK", "To " + currentItem);
		updateView();
		Log.d("STACK", "Current History " + currentHistory);
		return true;
	}

	/**
	 * Must be call when leaving this fragment. Save current history stack
	 */
	public void addNewHistory() {
		if (currentItem != null) {
			currentHistory.add(currentItem);
		}
		fragmentStateHistory.add(currentHistory);
		currentHistory = new Stack<T>();
		Log.d("STACK", "addNewHistoryG : " + fragmentStateHistory);
		Log.d("STACK", "addNewHistory : " + currentHistory);
	}

	/**
	 * Must be call when backing to this fragment (state). Pop last history
	 * stack
	 * 
	 * @return currentHistory popped
	 */
	public Stack<T> popHistory() {
		if (!fragmentStateHistory.isEmpty()) {
			currentHistory = fragmentStateHistory.pop();
			if (currentHistory != null && !currentHistory.isEmpty()) {
				currentItem = currentHistory.pop();
			}
			updateView();
			Log.d("STACK", "Popped history : " + currentHistory);
			Log.d("STACK", "Popped Current item : " + currentItem);
		}
		return currentHistory;
	}
}
