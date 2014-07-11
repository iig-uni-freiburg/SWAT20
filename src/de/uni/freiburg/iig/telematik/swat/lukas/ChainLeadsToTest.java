package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class ChainLeadsToTest {
	/*
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("tIn"), new Transition("t0"),
				new Transition("tOut"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test1() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("td"), new Transition("t1"),
				new Transition("t1"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test2() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("tIn"), new Transition("t0"),
				new Transition("t1"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("tIn"), new Transition("td"),
				new Transition("tOut"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		// t0 and td don't appear in a sequence, so the property should be full filled
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("t0"), new Transition("td"),
				new Transition("tOut"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		// sequence td, t1 appear in a sequence, but the sequence is never followed by tOut
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("td"), new Transition("t1"),
				new Transition("tOut"), true);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}*/
	
	
	@Test
	public void test6() {
		// the sequence td, t1 does not always follow tIn
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("tIn"), new Transition("td"),
				new Transition("t1"), false);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test7() {
		// the sequence td, tOut is not possible
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("tIn"), new Transition("td"),
				new Transition("tOut"), false);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test8() {
		// the sequence tOut, tOut is not possible
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("t0"), new Transition("tOut"),
				new Transition("tOut"), false);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test9() {
		// the sequence tOut, tOut is not possible
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("t1"), new Transition("t3"),
				new Transition("t4"), false);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test10() {
		// the sequence tOut, tOut is not possible
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		ChainLeadsTo p = new ChainLeadsTo(new Transition("t2"), new Transition("t3"),
				new Transition("t4"), false);
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
