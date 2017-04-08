package com.pokemongostats.model.bean;

import java.io.Serializable;

/**
 * Business object representing a real pokemon possibly own by a trainer
 * 
 * @author Zapagon
 *
 */
public class Pkmn implements HasID, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4409081291501324263L;

	private long id = HasID.NO_ID;

	private PkmnDesc desc;

	private int CP;

	private int defenseIV;

	private int attackIV;

	private int staminaIV;

	private float level;

	private Trainer owner;

	private String nickname;

	private Move quickMove, chargeMove;

	/**
	 * @return the desc
	 */
	public PkmnDesc getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(PkmnDesc desc) {
		this.desc = desc;
	}

	/**
	 * @return the cP
	 */
	public int getCP() {
		return CP;
	}

	/**
	 * @param cP
	 *            the cP to set
	 */
	public void setCP(int cP) {
		CP = cP;
	}

	/**
	 * @return the defenseIV
	 */
	public int getDefenseIV() {
		return defenseIV;
	}

	/**
	 * @param defenseIV
	 *            the defenseIV to set
	 */
	public void setDefenseIV(int defenseIV) {
		this.defenseIV = defenseIV;
	}

	/**
	 * @return the attackIV
	 */
	public int getAttackIV() {
		return attackIV;
	}

	/**
	 * @param attackIV
	 *            the attackIV to set
	 */
	public void setAttackIV(int attackIV) {
		this.attackIV = attackIV;
	}

	/**
	 * @return the staminaIV
	 */
	public int getStaminaIV() {
		return staminaIV;
	}

	/**
	 * @param staminaIV
	 *            the staminaIV to set
	 */
	public void setStaminaIV(int staminaIV) {
		this.staminaIV = staminaIV;
	}

	/**
	 * @return the level
	 */
	public float getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(float level) {
		this.level = level;
	}

	/**
	 * @return the owner
	 */
	public Trainer getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Trainer owner) {
		this.owner = owner;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the quickMove
	 */
	public Move getQuickMove() {
		return quickMove;
	}

	/**
	 * @param quickMove
	 *            the quickMove to set
	 */
	public void setQuickMove(Move quickMove) {
		this.quickMove = quickMove;
	}

	/**
	 * @return the chargeMove
	 */
	public Move getChargeMove() {
		return chargeMove;
	}

	/**
	 * @param chargeMove
	 *            the chargeMove to set
	 */
	public void setChargeMove(Move chargeMove) {
		this.chargeMove = chargeMove;
	}
}
