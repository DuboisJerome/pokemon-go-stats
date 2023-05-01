package com.pokemongostats.controller.external;

import com.pokemongostats.model.bean.UpdateStatus;

public interface IExternalDataPokedex<I> {

	void readDatas(I in, UpdateStatus updateStatus) throws ParserException;

}