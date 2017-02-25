package com.pokemongostats.model.commands;

import java.util.ArrayList;

public class MacroCompensableCommand implements CompensableCommand {

	private final ArrayList<CompensableCommand> commands = new ArrayList<CompensableCommand>();

	public void addCmd(final CompensableCommand cmd) {
		commands.add(cmd);
	}

	@Override
	public void execute() {
		if (commands == null) { return; }
		for (int i = 0; i < commands.size(); ++i) {
			commands.get(i).execute();
		}
	}

	@Override
	public void compensate() {
		if (commands == null) { return; }
		for (int i = commands.size() - 1; i >= 0; --i) {
			commands.get(i).compensate();
		}
	}
}