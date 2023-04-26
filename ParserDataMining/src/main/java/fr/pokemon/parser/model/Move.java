package fr.pokemon.parser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Move implements Serializable, Comparable<Move> {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -4372310325501132352L;

    public enum MoveType {
        QUICK, CHARGE, UNKNOWN;

        public static MoveType valueOfIgnoreCase(final String type) {
            if (type == null || type.isEmpty()) {
                return null;
            }
            try {
                return MoveType.valueOf(type.toUpperCase());
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
                return UNKNOWN;
            }
        }
    }

    public String templateId;
    public long id = -1;
    public String idStr;
    public Type type;
    public MoveType moveType;

    public int power;
    public double staminaLossScalar;
    public int duration;
    public int energyDelta;
    public double criticalChance;

    public int powerPvp;
    public int energyPvp;
    public int nbTurnPvp = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Move m) {
        if (m == null) {
            return 0;
        }
        return Long.compare(this.id, m.getId());
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Move other)) {
            return false;
        }
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }

    /**
     * @return #000 : Name [Type]
     */
    @Override
    public String toString() {
        return "Move{" +
                "templateId='" + templateId + '\'' +
                ", id=" + id +
                ", idStr='" + idStr + '\'' +
                ", type=" + type +
                ", moveType=" + moveType +
                ", power=" + power +
                ", staminaLossScalar=" + staminaLossScalar +
                ", duration=" + duration +
                ", energyDelta=" + energyDelta +
                ", criticalChance=" + criticalChance +
                ", powerPvp=" + powerPvp +
                ", energyPvp=" + energyPvp +
                ", nbTurnPvp=" + nbTurnPvp +
                '}';
    }
}