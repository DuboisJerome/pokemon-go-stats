package com.pokemongostats.test.controller.db;

import java.util.List;

import com.pokemongostats.controller.db.pokemon.PokedexTableDAO;
import com.pokemongostats.model.PokemonDescription;

import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

public class PokedexTableDAOTest extends TableDAOTest {

	private PokedexTableDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		dao = new PokedexTableDAO(context);
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
		Assert.assertEquals(null, dao.select(null));
	}

	@Override
	public void testSelectAll() {
		List<PokemonDescription> list = dao.selectAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

}