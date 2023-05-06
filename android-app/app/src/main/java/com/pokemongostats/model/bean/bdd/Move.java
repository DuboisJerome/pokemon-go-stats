package com.pokemongostats.model.bean.bdd;

import androidx.annotation.NonNull;

import com.pokemongostats.model.bean.Type;

import java.io.Serializable;
import java.util.Objects;

import fr.commons.generique.model.db.AbstractObjetBddAvecId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move extends AbstractObjetBddAvecId implements Serializable, Comparable<Move> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4372310325501132352L;

	public enum MoveType {
		QUICK, CHARGE, UNKNOWN;

		public static MoveType valueOfIgnoreCase(String type) {
			if (type == null || type.isEmpty()) {
				return null;
			}
			try {
				return MoveType.valueOf(type.toUpperCase());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return UNKNOWN;
			}
		}
	}

	private final MoveI18N i18n;
	private MoveType moveType;
	private Type type;
	private int power;
	private double staminaLossScalar;
	private int duration;
	private int energyDelta;
	private double criticalChance;
	private int powerPvp;
	private int energyPvp;
	private int durationPvp;

	public Move(){
		this.i18n = new MoveI18N();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.i18n.getName();
	}

	/**
	 * @return the moveType
	 */
	public MoveType getMoveType() {
		return this.moveType;
	}

	/**
	 * @param moveType the moveType to set
	 */
	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the power
	 */
	public int getPower() {
		return this.power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * @return the staminaLossScalar
	 */
	public double getStaminaLossScalar() {
		return this.staminaLossScalar;
	}

	/**
	 * @param staminaLossScalar the staminaLossScalar to set
	 */
	public void setStaminaLossScalar(double staminaLossScalar) {
		this.staminaLossScalar = staminaLossScalar;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the energyDelta
	 */
	public int getEnergyDelta() {
		return this.energyDelta;
	}

	/**
	 * @param energyDelta the energyDelta to set
	 */
	public void setEnergyDelta(int energyDelta) {
		this.energyDelta = energyDelta;
	}

	/**
	 * @return the criticalChance
	 */
	public double getCriticalChance() {
		return this.criticalChance;
	}

	/**
	 * @param criticalChance the criticalChance to set
	 */
	public void setCriticalChance(double criticalChance) {
		this.criticalChance = criticalChance;
	}

	/**
	 * @return #000 : Name [Type]
	 */
	@NonNull
	@Override
	public String toString() {
		return "#" + this.id + " : " + this.getName() + " [" + this.type.name() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Move m) {
		if (m == null || this.getName() == null) {
			return 0;
		}
		return this.getName().compareTo(m.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Move other)) {
			return false;
		}
		return this.id == other.id;
	}

	@Override
	public int hashCode() {
		return (int) this.id;
	}

	/**
	 * @return the powerPvp
	 */
	public int getPowerPvp() {
		return this.powerPvp;
	}

	/**
	 * @param powerPvp the powerPvp to set
	 */
	public void setPowerPvp(int powerPvp) {
		this.powerPvp = powerPvp;
	}

	/**
	 * @return the energyPvp
	 */
	public int getEnergyPvp() {
		return this.energyPvp;
	}

	/**
	 * @param energyPvp the energyPvp to set
	 */
	public void setEnergyPvp(int energyPvp) {
		this.energyPvp = energyPvp;
	}

	/**
	 * @return the durationPvp
	 */
	public int getDurationPvp() {
		return this.durationPvp;
	}

	/**
	 * @param durationPvp the durationPvp to set
	 */
	public void setDurationPvp(int durationPvp) {
		this.durationPvp = durationPvp;
	}

}