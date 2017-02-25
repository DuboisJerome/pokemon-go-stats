package com.pokemongostats.model.commands;

@FunctionalInterface
public interface Command {
	public void execute();
}