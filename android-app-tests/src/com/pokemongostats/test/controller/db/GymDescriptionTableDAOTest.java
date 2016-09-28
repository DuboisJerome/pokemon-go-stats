package com.pokemongostats.test.controller.db;

import java.util.List;

import com.pokemongostats.controller.db.gym.GymDescriptionTableDAO;
import com.pokemongostats.model.GymDescription;

import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

public class GymDescriptionTableDAOTest extends TableDAOTest {

	private GymDescriptionTableDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		dao = new GymDescriptionTableDAO(context);
	}

	@Override
	public void tearDown() throws Exception {
		dao.close();
		super.tearDown();
	}

	@Override
	public void testInsertOrReplace() {
		GymDescription desc = null;
		dao.insertOrReplace(desc);
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
		List<GymDescription> list = dao.selectAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

}