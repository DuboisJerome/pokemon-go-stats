package com.pokemongostats.controller.external.json.splitter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class SplitterJsonInputStream extends AbstractSplitterJson<InputStream> {

	@Override
	protected Reader toReader(InputStream in) {
		return new InputStreamReader(in, StandardCharsets.UTF_8);
	}
}