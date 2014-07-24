package de.uni.freiburg.iig.telematik.swat.lukas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class ChainPrecedesTest {
	/*
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainPrecedes p = new ChainPrecedes(new Transition("tIn"), new Transition("t0"),
				new Transition("tOut"));
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
		ChainPrecedes p = new ChainPrecedes(new Transition("td"), new Transition("t1"),
				new Transition("t1"));
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
		ChainPrecedes p = new ChainPrecedes(new Transition("tIn"), new Transition("t0"),
				new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		ChainPrecedes p = new ChainPrecedes(new Transition("tIn"), new Transition("td"),
				new Transition("tOut"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		ChainPrecedes p = new ChainPrecedes(new Transition("t1"), new Transition("t3"),
				new Transition("t4"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void test5() {
		// sequence t1, t1 not possible therefore the property should be satisfied
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PrecedesChain p = new PrecedesChain(new Transition("td"), new Transition("t1"),
				new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test6() {
		// sequence t0, t1 not possible therefore the property should be satisfied
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PrecedesChain p = new PrecedesChain(new Transition("tIn"), new Transition("t0"),
				new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test7() {
		// sequence tIn, td is possible and precedes tOut therefore the property should be satisfied
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PrecedesChain p = new PrecedesChain(new Transition("tIn"), new Transition("td"),
				new Transition("tOut"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test8() {
		// sequence tIn, t0 is possible and precedes tOut therefore the property should be satisfied
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		PrecedesChain p = new PrecedesChain(new Transition("t0"), new Transition("td"),
				new Transition("t1"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}*/
	
	@Test
	public void test9() {
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		ChainPrecedes p = new ChainPrecedes(new Transition("t1"), new Transition("t2"),
				new Transition("t4"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}


}
