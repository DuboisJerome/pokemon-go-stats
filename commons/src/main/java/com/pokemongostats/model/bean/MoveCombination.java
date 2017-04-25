package com.pokemongostats.model.bean;

public class MoveCombination {
	private double attPPS = 0, defPPS = 0;
	private PkmnDesc pkmnDesc;
	private Move quickMove;
	private Move chargeMove;

	public MoveCombination(PkmnDesc pkmnDesc, Move quickMove, Move chargeMove) {
		if (quickMove == null
			|| chargeMove == null) { throw new IllegalArgumentException(
					"Quick move and charge move must not be null"); }
		this.pkmnDesc = pkmnDesc;
		this.quickMove = quickMove;
		this.chargeMove = chargeMove;
	}

	/**
	 * @return the pkmnDesc
	 */
	public PkmnDesc getPkmnDesc() {
		return pkmnDesc;
	}

	/**
	 * @return the quickMove
	 */
	public Move getQuickMove() {
		return quickMove;
	}

	/**
	 * @return the chargeMove
	 */
	public Move getChargeMove() {
		return chargeMove;
	}

	/**
	 * @return the attPPS
	 */
	public double getAttPPS() {
		return attPPS;
	}

	/**
	 * @param attPPS
	 *            the attPPS to set
	 */
	public void setAttPPS(double attPPS) {
		this.attPPS = attPPS;
	}

	/**
	 * @return the defPPS
	 */
	public double getDefPPS() {
		return defPPS;
	}

	/**
	 * @param defPPS
	 *            the defPPS to set
	 */
	public void setDefPPS(double defPPS) {
		this.defPPS = defPPS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + quickMove.hashCode();
		result = prime * result + chargeMove.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MoveCombination)) { return false; }
		MoveCombination other = (MoveCombination) obj;
		return quickMove.getId() == other.quickMove.getId()
			&& chargeMove.getId() == other.chargeMove.getId();
	}
}
