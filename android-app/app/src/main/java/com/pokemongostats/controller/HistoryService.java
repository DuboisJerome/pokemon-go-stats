/**
 *
 */
package com.pokemongostats.controller;

import com.pokemongostats.model.commands.CompensableCommand;
import com.pokemongostats.model.commands.MacroCompensableCommand;

import java.util.LinkedList;

/**
 * @author Zapagon
 */
public class HistoryService<C extends CompensableCommand> {

    // private static final int MAX_CAPACITY = 10;
    public static final HistoryService<CompensableCommand> INSTANCE = new HistoryService<CompensableCommand>();

    private final LinkedList<CompensableCommand> history = new LinkedList<CompensableCommand>();

    private MacroCompensableCommand macro;

    private boolean isBacking = false;

    private boolean isMacro = false;

    public boolean back() {
        if (history.isEmpty()) {
            return false;
        }

        // get cmd on the top of history
        CompensableCommand latestCmd = history.removeLast();
        if (latestCmd == null) {
            return false;
        }

        // reverse the cmd
        this.isBacking = true;
        latestCmd.compensate();
        this.isBacking = false;

        return true;
    }

    public void add(C cmd) {
        if (isMacro) {
            macro.addCmd(cmd);
        } else {
            history.addLast(cmd);
            // if (history.size() > MAX_CAPACITY) {
            // history.removeFirst();
            // }
        }
    }

    public CompensableCommand getLastCmd() {
        if (history.isEmpty()) {
            return null;
        }
        return history.peek();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    /**
     * @return the isBacking
     */
    public boolean isBacking() {
        return isBacking;
    }

    public void startMacro() {
        isMacro = true;
        macro = new MacroCompensableCommand();
    }

    public CompensableCommand stopMacro() {
        isMacro = false;
        return macro;
    }
}
