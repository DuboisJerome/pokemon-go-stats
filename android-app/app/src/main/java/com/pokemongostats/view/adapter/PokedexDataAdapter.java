package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.pokedexdata.PokedexDataFactory;
import com.pokemongostats.databinding.CardViewIncomingDataEvolBinding;
import com.pokemongostats.databinding.CardViewIncomingDataPkmnDescBinding;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.MoveI18N;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnDescI18N;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.view.viewholder.pokedexdata.AbstractLstPokedexDataViewHolder;
import com.pokemongostats.view.viewholder.pokedexdata.LstPokedexDataDefaultViewHolder;
import com.pokemongostats.view.viewholder.pokedexdata.LstPokedexDataEvolViewHolder;
import com.pokemongostats.view.viewholder.pokedexdata.LstPokedexDataPkmnDescViewHolder;

import java.util.function.Predicate;

import fr.commons.generique.model.db.IObjetBdd;
import fr.commons.generique.ui.AbstractGeneriqueAdapter;

/**
 * @author Zapagon
 */
public class PokedexDataAdapter<T extends IObjetBdd> extends AbstractGeneriqueAdapter<IPokedexDataItem<T>,AbstractLstPokedexDataViewHolder<T>> {

	private enum TypeViewHolder {
		INCONNU,PKMN,PKMNI18N,MOVE,MOVEI18N,EVOL,PKMNMOVE
	}

	@Override
	protected Predicate<IPokedexDataItem<T>> getPredicateFilter(String s) {
		return str -> true;
	}
	@Override
	public int getItemViewType(int position) {
		IObjetBdd o = getItemAt(position).getData();
		if(o instanceof PkmnDesc){
			return TypeViewHolder.PKMN.ordinal();
		} else if(o instanceof PkmnDescI18N){
			return TypeViewHolder.PKMNI18N.ordinal();
		} else if(o instanceof Move){
			return TypeViewHolder.MOVE.ordinal();
		} else if(o instanceof MoveI18N){
			return TypeViewHolder.MOVEI18N.ordinal();
		} else if(o instanceof Evolution){
			return TypeViewHolder.EVOL.ordinal();
		} else if(o instanceof PkmnMove){
			return TypeViewHolder.PKMNMOVE.ordinal();
		}
		return TypeViewHolder.INCONNU.ordinal();
	}

	@NonNull
	@Override
	public AbstractLstPokedexDataViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		TypeViewHolder type = TypeViewHolder.values()[viewType];
		switch (type) {
			case PKMNI18N:
				break;
			case MOVE:
				break;
			case MOVEI18N:
				break;
			case EVOL:
				CardViewIncomingDataEvolBinding bindingEvol = CardViewIncomingDataEvolBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
				return (AbstractLstPokedexDataViewHolder<T>) new LstPokedexDataEvolViewHolder(bindingEvol);
			case PKMNMOVE:
				break;
			case PKMN:
				CardViewIncomingDataPkmnDescBinding bindingPkmn = CardViewIncomingDataPkmnDescBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
				return (AbstractLstPokedexDataViewHolder<T>) new LstPokedexDataPkmnDescViewHolder(bindingPkmn);
			case INCONNU:
				default:
				break;
		}
		return new LstPokedexDataDefaultViewHolder<>(parent);
	}

}