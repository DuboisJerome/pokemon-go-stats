package com.pokemongostats.model.bean.fight;

import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Pkmn;

/**
 *
 * @author Zapagon
 *
 */
public class Defender extends Fighter {
	private int cdWait;

	public Defender(final Pkmn def) {
		super(def);
		// this.hp = FightUtils.computeHP(def) * 2;
		this.cdWait = 1000;
	}

	@Override
	public void tick() {
		this.cdWait -= TICK;
		super.tick();
	}

	@Override
	protected void doAction() {
		if (this.cdWait < 1) {
			super.doAction();
		}
	}

	@Override
	protected void launchMove(final MoveType moveType) {
		super.launchMove(moveType);
		if (MoveType.QUICK.equals(moveType)) {
			this.cdWait = this.nbAttack < 2 ? 1000 : 2000;
		}
	}

	@Override
	protected double getEnergyMax() {
		return 200;
	}
}