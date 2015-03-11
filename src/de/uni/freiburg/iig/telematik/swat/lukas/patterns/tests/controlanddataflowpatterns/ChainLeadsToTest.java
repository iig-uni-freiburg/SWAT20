package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ChainLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.LeadsToChain;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class ChainLeadsToTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		ChainLeadsTo p = new ChainLeadsTo(new Activity("tIn"), new Activity("t0"),
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
		ChainLeadsTo p = new ChainLeadsTo(new Activity("td"), new Activity("t1"),
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
		ChainLeadsTo p = new ChainLeadsTo(new Activity("tIn"), new Activity("t0"),
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
		ChainLeadsTo p = new ChainLeadsTo(new Activity("tIn"), new Activity("td"),
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
		// t0 and td don't appear in a sequence, so the property should be full filled
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		ChainLeadsTo p = new ChainLeadsTo(new Activity("t0"), new Activity("td"),
				new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		ChainLeadsTo p = new ChainLeadsTo(new Activity("td"), new Activity("t1"),
				new Activity("tOut"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	public void test6() {
		// the sequence td, t1 does not always follow tIn
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		LeadsToChain p = new LeadsToChain(new Activity("tIn"), new Activity("td"),
				new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		LeadsToChain p = new LeadsToChain(new Activity("tIn"), new Activity("td"),
				new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		LeadsToChain p = new LeadsToChain(new Activity("t0"), new Activity("tOut"),
				new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		LeadsToChain p = new LeadsToChain(new Activity("t1"), new Activity("t3"),
				new Activity("t4"));
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
		TransitionToIDMapper.createMap(ifnet);
		LeadsToChain p = new LeadsToChain(new Activity("t2"), new Activity("t3"),
				new Activity("t4"));
		TestUtils tu = new TestUtils(ifnet, p);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
