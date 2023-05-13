package com.pokemongostats.controller.external;

import com.pokemongostats.controller.external.transformer.ITransformerMultiple;
import com.pokemongostats.controller.external.transformer.ITransformerSimple;
import com.pokemongostats.model.bean.UpdateStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractSplitter<I, T> {
	public static final String GRP_MOVE_COMBAT = "MOVE_COMBAT";
	public static final String GRP_MOVE_ARENE = "MOVE_ARENE";
	public static final String GRP_FORMS = "FORMS";
	public static final String GRP_PKMN = "PKMN";
	public static final String UNUSED = "UNUSED";

	@Getter
	protected List<Grp<T>> lstGrp = new ArrayList<>();

	@Getter
	@Setter
	public static class Grp<T> {
		String name;
		Predicate<T> funcFilter = t -> false;
		final List<T> lst = new ArrayList<>();

		public boolean filter(T in) {
			return this.funcFilter.test(in);
		}

		public boolean addItem(T item) {
			return this.lst.add(item);
		}
	}

	/**
	 * Méthode d'entrée de la classe
	 *
	 * @param input donnée d'entrée InputStream, fichier ou quoi que ce soit
	 * @throws ParserException exception de sortie
	 */
	public abstract void split(I input, UpdateStatus us) throws ParserException;

	protected abstract List<Grp<T>> initGrps();

	public <E> List<E> getLstElemTransform(String nomGrp, ITransformerMultiple<T,E> transform) throws ParserException {
		List<T> lstFromGrp = this.lstGrp
				.stream()
				.filter(grp -> grp.name.equals(nomGrp))
				.flatMap(grp -> grp.lst.stream()).collect(Collectors.toList());
		List<E> lstResults = new ArrayList<>();
		for (T t : lstFromGrp) {
			List<E> subLstElem = transform.transform(t);
			lstResults.addAll(subLstElem);
		}
		return lstResults;
	}

	public <E> List<E> getLstElemTransform(String nomGrp, ITransformerSimple<T,E> transform) throws ParserException {
		return getLstElemTransform(nomGrp, ITransformerMultiple.fromSimple(transform));
	}
}