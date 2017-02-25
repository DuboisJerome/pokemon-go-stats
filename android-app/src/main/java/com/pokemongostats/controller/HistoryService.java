/**
 * 
 */
package com.pokemongostats.controller;

import java.util.LinkedList;

import com.pokemongostats.model.commands.CompensableCommand;

/**
 * @author Zapagon
 *
 */
public class HistoryService<C extends CompensableCommand> {

	// private static final int MAX_CAPACITY = 10;
	public static final HistoryService<CompensableCommand> INSTANCE = new HistoryService<CompensableCommand>();

	private final LinkedList<CompensableCommand> history = new LinkedList<CompensableCommand>();

	private HistoryService() {}

	public boolean back() {
		if (history.isEmpty()) { return false; }

		CompensableCommand latestCmd = history.removeLast();
		if (latestCmd == null) { return false; }
		latestCmd.compensate();

		return true;
	}

	public void add(C cmd) {
		history.addLast(cmd);
		// if (history.size() > MAX_CAPACITY) {
		// history.removeFirst();
		// }
	}

	public CompensableCommand getLastCmd() {
		if (history.isEmpty()) { return null; }
		return history.peek();
	}

	public boolean isEmpty() {
		return history.isEmpty();
	}
}
