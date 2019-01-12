package com.pokemongostats.model.bean;

public class MoveCombination {
	private double attPPS = 0, defPPS = 0;
	private PkmnDesc pkmnDesc;
	private Move quickMove;
	private Move chargeMove;

	protected MoveCombination() {}

	public MoveCombination(final PkmnDesc pkmnDesc, final Move quickMove, final Move chargeMove) {
		if (quickMove == null || chargeMove == null) { throw new IllegalArgumentException(
				"Quick move and charge move must not be null"); }
		this.pkmnDesc = pkmnDesc;
		this.quickMove = quickMove;
		this.chargeMove = chargeMove;
	}

	/**
	 * @return the pkmnDesc
	 */
	public PkmnDesc getPkmnDesc() {
		return this.pkmnDesc;
	}

	/**
	 * @param pkmnDesc
	 *            the pkmnDesc to set
	 */
	public void setPkmnDesc(final PkmnDesc pkmnDesc) {
		this.pkmnDesc = pkmnDesc;
	}

	/**
	 * @return the quickMove
	 */
	public Move getQuickMove() {
		return this.quickMove;
	}

	/**
	 * @param quickMove
	 *            the quickMove to set
	 */
	public void setQuickMove(final Move quickMove) {
		this.quickMove = quickMove;
	}

	/**
	 * @return the chargeMove
	 */
	public Move getChargeMove() {
		return this.chargeMove;
	}

	/**
	 * @param chargeMove
	 *            the chargeMove to set
	 */
	public void setChargeMove(final Move chargeMove) {
		this.chargeMove = chargeMove;
	}

	/**
	 * @return the attPPS
	 */
	public double getAttPPS() {
		return this.attPPS;
	}

	/**
	 * @param attPPS
	 *            the attPPS to set
	 */
	public void setAttPPS(final double attPPS) {
		this.attPPS = attPPS;
	}

	/**
	 * @return the defPPS
	 */
	public double getDefPPS() {
		return this.defPPS;
	}

	/**
	 * @param defPPS
	 *            the defPPS to set
	 */
	public void setDefPPS(final double defPPS) {
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
		result = prime * result + this.quickMove.hashCode();
		result = prime * result + this.chargeMove.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof MoveCombination)) { return false; }
		final MoveCombination other = (MoveCombination) obj;
		return this.quickMove.getId() == other.quickMove.getId() && this.chargeMove.getId() == other.chargeMove.getId();
	}
}
