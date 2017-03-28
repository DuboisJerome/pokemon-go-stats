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

	private long pokedexNum;

	private int CP;

	private int HP;

	private int defenseIV;

	private int attackIV;

	private int staminaIV;

	private float level;

	private Trainer owner;

	private String nickname;

	/**
	 * @return the pokedexNum
	 */
	public long getPokedexNum() {
		return pokedexNum;
	}

	/**
	 * @param pokedexNum
	 *            the pokedexNum to set
	 */
	public void setPokedexNum(long pokedexNum) {
		this.pokedexNum = pokedexNum;
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
	 * @return the hP
	 */
	public int getHP() {
		return HP;
	}

	/**
	 * @param hP
	 *            the hP to set
	 */
	public void setHP(int hP) {
		HP = hP;
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
}
