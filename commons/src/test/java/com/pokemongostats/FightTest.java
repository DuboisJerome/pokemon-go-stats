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
import com.pokemongostats.model.bean.fight.Fight;
import com.pokemongostats.model.bean.fight.Fighter;
import com.pokemongostats.model.bean.fight.OnAttackListener;

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

		final Move coupJus = new Move();
		coupJus.setType(Type.ELECTRIC);
		coupJus.setPower(65);

		final Pkmn pAtt = new Pkmn();
		pAtt.setLevel(40f);
		pAtt.setAttackIV(15);
		pAtt.setDesc(voltaliAtt);
		pAtt.setChargeMove(coupJus);

		final PkmnDesc voltaliDef = new PkmnDesc();
		voltaliDef.setType1(Type.ELECTRIC);
		voltaliDef.setBaseDefense(201);

		final Pkmn pDef = new Pkmn();
		pDef.setLevel(40f);
		pDef.setDefenseIV(10);
		pDef.setDesc(voltaliDef);

		for (int i = 0; i <= 15; i++) {
			pAtt.setAttackIV(i);
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
		p.setDesc(aquali);
	}

	@Test
	public void testSimulate() {
		final PkmnDesc attDesc = new PkmnDesc();
		attDesc.setBaseAttack(205);
		attDesc.setBaseDefense(177);
		attDesc.setBaseStamina(260);
		attDesc.setName("Aquali att");
		attDesc.setType1(Type.WATER);

		final PkmnDesc defDesc = new PkmnDesc();
		defDesc.setBaseAttack(205);
		defDesc.setBaseDefense(177);
		defDesc.setBaseStamina(260);
		defDesc.setName("Aquali def");
		defDesc.setType1(Type.WATER);

		final Move quickMove = new Move();
		quickMove.setPower(5);
		quickMove.setMoveType(MoveType.QUICK);
		quickMove.setType(Type.WATER);
		quickMove.setDuration(500);
		quickMove.setEnergyDelta(5);
		quickMove.setName("Water gun");

		final Move chargeMove = new Move();
		chargeMove.setPower(130);
		chargeMove.setMoveType(MoveType.QUICK);
		chargeMove.setType(Type.WATER);
		chargeMove.setDuration(3300);
		chargeMove.setEnergyDelta(-100);
		chargeMove.setName("Hydro pump");

		final Pkmn pAtt = new Pkmn();
		pAtt.setAttackIV(15);
		pAtt.setDefenseIV(15);
		pAtt.setStaminaIV(15);
		pAtt.setLevel(30);
		pAtt.setQuickMove(quickMove);
		pAtt.setChargeMove(chargeMove);
		pAtt.setDesc(attDesc);

		final Pkmn pDef = new Pkmn();
		pDef.setAttackIV(15);
		pDef.setDefenseIV(15);
		pDef.setStaminaIV(15);
		pDef.setLevel(30);
		pDef.setQuickMove(quickMove);
		pDef.setChargeMove(chargeMove);
		pDef.setDesc(defDesc);

		Fight f = new Fight();
		f.addOnAttackListener(new OnAttackListener() {
			@Override
			public void onAttack(final int time, final Fighter att,
					final Fighter def, final Move m) {
				double dmg = FightUtils.computeDamage(m.getMoveType(),
						att.getPkmn(), def.getPkmn());
				StringBuilder sb = new StringBuilder();
				sb.append(time).append("ms : ")
						.append(att.getPkmn().getDesc().getName())
						.append(" deal ").append(dmg).append(" dmg to ")
						.append(def.getPkmn().getDesc().getName())
						.append(" with ").append(m.getName());

				System.out.println(sb.toString());
			}
		});

		f.simulate(pAtt, pDef);
	}
}
