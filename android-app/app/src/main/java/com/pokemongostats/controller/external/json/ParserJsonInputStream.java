package com.pokemongostats.controller.external.json;

import com.pokemongostats.controller.external.json.splitter.AbstractSplitterJson;
import com.pokemongostats.controller.external.json.splitter.SplitterJsonInputStream;

import java.io.InputStream;

public class ParserJsonInputStream extends AbstractParserJson<InputStream> {

	@Override
	protected AbstractSplitterJson<InputStream> getSplitter() {
		return new SplitterJsonInputStream();
	}
}