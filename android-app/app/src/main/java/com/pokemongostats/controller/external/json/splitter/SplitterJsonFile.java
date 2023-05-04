package com.pokemongostats.controller.external.json.splitter;

import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.model.bean.UpdateStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class SplitterJsonFile extends AbstractSplitterJson<File> {

	public void split(File inputFileJson, UpdateStatus us) throws ParserException {
		Log.info("-- Split le fichier "+ inputFileJson.getName());
		super.split(inputFileJson, us);
	}

	@Override
	protected Reader toReader(File f) throws ParserException {
		try {
			InputStream in = new FileInputStream(f.getAbsolutePath());
			return new InputStreamReader(in, StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			throw new ParserException(e);
		}
	}
}