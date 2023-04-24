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

	public String getInfosPve(){
		// DPS
		String d = round(FightUtils.computePowerPerSecond(move, owner));
		String e;
		if(move.getMoveType() == Move.MoveType.QUICK){
			// EPS
			e = round(FightUtils.computeEnergyPerSecond(move));
		} else {
			// DPE
			e = round(FightUtils.computePowerPerEnergyPvE(move, owner));
		}
		return d + "  |  " + e;
	}

	public String getInfosPvp(){
		// DPT
		String d = round(FightUtils.computePowerPerTurn(move, owner));
		String e;
		if(move.getMoveType() == Move.MoveType.QUICK){
			// EPT
			e = round(FightUtils.computeEnergyPerTurn(move));
		} else {
			// DPE
			e = round(FightUtils.computePowerPerEnergyPvP(move, owner));
		}
		return d + "  |  " + e;
	}

	private static final DecimalFormat dfZero = new DecimalFormat("#.##");

	private static String round(double input){
		return dfZero.format(Math.abs(input));
	}

	public boolean isStab(){
		return FightUtils.isSTAB(move, owner);
	}
}