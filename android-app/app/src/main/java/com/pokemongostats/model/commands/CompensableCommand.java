/**
 *
 */
package com.pokemongostats.model.commands;

/**
 * @author Zapagon
 */
public interface CompensableCommand extends Command {
    void compensate();
}