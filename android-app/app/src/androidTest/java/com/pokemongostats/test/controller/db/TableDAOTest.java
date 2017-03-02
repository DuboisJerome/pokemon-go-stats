package com.pokemongostats.test.controller.db;

import android.test.AndroidTestCase;

public abstract class TableDAOTest extends AndroidTestCase {

	public abstract void testInsertOrReplace();
	public abstract void testRemove();
	public abstract void testSelect();
	public abstract void testSelectAll();
}