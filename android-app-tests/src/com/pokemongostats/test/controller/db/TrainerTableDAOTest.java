package com.pokemongostats.test.controller.db;

import java.util.List;

import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.Team;
import com.pokemongostats.model.Trainer;

import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

public class TrainerTableDAOTest extends TableDAOTest {

	private TrainerTableDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
		dao = new TrainerTableDAO(context);
	}

	@Override
	public void tearDown() throws Exception {
		dao.close();
		super.tearDown();
	}

	@Override
	public void testInsertOrReplace() {
		List<Long> ids = dao.insertOrReplace(new Trainer("Zapagon", 22, Team.INSCTINCT));
		Assert.assertNotNull(ids);
		Assert.assertTrue(ids.size() > 0);
		Assert.assertFalse(TrainerTableDAO.NOT_INSERTED == ids.get(0));

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
		List<Trainer> list = dao.selectAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

}