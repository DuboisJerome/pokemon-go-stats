package com.pokemongostats.view.fragments;

import java.util.Stack;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class StackFragment<T> extends Fragment {

	protected T currentItem;

	protected Stack<T> currentHistory = new Stack<T>();
	protected Stack<Stack<T>> fragmentStateHistory = new Stack<Stack<T>>();

	/**
	 * @return the history
	 */
	public Stack<T> getCurrentHistory() {
		return currentHistory;
	}

	public T getCurrentItem() {
		return currentItem;
	}

	public boolean back() {
		if (currentHistory.isEmpty()) { return false; }
		T previonsCurrent = currentItem;
		currentItem = currentHistory.pop();
		updateView();
		Log.d("STACK", "back " + previonsCurrent + " to " + currentItem);
		Log.d("STACK", "history " + currentHistory);
		return true;
	}

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

	protected void updateView() {
		if (currentItem != null && getView() != null) {
			updateView(currentItem);
		}
	}

	protected abstract void updateView(final T item);

	public void addNewHistory() {
		if (currentItem != null) {
			currentHistory.add(currentItem);
			currentItem = null;
		}
		fragmentStateHistory.add(currentHistory);
		currentHistory = new Stack<T>();
		Log.d("STACK", "addNewHistoryG : " + fragmentStateHistory);
		Log.d("STACK", "addNewHistory : " + currentHistory);
	}

	public Stack<T> popHistory() {
		if (!fragmentStateHistory.isEmpty()) {
			currentHistory = fragmentStateHistory.pop();
			if (currentHistory != null && !currentHistory.isEmpty()) {
				currentItem = currentHistory.pop();
			} else {
				currentItem = null;
			}
			updateView();
			Log.d("STACK", "popHistoryG : " + fragmentStateHistory);
			Log.d("STACK", "popHistory : " + currentHistory);
			Log.d("STACK", "current item : " + currentItem);
		}
		return currentHistory;
	}
}
