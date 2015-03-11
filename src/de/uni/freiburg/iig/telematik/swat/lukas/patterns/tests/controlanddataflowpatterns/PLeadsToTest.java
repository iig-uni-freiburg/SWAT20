package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StatePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.PLeadsTo;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class PLeadsToTest {

	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		PLeadsTo p1 = new PLeadsTo(new Activity("tIn"), new Activity("tOut"));
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
		TransitionToIDMapper.createMap(ifnet);
		PLeadsTo p1 = new PLeadsTo(new Activity("t0"), new Activity("t1"));
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
		TransitionToIDMapper.createMap(ifnet);
		PLeadsTo p1 = new PLeadsTo(new Activity("t1"), new Activity("td"));
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
		TransitionToIDMapper.createMap(ifnet);
		PLeadsTo p1 = new PLeadsTo(new Activity("t1"), new Activity("t3"));
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
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("pIn_black", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("pOut_black", Relation.EQUALS, 1);
		PLeadsTo p1 = new PLeadsTo(ap1, ap2);
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
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("p0_green", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("p2_green", Relation.EQUALS, 1);
		PLeadsTo p1 = new PLeadsTo(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test6() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		PLeadsTo p1 = new PLeadsTo(new Activity("t0"), new Activity("tOut"));
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}


}
