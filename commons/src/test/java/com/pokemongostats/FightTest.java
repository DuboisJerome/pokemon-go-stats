/**
 * 
 */
package com.pokemongostats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Pkmn;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 *
 */
public final class FightTest {

	@Before
	public void onBefore() {
	}

	@Test
	public void testIsSTAB() {
		final Move m = new Move();
		m.setType(Type.NORMAL);
		final PkmnDesc p = new PkmnDesc();
		p.setType1(Type.NORMAL);
		assertTrue(FightUtils.isSTAB(m, p));
	}

	@Test
	public void testComputeDPS() {
		final Move m = new Move();
		m.setType(Type.NORMAL);
		m.setDuration(1000); // 1000ms = 1s
		m.setMoveType(MoveType.QUICK);
		m.setPower(100);

		assertEquals(100, FightUtils.computePPS(m, null), 0.001);
	}

	@Test
	public void testComputeDPSStab() {
		final Move m = new Move();
		m.setType(Type.NORMAL);
		m.setDuration(1000); // 1000ms = 1s
		m.setMoveType(MoveType.QUICK);
		m.setPower(100);
		final PkmnDesc p = new PkmnDesc();
		p.setType1(Type.NORMAL);

		assertEquals(125, FightUtils.computePPS(m, p), 0.001);
	}

	@Test
	public void testComputeAttack() {
		final PkmnDesc voltali = new PkmnDesc();
		voltali.setType1(Type.ELECTRIC);
		voltali.setBaseAttack(232);

		final Pkmn p = new Pkmn();
		p.setLevel(40f);

		for (int i = 0; i <= 15; i++) {
			p.setAttackIV(i);
			// System.out.println(FightUtils.computeAttack(voltali, p));
		}
	}

	@Test
	public void testComputeDamage() {
		final PkmnDesc voltaliAtt = new PkmnDesc();
		voltaliAtt.setType1(Type.ELECTRIC);
		voltaliAtt.setBaseAttack(232);

		final Pkmn pAtt = new Pkmn();
		pAtt.setLevel(40f);
		pAtt.setAttackIV(15);

		final PkmnDesc voltaliDef = new PkmnDesc();
		voltaliDef.setType1(Type.ELECTRIC);
		voltaliDef.setBaseDefense(201);

		final Pkmn pDef = new Pkmn();
		pDef.setLevel(40f);
		pDef.setDefenseIV(10);

		final Move coupJus = new Move();
		coupJus.setType(Type.ELECTRIC);
		coupJus.setPower(65);

		for (int i = 0; i <= 15; i++) {
			pAtt.setAttackIV(i);
			System.out.println(FightUtils.computeDamage(coupJus, voltaliAtt,
					pAtt, voltaliDef, pDef));
		}
	}

	@Test
	public void testComputeHP() {
		final PkmnDesc aquali = new PkmnDesc();
		aquali.setType1(Type.WATER);
		aquali.setBaseStamina(260);

		final Pkmn p = new Pkmn();
		p.setLevel(31.5f);
		p.setStaminaIV(15);

		System.out.println(FightUtils.computeHP(aquali, p));
	}
}
