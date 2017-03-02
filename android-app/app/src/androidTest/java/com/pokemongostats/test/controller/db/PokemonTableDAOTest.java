package com.pokemongostats.test.controller.db;

import java.util.List;

import com.pokemongostats.controller.db.pokemon.PokemonTableDAO;
import com.pokemongostats.model.Pokemon;

import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

public class PokemonTableDAOTest extends TableDAOTest {

	private PokemonTableDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		dao = new PokemonTableDAO(context);
	}

	@Override
	public void tearDown() throws Exception {
		dao.close();
		super.tearDown();
	}

	@Override
	public void testInsertOrReplace() {
	}

	@Override
	public void testRemove() {
		dao.remove(new Long[10]);
	}

	@Override
	public void testSelect() {
	}

	@Override
	public void testSelectAll() {
		List<Pokemon> list = dao.selectAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

}