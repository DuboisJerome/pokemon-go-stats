package com.pokemongostats.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pokemongostats.databinding.CardViewTypeBinding;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.viewholder.LstTypeViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.commons.generique.ui.AbstractGeneriqueAdapter;

/**
 * @author Zapagon
 */
public class TypeRecyclerViewAdapter extends AbstractGeneriqueAdapter<Type,LstTypeViewHolder> {

	public TypeRecyclerViewAdapter(boolean isVideParDefaut) {
		super(Arrays.asList(Type.values()));
		// Vide par défaut
		if (isVideParDefaut) {
			this.currentList = new ArrayList<>();
		}
	}

	public void filter(Collection<Type> colTypeInclu) {
		String str = colTypeInclu.stream().map(Type::name).collect(Collectors.joining(","));
		if (str.isEmpty()) str = "NO_TYPE";
		super.filter(str);
	}

	@Override
	protected Predicate<Type> getPredicateFilter(String s) {
		if ("NO_TYPE".equals(s)) return t -> false;
		Set<Type> lstType = Arrays.stream(s.split(",")).map(Type::valueOf).collect(Collectors.toSet());
		return lstType::contains;
	}

	@NonNull
	@Override
	public LstTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardViewTypeBinding binding = CardViewTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new LstTypeViewHolder(binding);
	}
}