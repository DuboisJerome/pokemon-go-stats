package com.pokemongostats.controller.external.transformer;

import com.pokemongostats.controller.external.ParserException;

/**
 * Prend un input en entrée et ressort une liste de output
 * équivalent de Function<I,List<O>> mais avec exception
 * @param <I> Class input
 * @param <O> Class output
 */
@FunctionalInterface
public interface ITransformerSimple<I, O> {
	O transform(I input) throws ParserException;
}