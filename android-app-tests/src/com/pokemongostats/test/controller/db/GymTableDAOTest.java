package com.pokemongostats.test.controller.db;

import java.util.Date;
import java.util.List;

import com.pokemongostats.controller.db.gym.GymTableDAO;
import com.pokemongostats.model.Gym;
import com.pokemongostats.model.GymDescription;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.Team;

import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

public class GymTableDAOTest extends TableDAOTest {

	private GymTableDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		dao = new GymTableDAO(context);
	}

	@Override
	public void tearDown() throws Exception {
		dao.close();
		super.tearDown();
	}

	@Override
	public void testInsertOrReplace() {
		GymDescription desc = null;
		int level = 0;
		Date date = null;
		Team team = null;
		List<Pokemon> list = null;
		Gym gym = new Gym(desc, level, date, team, list);
		dao.insertOrReplace(gym);
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
		List<Gym> list = dao.selectAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

}