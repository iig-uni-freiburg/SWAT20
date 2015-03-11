package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainPrecedes;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PrecedesChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class ChainPrecedesTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("tIn"), new Activity("t0"),
				new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("td"), new Activity("t1"),
				new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("tIn"), new Activity("t0"),
				new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("tIn"), new Activity("td"),
				new Activity("tOut"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("t1"), new Activity("t3"),
				new Activity("t4"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void test5() {
		// sequence t1, t1 not possible therefore the property should be satisfied
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		PrecedesChain p = new PrecedesChain(new Activity("td"), new Activity("t1"),
				new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		PrecedesChain p = new PrecedesChain(new Activity("tIn"), new Activity("t0"),
				new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		PrecedesChain p = new PrecedesChain(new Activity("tIn"), new Activity("td"),
				new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		PrecedesChain p = new PrecedesChain(new Activity("t0"), new Activity("td"),
				new Activity("t1"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test9() {
		IFNet ifnet = IFNetTestUtils.createExtendedUsageConflictIFNet();
		TransitionToIDMapper.createMap(ifnet);
		ChainPrecedes p = new ChainPrecedes(new Activity("t1"), new Activity("t2"),
				new Activity("t4"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}


}
