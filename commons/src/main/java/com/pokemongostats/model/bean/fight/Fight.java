package com.pokemongostats.model.bean.fight;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.fight.Fighter.OnLaunchMoveListener;

public class Fight {

	public static final int MAX_TIME = 99000;

	private List<OnAttackListener> listAttackListener = new ArrayList<OnAttackListener>();
	private int clock = MAX_TIME;

	public void simulate(final Pkmn pAtt, final Pkmn pDef) {
		clock = MAX_TIME;
		final Attacker att = new Attacker(pAtt);
		final Defender def = new Defender(pDef);

		OnLaunchMoveListener launchMoveListener = new OnLaunchMoveListener() {
			@Override
			public void onLaunchMove(Fighter att, Fighter def, Move m) {
				// TODO Auto-generated method stub
				for (OnAttackListener l : listAttackListener) {
					l.onAttack(clock, att, def, m);
				}
			}
		};

		att.setEnnemy(def);
		att.addOnLaunchMoveListener(launchMoveListener);
		def.setEnnemy(att);
		def.addOnLaunchMoveListener(launchMoveListener);

		for (clock = MAX_TIME; clock > 0 && !att.isKO()
			&& !def.isKO(); clock--) {
			att.tick();
			def.tick();
		}
	}

	public void addOnAttackListener(final OnAttackListener l) {
		this.listAttackListener.add(l);
	}

	public void removeOnAttackListener(final OnAttackListener l) {
		this.listAttackListener.remove(l);
	}
}
