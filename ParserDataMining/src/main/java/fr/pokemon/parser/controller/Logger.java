package fr.pokemon.parser.controller;

import java.io.PrintStream;
import java.util.stream.Stream;

public class Logger {

    private static final boolean isDebug = true;
    private static final boolean isInfo = true;

    public static void info(Object... objects) {
        if (isInfo) {
            logOut(objects);
        }
    }

    public static void warn(Object... objects) {
        logErr(objects);
    }

    public static void error(Object... objects) {
        logErr(objects);
    }

    public static void debug(Object... objects) {
        if (isDebug) {
            logOut(objects);
        }
    }

    private static void logOut(Object... objects) {
        log(System.out, objects);
    }

    private static void logErr(Object... objects) {
        log(System.err, objects);
    }

    private static void log(PrintStream p, Object... objects) {
        p.println(String.join(" ; ", Stream.of(objects).map(o -> o != null ? o.toString() : "null").toList()));
    }
}
