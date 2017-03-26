/**
 *
 */
package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public final class MoveUtils {

    private MoveUtils() {
    }

    public static double calculerDPS(final Move m) {
        if (m == null) {
            return 0.0;
        }
        double duration = m.getDuration();
        if (MoveType.CHARGE.equals(m.getMoveType())) {
            duration += 300;
        }
        return (duration > 0) ? m.getPower() / (duration / 1000.0) : 0d;
    }

    public static double calculerDPS(final Move m, final PokemonDescription owner) {
        double dps = calculerDPS(m);
        dps = isSTAB(m, owner) ? dps * 1.25 : dps;
        return Math.floor(dps * 100) / 100;
    }

    public static boolean isSTAB(final Move m, final PokemonDescription owner) {
        if (m == null || owner == null) {
            return false;
        }
        Type type = m.getType();
        return type.equals(owner.getType1())
                || type.equals(owner.getType2());
    }
}
