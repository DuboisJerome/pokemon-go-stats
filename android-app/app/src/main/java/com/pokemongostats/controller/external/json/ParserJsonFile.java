package com.pokemongostats.controller.external.json;

import com.pokemongostats.controller.external.json.splitter.AbstractSplitterJson;
import com.pokemongostats.controller.external.json.splitter.SplitterJsonFile;

import java.io.File;

public class ParserJsonFile extends AbstractParserJson<File> {

	@Override
	protected AbstractSplitterJson<File> getSplitter() {
		return new SplitterJsonFile();
	}
}