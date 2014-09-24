package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class PrecedesTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Precedes p1 = new Precedes(new Transition("tIn"), new Transition("tOut"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		Precedes p1 = new Precedes(new Transition("t0"), new Transition("t1"));
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
		Precedes p1 = new Precedes(new Transition("t1"), new Transition("td"));
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
		Precedes p1 = new Precedes(new Transition("t1"), new Transition("t3"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PlacePredicate ap1 = new PlacePredicate("pIn_black", Relation.EQUALS, 1);
		PlacePredicate ap2 = new PlacePredicate("pOut_black", Relation.EQUALS, 1);
		Precedes p1 = new Precedes(ap1, ap2);
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
		Precedes p1 = new Precedes(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}

}
