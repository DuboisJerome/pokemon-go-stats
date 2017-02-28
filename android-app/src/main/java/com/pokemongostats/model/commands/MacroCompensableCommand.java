package com.pokemongostats.model.commands;

import java.util.ArrayList;

import android.util.Log;

public class MacroCompensableCommand implements CompensableCommand {

	private final ArrayList<CompensableCommand> commands = new ArrayList<CompensableCommand>();

	public void addCmd(final CompensableCommand cmd) {
		commands.add(cmd);
	}

	@Override
	public void execute() {
		Log.d("STATE", "===== Before execute macro");
		if (commands == null) { return; }
		for (int i = 0; i < commands.size(); ++i) {
			commands.get(i).execute();
		}
		Log.d("STATE", "===== After execute macro");
	}

	@Override
	public void compensate() {
		Log.d("STATE", "===== Before compensate macro");
		if (commands == null) { return; }
		for (int i = commands.size() - 1; i >= 0; --i) {
			commands.get(i).compensate();
		}
		Log.d("STATE", "===== After compensate macro");
	}
}