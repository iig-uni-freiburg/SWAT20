package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.controlanddataflowpatterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.TransitionToIDMapper;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.StatePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Relation;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.Activity;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CoExists;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.IFNetTestUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests.TestUtils;

public class CoExistsTest {
	
	@Test
	public void test0() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		CoExists p1 = new CoExists(new Activity("tIn"), new Activity("tOut"), outputPlace);
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
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		CoExists p1 = new CoExists(new Activity("tOut"), new Activity("tIn"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
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
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		CoExists p1 = new CoExists(new Activity("t0"), new Activity("t1"), outputPlace);
		TestUtils tu = new TestUtils(ifnet, p1);
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
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		CoExists p1 = new CoExists(new Activity("t1"), new Activity("td"), outputPlace);
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
		TransitionToIDMapper.createMap(ifnet);
		Collection<IFNetPlace> outputPlaces = ifnet.getDrainPlaces();
		AbstractPlace<?,?> outputPlace = outputPlaces.iterator().next();
		CoExists p1 = new CoExists(new Activity("td"), new Activity("t1"), outputPlace);
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
		StatePredicate ap1 = new StatePredicate("pIn_black", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("pOut_black", Relation.EQUALS, 1);
		CoExists p1 = new CoExists(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertTrue(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void test6() {
		IFNet ifnet = IFNetTestUtils.create6PlaceIFNet();
		TransitionToIDMapper.createMap(ifnet);
		StatePredicate ap1 = new StatePredicate("p0_green", Relation.EQUALS, 1);
		StatePredicate ap2 = new StatePredicate("p2_green", Relation.EQUALS, 1);
		CoExists p1 = new CoExists(ap1, ap2);
		TestUtils tu = new TestUtils(ifnet, p1);
		try {
			assertFalse(tu.isPropertySatisfied());
		} catch (Exception e) {
			fail();
		}
	}
}
