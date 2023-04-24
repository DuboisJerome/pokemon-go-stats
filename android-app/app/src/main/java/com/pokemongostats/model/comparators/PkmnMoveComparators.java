package com.pokemongostats.model.comparators;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.PkmnMoveComplet;

import java.util.Comparator;

public class PkmnMoveComparators {

	public static Comparator<PkmnMoveComplet> getComparatorByPps() {
		return (pm1,pm2) -> {
			Integer nullParams = CheckNullComparator.checkNull(pm1, pm2);
			if (nullParams != null) {
				return nullParams;
			}
			nullParams = CheckNullComparator.checkNull(pm1.getMove(), pm2.getMove());
			if (nullParams != null) {
				return nullParams;
			}
			return Double.compare(FightUtils.computePowerPerSecond(pm1.getMove(), pm1.getOwner()), FightUtils.computePowerPerSecond(pm2.getMove(), pm2.getOwner()));
		};
	}
}