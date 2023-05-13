package com.pokemongostats.model.bean;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;

import java.text.DecimalFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnMoveComplet extends PkmnMove {
	private PkmnDesc owner;
	private Move move;

	public PkmnMoveComplet() {

	}

	public PkmnMoveComplet(PkmnMove pm) {
		setPokedexNum(pm.getPokedexNum());
		setForm(pm.getForm());
		setMoveId(pm.getMoveId());
		setElite(pm.isElite());
	}

	public String getName() {
		return this.move.getName() + (isElite() ? "*" : "");
	}

	public String getDpsArene() {
		return round(FightUtils.computePowerPerSecond(this.move, this.owner));
	}

	public String getEpsArene() {
		return round(FightUtils.computeEnergyPerSecond(this.move));
	}

	public String getDpeArene() {
		return round(FightUtils.computePowerPerEnergyPvE(this.move, this.owner));
	}

	public String getDptCombat() {
		return round(FightUtils.computePowerPerTurn(this.move, this.owner));
	}

	public String getEptCombat() {
		return round(FightUtils.computeEnergyPerTurn(this.move));
	}

	public String getDpeCombat() {
		return round(FightUtils.computePowerPerEnergyPvP(this.move, this.owner));
	}

	private static final DecimalFormat dfZero = new DecimalFormat("#.##");

	private static String round(double input) {
		return dfZero.format(Math.abs(input));
	}

	public boolean isStab() {
		return FightUtils.isSTAB(this.move, this.owner);
	}
}