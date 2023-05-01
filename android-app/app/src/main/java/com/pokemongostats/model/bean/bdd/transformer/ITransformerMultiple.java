package com.pokemongostats.model.bean.bdd.transformer;

import java.util.List;

/**
 * Prend un input en entrée et ressort une liste de output
 * équivalent de Function<I,List<O>> mais avec exception
 * @param <I> Class input
 * @param <O> Class output
 */
@FunctionalInterface
public interface ITransformerMultiple<I, O> extends ITransformerSimple<I, List<O>> {

	static <I, O> ITransformerMultiple<I, O> fromSimple(ITransformerSimple<I, O> transformerSimple) {
		return input -> {
			O output = transformerSimple.transform(input);
			return List.of(output);
		};
	}
}