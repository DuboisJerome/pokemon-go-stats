package com.pokemongostats.model.bean.fight;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Pkmn;

public abstract class Fighter {
	protected static final int TICK = 1;// ms

	protected Pkmn pkmn;
	protected Fighter ennemy;
	protected double hp;
	protected double cdQuickMove;
	protected double cdChargeMove;
	protected double energy;
	protected int nbAttack;

	protected List<OnLaunchMoveListener> listAttackListener = new ArrayList<OnLaunchMoveListener>();

	protected Fighter(final Pkmn p) {
		this.pkmn = p;
		this.cdQuickMove = 0;
		this.cdChargeMove = 0;
		this.energy = 0;
	}

	public Pkmn getPkmn() {
		return pkmn;
	}

	public Fighter getEnnemy() {
		return ennemy;
	}

	public void setEnnemy(Fighter ennemy) {
		this.ennemy = ennemy;
	}

	public void tick() {
		doAction();
		cdQuickMove -= TICK;
		cdChargeMove -= TICK;
	}

	protected void doAction() {
		if (cdQuickMove < 1 && cdChargeMove < 1) {
			if (energy >= Math.abs(pkmn.getChargeMove().getEnergyDelta())) {
				launchMove(MoveType.CHARGE);
			} else {
				launchMove(MoveType.QUICK);
			}
		}
	}

	protected void launchMove(MoveType moveType) {
		double dmg = FightUtils.computeDamage(moveType, pkmn, ennemy.getPkmn());
		ennemy.receiveDmg(dmg);

		Move m = MoveType.CHARGE.equals(moveType)
				? pkmn.getChargeMove()
				: pkmn.getQuickMove();
		// update cooldown
		cdQuickMove = m.getDuration();
		// update energy
		modifyEnergy(m.getEnergyDelta());
		nbAttack++;

		for (OnLaunchMoveListener l : listAttackListener) {
			l.onLaunchMove(this, ennemy, m);
		}
	}

	protected abstract double getEnergyMax();

	public void receiveDmg(final double dmg) {
		hp -= dmg;
		modifyEnergy(0.5 * dmg);
	}

	public boolean isKO() {
		return hp < 0;
	}

	private void modifyEnergy(final double delta) {
		this.energy += delta;
		if (this.energy < 0) {
			this.energy = 0;
		}
		if (this.energy > getEnergyMax()) {
			this.energy = getEnergyMax();
		}
	}

	public void addOnLaunchMoveListener(final OnLaunchMoveListener l) {
		this.listAttackListener.add(l);
	}

	public void removeOnLaunchMoveListener(final OnLaunchMoveListener l) {
		this.listAttackListener.remove(l);
	}

	public interface OnLaunchMoveListener {
		void onLaunchMove(final Fighter att, final Fighter def, final Move m);
	}

	public double getHP() {
		return hp;
	}

	public double getEnergy() {
		return energy;
	}
}