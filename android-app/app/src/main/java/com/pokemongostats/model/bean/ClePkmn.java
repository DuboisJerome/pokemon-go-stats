package com.pokemongostats.model.bean;

import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.comparators.CheckNullComparator;

import java.util.Objects;

public class ClePkmn implements Comparable<ClePkmn> {
	private final long id;
	private final String form;

	public ClePkmn(PkmnDesc p) {
		this(p.getPokedexNum(), p.getForm());
	}

	public ClePkmn(PkmnMove pm) {
		this(pm.getPokedexNum(), pm.getForm());
	}

	public ClePkmn(long pId, String pForm) {
		this.id = pId;
		this.form = pForm;
	}

	public static ClePkmn getCleBaseEvol(Evolution ev) {
		return new ClePkmn(ev.getBasePkmnId(), ev.getBasePkmnForm());
	}

	public static ClePkmn getCleEvolEvol(Evolution ev) {
		return new ClePkmn(ev.getEvolutionId(), ev.getEvolutionForm());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClePkmn clePkmn)) return false;

		if (this.id != clePkmn.id) return false;
		return Objects.equals(this.form, clePkmn.form);
	}

	@Override
	public int hashCode() {
		int result = (int) (this.id ^ (this.id >>> 32));
		result = 31 * result + (this.form != null ? this.form.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(ClePkmn that) {
		int res = Long.compare(this.id, that.id);
		if (res == 0) {
			Integer nullParams = CheckNullComparator.checkNull(this.form, that.form);
			if (nullParams == null) {
				res = this.form.compareTo(that.form);
			} else {
				res = nullParams;
			}
		}
		return res;

	}

}