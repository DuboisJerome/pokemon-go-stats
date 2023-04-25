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

	public PkmnMoveComplet(){

	}
	public PkmnMoveComplet(PkmnMove pm) {
		setPokedexNum(pm.getPokedexNum());
		setForm(pm.getForm());
		setMoveId(pm.getMoveId());
		setElite(pm.isElite());
	}

	public String getName(){
		return move.getName() + (isElite() ? "*" : "");
	}

	public String getDpsArene(){
		return round(FightUtils.computePowerPerSecond(move, owner));
	}

	public String getEpsArene(){
		return round(FightUtils.computeEnergyPerSecond(move));
	}

	public String getDpeArene(){
		return round(FightUtils.computePowerPerEnergyPvE(move, owner));
	}

	public String getDptCombat(){
		return round(FightUtils.computePowerPerTurn(move, owner));
	}
	public String getEptCombat(){
		return  round(FightUtils.computeEnergyPerTurn(move));
	}
	public String getDpeCombat(){
		return round(FightUtils.computePowerPerEnergyPvP(move, owner));
	}

	private static final DecimalFormat dfZero = new DecimalFormat("#.##");

	private static String round(double input){
		return dfZero.format(Math.abs(input));
	}

	public boolean isStab(){
		return FightUtils.isSTAB(move, owner);
	}
}