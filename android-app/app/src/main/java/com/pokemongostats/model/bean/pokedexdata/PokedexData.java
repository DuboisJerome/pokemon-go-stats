package com.pokemongostats.model.bean.pokedexdata;

import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.MoveI18N;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.bean.bdd.PkmnDescI18N;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokedexData implements Serializable {
	private Data<PkmnDesc> dataPkmn = new Data<>();
	private Data<PkmnDescI18N> dataPkmni18n = new Data<>();
	private Data<Move> dataMove = new Data<>();
	private Data<MoveI18N> dataMovei18n = new Data<>();
	private Data<PkmnMove> dataPkmnMove = new Data<>();
	private Data<Evolution> dataEvol = new Data<>();

	@Getter
	public static class Data<T> implements Serializable {
		private final List<T> lstToCreate = new ArrayList<>();
		private final Map<T, T> lstToUpdate = new HashMap<>();
		private final List<T> lstToDelete = new ArrayList<>();

		public void addCreate(T t){
			this.lstToCreate.add(t);
		}

		public void addUpdate(T oldT, T newT){
			this.lstToUpdate.put(oldT, newT);
		}

		public void addDelete(T t){
			this.lstToDelete.add(t);
		}
	}
}