package com.pokemongostats.model.commands;

import android.util.Log;

import com.pokemongostats.controller.utils.TagUtils;

import java.util.ArrayList;

public class MacroCompensableCommand implements CompensableCommand {

	private final ArrayList<CompensableCommand> commands = new ArrayList<CompensableCommand>();

	public void addCmd(final CompensableCommand cmd) {
		commands.add(cmd);
	}

	@Override
	public void execute() {
		Log.d(TagUtils.HIST, "===== Before execute macro");
		if (commands == null) { return; }
		for (int i = 0; i < commands.size(); ++i) {
			commands.get(i).execute();
		}
		Log.d(TagUtils.HIST, "===== After execute macro");
	}

	@Override
	public void compensate() {
		Log.d(TagUtils.HIST, "===== Before compensate macro");
		if (commands == null) { return; }
		for (int i = commands.size() - 1; i >= 0; --i) {
			commands.get(i).compensate();
		}
		Log.d(TagUtils.HIST, "===== After compensate macro");
	}
}