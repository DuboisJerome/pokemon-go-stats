package com.pokemongostats.controller.external;

import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.model.bean.UpdateStatus;

public interface IExternalDataPokedex<I> {

	PokedexData readDatas(I in, UpdateStatus updateStatus) throws ParserException;

}