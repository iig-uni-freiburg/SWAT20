package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Transition;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsTo;

public class LeadsToTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		LeadsTo p1 = new LeadsTo(new Transition("tIn"), new Transition("tOut"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		LeadsTo p1 = new LeadsTo(new Transition("t0"), new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		LeadsTo p1 = new LeadsTo(new Transition("t1"), new Transition("td"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.createUsageConflictIFNet();
		LeadsTo p1 = new LeadsTo(new Transition("t1"), new Transition("t3"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PlacePredicate ap1 = new PlacePredicate("pIn_black", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("pOut_black", Relation.EQUALS, 1);
		LeadsTo p1 = new LeadsTo(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PlacePredicate ap1 = new PlacePredicate("p0_green", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("p2_green", Relation.EQUALS, 1);
		LeadsTo p1 = new LeadsTo(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
